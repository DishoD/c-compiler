import java.util.HashMap;
import java.util.Map;

public class Djelokrug extends Node {
    public static enum Oznaka{
        PETLJA, FUNKCIJA, BLOK, GLOBALNI_DJELOKRUG
    }

    private Map<String, Varijabla> deklariraneVarijable = new HashMap<>();
    private Map<String, PrototipFunkcije> deklariraneFunkcije = new HashMap<>();

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

    public Djelokrug getParent() {
        return (Djelokrug)parent;
    }

    public Oznaka getOznaka() {
        return oznaka;
    }

    public PrototipFunkcije getPripadaFunkciji() {
        return pripadaFunkciji;
    }

    public void dodajVarijablu(Varijabla var) {
        deklariraneVarijable.put(var.getIdn(), var);
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

    public Djelokrug getDjelokrugOfVariable(String idn) {
        Djelokrug d = this;
        while(d != null) {
            if(d.postojiVarijabla(idn)) return d;
        }
        return null;
    }

    public Djelokrug getDjelokrugOfFunction(String idn) {
        Djelokrug d = this;
        while(d != null) {
            if(d.postojiFunkcija(idn)) return d;
        }
        return null;
    }
}
