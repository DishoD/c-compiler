public class LogIIzraz extends NezavrsniZnak {
    private String tip;
    private boolean lizraz;

    public LogIIzraz(Node parent) {
        super(parent, "<log_i_izraz>");
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
            BinIliIzraz ai = (BinIliIzraz)getChild(0);
            ai.provjeri();

            tip = ai.getTip();
            lizraz = ai.isLizraz();
        } else {
            LogIIzraz oi = (LogIIzraz)getChild(0);
            BinIliIzraz ai = (BinIliIzraz)getChild(2);
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
            //<log_i_izraz> ::= <bin_ili_izraz>
            BinIliIzraz bili = (BinIliIzraz)getChild(0);
            return bili.parse();
        } else {
            //<log_i_izraz> ::= <log_i_izraz> OP_I <bin_ili_izraz>
            LogIIzraz logi = (LogIIzraz)getChild(0);
            BinIliIzraz binili = (BinIliIzraz)getChild(2);
            return  GeneratorKoda.logIIzraz(logi.parse(), binili.parse());
        }

    }
}