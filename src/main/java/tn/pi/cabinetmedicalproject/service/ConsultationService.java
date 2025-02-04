package tn.pi.cabinetmedicalproject.service;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.pi.cabinetmedicalproject.model.Consultation;
import tn.pi.cabinetmedicalproject.model.Doctor;
import tn.pi.cabinetmedicalproject.repository.ConsultationRepository;
import tn.pi.cabinetmedicalproject.repository.DoctorRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@Service  // <-- Ajoute cette annotation pour déclarer le service comme un bean Spring
public class ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private DoctorRepository doctorRepository;

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





//    // Méthode pour récupérer un rendez-vous par son ID
//    public Consultation getConsultationById(Long id) {
//        return consultationRepository.findById(id).orElse(null);
//    }
//
//    // Méthode pour récupérer tous les rendez-vous
//    public List<Consultation> getAllConsultations() {
//        return consultationRepository.findAll();
//    }
}
