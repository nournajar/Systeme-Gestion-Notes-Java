package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Etudiant implements Comparable<Etudiant>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final String matricule;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;

    public Etudiant(String matricule,
                    String nom,
                    String prenom,
                    LocalDate dateNaissance) {

        if (matricule == null || matricule.isBlank()) {
            throw new IllegalArgumentException(
                    "Le matricule est obligatoire.");
        }

        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException(
                    "Le nom est obligatoire.");
        }

        if (prenom == null || prenom.isBlank()) {
            throw new IllegalArgumentException(
                    "Le prénom est obligatoire.");
        }

        if (dateNaissance == null ||
                dateNaissance.isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "La date de naissance est invalide.");
        }

        this.matricule = matricule.trim().toUpperCase();
        this.nom = nom.trim();
        this.prenom = prenom.trim();
        this.dateNaissance = dateNaissance;
    }

    public String getMatricule() {
        return matricule;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setNom(String nom) {

        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException(
                    "Le nom est obligatoire.");
        }

        this.nom = nom.trim();
    }

    public void setPrenom(String prenom) {

        if (prenom == null || prenom.isBlank()) {
            throw new IllegalArgumentException(
                    "Le prénom est obligatoire.");
        }

        this.prenom = prenom.trim();
    }

    public void setDateNaissance(LocalDate dateNaissance) {

        if (dateNaissance == null ||
                dateNaissance.isAfter(LocalDate.now())) {

            throw new IllegalArgumentException(
                    "La date de naissance est invalide.");
        }

        this.dateNaissance = dateNaissance;
    }

    @Override
    public int compareTo(Etudiant autre) {
        return this.matricule.compareToIgnoreCase(
                autre.matricule
        );
    }

    @Override
    public String toString() {

        return String.format(
                "%s | %s %s | Né(e) le %s",
                matricule,
                nom,
                prenom,
                dateNaissance.format(FORMAT)
        );
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Etudiant)) {
            return false;
        }

        Etudiant etudiant = (Etudiant) o;

        return matricule.equalsIgnoreCase(
                etudiant.matricule
        );
    }

    @Override
    public int hashCode() {

        return Objects.hash(
                matricule.toUpperCase()
        );
    }
}