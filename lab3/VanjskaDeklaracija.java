public class VanjskaDeklaracija extends NezavrsniZnak {
    public VanjskaDeklaracija(Node parent) {
        super(parent, "<vanjska_deklaracija>");
    }

    @Override
    public void provjeri() {
        ((NezavrsniZnak)getChild(0)).provjeri();
    }
}
