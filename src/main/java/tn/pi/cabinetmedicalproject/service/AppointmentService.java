package tn.pi.cabinetmedicalproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.pi.cabinetmedicalproject.model.Appointments;
import tn.pi.cabinetmedicalproject.repository.AppointmentRepository;

import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Méthode pour enregistrer un rendez-vous
    public void saveAppointment(Appointments appointment) {
        appointmentRepository.save(appointment);  // Utilise le repository pour sauvegarder le rendez-vous
    }

    // Méthode pour récupérer un rendez-vous par son ID
    public Appointments getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);  // Renvoie le rendez-vous ou null si pas trouvé
    }

    // Méthode pour récupérer tous les rendez-vous
    public List<Appointments> getAllAppointments() {
        return appointmentRepository.findAll();  // Récupère tous les rendez-vous de la base de données
    }
}
