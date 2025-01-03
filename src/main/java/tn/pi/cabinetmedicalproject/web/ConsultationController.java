package tn.pi.cabinetmedicalproject.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import tn.pi.cabinetmedicalproject.model.Consultation;
import tn.pi.cabinetmedicalproject.model.Patient;
import tn.pi.cabinetmedicalproject.repository.ConsultationRepository;
import tn.pi.cabinetmedicalproject.repository.PatientRepository;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Controller
@Slf4j
public class ConsultationController {

    private final ConsultationRepository consultationRepository;
    private final PatientRepository patientRepository;

    public ConsultationController(ConsultationRepository consultationRepository, PatientRepository patientRepository) {
        this.consultationRepository = consultationRepository;
        this.patientRepository = patientRepository;
    }

    /**
     * Affiche la liste des consultations d'un patient spécifique.
     */
    @GetMapping("/patient/{id}/consultations")
    public String getConsultations(@PathVariable Long id, Model model) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));

        List<Consultation> consultations = consultationRepository.findByPatientId(id);
        model.addAttribute("patient", patient);
        model.addAttribute("consultations", consultations);
        model.addAttribute("newConsultation", new Consultation());

        return "consultationPatient";
    }

    /**
     * Ajoute une nouvelle consultation pour un patient spécifique.
     */
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


    /**
     * Supprime une consultation spécifique d'un patient.
     */
    @Transactional
    @GetMapping("/patient/{patientId}/deleteConsultation/{consultationId}")
    public String deleteConsultation(@PathVariable Long patientId, @PathVariable Long consultationId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));

        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid consultation ID"));

        consultationRepository.delete(consultation);

        return "redirect:/patient/" + patientId + "/consultations";
    }

    /**
     * Mise à jour des informations du patient (Height, Weight, Pathology).
     */
    @Transactional
    @PostMapping("/patient/{id}/update")
    public String updatePatientInfo(
            @PathVariable Long id,
            @RequestParam String height,   // Récupère la hauteur sous forme de chaîne
            @RequestParam String weight,   // Récupère le poids sous forme de chaîne
            @RequestParam String pathology,
            Model model
    ) {
        // Trouver le patient par son ID
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));

        try {
            // Convertir les chaînes en float et affecter les valeurs aux champs du patient
            if (height != null && !height.isEmpty()) {
                patient.setHeight(Float.parseFloat(height));  // Convertit la chaîne en float
            }
            if (weight != null && !weight.isEmpty()) {
                patient.setWeight(Float.parseFloat(weight));  // Convertit la chaîne en float
            }

            // Affecter le champ Pathology
            patient.setPathology(pathology);

            // Sauvegarder les modifications dans la base de données
            patientRepository.save(patient);

        } catch (NumberFormatException e) {
            // En cas d'erreur de conversion, logguer l'erreur et ne pas mettre à jour
            e.printStackTrace();
            model.addAttribute("error", "Invalid height or weight format.");
            return "errorPage";  // Rediriger vers une page d'erreur
        }

        // Rediriger vers la page des consultations après la mise à jour
        return "redirect:/patient/" + id + "/consultations";
    }
}
