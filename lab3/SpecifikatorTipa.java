public class SpecifikatorTipa extends NezavrsniZnak {
    private String tip;

    public SpecifikatorTipa(Node parent) {
        super(parent, "<specifikator_tipa>");
    }

    @Override
    public void provjeri() {
        this.tip = ((UniformniZnak)getChild(0)).getGrupiraniZnakovi();
    }

    public String getTip() {
        return tip;
    }
}