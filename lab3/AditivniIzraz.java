public class AditivniIzraz extends NezavrsniZnak {
    private String tip;
    private boolean lizraz;

    public AditivniIzraz(Node parent) {
        super(parent, "<aditivni_izraz>");
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
            // <aditivni_izraz> ::= <multiplikativni_izraz>
            MultiplikativniIzraz mi = (MultiplikativniIzraz)getChild(0);
            mi.provjeri();

            tip = mi.getTip();
            lizraz = mi.isLizraz();
        } else {
            // <aditivni_izraz> ::= <aditivni_izraz> (PLUS | MINUS) <multiplikativni_izraz>
            AditivniIzraz ai = (AditivniIzraz)getChild(0);
            MultiplikativniIzraz mi = (MultiplikativniIzraz)getChild(2);
            ai.provjeri();
            if(!TipoviUtility.castableImplicit(ai.getTip(), "int")) greska();

            mi.provjeri();
            if(!TipoviUtility.castableImplicit(mi.getTip(), "int")) greska();

            tip = "int";
            lizraz = false;
        }
    }

    @Override
    public String parse() {
        if(children.size() == 1) {
            // <aditivni_izraz> ::= <multiplikativni_izraz>
            MultiplikativniIzraz mi = (MultiplikativniIzraz)getChild(0);
            return mi.parse();
        } else {
            // <aditivni_izraz> ::= <aditivni_izraz> (PLUS | MINUS) <multiplikativni_izraz>
            AditivniIzraz ai = (AditivniIzraz)getChild(0);
            MultiplikativniIzraz mi = (MultiplikativniIzraz)getChild(2);
            StringBuilder sb =  new StringBuilder();
            sb.append(ai.parse()).append(mi.parse());

            String op = getChildAsUniformniZnak(1).getToken();

            if(op.equals("PLUS")) {
                sb.append(GeneratorKoda.opAdd());
            } else {
                sb.append(GeneratorKoda.opSub());
            }
            return sb.toString();
        }
    }
}