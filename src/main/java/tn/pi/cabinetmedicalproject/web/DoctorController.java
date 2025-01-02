package tn.pi.cabinetmedicalproject.web;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import tn.pi.cabinetmedicalproject.model.Doctor;
import tn.pi.cabinetmedicalproject.model.Patient;
import tn.pi.cabinetmedicalproject.model.User;
import tn.pi.cabinetmedicalproject.repository.DoctorRepository;
import tn.pi.cabinetmedicalproject.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
public class DoctorController {

    private final DoctorRepository doctorRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private static final String UPLOAD_DIR = "/images/";

    public DoctorController(DoctorRepository doctorRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }
//    @GetMapping("/DoctorsList")
//    public String index(Model model,
//                        @RequestParam(name = "page", defaultValue = "0") int page,
//                        @RequestParam(name = "size", defaultValue = "4") int size,
//                        @RequestParam(name = "keyword", defaultValue = "") String keyword) {
//        Page<Doctor> doctors = doctorRepository.findByNameContains(keyword, PageRequest.of(page, size));
//
//        doctors.getContent().forEach(doctor -> log.info("Doctor: {}", doctor));
//        model.addAttribute("doctors", doctors.getContent());
//        model.addAttribute("pages", new int[doctors.getTotalPages()]);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("keyword", keyword);
//        return "DoctorsList";
//    }



    @GetMapping("/DoctorsList")
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size,
                        @RequestParam(name = "id", defaultValue = "0") Long id) {
        Page<Doctor> doctors;

        if (id != 0) {
            doctors = doctorRepository.findById(id, PageRequest.of(page, size));
        } else {
            doctors = doctorRepository.findAll(PageRequest.of(page, size)); // Recherche tous les docteurs si id est 0
        }

        doctors.getContent().forEach(doctor -> log.info("Doctor: {}", doctor));
        model.addAttribute("doctors", doctors.getContent());
        model.addAttribute("pages", new int[doctors.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("id", id);
        return "DoctorsList";
    }





    @PostMapping("/saveDoctor")
    public String saveDoctor( Model model,@ModelAttribute Doctor doctor, @RequestParam("image") MultipartFile image) {
        try {
            // Si un fichier est téléchargé
            if (!image.isEmpty()) {
                // Enregistrez l'image sur le serveur dans le répertoire static/images
                String imageFileName = image.getOriginalFilename();
                Path path = Paths.get("src/main/resources/static/images/" + imageFileName); // Save in static/images/
                Files.copy(image.getInputStream(), path);
                doctor.setImagePath("/images/" + imageFileName); // Set relative image path


                // Enregistrez seulement le chemin relatif dans la base de données
                doctor.setImagePath(UPLOAD_DIR + imageFileName); // Set relative image path
            }
            User user = doctor.getUser();
            user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash the password

            user.setRole("ROLE_DOCTOR");
            // Enregistrez le médecin dans la base de données
            doctorRepository.save(doctor);
            // Reset the form (clear the Doctor instance for the next input)
            model.addAttribute("doctor", new Doctor());
            return "redirect:/DoctorsList"; // Rediriger vers la liste des médecins
        } catch (IOException e) {
            e.printStackTrace();
            return "error"; // Retourner une page d'erreur si quelque chose se passe mal
        }
    }



    @GetMapping("/deleteDoctor")
    public String delete(@RequestParam Long id, @RequestParam String keyword, @RequestParam int page) {
        doctorRepository.deleteById(id);
        return "redirect:/DoctorsList?page=" + page + "&keyword=" + keyword;
    }

    @GetMapping("/formDoctor")
    public String formDoctor(Model model) {
        model.addAttribute("doctor", new Doctor());
        return "formDoctor";
    }
//
//    @PostMapping("/saveDoctor")
//    public String saveDoctor(Model model, @Valid Doctor doctor, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "formDoctor"; // Return the form page if there are validation errors
//        }
//
//        // Ensure the User object is set for the doctor
//        if (doctor.getUser() == null) {
//            doctor.setUser(new User()); // Create a new User if not provided
//        }
//
//        // Hash the password before saving it
//        User user = doctor.getUser();
//        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
//            user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash the password
//        }
//
//        user.setRole("ROLE_DOCTOR"); // Assign the role to "doctor"
//
//        // Save the User first
//        userService.save(user);
//
//        // After saving the user, associate the user with the doctor
//        doctor.setUser(user);
//
//        // Save the Doctor entity
//        doctorRepository.save(doctor);
//
//        // Reset the form (clear the Doctor instance for the next input)
//        model.addAttribute("doctor", new Doctor());
//        return "redirect:/DoctorsList"; // Redirect to the list of doctors
//    }



    @GetMapping("/EditDoctor")
    public String EditDoctor(Model model, @RequestParam(name = "id") Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
        model.addAttribute("doctor", doctor);
        return "EditDoctor";
    }

//    @GetMapping("/EditDoctor")
//    public String EditPatients(Model model,@RequestParam(name = "id") Long id ){
//        Doctor doctor =doctorRepository.findById(id).get();
//        model.addAttribute("patient", patient);
//        return "EditDoctor";
//    }


    @GetMapping("/ConsultationDoctor")
    public String ConsultationDoctor(Model model, @RequestParam(name = "id") Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
        model.addAttribute("doctor", doctor);
        return "ConsultationDoctor";
    }

    @GetMapping("/DoctorsListForPatient")
    public String doctorsListForPatient(Model model,
                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                        @RequestParam(name = "size", defaultValue = "5") int size) {
        // Récupérer tous les docteurs avec pagination
        Page<Doctor> doctors = doctorRepository.findAll(PageRequest.of(page, size));

        // Ajouter les attributs au modèle pour Thymeleaf
        model.addAttribute("doctors", doctors.getContent());
        model.addAttribute("pages", new int[doctors.getTotalPages()]);
        model.addAttribute("currentPage", page);

        return "DoctorsListForPatient"; // Nom du fichier HTML à afficher
    }


}