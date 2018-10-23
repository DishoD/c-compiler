package hr.fer.ppj.leksickiAnalizator;
import java.util.HashMap;
import java.util.Map;

/**
 * Razred koji služi za laku izgradnju LAutomata.
 */
public class LAutomatBuilder {
    private Map<Integer, Node> nodovi = new HashMap<>();
    private Node pocetnoStanje;
    private Node prihvatljivoStanje;

    /**
     * Spaja stanje izvor sa stanjem odrediste prijelazom za dani znak.
     *
     * @param izvor izvorišno stanje prijelaza
     * @param odrediste odredišno stanje prijelaza
     * @param znak znak prijelaza
     */
    public LAutomatBuilder dodajPrijelaz(int izvor, int odrediste, char znak){
        getNode(izvor).dodajPrijelaz(znak, getNode(odrediste));
        return this;
    }

    /**
     * Stvara epsilon prijelaz izmeču stanja izvor i odredište.
     *
     * @param izvor izvorišno stanje prijelaza
     * @param odrediste odredišno stanje prijelaza
     */
    public LAutomatBuilder dodajEPrijelaz(int izvor, int odrediste){
        getNode(izvor).dodajEPrijelaz(getNode(odrediste));
        return this;
    }

    /**
     * Postavlja dato stanje kao početno stanje automata.
     *
     * @param oznaka oznaka stanja
     */
    public void setPocetnoStanje(int oznaka){
        pocetnoStanje = getNode(oznaka);
    }

    /**
     * Postavlja dato stanje kao prihvatljivo stanje automata.
     *
     * @param oznaka oznaka stanja
     */
    public void setPrihvatljivoStanje(int oznaka){
        prihvatljivoStanje = getNode(oznaka);
    }

    /**
     * Konstruira automat za trenutnu tablicu prijelaza i stanja i vraća ga.
     *
     * @return izgrađeni automat
     */
    public LAutomat getLAutomat() {
        return new LAutomat(pocetnoStanje, prihvatljivoStanje);
    }

    private Node getNode(int oznaka) {
        Node temp = nodovi.get(oznaka);
        if(temp == null) {
            temp = new Node(oznaka);
            nodovi.put(oznaka, temp);
        }
        return temp;
    }
}
