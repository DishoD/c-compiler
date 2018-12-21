public class Inicijalizator extends NezavrsniZnak {

    public Inicijalizator(Node parent) {
        super(parent, "<inicijalizator>");
    }

    @Override
    public void provjeri() {
        if(children.size() == 1) {
            IzrazPridruzivanja ip = (IzrazPridruzivanja)getChild(0);
            ip.provjeri();
        }
    }
}
