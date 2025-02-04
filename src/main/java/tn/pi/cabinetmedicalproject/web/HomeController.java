package tn.pi.cabinetmedicalproject.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tn.pi.cabinetmedicalproject.model.User;

@Controller
public class HomeController {


    @GetMapping("/doctorhome")
    public String doctorHome() {
        return "doctorhome"; // Assurez-vous que le fichier doctorhome.html existe dans templates/
    }


//    @GetMapping("/pharmacyhome")
//    public String pharmacyHome() {
//        return "pharmacyhome"; // Assurez-vous que le fichier pharmacyhome.html existe dans templates/
//    }


//    @GetMapping("/home")
//    public String patientHome() {
//        return "Home"; // Assurez-vous que le fichier Home.html existe dans templates/
//    }


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
