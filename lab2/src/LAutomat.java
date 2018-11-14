import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * E-NKA simulator.
 */
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

    /**
     * Pokreće automat. Trenutna stanja su e-okruženje početnog stanja.
     * Ako je automat već bio pokrenut, resetira se opet na početno stanje + e-okruženje.
     * Nakon poziva ove metode automat može biti u stanju RADI ili stanju PRIHVATLJIV ovisno o
     * e-okruženju početnog stanja.
     */
    public void pokreniAutomat(){
        status = LAutomatStatus.RADI;
        trenutnaStanja.clear();
        trenutnaStanja.add(pocetnoStanje);
        EOkruzenje();
        postaviStatus();
    }

    /**
     * Za predani znak automat vrši prijelaze u nova stanja te ih proširuje e-okruženjem.
     *
     * @param znak za koji znak se izvršavaju prijelazi
     * @return status automata nakon izvršenih prijelaza
     */
    public LAutomatStatus prijelaz(String znak) {
        if(status == LAutomatStatus.STOPIRAN) return status;
        Set<Node> novaStanja = new HashSet<>();

        for(Node pojedinoStanje : trenutnaStanja){ // dodavanje stanja u koja idemo prijelazima automata
          List<Node> temp = pojedinoStanje.getPrijelazi(znak);
          if(temp == null) continue;
          novaStanja.addAll(temp);
        }

        trenutnaStanja = novaStanja;
        if(trenutnaStanja.isEmpty()){
            this.status = LAutomatStatus.STOPIRAN;
            return LAutomatStatus.STOPIRAN;
        }

        EOkruzenje();  // modificiramo trenutnaStanja, proširivanje EOkruženjem
        return postaviStatus();

    }

    /**
     * Identificira trenutni status automata i postavlja ga kao zadani.
     * @return trenutni status automata
     */
    private LAutomatStatus postaviStatus(){
        if(trenutnaStanja.isEmpty()) {
            status = LAutomatStatus.STOPIRAN;
            return status;
        }
        status = (trenutnaStanja.contains(prihvatljivoStanje)) ? LAutomatStatus.PRIHVATLJIV : LAutomatStatus.RADI;
        return status;
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
     * Trenutna stanja proširi stanjima epsilon prijelaza.
     */
    private void EOkruzenje() {

        List<Node>  stogStanja = new ArrayList<>(trenutnaStanja); // u pocetku su na stoga sva stanja koja cine skup trenutnih
        HashSet<Node> obiljezenaStanja = new HashSet<>(trenutnaStanja); // u obiljezenim stanjima ce biti epsilon okruzenje

        while(!stogStanja.isEmpty()){
            Node temp = stogStanja.get(stogStanja.size() - 1); // skidamo stanje s "vrha" stoga
            stogStanja.remove(stogStanja.size() - 1);
                for(Node pojedinoStanje : temp.getEPrijelazi()){ // iteriramo kroz skup stanja u koji ide eps-prijelazima
                    if(!obiljezenaStanja.contains(pojedinoStanje)){
                        obiljezenaStanja.add(pojedinoStanje); // ako nije bio obilježen, postaje obilježen
                        stogStanja.add(pojedinoStanje);
                    }
                }
            }
        trenutnaStanja = obiljezenaStanja;

    }
}
