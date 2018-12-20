public class InitDeklarator extends NezavrsniZnak {
    private String ntip;

    public InitDeklarator(Node parent) {
        super(parent, "<init_deklarator>");
    }

    public String getNtip() {
        return ntip;
    }

    public void setNtip(String ntip) {
        this.ntip = ntip;
    }

    @Override
    public void provjeri() {
        if(provjera == null) {
            provjera = children.size() == 1 ? new V1() : new V2();
        }

        provjera.provjeri();
    }

    /**
     * <init_deklarator> ::= <izravni_deklarator>
     */
    private class V1 implements Provjerljiv {

        @Override
        public void provjeri() {
            IzravniDeklarator id = (IzravniDeklarator)getChild(0);
            id.setNtip(ntip);
            id.provjeri();

            if(!id.isFunction()){
                String tip = id.getTip();
                if(TipoviUtility.isConst(tip) || TipoviUtility.isConstArray(tip)) greska();
            }
        }
    }

    /**
     * <init_deklarator> ::= <izravni_deklarator> OP_PRIDRUZI <inicijalizator>
     */
    private class V2 implements Provjerljiv {

        @Override
        public void provjeri() {
            IzravniDeklarator id = (IzravniDeklarator)getChild(0);
            id.setNtip(ntip);
            id.provjeri();

            Inicijalizator init = (Inicijalizator)getChild(2);
            init.provjeri();
            //TODO
        }
    }
}
