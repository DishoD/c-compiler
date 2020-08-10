public class OdnosniIzraz extends NezavrsniZnak {
    private String tip;
    private boolean lizraz;

    public OdnosniIzraz(Node parent) {
        super(parent, "<odnosni_izraz>");
    }

    public String getTip() {
        return tip;
    }

    public boolean isLizraz() {
        return lizraz;
    }

    @Override
    public void provjeri() {
        if(children.size() == 1) {
            AditivniIzraz ai = (AditivniIzraz)getChild(0);
            ai.provjeri();

            tip = ai.getTip();
            lizraz = ai.isLizraz();
        } else {
            OdnosniIzraz oi = (OdnosniIzraz)getChild(0);
            AditivniIzraz ai = (AditivniIzraz)getChild(2);
            oi.provjeri();
            if(!TipoviUtility.castableImplicit(oi.getTip(), "int")) greska();

            ai.provjeri();
            if(!TipoviUtility.castableImplicit(ai.getTip(), "int")) greska();

            tip = "int";
            lizraz = false;
        }
    }

    @Override
    public String parse() {
        if(children.size() == 1) {
            //<odnosni_izraz> ::= <aditivni_izraz>
            AditivniIzraz ai = (AditivniIzraz)getChild(0);
            return ai.parse();
        } else {
            //<odnosni_izraz> ::=
            //	| <odnosni_izraz> OP_LT <aditivni_izraz>
            //	| <odnosni_izraz> OP_GT <aditivni_izraz>
            //	| <odnosni_izraz> OP_LTE <aditivni_izraz>
            //	| <odnosni_izraz> OP_GTE <aditivni_izraz>
            OdnosniIzraz oi = (OdnosniIzraz)getChild(0);
            AditivniIzraz ai = (AditivniIzraz)getChild(2);
            StringBuilder sb = new StringBuilder();
            sb.append(oi.parse()).append(ai.parse());

            String op = getChildAsUniformniZnak(1).getToken();

            if(op.equals("OP_LT")) {
                sb.append(GeneratorKoda.opLT());
            } else if(op.equals("OP_GT")) {
                sb.append(GeneratorKoda.opGT());
            } else if(op.equals("OP_LTE")) {
                sb.append(GeneratorKoda.opLTE());
            } else if(op.equals("OP_GTE")) {
                sb.append(GeneratorKoda.opGTE());
            }

            return sb.toString();
        }
    }
}