package service;

import exception.ContrainteSuppressionException;
import exception.DuplicateEntityException;
import exception.EtudiantNotFoundException;
import exception.ModuleNotFoundException;
import exception.NoteNotFoundException;
import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import model.Etudiant;
import model.Mention;
import model.Module;
import model.Note;
public class GestionSGU {

    // =========================
    // COLLECTIONS PRINCIPALES
    // =========================

    private final Map<String, Etudiant> etudiants;
    private final Map<String, Module> modules;
    private final List<Note> notes;

    // =========================
    // CONSTRUCTEUR
    // =========================

    public GestionSGU() {
        etudiants = new HashMap<>();
        modules = new HashMap<>();
        notes = new ArrayList<>();
    }

    // ============================================================
    //                      ETUDIANTS
    // ============================================================

    public void ajouterEtudiant(Etudiant etudiant)
            throws DuplicateEntityException {

        if (etudiant == null) {
            throw new IllegalArgumentException(
                    "L'étudiant ne peut pas être null."
            );
        }

        String matricule = etudiant.getMatricule();

        if (etudiants.containsKey(matricule)) {
            throw new DuplicateEntityException(
                    "Un étudiant avec le matricule "
                            + matricule + " existe déjà."
            );
        }

        etudiants.put(matricule, etudiant);
    }

    public Etudiant getEtudiant(String matricule)
            throws EtudiantNotFoundException {

        if (matricule == null || matricule.isBlank()) {
            throw new EtudiantNotFoundException(
                    "Matricule vide"
            );
        }

        String key = matricule.trim().toUpperCase();

        Etudiant etudiant = etudiants.get(key);

        if (etudiant == null) {
            throw new EtudiantNotFoundException(key);
        }

        return etudiant;
    }

    public void modifierEtudiant(
            String matricule,
            String nom,
            String prenom,
            LocalDate dateNaissance
    ) throws EtudiantNotFoundException {

        Etudiant etudiant = getEtudiant(matricule);

        etudiant.setNom(nom);
        etudiant.setPrenom(prenom);
        etudiant.setDateNaissance(dateNaissance);
    }

    public void supprimerEtudiant(String matricule)
            throws EtudiantNotFoundException,
            ContrainteSuppressionException {

        Etudiant etudiant = getEtudiant(matricule);

        boolean possedeNotes = notes.stream()
                .anyMatch(note ->
                        note.getMatriculeEtudiant()
                                .equalsIgnoreCase(
                                        etudiant.getMatricule()
                                )
                );

        if (possedeNotes) {
            throw new ContrainteSuppressionException(
                    "Impossible de supprimer l'étudiant "
                            + etudiant.getMatricule()
                            + " car il possède des notes."
            );
        }

        etudiants.remove(etudiant.getMatricule());
    }

    // =========================
    // RECHERCHE ETUDIANTS
    // =========================

