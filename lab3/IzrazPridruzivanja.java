public class IzrazPridruzivanja extends NezavrsniZnak {
    private String tip;
    private boolean lizraz;

    public IzrazPridruzivanja(Node parent) {
        super(parent, "<izraz_pridruzivanja>");
    }

    public boolean isLizraz() {
        return lizraz;
    }

    public String getTip() {
        return tip;
    }

    @Override
    public void provjeri() {
        if(children.size() == 1) {
            LogIliIzraz liz = (LogIliIzraz)getChild(0);
            liz.provjeri();

            tip = liz.getTip();
            lizraz = liz.isLizraz();
        } else {
            PostfiksIzraz pi = (PostfiksIzraz)getChild(0);
            IzrazPridruzivanja ip = (IzrazPridruzivanja)getChild(2);
            pi.provjeri();
            if(!pi.isLizraz()) greska();

            ip.provjeri();
            if(!TipoviUtility.castableImplicit(ip.getTip(), pi.getTip())) greska();

            tip = pi.getTip();
            lizraz = false;
        }
    }


}
