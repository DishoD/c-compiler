public class LogIliIzraz extends NezavrsniZnak {
    private String tip;
    private boolean lizraz;

    public LogIliIzraz(Node parent) {
        super(parent, "<log_ili_izraz>");
    }

    public String getTip() {
        return tip;
    }

    public boolean isLizraz() {
        return lizraz;
    }

    @Override
    public void provjeri() {
        //<log_ili_izraz> ::= <log_i_izraz>
        if(children.size() == 1) {
            LogIIzraz ai = (LogIIzraz)getChild(0);
            ai.provjeri();

            tip = ai.getTip();
            lizraz = ai.isLizraz();
        } else {
            //<log_ili_izraz> ::= <log_ili_izraz> OP_ILI <log_i_izraz>
            LogIliIzraz oi = (LogIliIzraz)getChild(0);
            LogIIzraz ai = (LogIIzraz)getChild(2);
            oi.provjeri();
            if(!TipoviUtility.castableImplicit(oi.getTip(), "int")) greska();

            ai.provjeri();
            if(!TipoviUtility.castableImplicit(ai.getTip(), "int")) greska();

            tip = "int";
            lizraz = false;
        }
    }

    public String parse() {
        String ret;
        //<log_ili_izraz> ::= <log_i_izraz>
        if(children.size() == 1) {
            LogIIzraz li = (LogIIzraz)getChild(0);
            ret = li.parse();
        } else {
            //<log_ili_izraz> ::= <log_ili_izraz> OP_ILI <log_i_izraz>
            LogIliIzraz lili = (LogIliIzraz)getChild(0);
            LogIIzraz li = (LogIIzraz)getChild(2);

            ret = GeneratorKoda.logIliIzraz(lili.parse(), li.parse());
        }

        return ret;
    }
}