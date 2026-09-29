package util;

import model.Etudiant;
import model.Module;
import model.Note;
import service.GestionSGU;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public final class ExportUtil {

    private ExportUtil() {
    }

    // ============================================================
    //                         EXPORT CSV
    // ============================================================

    public static void exporterEtudiantsCSV(
            GestionSGU gestion,
            String chemin
    ) throws IOException {

        try (
                PrintWriter writer =
                        new PrintWriter(
                                new FileWriter(chemin)
                        )
        ) {

            writer.println(
                    "Matricule,Nom,Prenom,DateNaissance"
            );

            for (Etudiant etudiant :
                    gestion.getEtudiantsTries()) {

                writer.println(
                        etudiant.getMatricule()
                                + ","
                                + etudiant.getNom()
                                + ","
                                + etudiant.getPrenom()
                                + ","
                                + etudiant.getDateNaissance()
                );
            }
        }
    }

    // ============================================================
    //                          EXPORT NOTES
    // ============================================================

    public static void exporterNotesCSV(
            GestionSGU gestion,
            String chemin
    ) throws IOException {

        try (
                PrintWriter writer =
                        new PrintWriter(
                                new FileWriter(chemin)
                        )
        ) {

            writer.println(
                    "Matricule,Module,Note,TypeControle"
            );

            for (Note note : gestion.getNotes()) {

                writer.println(
                        note.getMatriculeEtudiant()
                                + ","
                                + note.getCodeModule()
                                + ","
                                + note.getValeur()
                                + ","
                                + note.getTypeControle()
                );
            }
        }
    }

    // ============================================================
    //                        EXPORT MODULES
    // ============================================================

    public static void exporterModulesCSV(
            GestionSGU gestion,
            String chemin
    ) throws IOException {

        try (
                PrintWriter writer =
                        new PrintWriter(
                                new FileWriter(chemin)
                        )
        ) {

            writer.println(
                    "Code,Nom,Coefficient,Obligatoire"
            );

            for (Module module :
                    gestion.getModulesTries()) {

                writer.println(
                        module.getCode()
                                + ","
                                + module.getNom()
                                + ","
                                + module.getCoefficient()
                                + ","
                                + module.isObligatoire()
                );
            }
        }
    }
}