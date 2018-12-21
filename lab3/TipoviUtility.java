/**
 * Pomocni razred za rukovanje tipovima.
 * Sadrzi pomocne funkcije vezane za tipove, pretvorbu, provjere, itd.
 */
public final class TipoviUtility {
    public static boolean isConst(String tip) {
        return tip.startsWith("const(");
    }

    public static boolean isArray(String tip) {
        return tip.startsWith("niz(");
    }

    public static boolean isConstArray(String tip) {
        return tip.startsWith("niz(const(");
    }

    public static String toConst(String tip) {
        return "const(" + tip + ")";
    }

    public static String toArray(String tip) {
        return "niz(" + tip + ")";
    }

    /**
     * Provjerava moze li se tip1 implicitno castati u tip2.
     */
    public static boolean castableImplicit(String tip1, String tip2) {
        //TODO implementirati

        return true;
    }

    public static boolean castableExplicit(String tip1, String tip2) {
        if(TipoviUtility.isArray(tip1) || TipoviUtility.isArray(tip2)) return false;
        if(castableImplicit(tip1, tip2)) return true;
        return castableImplicit(tip1, "int") && castableImplicit(tip2, "char");
    }
}
