package tn.pi.cabinetmedicalproject.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/doctorhome")
    public String doctorHome() {
        return "patients"; // Retourne la vue doctorhome.html
    }

    @GetMapping("/pharmacyhome")
    public String pharmacyHome() {
        return "pharmacyhome"; // Retourne la vue pharmacyhome.html
    }

    @GetMapping("/Home")
    public String patientHome() {
        return "Home"; // Retourne la vue patienthome.html
    }
}
