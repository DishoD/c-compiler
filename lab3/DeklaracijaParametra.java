public class DeklaracijaParametra extends NezavrsniZnak {
    private String tip;
    private  String ime;

    public DeklaracijaParametra(Node parent) {
        super(parent, "<deklaracija_parametra>");
    }

    @Override
    public void provjeri() {
        if(provjera == null) {
            provjera = children.size() == 2 ? new V1() : new V2();
        }

        provjera.provjeri();
    }

    public String getTip() {
        return tip;
    }

    public String getIme() {
        return ime;
    }

    /**
     * <deklaracija_parametra> ::= <ime_tipa> IDN
     */
    private class V1 implements Provjerljiv {
        @Override
        public void provjeri() {
            ImeTipa imeTipa = (ImeTipa)getChild(0);
            if(imeTipa.getTip().equals("void")) greska();

            tip = imeTipa.getTip();
            ime = ((UniformniZnak)getChild(1)).getGrupiraniZnakovi();
        }
    }

    /**
     * <deklaracija_parametra> ::= <ime_tipa> IDN L_UGL_ZAGRADA D_UGL_ZAGRADA
     */
    private class V2 implements Provjerljiv {
        @Override
        public void provjeri() {
            ImeTipa imeTipa = (ImeTipa)getChild(0);
            if(imeTipa.getTip().equals("void")) greska();

            tip = TipoviUtility.toArray(imeTipa.getTip());
            ime = ((UniformniZnak)getChild(1)).getGrupiraniZnakovi();
        }
    }
}
