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
}