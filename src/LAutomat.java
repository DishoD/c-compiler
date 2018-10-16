import java.awt.*;
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
        System.out.println("Automat je počeo s radom: ");
        System.out.println("---------------------------");
        status = LAutomatStatus.RADI;
        trenutnaStanja =  new HashSet<Node>(); // resetiranje trenutnog skupa stanja
        trenutnaStanja.add(pocetnoStanje);
        EOkruzenje();

    }

    /**
     * Za predani znak automat vrši prijelaze u nova stanja te ih proširuje e-okruženjem.
     *
     * @param znak za koji znak se izvršavaju prijelazi
     * @return status automata nakon izvršenih prijelaza
     */
    public LAutomatStatus prijelaz(char znak) {
        Set<Node> novaStanja = new HashSet<>();
        boolean nemaPrijelaza = true;

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
        status =  (trenutnaStanja.contains(prihvatljivoStanje)) ? LAutomatStatus.PRIHVATLJIV : LAutomatStatus.RADI;
        return  (trenutnaStanja.contains(prihvatljivoStanje)) ? LAutomatStatus.PRIHVATLJIV : LAutomatStatus.RADI;

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

        HashSet<Node> result = new HashSet<>();
        result.addAll(trenutnaStanja); //eps-okruženje skupa stanja će uvijek imati dotični skup stanja

        int setSize = result.size();

        while(true){
            for(Node stanje : new HashSet<Node>(result)){
                result.addAll(stanje.getEPrijelazi());
            }
            if(result.size() == setSize) break;
            setSize = result.size();
        }

        trenutnaStanja = result;
    }
}
