import java.util.ArrayList;
import java.util.List;

public class Inicijalizator extends NezavrsniZnak {
    private String tip;
    private boolean isCharArray;
    private int brElem;
    private List<String> tipovi;

    public Inicijalizator(Node parent) {
        super(parent, "<inicijalizator>");
    }

    public String getTip() {
        return tip;
    }

    public boolean isCharArray() {
        return isCharArray;
    }

    public int getBrElem() {
        return brElem;
    }

    public List<String> getTipovi() {
        return tipovi;
    }


    @Override
    public void provjeri() {
        if(children.size() == 1) {
            IzrazPridruzivanja ip = (IzrazPridruzivanja)getChild(0);
            ip.provjeri();

            NezavrsniZnak primarniIzraz = ip.getChildAsNezavrsniZnak(0);
            while(!(primarniIzraz instanceof PrimarniIzraz)) {
                primarniIzraz = primarniIzraz.getChildAsNezavrsniZnak(0);
            }
            UniformniZnak uz = primarniIzraz.getChildAsUniformniZnak(0);
            if(uz.getToken().equals("NIZ_ZNAKOVA")) {
                tip = "niz(const(char))";
                isCharArray = true;
                brElem = uz.getGrupiraniZnakovi().length() + 1;
                tipovi = new ArrayList<>();
                for(int i = 0; i < brElem; ++i) tipovi.add("char");
            } else {
                tip = ip.getTip();
            }
        } else {
            ListaIzrazaPridruzivanja lip = (ListaIzrazaPridruzivanja)getChild(1);
            lip.provjeri();

            brElem = lip.getBrElem();
            tipovi = getTipovi();
        }
    }
}
