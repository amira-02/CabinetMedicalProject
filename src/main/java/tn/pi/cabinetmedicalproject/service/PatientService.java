package tn.pi.cabinetmedicalproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.pi.cabinetmedicalproject.model.Patient;
import tn.pi.cabinetmedicalproject.repository.PatientRepository;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    // Méthode pour enregistrer un patient
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);  // Enregistrer le patient dans la base de données
    }
}
