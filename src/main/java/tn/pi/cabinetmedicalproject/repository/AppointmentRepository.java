package tn.pi.cabinetmedicalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.pi.cabinetmedicalproject.model.Appointments;
import tn.pi.cabinetmedicalproject.model.Patient;
import tn.pi.cabinetmedicalproject.model.Doctor;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointments, Long> {

    // Rechercher les rendez-vous par patient
    List<Appointments> findByPatient(Patient patient);

    // Rechercher les rendez-vous par médecin
    List<Appointments> findByDoctor(Doctor doctor);

    // Rechercher les rendez-vous pour un médecin et une date spécifique
    List<Appointments> findByDoctorAndAppointmentDate(Doctor doctor, LocalDate appointmentDate);
}
