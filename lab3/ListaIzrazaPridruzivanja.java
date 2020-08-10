import java.util.ArrayList;
import java.util.List;

public class ListaIzrazaPridruzivanja extends NezavrsniZnak {
    private List<String> tipovi =  new ArrayList<>();
    private  int brElem;

    public ListaIzrazaPridruzivanja(Node parent) {
        super(parent, "<lista_izraza_pridruzivanja>");
    }

    public List<String> getTipovi() {
        return tipovi;
    }

    public int getBrElem() {
        return brElem;
    }

    @Override
    public void provjeri() {
        if(children.size() == 1) {
            // <lista_izraza_pridruzivanja> ::= <izraz_pridruzivanja>
            IzrazPridruzivanja ip = (IzrazPridruzivanja)getChild(0);
            ip.provjeri();

            tipovi.add(ip.getTip());
            brElem = 1;
        } else {
            // <lista_izraza_pridruzivanja> ::= <lista_izraza_pridruzivanja> ZAREZ <izraz_pridruzivanja>
            ListaIzrazaPridruzivanja lip = (ListaIzrazaPridruzivanja)getChild(0);
            IzrazPridruzivanja ip = (IzrazPridruzivanja)getChild(2);
            lip.provjeri();
            ip.provjeri();

            tipovi.addAll(lip.getTipovi());
            tipovi.add(ip.getTip());
            brElem = lip.getBrElem() + 1;
        }
    }

    @Override
    public String parse() {
        if(children.size() == 1) {
            // <lista_izraza_pridruzivanja> ::= <izraz_pridruzivanja>
            IzrazPridruzivanja ip = (IzrazPridruzivanja)getChild(0);
            return ip.parse();
        } else {
            // <lista_izraza_pridruzivanja> ::= <lista_izraza_pridruzivanja> ZAREZ <izraz_pridruzivanja>
            ListaIzrazaPridruzivanja lip = (ListaIzrazaPridruzivanja)getChild(0);
            IzrazPridruzivanja ip = (IzrazPridruzivanja)getChild(2);
            return ip.parse() + lip.parse();
        }
    }
}
