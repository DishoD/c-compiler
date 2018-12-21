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
            MultiplikativniIzraz mi = (MultiplikativniIzraz)getChild(0);
            mi.provjeri();

            tip = mi.getTip();
            lizraz = mi.isLizraz();
        } else {
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
}