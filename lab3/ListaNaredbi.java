public class ListaNaredbi extends NezavrsniZnak {

    public ListaNaredbi(Node parent) {
        super(parent, "<lista_naredbi>");
    }

    @Override
    public void provjeri() {
        if(children.size() == 1) {
            //<lista_naredbi> ::= <naredba>
            ((NezavrsniZnak)getChild(0)).provjeri();
        } else {
            //<lista_naredbi> ::= <lista_naredbi> <naredba>
            ((NezavrsniZnak)getChild(0)).provjeri();
            ((NezavrsniZnak)getChild(1)).provjeri();
        }
    }

    @Override
    public String parse() {
        if(children.size() == 1) {
            //<lista_naredbi> ::= <naredba>
            return ((NezavrsniZnak)getChild(0)).parse();
        } else {
            //<lista_naredbi> ::= <lista_naredbi> <naredba>
            return ((NezavrsniZnak)getChild(0)).parse() + ((NezavrsniZnak)getChild(1)).parse();
        }
    }
}
