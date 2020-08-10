public class Izraz extends  NezavrsniZnak {
    private String tip;
    private boolean lizraz;

    public Izraz(Node parent) {
        super(parent, "<izraz>");
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
            IzrazPridruzivanja ip = (IzrazPridruzivanja)getChild(0);
            ip.provjeri();

            tip = ip.getTip();
            lizraz = ip.isLizraz();
        } else {
            Izraz iz = (Izraz)getChild(0);
            IzrazPridruzivanja ip = (IzrazPridruzivanja)getChild(2);
            iz.provjeri();
            ip.provjeri();

            tip = ip.getTip();
            lizraz = false;
        }
    }

    @Override
    public String parse() {
        if(children.size() == 1) {
            //<izraz> ::= <izraz_pridruzivanja>
            IzrazPridruzivanja ip = (IzrazPridruzivanja)getChild(0);
            return ip.parse();
        }
        return null;
    }
}
