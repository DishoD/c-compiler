import java.util.*;

public class Djelokrug extends Node {
    public enum Oznaka{
        PETLJA, FUNKCIJA, BLOK, GLOBALNI_DJELOKRUG
    }

    private Map<String, Varijabla> deklariraneVarijable = new HashMap<>();
    private Map<String, PrototipFunkcije> deklariraneFunkcije = new HashMap<>();

    /**
     * zapocinje sa 4 zato sto se na vrhu stoga na pocetku nalazi povratna adresa ako se radi o funkciji
     * ili stara vrijednost vrha stoga (R5) ako se radi o bloku
     */
    private static final int POCETNA_VELICINA_STOGA = 4;

    private int velicina = 0;
    private int brojLokalnihVarijabli = 0;
    private int brojParametaraFunkcije = 0;

    private final Oznaka oznaka;
    private final PrototipFunkcije pripadaFunkciji;

    public Djelokrug(Node parent, Oznaka oznaka, PrototipFunkcije pripadaFunkciji) {
        super(parent);
        this.oznaka = oznaka;
        this.pripadaFunkciji = pripadaFunkciji;

        if(oznaka == Oznaka.FUNKCIJA && pripadaFunkciji == null) {
            throw new RuntimeException("Ako se stvara djelokrug funkcije, mora se odrediti kojoj funkciji pripada");
        }
    }

    public int getBrojParametaraFunkcije() {
        return brojParametaraFunkcije;
    }

    public void setBrojParametaraFunkcije(int brojParametaraFunkcije) {
        this.brojParametaraFunkcije = brojParametaraFunkcije;
    }

    public Map<String, PrototipFunkcije> getDeklariraneFunkcije() {
        return deklariraneFunkcije;
    }

    public Map<String, Varijabla> getDeklariraneVarijable() {
        return deklariraneVarijable;
    }

    public List<Djelokrug> getChildren() {
        List<Djelokrug> res = new ArrayList<>();
        for(Node child : children) {
            res.add((Djelokrug)child);
        }

        return res;
    }

    public int getBrojLokalnihVarijabli() {
        return brojLokalnihVarijabli;
    }

    public void setBrojLokalnihVarijabli(int brojLokalnihVarijabli) {
        this.brojLokalnihVarijabli = brojLokalnihVarijabli;
    }

    public int getVelicina() {
        return velicina;
    }

    public void setVelicina(int velicina) {
        this.velicina = velicina;
    }

    public Djelokrug getParent() {
        return (Djelokrug)parent;
    }

    public Oznaka getOznaka() {
        return oznaka;
    }

    public PrototipFunkcije getPripadaFunkciji() {
        return pripadaFunkciji;
    }

    public void dodajVarijablu(String idn, String type, int brElem) {
        if(oznaka == Oznaka.GLOBALNI_DJELOKRUG) {
            deklariraneVarijable.put(idn, new GloblnaVarijabla(idn, type, brElem, this));
        } else if(oznaka == Oznaka.FUNKCIJA) {
            int l = pripadaFunkciji.getPareterTypes().size();
            if (l > brojParametaraFunkcije) {
                deklariraneVarijable.put(idn, new ParamaterVarijabla(idn, type, brElem, this));
            } else {
                deklariraneVarijable.put(idn, new StogVarijabla(idn, type, brElem, this));
            }
        } else {
            deklariraneVarijable.put(idn, new StogVarijabla(idn, type, brElem, this));
        }
    }

    public void dodajDeklaracijuFunkcije(PrototipFunkcije f) {
        deklariraneFunkcije.put(f.getIdn(), f);
    }

    public boolean postojiVarijabla(String var) {
        return deklariraneVarijable.containsKey(var);
    }

    public boolean postojiFunkcija(String f) {
        return deklariraneFunkcije.containsKey(f);
    }

    public Varijabla getVarijabla(String var) {
        return deklariraneVarijable.get(var);
    }

    public PrototipFunkcije getFunkcija(String f) {
        return  deklariraneFunkcije.get(f);
    }

    public Collection<Varijabla> getVarijable() {
        return deklariraneVarijable.values();
    }

    public Djelokrug getDjelokrugOfVariable(String idn) {
        Djelokrug d = this;
        while(d != null) {
            if(d.postojiVarijabla(idn)) return d;
            d = d.getParent();
        }
        return d;
    }

    public Djelokrug getDjelokrugOfFunction(String idn) {
        Djelokrug d = this;
        while(d != null) {
            if(d.postojiFunkcija(idn)) return d;
            d = d.getParent();
        }
        return d;
    }

    public static int getPocetnaVelicinaStoga() {
        return POCETNA_VELICINA_STOGA;
    }
}
