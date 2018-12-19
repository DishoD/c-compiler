public class NaredbaSkoka extends NezavrsniZnak {

    public NaredbaSkoka(Node parent) {
        super(parent, "<naredba_skoka>");
    }

    @Override
    public void provjeri() {
        UniformniZnak kr = getChildAsUniformniZnak(0);
        Djelokrug trenutni = TablicaZnakova.getTrenutniDjelokrug();

        if(kr.getToken().equals("KR_CONTINUE") || kr.getToken().equals("KR_BREAK")) {
            //<naredba_skoka> ::= (KR_CONTINUE | KR_BREAK) TOCKAZAREZ
            while(trenutni != null || trenutni.getOznaka() != Djelokrug.Oznaka.PETLJA) {
                trenutni = trenutni.getParent();
            }
            if(trenutni == null) greska();
        } else {
            if(children.size() == 2) {
                //<naredba_skoka> ::= KR_RETURN TOCKAZAREZ
                while(trenutni.getOznaka() != Djelokrug.Oznaka.FUNKCIJA) {
                    trenutni = trenutni.getParent();
                }
                if(!trenutni.getPripadaFunkciji().getReturnType().equals("void")) greska();
            } else {
                //<naredba_skoka> ::= KR_RETURN <izraz> TOCKAZAREZ
                Izraz izraz = (Izraz)getChild(1);
                izraz.provjeri();
                while(trenutni.getOznaka() != Djelokrug.Oznaka.FUNKCIJA) {
                    trenutni = trenutni.getParent();
                }
                if(!TipoviUtility.castable(izraz.getTip(), trenutni.getPripadaFunkciji().getReturnType())) {
                    greska();
                }
            }
        }
    }
}
