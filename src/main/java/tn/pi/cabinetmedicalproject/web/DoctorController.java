package tn.pi.cabinetmedicalproject.web;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.pi.cabinetmedicalproject.model.Consultation;
import tn.pi.cabinetmedicalproject.model.Doctor;
import tn.pi.cabinetmedicalproject.model.Patient;
import tn.pi.cabinetmedicalproject.model.User;
import tn.pi.cabinetmedicalproject.repository.ConsultationRepository;
import tn.pi.cabinetmedicalproject.repository.DoctorRepository;
import tn.pi.cabinetmedicalproject.repository.PatientRepository;
import tn.pi.cabinetmedicalproject.repository.UserRepository;
import tn.pi.cabinetmedicalproject.service.DoctorService;
import tn.pi.cabinetmedicalproject.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j

public class DoctorController {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private UserRepository userRepository;

    private final DoctorRepository doctorRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private static final String UPLOAD_DIR = "/images/";
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientRepository patientRepository;
    public DoctorController(DoctorRepository doctorRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/doctor/patients")
    public String getUniquePatientsForDoctor(Model model, Principal principal) {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Trouver le médecin associé à cet utilisateur
        Doctor doctor = doctorRepository.findByUserEmail(username);
        if (doctor == null) {
            model.addAttribute("consultation", Collections.emptyList());
            return "doctorhome"; // Rediriger vers la page sans consultations
        }

        // Utiliser la requête personnalisée pour récupérer les patients distincts
        List<Patient> distinctPatients = consultationRepository.findDistinctPatientsByDoctor(doctor);

        // Vérifier si la liste est vide
        if (distinctPatients.isEmpty()) {
            model.addAttribute("message", "Aucun patient trouvé.");
        }

        // Ajouter les patients distincts au modèle
        model.addAttribute("patients", distinctPatients);
        model.addAttribute("doctor", doctor);
        return "doctorhome"; // Affichage Thymeleaf
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

//
//
//    @GetMapping("/doctor/patients")
//    public String getUniquePatientsForDoctor(Model model) {
//        // Récupérer l'utilisateur connecté (doctor)
//        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long doctorId = Long.parseLong(principal.getEmail()); // Si l'ID du médecin est stocké dans le nom d'utilisateur
//
//        // Récupérer toutes les consultations du médecin avec l'ID doctorId
//        List<Consultation> consultations = consultationRepository.findAllByDoctorId(doctorId);
//
//        // Créer un Set pour garantir l'unicité des patients
//        Set<Patient> uniquePatients = new HashSet<>();
//
//        // Ajouter chaque patient de chaque consultation au Set pour filtrer les doublonsx  x
//        for (Consultation consultation : consultations) {
//            uniquePatients.add(consultation.getPatient());
//        }
//
//        // Ajouter la liste des patients uniques au modèle
//        model.addAttribute("uniquePatients", uniquePatients);
//
//        // Retourner le nom de la vue
//        return "doctorhome";
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
            return "redirect:/admin"; // Rediriger vers la liste des médecins
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