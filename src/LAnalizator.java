import java.util.*;

public class LAnalizator {
    /**
     * par(stanje, lista automata za to stanje poredani po prioritetu)
     */
    private Map<String, List<LAutomat>> automati = new HashMap<>();

    private char[] ulazniNiz;
    private String trenutnoStanje;
    private int brojRetka = 1;

    private int pocetak = 0;
    private int posljednji = 0;
    private int zavrsetak = -1;
    private int indexPrihvacenogAutomata = -1;

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

    public LAnalizator() {}

    /**
     * Dodaje dani automat u analizator. Dani automat će biti pridružen danom stanju.
     * Automati se moraju dodavati u poretku prioriteta (prvi dodani automat za dano stanje ima najveći prioritet).
     *
     * @param stanje stanje kojemu će biti pridružen automat
     * @param automat automat koji se dodaje
     */
    public void dodajAutomat(String stanje, LAutomat automat) {
        List<LAutomat> a = automati.get(stanje);
        if(a == null) {
            a = new ArrayList<>();
            a.add(automat);
        } else {
            a.add(automat);
        }
    }

    public BuilderAkcija getNoviBuilderAkcija() {
        return new BuilderAkcija(this);
    }

    public List<UniformniZnak> getTablicaUniformnihZnakova() {
        return tablicaUniformnihZnakova;
    }

    /**
     * Postavi ulazni niz leksičkog analizatora.
     *
     * @param ulazniNiz ulazni niz
     */
    public void setUlazniNiz(String ulazniNiz) {
        this.ulazniNiz = ulazniNiz.toCharArray();
    }

    /**
     * Postavi početno stanje leksičkog analizatora.
     *
     * @param pocetnoStanje početno stanje
     */
    public void setPocetnoStanje(String pocetnoStanje) {
        this.trenutnoStanje = pocetnoStanje;
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

    private void ubaciNoviUniformniZnakUTablicu (String token, int redak, String grupiraniZnakovi) {
        tablicaUniformnihZnakova.add(new UniformniZnak(token, redak, grupiraniZnakovi));
    }

    //-----------------------------------------TODO-------------------------------------------------------------------

    /**
     * Analizator se pokreće te popunjava tablicu uniformnih znakova za ulazni niz predan analizatoru.
     */
    public void pokreniAnalizator() {
        pokreniAutomate();

        while(pocetak < ulazniNiz.length){
            LAutomat.LAutomatStatus status = getStatusAutomata();

            if(status == LAutomat.LAutomatStatus.RADI) {
                zavrsetak++;
                if(zavrsetak < ulazniNiz.length) {
                    char znak = ulazniNiz[zavrsetak];
                    izvrsiPrijelazeAutomata(znak);
                } else {
                    status = LAutomat.LAutomatStatus.STOPIRAN;
                }
            }
            if(status == LAutomat.LAutomatStatus.PRIHVATLJIV) {
                posljednji = zavrsetak;
                zavrsetak++;
                if(zavrsetak < ulazniNiz.length) {
                    char znak = ulazniNiz[zavrsetak];
                    izvrsiPrijelazeAutomata(znak);
                } else {
                    status = LAutomat.LAutomatStatus.STOPIRAN;
                }
            }
            if(status == LAutomat.LAutomatStatus.STOPIRAN) {
                if(indexPrihvacenogAutomata == -1) {
                    System.err.println(ulazniNiz[pocetak]);
                    zavrsetak = pocetak;
                    pocetak++;
                    pokreniAutomate();
                } else {
                    automati.get(trenutnoStanje).get(indexPrihvacenogAutomata).izvrsiAkcije();
                    indexPrihvacenogAutomata = -1;
                    pokreniAutomate();
                }
            }
        }

    }

    /**
     * pokreće sve automate za trenutno stanje analizatora
     */
    private void pokreniAutomate(){
        for(LAutomat automat : automati.get(trenutnoStanje)) automat.pokreniAutomat();
    }

    /**
     * Izvrši prijelaze za sve automate iz strenutnog stanja analizatora za zadani znak.
     * @param znak znak za prijelaze
     */
    private void izvrsiPrijelazeAutomata(char znak) {
        for(LAutomat automat : automati.get(trenutnoStanje)) automat.prijelaz(znak);
    }

    /**
     * Dohvaća trenutni status automata trenutnog stanja analizatora.
     *      STOPIRAN - svi automati su stopirani
     *      RADI - radi barem jedan automat
     *      PRIHVATLJIV - barem jedan automat je u prihvatljivom stanju; indexPrihvatljivogAutomata se postavlja na index prihvatljivog automata s najvećim prioritetom
     * @return status automata
     */
    private LAutomat.LAutomatStatus getStatusAutomata() {
        LAutomat.LAutomatStatus status = LAutomat.LAutomatStatus.STOPIRAN;
        int index = 0;
        for(LAutomat automat : automati.get(trenutnoStanje)){
            LAutomat.LAutomatStatus statusTrenutnogAutomata = automat.getStatus();

            if(statusTrenutnogAutomata == LAutomat.LAutomatStatus.PRIHVATLJIV) {
                if(status != LAutomat.LAutomatStatus.PRIHVATLJIV) {
                    status = LAutomat.LAutomatStatus.PRIHVATLJIV;
                    indexPrihvacenogAutomata = index;
                }
            }

            if(statusTrenutnogAutomata == LAutomat.LAutomatStatus.RADI && status == LAutomat.LAutomatStatus.STOPIRAN) {
                status = LAutomat.LAutomatStatus.RADI;
            }

            index++;
        }
        return status;
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
            String grupiraniZnakovi = new String(ulazniNiz, pocetak, posljednji - pocetak + 1);
            ubaciNoviUniformniZnakUTablicu(imeTokena, brojRetka, grupiraniZnakovi);
            pocetak = posljednji + 1;
            zavrsetak = posljednji;
        }
    }

    /**
     * Akcija(argument) za odbacivanje (-).
     */
    private class AkcijaOdbaci implements Akcija{

        @Override
        public void izvrsi() {
            pocetak = posljednji + 1;
            zavrsetak = posljednji;
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
            posljednji = pocetak + naZnak - 1;
        }
    }

    /**
     * Omogućava laku izgradnju akcije (niza argumenata).
     * Akcije moraju biti dodavane redom koji je zadan u uputi.
     */
    public static class BuilderAkcija {
        private LAnalizator analizator;
        private List<Akcija> akcije = new LinkedList<>();

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
            akcije.add(0, analizator.getAkcijaVratiSe(naZnak));
            return this;
        }

        public List<Akcija> getAkcije() {
            return akcije;
        }

        /**
         * Briše sve akcije koje se trenutno nalaze u listi.
         * Zvati ovu metodu kada želite starati novi set akcija.
         */
        public void clear() {akcije = new LinkedList<>();}
    }
}
