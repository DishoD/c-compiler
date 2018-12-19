public class IzrazNaredba extends NezavrsniZnak {
    private String tip;

    public IzrazNaredba(Node parent) {
        super(parent, "<izraz_naredba>");
    }

    public String getTip() {
        return tip;
    }

    @Override
    public void provjeri() {
        if(children.size() == 1) {
            tip = "int";
        } else {
            Izraz izraz = (Izraz)getChild(0);
            izraz.provjeri();
            tip = izraz.getTip();
        }
    }
}
