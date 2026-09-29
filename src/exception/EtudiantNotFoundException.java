package exception;

public class EtudiantNotFoundException extends SGUException {

    public EtudiantNotFoundException(String matricule) {
        super("Étudiant introuvable : " + matricule);
    }
}