package tn.pi.cabinetmedicalproject.web;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tn.pi.cabinetmedicalproject.enums.AppointmentStatus;
import tn.pi.cabinetmedicalproject.model.Consultation;
import tn.pi.cabinetmedicalproject.model.Doctor;
import tn.pi.cabinetmedicalproject.model.Patient;
import tn.pi.cabinetmedicalproject.model.User;
import tn.pi.cabinetmedicalproject.repository.ConsultationRepository;
import tn.pi.cabinetmedicalproject.repository.DoctorRepository;
import tn.pi.cabinetmedicalproject.repository.PatientRepository;
import tn.pi.cabinetmedicalproject.repository.UserRepository;
import tn.pi.cabinetmedicalproject.service.ConsultationService;
import tn.pi.cabinetmedicalproject.service.DoctorService;
import tn.pi.cabinetmedicalproject.service.PatientService;
import tn.pi.cabinetmedicalproject.service.UserServiceImpl;
import tn.pi.cabinetmedicalproject.web.dto.UserRegistrationDto;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;



@Controller
@RequestMapping("/Consultation")

public class ConsultationController {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientService patientService;
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private ConsultationRepository consultationRepository;
    private static final Logger logger = LoggerFactory.getLogger(ConsultationController.class);


    @Autowired
    private UserDetailsService userDetailsService;  // Pour récupérer l'utilisateur connecté

    @Autowired
    private ConsultationService consultationService; // Injection correcte


    @PostMapping("/submit")
    public String submitConsultation(@ModelAttribute Consultation consultation, Model model) {
        // Get the logged-in user's email from the Spring Security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();  // Assuming email is used as the username for login

        // Fetch the Patient by the associated User's email
        Patient patient = patientService.findByEmail(email);  // This will call findByUserEmail

        // Check if time and date are provided
        if (consultation.getTime() == null) {
            logger.error("Time is missing!");
            model.addAttribute("error", "Time is required.");
            return "doctorhome";  // Return the form with an error
        }

        if (consultation.getDate() == null) {
            logger.error("Date is missing!");
            model.addAttribute("error", "Date is required.");
            return "doctorhome";  // Return the form with an error
        }

        // Set the patient in the consultation
        consultation.setPatient(patient);
        consultation.setStatus(AppointmentStatus.SCHEDULED);

        // Save the consultation
        consultationService.saveConsultation(consultation);

        // Add the consultation data to the model for the view
        model.addAttribute("consultationDate", consultation.getDate());
        model.addAttribute("consultationTime", consultation.getTime());
        model.addAttribute("consultationDoctor", consultation.getDoctor());
        model.addAttribute("consultationPatient", consultation.getPatient());

        // Redirect or return the view
        return "Home";  // Or wherever you need to redirect after submission
    }
//
//
//    @PostMapping("/submit")
//    public String submitConsultation(@ModelAttribute Consultation consultation, Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String email = userDetails.getUsername();
//
//        Patient patient = patientService.findByEmail(email);
//        if (patient == null) {
//            logger.error("Patient not found for email: {}", email);
//            model.addAttribute("error", "Patient not found.");
//            return "doctorhome";
//        }
//
//        // Vérifier si le patient a déjà une consultation avec ce médecin à cette date
//        boolean exists = consultationRepository.existsByPatientAndDoctorAndDate(patient, consultation.getDoctor());
//        if (exists) {
//            logger.error("Patient already has a consultation with this doctor on this date.");
//            model.addAttribute("error", "You already have a consultation scheduled with this doctor on this date.");
//            return "doctorhome";
//        }
//
//        if (consultation.getTime() == null || consultation.getDate() == null) {
//            logger.error("Time or Date is missing!");
//            model.addAttribute("error", "Time and Date are required.");
//            return "doctorhome";
//        }
//
//        consultation.setPatient(patient);
//        consultation.setStatus(AppointmentStatus.SCHEDULED);
//        consultationService.saveConsultation(consultation);
//
//        model.addAttribute("consultationDate", consultation.getDate());
//        model.addAttribute("consultationTime", consultation.getTime());
//        model.addAttribute("consultationDoctor", consultation.getDoctor());
//        model.addAttribute("consultationPatient", consultation.getPatient());
//
//        return "Home";
//    }


