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

        input = new BufferedReader(new FileReader("definicija.txt"));
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
            String[] temp = prijelaz.split("\\s");
            int s1 = Integer.parseInt(temp[0]);
            int s2 = Integer.parseInt(temp[1]);
            char c = 0;

            if(temp[2].length() > 1) {
                if(temp[2].equals("\\_")) c = ' ';
                if(temp[2].equals("\\n")) c = '\n';
                if(temp[2].equals("\\t")) c = '\t';
            } else {
                c = temp[2].charAt(0);
            }
            builder.dodajPrijelaz(s1, s2, c);
        }

        for(String eprijelaz : segmenti[2].split(",")){
            String[] temp = eprijelaz.split("\\s");
            int s1 = Integer.parseInt(temp[0]);
            int s2 = Integer.parseInt(temp[1]);
            builder.dodajEPrijelaz(s1, s2);
        }

        //TODO parsiranje akcija

        builder.setPocetnoStanje(0);
        builder.setPrihvatljivoStanje(1);
        LAutomat automat = builder.getLAutomat();
        automat.setAkcije(akcije.getAkcije());
        analizator.dodajAutomat(stanje, automat);
    }

}