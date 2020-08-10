public class PrijevodnaJedinica extends NezavrsniZnak {

    public PrijevodnaJedinica(Node parent) {
        super(parent, "<prijevodna_jedinica>");
    }

    @Override
    public void provjeri() {
        if(provjera == null) {
            if(children.size() == 1)
                provjera = new V1();
            else
                provjera = new V2();
        }

        provjera.provjeri();
    }

    @Override
    public String parse() {
        if(children.size() == 1) //<prijevodna_jedinica> ::= <vanjska_deklaracija>
            return getChildAsNezavrsniZnak(0).parse();
        else //<prijevodna_jedinica> ::= <prijevodna_jedinica> <vanjska_deklaracija>
            getChildAsNezavrsniZnak(0).parse();
            getChildAsNezavrsniZnak(1).parse();
            return null;
    }

    /**
     * <prijevodna_jedinica> ::= <vanjska_deklaracija>
     */
    private class V1 implements Provjerljiv {
        @Override
        public void provjeri() {
            ((NezavrsniZnak)getChild(0)).provjeri();
        }
    }

    /**
     * <prijevodna_jedinica> ::= <prijevodna_jedinica> <vanjska_deklaracija>
     */
    private class V2 implements Provjerljiv {
        @Override
        public void provjeri() {
            ((NezavrsniZnak)getChild(0)).provjeri();
            ((NezavrsniZnak)getChild(1)).provjeri();
        }
    }
}
