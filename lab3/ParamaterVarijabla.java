public class ParamaterVarijabla extends Varijabla implements StogParametar {
    private int index;

    public ParamaterVarijabla(String idn, String type, int brElem, Djelokrug pripadaDjelokrugu) {
        super(idn, type, brElem, pripadaDjelokrugu);
        index = pripadaDjelokrugu.getBrojParametaraFunkcije();
        pripadaDjelokrugu.setBrojParametaraFunkcije(index+1);
    }

    @Override
    public int getVelicina() {
        return 4;
    }

    @Override
    public int getOffsetOdVrha() {
        return (pripadaDjelokrugu.getBrojParametaraFunkcije() - index)*4 + pripadaDjelokrugu.getVelicina() + Djelokrug.getPocetnaVelicinaStoga();
    }
}
