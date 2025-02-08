package tn.pi.cabinetmedicalproject.web;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
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
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
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

    //    /**
//     * Ajoute une nouvelle consultation pour un patient spécifique.
//     */
    @PostMapping("/patient/{id}/consultation")
    public String addConsultations(
            @PathVariable Long id,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String prescription,
            @RequestParam(required = false) String allergies,
            @RequestParam(required = false) String currentTreatments,
            @RequestParam(required = false) String medicalHistory,
            Model model
    ) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));

        // Créer une nouvelle consultation
        Consultation consultation = new Consultation();
        consultation.setPatient(patient);
        consultation.setDescription(description);
        consultation.setPrescription(prescription);
        consultation.setAllergies(allergies);
        consultation.setCurrentTreatments(currentTreatments);
        consultation.setMedicalHistory(medicalHistory);
        consultation.setDate(LocalDate.now());  // Utilisation de la date actuelle

        // Enregistrer la consultation dans la base de données
        consultationRepository.save(consultation);

        // Ajouter un attribut pour le message de succès et la liste des consultations
        model.addAttribute("consultations", consultationRepository.findByPatientId(id));
        model.addAttribute("patient", patient);
        return "redirect:/patient/{id}/consultations";  // Rediriger vers la liste des consultations du patient
    }

}



    // Endpoint to display the Consultation form for a specific doctor




















    /**
     * Affiche la liste des consultations d'un patient spécifique.
     */
//    @GetMapping("/patient/{id}/consultations")
//    public String getConsultations(@PathVariable Long id, Model model) {
//        Patient patient = patientRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));
//
//        List<Consultation> consultations = consultationRepository.findByPatientId(id);
//        model.addAttribute("patient", patient);
//        model.addAttribute("consultations", consultations);
//        model.addAttribute("newConsultation", new Consultation());
//
//        return "consultationPatient";
//    }





//    @GetMapping
//    public String getAllConsultation(Model model) {
//        List<Consultation> Consultation = ConsultationService.getAllConsultations();
//        model.addAttribute("Consultation", Consultation);
//        return "appointments"; // Thymeleaf view for Consultation
//    }




























//    // Endpoint to display the Consultation form for a logged-in patient
//    @GetMapping("/patient-form")
//    public String showPatientappointmentForm(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String username = authentication.getName();
//        User user = userRepository.findByEmail(username);
//
//        if (user == null) {
//            return "redirect:/registration"; // Redirect to registration if not logged in
//        }
//
//        // Get patient associated with the logged-in user
//        Patient patient = patientService.findByUser(user);
//        if (patient == null) {
//            return "redirect:/registration"; // Ensure patient exists, redirect to registration if not
//        }
//
//        List<Doctor> doctors = doctorService.getAllDoctors();
//        model.addAttribute("doctors", doctors);
//        model.addAttribute("patient", patient);
//        model.addAttribute("Consultation", new Consultation());
//
//        return "appointmentForm"; // Thymeleaf view for the form
//    }



    // Registration logic (handle registration if the user is not logged in)
//    @GetMapping("/registration")
//    public String showRegistrationPage() {
//        return "registration"; // Show registration page if not logged in
//    }

//    @PostMapping("/registration")
//    public String handleRegistration(@ModelAttribute UserRegistrationDto userDto, Model model) {
//        // Registration logic here
//        // Save the user to the database
//        User user = userServiceImpl.save(userDto); // Use the correct method from UserServiceImpl
//
//        // Create the associated patient
//        Patient patient = new Patient();
//        patient.setUser(user);
//        patientService.savePatient(patient);
//
//        // After successful registration, redirect to Consultation form
//        return "redirect:/Consultation/patient-form"; // Redirect to the patient Consultation form after registration
//    }






//    /**
//     * Supprime une consultation spécifique d'un patient.
//     */
//    @Transactional
//    @GetMapping("/patient/{patientId}/deleteConsultation/{consultationId}")
//    public String deleteConsultation(@PathVariable Long patientId, @PathVariable Long consultationId) {
//        Patient patient = patientRepository.findById(patientId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));
//
//        Consultation consultation = consultationRepository.findById(consultationId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid consultation ID"));
//
//        consultationRepository.delete(consultation);
//
//        return "redirect:/patient/" + patientId + "/consultations";
//    }
//
//    /**
//     * Mise à jour des informations du patient (Height, Weight, Pathology).
//     */
//    @Transactional
//    @PostMapping("/patient/{id}/update")
//    public String updatePatientInfo(
//            @PathVariable Long id,
//            @RequestParam String height,   // Récupère la hauteur sous forme de chaîne
//            @RequestParam String weight,   // Récupère le poids sous forme de chaîne
//            @RequestParam String pathology,
//            Model model
//    ) {
//        // Trouver le patient par son ID
//        Patient patient = patientRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));
//
//        try {
//            // Convertir les chaînes en float et affecter les valeurs aux champs du patient
//            if (height != null && !height.isEmpty()) {
//                patient.setHeight(Float.parseFloat(height));  // Convertit la chaîne en float
//            }
//            if (weight != null && !weight.isEmpty()) {
//                patient.setWeight(Float.parseFloat(weight));  // Convertit la chaîne en float
//            }
//
//            // Affecter le champ Pathology
//            patient.setPathology(pathology);
//
//            // Sauvegarder les modifications dans la base de données
//            patientRepository.save(patient);
//
//        } catch (NumberFormatException e) {
//            // En cas d'erreur de conversion, logguer l'erreur et ne pas mettre à jour
//            e.printStackTrace();
//            model.addAttribute("error", "Invalid height or weight format.");
//            return "errorPage";  // Rediriger vers une page d'erreur
//        }
//
//        // Rediriger vers la page des consultations après la mise à jour
//        return "redirect:/patient/" + id + "/consultations";
//    }