    @PostMapping("/save")
    public String saveConsultation(@ModelAttribute Consultation consultation, Model model) {
        logger.info("Attempting to save consultation...");
        logger.info("Consultation Date: " + consultation.getDate());
        logger.info("Consultation Time: " + consultation.getTime());

        // Vérification si le patient et le docteur sont bien définis
        if (consultation.getPatient() == null || consultation.getDoctor() == null) {
            logger.error("Error: Patient or Doctor is missing!");
            model.addAttribute("error", "Patient or Doctor information is missing.");
            return "doctorhome"; // Retourne la page avec le message d'erreur
        }

        // Vérification si la date et l'heure sont renseignées
        if (consultation.getDate() == null || consultation.getTime() == null) {
            logger.error("Error: Date or Time is missing!");
            model.addAttribute("error", "Date and time must be provided.");
            return "doctorhome"; // Retourne la page avec le message d'erreur
        }

        try {
            // Définition du statut de la consultation
            consultation.setStatus(AppointmentStatus.SCHEDULED);

            // Appel du service pour sauvegarder la consultation
            consultationService.saveConsultation(consultation);
            logger.info("Consultation successfully saved!");

            return "redirect:/doctorhome"; // Redirige après un succès
        } catch (Exception e) {
            logger.error("Error saving consultation: {}", e.getMessage());
            model.addAttribute("error", "An unexpected error occurred while saving the consultation.");
            return "doctorhome"; // Affiche une erreur en cas d'exception
        }
    }

    @GetMapping("/form/{doctorId}")
    public String showConsultationForm(@PathVariable Long doctorId, Model model) {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        // Récupérer le patient associé à cet utilisateur
        Patient patient = patientRepository.findByUserEmail(email);

        // Vérifier si le patient existe
        if (patient == null) {
            model.addAttribute("error", "Patient not found");
            return "error patient";
        }

        // Récupérer le docteur
        Doctor doctor = doctorService.findById(doctorId);
        if (doctor == null) {
            model.addAttribute("error", "Doctor not found");
            return "error";
        }

        // Créer une consultation et pré-remplir les champs
        Consultation consultation = new Consultation();
        consultation.setDoctor(doctor);
        consultation.setPatient(patient); // Associer le patient à la consultation

        // Ajouter les objets au modèle pour Thymeleaf
        model.addAttribute("consultation", consultation);
        model.addAttribute("doctor", doctor);
        model.addAttribute("patient", patient); // Ajouter le patient pour pré-remplir le formulaire

        return "appointmentForm"; // Thymeleaf view
    }


    // Endpoint to display Consultation for the logged-in doctor
    @GetMapping("/doctor")
    public String showDoctorConsultation(Model model) {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Trouver le médecin associé à cet utilisateur
        Doctor doctor = doctorRepository.findByUserEmail(username);
        if (doctor == null) {
            model.addAttribute("consultation", Collections.emptyList());
            return "doctorhome"; // Rediriger vers la page sans consultations
        }

        // Récupérer les consultations du médecin
        List<Consultation> consultations = consultationRepository.findByDoctor(doctor);

        model.addAttribute("consultation", consultations);
        model.addAttribute("doctor", doctor);
        return "doctorhome"; // Affichage Thymeleaf
    }

    @GetMapping("/doctor/PendingPayment")
    public String showDoctorConsultationPendingPayment(Model model) {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Trouver le médecin associé à cet utilisateur
        Doctor doctor = doctorRepository.findByUserEmail(username);
        if (doctor == null) {
            model.addAttribute("consultation", Collections.emptyList());
            return "doctorhome"; // Rediriger vers la page sans consultations
        }

        // Récupérer les consultations du médecin
        List<Consultation> consultations = consultationRepository.findByDoctor(doctor);

        model.addAttribute("consultation", consultations);
        model.addAttribute("doctor", doctor);
        return "doctorhome"; // Affichage Thymeleaf
    }







    @GetMapping("/appointments")
    public String showAppointmentsPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get the logged-in user's username (email)

        // Retrieve the doctor associated with this username
        Doctor doctor = doctorRepository.findByUserEmail(username);
        if (doctor == null) {
            model.addAttribute("error", "Doctor not found");
            return "error";  // Return an error page if the doctor is not found
        }

