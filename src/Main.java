import exception.DuplicateEntityException;
import exception.EtudiantNotFoundException;
import exception.ModuleNotFoundException;
import java.time.LocalDate;
import model.Etudiant;
import model.Module;
import model.Note;
import model.TypeControle;
import service.GestionSGU;
import ui.MenuConsole;

public class Main {

    public static void main(String[] args) {

        GestionSGU gestion =
                new GestionSGU();

        // ========================================================
        // DONNEES DE TEST
        // ========================================================

        try {

            // -------------------------
            // ETUDIANTS
            // -------------------------

            gestion.ajouterEtudiant(
                    new Etudiant(
                            "ET123",
                            "Ben Salah",
                            "Amal",
                            LocalDate.of(
                                    2003,
                                    5,
                                    12
                            )
                    )
            );

            gestion.ajouterEtudiant(
                    new Etudiant(
                            "ET124",
                            "Trabelsi",
                            "Ahmed",
                            LocalDate.of(
                                    2002,
                                    8,
                                    20
                            )
                    )
            );

            gestion.ajouterEtudiant(
                    new Etudiant(
                            "ET125",
                            "Mansour",
                            "Sara",
                            LocalDate.of(
                                    2003,
                                    2,
                                    10
                            )
                    )
            );

            // -------------------------
            // MODULES
            // -------------------------

            gestion.ajouterModule(
                    new Module(
                            "PROG1",
                            "Programmation",
                            3,
                            true
                    )
            );

            gestion.ajouterModule(
                    new Module(
                            "RESEAUX",
                            "Réseaux",
                            2,
                            true
                    )
            );

            gestion.ajouterModule(
                    new Module(
                            "BD",
                            "Base de données",
                            2,
                            true
                    )
            );

            gestion.ajouterModule(
                    new Module(
                            "WEB",
                            "Développement Web",
                            2,
                            false
                    )
            );

            // -------------------------
            // NOTES AMAL
            // -------------------------

            gestion.ajouterNote(
                    new Note(
                            "ET123",
                            "PROG1",
                            15.5,
                            TypeControle.EXAMEN
                    )
            );

            gestion.ajouterNote(
                    new Note(
                            "ET123",
                            "RESEAUX",
                            12,
                            TypeControle.EXAMEN
                    )
            );

            gestion.ajouterNote(
                    new Note(
                            "ET123",
                            "BD",
                            16,
                            TypeControle.EXAMEN
                    )
            );

            // -------------------------
            // NOTES AHMED
            // -------------------------

            gestion.ajouterNote(
                    new Note(
                            "ET124",
                            "PROG1",
                            13,
                            TypeControle.EXAMEN
                    )
            );

            gestion.ajouterNote(
                    new Note(
                            "ET124",
                            "RESEAUX",
                            14,
                            TypeControle.EXAMEN
                    )
            );

            gestion.ajouterNote(
                    new Note(
                            "ET124",
                            "BD",
                            11,
                            TypeControle.EXAMEN
                    )
            );

            // -------------------------
            // NOTES SARA
            // -------------------------

            gestion.ajouterNote(
                    new Note(
                            "ET125",
                            "PROG1",
                            17,
                            TypeControle.EXAMEN
                    )
            );

            gestion.ajouterNote(
                    new Note(
                            "ET125",
                            "RESEAUX",
                            9,
                            TypeControle.EXAMEN
                    )
            );

            gestion.ajouterNote(
                    new Note(
                            "ET125",
                            "BD",
                            15,
                            TypeControle.EXAMEN
                    )
            );

        } catch (DuplicateEntityException | EtudiantNotFoundException | ModuleNotFoundException e) {

            System.out.println(
                    "Erreur lors du chargement "
                            + "des données de test : "
                            + e.getMessage()
            );
        }

        // ========================================================
        // LANCEMENT DE L'APPLICATION
        // ========================================================

        MenuConsole menu =
                new MenuConsole(gestion);

        menu.lancer();
    }
}