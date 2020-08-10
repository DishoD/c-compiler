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

    @Override
    public String parse() {
        if(children.size() == 1) {
            //<unarni_izraz> ::= <postfiks_izraz>
            PostfiksIzraz pi = (PostfiksIzraz)getChild(0);
            return pi.parseValue();
        } else if(getChild(0) instanceof UniformniZnak) {
            //<unarni_izraz> ::= (OP_INC | OP_DEC) <unarni_izraz>
            UnarniIzraz ui = (UnarniIzraz)getChild(1);
            StringBuilder sb = new StringBuilder();
            sb.append(ui.parseAddress());

            String op = getChildAsUniformniZnak(0).getToken();
            if(op.equals("OP_INC")) {
                sb.append(GeneratorKoda.opPreINC());
            } else {
                sb.append(GeneratorKoda.opPreDEC());
            }
            return sb.toString();
        } else {
            //<unarni_izraz> ::= <unarni_operator> <cast_izraz>
            UnarniOperator uop = (UnarniOperator)getChild(0);
            CastIzraz ci = (CastIzraz)getChild(1);
            StringBuilder sb = new StringBuilder();
            sb.append(ci.parse());

            String op = uop.getChildAsUniformniZnak(0).getToken();
            if(op.equals("MINUS")) {
                sb.append(GeneratorKoda.opUnMINUS());
            } else if(op.equals("OP_TILDA")) {
                sb.append(GeneratorKoda.opUnTILDA());
            } else if(op.equals("OP_NEG")) {
                sb.append(GeneratorKoda.opUnNEG());
            }

            return sb.toString();
        }
    }

    public String parseAddress() {
        if(children.size() == 1) {
            //<unarni_izraz> ::= <postfiks_izraz>
            PostfiksIzraz pi = (PostfiksIzraz)getChild(0);
            return pi.parseAddress();
        }
        return null;
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