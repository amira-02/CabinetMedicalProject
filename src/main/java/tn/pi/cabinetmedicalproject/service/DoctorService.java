package tn.pi.cabinetmedicalproject.service;



import tn.pi.cabinetmedicalproject.model.Doctor;
import tn.pi.cabinetmedicalproject.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

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
}
