public class Main {

    public static void main(String[] args) {
        LAnalizator analizator = new LAnalizator("abc 123\n   123 abc   123abc\n", "s");
        LAnalizator.BuilderAkcija builderAkcija = analizator.getNoviBuilderAkcija();
        LAutomat automat;

        //regex: abc
        LAutomatBuilder abc = new LAutomatBuilder();
        abc.dodajPrijelaz(1,2,'a').dodajPrijelaz(2,3,'b').dodajPrijelaz(3,4,'c');
        abc.setPrihvatljivoStanje(4);
        abc.setPocetnoStanje(1);

        builderAkcija.clear();
        builderAkcija.dodajAkcijuDodajToken("KR_ABC");

        automat = abc.getLAutomat();
        automat.setAkcije(builderAkcija.getAkcije());
        analizator.dodajAutomat("s", automat);

        //regex: 123
        LAutomatBuilder _123 = new LAutomatBuilder();
        _123.dodajPrijelaz(1,2,'1').dodajPrijelaz(2,3,'2').dodajPrijelaz(3,4,'3');
        _123.setPrihvatljivoStanje(4);
        _123.setPocetnoStanje(1);

        builderAkcija.clear();
        builderAkcija.dodajAkcijuDodajToken("KR_123");

        automat = _123.getLAutomat();
        automat.setAkcije(builderAkcija.getAkcije());
        analizator.dodajAutomat("s", automat);

        //regex: \n
        LAutomatBuilder noviRed = new LAutomatBuilder();
        noviRed.dodajPrijelaz(1,2,'\n');
        noviRed.setPrihvatljivoStanje(2);
        noviRed.setPocetnoStanje(1);

        builderAkcija.clear();
        builderAkcija.dodajAkcijuOdbaci().dodajAkcijuNoviRedak();

        automat = noviRed.getLAutomat();
        automat.setAkcije(builderAkcija.getAkcije());
        analizator.dodajAutomat("s", automat);

        //regex: \_|\t
        LAutomatBuilder bjeline = new LAutomatBuilder();
        bjeline.dodajPrijelaz(1,2,'\t').dodajPrijelaz(1,3, ' ');
        bjeline.dodajEPrijelaz(2,4).dodajEPrijelaz(3,4);
        bjeline.setPrihvatljivoStanje(4);
        bjeline.setPocetnoStanje(1);

        builderAkcija.clear();
        builderAkcija.dodajAkcijuOdbaci();

        automat = bjeline.getLAutomat();
        automat.setAkcije(builderAkcija.getAkcije());
        analizator.dodajAutomat("s", automat);

        analizator.pokreniAnalizator();
        for(UniformniZnak token : analizator.getTablicaUniformnihZnakova()) {
            System.out.println(token);
        }
    }

}