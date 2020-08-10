public class MultiplikativniIzraz extends NezavrsniZnak {
    private String tip;
    private boolean lizraz;

    public MultiplikativniIzraz(Node parent) {
        super(parent, "<multiplikativni_izraz>");
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
            CastIzraz ci = (CastIzraz)getChild(0);
            ci.provjeri();

            tip = ci.getTip();
            lizraz = ci.isLizraz();
        } else {
            MultiplikativniIzraz mi = (MultiplikativniIzraz)getChild(0);
            CastIzraz ci = (CastIzraz)getChild(2);
            mi.provjeri();
            if(!TipoviUtility.castableImplicit(mi.getTip(), "int")) greska();

            ci.provjeri();
            if(!TipoviUtility.castableImplicit(ci.getTip(), "int")) greska();

            tip = "int";
            lizraz = false;
        }
    }

    @Override
    public String parse() {
        if(children.size() == 1) {
            //<multiplikativni_izraz> ::= <cast_izraz>
            CastIzraz ci = (CastIzraz)getChild(0);
            return ci.parse();
        } else {
            //<multiplikativni_izraz> ::=
            //	| <multiplikativni_izraz> OP_PUTA <cast_izraz>
            //	| <multiplikativni_izraz> OP_DIJELI <cast_izraz>
            //	| <multiplikativni_izraz> OP_MOD <cast_izraz>
            MultiplikativniIzraz mi = (MultiplikativniIzraz)getChild(0);
            CastIzraz ci = (CastIzraz)getChild(2);
            StringBuilder sb = new StringBuilder();
            sb.append(mi.parse()).append(ci.parse());

            String op = getChildAsUniformniZnak(1).getToken();
            if(op.equals("OP_PUTA")) {
                sb.append(GeneratorKoda.opMUL());
            } else if(op.equals("OP_DIJELI")) {
                sb.append(GeneratorKoda.opDIV());
            } else {
                sb.append(GeneratorKoda.opMOD());
            }

            return sb.toString();
        }
    }
}