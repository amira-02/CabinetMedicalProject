package tn.pi.cabinetmedicalproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.pi.cabinetmedicalproject.enums.AppointmentStatus;
import tn.pi.cabinetmedicalproject.model.Consultation;
import tn.pi.cabinetmedicalproject.model.Doctor;
import tn.pi.cabinetmedicalproject.model.Patient;
import tn.pi.cabinetmedicalproject.repository.ConsultationRepository;
import tn.pi.cabinetmedicalproject.repository.DoctorRepository;
import tn.pi.cabinetmedicalproject.repository.PatientRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Transactional
    public void saveConsultation(Consultation consultation) {
        consultationRepository.save(consultation);
    }

    // Méthode pour récupérer les consultations d'un patient par ID avec pagination
    public Page<Consultation> findConsultationsByPatientId(Long patientId, Pageable pageable) {
        return consultationRepository.findByPatientId(patientId, pageable);
    }

    public boolean hasExistingConsultation(Long patientId, Long doctorId) {
        return consultationRepository.countByPatientIdAndDoctorId(patientId, doctorId) >= 1;
    }

    public List<Consultation> findByPatientAndDoctor(Patient patient, Doctor doctor) {
        return consultationRepository.findByPatientAndDoctor(patient, doctor);
    }

    public List<Consultation> findByDoctor(Doctor doctor) {
        return consultationRepository.findByDoctor(doctor);
    }

    public Consultation getConsultationById(Long id) {
        return consultationRepository.findById(id).orElse(null);
    }

    public List<Consultation> getAllConsultations() {
        return consultationRepository.findAll();
    }

    // Récupérer les consultations programmées d'un patient par son ID avec statut SCHEDULED et pagination
    public Page<Consultation> findConsultationsByPatientIdAndStatus(Long patientId, AppointmentStatus status, Pageable pageable) {
        return consultationRepository.findByPatientIdAndStatus(patientId, status, pageable);
    }

    // Méthode pour récupérer les rendez-vous programmés pour un patient avec pagination
    public Page<Consultation> getScheduledAppointmentsForPatient(Long patientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return consultationRepository.findByPatientIdAndStatus(patientId, AppointmentStatus.SCHEDULED, pageable);
    }
}
