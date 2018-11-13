import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
public class GSA{

private Set<String> terminals;
private Set<String> variables;
private Set<String> synTerminals;
private Map<String, List<String>> productions;

    public GSA(){
        terminals = new HashSet<>();
        variables = new HashSet<>();
        synTerminals = new HashSet<>();
        productions = new HashMap<String, List<String>>();
    }


private void addProduction(String variable, String rhs){ // RHS = right-hand side of the production
            List<String> productionsRHSList = productions.get(variable);
            productionsRHSList.add(rhs);
            productions.replace(variable, productionsRHSList);

}

private void initializeMap(){
        for(String v : this.variables){
            this.productions.put(v, new LinkedList<>());
        }
}

private static Set<String> poljeUSet(String[] ulaz) {
        Set<String> rezultat = new HashSet<>();

        for (String el : ulaz) {
            rezultat.add(el);
        }
        return rezultat; // vraca treeset!
    }


    public static void main(String[] args) throws FileNotFoundException{

        Scanner sc = new Scanner(new FileReader("gramatika.txt"));
        GSA generator = new GSA();
        StringBuilder sb;

        for(int i = 1; i <= 3; i++){
            sb = new StringBuilder(sc.nextLine());
            System.out.println(sb);
            if(sb.substring(0, 3).equals("%V ")){
                String[] variablesArray = sb.substring(3).split("\\s+");
                generator.variables = GSA.poljeUSet(variablesArray);

            } else if(sb.substring(0, 3).equals("%T ")){
                String[] terminalsArray = sb.substring(3).split("\\s+");
                generator.terminals = GSA.poljeUSet(terminalsArray);
            } else if(sb.substring(0, 5).equals("%Syn ")){
                String[] synTerminalsArray = sb.substring(5).split("\\s+");
                generator.synTerminals = GSA.poljeUSet(synTerminalsArray);
            } else {
                System.out.print("GREŠKA!");
            }
        }

        System.out.println(generator.variables.toString());
        System.out.println(generator.terminals.toString());
        System.out.println(generator.synTerminals.toString());
        generator.initializeMap();

        String productionLHS = null ;
        String productionRHS = null;
        boolean add = false;
        while(sc.hasNext()){
            String line = sc.nextLine();
            if(line.startsWith("<")){ // this indicated that we will encounter LHS of the production
                productionLHS = line.trim();
            } else if(line.contains("$")) { // found an epsilon production
                productionRHS = "$";
                add = true;
            } else { // found non-epsilon production
                productionRHS = line.substring(1);
                add = true;
            }



            if(add) {
                try {
                    generator.addProduction(productionLHS, productionRHS.trim());
                } catch(Exception e) {
                    System.out.println("GREŠKA");
                }
            }
            add = false;
        }
        System.out.println(generator.productions.entrySet());
    }
}