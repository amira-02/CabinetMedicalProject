package tn.pi.cabinetmedicalproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.pi.cabinetmedicalproject.model.Appointments;
import tn.pi.cabinetmedicalproject.model.Patient;
import tn.pi.cabinetmedicalproject.repository.AppointmentRepository;

import java.util.List;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;


    // Method to get all appointments
    public List<Appointments> getAllAppointments() {
        return appointmentRepository.findAll();
    }
    // Méthode pour obtenir les rendez-vous d'un patient donné
    public List<Appointments> findAppointmentsByPatient(Patient patient) {
        return appointmentRepository.findByPatient(patient);
    }
    public Appointments saveAppointment(Appointments appointment) {
        return appointmentRepository.save(appointment); // Enregistre l'entité Appointment dans la base de données
    }

}
