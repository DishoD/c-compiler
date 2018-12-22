import java.util.List;

public class ListaIzrazaPridruzivanja extends NezavrsniZnak {
    private List<String> tipovi;
    private  int brElem;

    public ListaIzrazaPridruzivanja(Node parent) {
        super(parent, "lista_izraza_pridruzivanja");
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
            IzrazPridruzivanja ip = (IzrazPridruzivanja)getChild(0);
            ip.provjeri();

            tipovi.add(ip.getTip());
            brElem = 1;
        } else {
            ListaIzrazaPridruzivanja lip = (ListaIzrazaPridruzivanja)getChild(0);
            IzrazPridruzivanja ip = (IzrazPridruzivanja)getChild(2);
            lip.provjeri();
            ip.provjeri();

            tipovi.addAll(lip.getTipovi());
            tipovi.add(ip.getTip());
            brElem = lip.getBrElem() + 1;
        }
    }
}
