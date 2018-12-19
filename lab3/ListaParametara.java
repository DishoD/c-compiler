import java.util.ArrayList;
import java.util.List;

public class ListaParametara extends NezavrsniZnak {
    private List<String> tipovi = new ArrayList<>();
    private List<String> imena = new ArrayList<>();

    public ListaParametara(Node parent) {
        super(parent, "<lista_parametara>");
    }

    @Override
    public void provjeri() {
        if(provjera == null) {
            provjera = children.size() == 1 ? new V1() : new V2();
        }

        provjera.provjeri();
    }

    public List<String> getTipovi() {
        return tipovi;
    }

    public List<String> getImena() {
        return imena;
    }

    /**
     * <lista_parametara> ::= <deklaracija_parametra>
     */
    private class V1 implements Provjerljiv {

        @Override
        public void provjeri() {
            DeklaracijaParametra dp = (DeklaracijaParametra)getChild(0);
            dp.provjeri();

            tipovi.add(dp.getTip());
            imena.add(dp.getIme());
        }
    }

    /**
     * <lista_parametara> ::= <lista_parametara> ZAREZ <deklaracija_parametra>
     */
    private class V2 implements Provjerljiv {

        @Override
        public void provjeri() {
            ListaParametara lp = (ListaParametara)getChild(0);
            DeklaracijaParametra dp = (DeklaracijaParametra)getChild(1);

            lp.provjeri();
            dp.provjeri();

            if(lp.getImena().contains(dp.getIme())) greska();

            tipovi.addAll(lp.getTipovi());
            tipovi.add(dp.getTip());

            imena.addAll(lp.getImena());
            imena.add(dp.getIme());
        }
    }
}
