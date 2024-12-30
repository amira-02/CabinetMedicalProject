package tn.pi.cabinetmedicalproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.pi.cabinetmedicalproject.model.Doctor;
import tn.pi.cabinetmedicalproject.model.Patient;


public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    // Méthode pour rechercher un docteur par nom, avec pagination
    Page<Doctor> findByNameContains(String keyword, Pageable pageable);

    // Méthode pour rechercher par spécialité, avec pagination
    Page<Doctor> findBySpecialtyContains(String keyword, Pageable pageable);
    Page<Doctor> findById(Long id, Pageable pageable);
}
