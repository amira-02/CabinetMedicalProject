
package tn.pi.cabinetmedicalproject.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tn.pi.cabinetmedicalproject.enums.AppointmentStatus;
import tn.pi.cabinetmedicalproject.model.Consultation;
import tn.pi.cabinetmedicalproject.model.Patient;
import tn.pi.cabinetmedicalproject.model.User;
import tn.pi.cabinetmedicalproject.repository.UserRepository;
import tn.pi.cabinetmedicalproject.service.ConsultationService;
import tn.pi.cabinetmedicalproject.service.PatientService;

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

        // Ajouter la pagination dans le modèle
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", consultationsPage.getTotalPages());

        return "profile";
    }





}

