import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Čvor(stanje) u e-NKA.
 */
public class Node {
    private int oznaka;
    private Map<Character, List<Node>> prijelazi = new HashMap<>();
    private List<Node> EPrijelazi = new ArrayList<>();

    public Node(int oznaka) {
        this.oznaka = oznaka;
    }

    /**
     * Dodaje epsilon prijelaz iz trenutnog node-a u zadani node.
     *
     * @param node odredišni node e-prijelaza
     */
    public void dodajEPrijelaz(Node node) {
        EPrijelazi.add(node);
    }

    /**
     * Dodaje prijelaz iz trenutnog čvora u zadani čvor za zadani znak.
     *
     * @param znak znak prijelaza
     * @param node odredišni čvor prijelaza
     */
    public void dodajPrijelaz(char znak, Node node) {
        List<Node> n = prijelazi.get(znak);
        if(n == null) {
            n = new ArrayList<>();
            n.add(node);
        } else {
            n.add(node);
        }
    }

    /**
     * @return Sva stanja e-prijelaza iz ovog čvora.
     */
    public List<Node> getEPrijelazi() {
        return EPrijelazi;
    }

    /**
     * @param znak znak prijelaza
     * @return Sva stanja prijelaza za zadani znak iz ovog stanja.
     */
    public List<Node> getPrijelazi(char znak) {
        return prijelazi.get(znak);
    }

    /**
     * @return oznaka čvora
     */
    public int getOznaka() {
        return oznaka;
    }
}
