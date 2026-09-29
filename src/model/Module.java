package model;

import java.io.Serializable;
import java.util.Objects;

public class Module implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String code;
    private String nom;
    private double coefficient;
    private boolean obligatoire;

    public Module(String code,
                  String nom,
                  double coefficient,
                  boolean obligatoire) {

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException(
                    "Le code du module est obligatoire.");
        }

        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException(
                    "Le nom du module est obligatoire.");
        }

        if (coefficient <= 0) {
            throw new IllegalArgumentException(
                    "Le coefficient doit être strictement positif.");
        }

        this.code = code.trim().toUpperCase();
        this.nom = nom.trim();
        this.coefficient = coefficient;
        this.obligatoire = obligatoire;
    }

    public String getCode() {
        return code;
    }

    public String getNom() {
        return nom;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public boolean isObligatoire() {
        return obligatoire;
    }

    public void setNom(String nom) {

        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException(
                    "Le nom du module est obligatoire.");
        }

        this.nom = nom.trim();
    }

    public void setCoefficient(double coefficient) {

        if (coefficient <= 0) {
            throw new IllegalArgumentException(
                    "Le coefficient doit être strictement positif.");
        }

        this.coefficient = coefficient;
    }

    public void setObligatoire(boolean obligatoire) {
        this.obligatoire = obligatoire;
    }

    @Override
    public String toString() {

        return String.format(
                "%s - %s | Coeff: %.1f | %s",
                code,
                nom,
                coefficient,
                obligatoire
                        ? "OBLIGATOIRE"
                        : "OPTIONNEL"
        );
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Module)) {
            return false;
        }

        Module module = (Module) o;

        return code.equalsIgnoreCase(module.code);
    }

    @Override
    public int hashCode() {

        return Objects.hash(
                code.toUpperCase()
        );
    }
}