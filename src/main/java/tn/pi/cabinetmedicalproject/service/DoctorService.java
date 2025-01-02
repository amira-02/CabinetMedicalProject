package tn.pi.cabinetmedicalproject.service;



import tn.pi.cabinetmedicalproject.model.Doctor;
import tn.pi.cabinetmedicalproject.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.pi.cabinetmedicalproject.repository.UserRepository;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;
    private UserRepository userRepository;

    // Recherche de docteurs avec pagination et filtrage par nom
    public Page<Doctor> searchDoctorsByName(String keyword, Pageable pageable) {
        return doctorRepository.findByNameContains(keyword, pageable);
    }

    // Recherche de docteurs avec pagination et filtrage par spécialité
    public Page<Doctor> searchDoctorsBySpecialty(String keyword, Pageable pageable) {
        return doctorRepository.findBySpecialtyContains(keyword, pageable);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();  // Utilisation de la méthode de JPA pour récupérer tous les docteurs
    }
    public Doctor saveDoctor(Doctor doctor) {
        // Vérifiez si l'utilisateur est déjà enregistré
        if (doctor.getUser() != null && doctor.getUser().getId() == null) {
            userRepository.save(doctor.getUser());  // Sauvegarder l'utilisateur si nécessaire
        }

        // Sauvegarder le docteur
        return doctorRepository.save(doctor);
    }

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }
    public Doctor findById(Long id) {
        return doctorRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

}
