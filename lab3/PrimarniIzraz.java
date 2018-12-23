import java.util.HashSet;
import java.util.Set;

public class PrimarniIzraz extends NezavrsniZnak {
    private String tip;
    private boolean lizraz;
    private boolean isFunction;
    private PrototipFunkcije prototipFunkcije;

    private static final Set<String> DOZVOLJENI_CHAROVI = new HashSet<>();
     static {
         DOZVOLJENI_CHAROVI.add("\\t");
         DOZVOLJENI_CHAROVI.add("\\n");
         DOZVOLJENI_CHAROVI.add("\\0");
         DOZVOLJENI_CHAROVI.add("\\'");
         DOZVOLJENI_CHAROVI.add("\\\"");
         DOZVOLJENI_CHAROVI.add("\\\\");
     }

    public PrimarniIzraz(Node parent) {
        super(parent, "<primarni_izraz>");
    }

    public String getTip() {
        return tip;
    }

    public boolean isLizraz() {
        return lizraz;
    }

    public boolean isFunction() {
        return isFunction;
    }

    public PrototipFunkcije getPrototipFunkcije() {
        return prototipFunkcije;
    }

    private static boolean isStringValid(String str) {
         str = str.substring(1, str.length()-1);
         for(int i = 0; i < str.length(); ++i) {
             char c = str.charAt(i);
             if(c == '\\') {
                 if(i+1 >= str.length()) return false;
                 if(!DOZVOLJENI_CHAROVI.contains(String.format("%c%c", c, str.charAt(i+1)))) return false;
                 i++;
             }
         }
         //TODO :ispravi za slicajeve \\"
         return true;
    }

    @Override
    public void provjeri() {
        if(children.size() == 1) {
            UniformniZnak znak = getChildAsUniformniZnak(0);

            switch (znak.getToken()){
                case "IDN":
                    //TODO pararelno se mora provjeravati postoji li i funkcija i varijabla
                    Djelokrug d = TablicaZnakova.getTrenutniDjelokrug().getDjelokrugOfVariable(znak.getGrupiraniZnakovi());
                    if(d == null) {
                        //ako je idn funkcija
                        isFunction = true;
                        d = TablicaZnakova.getTrenutniDjelokrug().getDjelokrugOfFunction(znak.getGrupiraniZnakovi());
                        if(d == null) greska();
                        prototipFunkcije = d.getFunkcija(znak.getGrupiraniZnakovi());
                        tip = prototipFunkcije.getReturnType();
                    } else {
                        //ako je idn varijabla
                        Varijabla var = d.getVarijabla(znak.getGrupiraniZnakovi());
                        tip = var.getType();
                        lizraz = var.isLizraz();
                    }
                    break;

                case "BROJ":
                    try{
                        Integer.parseInt(znak.getGrupiraniZnakovi());
                    } catch (NumberFormatException e) {
                        greska();
                    }
                    tip = "int";
                    lizraz = false;
                    break;

                case "ZNAK":
                    String c = znak.getGrupiraniZnakovi();
                    c = c.substring(1, c.length()-1);
                    if(c.length() > 1 && !DOZVOLJENI_CHAROVI.contains(c)) greska();
                    tip = "char";
                    lizraz = false;
                    break;

                case "NIZ_ZNAKOVA":
                    if(!isStringValid(znak.getGrupiraniZnakovi())) greska();
                    tip = "niz(const(char))";
                    lizraz = false;
                    break;
            }
        } else {
            Izraz izraz = (Izraz)getChild(1);
            izraz.provjeri();
            tip = izraz.getTip();
            lizraz = izraz.isLizraz();
        }
    }
}