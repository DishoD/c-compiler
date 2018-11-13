import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
public class GSA{

private Set<String> terminals;
private Set<String> variables;
private Set<String> synTerminals;
private Map<String, String> productions;


private void addProduction(String variable, String rhs){ // RHS = right-hand side of the production
    productions.put(variable, rhs);
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

        System.out.print(generator.variables.toString());
        System.out.print(generator.terminals.toString());
        System.out.print(generator.synTerminals.toString());

        String productionLHS = null;
        String productionRHS = null;
        boolean add = false;
        while(sc.hasNext()){
            String production = sc.nextLine();
            System.out.println(production);
            if(!production.startsWith(" ")){ // this indicated that we will encounter LHS of the production
                productionLHS = production.substring(1).trim();
            } else if(production.startsWith(" ") && production.contains("$")) { // found an epsilon production
                productionRHS = "$";
                add = true;
            } else { // found non-epsilon production
                productionRHS = production.substring(1);
                add = true;
            }
            if(add) generator.productions.put(productionLHS, productionRHS);
            add = false;
        }
        System.out.println(generator.productions.entrySet());
    }
}