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
}