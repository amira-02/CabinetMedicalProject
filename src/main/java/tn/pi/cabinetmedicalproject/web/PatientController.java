package tn.pi.cabinetmedicalproject.web;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tn.pi.cabinetmedicalproject.model.Consultation;
import tn.pi.cabinetmedicalproject.model.Doctor;
import tn.pi.cabinetmedicalproject.model.Patient;
import tn.pi.cabinetmedicalproject.repository.PatientRepository;
import tn.pi.cabinetmedicalproject.repository.UserRepository;
import tn.pi.cabinetmedicalproject.service.ConsultationService;
import tn.pi.cabinetmedicalproject.service.DoctorService;

import java.security.Principal;
import java.util.List;

@Controller
@Slf4j
public class PatientController {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository; // Repository pour User
    @Autowired
    private ConsultationService consultationService; // Injection correcte
    @Autowired
    private DoctorService doctorService;
    public PatientController(PatientRepository patientRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

//    @GetMapping("/index")
//    public String index(Model model,
//                        @RequestParam(name = "page", defaultValue = "0") int page,
//                        @RequestParam(name = "size", defaultValue = "4") int size,
//                        @RequestParam(name = "keyword", defaultValue = "") String keyword) {
//        Page<Patient> patients = patientRepository.findByNameContains(keyword, PageRequest.of(page, size));
//        model.addAttribute("patients", patients.getContent());
//        model.addAttribute("pages", new int[patients.getTotalPages()]);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("keyword", keyword);
//        return "patients";
//    }

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

//    @GetMapping("/ConsultationPatient")
//    public String ConsultationPatient(Model model, @RequestParam(name = "id") Long id) {
//        Patient patient = patientRepository.findById(id).get();
//        model.addAttribute("patient", patient);
//        return "ConsultationPatient";
//    }

//    @GetMapping("/patient/{id}/consultation")
//    public String ConsultationPatient(@PathVariable("id") Long id, Model model, Principal principal) {
//        // Récupérer le patient par son ID
//        Patient patient = patientRepository.findById(id).orElse(null);
//
//        // Récupérer l'email du docteur connecté
//        String email = principal.getName();
//        Doctor doctor = doctorService.findByEmail(email);
//
//        // Vérifier si le patient existe
//        if (patient == null) {
//            model.addAttribute("errorMessage", "Patient non trouvé");
//            return "error"; // Ou rediriger vers une page d'erreur
//        }
//
//        // Récupérer les consultations spécifiques pour ce patient et ce médecin
//        List<Consultation> consultation = consultationService.findByPatientAndDoctor(patient, doctor);
//
//        // Ajouter les objets au modèle pour Thymeleaf
//        model.addAttribute("patient", patient);
//        model.addAttribute("doctor", doctor);
//        model.addAttribute("consultation", consultation);
//
//        // Retourner la vue de consultation
//        return "ConsultationPatient";
//    }

    @GetMapping("/patient/{id}/consultation")
    public String ConsultationPatient(@PathVariable("id") Long id, Model model, Principal principal) {
        // Récupérer le patient par son ID
        Patient patient = patientRepository.findById(id).orElse(null);

        // Récupérer l'email du docteur connecté
        String email = principal.getName();
        Doctor doctor = doctorService.findByEmail(email);

        // Vérifier si le patient existe
        if (patient == null) {
            model.addAttribute("errorMessage", "Patient non trouvé");
            return "error"; // Ou rediriger vers une page d'erreur
        }

        // Récupérer les consultations spécifiques pour ce patient et ce médecin
        List<Consultation> consultations = consultationService.findByPatientAndDoctor(patient, doctor);

        // Vérifier si des consultations existent pour ce patient et ce médecin
        if (consultations.isEmpty()) {
            model.addAttribute("errorMessage", "Aucune consultation trouvée pour ce patient et ce médecin.");
            return "error"; // Ou rediriger vers une page d'erreur
        }

        // Supposons que vous voulez récupérer la première consultation de la liste (ou bien ajouter une logique pour choisir une consultation spécifique)
        Consultation consultation = consultations.get(0); // Ou récupérez un autre élément selon le critère souhaité

        // Ajouter le patient, le docteur et la consultation au modèle
        model.addAttribute("patient", patient);
        model.addAttribute("doctor", doctor);
        model.addAttribute("consultation", consultation); // Passer l'objet consultation, pas la liste

        // Retourner la vue de consultation
        return "ConsultationPatient";
    }


}
