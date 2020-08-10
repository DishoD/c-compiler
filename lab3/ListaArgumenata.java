import java.util.ArrayList;
import java.util.List;

public class ListaArgumenata extends NezavrsniZnak {
    private List<String> tipovi = new ArrayList<>();

    public ListaArgumenata(Node parent) {
        super(parent, "<lista_argumenata>");
    }

    public List<String> getTipovi() {
        return tipovi;
    }

    @Override
    public void provjeri() {
        if(children.size() == 1) {
            IzrazPridruzivanja ip = (IzrazPridruzivanja)getChild(0);
            ip.provjeri();
            tipovi.add(ip.getTip());
        } else {
            ListaArgumenata la = (ListaArgumenata)getChild(0);
            IzrazPridruzivanja ip = (IzrazPridruzivanja)getChild(2);
            la.provjeri();
            ip.provjeri();

            tipovi.addAll(la.getTipovi());
            tipovi.add(ip.getTip());
        }
    }

    @Override
    public String parse() {
        if(children.size() == 1) {
            //<lista_argumenata> ::= <izraz_pridruzivanja>
            IzrazPridruzivanja ip = (IzrazPridruzivanja)getChild(0);
            return  ip.parse();
        } else {
            //<lista_argumenata> ::= <lista_argumenata> ZAREZ <izraz_pridruzivanja>
            ListaArgumenata la = (ListaArgumenata)getChild(0);
            IzrazPridruzivanja ip = (IzrazPridruzivanja)getChild(2);
            StringBuilder sb = new StringBuilder();
            sb.append(la.parse()).append(ip.parse());
            return sb.toString();
        }
    }
}
