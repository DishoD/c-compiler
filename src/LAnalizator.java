import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class LAnalizator {
    /**
     * par(stanje, lista automata za to stanje poredani po prioritetu)
     */
    private Map<String, List<LAutomat>> automati = new HashMap<>();

    private char[] ulazniNiz;
    private String trenutnoStanje;
    private StringBuilder grupiraniZnakovi;
    private int brojRetka;

    private int pocetak;
    private int posljednji;
    private int zavrsetak;
    private int indexPrihvacenogAutomata;

    List<UniformniZnak> tablicaUniformnihZnakova = new ArrayList<>();

    /**
     * Unapred definirane akcije(argumenti) koji se ne trebaju ponovno inicijalizirati.
     */
    private final AkcijaOdbaci ODBACI = new AkcijaOdbaci();
    private final AkcijaNoviRedak NOVI_REDAK = new AkcijaNoviRedak();

    /**
     * Inicijalizira analizator s zadanim ulaznim nizom i početnim stanjem.
     *
     * @param ulazniNiz ulazni niz koji se treba analizirati
     * @param pocetnoStanje početno stanje analizatora za razrješavanje lijevog konteksta
     */
    public LAnalizator(String ulazniNiz, String pocetnoStanje) {
        this.ulazniNiz = ulazniNiz.toCharArray();
        this.trenutnoStanje = pocetnoStanje;
    }

    /**
     * Dodaje dani automat u analizator. Dani automat će biti pridružen danom stanju.
     * Automati se moraju dodavati u poretku prioriteta (prvi dodani automat za dano stanje ima najveći prioritet).
     *
     * @param stanje stanje kojemu će biti pridružen automat
     * @param automat automat koji se dodaje
     */
    public void dodajAutomat(String stanje, LAutomat automat) {
        automati.putIfAbsent(stanje, new ArrayList<>());
        automati.compute(stanje, new BiFunction<String, List<LAutomat>, List<LAutomat>>() {
            @Override
            public List<LAutomat> apply(String s, List<LAutomat> lAutomats) {
                lAutomats.add(automat);
                return lAutomats;
            }
        });
    }

    public BuilderAkcija getNoviBuilderAkcija() {
        return new BuilderAkcija(this);
    }

    public List<UniformniZnak> getTablicaUniformnihZnakova() {
        return tablicaUniformnihZnakova;
    }

    private void umetniNoviUniformniZnak(String token, int brojRetka, String grupiraniZnakovi) {
        tablicaUniformnihZnakova.add(new UniformniZnak(token, brojRetka, grupiraniZnakovi));
    }

    private AkcijaOdbaci getAkcijaOdbaci(){
        return ODBACI;
    }

    private AkcijaNoviRedak getAkcijaNoviRedak() {
        return NOVI_REDAK;
    }

    private AkcijaDodajToken getAkcijaDodajToken(String imeTokena){
        return new AkcijaDodajToken(imeTokena);
    }

    private AkcijaUdiUStanje getAkcijaUdiUstanje(String stanje){
        return new AkcijaUdiUStanje(stanje);
    }

    private AkcijaVratiSe getAkcijaVratiSe(int naZnak){
        return new AkcijaVratiSe(naZnak);
    }

    //-----------------------------------------TODO-------------------------------------------------------------------

    /**
     * Analizator se pokreće te popunjava tablicu uniformnih znakova za ulazni niz predan analizatoru.
     */
    public void pokreniAnalizator() {

    }

    /**
     * Akcija(argument) za dodavanje uniformnog znaka u tablicu.
     */
    private class AkcijaDodajToken implements Akcija {
        private String imeTokena;

        public AkcijaDodajToken(String imeTokena) {
            this.imeTokena = imeTokena;
        }

        @Override
        public void izvrsi() {

        }
    }

    /**
     * Akcija(argument) za odbacivanje (-).
     */
    private class AkcijaOdbaci implements Akcija{

        @Override
        public void izvrsi() {

        }
    }

    /**
     * Akcija(argument) za inkrementiranje brojača novog retka.
     */
    private class AkcijaNoviRedak implements Akcija{

        @Override
        public void izvrsi() {
            brojRetka++;
        }
    }

    /**
     * Akcija(argument) koja prebacije analizator u zadano stanje.
     */
    private class AkcijaUdiUStanje implements Akcija {
        private String stanje;

        public AkcijaUdiUStanje(String stanje) {
            this.stanje = stanje;
        }

        @Override
        public void izvrsi() {
            trenutnoStanje = this.stanje;
        }
    }

    /**
     * Akcija(argument) VratiSe naZnak.
     */
    private class AkcijaVratiSe implements Akcija {
        private int naZnak;

        public AkcijaVratiSe(int naZnak) {
            this.naZnak = naZnak;
        }

        @Override
        public void izvrsi() {

        }
    }

    /**
     * Omogućava laku izgradnju akcije (niza argumenata).
     */
    public static class BuilderAkcija {
        private LAnalizator analizator;
        private List<Akcija> akcije;

        public BuilderAkcija(LAnalizator analizator) {
            this.analizator = analizator;
        }

        public BuilderAkcija dodajAkcijuDodajToken(String imeTokena) {
            akcije.add(analizator.getAkcijaDodajToken(imeTokena));
            return this;
        }

        public BuilderAkcija dodajAkcijuOdbaci() {
            akcije.add(analizator.getAkcijaOdbaci());
            return this;
        }

        public BuilderAkcija dodajAkcijuNoviRedak() {
            akcije.add(analizator.getAkcijaNoviRedak());
            return this;
        }

        public BuilderAkcija dodajAkcijuUdiUStanje(String stanje) {
            akcije.add(analizator.getAkcijaUdiUstanje(stanje));
            return this;
        }

        public BuilderAkcija dodajAkcijuVratiSe(int naZnak) {
            akcije.add(analizator.getAkcijaVratiSe(naZnak));
            return this;
        }

        public List<Akcija> getAkcije() {
            return akcije;
        }
    }
}
