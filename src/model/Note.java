package model;

import java.io.Serializable;
import java.util.Objects;

public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String matriculeEtudiant;
    private final String codeModule;

    private double valeur;
    private TypeControle typeControle;

    public Note(String matriculeEtudiant,
                String codeModule,
                double valeur,
                TypeControle typeControle) {

        if (matriculeEtudiant == null ||
                matriculeEtudiant.isBlank()) {

            throw new IllegalArgumentException(
                    "Le matricule est obligatoire.");
        }

        if (codeModule == null ||
                codeModule.isBlank()) {

            throw new IllegalArgumentException(
                    "Le code du module est obligatoire.");
        }

        if (typeControle == null) {
            throw new IllegalArgumentException(
                    "Le type de contrôle est obligatoire.");
        }

        setValeur(valeur);

        this.matriculeEtudiant =
                matriculeEtudiant.trim().toUpperCase();

        this.codeModule =
                codeModule.trim().toUpperCase();

        this.typeControle = typeControle;
    }

    public String getMatriculeEtudiant() {
        return matriculeEtudiant;
    }

    public String getCodeModule() {
        return codeModule;
    }

    public double getValeur() {
        return valeur;
    }

    public TypeControle getTypeControle() {
        return typeControle;
    }

    public void setValeur(double valeur) {

        if (valeur < 0 || valeur > 20) {

            throw new IllegalArgumentException(
                    "La note doit être comprise entre 0 et 20."
            );
        }

        this.valeur = valeur;
    }

    public void setTypeControle(TypeControle typeControle) {

        if (typeControle == null) {

            throw new IllegalArgumentException(
                    "Le type de contrôle est obligatoire."
            );
        }

        this.typeControle = typeControle;
    }

    public String getKey() {

        return matriculeEtudiant
                + "|"
                + codeModule
                + "|"
                + typeControle;
    }

    @Override
    public String toString() {

        return String.format(
                "%s | %.2f | %s",
                codeModule,
                valeur,
                typeControle
        );
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Note)) {
            return false;
        }

        Note note = (Note) o;

        return getKey().equals(note.getKey());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getKey());
    }
}