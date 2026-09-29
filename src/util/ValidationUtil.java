package util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public final class ValidationUtil {

    private ValidationUtil() {
    }

    public static boolean estNombre(
            String valeur
    ) {

        if (valeur == null || valeur.isBlank()) {
            return false;
        }

        try {
            Double.parseDouble(valeur);
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static double lireNombre(
            String valeur
    ) {

        if (!estNombre(valeur)) {
            throw new IllegalArgumentException(
                    "La valeur doit être un nombre."
            );
        }

        return Double.parseDouble(
                valeur.trim()
        );
    }

    public static LocalDate lireDate(
            String valeur
    ) {

        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException(
                    "La date est obligatoire."
            );
        }

        try {

            return LocalDate.parse(
                    valeur.trim()
            );

        } catch (DateTimeParseException e) {

            throw new IllegalArgumentException(
                    "Format de date invalide. "
                            + "Utilisez yyyy-MM-dd."
            );
        }
    }

    public static boolean estVide(
            String valeur
    ) {

        return valeur == null ||
                valeur.isBlank();
    }
}