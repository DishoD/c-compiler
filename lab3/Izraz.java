public class Izraz extends  NezavrsniZnak {
    private String tip;

    public Izraz(Node parent) {
        super(parent, "<izraz>");
    }

    public String getTip() {
        return tip;
    }

    @Override
    public void provjeri() {

    }
}
