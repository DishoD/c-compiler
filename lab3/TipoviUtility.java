/**
 * Pomocni razred za rukovanje tipovima.
 * Sadrzi pomocne funkcije vezane za tipove, pretvorbu, provjere, itd.
 */
public final class TipoviUtility {
    public static boolean isConst(String tip) {
        return tip.startsWith("const(");
    }
}
