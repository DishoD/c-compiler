public class VanjskaDeklaracija extends NezavrsniZnak {
    public VanjskaDeklaracija(Node parent) {
        super(parent, "<vanjska_deklaracija>");
    }

    @Override
    public void provjeri() {
        ((NezavrsniZnak)getChild(0)).provjeri();
    }

    @Override
    public String parse() {
        NezavrsniZnak z = getChildAsNezavrsniZnak(0);

        if(z instanceof Deklaracija) {
            GeneratorKoda.addGlobalDeclaration(z.parse());
            return null;
        }

        return ((NezavrsniZnak)getChild(0)).parse();
    }
}
