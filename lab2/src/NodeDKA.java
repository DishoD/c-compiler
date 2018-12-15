import java.util.*;

public class NodeDKA {
    private int oznaka;
    private Set<Node> stavke = new HashSet<>();
    public Map<String, NodeDKA> prijelazi = new HashMap<>();

    public NodeDKA(int oznaka) {
        this.oznaka = oznaka;
    }

    public void dodajStavku(Node stavka) {
        stavke.add(stavka);
    }

    public void dodajStavke(Set<Node> stavke) {
        this.stavke.addAll(stavke);
    }

    public void dodajPrijelaz(String znak, NodeDKA stanje) {
        prijelazi.put(znak, stanje);
    }

    public Set<Node> getStavke() {
        return stavke;
    }

    public int getOznaka() {
        return oznaka;
    }

    public void setOznaka(int oznaka) {
        this.oznaka = oznaka;
    }

    public Map<String, NodeDKA> getPrijelazi() {
        return prijelazi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeDKA nodeDKA = (NodeDKA) o;
        return Objects.equals(stavke, nodeDKA.stavke);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stavke);
    }
}

