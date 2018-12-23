import java.util.*;

public class Djelokrug extends Node {
    public enum Oznaka{
        PETLJA, FUNKCIJA, BLOK, GLOBALNI_DJELOKRUG
    }

    private Map<String, Varijabla> deklariraneVarijable = new HashMap<>();
    private Map<String, PrototipFunkcije> deklariraneFunkcije = new HashMap<>();

    private final Oznaka oznaka;
    private final PrototipFunkcije pripadaFunkciji;

    public Djelokrug(Node parent, Oznaka oznaka, PrototipFunkcije pripadaFunkciji) {
        super(parent);
        Objects.nonNull(oznaka);
        this.oznaka = oznaka;
        this.pripadaFunkciji = pripadaFunkciji;

        if(oznaka == Oznaka.FUNKCIJA && pripadaFunkciji == null) {
            throw new RuntimeException("Ako se stvara djelokrug funkcije, mora se odrediti kojoj funkciji pripada");
        }
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
}
