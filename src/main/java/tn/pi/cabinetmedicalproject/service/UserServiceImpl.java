package tn.pi.cabinetmedicalproject.service;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import tn.pi.cabinetmedicalproject.model.User;
import tn.pi.cabinetmedicalproject.repository.UserRepository;
import tn.pi.cabinetmedicalproject.web.dto.UserRegistrationDto;
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User save(UserRegistrationDto registrationDto) {
        // Utilisez directement 'name' du DTO
        User user = new User(
                registrationDto.getName(),  // Utilisez le 'name' complet depuis le DTO
                registrationDto.getEmail(),  // L'email
                passwordEncoder.encode(registrationDto.getPassword()),  // Le mot de passe crypté
                registrationDto.getRole()  // Le rôle
        );

        // Sauvegarder l'utilisateur dans la base de données
        return userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        System.out.println("User found: " + user);
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRole())
        );
    }
    @Override
    public User getCurrentUser() {
        // Récupérer l'authentification de l'utilisateur courant
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();  // Retourner l'utilisateur authentifié
        }
        return null;  // Si aucun utilisateur n'est connecté, retourner null
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(String role) {
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }

}
