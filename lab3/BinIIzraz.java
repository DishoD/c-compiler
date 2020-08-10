public class BinIIzraz extends NezavrsniZnak {
    private String tip;
    private boolean lizraz;

    public BinIIzraz(Node parent) {
        super(parent, "<bin_i_izraz>");
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
            JednakosniIzraz ai = (JednakosniIzraz)getChild(0);
            ai.provjeri();

            tip = ai.getTip();
            lizraz = ai.isLizraz();
        } else {
            BinIIzraz oi = (BinIIzraz)getChild(0);
            JednakosniIzraz ai = (JednakosniIzraz)getChild(2);
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
            //<bin_i_izraz> ::= <jednakosni_izraz>
            JednakosniIzraz ji = (JednakosniIzraz)getChild(0);
            return ji.parse();
        } else {
            //<bin_i_izraz> ::= <bin_i_izraz> OP_BIN_I <jednakosni_izraz>
            BinIIzraz bi = (BinIIzraz)getChild(0);
            JednakosniIzraz ji = (JednakosniIzraz)getChild(2);
            StringBuilder sb = new StringBuilder();
            sb.append(bi.parse())
                    .append(ji.parse())
                    .append(GeneratorKoda.opBinI());
            return sb.toString();
        }
    }
}