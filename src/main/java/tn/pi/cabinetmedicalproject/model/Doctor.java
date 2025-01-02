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

    private String specialty;

    private String address;

    private int postalCode;

    private String city;

    private int phone;

   private String Description;

    // Relationship to Consultation, assuming a doctor can have many consultations
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consultation> consultations = new ArrayList<>();


    @ManyToMany
    @JoinTable(
            name = "doctor_patient",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "patient_id"))
    private Set<Patient> patients;

    @ManyToOne(cascade = CascadeType.PERSIST)  // Cascade persist to automatically persist User if not already saved
    @JoinColumn(name = "user_id", nullable = false)  // Foreign key to User entity, nullable = false ensures it's required
    private User user;



private String imagePath;

    // Getters et Setters
    public Long getId() {
        return id;
    }
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        this.Description = description;
    }
}