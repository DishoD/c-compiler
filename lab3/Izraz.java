public class Izraz extends  NezavrsniZnak {
    private String tip;
    private boolean lizraz;
    private boolean isFunction;
    private PrototipFunkcije prototipFunkcije;

    public Izraz(Node parent) {
        super(parent, "<izraz>");
    }

    public String getTip() {
        return tip;
    }

    public boolean isFunction() {
        return isFunction;
    }

    public PrototipFunkcije getPrototipFunkcije() {
        return prototipFunkcije;
    }

    public boolean isLizraz() {
        return lizraz;
    }

    @Override
    public void provjeri() {

    }

}
