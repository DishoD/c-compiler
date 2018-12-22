import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SemantickiAnalizator {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        NezavrsniZnak root = parseInput(reader);
        reader.close();

        root.provjeri();

        //provjere nakon obilaska stabla
        Djelokrug globalni = TablicaZnakova.getGlobalniDjelokrug();
        PrototipFunkcije fMain = globalni.getFunkcija("main");
        PrototipFunkcije fTrazena = new PrototipFunkcije("main", "int", PrototipFunkcije.VOID_PARAMETER);
        if(fMain == null || !fMain.equals(fTrazena)) {
            System.out.println("main");
            return;
        }

        Queue<Djelokrug> red = new ArrayDeque<>();
        red.add(globalni);
        while(!red.isEmpty()) {
            Djelokrug trenutni = red.remove();
            red.addAll(trenutni.getChildren());

            for(Map.Entry<String, PrototipFunkcije> e : trenutni.getDeklariraneFunkcije().entrySet()) {
                if(!TablicaZnakova.postojiDefiniranaFunkcija(e.getKey())) {
                    System.out.println("funkcija");
                    return;
                }
            }
        }
    }

    private static NezavrsniZnak parseInput(BufferedReader input) throws IOException {
        Map<Integer, NezavrsniZnak> zadnji = new HashMap<>();
        zadnji.put(0, new PrijevodnaJedinica(null));
        input.readLine(); //izbaci prvu liniju koja uvijek mora biti: <prijevodna_jedinica>

        String line;
        while ((line = input.readLine()) != null) {
            String znak = line.trim();
            int prefix = line.length() - znak.length();
            NezavrsniZnak parent = zadnji.get(prefix-1);

            if(znak.charAt(0) == '<') {
                NezavrsniZnak child = produceNezavrsniZnak(znak, parent);
                parent.addChild(child);
                zadnji.put(prefix, child);
            } else {
                String[] comps = znak.split("\\s");
                UniformniZnak uz = new UniformniZnak(parent, comps[0], Integer.parseInt(comps[1]), comps[2]);
            }
        }

        return zadnji.get(0);
    }

    private static NezavrsniZnak produceNezavrsniZnak(String naziv, NezavrsniZnak parent) {
        if     (naziv.equals("<prijevodna_jedinica>")) return new PrijevodnaJedinica(parent);
        else if(naziv.equals("<aditivni_izraz>")) return new AditivniIzraz(parent);
        else if(naziv.equals("<bin_i_izraz>")) return new BinIIzraz(parent);
        else if(naziv.equals("<cast_izraz>")) return new CastIzraz(parent);
        //TODO

        throw new RuntimeException("Ne mogu producirati razred naziva: " + naziv);
    }
}
