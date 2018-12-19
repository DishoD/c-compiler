public class SlozenaNaredba extends  NezavrsniZnak {

    public SlozenaNaredba(Node parent) {
        super(parent, "<slozena_naredba>");
    }

    @Override
    public void provjeri() {
        if(children.size() == 3) {
            // <slozena_naredba> ::= L_VIT_ZAGRADA <lista_naredbi> D_VIT_ZAGRADA
            ((NezavrsniZnak)getChild(1)).provjeri();
        } else {
            //<slozena_naredba> ::= L_VIT_ZAGRADA <lista_deklaracija> <lista_naredbi> D_VIT_ZAGRADA
            ((NezavrsniZnak)getChild(1)).provjeri();
            ((NezavrsniZnak)getChild(2)).provjeri();
        }
    }
}
