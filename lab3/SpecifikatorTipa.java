public class SpecifikatorTipa extends NezavrsniZnak {
    private String tip;

    public SpecifikatorTipa(Node parent) {
        super(parent, "<specifikator_tipa>");
    }

    @Override
    public void provjeri() {
        this.tip = ((UniformniZnak)getChild(0)).getGrupiraniZnakovi();
    }

    @Override
    public String parse() {
        //TODO
        return null;
    }

    public String getTip() {
        return tip;
    }
}