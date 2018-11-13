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

    public GSA() {
        terminals = new HashSet<>();
        variables = new HashSet<>();
        synTerminals = new HashSet<>();
        productions = new HashMap<String, List<List<String>>>();
        items = new HashMap<String, List<List<String>>>();
        nodes = new HashSet<>();
    }


    private void addProduction(String variable, List<String> rhs) { // RHS = right-hand side of the production
        List<List<String>> productionsRHSList = productions.get(variable); // returns list of lists
        productionsRHSList.add(rhs);
        productions.replace(variable, productionsRHSList);

    }

    private void addItems() {
        for (Map.Entry<String, List<List<String>>> entry : this.productions.entrySet()) {
            List<List<String>> allItemsForVariable = new ArrayList<>();
            List<String> productionItem = new ArrayList<>();

            boolean last = false;

            for (List<String> singleProduction : entry.getValue()) { // we have one entry for each key
                // for a production with n symbols, we get n+1 items

                int n = singleProduction.size();

                if (entry.getKey().equals("q0")) { // special case
                    productionItem = singleProduction;
                    allItemsForVariable.add(productionItem);
                    this.items.put("q0", allItemsForVariable);
                    last = true;
                    break; // one item for new starting variable
                }

                if (singleProduction.contains("$")) {
                    productionItem = new ArrayList<>();
                    productionItem.add(".");
                    allItemsForVariable.add(productionItem);  // samo jedna točkica za epsilon produkciju
                } else {
                    while (n >= 0) {
                        // single production as a list of variables/terminals
                        productionItem = new ArrayList<>(singleProduction);
                        productionItem.add(n, String.valueOf(this.delimeter));
                        allItemsForVariable.add(productionItem);
                        n--;
                    }
                }
            }
            if (!entry.getKey().equals("q0")) this.items.put(entry.getKey(), allItemsForVariable);


            // one iteration for every key
        }

    }

    private void initializeMap() {
        for (String v : this.variables) {
            this.productions.put(v, new ArrayList<>());
        }
        this.productions.put("q0", new ArrayList<>());
    }


  /*  private void initializeItems(){
        for(String v : this.variables){
            this.items.put(v, new ArrayList<>());
        }
        this.items.put("q0", new ArrayList<>());
    }*/

    public static Set<String> arrayIntoSet(String[] ulaz) {
        Set<String> rezultat = new HashSet<>();

        for (String el : ulaz) {
            rezultat.add(el);
        }
        return rezultat;
    }

    /*
        Method to generate epsilon-NFA nodes based on LR items
     */
    public void generateNodes() {

        for (Map.Entry<String, List<List<String>>> entry : this.items.entrySet()) {
            for (List<String> singleItem : entry.getValue()) {
                Node n = new Node(entry.getKey(), singleItem);
                this.nodes.add(n);
            }
        }

    }

    public static List<String> swapDotSymbol(List<String> list) {
        int index = list.indexOf(".");
        if(index == list.size()){
            System.out.println("ŠTA JE OVO");
        }
        Collections.swap(list, index, index+1);

        return list;
    }


    public static void main(String[] args) throws FileNotFoundException {

        Scanner sc = new Scanner(new FileReader("gramatika.txt"));
        GSA generator = new GSA();
        StringBuilder sb;

        for (int i = 1; i <= 3; i++) {
            sb = new StringBuilder(sc.nextLine());
            System.out.println(sb);
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
                System.out.print("GREŠKA!");
            }
        }

        System.out.println(generator.variables.toString());
        System.out.println(generator.terminals.toString());
        System.out.println(generator.synTerminals.toString());
        generator.initializeMap();


        String productionLHS = new String();
        List<String> productionRHS = new ArrayList<>();
        boolean add = false;
        while (sc.hasNext()) {
            String line = sc.nextLine();
            if (line.startsWith("<")) { // this indicated that we will encounter LHS of the production
                productionLHS = line.trim();
            } else if (line.contains("$")) { // found an epsilon production
                productionRHS.clear();
                productionRHS.add("$");
                add = true;
            } else { // found non-epsilon production
                String[] tempArrayRHS = line.substring(1).split("\\s+");
                System.out.println(tempArrayRHS[0]);
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
        productionRHS.add(generator.oldStart); // extra production

        generator.addProduction("q0", productionRHS);
        generator.addItems();

        generator.generateNodes();

        for (Node n : generator.nodes) {
            int index = n.getItemRHS().indexOf(".");
            System.out.println(n.getItemLHS() + " - > " + n.getItemRHS());
            if (index == n.getItemRHS().size() - 1 || n.getItemLHS().equals("q0")) {
                System.out.println("PAZI!!!");// special cases
            } else {
                Node next;
                for (Node singleNode : generator.nodes) { // iterating over all nodes
                    if ((singleNode.getItemLHS().equals(n.getItemLHS()))
                            && (singleNode.getItemRHS().equals(swapDotSymbol(new ArrayList(n.getItemRHS()))))) {
                        next = singleNode;
                        System.out.println("Ako primim: " + n.getItemRHS().get(index)
                                + "iduća je" + next.getItemLHS() + " -> " + next.getItemRHS());
                    }
                }


            }


        }
    }
}
