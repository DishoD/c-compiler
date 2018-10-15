import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LAutomat {

    public static enum LAutomatStatus {
        /**
         * Automat se nalazi u jednom ili više stanja trenutno, ali niti jedno nije prihvatljivo.
         */
        RADI,

        /**
         * Automat se nalazi u prihvatljivom stanju.
         */
        PRIHVATLJIV,

        /**
         * Automat se trenutno ne nalazi niti u jednom stanju.
         */
        STOPIRAN
    }

    private Node pocetnoStanje;
    private Node prihvatljivoStanje;

    private Set<Node> trenutnaStanja = new HashSet<>();
    private LAutomatStatus status;

    private List<Akcija> akcije = new ArrayList<>();

    /**
     * Inicijalizira novi automat sa zadanim početnim i prihvatljivim stanjem. Automatov status je 'STOPIRAN'.
     * Automat se mora pokrenuti nakon inicijalizacije.
     *
     * @param pocetnoStanje početno stanje automata
     * @param prihvatljivoStanje prihvatljivo stanje automat
     */
    public LAutomat(Node pocetnoStanje, Node prihvatljivoStanje) {
        this.pocetnoStanje = pocetnoStanje;
        this.prihvatljivoStanje = prihvatljivoStanje;
        this.status = LAutomatStatus.STOPIRAN;
    }

    //---------------------------------------TODO---------------------------------------------------

    /**
     * Pokreće automat. Status mu postaje 'RADI'. Trenutna stanja su e-okruženje početnog stanja.
     * Ako je automat već bio pokrenut, resetira se opet na početno stanje + e-okruženje.
     */
    public void pokreniAutomat(){

    }

    /**
     * Za predani znak automat vrši prijelaze u nova stanja te ih proširuje e-okruženjem.
     *
     * @param znak za koji znak se izvršavaju prijelazi
     * @return status automata nakon izvršenih prijelaza
     */
    public LAutomatStatus prijelaz(char znak) {
        return null;
    }

    public LAutomatStatus getStatus() {
        return status;
    }

    public void setAkcije(List<Akcija> akcije) {
        this.akcije = akcije;
    }

    public void izvrsiAkcije() {
        for(Akcija akcija : akcije) akcija.izvrsi();
    }

    /**
     * Trenutna stanja proširi stanjima epsilon prijalaza.
     */
    private void EOkruzenje() {

    }
}
