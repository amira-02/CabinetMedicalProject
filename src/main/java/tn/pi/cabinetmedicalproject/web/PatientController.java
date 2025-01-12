package tn.pi.cabinetmedicalproject.web;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tn.pi.cabinetmedicalproject.model.Patient;
import tn.pi.cabinetmedicalproject.repository.PatientRepository;
import tn.pi.cabinetmedicalproject.repository.UserRepository;

@Controller
@Slf4j
public class PatientController {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository; // Repository pour User

    public PatientController(PatientRepository patientRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/index")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "4") int size,
                        @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Page<Patient> patients = patientRepository.findByNameContains(keyword, PageRequest.of(page, size));
        model.addAttribute("patients", patients.getContent());
        model.addAttribute("pages", new int[patients.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "patients";
    }

    @GetMapping("/delete")
    public String delete(Long id, String keyword, int page) {
        patientRepository.deleteById(id);
        return "redirect:/index?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/formPatients")
    public String formPatients(Model model) {
        model.addAttribute("patient", new Patient());
        return "formPatients";
    }

    @PostMapping("/savePatient")
    public String savePatient(Model model, @Valid Patient patient, BindingResult bindingResult) {
        // Vérifier si l'email existe déjà
        if (userRepository.existsByEmail(patient.getEmail())) {
            bindingResult.rejectValue("email", "error.patient", "Cet email existe déjà.");
            return "formPatients"; // Renvoyer au formulaire avec un message d'erreur
        }

        if (bindingResult.hasErrors()) {
            return "formPatients";
        }

        log.info("Le patient envoyé est : " + patient);
        patientRepository.save(patient);
        model.addAttribute("patient", new Patient());
        return "redirect:/index";
    }

    @GetMapping("/EditPatients")
    public String EditPatients(Model model, @RequestParam(name = "id") Long id) {
        Patient patient = patientRepository.findById(id).get();
        model.addAttribute("patient", patient);
        return "EditPatients";
    }

    @GetMapping("/ConsultationPatient")
    public String ConsultationPatient(Model model, @RequestParam(name = "id") Long id) {
        Patient patient = patientRepository.findById(id).get();
        model.addAttribute("patient", patient);
        return "ConsultationPatient";
    }
}
