package tn.pi.cabinetmedicalproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.pi.cabinetmedicalproject.model.Patient;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository
        extends JpaRepository<Patient, Long> {

    Page<Patient> findByNameContains(String keyword, Pageable pageable);
    // Trouver les patients associés à un médecin par ID et filtrer par nom (case-insensitive)
    List<Patient> findByDoctors_IdAndNameContaining(Long doctorId, String name);
    // Recherche les patients associés à un médecin
    List<Patient> findByDoctors_Id(Long doctorId);
    Optional<Patient> findById(Long patientId);

}