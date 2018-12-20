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
    public static boolean castable(String tip1, String tip2) {
        //TODO implementirati

        return true;
    }
}
