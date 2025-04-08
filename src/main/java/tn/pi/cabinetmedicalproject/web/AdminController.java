package tn.pi.cabinetmedicalproject.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tn.pi.cabinetmedicalproject.model.Doctor;
import tn.pi.cabinetmedicalproject.model.Patient;
import tn.pi.cabinetmedicalproject.service.DoctorService;
import tn.pi.cabinetmedicalproject.service.PatientService;

import java.util.List;

@Controller
public class AdminController {

    private final DoctorService doctorService;


    @Autowired
    public AdminController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin"; // Cela correspond au fichier `admin.html` dans le dossier `templates`.
    }

    @GetMapping("/DoctorsListAdmin")
    public String getDoctorsList(Model model) {
        // Appel de la méthode getAllDoctors() de l'instance de DoctorService
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "DoctorsList"; // Cela correspond au fichier `DoctorsList.html` dans le dossier `templates`
    }
}
