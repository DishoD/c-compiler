package hr.fer.ppj.leksickiAnalizator;
public class Main {

    public static void main(String[] args) {
        LAutomatBuilder b = new LAutomatBuilder();

        b.dodajEPrijelaz(1,6).dodajEPrijelaz(1,2);
        b.dodajPrijelaz(2,3,'a').dodajPrijelaz(3,4,'b');
        b.dodajPrijelaz(4,5,'c').dodajEPrijelaz(5,2).dodajEPrijelaz(5,6);
        b.setPocetnoStanje(1);
        b.setPrihvatljivoStanje(6);

        testiraj("abcabc", b.getLAutomat());
    }

    private static void testiraj(String str, LAutomat automat) {
        automat.pokreniAutomat();
        for(char c : str.toCharArray()){
            automat.prijelaz(c);
        }
        System.out.println(automat.getStatus());
    }
}