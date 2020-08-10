public class StogVarijabla extends Varijabla implements StogParametar{
    private int offsetOdDna;
    private int velicina;

    public StogVarijabla(String idn, String type, int brElem, Djelokrug pripadaDjelokrugu) {
        super(idn, type, brElem, pripadaDjelokrugu);

        offsetOdDna = pripadaDjelokrugu.getVelicina();
        velicina = Math.abs(brElem)*4;
        pripadaDjelokrugu.setVelicina(offsetOdDna+velicina);
        pripadaDjelokrugu.setBrojLokalnihVarijabli(pripadaDjelokrugu.getBrojLokalnihVarijabli()+1);
    }

    @Override
    public int getVelicina() {
        return velicina;
    }

    @Override
    public int getOffsetOdVrha() {
        int n = Math.abs(this.brElem);
        return pripadaDjelokrugu.getVelicina() - offsetOdDna - n*4;
    }
}
