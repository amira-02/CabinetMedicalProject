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
import tn.pi.cabinetmedicalproject.repository.AppointmentRepository;
import tn.pi.cabinetmedicalproject.repository.DoctorRepository;
import tn.pi.cabinetmedicalproject.repository.PatientRepository;
import tn.pi.cabinetmedicalproject.service.AppointmentService;
import tn.pi.cabinetmedicalproject.service.DoctorService;
import tn.pi.cabinetmedicalproject.service.PatientService;
import tn.pi.cabinetmedicalproject.service.UserService;

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

    // Endpoint to save an appointment
    @PostMapping("/appointments/save")
    public String saveAppointment(Appointments appointment) {
        appointmentService.saveAppointment(appointment);
        return "redirect:/appointments"; // Redirect to appointments list
    }

    // Endpoint to display the list of all appointments
    @GetMapping("/appointments")
    public String getAppointments(Model model) {
        List<Appointments> appointments = appointmentService.getAllAppointments();
        model.addAttribute("appointments", appointments);
        return "appointments";  // Return appointments view
    }

    // Endpoint to show the appointment form for a specific doctor
    @GetMapping("/appointmentForm/{doctorId}")
    public String showAppointmentForm(@PathVariable Long doctorId, Model model) {
        Doctor doctor = doctorService.findById(doctorId);
        User user = userService.getCurrentUser();  // Retrieve current user (logged-in user)

        // Create a new patient object and a new appointment
        Patient patient = new Patient();
        Appointments appointment = new Appointments();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);

        model.addAttribute("appointment", appointment);
        model.addAttribute("doctor", doctor);
        return "appointmentForm"; // Return the form view
    }

    // Endpoint to handle the submission of an appointment form
    @PostMapping("/submit-appointment")
    public String submitAppointment(@ModelAttribute Appointments appointment) {
        Patient patient = appointment.getPatient();

        // Check if phone number and birthdate are provided
        if (patient != null && patient.getTelephone() != null && !patient.getTelephone().isEmpty() && patient.getBirthDate() != null) {
            // Save the patient if valid
            patientService.savePatient(patient);
        }

        // Save the appointment
        appointmentService.saveAppointment(appointment);

        return "redirect:/appointments"; // Redirect to the list of appointments
    }

    // Endpoint for the doctor to view their appointments
    @GetMapping("/appointments/doctor")
    public String showDoctorAppointments(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Retrieve the doctor using the logged-in username
        Doctor doctor = doctorRepository.findByname(username);

        if (doctor != null) {
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

        return "appointments";  // Return the appointments page for the doctor
    }

    // Optional: Endpoint to show doctor's profile along with appointments (if necessary)
    @GetMapping("/appointment")
    public String showDoctorProfile(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Doctor doctor = doctorRepository.findByUserEmail(username);

        if (doctor != null) {
            Long doctorId = doctor.getId();
            model.addAttribute("doctorEmail", doctor.getUser().getEmail());
            model.addAttribute("doctorId", doctorId);

            // Retrieve appointments for this doctor
            List<Appointments> appointments = appointmentRepository.findByDoctor(doctor);
            model.addAttribute("appointments", appointments);
        } else {
            model.addAttribute("error", "No doctor found for the logged-in user.");
        }

        return "appointments"; // Return the appointments page for the doctor
    }
}
