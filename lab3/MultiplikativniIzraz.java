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
}