package tn.pi.cabinetmedicalproject.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tn.pi.cabinetmedicalproject.model.Consultation;
import tn.pi.cabinetmedicalproject.model.Doctor;
import tn.pi.cabinetmedicalproject.model.Patient;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findByPatientId(Long patientId);
        @Query("SELECT DISTINCT c.patient.id FROM Consultation c WHERE c.doctor.id = :doctor")
    List<Patient> findDistinctPatientsByDoctor(@Param("doctor") Doctor doctor);
    List<Consultation> findByDoctor(Doctor doctor);
    List<Consultation> findByPatient(Patient patient);
    List<Consultation> findByPatientAndDoctor(Patient patient, Doctor doctor);
    List<Consultation> findByDoctorId(Long doctorId);
    List<Consultation> findAllByDoctorId(Long doctorId);
    // Trouver des patients distincts pour un médec
    int countByPatientIdAndDoctorId(Long patientId, Long doctorId);
//    boolean existsByPatientAndDoctorAndDate(Patient patient, Doctor doctor);
Page<Consultation> findByPatientId(Long patientId, Pageable pageable);
}