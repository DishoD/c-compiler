import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SA {
    private static SAnalizator analizator = new SAnalizator();

    public static void main (String[] args) throws IOException {

        //BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader input = new BufferedReader(new FileReader("simplePpjLang_veci.in"));

        analizator.popuniUniformneZnakove(input);

        input = new BufferedReader(new FileReader("definicije.txt"));

        analizator.popuniTablicuAkcija(input);
        analizator.popuniTablicuNovoStanje(input);
        analizator.popuniProdukcije(input);
        analizator.popuniSinkronizacijskeZnakove(input);
        analizator.odrediPocetnoStanje(input);
        analizator.popuniNezavrsne(input);
        analizator.popuniZavrsne(input);
        analizator.popuniStop(input);

        input.close();

        analizator.pokreni();
    }
}

