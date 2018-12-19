public class ImeTipa extends NezavrsniZnak {
    private String tip;

    public ImeTipa(Node parent) {
        super(parent, "<ime_tipa>");
    }

    @Override
    public void provjeri() {
        if(provjera == null){
            provjera = children.size() == 1 ? new V1() : new V2();
        }

        provjera.provjeri();
    }

    public String getTip() {
        return tip;
    }

    /**
     * <ime_tipa> ::= <specifikator_tipa>
     */
    private class V1 implements Provjerljiv {
        @Override
        public void provjeri() {
            SpecifikatorTipa sp = (SpecifikatorTipa)getChild(0);
            sp.provjeri();

            tip = sp.getTip();
        }
    }

    /**
     * <ime_tipa> ::= KR_CONST <specifikator_tipa>
     */
    private class V2 implements Provjerljiv {
        @Override
        public void provjeri() {
            SpecifikatorTipa sp = (SpecifikatorTipa)getChild(1);
            sp.provjeri();
            if(sp.getTip().equals("void")) greska();

            tip = "const(" + sp.getTip() + ")";
        }
    }
}