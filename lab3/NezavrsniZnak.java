public abstract class NezavrsniZnak extends Node{
    protected interface Provjerljiv {
        void provjeri();
    }

    private String naziv;
    protected Provjerljiv provjera;

    public NezavrsniZnak(Node parent, String naziv) {
        super(parent);
        this.naziv = naziv;
    }

    public abstract void provjeri();
    public abstract String parse();

    public void greska() {
        StringBuilder sb = new StringBuilder();
        sb.append(this).append(" ::=");

        for(Node child : children)
            sb.append(" ").append(child);

        sb.append('\n');
        System.out.print(sb.toString());
        System.exit(0);
    }

    protected NezavrsniZnak getChildAsNezavrsniZnak(int index) {
        return (NezavrsniZnak)getChild(index);
    }

    protected UniformniZnak getChildAsUniformniZnak(int index) {
        return (UniformniZnak)getChild(index);
    }


    public String getNaziv() {
        return naziv;
    }
    public NezavrsniZnak getParent() {
        return (NezavrsniZnak)parent;
    }

    @Override
    public String toString() {
        return naziv;
    }
}
