package tn.pi.cabinetmedicalproject.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tn.pi.cabinetmedicalproject.model.Patient;
import tn.pi.cabinetmedicalproject.model.User;
import tn.pi.cabinetmedicalproject.repository.UserRepository;
import tn.pi.cabinetmedicalproject.service.PatientService;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserRepository userRepository; // Repository pour gérer les utilisateurs

    @GetMapping
    public String getProfile(Model model, Principal principal) {
        // Récupérer l'email de l'utilisateur connecté
        String email = principal.getName();

        // Trouver l'utilisateur dans la base de données
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Récupérer les informations du patient via le userId
        Patient patient = patientService.findByUserId(user.getId());
        model.addAttribute("patient", patient);

        return "profile";
    }


    @GetMapping("/profile")
    public String profilePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // L'utilisateur est authentifié
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                // Ajout du nom d'utilisateur au modèle pour l'affichage
                model.addAttribute("username", username);
            }
        } else {
            // L'utilisateur n'est pas authentifié
            model.addAttribute("message", "You need to log in!");
        }
        return "profile"; // La vue profile.html

    }}
