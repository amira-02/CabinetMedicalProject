package tn.pi.cabinetmedicalproject.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/doctorhome")
    public String doctorHome() {
        return "doctorhome"; // Assurez-vous que le fichier doctorhome.html existe dans templates/
    }

    /**
     * Méthode pour afficher la page d'accueil des pharmacies.
     * @return Le nom de la vue "pharmacyhome.html".
     */
    @GetMapping("/pharmacyhome")
    public String pharmacyHome() {
        return "pharmacyhome"; // Assurez-vous que le fichier pharmacyhome.html existe dans templates/
    }

    /**
     * Méthode pour afficher la page d'accueil générale des patients ou visiteurs.
     * @return Le nom de la vue "Home.html".
     */
    @GetMapping("/home")
    public String homePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                model.addAttribute("username", username);
            }
        } else {
            model.addAttribute("message", "You need to log in!");
        }
        return "home"; // La vue home.html
    }
}
