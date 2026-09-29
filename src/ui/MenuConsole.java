package ui;

import exception.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import model.*;
import model.Module;
import service.GestionSGU;
import util.ExportUtil;
import util.ValidationUtil;

public class MenuConsole {

    private final GestionSGU gestion;
    private final Scanner scanner;

    public MenuConsole(GestionSGU gestion) {
        this.gestion = gestion;
        this.scanner = new Scanner(System.in);
    }

    // ============================================================
    //                         LANCEMENT
    // ============================================================

    public void lancer() {

        try (scanner) {
            boolean continuer = true;
            
            while (continuer) {
                
                afficherMenuPrincipal();
                
                int choix = lireEntier("Votre choix : ");
                
                try {
                    
                                        switch (choix) {
                                                case 1 -> menuEtudiants();
                                                case 2 -> menuModules();
                                                case 3 -> menuNotes();
                                                case 4 -> menuStatistiques();
                                                case 5 -> menuSauvegarde();
                                                case 0 -> {
                            continuer = false;
                            System.out.println(
                                    "\nApplication fermée."
                            );
                                                }
                                                default ->
                            System.out.println(
                                    "\nChoix invalide."
                            );
                    }
                    
                } catch (SGUException | IllegalArgumentException e) {
                    
                    System.out.println(
                            "\n[ERREUR] " + e.getMessage()
                    );
                    
                } catch (IOException e) {
                    
                    System.out.println(
                            "\n[ERREUR FICHIER] "
                                    + e.getMessage()
                    );
                }
            }
        }
    }

    // ============================================================
    //                     MENU PRINCIPAL
    // ============================================================

    private void afficherMenuPrincipal() {

        System.out.println();
        System.out.println("========================================");
        System.out.println("       SYSTEME DE GESTION DES NOTES");
        System.out.println("========================================");
        System.out.println("1. Gestion des étudiants");
        System.out.println("2. Gestion des modules");
        System.out.println("3. Gestion des notes");
        System.out.println("4. Statistiques et résultats");
        System.out.println("5. Sauvegarde / Chargement / Export");
        System.out.println("0. Quitter");
        System.out.println("========================================");
    }

    // ============================================================
    //                     MENU ETUDIANTS
    // ============================================================

