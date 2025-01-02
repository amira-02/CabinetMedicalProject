package tn.pi.cabinetmedicalproject.model;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.Set;
@Entity
@Setter @Getter @NoArgsConstructor
@AllArgsConstructor @ToString @Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 4, max = 50)
    private String name;
    @Column(name = "telephone")
    private String telephone;  // Changez de 'int' à 'String'

    private LocalDate birthDate;
    private float height;
    private float weight;
    private boolean gender;
    private String pathology;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consultation> consultations = new ArrayList<>();

    @ManyToMany(mappedBy = "patients")
    private Set<Doctor> doctors;

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getTelephone() {
        return telephone;  // Changez le type de retour
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;  // Changez le type du paramètre
    }
}
