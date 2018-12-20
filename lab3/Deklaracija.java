public class Deklaracija extends NezavrsniZnak {

    public Deklaracija(Node parent) {
        super(parent, "<deklaracija>");
    }

    @Override
    public void provjeri() {
        //<deklaracija> ::= <ime_tipa> <lista_init_deklaratora> TOCKAZAREZ
        ImeTipa imeTipa = (ImeTipa)getChild(0);
        ListaInitDeklaratora lid = (ListaInitDeklaratora)getChild(1);
        lid.setNtip(imeTipa.getTip());
        lid.provjeri();
    }
}