    private void menuEtudiants()
            throws SGUException {

        boolean retour = false;

        while (!retour) {

            System.out.println();
            System.out.println("----------------------------------------");
            System.out.println("          GESTION DES ETUDIANTS");
            System.out.println("----------------------------------------");
            System.out.println("1. Ajouter un étudiant");
            System.out.println("2. Modifier un étudiant");
            System.out.println("3. Supprimer un étudiant");
            System.out.println("4. Rechercher par matricule");
            System.out.println("5. Rechercher par nom");
            System.out.println("6. Rechercher par date de naissance");
            System.out.println("7. Afficher tous les étudiants");
            System.out.println("0. Retour");

            int choix = lireEntier("Votre choix : ");

            switch (choix) {
                case 1 -> ajouterEtudiant();
                case 2 -> modifierEtudiant();
                case 3 -> supprimerEtudiant();
                case 4 -> rechercherEtudiant();
                case 5 -> rechercherParNom();
                case 6 -> rechercherParDate();
                case 7 -> afficherEtudiants();
                case 0 -> retour = true;
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void ajouterEtudiant()
            throws DuplicateEntityException {

        System.out.println("\n--- AJOUT D'UN ETUDIANT ---");

        String matricule =
                lireTexte("Matricule : ");

        String nom =
                lireTexte("Nom : ");

        String prenom =
                lireTexte("Prénom : ");

        LocalDate date =
                lireDate("Date de naissance (yyyy-MM-dd) : ");

        Etudiant etudiant =
                new Etudiant(
                        matricule,
                        nom,
                        prenom,
                        date
                );

        gestion.ajouterEtudiant(etudiant);

        System.out.println(
                "Étudiant ajouté avec succès."
        );
    }

    private void modifierEtudiant()
            throws EtudiantNotFoundException {

        System.out.println("\n--- MODIFICATION ---");

        String matricule =
                lireTexte("Matricule : ");

        String nom =
                lireTexte("Nouveau nom : ");

        String prenom =
                lireTexte("Nouveau prénom : ");

        LocalDate date =
                lireDate(
                        "Nouvelle date de naissance : "
                );

        gestion.modifierEtudiant(
                matricule,
                nom,
                prenom,
                date
        );

        System.out.println(
                "Étudiant modifié avec succès."
        );
    }

    private void supprimerEtudiant()
            throws EtudiantNotFoundException,
            ContrainteSuppressionException {

        String matricule =
                lireTexte("Matricule à supprimer : ");

        gestion.supprimerEtudiant(matricule);

        System.out.println(
                "Étudiant supprimé avec succès."
        );
    }

    private void rechercherEtudiant()
            throws EtudiantNotFoundException {

        String matricule =
                lireTexte("Matricule recherché : ");

        Etudiant etudiant =
                gestion.getEtudiant(matricule);

        System.out.println("\nRésultat :");
        System.out.println(etudiant);
    }

    private void rechercherParNom() {

        String nom =
                lireTexte("Nom ou prénom recherché : ");

        List<Etudiant> resultats =
                gestion.rechercherParNom(nom);

        afficherListeEtudiants(resultats);
    }

    private void rechercherParDate() {

        LocalDate debut =
                lireDate("Date début : ");

        LocalDate fin =
                lireDate("Date fin : ");

        List<Etudiant> resultats =
                gestion.rechercherParIntervalle(
                        debut,
                        fin
                );

        afficherListeEtudiants(resultats);
    }

    private void afficherEtudiants() {

        List<Etudiant> etudiants =
                gestion.getEtudiantsTries();

        afficherListeEtudiants(etudiants);
    }

    private void afficherListeEtudiants(
            List<Etudiant> etudiants
    ) {

        System.out.println();

        if (etudiants.isEmpty()) {
            System.out.println(
                    "Aucun étudiant trouvé."
            );
            return;
        }

        System.out.println(
                "Nombre d'étudiants : "
                        + etudiants.size()
        );

        for (Etudiant etudiant : etudiants) {
            System.out.println(
                    "- " + etudiant
            );
        }
    }

    // ============================================================
    //                       MENU MODULES
    // ============================================================

    private void menuModules()
            throws SGUException {

        boolean retour = false;

        while (!retour) {

            System.out.println();
            System.out.println("----------------------------------------");
            System.out.println("            GESTION DES MODULES");
            System.out.println("----------------------------------------");
            System.out.println("1. Ajouter un module");
            System.out.println("2. Modifier un module");
            System.out.println("3. Supprimer un module");
            System.out.println("4. Rechercher un module");
            System.out.println("5. Afficher tous les modules");
            System.out.println("0. Retour");

            int choix =
                    lireEntier("Votre choix : ");

                        switch (choix) {

                                case 1 -> ajouterModule();

                                case 2 -> modifierModule();

                                case 3 -> supprimerModule();

                                case 4 -> rechercherModule();

                                case 5 -> afficherModules();

                                case 0 -> retour = true;

                                default -> System.out.println(
                                                "Choix invalide."
                                );
                        }
        }
    }

    private void ajouterModule()
            throws DuplicateEntityException {

        System.out.println("\n--- AJOUT D'UN MODULE ---");

        String code =
                lireTexte("Code : ");

        String nom =
                lireTexte("Nom : ");

        double coefficient =
                lireDouble("Coefficient : ");

        boolean obligatoire =
                lireOuiNon(
                        "Module obligatoire ? (o/n) : "
                );

        model.Module module = new model.Module(code, nom, coefficient, obligatoire);

        gestion.ajouterModule(module);

        System.out.println(
                "Module ajouté avec succès.");
    }

    private void modifierModule()
            throws ModuleNotFoundException {

        String code =
                lireTexte("Code du module : ");

        String nom =
                lireTexte("Nouveau nom : ");

        double coefficient =
                lireDouble("Nouveau coefficient : ");

        boolean obligatoire =
                lireOuiNon(
                        "Obligatoire ? (o/n) : "
                );

        gestion.modifierModule(
                code,
                nom,
                coefficient,
                obligatoire
        );

        System.out.println(
                "Module modifié avec succès."
        );
    }

    private void supprimerModule()
            throws ModuleNotFoundException,
            ContrainteSuppressionException {

        String code =
                lireTexte("Code à supprimer : ");

        gestion.supprimerModule(code);

        System.out.println(
                "Module supprimé avec succès."
        );
    }

    private void rechercherModule()
            throws ModuleNotFoundException {

        String code =
                lireTexte("Code recherché : ");

        model.Module module = 
        
        gestion.getModule(code);

        System.out.println(module);
    }

    private void afficherModules() {

        List<model.Module> modules =
                gestion.getModulesTries();

        System.out.println();

        if (modules.isEmpty()) {
            System.out.println(
                    "Aucun module enregistré."
            );
            return;
        }

        for (model.Module module : modules) {
            System.out.println(
                    "- " + module
            );
        }
    }

    // ============================================================
    //                       MENU NOTES
    // ============================================================

    private void menuNotes()
            throws SGUException {

        boolean retour = false;

        while (!retour) {

            System.out.println();
            System.out.println("----------------------------------------");
            System.out.println("             GESTION DES NOTES");
            System.out.println("----------------------------------------");
            System.out.println("1. Ajouter une note");
            System.out.println("2. Modifier une note");
            System.out.println("3. Supprimer une note");
            System.out.println("4. Afficher les notes d'un étudiant");
            System.out.println("0. Retour");

            int choix =
                    lireEntier("Votre choix : ");

            switch (choix) {
                case 1 -> ajouterNote();
                case 2 -> modifierNote();
                case 3 -> supprimerNote();
                case 4 -> afficherNotesEtudiant();
                case 0 -> retour = true;
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void ajouterNote()
            throws DuplicateEntityException,
            EtudiantNotFoundException,
            ModuleNotFoundException {

        System.out.println("\n--- AJOUT D'UNE NOTE ---");

        String matricule =
                lireTexte("Matricule étudiant : ");

        String codeModule =
                lireTexte("Code module : ");

        double valeur =
                lireDouble("Note /20 : ");

        TypeControle type =
                lireTypeControle();

        Note note =
                new Note(
                        matricule,
                        codeModule,
                        valeur,
                        type
                );

        gestion.ajouterNote(note);

        System.out.println(
                "Note ajoutée avec succès."
        );
    }

    private void modifierNote()
            throws NoteNotFoundException {

        String matricule =
                lireTexte("Matricule étudiant : ");

        String code =
                lireTexte("Code module : ");

        TypeControle type =
                lireTypeControle();

        double valeur =
                lireDouble("Nouvelle note : ");

        gestion.modifierNote(
                matricule,
                code,
                type,
                valeur
        );

        System.out.println(
                "Note modifiée avec succès."
        );
    }

    private void supprimerNote()
            throws NoteNotFoundException {

        String matricule =
                lireTexte("Matricule étudiant : ");

        String code =
                lireTexte("Code module : ");

        TypeControle type =
                lireTypeControle();

        gestion.supprimerNote(
                matricule,
                code,
                type
        );

        System.out.println(
                "Note supprimée avec succès."
        );
    }

    private void afficherNotesEtudiant()
            throws EtudiantNotFoundException {

        String matricule =
                lireTexte("Matricule étudiant : ");

        List<Note> notes =
                gestion.getNotesEtudiant(
                        matricule
                );

        System.out.println();

        if (notes.isEmpty()) {
            System.out.println(
                    "Aucune note enregistrée."
            );
            return;
        }

        for (Note note : notes) {
            System.out.println(
                    "- " + note
            );
        }
    }

    // ============================================================
    //                    STATISTIQUES
    // ============================================================

    private void menuStatistiques()
            throws SGUException {

        boolean retour = false;

        while (!retour) {

            System.out.println();
            System.out.println("----------------------------------------");
            System.out.println("        STATISTIQUES ET RESULTATS");
            System.out.println("----------------------------------------");
            System.out.println("1. Moyenne générale d'un étudiant");
            System.out.println("2. Mention d'un étudiant");
            System.out.println("3. Modules non validés");
            System.out.println("4. Classement général");
            System.out.println("5. Moyenne d'un module");
            System.out.println("6. Afficher un bulletin");
            System.out.println("0. Retour");

            int choix =
                    lireEntier("Votre choix : ");

                        switch (choix) {
                                case 1 -> afficherMoyenne();
                                case 2 -> afficherMention();
                                case 3 -> afficherModulesNonValides();
                                case 4 -> afficherClassement();
                                case 5 -> afficherMoyenneModule();
                                case 6 -> afficherBulletin();
                                case 0 -> retour = true;
                                default -> System.out.println(
                                                "Choix invalide."
                                );
                        }
        }
    }

    private void afficherMoyenne()
            throws EtudiantNotFoundException {

        String matricule =
                lireTexte("Matricule : ");

        double moyenne =
                gestion.calculerMoyenneGenerale(
                        matricule
                );

        System.out.printf(
                "Moyenne générale : %.2f/20%n",
                moyenne
        );
    }

    private void afficherMention()
            throws EtudiantNotFoundException {

        String matricule =
                lireTexte("Matricule : ");

        double moyenne =
                gestion.calculerMoyenneGenerale(
                        matricule
                );

        Mention mention =
                gestion.calculerMention(
                        matricule
                );

        System.out.printf(
                "Moyenne : %.2f/20%n",
                moyenne
        );

        System.out.println(
                "Mention : "
                        + mention.getLibelle()
        );
    }

    private void afficherModulesNonValides()
            throws EtudiantNotFoundException {

        String matricule =
                lireTexte("Matricule : ");

        List<Module> modules =
                gestion.modulesNonValides(
                        matricule
                );

        System.out.println(
                "\nModules non validés :"
        );

        if (modules.isEmpty()) {
            System.out.println(
                    "Aucun module non validé."
            );
            return;
        }

        for (Module module : modules) {

            double moyenne = 0;

            try {
                moyenne =
                        gestion.moyenneModule(
                                matricule,
                                module.getCode()
                        );
            } catch (ModuleNotFoundException e) {
                // Impossible normalement.
            }

            System.out.printf(
                    "- %s : %.2f/20%n",
                    module.getCode(),
                    moyenne
            );
        }
    }

    private void afficherClassement() {

        List<Etudiant> classement =
                gestion.classementGeneral();

        System.out.println();
        System.out.println(
                "========== CLASSEMENT =========="
        );

        int position = 1;

        for (Etudiant etudiant : classement) {

            double moyenne = 0;

            try {
                moyenne =
                        gestion.calculerMoyenneGenerale(
                                etudiant.getMatricule()
                        );
            } catch (EtudiantNotFoundException e) {
                continue;
            }

            System.out.printf(
                    "%d. %s %s - %.2f/20%n",
                    position,
                    etudiant.getNom(),
                    etudiant.getPrenom(),
                    moyenne
            );

            position++;
        }
    }

    private void afficherMoyenneModule()
            throws ModuleNotFoundException {

        String code =
                lireTexte("Code module : ");

        double moyenne =
                gestion.moyenneDuModulePourTous(
                        code
                );

        System.out.printf(
                "Moyenne du module : %.2f/20%n",
                moyenne
        );
    }

    // ============================================================
    //                         BULLETIN
    // ============================================================

    private void afficherBulletin()
            throws EtudiantNotFoundException, ModuleNotFoundException {

        String matricule =
                lireTexte("Matricule : ");

        Etudiant etudiant =
                gestion.getEtudiant(matricule);

        System.out.println();
        System.out.println(
                "========================================"
        );
        System.out.println(
                "              BULLETIN"
        );
        System.out.println(
                "========================================"
        );

        System.out.println(
                "Matricule : "
                        + etudiant.getMatricule()
        );

        System.out.println(
                "Nom : "
                        + etudiant.getNom()
                        + " "
                        + etudiant.getPrenom()
        );

        System.out.println("----------------------------------------");

        List<Note> notes;

        try {
            notes =
                    gestion.getNotesEtudiant(
                            matricule
                    );
        } catch (EtudiantNotFoundException e) {
            return;
        }

        for (Module module :
                gestion.getModulesTries()) {

            double moyenne;

            moyenne =
                    gestion.moyenneModule(
                            matricule,
                            module.getCode()
                    );

            boolean possedeNote =
                    notes.stream()
                            .anyMatch(note ->
                                    note.getCodeModule()
                                            .equalsIgnoreCase(
                                                    module.getCode()
                                            )
                            );

            if (possedeNote) {

                System.out.printf(
                        "%-15s | Moyenne : %5.2f | Coeff : %.1f%n",
                        module.getCode(),
                        moyenne,
                        module.getCoefficient()
                );
            }
        }

        double moyenneGenerale =
                gestion.calculerMoyenneGenerale(
                        matricule
                );

        Mention mention =
                gestion.calculerMention(
                        matricule
                );

        System.out.println("----------------------------------------");

        System.out.printf(
                "Moyenne générale : %.2f/20%n",
                moyenneGenerale
        );

        System.out.println(
                "Mention : "
                        + mention.getLibelle()
        );

        System.out.println(
                "========================================"
        );
    }

    // ============================================================
    //               SAUVEGARDE / CHARGEMENT
    // ============================================================

    private void menuSauvegarde()
            throws IOException {

        boolean retour = false;

        while (!retour) {

            System.out.println();
            System.out.println("----------------------------------------");
            System.out.println(
                    "       SAUVEGARDE / EXPORTATION"
            );
            System.out.println("----------------------------------------");
            System.out.println("1. Sauvegarder (.dat)");
            System.out.println("2. Charger (.dat)");
            System.out.println("3. Exporter étudiants (.csv)");
            System.out.println("4. Exporter modules (.csv)");
            System.out.println("5. Exporter notes (.csv)");
            System.out.println("0. Retour");

            int choix =
                    lireEntier("Votre choix : ");

            switch (choix) {

                case 1 -> sauvegarder();

                case 2 -> charger();

                case 3 -> exporterEtudiants();

                case 4 -> exporterModules();

                case 5 -> exporterNotes();

                case 0 -> retour = true;

                default -> System.out.println(
                            "Choix invalide."
                    );
            }
        }
    }

    private void sauvegarder()
            throws IOException {

        String chemin =
                lireTexte(
                        "Nom du fichier (.dat) : "
                );

        gestion.sauvegarder(chemin);

        System.out.println(
                "Sauvegarde effectuée."
        );
    }

    private void charger()
            throws IOException {

        String chemin =
                lireTexte(
                        "Nom du fichier (.dat) : "
                );

        try {

            gestion.charger(chemin);

            System.out.println(
                    "Données chargées avec succès."
            );

        } catch (ClassNotFoundException e) {

            throw new IOException(
                    "Format de fichier invalide."
            );
        }
    }

    private void exporterEtudiants()
            throws IOException {

        String chemin =
                lireTexte(
                        "Nom du fichier CSV : "
                );

        ExportUtil.exporterEtudiantsCSV(
                gestion,
                chemin
        );

        System.out.println(
                "Export terminé."
        );
    }

    private void exporterModules()
            throws IOException {

        String chemin =
                lireTexte(
                        "Nom du fichier CSV : "
                );

        ExportUtil.exporterModulesCSV(
                gestion,
                chemin
        );

        System.out.println(
                "Export terminé."
        );
    }

    private void exporterNotes()
            throws IOException {

        String chemin =
                lireTexte(
                        "Nom du fichier CSV : "
                );

        ExportUtil.exporterNotesCSV(
                gestion,
                chemin
        );

        System.out.println(
                "Export terminé."
        );
    }

    // ============================================================
    //                      UTILITAIRES
    // ============================================================

    private String lireTexte(String message) {

        System.out.print(message);

        String valeur = scanner.nextLine();

        if (ValidationUtil.estVide(valeur)) {

            throw new IllegalArgumentException(
                    "La valeur ne peut pas être vide."
            );
        }

        return valeur.trim();
    }

    private int lireEntier(String message) {

        while (true) {

            System.out.print(message);

            String valeur =
                    scanner.nextLine();

            try {

                return Integer.parseInt(
                        valeur.trim()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Veuillez entrer un entier."
                );
            }
        }
    }

    private double lireDouble(String message) {

        while (true) {

            System.out.print(message);

            String valeur =
                    scanner.nextLine();

            try {

                double nombre =
                        Double.parseDouble(
                                valeur.trim()
                        );

                if (nombre < 0 || nombre > 20) {

                    System.out.println(
                            "La valeur doit être comprise entre 0 et 20."
                    );

                    continue;
                }

                return nombre;

            } catch (NumberFormatException e) {

                System.out.println(
                        "Veuillez entrer un nombre valide."
                );
            }
        }
    }

    private LocalDate lireDate(String message) {

        while (true) {

            System.out.print(message);

            String valeur =
                    scanner.nextLine();

            try {

                return ValidationUtil.lireDate(
                        valeur
                );

            } catch (IllegalArgumentException e) {

                System.out.println(
                        e.getMessage()
                );
            }
        }
    }

    private boolean lireOuiNon(String message) {

        while (true) {

            System.out.print(message);

            String valeur =
                    scanner.nextLine()
                            .trim()
                            .toLowerCase();

            if (valeur.equals("o")
                    || valeur.equals("oui")) {

                return true;
            }

            if (valeur.equals("n")
                    || valeur.equals("non")) {

                return false;
            }

            System.out.println(
                    "Répondez par o ou n."
            );
        }
    }

    private TypeControle lireTypeControle() {

        while (true) {

            System.out.println(
                    "1. EXAMEN"
            );
            System.out.println(
                    "2. DS"
            );
            System.out.println(
                    "3. TP"
            );
            System.out.println(
                    "4. PROJET"
            );

            int choix =
                    lireEntier(
                            "Type de contrôle : "
                    );

            switch (choix) {

                case 1:
                    return TypeControle.EXAMEN;

                case 2:
                    return TypeControle.DS;

                case 3:
                    return TypeControle.TP;

                case 4:
                    return TypeControle.PROJET;

                default:
                    System.out.println(
                            "Choix invalide."
                    );
            }
        }
    }
}