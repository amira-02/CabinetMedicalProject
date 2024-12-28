package tn.pi.cabinetmedicalproject.web;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class 	AboutUsController {

    // Cette méthode est liée à la route '/doctors' et retourne la vue 'doctorsList.html'
    @GetMapping("/AboutUs")
    public String showDoctorsList() {
        // Vous pouvez ajouter ici le code pour récupérer la liste des médecins à partir de la base de données
        return "AboutUs";  // Cette vue sera affichée (assurez-vous que 'doctorsList.html' existe dans le dossier 'resources/templates')
    }
}
