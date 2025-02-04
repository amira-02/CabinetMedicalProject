package tn.pi.cabinetmedicalproject.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.pi.cabinetmedicalproject.model.Patient;
import tn.pi.cabinetmedicalproject.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository
        extends JpaRepository<Patient, Long> {



        // Correct method to find Patient by the associated User's email
        Patient findByUserEmail(String email);

    Patient findByUser(User user);
    Page<Patient> findByNameContains(String keyword, Pageable pageable);
    // Trouver les patients associés à un médecin par ID et filtrer par nom (case-insensitive)
    List<Patient> findByDoctors_IdAndNameContaining(Long doctorId, String name);
    // Recherche les patients associés à un médecin
    List<Patient> findByDoctors_Id(Long doctorId);
    Optional<Patient> findById(Long patientId);
    // Méthode pour trouver un patient en fonction de l'id de l'utilisateur

    @Query("SELECT p FROM Patient p WHERE p.user.id = :userId")
    Patient findByUserId(@Param("userId") Long userId);
  // Adjust according to the field name in your Patient entity




}

