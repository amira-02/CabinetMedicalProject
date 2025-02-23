package tn.pi.cabinetmedicalproject.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 4, max = 50)
    private String name;

    @Column(name = "telephone")
    private String telephone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private float height;
    private float weight;

    @Column(nullable = false)
    private String gender;




    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consultation> consultations = new ArrayList<>();

    @ManyToMany(mappedBy = "patients")
    private Set<Doctor> doctors;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    // One-to-one relationship with User

    // Constructors
    public Patient() {}

    public Patient(Long id, String name, String telephone, LocalDate birthDate, float height, float weight,
                   String gender, String pathology, User user) {
        this.id = id;
        this.name = name;
        this.telephone = telephone;
        this.birthDate = birthDate;
        this.height = height;
        this.weight = weight;
        this.gender = gender;

        this.user = user;
    }
    public String getEmail() {
        return user != null ? user.getEmail() : null; // Retourne l'email de l'utilisateur lié
    }
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }



    public List<Consultation> getConsultations() {
        return consultations;
    }

    public void setConsultations(List<Consultation> consultations) {
        this.consultations = consultations;
    }

    public Set<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(Set<Doctor> doctors) {
        this.doctors = doctors;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
