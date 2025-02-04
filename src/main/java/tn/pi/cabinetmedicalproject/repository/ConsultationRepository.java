package tn.pi.cabinetmedicalproject.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tn.pi.cabinetmedicalproject.model.Consultation;
import tn.pi.cabinetmedicalproject.model.Doctor;
import tn.pi.cabinetmedicalproject.model.Patient;

import java.util.List;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {
    List<Consultation> findByPatientId(Long patientId);


    List<Consultation> findByPatient(Patient patient);
    List<Consultation> findByDoctor(Doctor doctor);
    List<Consultation> findByDoctorId(Long doctorId);

}