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

    public void greska() {
        StringBuilder sb = new StringBuilder();
        sb.append(this).append(" ::= ");

        for(Node child : children)
            sb.append(child).append(" ");

        System.out.println(sb.toString());
        System.exit(0);
    }

    public String getNaziv() {
        return naziv;
    }

    @Override
    public String toString() {
        return naziv;
    }
}
