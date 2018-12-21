public class BinXiliIzraz extends NezavrsniZnak {
    private String tip;
    private boolean lizraz;

    public BinXiliIzraz(Node parent) {
        super(parent, "<bin_xili_izraz>");
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
            BinIIzraz ai = (BinIIzraz)getChild(0);
            ai.provjeri();

            tip = ai.getTip();
            lizraz = ai.isLizraz();
        } else {
            BinXiliIzraz oi = (BinXiliIzraz)getChild(0);
            BinIIzraz ai = (BinIIzraz)getChild(2);
            oi.provjeri();
            if(!TipoviUtility.castableImplicit(oi.getTip(), "int")) greska();

            ai.provjeri();
            if(!TipoviUtility.castableImplicit(ai.getTip(), "int")) greska();

            tip = "int";
            lizraz = false;
        }
    }
}