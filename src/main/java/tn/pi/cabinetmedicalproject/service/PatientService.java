package tn.pi.cabinetmedicalproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.pi.cabinetmedicalproject.model.Patient;
import tn.pi.cabinetmedicalproject.model.User;
import tn.pi.cabinetmedicalproject.repository.PatientRepository;
import tn.pi.cabinetmedicalproject.repository.UserRepository;
import tn.pi.cabinetmedicalproject.web.dto.UserRegistrationDto;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

     //Méthode pour sauvegarder un patient à partir d'un objet Patient
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient); // Enregistre le Patient dans la base de données
    }

    // Méthode pour enregistrer un patient à partir d'un DTO
//    public Patient savePatient(UserRegistrationDto registrationDto) {
//        // Crée un nouvel utilisateur à partir des données du DTO
//        User user = new User();
//        user.setEmail(registrationDto.getEmail());
//        user.setPassword(registrationDto.getPassword());
//        user.setRole("USER_ROLE"); // Définir le rôle par défaut
//
//        // Sauvegarde de l'utilisateur dans la base de données
//        user = userRepository.save(user);
//
//        // Crée un objet Patient et associe-le à l'utilisateur
//        Patient patient = new Patient();
//        patient.setUser(user); // Associe l'utilisateur au patient
//        patient.setTelephone(registrationDto.getTelephone());
//        patient.setBirthDate(registrationDto.getBirthDate());
//        patient.setHeight((float) registrationDto.getHeight());
//        patient.setWeight((float) registrationDto.getWeight());
//        patient.setGender(registrationDto.getGender());
//
//        // Sauvegarde du patient dans la base de données
//        return patientRepository.save(patient);
//    }

    // Méthode pour récupérer un patient par son ID
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElse(null); // Renvoie le patient ou null si pas trouvé
    }
}