        // Get the consultations associated with this doctor
        List<Consultation> consultations = consultationRepository.findByDoctor(doctor);

        model.addAttribute("consultations", consultations);
        model.addAttribute("doctor", doctor);
//        model.addAttribute("patient", patient);
        return "doctorhome";  // Return the appointments view for the doctor
    }


    @GetMapping("/doctor/home")
    public String doctorHome(Model model, Principal principal) {
        String email = principal.getName();
        Doctor doctor = doctorService.findByEmail(email);

        if (doctor == null) {
            return "redirect:/error";
        }

        // Charger les rendez-vous
        List<Consultation> consultation = consultationService.findByDoctor(doctor);

        model.addAttribute("doctor", doctor);
        model.addAttribute("consultation", consultation);

        return "doctorhome";
    }

    @PostMapping("/patient/{patientId}/consultation/{consultationId}/update")
    @Transactional
    public String updateConsultationAndPatient(
            @PathVariable Long patientId,
            @PathVariable Long consultationId,
            @RequestParam String description,
            @RequestParam String prescription,
            @RequestParam String allergies,
            @RequestParam String currentTreatments,
            @RequestParam String medicalHistory,
            @RequestParam float height,
            @RequestParam float weight,
            Model model,
            Principal principal) {

        // Logs for debugging
        System.out.println("Attempting to update consultation: " + consultationId + " for patient: " + patientId);

        // Validate form inputs
        if (description.isEmpty() || prescription.isEmpty() || allergies.isEmpty() || currentTreatments.isEmpty() || medicalHistory.isEmpty()) {
            model.addAttribute("error", "All fields must be filled out.");
            return "errorPage"; // Change to your error page view
        }

        // Vérifier si la consultation et le patient existent
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new RuntimeException("Consultation ID " + consultationId + " not found"));

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient ID " + patientId + " not found"));

        Doctor doctor = doctorService.findByEmail(principal.getName());

        // Mise à jour des informations de la consultation
        consultation.setDescription(description);
        consultation.setPrescription(prescription);
        consultation.setAllergies(allergies);
        consultation.setCurrentTreatments(currentTreatments);
        consultation.setMedicalHistory(medicalHistory);
        consultation.setStatus(AppointmentStatus.PENDING_PAYMENT); // Assuming AppointmentStatus enum is defined

        // Mise à jour des informations du patient
        patient.setHeight(height);
        patient.setWeight(weight);

        // Save consultation and patient in the database
        consultationRepository.save(consultation);
        patientRepository.save(patient);

        // Add the patient, consultation, and doctor objects to the model
        model.addAttribute("patient", patient);
        model.addAttribute("consultation", consultation);
        model.addAttribute("doctor", doctor);

        // Redirect to the doctor's home page
        return "doctorhome";
    }












    @PostMapping("/{id}/complete")
    public String markConsultationAsCompleted(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Récupérer la consultation par son ID
        Optional<Consultation> optionalConsultation = consultationRepository.findById(id);

        if (optionalConsultation.isPresent()) {
            Consultation consultation = optionalConsultation.get();

            // Vérifier que la consultation est en attente de paiement (ou tout autre statut que tu veux)
            if (AppointmentStatus.PENDING_PAYMENT.equals(consultation.getStatus())) {
                consultation.setStatus(AppointmentStatus.COMPLETED);  // Modifier le statut
                consultationRepository.save(consultation);  // Sauvegarder la consultation mise à jour

                // Ajouter un message de succès et rediriger vers la page des consultations
                redirectAttributes.addFlashAttribute("success", "Consultation marked as completed!");
                return "redirect:/doctorhome";  // Rediriger vers la page d'accueil du médecin
            } else {
                // Ajouter un message d'erreur si le statut n'est pas "PENDING_PAYMENT"
                redirectAttributes.addFlashAttribute("error", "Consultation cannot be marked as completed because it is not pending payment.");
            }
        } else {
            // Ajouter un message d'erreur si la consultation n'est pas trouvée
            redirectAttributes.addFlashAttribute("error", "Consultation not found.");
        }

        return "redirect:/doctorhome";  // Rediriger vers la page d'accueil en cas d'erreur
    }




}