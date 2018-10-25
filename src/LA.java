import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LA {
    private static LAnalizator analizator = new LAnalizator();

    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder inputString = new StringBuilder();

        String line;
        while((line = input.readLine()) != null) {
            inputString.append(line).append('\n');
        }

        input = new BufferedReader(new FileReader("out4.txt"));
        String pocetnoStanje = input.readLine();
        while((line = input.readLine()) != null) {
            parseLine(line);
        }


        analizator.setUlazniNiz(inputString.toString());
        analizator.setPocetnoStanje(pocetnoStanje);
        analizator.pokreniAnalizator();
        for(UniformniZnak token : analizator.getTablicaUniformnihZnakova()) {
            System.out.println(token);
        }
    }

    private static void parseLine(String line) {
        String[] segmenti = line.split("\\t");
        String stanje = segmenti[0];
        LAutomatBuilder builder = new LAutomatBuilder();
        LAnalizator.BuilderAkcija akcije = analizator.getNoviBuilderAkcija();

        String[] prijelazi = segmenti[1].split("\\s");
        for(int i = 0; i < prijelazi.length; i += 3) {
            int s1 = Integer.parseInt(prijelazi[i]);
            int s2 = Integer.parseInt(prijelazi[i+1]);
            char c;

            String t = prijelazi[i+2];
            if(t.equals("\\t"))         c = '\t';
            else if(t.equals("\\_"))    c = ' ';
            else if(t.equals("\\n"))    c = '\n';
            else                        c = t.charAt(0);

            builder.dodajPrijelaz(s1, s2, c);
        }

        String[] eprijelazi = segmenti[2].split("\\s");
        for(int i = 0; i < eprijelazi.length; i += 2) {
            int s1 = Integer.parseInt(eprijelazi[i]);
            int s2 = Integer.parseInt(eprijelazi[i+1]);
            builder.dodajEPrijelaz(s1, s2);
        }

        String[] a = segmenti[3].split(",");

        if(a[0].equals("ODBACI")) {
            akcije.dodajAkcijuOdbaci();
        } else {
            akcije.dodajAkcijuDodajToken(a[0]);
        }

        for(int i = 1; i < a.length; ++i) {
            String akc = a[i].trim();
            if(akc.equals("NOVI_REDAK")) {
                akcije.dodajAkcijuNoviRedak();
            } else if( akc.startsWith("UDJI_U_STANJE")) {
                akcije.dodajAkcijuUdiUStanje(akc.split("\\s")[1]);
            } else if(akc.startsWith("VRATI_SE")) {
                akcije.dodajAkcijuVratiSe(Integer.parseInt(akc.split("\\s")[1]));
            }
        }

        builder.setPocetnoStanje(0);
        builder.setPrihvatljivoStanje(1);
        LAutomat automat = builder.getLAutomat();
        automat.setAkcije(akcije.getAkcije());
        analizator.dodajAutomat(stanje, automat);
    }

}