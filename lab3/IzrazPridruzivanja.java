public class IzrazPridruzivanja extends NezavrsniZnak {
    private String tip;

    public IzrazPridruzivanja(Node parent) {
        super(parent, "<izraz_pridruzivanja>");
    }

    @Override
    public void provjeri() {
        //TODO
    }

    public String getTip() {
        return tip;
    }
}
