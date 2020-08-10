public class NaredbaPetlje extends NezavrsniZnak{
    private Djelokrug djelokrugPetlje;

    private static int labelCnt = 0;

    private static String getNewLabelPrefix() {
        return "PETLJA" + labelCnt++;
    }

    public NaredbaPetlje(Node parent) {
        super(parent, "<naredba_petlje>");
    }

    @Override
    public void provjeri() {
        if(children.size() == 5) {
            //<naredba_petlje> ::= KR_WHILE L_ZAGRADA <izraz> D_ZAGRADA <naredba>
            Izraz izraz = (Izraz)getChild(2);
            izraz.provjeri();
            if(!TipoviUtility.castableImplicit(izraz.getTip(), "int")) greska();
            TablicaZnakova.stvoriNoviDjelokrug(Djelokrug.Oznaka.PETLJA, null);
            djelokrugPetlje = TablicaZnakova.getTrenutniDjelokrug();
            getChildAsNezavrsniZnak(4).provjeri();
            TablicaZnakova.vratiSe();
        } else {
            //jednako je za slucaj 2 i 3
            getChildAsNezavrsniZnak(2).provjeri();
            IzrazNaredba in = (IzrazNaredba)getChild(3);
            in.provjeri();
            if(!TipoviUtility.castableImplicit(in.getTip(), "int")) greska();

            if(children.size() == 6) {
                //<naredba_petlje> ::= KR_FOR L_ZAGRADA <izraz_naredba>1 <izraz_naredba>2 D_ZAGRADA <naredba>
                TablicaZnakova.stvoriNoviDjelokrug(Djelokrug.Oznaka.PETLJA, null);
                djelokrugPetlje = TablicaZnakova.getTrenutniDjelokrug();
                getChildAsNezavrsniZnak(5).provjeri();
                TablicaZnakova.vratiSe();
            } else {
                //<naredba_petlje> ::= KR_FOR L_ZAGRADA <izraz_naredba>1 <izraz_naredba>2 <izraz> D_ZAGRADA <naredba>
                getChildAsNezavrsniZnak(4).provjeri();
                TablicaZnakova.stvoriNoviDjelokrug(Djelokrug.Oznaka.PETLJA, null);
                djelokrugPetlje = TablicaZnakova.getTrenutniDjelokrug();
                getChildAsNezavrsniZnak(6).provjeri();
                TablicaZnakova.vratiSe();
            }
        }
    }


    public String parse() {
        StringBuilder sb = new StringBuilder();
        String prefix = getNewLabelPrefix();

        if(children.size() == 5) {
            //<naredba_petlje> ::= KR_WHILE L_ZAGRADA <izraz> D_ZAGRADA <naredba>
            Izraz izraz = (Izraz)getChild(2);
            Naredba naredba = (Naredba)getChild(4);
            naredba.setDjelokrugNaredbe(djelokrugPetlje);

            sb.append(prefix + "_TEST" + izraz.parse())
                    .append(" POP R0\n")
                    .append(" OR R0, R0, R0\n")
                    .append(" JR_Z " + prefix + "_KRAJ\n")
                    .append(naredba.parse())
                    .append(" JR " + prefix + "_TEST\n")
                    .append(prefix + "_KRAJ OR R0, R0, R0\n");

            return sb.toString();
        } else {
            if(children.size() == 6) {
                //<naredba_petlje> ::= KR_FOR L_ZAGRADA <izraz_naredba>1 <izraz_naredba>2 D_ZAGRADA <naredba>
                IzrazNaredba in1 = (IzrazNaredba) getChild(2);
                IzrazNaredba in2 = (IzrazNaredba) getChild(3);
                Naredba naredba = (Naredba)getChild(5);
                naredba.setDjelokrugNaredbe(djelokrugPetlje);

                sb      .append(in1.parse())
                        .append(prefix + "_TEST" + in2.parse())
                        .append(" POP R0\n")
                        .append(" OR R0, R0, R0\n")
                        .append(" JR_Z " + prefix + "_KRAJ\n")
                        .append(naredba.parse())
                        .append(" JR " + prefix + "_TEST\n")
                        .append(prefix + "_KRAJ OR R0, R0, R0\n");

                return sb.toString();
            } else {
                //<naredba_petlje> ::= KR_FOR L_ZAGRADA <izraz_naredba>1 <izraz_naredba>2 <izraz> D_ZAGRADA <naredba>
                IzrazNaredba in1 = (IzrazNaredba) getChild(2);
                IzrazNaredba in2 = (IzrazNaredba) getChild(3);
                Izraz inc = (Izraz)getChild(4);
                Naredba naredba = (Naredba)getChild(6);
                naredba.setDjelokrugNaredbe(djelokrugPetlje);

                sb      .append(in1.parse())
                        .append(prefix + "_TEST" + in2.parse())
                        .append(" POP R0\n")
                        .append(" OR R0, R0, R0\n")
                        .append(" JR_Z " + prefix + "_KRAJ\n")
                        .append(naredba.parse())
                        .append(inc.parse())
                        .append(" JR " + prefix + "_TEST\n")
                        .append(prefix + "_KRAJ OR R0, R0, R0\n");

                return sb.toString();
            }
        }
    }
}
