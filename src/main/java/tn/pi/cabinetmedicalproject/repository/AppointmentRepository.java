//package tn.pi.cabinetmedicalproject.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import tn.pi.cabinetmedicalproject.model.Appointments;
//import tn.pi.cabinetmedicalproject.model.Patient;
//import tn.pi.cabinetmedicalproject.model.Doctor;
//
//import java.time.LocalDate;
//import java.util.List;
//
//public interface AppointmentRepository extends JpaRepository<Appointments, Long> {
//
//
//    List<Appointments> findByPatient(Patient patient);
//    List<Appointments> findByDoctor(Doctor doctor);
//    List<Appointments> findByDoctorId(Long doctorId);
//}
