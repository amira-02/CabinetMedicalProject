package tn.pi.cabinetmedicalproject.web;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder; // Importer PasswordEncoder
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tn.pi.cabinetmedicalproject.model.Doctor;
import tn.pi.cabinetmedicalproject.model.User;
import tn.pi.cabinetmedicalproject.repository.DoctorRepository;
import tn.pi.cabinetmedicalproject.service.UserService;

@Controller
@Slf4j
public class DoctorController {

    private final DoctorRepository doctorRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder; // Injection du PasswordEncoder

    public DoctorController(DoctorRepository doctorRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder; // Initialisation du PasswordEncoder
    }

    // Route to display the list of doctors with pagination and search
    @GetMapping("/DoctorsList")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "4") int size,
                        @RequestParam(name = "keyword", defaultValue = "") String keyword) {

        Page<Doctor> doctors = doctorRepository.findByNameContains(keyword, PageRequest.of(page, size));
        model.addAttribute("doctors", doctors.getContent());
        model.addAttribute("pages", new int[doctors.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);

        return "DoctorsList";
    }

    // Route to delete a doctor
    @GetMapping("/deleteDoctor")
    public String delete(@RequestParam Long id, @RequestParam String keyword, @RequestParam int page) {
        doctorRepository.deleteById(id);
        return "redirect:/DoctorsList?page=" + page + "&keyword=" + keyword;
    }

    // Route to show the form for adding a new doctor
    @GetMapping("/formDoctor")
    public String formDoctor(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "formDoctor";
    }

    // Route to save the new or edited doctor
    @PostMapping("/saveDoctor")
    public String saveDoctor(Model model, @Valid Doctor doctor, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "formDoctor"; // Return the form page if there are validation errors
        }

        // Ensure the User object is set for the doctor
        if (doctor.getUser() == null) {
            doctor.setUser(new User()); // Create a new User if not provided
        }

        // Hash the password before saving it
        User user = doctor.getUser();
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash the password
        }

        user.setRole("ROLE_DOCTOR"); // Assign the role to "doctor"

        // Save the User first
        userService.save(user);

        // After saving the user, associate the user with the doctor
        doctor.setUser(user);

        // Save the Doctor entity
        doctorRepository.save(doctor);

        // Reset the form (clear the Doctor instance for the next input)
        model.addAttribute("doctor", new Doctor());
        return "redirect:/DoctorsList"; // Redirect to the list of doctors
    }

    // Route to show the form to edit an existing doctor
    @GetMapping("/EditDoctor")
    public String EditDoctor(Model model, @RequestParam(name = "id") Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
        model.addAttribute("doctor", doctor);
        return "EditDoctor";
    }

    // Route to show the consultations for a doctor
    @GetMapping("/ConsultationDoctor")
    public String ConsultationDoctor(Model model, @RequestParam(name = "id") Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
        model.addAttribute("doctor", doctor);
        return "ConsultationDoctor";
    }
}
