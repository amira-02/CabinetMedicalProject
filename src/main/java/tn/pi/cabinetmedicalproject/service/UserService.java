package tn.pi.cabinetmedicalproject.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tn.pi.cabinetmedicalproject.model.User;
import tn.pi.cabinetmedicalproject.web.dto.UserRegistrationDto;

public interface UserService extends UserDetailsService{
    User save(User user);

    User save(UserRegistrationDto registrationDto);

    User getCurrentUser();  // Ajoutez cette méthode pour récupérer l'utilisateur actuellement connecté


    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;


}
