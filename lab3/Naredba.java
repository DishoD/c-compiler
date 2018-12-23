public class Naredba extends NezavrsniZnak {

    public Naredba(Node parent) {
        super(parent, "<naredba>");
    }

    @Override
    public void provjeri() {
        NezavrsniZnak znak = getChildAsNezavrsniZnak(0);
        if(znak instanceof SlozenaNaredba) {
            TablicaZnakova.stvoriNoviDjelokrug(Djelokrug.Oznaka.BLOK, null);
            znak.provjeri();
            TablicaZnakova.vratiSe();
        } else {
            znak.provjeri();
        }
    }
}