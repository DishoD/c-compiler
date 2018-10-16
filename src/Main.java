public class Main {

    public static void main(String[] args) {
        LAnalizator analizator = new LAnalizator("sdas", "S_Pocetno");
        LAutomatBuilder automatBuilder = new LAutomatBuilder();
        LAnalizator.BuilderAkcija builderAkcija = analizator.getNoviBuilderAkcija();

        automatBuilder.dodajPrijelaz(1, 2, 'a');
        automatBuilder.dodajPrijelaz(2, 3, 'b');
        automatBuilder.dodajPrijelaz(3, 4, 'c');
        automatBuilder.setPocetnoStanje(1);
        automatBuilder.setPrihvatljivoStanje(4);

        builderAkcija.dodajAkcijuOdbaci();
        builderAkcija.dodajAkcijuNoviRedak();
        builderAkcija.dodajAkcijuDodajToken("KR_FOR");

        LAutomat automat = automatBuilder.getLAutomat();
        automat.setAkcije(builderAkcija.getAkcije());
        analizator.dodajAutomat("S_Pocetno", automat);

        analizator.pokreniAnalizator();
        analizator.getTablicaUniformnihZnakova();
    }
}