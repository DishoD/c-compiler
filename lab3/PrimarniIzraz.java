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
             if(c == '"') return false;
             if(c == '\\') {
                 if(i+1 >= str.length()) return false;
                 if(!DOZVOLJENI_CHAROVI.contains(String.format("%c%c", c, str.charAt(i+1)))) return false;
                 i++;
             }
         }
         return true;
    }

    @Override
    public void provjeri() {
        if(children.size() == 1) {
            UniformniZnak znak = getChildAsUniformniZnak(0);

            switch (znak.getToken()){
                case "IDN":
                    Djelokrug d = TablicaZnakova.getTrenutniDjelokrug();
                    String ime = znak.getGrupiraniZnakovi();
                    while(true) {
                        if(d == null) greska();
                        if(d.postojiVarijabla(ime)) {
                            tip = d.getVarijabla(ime).getType();
                            lizraz = tip.equals("int") || tip.equals("char");
                            break;
                        } else if(d.postojiFunkcija(ime)) {
                            prototipFunkcije = d.getFunkcija(ime);
                            tip = prototipFunkcije.getReturnType();
                            isFunction = true;
                            break;
                        }
                        d = d.getParent();
                    }
                    break;
                case "BROJ":
                    String broj = znak.getGrupiraniZnakovi();
                    try{
                        if(broj.startsWith("0x") || broj.startsWith("0X")) {
                            Integer.parseInt(broj.substring(2), 16);
                        } else {
                            Integer.parseInt(broj);
                        }
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
                    if(c.equals("\\")) greska();
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

    @Override
    public String parse() {
        //ne koristiti
        return null;
    }

    public String parseValue() {
        StringBuilder sb = new StringBuilder();

        if(children.size() == 1) {
            String token = getChildAsUniformniZnak(0).getToken();
            String znak = getChildAsUniformniZnak(0).getGrupiraniZnakovi();

            if(token.equals("IDN")) {
                //<primarni_izraz> ::= IDN
                if(TipoviUtility.isArray(tip)) return this.parseAddress();

                sb.append(this.parseAddress()).append(GeneratorKoda.dereferenceAddresse());
                return sb.toString();
            } else if(token.equals("BROJ")) {
                int k;
                if(znak.startsWith("0x") || znak.startsWith("0X")) {
                    k = Integer.parseInt(znak.substring(2), 16);
                } else {
                    k = Integer.parseInt(znak);
                }
                return GeneratorKoda.pushConstant(k);
            } else if(token.equals("ZNAK")) {
                znak = znak.substring(1, znak.length()-1);
                char c = znak.charAt(0);
                return GeneratorKoda.pushConstant(c);
            } else if(token.equals("NIZ_ZNAKOVA")) {
                ////<primarni_izraz> ::= NIZ_ZNAKOVA
                return this.parseAddress();
            }

        } else {
            Izraz izraz = (Izraz)getChild(1);
            return izraz.parse();
        }
        return null;
    }

    public String parseAddress() {
        if(children.size() == 1) {
            String token = getChildAsUniformniZnak(0).getToken();
            String znak = getChildAsUniformniZnak(0).getGrupiraniZnakovi();

            if(token.equals("IDN")) {
                //<primarni_izraz> ::= IDN
                return GeneratorKoda.getVarReference(znak, TablicaZnakova.getTrenutniDjelokrug());
            } else if(token.equals("NIZ_ZNAKOVA")) {
                ////<primarni_izraz> ::= NIZ_ZNAKOVA
                znak = znak.substring(1, znak.length()-1);
                return GeneratorKoda.pushStringReference(znak);
            }
        }

        return null;
    }
}