    public List<Etudiant> rechercherParNom(String nom) {

        if (nom == null || nom.isBlank()) {
            return new ArrayList<>();
        }

        String recherche = nom.trim().toLowerCase();

        return etudiants.values()
                .stream()
                .filter(e ->
                        e.getNom()
                                .toLowerCase()
                                .contains(recherche)
                        ||
                        e.getPrenom()
                                .toLowerCase()
                                .contains(recherche)
                )
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Etudiant> rechercherParIntervalle(
            LocalDate debut,
            LocalDate fin
    ) {

        if (debut == null || fin == null) {
            throw new IllegalArgumentException(
                    "Les dates sont obligatoires."
            );
        }

        if (debut.isAfter(fin)) {
            throw new IllegalArgumentException(
                    "La date de début doit être avant la date de fin."
            );
        }

        return etudiants.values()
                .stream()
                .filter(e ->
                        !e.getDateNaissance().isBefore(debut)
                        &&
                        !e.getDateNaissance().isAfter(fin)
                )
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Etudiant> getEtudiantsTries() {

        return etudiants.values()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    // ============================================================
    //                         MODULES
    // ============================================================

    public void ajouterModule(Module module)
            throws DuplicateEntityException {

        if (module == null) {
            throw new IllegalArgumentException(
                    "Le module ne peut pas être null."
            );
        }

        String code = module.getCode();

        if (modules.containsKey(code)) {
            throw new DuplicateEntityException(
                    "Le module "
                            + code
                            + " existe déjà."
            );
        }

        modules.put(code, module);
    }

    public Module getModule(String code)
            throws ModuleNotFoundException {

        if (code == null || code.isBlank()) {
            throw new ModuleNotFoundException(
                    "Code vide"
            );
        }

        String key = code.trim().toUpperCase();

        Module module = modules.get(key);

        if (module == null) {
            throw new ModuleNotFoundException(key);
        }

        return module;
    }

    public void modifierModule(
            String code,
            String nom,
            double coefficient,
            boolean obligatoire
    ) throws ModuleNotFoundException {

        Module module = getModule(code);

        module.setNom(nom);
        module.setCoefficient(coefficient);
        module.setObligatoire(obligatoire);
    }

    public void supprimerModule(String code)
            throws ModuleNotFoundException,
            ContrainteSuppressionException {

        Module module = getModule(code);

        boolean possedeNotes = notes.stream()
                .anyMatch(note ->
                        note.getCodeModule()
                                .equalsIgnoreCase(
                                        module.getCode()
                                )
                );

        if (possedeNotes) {
            throw new ContrainteSuppressionException(
                    "Impossible de supprimer le module "
                            + module.getCode()
                            + " car il possède des notes."
            );
        }

        modules.remove(module.getCode());
    }

    public List<Module> getModulesTries() {

        return modules.values()
                .stream()
                .sorted(
                        Comparator.comparing(
                                Module::getCode
                        )
                )
                .collect(Collectors.toList());
    }

    // ============================================================
    //                           NOTES
    // ============================================================

    public void ajouterNote(Note note)
            throws DuplicateEntityException,
            EtudiantNotFoundException,
            ModuleNotFoundException {

        if (note == null) {
            throw new IllegalArgumentException(
                    "La note ne peut pas être null."
            );
        }

        // Vérifier que l'étudiant existe
        getEtudiant(note.getMatriculeEtudiant());

        // Vérifier que le module existe
        getModule(note.getCodeModule());

        // Vérifier les doublons
        boolean existe = notes.stream()
                .anyMatch(n ->
                        n.getKey().equals(note.getKey())
                );

        if (existe) {
            throw new DuplicateEntityException(
                    "Cette note existe déjà."
            );
        }

        notes.add(note);
    }

    public Note getNote(
            String matricule,
            String codeModule,
            model.TypeControle typeControle
    ) throws NoteNotFoundException {

        for (Note note : notes) {

            if (
                    note.getMatriculeEtudiant()
                            .equalsIgnoreCase(matricule)
                    &&
                    note.getCodeModule()
                            .equalsIgnoreCase(codeModule)
                    &&
                    note.getTypeControle()
                            == typeControle
            ) {
                return note;
            }
        }

        throw new NoteNotFoundException(
                "Note introuvable pour l'étudiant "
                        + matricule
                        + ", module "
                        + codeModule
                        + ", contrôle "
                        + typeControle
        );
    }

    public void modifierNote(
            String matricule,
            String codeModule,
            model.TypeControle typeControle,
            double nouvelleValeur
    ) throws NoteNotFoundException {

        Note note = getNote(
                matricule,
                codeModule,
                typeControle
        );

        note.setValeur(nouvelleValeur);
    }

    public void supprimerNote(
            String matricule,
            String codeModule,
            model.TypeControle typeControle
    ) throws NoteNotFoundException {

        Note note = getNote(
                matricule,
                codeModule,
                typeControle
        );

        notes.remove(note);
    }

    public List<Note> getNotesEtudiant(String matricule)
            throws EtudiantNotFoundException {

        getEtudiant(matricule);

        return notes.stream()
                .filter(note ->
                        note.getMatriculeEtudiant()
                                .equalsIgnoreCase(matricule)
                )
                .collect(Collectors.toList());
    }

    // ============================================================
    //                         MOYENNES
    // ============================================================

    public double moyenneModule(
            String matricule,
            String codeModule
    ) throws EtudiantNotFoundException,
            ModuleNotFoundException {

        getEtudiant(matricule);

        List<Note> notesModule = notes.stream()
                .filter(note ->
                        note.getMatriculeEtudiant()
                                .equalsIgnoreCase(matricule)
                        &&
                        note.getCodeModule()
                                .equalsIgnoreCase(codeModule)
                )
                .collect(Collectors.toList());

        if (notesModule.isEmpty()) {
            return 0.0;
        }

        double somme = 0;

        for (Note note : notesModule) {
            somme += note.getValeur();
        }

        return somme / notesModule.size();
    }

    public double calculerMoyenneGenerale(
            String matricule
    ) throws EtudiantNotFoundException {

        getEtudiant(matricule);

        double sommePonderee = 0;
        double sommeCoefficients = 0;

        Set<String> modulesEtudiant = notes.stream()
                .filter(note ->
                        note.getMatriculeEtudiant()
                                .equalsIgnoreCase(matricule)
                )
                .map(Note::getCodeModule)
                .collect(Collectors.toSet());

        for (String codeModule : modulesEtudiant) {

            try {

                Module module = getModule(codeModule);

                double moyenne =
                        moyenneModule(
                                matricule,
                                codeModule
                        );

                sommePonderee +=
                        moyenne * module.getCoefficient();

                sommeCoefficients +=
                        module.getCoefficient();

            } catch (ModuleNotFoundException e) {

                // Un module supprimé ne doit pas bloquer
                // le calcul des autres résultats.
            }
        }

        if (sommeCoefficients == 0) {
            return 0.0;
        }

        return sommePonderee / sommeCoefficients;
    }

    public Mention calculerMention(
            String matricule
    ) throws EtudiantNotFoundException {

        double moyenne =
                calculerMoyenneGenerale(matricule);

        return Mention.fromMoyenne(moyenne);
    }

    // ============================================================
    //                     MODULES NON VALIDES
    // ============================================================

    public List<Module> modulesNonValides(
            String matricule
    ) throws EtudiantNotFoundException {

        getEtudiant(matricule);

        List<Module> resultat = new ArrayList<>();

        for (Module module : modules.values()) {

            try {

                double moyenne =
                        moyenneModule(
                                matricule,
                                module.getCode()
                        );

                if (moyenne < 10) {
                    resultat.add(module);
                }

            } catch (ModuleNotFoundException e) {

                // Normalement impossible ici.
            }
        }

        resultat.sort(
                Comparator.comparing(Module::getCode)
        );

        return resultat;
    }

    // ============================================================
    //                         CLASSEMENT
    // ============================================================

    public List<Etudiant> classementGeneral() {

        List<Etudiant> classement =
                new ArrayList<>(etudiants.values());

        classement.sort(
                (e1, e2) -> {

                    try {

                        double moyenne1 =
                                calculerMoyenneGenerale(
                                        e1.getMatricule()
                                );

                        double moyenne2 =
                                calculerMoyenneGenerale(
                                        e2.getMatricule()
                                );

                        return Double.compare(
                                moyenne2,
                                moyenne1
                        );

                    } catch (EtudiantNotFoundException e) {

                        return 0;
                    }
                }
        );

        return classement;
    }

    // ============================================================
    //                  STATISTIQUES MODULE
    // ============================================================

    public double moyenneDuModulePourTous(
            String codeModule
    ) throws ModuleNotFoundException {

        getModule(codeModule);

        List<Note> notesModule = notes.stream()
                .filter(note ->
                        note.getCodeModule()
                                .equalsIgnoreCase(codeModule)
                )
                .collect(Collectors.toList());

        if (notesModule.isEmpty()) {
            return 0.0;
        }

        double somme = 0;

        for (Note note : notesModule) {
            somme += note.getValeur();
        }

        return somme / notesModule.size();
    }

    // ============================================================
    //                          GETTERS
    // ============================================================

    public int nombreEtudiants() {
        return etudiants.size();
    }

    public int nombreModules() {
        return modules.size();
    }

    public int nombreNotes() {
        return notes.size();
    }

    public Collection<Etudiant> getEtudiants() {
        return Collections.unmodifiableCollection(
                etudiants.values()
        );
    }

    public Collection<Module> getModules() {
        return Collections.unmodifiableCollection(
                modules.values()
        );
    }

    public List<Note> getNotes() {
        return Collections.unmodifiableList(notes);
    }

    // ============================================================
    //                     SAUVEGARDE .DAT
    // ============================================================

    public void sauvegarder(String chemin)
            throws IOException {

        DonneesSGU donnees = new DonneesSGU(
                etudiants,
                modules,
                notes
        );

        try (
                ObjectOutputStream out =
                        new ObjectOutputStream(
                                new FileOutputStream(chemin)
                        )
        ) {

            out.writeObject(donnees);
        }
    }

    // ============================================================
    //                     CHARGEMENT .DAT
    // ============================================================

    public void charger(String chemin)
            throws IOException,
            ClassNotFoundException {

        try (
                ObjectInputStream in =
                        new ObjectInputStream(
                                new FileInputStream(chemin)
                        )
        ) {

            DonneesSGU donnees =
                    (DonneesSGU) in.readObject();

            etudiants.clear();
            modules.clear();
            notes.clear();

            etudiants.putAll(donnees.etudiants);
            modules.putAll(donnees.modules);
            notes.addAll(donnees.notes);
        }
    }

    // ============================================================
    //                    CLASSE INTERNE
    // ============================================================

    private static class DonneesSGU
            implements Serializable {

        private static final long serialVersionUID = 1L;

        private final Map<String, Etudiant> etudiants;
        private final Map<String, Module> modules;
        private final List<Note> notes;

        public DonneesSGU(
                Map<String, Etudiant> etudiants,
                Map<String, Module> modules,
                List<Note> notes
        ) {

            this.etudiants =
                    new HashMap<>(etudiants);

            this.modules =
                    new HashMap<>(modules);

            this.notes =
                    new ArrayList<>(notes);
        }
    }
}