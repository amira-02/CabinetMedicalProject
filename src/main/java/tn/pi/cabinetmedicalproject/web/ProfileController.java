//package tn.pi.cabinetmedicalproject.web;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import tn.pi.cabinetmedicalproject.model.Consultation;
//import tn.pi.cabinetmedicalproject.model.Patient;
//import tn.pi.cabinetmedicalproject.model.User;
//import tn.pi.cabinetmedicalproject.repository.UserRepository;
//import tn.pi.cabinetmedicalproject.service.ConsultationService;
//import tn.pi.cabinetmedicalproject.service.PatientService;
//import tn.pi.cabinetmedicalproject.service.UserService;
//
//import java.security.Principal;
//import java.util.List;
//
//@Controller
//@RequestMapping("/profile")
//public class ProfileController {
//    @Autowired
//    private ConsultationService consultationService;
//
//    @Autowired
//    private PatientService patientService;
//
//    @Autowired
//    private UserRepository userRepository; // Repository pour gérer les utilisateurs
//
//    @Autowired
//    private UserService userService;
//    @GetMapping
//    public String getProfile(Model model, Principal principal) {
//        // Récupérer l'email de l'utilisateur connecté
//        String email = principal.getName();
//
//        // Trouver l'utilisateur dans la base de données
//        User user = userRepository.findByEmail(email);
//
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found");
//        }
//
//        // Récupérer les informations du patient via le userId
//        Patient patient = patientService.findByUserId(user.getId());
//        model.addAttribute("patient", patient);
//
//        return "profile";
//    }
//
//
//    @GetMapping("/profile")
//    public String profilePage(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            // L'utilisateur est authentifié
//            Object principal = authentication.getPrincipal();
//            if (principal instanceof UserDetails) {
//                String username = ((UserDetails) principal).getUsername();
//                // Ajout du nom d'utilisateur au modèle pour l'affichage
//                model.addAttribute("username", username);
//            }
//        } else {
//            // L'utilisateur n'est pas authentifié
//            model.addAttribute("message", "You need to log in!");
//        }
//        return "profile"; // La vue profile.html
//
//    }
//    @GetMapping("/profileConsultation")
//    public String showProfile(Model model, Principal principal) {
//        // Récupérer l'utilisateur connecté
//        User user = userService.findByemail(principal.getName());
//
//        // Vérifier si l'utilisateur est un patient
//        if (user.getRole().equals("ROLE_USER")) {
//            // Récupérer les consultations du patient connecté
//            List<Consultation> consultations = consultationService.findByPatientId(user.getId());
//            model.addAttribute( "consultations", consultations);
//        }
//
//        model.addAttribute("user", user);
//
//        return "profile"; // Nom du fichier Thymeleaf à afficher
//    }
//
//}
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
        String email = principal.getName();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Patient patient = patientService.findByUserId(user.getId());
        model.addAttribute("patient", patient);

        Pageable pageable = PageRequest.of(page, size);
        Page<Consultation> consultationsPage = consultationService.findConsultationsByPatientId(patient.getId(), pageable);

        model.addAttribute("consultations", consultationsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", consultationsPage.getTotalPages());

        return "profile";
    }}
//        @GetMapping
//    public String getProfile(Model model, Principal principal) {
//        // Récupérer l'email de l'utilisateur connecté
//        String email = principal.getName();
//
//        // Trouver l'utilisateur dans la base de données
//        User user = userRepository.findByEmail(email);
//        if (user == null) {
//            return "redirect:/login"; // Rediriger vers la page de connexion si l'utilisateur n'est pas trouvé
//        }
//
//        // Vérifier si l'utilisateur est un patient
//        Patient patient = patientService.findByUserId(user.getId());
//        if (patient == null) {
//            model.addAttribute("message", "Aucun profil patient trouvé.");
//            return "profile";
//        }
//
//        // Récupérer les consultations du patient
//        List<Consultation> consultations = consultationService.findByPatientId(patient.getId());
//
//        // Ajouter les données au modèle
//        model.addAttribute("user", user);
//        model.addAttribute("patient", patient);
//        model.addAttribute("consultations", consultations);
//
//        return "profile"; // Affiche profile.html
//    }

