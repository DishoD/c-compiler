public class DefinicijaFunkcije extends NezavrsniZnak {

    public DefinicijaFunkcije(Node parent) {
        super(parent, "<definicija_funkcije>");
    }

    @Override
    public void provjeri() {

    }

    /**
     * <definicija_funkcije> ::= <ime_tipa> IDN L_ZAGRADA KR_VOID D_ZAGRADA <slozena_naredba>
     */
    private class V1 implements Provjerljiv {
        @Override
        public void provjeri() {
            ImeTipa povratniTip = (ImeTipa)getChild(0);
            povratniTip.provjeri();
            if(TipoviUtility.isConst(povratniTip.getTip())) greska();

            String ime = ((UniformniZnak)getChild(1)).getGrupiraniZnakovi();
            if(TablicaZnakova.postojiDefiniranaFunkcija(ime)) greska();

            PrototipFunkcije f = new PrototipFunkcije(ime, povratniTip.getTip(), PrototipFunkcije.VOID_PARAMETER);
            PrototipFunkcije postojeciTip = TablicaZnakova.getGlobalniDjelokrug().getFunkcija(ime);
            if(postojeciTip != null && !postojeciTip.equals(f)) greska();

            TablicaZnakova.getTrenutniDjelokrug().dodajDeklaracijuFunkcije(f);
            TablicaZnakova.dodajDefiniranuFunkciju(f);

            TablicaZnakova.stvoriNoviDjelokrug();
            ((NezavrsniZnak)getChild(5)).provjeri();
        }
    }

    /**
     * <definicija_funkcije> ::= <ime_tipa> IDN L_ZAGRADA <lista_parametara> D_ZAGRADA <slozena_naredba>
     */
    private class V2 implements Provjerljiv {
        @Override
        public void provjeri() {
            ImeTipa povratniTip = (ImeTipa)getChild(0);
            povratniTip.provjeri();
            if(TipoviUtility.isConst(povratniTip.getTip())) greska();

            String ime = ((UniformniZnak)getChild(1)).getGrupiraniZnakovi();
            if(TablicaZnakova.postojiDefiniranaFunkcija(ime)) greska();
        }
    }
}
