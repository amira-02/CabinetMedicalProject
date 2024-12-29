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
    private int Telephone;
    private String mail;
    private LocalDate BirthDate;
    private float Height;
    private float Weight;
    private boolean Gender;
    private String Pathology;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consultation> consultations = new ArrayList<>();


    @ManyToMany(mappedBy = "patients")
    private Set<Doctor> doctors;

}