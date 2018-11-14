import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.StringBuilder;

/**
 * Čvor(stanje) u e-NKA.
 */
public class Node {
    private String itemLHS;
    private List<String> itemRHS;
    private Map<String, List<Node>> prijelazi = new HashMap<>();
    private List<Node> EPrijelazi = new ArrayList<>();

    public Node(String itemLHS, List<String> itemRHS) {
        this.itemRHS = itemRHS;
        this.itemLHS = itemLHS;
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
    public void dodajPrijelaz(String znak, Node node) {
        List<Node> n = prijelazi.get(znak);
        if(n == null) {
            n = new ArrayList<>();
            n.add(node);
            prijelazi.put(znak,n);
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
     * @param znak znak prijelaza (završni ili nezavršni znak)
     * @return Sva stanja prijelaza za zadani znak iz ovog stanja.
     */
    public List<Node> getPrijelazi(String znak) {
        return prijelazi.get(znak);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Node)) return false;
        Node n = (Node)o;
        return (this.itemRHS == n.getItemRHS() && this.itemLHS == n.getItemLHS());
    }

    /**
     * @return right-hand side of the item
     */
    public List<String> getItemRHS() {
        return itemRHS;
    }

    /**
     * @return left-hand side of the item
     */
    public String getItemLHS(){
        return itemLHS;
    }

    @Override
    public String toString(){
        StringBuilder temp = new StringBuilder();
        for(String singleSimbol : this.getItemRHS()){
            temp.append(singleSimbol + " ");
        }

        String s =  System.lineSeparator() + this.itemLHS + "->" + temp.toString();
        return s;
    }

    public void printAllTransitions(){
        for(Map.Entry<String, List<Node>> p : this.prijelazi.entrySet()){
            System.out.print("znak = " + p.getKey());
            for(Node n : p.getValue()){
                System.out.println(n);
            }
            System.out.println("-------");
        }
        if(this.EPrijelazi.isEmpty()){
            System.out.println("Nema eps. prijelaza");

        } else {
            for (Node e : this.EPrijelazi) {
                System.out.println("eps " + e);
            }
        }
        System.out.println("#####################################################");

    }

}
