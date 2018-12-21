import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SemantickiAnalizator {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    }

    private static NezavrsniZnak parseInput(BufferedReader input) {
        //TODO

        return null;
    }

    private static NezavrsniZnak produceNezavrsniZnak(String naziv, NezavrsniZnak parent) {
        if     (naziv.equals("<prijevodna_jedinica>")) return new PrijevodnaJedinica(parent);
        else if(naziv.equals("<aditivni_izraz>")) return new AditivniIzraz(parent);
        else if(naziv.equals("<bin_i_izraz>")) return new BinIIzraz(parent);
        else if(naziv.equals("<cast_izraz>")) return new CastIzraz(parent);


        throw new RuntimeException("Ne mogu producirati razred naziva: " + naziv);
    }
}
