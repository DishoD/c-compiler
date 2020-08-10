public class UnarniOperator extends NezavrsniZnak {

    public UnarniOperator(Node parent) {
        super(parent, "<unarni_operator>");
    }

    @Override
    public void provjeri() {
        //nema semanticne provjere
    }

    @Override
    public String parse() {
        //TODO
        return null;
    }
}