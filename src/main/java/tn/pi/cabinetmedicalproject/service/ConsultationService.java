package tn.pi.cabinetmedicalproject.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
//    public Doctor getDoctorByEmail(String email) {
//        return doctorRepository.findByUserEmail(email);
//    }
//
//    public List<Consultation> getConsultationsForDoctor(Long doctorId) {
//        return consultationRepository.findByDoctorId(doctorId);
//    }
public List<Consultation> findByPatientAndDoctor(Patient patient, Doctor doctor) {
    // Requête pour récupérer les consultations du patient avec ce médecin
    return consultationRepository.findByPatientAndDoctor(patient, doctor);
}

//    @Transactional
//    public void updateConsultationAndPatient(Long consultationId, Long patientId, String description, String prescription,
//                                             String allergies, String currentTreatments, String medicalHistory,
//                                             float height, float weight) {
//        Consultation consultation = consultationRepository.findById(consultationId)
//                .orElseThrow(() -> new RuntimeException("Consultation non trouvée"));
//
//        Patient patient = patientRepository.findById(patientId)
//                .orElseThrow(() -> new RuntimeException("Patient non trouvé"));
//
//        // Mettre à jour la consultation
//        consultation.setDescription(description);
//        consultation.setPrescription(prescription);
//        consultation.setAllergies(allergies);
//        consultation.setCurrentTreatments(currentTreatments);
//        consultation.setMedicalHistory(medicalHistory);
//        consultation.setStatus(AppointmentStatus.PENDING_PAYMENT); // Mettre le statut à "PENDING_PAYMENT"
//
//        // Mettre à jour la taille et le poids du patient
//        patient.setHeight(height);
//        patient.setWeight(weight);
//
//        // Sauvegarde dans la base de données
//        consultationRepository.save(consultation);
//        patientRepository.save(patient);
//    }

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
