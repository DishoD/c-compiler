public class ListaDeklaracija extends NezavrsniZnak {

    public ListaDeklaracija(Node parent) {
        super(parent, "<lista_deklaracija>");
    }

    @Override
    public void provjeri() {
        if(children.size() == 1) {
            //<lista_deklaracija> ::= <deklaracija>
            getChildAsNezavrsniZnak(0).provjeri();
        } else {
            //<lista_deklaracija> ::= <lista_deklaracija> <deklaracija>
            getChildAsNezavrsniZnak(0).provjeri();
            getChildAsNezavrsniZnak(1).provjeri();
        }
    }

    @Override
    public String parse() {
        if(children.size() == 1) {
            //<lista_deklaracija> ::= <deklaracija>
            return getChildAsNezavrsniZnak(0).parse();
        } else {
            //<lista_deklaracija> ::= <lista_deklaracija> <deklaracija>
            return getChildAsNezavrsniZnak(0).parse() + getChildAsNezavrsniZnak(1).parse();
        }
    }
}
