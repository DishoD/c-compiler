public class Naredba extends NezavrsniZnak {

    public Naredba(Node parent) {
        super(parent, "<naredba>");
    }

    @Override
    public void provjeri() {
        getChildAsNezavrsniZnak(0).provjeri();
    }
}