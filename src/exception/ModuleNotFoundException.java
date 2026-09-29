package exception;

public class ModuleNotFoundException extends SGUException {

    public ModuleNotFoundException(String code) {
        super("Module introuvable : " + code);
    }
}