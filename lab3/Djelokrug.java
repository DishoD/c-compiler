import java.util.HashMap;
import java.util.Map;

public class Djelokrug extends Node {
    private Map<String, Varijabla> deklariraneVarijable = new HashMap<>();
    private Map<String, PrototipFunkcije> deklariraneFunkcije = new HashMap<>();

    public Djelokrug(Node parent) {
        super(parent);
    }

    public Djelokrug getParent() {
        return (Djelokrug)parent;
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

    public boolean postojiDeklariranaFunkcija(String f) {
        return deklariraneFunkcije.containsKey(f);
    }

    public Varijabla getVarijabla(String var) {
        return deklariraneVarijable.get(var);
    }

    public PrototipFunkcije getFunkcija(String f) {
        return  deklariraneFunkcije.get(f);
    }
}
