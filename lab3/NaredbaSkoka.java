import java.util.ArrayList;
import java.util.List;

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
            while(trenutni.getOznaka() != Djelokrug.Oznaka.PETLJA) {
                trenutni = trenutni.getParent();
                if(trenutni == null) greska();
            }
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
                if(!TipoviUtility.castableImplicit(izraz.getTip(), trenutni.getPripadaFunkciji().getReturnType())) {
                    greska();
                }
            }
        }
    }

    @Override
    public String parse() {
        UniformniZnak kr = getChildAsUniformniZnak(0);
        Djelokrug trenutni = TablicaZnakova.getTrenutniDjelokrug();
        StringBuilder sb = new StringBuilder();

        if(kr.getToken().equals("KR_CONTINUE") || kr.getToken().equals("KR_BREAK")) {
            //<naredba_skoka> ::= (KR_CONTINUE | KR_BREAK) TOCKAZAREZ
            //TODO
            return null;
        } else {
            if(children.size() == 2) {
                //<naredba_skoka> ::= KR_RETURN TOCKAZAREZ
                sb.append(" ;return\n");
                for(Djelokrug d : getScopeChain(trenutni, Djelokrug.Oznaka.FUNKCIJA)) {
                    sb.append(GeneratorKoda.popScope(d));
                }
                sb.append(" RET\n");
                return  sb.toString();
            } else {
                //<naredba_skoka> ::= KR_RETURN <izraz> TOCKAZAREZ
                Izraz izraz = (Izraz)getChild(1);
                sb.append(izraz.parse())
                  .append(GeneratorKoda.popStack("R6"));

                sb.append(" ;return\n");
                for(Djelokrug d : getScopeChain(trenutni, Djelokrug.Oznaka.FUNKCIJA)) {
                    sb.append(GeneratorKoda.popScope(d));
                }
                sb.append(" RET\n");
                return  sb.toString();
            }
        }
    }

    private static List<Djelokrug> getScopeChain(Djelokrug trenutni, Djelokrug.Oznaka stop) {
        List<Djelokrug> ret = new ArrayList<>();

        while(true) {
            ret.add(trenutni);
            if(trenutni.getOznaka() == stop) break;
            trenutni = trenutni.getParent();
        }

        return ret;
    }

}
