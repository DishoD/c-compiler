public class UnarniIzraz extends NezavrsniZnak {
    private String tip;
    private boolean lizraz;

    public UnarniIzraz(Node parent) {
        super(parent, "<unarni_izraz>");
    }

    public String getTip() {
        return tip;
    }

    public boolean isLizraz() {
        return lizraz;
    }

    @Override
    public void provjeri() {
        if(provjera == null) {
            if(children.size() == 1) {
                provjera = new V1();
            } else if(getChild(0) instanceof UniformniZnak) {
                provjera = new V2();
            } else {
                provjera = new V3();
            }
        }

        provjera.provjeri();
    }

    /**
     * <unarni_izraz> ::= <postfiks_izraz>
     */
    private class V1 implements Provjerljiv {

        @Override
        public void provjeri() {
            PostfiksIzraz pi = (PostfiksIzraz)getChild(0);
            pi.provjeri();

            tip = pi.getTip();
            lizraz = pi.isLizraz();
        }
    }

    /**
     * <unarni_izraz> ::= (OP_INC | OP_DEC) <unarni_izraz>
     */
    private class V2 implements Provjerljiv {

        @Override
        public void provjeri() {
            UnarniIzraz ui = (UnarniIzraz)getChild(1);
            ui.provjeri();
            if(!ui.isLizraz() || !TipoviUtility.castableImplicit(ui.getTip(), "int")) greska();

            tip = "int";
            lizraz = false;
        }
    }

    /**
     * <unarni_izraz> ::= <unarni_operator> <cast_izraz>
     */
    private class V3 implements Provjerljiv {

        @Override
        public void provjeri() {
            CastIzraz ci = (CastIzraz)getChild(1);
            ci.provjeri();
            if(!TipoviUtility.castableImplicit(ci.getTip(), "int")) greska();

            tip = "int";
            lizraz = false;
        }
    }
}