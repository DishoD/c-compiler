public class CastIzraz extends NezavrsniZnak {
    private String tip;
    private boolean lizraz;

    public CastIzraz(Node parent) {
        super(parent, "<cast_izraz>");
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
            UnarniIzraz ui = (UnarniIzraz)getChild(0);
            ui.provjeri();

            tip = ui.getTip();
            lizraz = ui.isLizraz();
        } else {
            ImeTipa it = (ImeTipa)getChild(1);
            CastIzraz ci = (CastIzraz)getChild(3);
            it.provjeri();
            ci.provjeri();

            if(!TipoviUtility.castableExplicit(ci.getTip(), it.getTip())) greska();

            tip = it.getTip();
            lizraz = false;
        }
    }

    @Override
    public String parse() {
        if(children.size() == 1) {
            //<cast_izraz> ::= <unarni_izraz>
            UnarniIzraz ui = (UnarniIzraz)getChild(0);
            return ui.parse();
        } else {
            //<cast_izraz> ::= L_ZAGRADA <ime_tipa> D_ZAGRADA <cast_izraz>
            ImeTipa it = (ImeTipa)getChild(1);
            CastIzraz ci = (CastIzraz)getChild(3);
            return ci.parse();
        }
    }

}