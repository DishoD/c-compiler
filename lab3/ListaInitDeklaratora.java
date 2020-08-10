public class ListaInitDeklaratora extends NezavrsniZnak {
    private String ntip;

    public ListaInitDeklaratora(Node parent) {
        super(parent, "<lista_init_deklaratora>");
    }

    public String getTip() {
        return ntip;
    }

    public void setNtip(String ntip) {
        this.ntip = ntip;
    }

    @Override
    public void provjeri() {
        if(children.size() == 1) {
            //<lista_init_deklaratora> ::= <init_deklarator>
            InitDeklarator id = (InitDeklarator)getChild(0);
            id.setNtip(ntip);
            id.provjeri();
        } else {
            //<lista_init_deklaratora>1 ::= <lista_init_deklaratora>2 ZAREZ <init_deklarator>
            ListaInitDeklaratora lid = (ListaInitDeklaratora)getChild(0);
            InitDeklarator id = (InitDeklarator)getChild(2);

            lid.setNtip(ntip);
            id.setNtip(ntip);

            lid.provjeri();
            id.provjeri();
        }
    }

    @Override
    public String parse() {
        if(children.size() == 1) {
            //<lista_init_deklaratora> ::= <init_deklarator>
            InitDeklarator id = (InitDeklarator)getChild(0);
            return  id.parse();
        } else {
            //<lista_init_deklaratora>1 ::= <lista_init_deklaratora>2 ZAREZ <init_deklarator>
            ListaInitDeklaratora lid = (ListaInitDeklaratora)getChild(0);
            InitDeklarator id = (InitDeklarator)getChild(2);
            return  lid.parse() + id.parse();
        }
    }
}
