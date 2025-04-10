package tn.pi.cabinetmedicalproject.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.pi.cabinetmedicalproject.model.Doctor;
import tn.pi.cabinetmedicalproject.model.Patient;

import java.util.Optional;


public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Doctor findByname(String name);
    // Méthode pour rechercher un docteur par nom, avec pagination
    Page<Doctor> findByNameContains(String keyword, Pageable pageable);
    Optional<Doctor> findByUserId(Long userId);
    // Méthode pour rechercher par spécialité, avec pagination
    Page<Doctor> findBySpecialtyContains(String keyword, Pageable pageable);
    Page<Doctor> findById(Long id, Pageable pageable);
    Page<Doctor> findByNameContainingIgnoreCase(String name, Pageable pageable);
    // Trouver un médecin basé sur l'email de l'utilisateur connecté
    Doctor findByUserEmail(String email);
    // Recherche par email de l'utilisateur
    Page<Doctor> findByNameContaining(String name, Pageable pageable);




    Doctor findByName(String name);  // Recherche un docteur par son nom

}
