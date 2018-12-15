import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * E-NKA simulator.
 */
public class LAutomat {

    public static enum LAutomatStatus {
        /**
         * Automat se nalazi u jednom ili vise stanja trenutno, ali niti jedno nije prihvatljivo.
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
    private Set<Node> prihvatljivaStanja;
    private Set<Node> trenutnaStanja = new HashSet<>();
    private LAutomatStatus status;
    private List<Akcija> akcije = new ArrayList<>();

    /**
     * Inicijalizira novi automat sa zadanim pocetnim i skupom prihvatljivih stanja. Automatov status je 'STOPIRAN'.
     * Automat se mora pokrenuti nakon inicijalizacije.
     *
     * @param pocetnoStanje pocetno stanje automata
     * @param prihvatljivaStanja skup prihvatljivih stanja automat
     */
    public LAutomat(Node pocetnoStanje, Set<Node> prihvatljivaStanja) {
        this.pocetnoStanje = pocetnoStanje;
        this.prihvatljivaStanja = prihvatljivaStanja;
        this.status = LAutomatStatus.STOPIRAN;
    }

    /**
     * Pokrece automat. Trenutna stanja su e-okruzenje pocetnog stanja.
     * Ako je automat vec bio pokrenut, resetira se opet na pocetno stanje + e-okruzenje.
     * Nakon poziva ove metode automat moze biti u stanju RADI ili stanju PRIHVATLJIV ovisno o
     * e-okruzenju pocetnog stanja.
     */
    public void pokreniAutomat(){
        status = LAutomatStatus.RADI;
        trenutnaStanja.clear();
        trenutnaStanja.add(pocetnoStanje);
        EOkruzenje();
        postaviStatus();
    }

    /**
     * Za predani znak automat vrsi prijelaze u nova stanja te ih prosiruje e-okruzenjem.
     *
     * @param znak za koji znak se izvrsavaju prijelazi
     * @return status automata nakon izvrsenih prijelaza
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

        EOkruzenje();  // modificiramo trenutnaStanja, prosirivanje EOkruzenjem
        return postaviStatus();

    }

    /**
     * Svaka LR stavka je prihvatljiva!
     * @return trenutni status automata
     */
    private LAutomatStatus postaviStatus(){
        return LAutomatStatus.PRIHVATLJIV;

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
     * Trenutna stanja prosiri stanjima epsilon prijelaza.
     */
    private void EOkruzenje() {

        List<Node>  stogStanja = new ArrayList<>(trenutnaStanja); // u pocetku su na stoga sva stanja koja cine skup trenutnih
        HashSet<Node> obiljezenaStanja = new HashSet<>(trenutnaStanja); // u obiljezenim stanjima ce biti epsilon okruzenje

        while(!stogStanja.isEmpty()){
            Node temp = stogStanja.get(stogStanja.size() - 1); // skidamo stanje s "vrha" stoga
            stogStanja.remove(stogStanja.size() - 1);
            for(Node pojedinoStanje : temp.getEPrijelazi()){ // iteriramo kroz skup stanja u koji ide eps-prijelazima
                if(!obiljezenaStanja.contains(pojedinoStanje)){
                    obiljezenaStanja.add(pojedinoStanje); // ako nije bio obiljezen, postaje obiljezen
                    stogStanja.add(pojedinoStanje);
                }
            }
        }
        trenutnaStanja = obiljezenaStanja;

    }
}
