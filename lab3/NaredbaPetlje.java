public class NaredbaPetlje extends NezavrsniZnak{

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
                getChildAsNezavrsniZnak(5).provjeri();
                TablicaZnakova.vratiSe();
            } else {
                //<naredba_petlje> ::= KR_FOR L_ZAGRADA <izraz_naredba>1 <izraz_naredba>2 <izraz> D_ZAGRADA <naredba>
                getChildAsNezavrsniZnak(4).provjeri();
                TablicaZnakova.stvoriNoviDjelokrug(Djelokrug.Oznaka.PETLJA, null);
                getChildAsNezavrsniZnak(6).provjeri();
                TablicaZnakova.vratiSe();
            }
        }
    }
}
