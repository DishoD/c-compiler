public class BinIliIzraz extends NezavrsniZnak {
    private String tip;
    private boolean lizraz;

    public BinIliIzraz(Node parent) {
        super(parent, "<bin_ili_izraz>");
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
            BinXiliIzraz ai = (BinXiliIzraz)getChild(0);
            ai.provjeri();

            tip = ai.getTip();
            lizraz = ai.isLizraz();
        } else {
            BinIliIzraz oi = (BinIliIzraz)getChild(0);
            BinXiliIzraz ai = (BinXiliIzraz)getChild(2);
            oi.provjeri();
            if(!TipoviUtility.castableImplicit(oi.getTip(), "int")) greska();

            ai.provjeri();
            if(!TipoviUtility.castableImplicit(ai.getTip(), "int")) greska();

            tip = "int";
            lizraz = false;
        }
    }
}