package tn.pi.cabinetmedicalproject.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Setter @Getter @NoArgsConstructor
@AllArgsConstructor @ToString @Builder
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 4, max = 50)
    private String name;
    private int Telephone;
    private String specialty;

    private String address;

    private int postalCode;

    private String city;

    private int phone;



    // Relationship to Consultation, assuming a doctor can have many consultations
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consultation> consultations = new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name = "doctor_patient",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id"))
    private Set<Patient> patients;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;






}