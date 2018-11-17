import java.util.*;
import java.lang.StringBuilder;

/**
 * Čvor(stanje) u e-NKA.
 */
public class Node {
    private int oznaka;
    private String itemLHS;
    private List<String> itemRHS;
    public Map<String, List<Node>> prijelazi = new HashMap<>();
    public List<Node> EPrijelazi = new ArrayList<>();

    // ##################################################

    private Set<String> skup; // predstavlja Skup unutar {}, npr. S -> * A B, {b,d}

    public Node(int oznaka, String itemLHS, List<String> itemRHS) {
        this.oznaka = oznaka;
        this.itemRHS = itemRHS;
        this.itemLHS = itemLHS;
        skup = new HashSet<>();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + itemLHS.hashCode();
        result = prime * result + itemRHS.hashCode();
        result = prime * result + oznaka;
        return result;
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

    /**
     * Overloadana metoda koja će vratit mapu svih prijelaza određenog čvora (bez eps.!)
     *
     * @return mapa svih prijelaza
     */
    public Map<String, List<Node>> getPrijelazi() {
        return this.prijelazi;
    }


    @Override
    public boolean equals(Object o){
        if(!(o instanceof Node)) return false;
        Node n = (Node)o;
        return (this.itemRHS.equals(n.getItemRHS()) && this.itemLHS.equals(n.getItemLHS()) &&
                this.skup.equals(n.skup));
    }



    /**
     * @return desna strana stavke
     */
    public List<String> getItemRHS() {
        return itemRHS;
    }

    /**
     * @return lijeva strana stavke
     */
    public String getItemLHS(){
        return itemLHS;
    }

    /**
     *
     * @return oznaka stavke(stanja)
     */
    public int getOznaka(){
        return oznaka;
    }

    public Set<String> getSkup() {
        return skup;
    }

    public void setSkup(Set<String> skup) {
        this.skup = skup;
    }


    /**
     *
     * @param el element koji će se dodati u skup
     */
    public void addToSkup(String el){
        this.skup.add(el);
    }

    /**
     * Overloading gornje metode addToSkup
     * @return
     */
    public void addToSkup(Set<String> skup){
        this.skup.addAll(skup);
    }
    @Override
    public String toString(){
        StringBuilder temp = new StringBuilder();
        for(String singleSimbol : this.getItemRHS()){
            temp.append(singleSimbol + " ");
        }

        String s =  System.lineSeparator() + this.oznaka + ": " + this.itemLHS + "->" + temp.toString() + "{" + this.skup.toString() + "}";
        return s;
    }

    /**
     * Ispiši sve prijelaze
     */
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
                System.out.println("eps. transition: " + e);
            }
        }
        System.out.println("#####################################################");

    }

}
