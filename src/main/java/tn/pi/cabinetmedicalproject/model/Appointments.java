package tn.pi.cabinetmedicalproject.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Entity
public class Appointments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id") // La colonne "doctor_id" doit exister dans la table Appointments
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id") // Optionnel : relation avec Patient
    private Patient patient;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    // Getter for 'date'
//    public Date getDate() {
//        return date;
//    }
//
//    // Setter for 'date'
//    public void setDate(Date date) {
//        this.date = date;
//    }
    // Getters et setters
//    public LocalDateTime getTime() {
//        return time;
//    }
//
//    public void setTime(LocalDateTime time) {
//        this.time = time;
//    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
    public Long getPatientId() {
        return this.patient != null ? this.patient.getId() : null;
    }
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}
