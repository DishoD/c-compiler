import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class SAnalizator {
    private List<String> leksickeJedinke = new ArrayList<>();
    private List<ElementStoga> stog = new ArrayList<>();
    private List<ElementStoga> pomocniStog = new ArrayList<>();
    private Map<Integer, Map<String, ElementTablice>> tablicaAkcija = new HashMap<>();
    private Map<Integer, Map<String, ElementTablice>> tablicaNovoStanje = new HashMap<>();
    private List<UniformniZnak> uniformniZnakovi = new ArrayList<>();
    private List<String> produkcije = new ArrayList<>();
    private List<String> sinkronizacijskiZnakovi = new ArrayList<>();
    private List<String> zavrsni = new ArrayList<>();
    private List<String> nezavrsni = new ArrayList<>();
    private String stop = "#";

    public SAnalizator(Map<Integer, Map<String, ElementTablice>> tablicaAkcija,
                       Map<Integer, Map<String, ElementTablice>> tablicaNovoStanje,
                       List<UniformniZnak> uniformniZnakovi,
                       List<String> produkcije,
                       List<String> sinkronizacijskiZnakovi) {
        this.tablicaAkcija = tablicaAkcija;
        this.tablicaNovoStanje = tablicaNovoStanje;
        this.uniformniZnakovi = uniformniZnakovi;
        this.produkcije = produkcije;
        this.sinkronizacijskiZnakovi = sinkronizacijskiZnakovi;
    }

    public SAnalizator () { }

    public void reset (List<UniformniZnak> uniformniZnakovi) {
        stog = new ArrayList<>();
        stog.add(new Stanje(0));
        this.uniformniZnakovi = uniformniZnakovi;
    }

    public void pokreni () {
        int k = 0;

        while (true) {
            UniformniZnak uz;
            UniformniZnak stop = new UniformniZnak(this.stop, 0, this.stop);
            if (k < uniformniZnakovi.size())
                uz = uniformniZnakovi.get(k);
            else
                uz = stop;

            if (!uniformniZnakovi.contains(uz) && !uz.equals(stop)) {
                System.out.println("Nepostojeci uniformni znak.");
                System.exit(1);
            }
            int stanje = ((Stanje) stog.get(stog.size() - 1)).stanje;
            Map<String, ElementTablice> redak = tablicaAkcija.get(stanje);

            ElementTablice et = redak.get(uz.getZnak());
            if (et == null) {
                et = oporavakOdPogreske(k);
            }

            if (et.naziv.equals("pomakni")) {
                stog.add(new Cvor(uz.getZnak(), uz.getRedak(), uz.getLeksickaJedinka()));
                stog.add(new Stanje(et.broj));
                k++;
            }
            else if (et.naziv.equals("reduciraj")) {
                String produkcija = produkcije.get(et.broj);

                String[] strane = produkcija.split("->");
                String[] desnaStranaPom = strane[1].split(" ");
                List<String> desnaStrana = new ArrayList<String>();

               // if (desnaStranaPom.length == 0)
                //    desnaStrana.add(strane[1]);
                //else
                    for(String s : desnaStranaPom)
                        desnaStrana.add(s);

                /*for (int i = 0; i < desnaStranaPom.length; i++) {
                    StringBuilder znak = new StringBuilder();
                    znak.append(desnaStranaPom[i]);

                    if (znak.toString().equals("<")) {
                        znak.append(desnaStranaPom[++i]);
                        znak.append(desnaStranaPom[++i]);
                    }

                    desnaStrana.add(znak.toString());
                }*/


                int brojZnakova = desnaStrana.size();
                Cvor c = new Cvor(strane[0]);

                while (brojZnakova > 0) {
                    if(desnaStrana.get(0).equals("$")){
                        pomocniStog.add(new Cvor("$"));
                        brojZnakova--;
                        continue;
                    }
                    if (!((Cvor) stog.get(stog.size() - 2)).znakGramatike.equals(desnaStrana.get(brojZnakova - 1)) ){
                                System.out.println("Nemamo sve znakove redukcije na stogu.");
                                System.exit(1);
                    }
                    else {
                        pomocniStog.add(stog.get(stog.size() - 2));

                        stog.remove(stog.get(stog.size() - 1));
                        stog.remove(stog.get(stog.size() - 1));

                        brojZnakova--;
                    }
                }

                while (pomocniStog.size() > 0) {
                    c.dodajDijete((Cvor) pomocniStog.get(pomocniStog.size() - 1));
                    pomocniStog.remove(pomocniStog.size() - 1);
                }
                stog.add(c);

                stanje = ((Stanje) stog.get(stog.size() - 2)).stanje;
                redak = tablicaNovoStanje.get(stanje);
                et = redak.get(c.znakGramatike);
                if (et == null) {
                    System.out.println("Nepostojeci zapis u tablici novo stanje.");
                    System.exit(1);
                }
                stog.add(new Stanje(et.broj));
            }

            else if (et.naziv.equals("prihvati")) {
                ispisiStablo((Cvor) stog.get(stog.size() - 2), 0);
                break;
            }

           // else if (et.naziv.equals("odbaci"))
           //     oporavakOdPogreske(k);
        }
    }

    private static void ispisiStablo(Cvor korijen, int razina) {
        for(int i = 0; i < razina; i++)
            System.out.print(" ");

        korijen.ispisiCvor();

        for (int i = 0; i < korijen.djeca.size(); i++)
            ispisiStablo(korijen.djeca.get(i), razina + 1);

    }

    private ElementTablice oporavakOdPogreske(int k) {
        UniformniZnak uz = new UniformniZnak("", 0, "");

        while (k < uniformniZnakovi.size()) {
            uz = uniformniZnakovi.get(k);

            if (sinkronizacijskiZnakovi.contains(uz.getZnak()))
                break;
            k++;
        }

        if (uz.getZnak().equals("")) {
            System.out.println("Oporavak do pogreske dosao do kraja ulaznog niza. Izlaz.");
            System.exit(0);
        }

        ElementTablice et;

        do {
            int stanje = ((Stanje) stog.get(stog.size() - 1)).stanje;
            Map<String, ElementTablice> redak = tablicaAkcija.get(stanje);
            et = redak.get(uz.getZnak());

            if (et == null) {
                    //pomocniStog.add(stog.get(stog.size() - 2));

                stog.remove(stog.get(stog.size() - 1));
                stog.remove(stog.get(stog.size() - 1));
            }
        } while (et == null && stog.size() > 0);

        if (stog.size() == 0) {
            System.out.println("Oporavak od pogreske izbacio sva stanja sa stoga. Izlaz.");
            System.exit(0);
        }

        return et;
    }

    void popuniStop (BufferedReader input) throws IOException {
        if (input.readLine().equals("%Stop"))
            stop = input.readLine();
            input.readLine();
    }

    void popuniNezavrsne (BufferedReader input) throws IOException {
        String line;
        if ((line = input.readLine()).equals("%V"))
            while (!(line = input.readLine()).equals("")) {
                nezavrsni.add(line);
            }
    }
    void popuniZavrsne (BufferedReader input) throws IOException {
        String line;
        if ((line = input.readLine()).equals("%T"))
        while (!(line = input.readLine()).equals("")) {
            zavrsni.add(line);
        }
    }

     void popuniUniformneZnakove (BufferedReader input) throws IOException {
        String line;

        while ((line = input.readLine()) != null) {
            String[] dijelovi = line.split(" ");
            StringBuilder leksickaJedinka = new StringBuilder();

            leksickaJedinka.append(dijelovi[2]);

            for (int i = 3; i < dijelovi.length; i++) {
                leksickaJedinka.append(" ");
                leksickaJedinka.append(dijelovi[i]);
            }
            uniformniZnakovi.add(new UniformniZnak(dijelovi[0], Integer.parseInt(dijelovi[1]), leksickaJedinka.toString()));
        }
    }

     void popuniSinkronizacijskeZnakove(BufferedReader input) throws IOException{
        String line;

        if((line = input.readLine()).equals("%sz")) {
            while (!(line = input.readLine()).equals("")) {
                sinkronizacijskiZnakovi.add(line);
            }
        }
    }

    /*void popuniProdukcije(BufferedReader input) throws IOException{
        String line;
        String lijevaStrana = null;

        if((line = input.readLine()).equals("%p")) {
            while (!(line = input.readLine()).equals("")) {
                if (line.startsWith(" ")) {
                    String[] segmenti = line.split("");

                    StringBuilder sb = new StringBuilder();
                    sb.append(lijevaStrana);
                    sb.append("->");

                    for(String segment : segmenti)
                        sb.append(segment);

                    produkcije.add(sb.toString());

                } else {
                    lijevaStrana = line;
                }
            }
        }
    } */

     void popuniProdukcije(BufferedReader input) throws IOException{
        String line;

        if((line = input.readLine()).equals("%p")) {
            while (!(line = input.readLine()).equals("")) {
                produkcije.add(line);
            }
        }
    }

     void popuniTablicuNovoStanje(BufferedReader input) throws IOException{
        String line;

        if ((line = input.readLine()).equals("%ns")) {
            while (!(line = input.readLine()).equals("")) {
                String[] segmenti = line.split(" ");
                Map<String, ElementTablice> redak;
                ElementTablice et;

                redak = tablicaNovoStanje.get(Integer.parseInt(segmenti[0]));

                if (redak == null)
                    redak = new HashMap<>();

                redak.put(segmenti[1], new ElementTablice(segmenti[2], Integer.parseInt(segmenti[3])));

                tablicaNovoStanje.put(Integer.parseInt(segmenti[0]), redak);
            }
        }
    }

     void popuniTablicuAkcija(BufferedReader input) throws IOException {
        String line;

        if ((line = input.readLine()).equals("%a")) {
            while (!(line = input.readLine()).equals("")) {
                String[] segmenti = line.split(" ");
                Map<String, ElementTablice> redak;

                redak = tablicaAkcija.get(Integer.parseInt(segmenti[0]));

                if (redak == null)
                    redak = new HashMap<>();

                redak.put(segmenti[1], new ElementTablice(segmenti[2], Integer.parseInt(segmenti[3])));

                tablicaAkcija.put(Integer.parseInt(segmenti[0]), redak);
            }
        }
    }

     void odrediPocetnoStanje (BufferedReader input) throws IOException{
        String line;

        if ((line = input.readLine()).equals("%ps")) {
            line = input.readLine();

            stog.add(new Stanje(Integer.parseInt(line)));

            input.readLine();
        }
    }

    //pomocne klase

    private class ElementTablice {
        private String naziv;
        private int broj;

        ElementTablice(String naziv, int broj) {
            this.naziv = naziv;
            this.broj = broj;
        }
    }

    private class ElementStoga {
        private Tip tip;

        private ElementStoga (Tip tip) {
            this.tip = tip;
        }
    }

    private class Stanje extends ElementStoga {
        private int stanje;

        private Stanje (int stanje) {
            super (Tip.STANJE);
            this.stanje = stanje;
        }
    }

    private class Cvor extends ElementStoga {
        private String znakGramatike;
        private int redak = 0;
        private String leksickaJedinka = "";
        private List<Cvor> djeca = new ArrayList<>();

        private Cvor (String znakGramatike) {
            super (Tip.CVOR);
            this.znakGramatike = znakGramatike;
        }

        private Cvor(String znakGramatike, int redak, String leksickaJedinka) {
            super(Tip.CVOR);
            this.znakGramatike = znakGramatike;
            this.redak = redak;
            this.leksickaJedinka = leksickaJedinka;
        }

        private void dodajDijete (Cvor dijete) {
            djeca.add(dijete);
        }

        private void ispisiCvor () {
            if (leksickaJedinka.equals(""))
                System.out.println(znakGramatike);
            else
                System.out.format("%s %d %s\n", znakGramatike, redak, leksickaJedinka);
        }

    }
}
