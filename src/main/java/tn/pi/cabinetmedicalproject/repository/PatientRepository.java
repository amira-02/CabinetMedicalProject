package tn.pi.cabinetmedicalproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.pi.cabinetmedicalproject.model.Patient;

@Repository
public interface PatientRepository
        extends JpaRepository<Patient, Long> {

    Page<Patient> findByNameContains(String keyword, Pageable pageable);
}