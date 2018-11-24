import com.sun.javafx.collections.MappingChange;

import javax.print.DocFlavor;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class GSA {

    private Set<String> terminals;
    private Set<String> variables;
    private Set<String> synTerminals;
    private Map<String, List<List<String>>> productions;
    private String oldStart;
    private Map<String, List<List<String>>> nodeKernels;
    private char delimeter = '.';
    private char stop = '⊥';
    private Set<Node> nodes; // kostur za prave LR(1) stavke, tj. stanja eps enka
    private int countProductions = 0;
    private Set<Node> epsNodes;
    private Set<String> emptyVariables;

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
        nodeKernels = new HashMap<String, List<List<String>>>();
        nodes = new HashSet<>();
        epsNodes = new HashSet<>();
        emptyVariables = new HashSet<>();

    }

    /**
     * Dodavanje produkcija
     *
     * @param variable - oznacava nezavrsni znak
     * @param rhs      - desna strana produkcije
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

            this.nodeKernels.put(entry.getKey(), allItemsForVariable);
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
     * @param ulaz - Polje stringova
     * @return - Set stringova
     */
    public static Set<String> arrayIntoSet(String[] ulaz) {
        Set<String> rezultat = new HashSet<>();

        for (String el : ulaz) {
            rezultat.add(el);
        }
        return rezultat;
    }

    /**
     * Generiranje STANJA za eps-NKA, to nisu jezgre stavki
     * POPRAVITI; ZASAD JE GENERIRANJE  jezgri stavki
     * prebacivanje iz vrijednosti entryja mape u skup
     */
    public void generateNodeKernels() {

        int nodeId = 1; // we need to asign integer to every existing node
        for (Map.Entry<String, List<List<String>>> entry : this.nodeKernels.entrySet()) {
            for (List<String> singleItem : entry.getValue()) {
                Node n = new Node(nodeId, entry.getKey(), singleItem);
                this.nodes.add(n);
                nodeId++;
            }
        }
        this.countProductions = nodeId - 1; // treba - 1
    }

    /* iz node kernela počevši od q0 pronaći ostale skupove i pridijeliti ih stanima*/
    public void findSetsForNodes() {
        int label = 1;
        Node firstNode = null;

        for (Node n : this.nodes) {
            if (n.getOznaka() == this.countProductions) {
                firstNode = new Node(label, n.getItemLHS(), n.getItemRHS());
                label++;
                firstNode.EPrijelazi = new ArrayList<>(n.getEPrijelazi());
                firstNode.prijelazi = new HashMap<>(n.getPrijelazi());
                break;
            }
        }

        firstNode.addToSkup(String.valueOf(this.stop)); // pronasli smo prvoga...

        Node n = firstNode; // n je radni cvor
        this.epsNodes.add(firstNode); // prvo pravo stanje eps-NKA

        boolean addEps = true;
        List<Node> stackForNextNodes = new ArrayList<>(); // koje sve treba obraditi

        List<Node> usedNodes = new ArrayList<>(); // iskoristeni

        boolean f1 = false, f2 = false;
        while (true) {
            Set<String> SetToAdd = new HashSet<>();

            usedNodes.add(n); // oznacavamo da smo cvor iskoristili
            int index = n.getItemRHS().indexOf(String.valueOf(this.delimeter));// pronadji indeks tocke

            if (index == n.getItemRHS().size() - 1) { // samo tockica i nista drugo, idemo dalje
                if (stackForNextNodes.size() == 0 || n == null) break;
                n = stackForNextNodes.get(0); // uzimanje sa stoga, iduci cvor
                stackForNextNodes.remove(0); // skidanje sa stoga

                continue;
            } else if ((index + 1) == (n.getItemRHS().size() - 1)) {

                if (this.terminals.contains(n.getItemRHS().get(index + 1))) {
                    addEps = false;
                } else {
                    SetToAdd.addAll(new HashSet<>(n.getSkup()));
                    addEps = true;
                }
            } else {
                if (this.terminals.contains(n.getItemRHS().get(index + 1))) { // nema eps. prijelaze!
                    // System.out.println("Nema eps. .");
                    addEps = false;
                } else {
                    addEps = true; // nezavrsni neposredno iza tockice
                    // ######################################################### najzeznutiji slucaj
                    SetToAdd.addAll(new HashSet<>(this.variableSetFirst.get(n.getItemRHS().get(index + 1))));

                    // prazni nezavrsni odma iza tocke
                    // beta niz, ono iza nezavrsnog znaka
                    boolean isEmpty = true;
                    int index2 = index + 2; // pocetak bete

                    while (isEmpty && index2 <= n.getItemRHS().size() - 1) {

                        if (this.emptyVariables.contains(n.getItemRHS().get(index2))) {
                            SetToAdd.addAll(this.variableSetFirst.get(n.getItemRHS().get(index + 2))); // unija zapocinje od beta i ovaj nas skup
                        } else { // nakon nezavrsnog se ne nalazi prazni nezavrsni znak; u ovoj grani se zaustavljamo
                            if (this.terminals.contains(n.getItemRHS().get(index2))) { // zavrsni
                                SetToAdd.add(n.getItemRHS().get(index2));
                            } else {
                                SetToAdd.addAll(new HashSet<>(this.variableSetFirst.get(n.getItemRHS().get(index2))));
                            }
                            isEmpty = false;
                        }
                        index2++;
                    } // izlazak iz while petlje

                    // ako je niz beta niz praznih nezavrsnih tada
                    if (index2 >= n.getItemRHS().size() && this.emptyVariables.contains(
                            n.getItemRHS().get(n.getItemRHS().size() - 1)
                    )) {
                        SetToAdd.addAll(n.getSkup()); // udzbenik 4. c ii)
                    }
                    // ################################################################
                }
            }
            if (addEps) {
                List<Node> s = n.getEPrijelazi();
                // stackForNextNodes.addAll(s); // epsilon stanja dodamo na obradu!
                for (Node e : s) {

                    // e.addToSkup(SetToAdd); // pridijelimo im skupove koje trebaju imat
                    Node temp = new Node(label, e.getItemLHS(), e.getItemRHS());
                    // obavezno
                    temp.addToSkup(new HashSet<>(SetToAdd));
                    temp.prijelazi = new HashMap<>(e.getPrijelazi());
                    temp.EPrijelazi = new ArrayList<Node>(e.getEPrijelazi());
                    if (!this.epsNodes.contains(temp)) {
                        f1 = true;
                        this.epsNodes.add(temp); // dodamo LR(1) stavku!
                    }
                    if (!usedNodes.contains(temp)) {
                        f2 = true;
                        stackForNextNodes.add(temp); // moramo pamtit na stogu koji smo dodali
                    }
                    if (f1 && f2) {
                        label++;
                    }
                    f1 = f2 = false; // reset

                }
            }
            // ####################  "obicnim" prijelazim dodaj iste skupove, obicne treba dodati!
            //if (!n.getItemLHS().equals("q0")) { // ne uzimamo u obzir dodatni pocetni nezavrsni znak
            for (Map.Entry<String, List<Node>> m : n.getPrijelazi().entrySet()) {
                List<Node> lista = m.getValue();
                for (Node strelicaU : lista) {
                    //   strelicaU.addToSkup(n.getSkup()); // prepišemo!!!
                    Node temp = new Node(label, strelicaU.getItemLHS(), strelicaU.getItemRHS());
                    temp.EPrijelazi = new ArrayList<Node>(strelicaU.getEPrijelazi());
                    temp.addToSkup(new HashSet<>(n.getSkup()));
                    temp.prijelazi = new HashMap<>(strelicaU.prijelazi);
                    if (!this.epsNodes.contains(temp)) {
                        f1 = true;
                        this.epsNodes.add(temp);
                    }

                    if (!usedNodes.contains(temp)) {
                        f2 = true;
                        stackForNextNodes.add(temp);
                    }
                    if (f1 && f2) {
                        label++;
                    }
                    f1 = f2 = false;
                }
            }
            //} zbog ovog je za gramatika100 bilo 107 umjesto 108

            // ispraznili smo sve
            n = stackForNextNodes.get(0); // uzimanje sa stoga, iduci cvor
            stackForNextNodes.remove(0); // skidanje sa stoga*/
            if (stackForNextNodes.size() == 0 && n == null) break;

        } //kraj while petlje

    }


    /**
     * ubaciti metoda koja ce provjeriti postoji li već takav identican cvor
     */
    /**
     * @param list desna strana stavke
     * @return lista sa zamijenjenim pozicijama elemenata
     */
    public static List<String> swapDotSymbol(List<String> list) {
        int index = list.indexOf(".");
        Collections.swap(list, index, index + 1);

        return list;
    }

    /**
     * Prijelazi u ostala stanja
     */
    public void addNodeTransitions() {
        String nextSymbol = null;
        int index; // index of dot
        for (Node n : this.nodes) {

            index = n.getItemRHS().indexOf(".");
            if (index == n.getItemRHS().size() - 1) {
                nextSymbol = this.oldStart; // poseban slucaj
            } else {
                for (Node singleNode : this.nodes) {
                    if ((singleNode.getItemLHS().equals(n.getItemLHS()))
                            && (singleNode.getItemRHS().equals(swapDotSymbol(new ArrayList(n.getItemRHS()))))) {
                        nextSymbol = n.getItemRHS().get(index + 1);
                        singleNode.setSkup(n.getSkup()); // dodano
                        n.dodajPrijelaz(nextSymbol, singleNode);

                    }
                }
            }
            // dodavanje eps. prijelaza samo ako je iza znaka tocke nezavrsni znak

            if (this.terminals.contains(nextSymbol) || index == n.getItemRHS().size() - 1) continue;

            for (Node singleNode : this.nodes) {
                if (singleNode.getItemLHS().equals(nextSymbol) && singleNode.getItemRHS().get(0).equals(".")) { // not to every item; the dot on index 0!
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

                        if (this.variableSetFirst.containsKey(oneP.get(0))) { // ako taj nezavrsni ima zabiljezene zavrsne kojima pocinje

                            if (!productionListEntry.getKey().equals("q0"))
                                toBeAdded.addAll(this.variableSetFirst.get(oneP.get(0)));
                            else { // iznimno za novo pocetno stanje
                                toBeAdded.add(String.valueOf(this.stop));
                            }
                        }
                    }

                    if (this.variableSetFirst.containsKey(productionListEntry.getKey()) == false) { // nije bilo zapisa
                        Set<String> temp = new HashSet<>();
                        temp.addAll(toBeAdded);
                        this.variableSetFirst.put(productionListEntry.getKey(), temp);
                    } else { // prosiri skup
                        Set<String> temp = new HashSet<>(this.variableSetFirst.get(productionListEntry.getKey()));
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
                generator.variables.add("q0");

            } else if (sb.substring(0, 3).equals("%T ")) {
                String[] terminalsArray = sb.substring(3).split("\\s+");
                generator.terminals = GSA.arrayIntoSet(terminalsArray);
            } else if (sb.substring(0, 5).equals("%Syn ")) {
                String[] synTerminalsArray = sb.substring(5).split("\\s+");
                generator.synTerminals = GSA.arrayIntoSet(synTerminalsArray);
            } else {
                System.out.print("Error!");
            }
        }

        generator.initializeMap();

        String productionLHS = new String();
        List<String> productionRHS = new ArrayList<>();
        boolean add = false;
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.startsWith("<")) { //  pronasli lijevu stranu produkcije
                productionLHS = line.trim();
            } else if (line.contains("$")) { // epsilon produkcija
                generator.emptyVariables.add(productionLHS); //
                generator.countProductions++;
                productionRHS.clear();
                productionRHS.add("$");
                add = true;
            } else { //
                generator.countProductions++;
                String[] tempArrayRHS = line.substring(1).split("\\s+");
                productionRHS = new ArrayList<>(Arrays.asList(tempArrayRHS));

                add = true;
            }
            if (add) {
                try {
                    generator.addProduction(productionLHS, new ArrayList<>(productionRHS));
                } catch (Exception e) {

                }
            }
            add = false;
        }
        productionRHS.clear();
        productionRHS.add(generator.oldStart); // dodatna produkcija za novi nezavrsni

        generator.countProductions++;

        generator.addProduction("q0", productionRHS);


        generator.findFirstSets();
        generator.addItems();

        generator.generateNodeKernels();
        generator.addNodeTransitions();

        Node initialNode = null;
        for (Node singleNode : generator.nodes) {

            if (singleNode.getOznaka() == generator.nodes.size()) {
                initialNode = singleNode;
            }
        }



        generator.findSetsForNodes();
        List<Node> poredani = new ArrayList<>(generator.epsNodes);
        LAutomat a = null;
        poredani.sort((o1, o2) -> Integer.valueOf(o1.getOznaka()).compareTo(Integer.valueOf(o2.getOznaka())));
        boolean prvi = true;
        for (Node n : poredani) {
            System.out.println(n);
            if(prvi){
                prvi = false;
                a = new LAutomat(n, generator.epsNodes);
            }
        }



       a.pokreniAutomat();
        System.out.println(a.getStatus());
        a.prijelaz("<B>");
        System.out.println(a.getStatus());

    }

}
