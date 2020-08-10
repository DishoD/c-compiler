public class Naredba extends NezavrsniZnak {
    private Djelokrug djelokrugNaredbe;

    public Naredba(Node parent) {
        super(parent, "<naredba>");
    }

    @Override
    public void provjeri() {
        NezavrsniZnak znak = getChildAsNezavrsniZnak(0);
        if(znak instanceof SlozenaNaredba) {
            if(parent instanceof NaredbaPetlje || parent instanceof NaredbaGrananja) {
                znak.provjeri();
            } else {
                TablicaZnakova.stvoriNoviDjelokrug(Djelokrug.Oznaka.BLOK, null);
                djelokrugNaredbe = TablicaZnakova.getTrenutniDjelokrug();
                znak.provjeri();
                TablicaZnakova.vratiSe();
            }
        } else {
            znak.provjeri();
        }
    }

    public void setDjelokrugNaredbe(Djelokrug djelokrugNaredbe) {
        this.djelokrugNaredbe = djelokrugNaredbe;
    }

    public String parse() {
        StringBuilder sb = new StringBuilder();

        Djelokrug izvorni = TablicaZnakova.getTrenutniDjelokrug();
        if(djelokrugNaredbe != null) {
            TablicaZnakova.setTrenutniDjelokrug(djelokrugNaredbe);
            sb.append(GeneratorKoda.pushScope(djelokrugNaredbe));
        }

        sb.append(getChildAsNezavrsniZnak(0).parse());

        if(djelokrugNaredbe != null) {
            TablicaZnakova.setTrenutniDjelokrug(izvorni);
            sb.append(GeneratorKoda.popScope(djelokrugNaredbe));
        }
        return sb.toString();
    }
}