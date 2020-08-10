public class IzravniDeklarator extends NezavrsniZnak {
    private String ntip;
    private String tip;
    private boolean isFunction;
    private PrototipFunkcije prototipFunkcije;
    private  int brElem;

    public IzravniDeklarator(Node parent) {
        super(parent, "<izravni_deklarator>");
    }

    public boolean isFunction() {
        return isFunction;
    }

    public PrototipFunkcije getPrototipFunkcije() {
        return prototipFunkcije;
    }

    public String getNtip() {
        return ntip;
    }

    public void setNtip(String ntip) {
        this.ntip = ntip;
    }

    public String getTip() {
        return tip;
    }

    public int getBrElem() {
        return brElem;
    }

    @Override
    public void provjeri() {
        if(provjera == null) {
            if(children.size() == 1) {
                provjera = new V1();
            } else if(getChildAsUniformniZnak(1).getToken().equals("L_UGL_ZAGRADA")) {
                provjera = new V2();
            } else if(getChild(2) instanceof UniformniZnak) {
                provjera = new V3();
            } else {
                provjera = new V4();
            }
        }

        provjera.provjeri();
    }

    @Override
    public String parse() {
        if(children.size() == 1) {
            //<izravni_deklarator> ::= IDN
            String znak = getChildAsUniformniZnak(0).getGrupiraniZnakovi();
            return GeneratorKoda.getVarReference(znak, TablicaZnakova.getTrenutniDjelokrug());
        } else if(getChildAsUniformniZnak(1).getToken().equals("L_UGL_ZAGRADA")) {
            //<izravni_deklarator> ::= IDN L_UGL_ZAGRADA BROJ D_UGL_ZAGRADA
            String znak = getChildAsUniformniZnak(0).getGrupiraniZnakovi();
            return GeneratorKoda.getVarReference(znak, TablicaZnakova.getTrenutniDjelokrug());
        }
        return null;
    }

    /**
     * <izravni_deklarator> ::= IDN
     */
    private class V1 implements Provjerljiv {

        @Override
        public void provjeri() {
            if(ntip.equals("void")) greska();
            String idn = getChildAsUniformniZnak(0).getGrupiraniZnakovi();
            Djelokrug d = TablicaZnakova.getTrenutniDjelokrug();
            if(d.postojiVarijabla(idn) || d.postojiFunkcija(idn)) greska();
            TablicaZnakova.getTrenutniDjelokrug().dodajVarijablu(idn, ntip, -1);
            tip = ntip;
        }
    }

    /**
     * <izravni_deklarator> ::= IDN L_UGL_ZAGRADA BROJ D_UGL_ZAGRADA
     */
    private class V2 implements Provjerljiv {

        @Override
        public void provjeri() {
            if(ntip.equals("void")) greska();
            String idn = getChildAsUniformniZnak(0).getGrupiraniZnakovi();
            Djelokrug d = TablicaZnakova.getTrenutniDjelokrug();
            if(d.postojiVarijabla(idn) || d.postojiFunkcija(idn)) greska();
            int vr = -1;
            try{
                vr = Integer.parseInt(getChildAsUniformniZnak(2).getGrupiraniZnakovi());
            } catch (NumberFormatException e) {
                greska();
            }
            if(vr <= 0 || vr > 1024) greska();
            TablicaZnakova.getTrenutniDjelokrug().dodajVarijablu(idn, TipoviUtility.toArray(ntip), vr);

            tip = TipoviUtility.toArray(ntip);
            brElem = vr;
        }
    }

    /**
     * <izravni_deklarator> ::= IDN L_ZAGRADA KR_VOID D_ZAGRADA
     */
    private class V3 implements Provjerljiv {

        @Override
        public void provjeri() {
            String idn = getChildAsUniformniZnak(0).getGrupiraniZnakovi();
            PrototipFunkcije f = new PrototipFunkcije(idn, ntip, PrototipFunkcije.VOID_PARAMETER);
            PrototipFunkcije fTrazena = TablicaZnakova.getTrenutniDjelokrug().getFunkcija(idn);
            if(fTrazena != null && !f.equals(fTrazena)) greska();
            if(TablicaZnakova.getTrenutniDjelokrug().postojiVarijabla(idn)) greska();

            TablicaZnakova.getTrenutniDjelokrug().dodajDeklaracijuFunkcije(f);
            isFunction = true;
            prototipFunkcije = f;
        }
    }

    /**
     * <izravni_deklarator> ::= IDN L_ZAGRADA <lista_parametara> D_ZAGRADA
     */
    private class V4 implements Provjerljiv {

        @Override
        public void provjeri() {
            ListaParametara parametri = (ListaParametara)getChild(2);
            parametri.provjeri();
            String idn = getChildAsUniformniZnak(0).getGrupiraniZnakovi();

            PrototipFunkcije f = new PrototipFunkcije(idn, ntip, parametri.getTipovi());
            PrototipFunkcije fTrazena = TablicaZnakova.getTrenutniDjelokrug().getFunkcija(idn);
            if(fTrazena != null && !f.equals(fTrazena)) greska();
            if(TablicaZnakova.getTrenutniDjelokrug().postojiVarijabla(idn)) greska();

            TablicaZnakova.getTrenutniDjelokrug().dodajDeklaracijuFunkcije(f);
            isFunction = true;
            prototipFunkcije = f;
        }
    }
}
