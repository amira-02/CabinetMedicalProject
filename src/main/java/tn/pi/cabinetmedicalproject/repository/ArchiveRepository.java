package tn.pi.cabinetmedicalproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.pi.cabinetmedicalproject.enums.AppointmentStatus;
import tn.pi.cabinetmedicalproject.model.Archive;

import java.util.List;

public interface ArchiveRepository extends JpaRepository<Archive, Long> {
    List<Archive> findByPatientIdAndStatus(Long patientId, AppointmentStatus status);

}
