public class JednakosniIzraz extends NezavrsniZnak {
    private String tip;
    private boolean lizraz;

    public JednakosniIzraz(Node parent) {
        super(parent, "<jednakosni_izraz>");
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
            OdnosniIzraz ai = (OdnosniIzraz)getChild(0);
            ai.provjeri();

            tip = ai.getTip();
            lizraz = ai.isLizraz();
        } else {
            JednakosniIzraz oi = (JednakosniIzraz)getChild(0);
            OdnosniIzraz ai = (OdnosniIzraz)getChild(2);
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
            //<jednakosni_izraz> ::= <odnosni_izraz>
            OdnosniIzraz oi = (OdnosniIzraz)getChild(0);
            return  oi.parse();
        } else {
            JednakosniIzraz ji = (JednakosniIzraz)getChild(0);
            OdnosniIzraz oi = (OdnosniIzraz)getChild(2);
            StringBuilder sb = new StringBuilder();
            sb.append(ji.parse()).append(oi.parse());

            String op = getChildAsUniformniZnak(1).getToken();
            if(op.equals("OP_EQ")) {
                //<jednakosni_izraz> ::= <jednakosni_izraz> OP_EQ <odnosni_izraz>
                sb.append(GeneratorKoda.opEq());
            } else {
                //<jednakosni_izraz> ::= <jednakosni_izraz> OP_NEQ <odnosni_izraz>
                sb.append(GeneratorKoda.opNe());
            }

            return sb.toString();
        }
    }
}