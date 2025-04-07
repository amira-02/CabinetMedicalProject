package tn.pi.cabinetmedicalproject.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.util.logging.Logger;

@Service  // <-- Ajoute cette annotation pour déclarer le service comme un bean Spring
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
//
//    public List<Consultation> findByPatientId(Long patientId) {
//        return consultationRepository.findByPatientId(patientId);
//    }


    public Page<Consultation> findConsultationsByPatientId(Long patientId, Pageable pageable) {
        return consultationRepository.findByPatientId(patientId, pageable);
    }
    public boolean hasExistingConsultation(Long patientId, Long doctorId) {
        return consultationRepository.countByPatientIdAndDoctorId(patientId, doctorId) >= 1;
    }

public List<Consultation> findByPatientAndDoctor(Patient patient, Doctor doctor) {
    // Requête pour récupérer les consultations du patient avec ce médecin
    return consultationRepository.findByPatientAndDoctor(patient, doctor);
}



    public List<Consultation> findByDoctor(Doctor doctor) {
        return consultationRepository.findByDoctor(doctor);
    }


//    // Méthode pour récupérer un rendez-vous par son ID
    public Consultation getConsultationById(Long id) {
        return consultationRepository.findById(id).orElse(null);
    }
//
//    // Méthode pour récupérer tous les rendez-vous
    public List<Consultation> getAllConsultations() {
        return consultationRepository.findAll();
    }
}
