package tn.pi.cabinetmedicalproject.web.dto;

import java.time.LocalDate;

public class UserRegistrationDto {

    private String name;
    private String email;
    private String password;
    private String role;

    // Nouveaux champs ajoutés pour le patient
    private String telephone;
    private LocalDate birthDate;
    private double height;
    private double weight;
    private String pathology; // Ajoutez gender si vous souhaitez l'utiliser également
    private String gender;

    // Constructeurs
    public UserRegistrationDto() {}

    public UserRegistrationDto(String name, String email, String password, String role,
                               String telephone, LocalDate birthDate, double height, double weight, String pathology, String gender) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.telephone = telephone;
        this.birthDate = birthDate;
        this.height = height;
        this.weight = weight;
        this.pathology = pathology;
        this.gender = gender;
    }

    // Getters et setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getPathology() {
        return pathology;
    }

    public void setPathology(String pathology) {
        this.pathology = pathology;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
