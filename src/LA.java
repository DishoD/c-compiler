import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LA {
    private static LAnalizator analizator = new LAnalizator();

    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new FileReader("c_program.c"));
        StringBuilder inputString = new StringBuilder();

        String line;
        while((line = input.readLine()) != null) {
            inputString.append(line).append('\n');
        }

        input = new BufferedReader(new FileReader("out.txt"));
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
        String[] segmenti = line.split("--");
        String stanje = segmenti[0];
        LAutomatBuilder builder = new LAutomatBuilder();
        LAnalizator.BuilderAkcija akcije = analizator.getNoviBuilderAkcija();

        for(String prijelaz : segmenti[1].split(",")){
            String[] temp = prijelaz.trim().split("\\s");
            if(temp[0].isEmpty()) continue;
            int s1 = Integer.parseInt(temp[0].trim());
            int s2 = Integer.parseInt(temp[1].trim());
            char c = 0;

            if(temp.length < 3) {
                c = ',';
            } else {
                if (temp[2].length() > 1) {
                    if (temp[2].trim().equals("\\_")) c = ' ';
                    if (temp[2].trim().equals("\\n")) c = '\n';
                    if (temp[2].trim().equals("\\t")) c = '\t';
                } else {
                    c = temp[2].trim().charAt(0);
                }
            }
            builder.dodajPrijelaz(s1, s2, c);
        }

        for(String eprijelaz : segmenti[2].split(",")){
            String[] temp = eprijelaz.trim().split("\\s");
            int s1 = Integer.parseInt(temp[0].trim());
            int s2 = Integer.parseInt(temp[1].trim());
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