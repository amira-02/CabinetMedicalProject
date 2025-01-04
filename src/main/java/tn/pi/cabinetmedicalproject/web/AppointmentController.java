package tn.pi.cabinetmedicalproject.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import tn.pi.cabinetmedicalproject.model.Appointments;
import tn.pi.cabinetmedicalproject.model.Doctor;
import tn.pi.cabinetmedicalproject.model.Patient;
import tn.pi.cabinetmedicalproject.model.User;
import tn.pi.cabinetmedicalproject.repository.PatientRepository;
import tn.pi.cabinetmedicalproject.service.AppointmentService;
import tn.pi.cabinetmedicalproject.service.DoctorService;
import tn.pi.cabinetmedicalproject.service.UserService;
import tn.pi.cabinetmedicalproject.service.PatientService;
import tn.pi.cabinetmedicalproject.repository.AppointmentRepository;
import tn.pi.cabinetmedicalproject.repository.DoctorRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @PostMapping("/appointments/save")
    public String saveAppointment(Appointments appointment) {
        appointmentService.saveAppointment(appointment); // Utilisation de saveAppointment du service
        return "redirect:/appointments"; // Redirection vers la page des rendez-vous
    }

    @GetMapping("/appointmentForm/{doctorId}")
    public String showAppointmentForm(@PathVariable Long doctorId, Model model) {
        // Récupère le médecin sélectionné
        Doctor doctor = doctorService.findById(doctorId);

        // Récupère l'utilisateur connecté
        User user = userService.getCurrentUser();

        // Crée un nouvel objet Patient (vide pour être rempli par l'utilisateur)
        Patient patient = new Patient();

        // Crée un nouvel objet Appointment avec le médecin et patient
        Appointments appointment = new Appointments();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        // Ajoute les objets nécessaires au modèle
        model.addAttribute("appointment", appointment);
        model.addAttribute("doctor", doctor);

        return "appointmentForm"; // Retourne la vue du formulaire
    }

    @PostMapping("/submit-appointment")
    public String submitAppointment(@ModelAttribute Appointments appointment) {
        // Récupérer le patient du formulaire de rendez-vous
        Patient patient = appointment.getPatient();

        // Vérifier si le téléphone et la date de naissance sont fournis et valides
        if (patient != null) {
            if (patient.getTelephone() != null && !patient.getTelephone().isEmpty() && patient.getBirthDate() != null) {
                // Sauvegarder les informations du patient si elles sont valides
                patientService.savePatient(patient);  // Sauvegarde du patient
            }
        }

        // Sauvegarder le rendez-vous
        appointmentService.saveAppointment(appointment);

        // Rediriger vers la liste des rendez-vous
        return "redirect:/appointments";
    }

    @GetMapping("/appointments")
    public String showAppointmentsPage(Model model) {
        // Cette méthode affiche la page principale des rendez-vous
        return "appointments";  // Cette vue est générée par l'URL /appointments
    }

    @GetMapping("/appointments/doctor")
    public String showAppointments(Model model) {
        // Cette méthode affiche les rendez-vous spécifiques pour le médecin
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Doctor doctor = doctorRepository.findByname(username); // Modifié pour récupérer le médecin via username

        if (doctor != null) {
            // Récupérer tous les rendez-vous associés au médecin
            List<Appointments> appointments = appointmentRepository.findByDoctor(doctor);
            List<Patient> patients = new ArrayList<>();
            for (Appointments appointment : appointments) {
                Patient patient = appointment.getPatient();
                if (patient != null) {
                    patients.add(patient);
                }
            }

            model.addAttribute("appointments", appointments);
            model.addAttribute("patients", patients);
        }

        return "appointments";  // Cette vue est générée par l'URL /appointments/doctor
    }




}
