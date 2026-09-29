package model;

public enum Mention {

    AJOURNE("AJOURNÉ"),
    PASSABLE("PASSABLE"),
    ASSEZ_BIEN("ASSEZ BIEN"),
    BIEN("BIEN"),
    TRES_BIEN("TRÈS BIEN");

    private final String libelle;

    Mention(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public static Mention fromMoyenne(double moyenne) {

        if (moyenne < 10) {
            return AJOURNE;
        }

        if (moyenne < 12) {
            return PASSABLE;
        }

        if (moyenne < 14) {
            return ASSEZ_BIEN;
        }

        if (moyenne < 16) {
            return BIEN;
        }

        return TRES_BIEN;
    }
}
