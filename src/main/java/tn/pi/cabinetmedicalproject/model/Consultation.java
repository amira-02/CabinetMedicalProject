package tn.pi.cabinetmedicalproject.model;

import javax.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    @Column(nullable = false)
    private LocalDate date;  // Date of the consultation

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

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)  // Foreign key linking to Patient
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")  // Reference to the Doctor entity
    private Doctor doctor;

}
