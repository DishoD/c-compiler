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
