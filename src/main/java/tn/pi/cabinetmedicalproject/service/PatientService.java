package tn.pi.cabinetmedicalproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.pi.cabinetmedicalproject.model.Patient;
import tn.pi.cabinetmedicalproject.model.User;
import tn.pi.cabinetmedicalproject.repository.PatientRepository;
import tn.pi.cabinetmedicalproject.repository.UserRepository;
import tn.pi.cabinetmedicalproject.web.dto.UserRegistrationDto;

import java.util.List;

@Service
public class PatientService {



    // Fetch the patient by their email address
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    // Méthode pour obtenir tous les patients
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();  // Utiliser la méthode de repository pour récupérer les patients
    }
    // Method to find a patient by the associated user
    public Patient findByUser(User user) {
        return patientRepository.findByUser(user);
    }
     //Méthode pour sauvegarder un patient à partir d'un objet Patient
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient); // Enregistre le Patient dans la base de données
    }


    public Patient findByEmail(String email) {
        return patientRepository.findByUserEmail(email);  // This is the correct method to call
    }
    public Patient findByUserId(Long userId) {
        return patientRepository.findByUserId(userId);
    }


    // Méthode pour récupérer un patient par son ID
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElse(null); // Renvoie le patient ou null si pas trouvé
    }


    public List<Patient> findAllPatients() {
        return patientRepository.findAll();
    }
}
