import com.sun.javafx.collections.MappingChange;

import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class GSA {

    private Set<String> terminals;
    private Set<String> variables;
    private Set<String> synTerminals;
    private Map<String, List<List<String>>> productions;
    private String oldStart;
    private Map<String, List<List<String>>> items;
    private char delimeter = '.';
    private Set<Node> nodes;
    private int countProductions = 0;


    // ########################################################

    private Map<String, Set<String>> variableSetFirst; // skup ZAPOCINJE za svaki nezavrsni znak

    /**
     * Defaultni konstruktor
     */
    public GSA() {
        terminals = new HashSet<>();
        variables = new HashSet<>();
        synTerminals = new HashSet<>();
        productions = new HashMap<String, List<List<String>>>();
        items = new HashMap<String, List<List<String>>>();
        nodes = new HashSet<>();

    }

    /**
     *  Dodavanje produkcija
     * @param variable - oznacava nezavrsni znak
     * @param rhs - desna strana produkcije
     */
    private void addProduction(String variable, List<String> rhs) {
        List<List<String>> productionsRHSList = productions.get(variable); // vraca listu lista
        productionsRHSList.add(rhs);
        productions.replace(variable, productionsRHSList);

    }

    /**
     * Dodavanje stavki na osnovu produkcija
     */
    private void addItems() {

        for (Map.Entry<String, List<List<String>>> entry : this.productions.entrySet()) {
            List<List<String>> allItemsForVariable = new ArrayList<>();
            List<String> productionItem = new ArrayList<>();


            for (List<String> singleProduction : entry.getValue()) {

                int n = singleProduction.size();

                if (entry.getKey().equals("q0")) { // poseban slučaj
                    productionItem = singleProduction;
                    productionItem.add(0, String.valueOf(this.delimeter));
                    allItemsForVariable.add(productionItem);
                    this.items.put("q0", allItemsForVariable);

                    break; // jedna stavka za novi nezavršni
                }

                if (singleProduction.contains("$")) {
                    productionItem = new ArrayList<>();
                    productionItem.add(".");
                    allItemsForVariable.add(productionItem);
                } else {
                    while (n >= 0) {
                        productionItem = new ArrayList<>(singleProduction);
                        productionItem.add(n, String.valueOf(this.delimeter));
                        allItemsForVariable.add(productionItem);
                        n--;
                    }
                }
            }
            if (!entry.getKey().equals("q0")) this.items.put(entry.getKey(), allItemsForVariable);
        }

    }

    /**
     * Postavljanje ključeva
     */
    private void initializeMap() {
        for (String v : this.variables) {
            this.productions.put(v, new ArrayList<>());
        }
        this.productions.put("q0", new ArrayList<>());
    }

    /**
     *
     * @param ulaz - Polje stringova
     * @return - Set stringova
     * */
    public static Set<String> arrayIntoSet(String[] ulaz) {
        Set<String> rezultat = new HashSet<>();

        for (String el : ulaz) {
            rezultat.add(el);
        }
        return rezultat;
    }

    /*
        Generiranje stanja za eps-NKA
     */
    public void generateNodes() {
        int nodeId = 1; // we need to asign integer to every existing node
        for (Map.Entry<String, List<List<String>>> entry : this.items.entrySet()) {
            for (List<String> singleItem : entry.getValue()) {
                Node n = new Node(nodeId, entry.getKey(), singleItem);
                this.nodes.add(n);
                nodeId++;
            }
        }

    }

    /**
     *
     * @param list desna strana stavke
     * @return lista sa zamijenjenim pozicijama elemenata
     */
    public static List<String> swapDotSymbol(List<String> list) {
        int index = list.indexOf(".");
        Collections.swap(list, index, index+1);

        return list;
    }

    /**
     * Prijelazi u ostala stanja
     */
    public void addNodeTransitions(){
        String nextSymbol = null;
        int index; // index of dot
        for (Node n : this.nodes) {

            index = n.getItemRHS().indexOf(".");
            if (index == n.getItemRHS().size() - 1 || n.getItemLHS().equals("q0")) {
               nextSymbol = this.oldStart; // poseban slucaj

            } else {
                for (Node singleNode : this.nodes) {
                    if ((singleNode.getItemLHS().equals(n.getItemLHS()))
                            && (singleNode.getItemRHS().equals(swapDotSymbol(new ArrayList(n.getItemRHS()))))) {
                        nextSymbol = n.getItemRHS().get(index+1);
                        n.dodajPrijelaz(nextSymbol, singleNode);

                    }
                }
            }
            // dodavanje eps. prijelaza samo ako je iza znaka tocke nezavrsni znak

            if(this.terminals.contains(nextSymbol) || index == n.getItemRHS().size() - 1) continue;

            for (Node singleNode : this.nodes) {
                if(singleNode.getItemLHS().equals(nextSymbol) && singleNode.getItemRHS().get(0).equals(".")) { // not to every item; the dot on index 0!
                    n.dodajEPrijelaz(singleNode);
                }
            }
        }
    }

    /**
     * Iterira kroz produkcije i ažurira skupove ZAPOČINJE za svaki nezavršni znak
     */
    public void findFirstSets() {

        this.variableSetFirst = new HashMap<>();


        for (int i = 1; i <= this.countProductions; i++) {

            for (Map.Entry<String, List<List<String>>> productionListEntry : this.productions.entrySet()) {

                for (List<String> oneP : productionListEntry.getValue()) {

                    Set<String> toBeAdded = new HashSet<>();

                    if (this.terminals.contains(oneP.get(0))) { // produkcija pocinje zavrsnim
                        toBeAdded.add(oneP.get(0));
                    } else if (this.variables.contains(oneP.get(0))) { // prod. pocinje nezavrsnim
                        // provjeri cime zapocinje nezavrsni znak na koji smo naisli
                     if(this.variableSetFirst.containsKey(oneP.get(0))) { // ako taj nezavrsni ima zabiljezene zavrsne kojima pocinje
                         toBeAdded.addAll(this.variableSetFirst.get(oneP.get(0)));
                         }
                    }


                    if(this.variableSetFirst.containsKey(productionListEntry.getKey()) == false){ // nije bilo zapisa
                            Set<String> temp = new HashSet<String>();
                            temp.addAll(toBeAdded);
                            this.variableSetFirst.put(productionListEntry.getKey(), temp);
                        } else { // prosiri skup
                            Set<String> temp = new HashSet<String>(this.variableSetFirst.get(productionListEntry.getKey()));
                            temp.addAll(toBeAdded);
                            this.variableSetFirst.put(productionListEntry.getKey(), temp);
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {

        Scanner sc = new Scanner(new FileReader("gramatika.txt"));
        GSA generator = new GSA();
        StringBuilder sb;

        for (int i = 1; i <= 3; i++) {
            sb = new StringBuilder(sc.nextLine());

            if (sb.substring(0, 3).equals("%V ")) {
                String[] variablesArray = sb.substring(3).split("\\s+");
                generator.oldStart = variablesArray[0]; // store old starting variable
                generator.variables = GSA.arrayIntoSet(variablesArray);

            } else if (sb.substring(0, 3).equals("%T ")) {
                String[] terminalsArray = sb.substring(3).split("\\s+");
                generator.terminals = GSA.arrayIntoSet(terminalsArray);
            } else if (sb.substring(0, 5).equals("%Syn ")) {
                String[] synTerminalsArray = sb.substring(5).split("\\s+");
                generator.synTerminals = GSA.arrayIntoSet(synTerminalsArray);
            } else {
                System.out.print("error!");
            }
        }

        System.out.println(generator.variables.toString());
        System.out.println(generator.terminals.toString());
        System.out.println(generator.synTerminals.toString());

        generator.initializeMap();

        String productionLHS = new String();
        List<String> productionRHS = new ArrayList<>();
        boolean add = false;
        while (sc.hasNext()){
            String line = sc.nextLine();
            if (line.startsWith("<")) { //  pronasli lijevu stranu produkcije
                productionLHS = line.trim();
            } else if (line.contains("$")) { // epsilon produkcija
                generator.countProductions++;
                productionRHS.clear();
                productionRHS.add("$");
                add = true;
            } else { // found non-epsilon production
                generator.countProductions++;
                // dodaj završni znak u skup ZAPOČINJE za nezavršni u productionLHS?

                String[] tempArrayRHS = line.substring(1).split("\\s+");
                productionRHS = new ArrayList<>(Arrays.asList(tempArrayRHS));

                add = true;
            }
            if (add) {
                try {
                    generator.addProduction(productionLHS, new ArrayList<>(productionRHS));
                } catch (Exception e) {
                    System.out.println("GREŠKA");
                }
            }
            add = false;
        }
        productionRHS.clear();
        productionRHS.add(generator.oldStart); // dodatna produkcija za novi nezavrsni

        generator.countProductions++;

        generator.addProduction("q0", productionRHS);
        generator.addItems();

        generator.generateNodes();
        generator.addNodeTransitions();

        Node initialNode = null;
        for(Node singleNode : generator.nodes){
            System.out.println(singleNode);
            singleNode.printAllTransitions();
            if(singleNode.getOznaka() == generator.nodes.size()){
                initialNode = singleNode;
            }
        }
        System.out.println("ukupno: " + generator.nodes.size());
        System.out.println("br prod : " + generator.countProductions);


        generator.findFirstSets();
        System.out.println(generator.variableSetFirst.toString());

        LAutomat a = new LAutomat(initialNode, generator.nodes);



    }
}
