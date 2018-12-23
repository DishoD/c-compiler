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
            System.out.print("main" + '\n');
            System.exit(0);
        }

        Queue<Djelokrug> red = new ArrayDeque<>();
        red.add(globalni);
        while(!red.isEmpty()) {
            Djelokrug trenutni = red.remove();
            red.addAll(trenutni.getChildren());

            for(Map.Entry<String, PrototipFunkcije> e : trenutni.getDeklariraneFunkcije().entrySet()) {
                PrototipFunkcije f = TablicaZnakova.getDefiniranaFunkcija(e.getKey());
                if(f == null || !f.equals(e.getValue())) {
                    System.out.print("funkcija" + '\n');
                    System.exit(0);
                }
            }
        }
    }

    private static NezavrsniZnak parseInput(BufferedReader input) throws IOException {
        Map<Integer, NezavrsniZnak> zadnji = new HashMap<>();

        String line;
        while ((line = input.readLine()) != null) {
            String znak = line.trim();
            int prefix = line.length() - znak.length();
            NezavrsniZnak parent = zadnji.get(prefix-1);

            if(znak.charAt(0) == '<') {
                NezavrsniZnak child = produceNezavrsniZnak(znak, parent);
                if(parent != null) parent.addChild(child);
                zadnji.put(prefix, child);
            } else {
                String token = znak.substring(0, znak.indexOf(' '));
                String ostatak = znak.substring(znak.indexOf(' ') + 1);
                String red = ostatak.substring(0, ostatak.indexOf(' '));
                ostatak = ostatak.substring(ostatak.indexOf(' ') + 1);
                String grupirani = ostatak.trim();
                UniformniZnak uz = new UniformniZnak(parent, token, Integer.parseInt(red), grupirani);
                parent.addChild(uz);
            }
        }

        return zadnji.get(0);
    }




    private static NezavrsniZnak produceNezavrsniZnak(String naziv, NezavrsniZnak parent) {
        if     (naziv.equals("<primarni_izraz>"))             return new PrimarniIzraz(parent);
        else if(naziv.equals("<postfiks_izraz>"))             return new PostfiksIzraz(parent);
        else if(naziv.equals("<lista_argumenata>"))           return new ListaArgumenata(parent);
        else if(naziv.equals("<unarni_izraz>"))               return new UnarniIzraz(parent);
        else if(naziv.equals("<unarni_operator>"))            return new UnarniOperator(parent);
        else if(naziv.equals("<cast_izraz>"))                 return new CastIzraz(parent);
        else if(naziv.equals("<ime_tipa>"))                   return new ImeTipa(parent);
        else if(naziv.equals("<specifikator_tipa>"))          return new SpecifikatorTipa(parent);
        else if(naziv.equals("<multiplikativni_izraz>"))      return new MultiplikativniIzraz(parent);
        else if(naziv.equals("<aditivni_izraz>"))             return new AditivniIzraz(parent);
        else if(naziv.equals("<odnosni_izraz>"))              return new OdnosniIzraz(parent);
        else if(naziv.equals("<jednakosni_izraz>"))           return new JednakosniIzraz(parent);
        else if(naziv.equals("<bin_i_izraz>"))                return new BinIIzraz(parent);
        else if(naziv.equals("<bin_xili_izraz>"))             return new BinXiliIzraz(parent);
        else if(naziv.equals("<bin_ili_izraz>"))              return new BinIliIzraz(parent);
        else if(naziv.equals("<log_i_izraz>"))                return new LogIIzraz(parent);
        else if(naziv.equals("<log_ili_izraz>"))              return new LogIliIzraz(parent);
        else if(naziv.equals("<izraz_pridruzivanja>"))        return new IzrazPridruzivanja(parent);
        else if(naziv.equals("<izraz>"))                      return new Izraz(parent);
        else if(naziv.equals("<slozena_naredba>"))            return new SlozenaNaredba(parent);
        else if(naziv.equals("<lista_naredbi>"))              return new ListaNaredbi(parent);
        else if(naziv.equals("<naredba>"))                    return new Naredba(parent);
        else if(naziv.equals("<izraz_naredba>"))              return new IzrazNaredba(parent);
        else if(naziv.equals("<naredba_grananja>"))           return new NaredbaGrananja(parent);
        else if(naziv.equals("<naredba_petlje>"))             return new NaredbaPetlje(parent);
        else if(naziv.equals("<naredba_skoka>"))              return new NaredbaSkoka(parent);
        else if(naziv.equals("<prijevodna_jedinica>"))        return new PrijevodnaJedinica(parent);
        else if(naziv.equals("<vanjska_deklaracija>"))        return new VanjskaDeklaracija(parent);
        else if(naziv.equals("<definicija_funkcije>"))        return new DefinicijaFunkcije(parent);
        else if(naziv.equals("<lista_parametara>"))           return new ListaParametara(parent);
        else if(naziv.equals("<deklaracija_parametra>"))      return new DeklaracijaParametra(parent);
        else if(naziv.equals("<lista_deklaracija>"))          return new ListaDeklaracija(parent);
        else if(naziv.equals("<deklaracija>"))                return new Deklaracija(parent);
        else if(naziv.equals("<lista_init_deklaratora>"))     return new ListaInitDeklaratora(parent);
        else if(naziv.equals("<init_deklarator>"))            return new InitDeklarator(parent);
        else if(naziv.equals("<izravni_deklarator>"))         return new IzravniDeklarator(parent);
        else if(naziv.equals("<inicijalizator>"))             return new Inicijalizator(parent);
        else if(naziv.equals("<lista_izraza_pridruzivanja>")) return new ListaIzrazaPridruzivanja(parent);

        throw new RuntimeException("Ne mogu producirati razred naziva: " + naziv);
    }
}
