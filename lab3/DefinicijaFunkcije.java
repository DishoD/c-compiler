public class DefinicijaFunkcije extends NezavrsniZnak {
    private Djelokrug djelokrugFuncije;

    public DefinicijaFunkcije(Node parent) {
        super(parent, "<definicija_funkcije>");
    }

    @Override
    public void provjeri() {
        if(provjera == null){
            provjera = getChild(3) instanceof UniformniZnak ? new V1() : new V2();
        }

        provjera.provjeri();
    }


    public String parse() {
        Djelokrug izvorni = TablicaZnakova.getTrenutniDjelokrug();
        TablicaZnakova.setTrenutniDjelokrug(djelokrugFuncije);
        PrototipFunkcije f = djelokrugFuncije.getPripadaFunkciji();
        String labela = f.getIdn();
        SlozenaNaredba sn = ((SlozenaNaredba)getChild(5));

        StringBuilder sb = new StringBuilder();
        sb      .append(labela)
                .append(GeneratorKoda.pushScope(djelokrugFuncije))
                .append(sn.parse());

        if(f.getReturnType().equals("void")) {
            sb.append(GeneratorKoda.popScope(djelokrugFuncije))
                    .append(" RET\n");
        }

        GeneratorKoda.addFunctionCode(sb.toString());
        TablicaZnakova.setTrenutniDjelokrug(izvorni);


        return null;
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
            if(TablicaZnakova.getGlobalniDjelokrug().postojiVarijabla(ime)) greska();

            TablicaZnakova.getTrenutniDjelokrug().dodajDeklaracijuFunkcije(f);
            TablicaZnakova.dodajDefiniranuFunkciju(f);

            TablicaZnakova.stvoriNoviDjelokrug(Djelokrug.Oznaka.FUNKCIJA, f);
            djelokrugFuncije = TablicaZnakova.getTrenutniDjelokrug();
            ((NezavrsniZnak)getChild(5)).provjeri();
            TablicaZnakova.vratiSe();
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

            ListaParametara parametri = (ListaParametara)getChild(3);
            parametri.provjeri();

            PrototipFunkcije f = new PrototipFunkcije(ime, povratniTip.getTip(), parametri.getTipovi());
            PrototipFunkcije postojeciTip = TablicaZnakova.getGlobalniDjelokrug().getFunkcija(ime);
            if(postojeciTip != null && !postojeciTip.equals(f)) greska();
            if(TablicaZnakova.getGlobalniDjelokrug().postojiVarijabla(ime)) greska();

            TablicaZnakova.getTrenutniDjelokrug().dodajDeklaracijuFunkcije(f);
            TablicaZnakova.dodajDefiniranuFunkciju(f);

            TablicaZnakova.stvoriNoviDjelokrug(Djelokrug.Oznaka.FUNKCIJA, f);
            djelokrugFuncije = TablicaZnakova.getTrenutniDjelokrug();
            Djelokrug trenutniDjelokrug = TablicaZnakova.getTrenutniDjelokrug();
            for (int i = 0; i < parametri.getImena().size(); ++i) {
                String idn = parametri.getImena().get(i);
                String type = parametri.getTipovi().get(i);
                trenutniDjelokrug.dodajVarijablu(idn, type, -1);
            }
            ((NezavrsniZnak)getChild(5)).provjeri();
            TablicaZnakova.vratiSe();
        }
    }
}
