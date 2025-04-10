
package tn.pi.cabinetmedicalproject.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tn.pi.cabinetmedicalproject.enums.AppointmentStatus;
import tn.pi.cabinetmedicalproject.model.*;
import tn.pi.cabinetmedicalproject.repository.ArchiveRepository;
import tn.pi.cabinetmedicalproject.repository.ConsultationRepository;
import tn.pi.cabinetmedicalproject.repository.DoctorRepository;
import tn.pi.cabinetmedicalproject.repository.UserRepository;
import tn.pi.cabinetmedicalproject.service.ConsultationService;
import tn.pi.cabinetmedicalproject.service.PatientService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
@Controller
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ConsultationService consultationService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private ArchiveRepository archiveRepository;

    @PostMapping("/{id}/canceled")
    public String markAsCanceled(@PathVariable Long id, Principal principal) {
        String email = principal.getName();

        // Trouver l'utilisateur connecté
        User user = userRepository.findByEmail(email);

        // Trouver le patient lié à l'utilisateur
        Patient patient = patientService.findByUserId(user.getId());

        // Trouver la consultation
        Consultation consultation = consultationRepository.findById(id).orElse(null);

        // Vérifier que la consultation appartient bien au patient connecté
        if (consultation != null && consultation.getPatient().getId().equals(patient.getId())) {

            // Mettre à jour le statut
            consultation.setStatus(AppointmentStatus.CANCELED);

            // Créer une archive
            Archive archive = Archive.builder()
                    .doctor(consultation.getDoctor())
                    .patient(consultation.getPatient())
                    .date(consultation.getDate())
                    .time(consultation.getTime())
                    .status(AppointmentStatus.CANCELED)
                    .description(consultation.getDescription())
                    .prescription(consultation.getPrescription())
                    .build();

            // Sauvegarder l'archive
            archiveRepository.save(archive);

            // Supprimer la consultation (ou la sauvegarder si tu préfères la garder)
            consultationRepository.delete(consultation);
        }

        // Rediriger vers la page profile avec un message de succès
        return "redirect:/profile?success=Consultation annulée avec succès";
    }

    @GetMapping
    public String getProfile(Model model, Principal principal,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "5") int size) {
        // Récupérer l'email de l'utilisateur connecté
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Récupérer les informations du patient
        Patient patient = patientService.findByUserId(user.getId());
        model.addAttribute("patient", patient);

        // Créer une page pageable
        Pageable pageable = PageRequest.of(page, size);

        // Consultation filtrée par statut "PENDING_PAYMENT"
        Page<Consultation> pendingPaymentsPage = consultationService.findConsultationsByPatientIdAndStatus(patient.getId(), AppointmentStatus.PENDING_PAYMENT, pageable);
        model.addAttribute("pendingPayments", pendingPaymentsPage.getContent());

        // Consultation filtrée par statut "SCHEDULED"
        Page<Consultation> scheduledAppointmentsPage = consultationService.findConsultationsByPatientIdAndStatus(patient.getId(), AppointmentStatus.SCHEDULED, pageable);
        model.addAttribute("scheduledAppointments", scheduledAppointmentsPage.getContent());

        // Consultation générale pour l'historique
        Page<Consultation> consultationsPage = consultationService.findConsultationsByPatientId(patient.getId(), pageable);
        model.addAttribute("consultations", consultationsPage.getContent());

        List<Archive> completedConsultations = archiveRepository.findByPatientIdAndStatus(patient.getId(), AppointmentStatus.COMPLETED);
        model.addAttribute("completedConsultations", completedConsultations);



        // Ajouter la pagination dans le modèle
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", consultationsPage.getTotalPages());

        return "profile";  // Nom de la vue Thymeleaf
    }

    @PostMapping("/{id}/completed")
    public String markAsCompleted(@PathVariable Long id, Principal principal) {
        String email = principal.getName();

        // Trouver l'utilisateur connecté
        User user = userRepository.findByEmail(email);

        // Trouver le patient lié à l'utilisateur
        Patient patient = patientService.findByUserId(user.getId());

        // Trouver la consultation
        Consultation consultation = consultationRepository.findById(id).orElse(null);

        // Vérifier que la consultation appartient bien au patient connecté
        if (consultation != null && consultation.getPatient().getId().equals(patient.getId())) {

            // Mettre à jour le statut
            consultation.setStatus(AppointmentStatus.COMPLETED);

            // Créer une archive
            Archive archive = Archive.builder()
                    .doctor(consultation.getDoctor())
                    .patient(consultation.getPatient())
                    .date(consultation.getDate())
                    .time(consultation.getTime())
                    .status(AppointmentStatus.COMPLETED)
                    .description(consultation.getDescription())
                    .prescription(consultation.getPrescription())
                    .build();

            // Sauvegarder l'archive
            archiveRepository.save(archive);

            // Supprimer la consultation (ou la sauvegarder si tu préfères la garder)
            consultationRepository.delete(consultation);
        }

        // Rediriger vers la page profile avec un message de succès
        return "redirect:/profile?success=Consultation terminée avec succès";
    }
}

