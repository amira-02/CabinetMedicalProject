package tn.pi.cabinetmedicalproject.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tn.pi.cabinetmedicalproject.model.Appointments;
import tn.pi.cabinetmedicalproject.model.Doctor;
import tn.pi.cabinetmedicalproject.model.Patient;
import tn.pi.cabinetmedicalproject.repository.AppointmentRepository;
import tn.pi.cabinetmedicalproject.repository.DoctorRepository;
import tn.pi.cabinetmedicalproject.service.AppointmentService;
import tn.pi.cabinetmedicalproject.service.DoctorService;
import tn.pi.cabinetmedicalproject.service.PatientService;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    // Endpoint to save an appointment
    @PostMapping("/save")
    public String saveAppointment(@ModelAttribute Appointments appointment) {
        appointmentService.saveAppointment(appointment);
        return "redirect:/appointments"; // Redirect to appointments list
    }

    // Endpoint to display all appointments (for admin or general users)
    @GetMapping
    public String getAllAppointments(Model model) {
        List<Appointments> appointments = appointmentService.getAllAppointments();
        model.addAttribute("appointments", appointments);
        return "appointments"; // Thymeleaf view for appointments
    }

    // Endpoint to display the appointment form for a specific doctor
    @GetMapping("/form/{doctorId}")
    public String showAppointmentForm(@PathVariable Long doctorId, Model model) {
        Doctor doctor = doctorService.findById(doctorId);
        if (doctor == null) {
            model.addAttribute("error", "Doctor not found");
            return "error"; // Handle missing doctor scenario
        }

        Appointments appointment = new Appointments();
        appointment.setDoctor(doctor);
        model.addAttribute("appointment", appointment);
        model.addAttribute("doctor", doctor);
        return "appointmentForm"; // Thymeleaf view for the form
    }

    // Endpoint to display appointments for the logged-in doctor
    @GetMapping("/doctor")
    public String showDoctorAppointments(Model model) {
        // Get the current authenticated user's username (email or username)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Find the doctor by the username
        Doctor doctor = doctorRepository.findByUserEmail(username); // Or findByEmail if using email to log in
        if (doctor == null) {
            model.addAttribute("appointments", Collections.emptyList());
            return "appointments"; // No appointments if doctor not found
        }

        // Get appointments for the specific doctor
        List<Appointments> appointments = appointmentRepository.findByDoctor(doctor);
        model.addAttribute("appointments", appointments);
        return "appointments"; // Thymeleaf view for doctor's appointments
    }

    // Optional: Endpoint for submitting an appointment form
    @PostMapping("/submit")
    public String submitAppointment(@ModelAttribute Appointments appointment, Model model) {
        // Check if the patient data is provided
        Patient patient = appointment.getPatient();
        if (patient == null) {
            model.addAttribute("error", "Patient information is missing");
            return "appointments" ; // Show error in form if patient info is missing
        }

        // Check if phone number and birthdate are provided for the patient
        if (patient.getTelephone() != null && !patient.getTelephone().isEmpty() && patient.getBirthDate() != null) {
            // Save the patient
            patientService.savePatient(patient);
        } else {
            model.addAttribute("error", "Phone number and birthdate must be provided");
            return "appointments"; // Show error in form if details are missing
        }

        // Save the appointment
        appointmentService.saveAppointment(appointment);

        logger.info("Appointment saved for patient: {} with doctor: {}", patient.getName(), appointment.getDoctor().getName());

        return "Home"; // Redirect to appointments list after successful submission
    }
}
