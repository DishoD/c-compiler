import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class Node {
    //private int oznaka;
    private Map<Character, List<Node>> prijelazi = new HashMap<>();
    private List<Node> EPrijelazi = new ArrayList<>();

    public void dodajEPrijelaz(Node node) {
        EPrijelazi.add(node);
    }

    public void dodajPrijelaz(char znak, Node node) {
        prijelazi.merge(znak, new ArrayList<>(), new BiFunction<List<Node>, List<Node>, List<Node>>() {
            @Override
            public List<Node> apply(List<Node> nodes, List<Node> nodes2) {
                nodes.add(node);
                return nodes;
            }
        });
    }

    public List<Node> getEPrijelazi() {
        return EPrijelazi;
    }

    public List<Node> getPrijelazi(char znak) {
        return prijelazi.get(znak);
    }
}
