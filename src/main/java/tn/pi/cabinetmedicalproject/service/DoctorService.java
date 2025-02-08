package tn.pi.cabinetmedicalproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tn.pi.cabinetmedicalproject.model.Doctor;
import tn.pi.cabinetmedicalproject.model.User;
import tn.pi.cabinetmedicalproject.repository.DoctorRepository;

import java.util.List;
@Service
public class DoctorService {


    @Autowired
    private DoctorRepository doctorRepository;
   // Assuming you have a DoctorRepository

    public Doctor getLoggedInDoctor() {
        // Get the username of the logged-in user
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail();

        // Find the doctor by user (or by username, depending on your design)
        return doctorRepository.findByUserEmail(username); // Assuming there's a method like this in your repository
    }
    // Méthode dans le service pour récupérer un docteur par son email
    public Doctor findByEmail(String email) {
        return doctorRepository.findByUserEmail(email);  // Appel au repository
    }
    // Recherche de docteurs avec pagination et filtrage par nom
    public Page<Doctor> searchDoctorsByName(String keyword, Pageable pageable) {
        return doctorRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    // Recherche de docteurs avec pagination et filtrage par spécialité
    public Page<Doctor> searchDoctorsBySpecialty(String keyword, Pageable pageable) {
        return doctorRepository.findBySpecialtyContains(keyword, pageable);
    }
    public Doctor findDoctorByEmail(String email) {
        return doctorRepository.findByUserEmail(email);  // Assurez-vous que cette méthode retourne un doctor valide
    }

    // Obtenir tous les docteurs
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();  // Utilisation de la méthode de JPA pour récupérer tous les docteurs
    }

    // Sauvegarder un docteur
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    // Trouver un docteur par son ID
    public Doctor findById(Long id) {
        return doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
    }
}
