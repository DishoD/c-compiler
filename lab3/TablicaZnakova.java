import java.util.HashMap;
import java.util.Map;

/**
 * Ovaj razred predstavlja tablicu znakova.
 *
 * Sve varijable i metode u ovom razredu moraju biti staticke
 * kako bi se ovaj razred mogao koristiti na globalnoj razini te da mu
 * svi razredi imaju pristup.
 */
public final class TablicaZnakova {
    private static final Djelokrug GLOBALNI_DJELOKRUG = new Djelokrug(null, Djelokrug.Oznaka.GLOBALNI_DJELOKRUG, null);
    private static Djelokrug trenutniDjelokrug = GLOBALNI_DJELOKRUG;

    private static Map<String, PrototipFunkcije> definiraneFunkcije = new HashMap<>();

    /**
     * Stavara novi djelokrug u trenutnom djelokrugu te
     * novostvoreni djelokrug postavlja kao trenutni djelokrug.
     */
    public static void stvoriNoviDjelokrug(Djelokrug.Oznaka oznaka, PrototipFunkcije pripadaFunkciji) {
        Djelokrug novi = new Djelokrug(trenutniDjelokrug, oznaka, pripadaFunkciji);
        trenutniDjelokrug.addChild(novi);
        trenutniDjelokrug = novi;
    }

    /**
     * Vraca se (postavlja kao trenutni) u ugnjezdujuci djelokrug.
     */
    public static void vratiSe() {
        trenutniDjelokrug = trenutniDjelokrug.getParent();
    }

    public static Djelokrug getGlobalniDjelokrug() {
        return GLOBALNI_DJELOKRUG;
    }

    public static Djelokrug getTrenutniDjelokrug() {
        return trenutniDjelokrug;
    }

    public static boolean postojiDefiniranaFunkcija(String f) {
        return definiraneFunkcije.containsKey(f);
    }

    public static PrototipFunkcije getDefiniranaFunkcija(String f) {return  definiraneFunkcije.get(f);}

    public static void dodajDefiniranuFunkciju(PrototipFunkcije f) {
        definiraneFunkcije.put(f.getIdn(), f);
    }
}
