package tn.pi.cabinetmedicalproject.model;

import javax.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import tn.pi.cabinetmedicalproject.enums.AppointmentStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;
    // Date of the consultation

    @Lob
    private String description;  // Description of the consultation

    @Lob
    private String prescription;  // Prescription details

    @Lob
    private String allergies;  // Allergies details (in English)

    @Lob
    private String currentTreatments;  // Current treatments (in English)

    @Lob
    private String medicalHistory;  // Medical history (in English)
    private String pathology;
    private float price;

    private LocalTime time;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;


    public Patient getPatient() {
        return this.patient;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate appointmentDate) {
        this.date = appointmentDate;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime appointmentTime) {
        this.time =appointmentTime;
    }


    public String getPathology() {
        return pathology;
    }

    public void setPathology(String pathology) {
        this.pathology = pathology;
    }

}
