package tn.pi.cabinetmedicalproject.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Méthode pour afficher la page d'accueil des médecins.
     * @return Le nom de la vue "doctorhome.html".
     */
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
    public String patientHome() {
        return "Home"; // Assurez-vous que le fichier Home.html existe dans templates/
    }

    // Uncomment and modify this method if you need a default home page
    /*
    @GetMapping("/")
    public String home() {
        // Retourner la vue avec le formulaire d'upload
        return "upload";  // Assurez-vous que ce fichier HTML est dans le bon répertoire (par ex. src/main/resources/templates)
    }
    */
}