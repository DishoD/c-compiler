public class NaredbaGrananja extends NezavrsniZnak {
    private Djelokrug djelokrugBlok1;
    private Djelokrug djelokrugBlok2;

    private static int lalbelCounter = 0;

    private static String getNewLabelPrefix() {
        return "IF" + lalbelCounter++;
    }

    public NaredbaGrananja(Node parent) {
        super(parent, "<naredba_grananja>");
    }

    @Override
    public void provjeri() {
        //<naredba_grananja> ::= KR_IF L_ZAGRADA <izraz> D_ZAGRADA <naredba>
        Izraz izraz = (Izraz)getChild(2);
        izraz.provjeri();
        if(!TipoviUtility.castableImplicit(izraz.getTip(), "int")) greska();
        TablicaZnakova.stvoriNoviDjelokrug(Djelokrug.Oznaka.BLOK, null);
        djelokrugBlok1 = TablicaZnakova.getTrenutniDjelokrug();
        getChildAsNezavrsniZnak(4).provjeri();
        TablicaZnakova.vratiSe();

        //<naredba_grananja> ::= KR_IF L_ZAGRADA <izraz> D_ZAGRADA <naredba> KR_ELSE <naredba>
        if(children.size() == 7) {
            TablicaZnakova.stvoriNoviDjelokrug(Djelokrug.Oznaka.BLOK, null);
            djelokrugBlok2 = TablicaZnakova.getTrenutniDjelokrug();
            getChildAsNezavrsniZnak(6).provjeri();
            TablicaZnakova.vratiSe();
        }
    }

    @Override
    public String parse() {
        String prefix = getNewLabelPrefix();
        StringBuilder sb = new StringBuilder();

        if(children.size() == 5) {
            //<naredba_grananja> ::= KR_IF L_ZAGRADA <izraz> D_ZAGRADA <naredba>
            Izraz izraz = (Izraz)getChild(2);
            Naredba naredba = (Naredba)getChild(4);
            naredba.setDjelokrugNaredbe(djelokrugBlok1);

            sb.append(izraz.parse())
                    .append(" POP R0\n")
                    .append(" OR R0, R0, R0\n")
                    .append(" JR_Z " + prefix + "_KRAJ\n")
                    .append(naredba.parse())
                    .append(prefix+"_KRAJ OR R0, R0, R0\n");

            return sb.toString();
        } else {
            //<naredba_grananja> ::= KR_IF L_ZAGRADA <izraz> D_ZAGRADA <naredba> KR_ELSE <naredba>
            Izraz izraz = (Izraz)getChild(2);
            Naredba naredba1 = (Naredba)getChild(4);
            Naredba naredba2 = (Naredba)getChild(6);
            naredba1.setDjelokrugNaredbe(djelokrugBlok1);
            naredba2.setDjelokrugNaredbe(djelokrugBlok2);

            sb.append(izraz.parse())
                    .append(" POP R0\n")
                    .append(" OR R0, R0, R0\n")
                    .append(" JR_Z " + prefix + "_BLOK2\n")
                    .append(naredba1.parse())
                    .append(" JR " + prefix +"_KRAJ\n")
                    .append(prefix + "_BLOK2" + naredba2.parse())
                    .append(prefix+"_KRAJ OR R0, R0, R0\n");

            return sb.toString();
        }
    }
}
