package tn.pi.cabinetmedicalproject.web;

import  javax.validation.Valid;
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
    @Transactional
    @PostMapping("/patient/{id}/consultation")
    public String addConsultation(
            @PathVariable Long id,
            @Valid @ModelAttribute("newConsultation") Consultation consultation,
            BindingResult bindingResult,
            Model model
    ) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid patient ID"));

        if (bindingResult.hasErrors()) {
            log.error("Validation errors: {}", bindingResult.getAllErrors());
            List<Consultation> consultations = consultationRepository.findByPatientId(id);
            model.addAttribute("patient", patient);
            model.addAttribute("consultations", consultations);
            return "consultationPatient";
        }

        consultation.setPatient(patient);
        consultation.setDate(LocalDate.now());
        consultationRepository.save(consultation);

        return "redirect:/patient/" + id + "/consultations";
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
}