public class NaredbaGrananja extends NezavrsniZnak {

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
        getChildAsNezavrsniZnak(4).provjeri();
        TablicaZnakova.vratiSe();

        //<naredba_grananja> ::= KR_IF L_ZAGRADA <izraz> D_ZAGRADA <naredba> KR_ELSE <naredba>
        if(children.size() == 7) {
            TablicaZnakova.stvoriNoviDjelokrug(Djelokrug.Oznaka.BLOK, null);
            getChildAsNezavrsniZnak(6).provjeri();
            TablicaZnakova.vratiSe();
        }
    }
}
