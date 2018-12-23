import java.util.ArrayList;
import java.util.List;

public class Inicijalizator extends NezavrsniZnak {
    private String tip;
    private boolean isCharArray;
    private int brElem;
    private List<String> tipovi = new ArrayList<>();

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
                //TODO pr 964, 732, 695
            }
            UniformniZnak uz = primarniIzraz.getChildAsUniformniZnak(0);
            if(uz.getToken().equals("NIZ_ZNAKOVA")) {
                tip = "niz(const(char))";
                isCharArray = true;
                String grupirani = uz.getGrupiraniZnakovi();
                int slashCnt = 0;
                for(int i = 0; i < grupirani.length(); ++i) {
                    char c = grupirani.charAt(i);
                   if(c == '\\') {
                       slashCnt++;
                       i++;
                   }
                }
                brElem = grupirani.length() - 1 - slashCnt;
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
