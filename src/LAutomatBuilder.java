import java.util.Map;

public class LAutomatBuilder {
    private Map<Integer, Node> nodovi;
    private Node pocetnoStanje;
    private Node prihvatljivoStanje;

    /**
     * Spaja stanje izvor sa stanjem odrediste prijelazom za dani znak.
     *
     * @param izvor izvorišno stanje prijelaza
     * @param odrediste odredišno stanje prijelaza
     * @param znak znak prijelaza
     */
    public void dodajPrijelaz(int izvor, int odrediste, char znak){

    }

    /**
     * Stvara epsilon prijelaz izmeču stanja izvor i odredište.
     *
     * @param izvor izvorišno stanje prijelaza
     * @param odrediste odredišno stanje prijelaza
     */
    public void dodajEPrijelaz(int izvor, int odrediste){

    }

    /**
     * Postavlja dato stanje kao početno stanje automata.
     *
     * @param oznaka oznaka stanja
     */
    public void setPocetnoStanje(int oznaka){

    }

    /**
     * Postavlja dato stanje kao prihvatljivo stanje automata.
     *
     * @param oznaka oznaka stanja
     */
    public void setPrihvatljivoStanje(int oznaka){

    }

    /**
     * Konstruira automat za trenutnu tablicu prijelaza i stanja i vraća ga.
     *
     * @return izgrađeni automat
     */
    public LAutomat getLAutomat() {
        return null;
    }
}
