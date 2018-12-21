public class PostfiksIzraz extends NezavrsniZnak {

    private String tip;
    private boolean lizraz;
    private boolean isFunction;
    private PrototipFunkcije prototipFunkcije;

    public PostfiksIzraz(Node parent) {
        super(parent, "<postfiks_izraz>");
    }

    public String getTip() {
        return tip;
    }

    public boolean isFunction() {
        return isFunction;
    }

    public PrototipFunkcije getPrototipFunkcije() {
        return prototipFunkcije;
    }

    public boolean isLizraz() {
        return lizraz;
    }

    @Override
    public void provjeri() {
        if(provjera == null) {
            if(children.size() == 1) {
                provjera = new V1();
            } else if(children.size() == 2) {
                provjera = new V5();
            } else if(children.size() == 3) {
                provjera = new V3();
            } else {
                String s = getChildAsUniformniZnak(1).getToken();
                if(s.equals("L_UGL_ZAGRADA")) {
                    provjera = new V2();
                } else {
                    provjera = new V4();
                }
            }
        }

        provjera.provjeri();
    }

    /**
     * <postfiks_izraz> ::= <primarni_izraz>
     */
    private class V1 implements Provjerljiv {

        @Override
        public void provjeri() {
            PrimarniIzraz pi = (PrimarniIzraz)getChild(0);
            pi.provjeri();

            tip = pi.getTip();
            isFunction = pi.isFunction();
            lizraz = pi.isLizraz();
            prototipFunkcije = pi.getPrototipFunkcije();
        }
    }


    /**
     * <postfiks_izraz> ::= <postfiks_izraz> L_UGL_ZAGRADA <izraz> D_UGL_ZAGRADA
     */
    private class V2 implements Provjerljiv {

        @Override
        public void provjeri() {
            PostfiksIzraz pi = (PostfiksIzraz)getChild(0);
            pi.provjeri();
            if(pi.getTip() == null || !TipoviUtility.isArray(pi.getTip())) greska();
            Izraz izraz = (Izraz) getChild(2);
            izraz.provjeri();
            if(!TipoviUtility.castableImplicit(izraz.getTip(), "int")) greska();

            String X = pi.getTip().substring(4, pi.getTip().length()-1);

            tip = pi.getTip();
            isFunction = pi.isFunction();
            lizraz = !TipoviUtility.isConst(X);
            prototipFunkcije = pi.getPrototipFunkcije();
        }
    }

    /**
     * <postfiks_izraz> ::= <postfiks_izraz> L_ZAGRADA D_ZAGRADA
     */
    private class V3 implements Provjerljiv {

        @Override
        public void provjeri() {
            PostfiksIzraz pi = (PostfiksIzraz)getChild(0);
            if(!pi.isFunction()) greska();
            PrototipFunkcije f = pi.getPrototipFunkcije();
            if(f.getPareterTypes() != PrototipFunkcije.VOID_PARAMETER) greska();

            tip = pi.getTip();
            isFunction = pi.isFunction();
            lizraz = false;
            prototipFunkcije = pi.getPrototipFunkcije();
        }
    }

    /**
     * <postfiks_izraz> ::= <postfiks_izraz> L_ZAGRADA <lista_argumenata> D_ZAGRADA
     */
    private class V4 implements Provjerljiv {

        @Override
        public void provjeri() {
            PostfiksIzraz pi = (PostfiksIzraz)getChild(0);
            ListaArgumenata la = (ListaArgumenata)getChild(2);
            pi.provjeri();
            la.provjeri();

            if(!pi.isFunction()) greska();
            PrototipFunkcije f = pi.getPrototipFunkcije();
            if(f.getPareterTypes().size() != la.getTipovi().size()) greska();
            for(int i = 0; i < f.getPareterTypes().size(); ++i) {
                String arg = la.getTipovi().get(i);
                String param = f.getPareterTypes().get(i);
                if(!TipoviUtility.castableImplicit(arg, param)) greska();
            }

            tip = pi.getTip();
            isFunction = pi.isFunction();
            lizraz = false;
            prototipFunkcije = pi.getPrototipFunkcije();
        }
    }

    /**
     * <postfiks_izraz> ::= <postfiks_izraz> (OP_INC | OP_DEC)
     */
    private class V5 implements Provjerljiv {

        @Override
        public void provjeri() {
            PostfiksIzraz pi = (PostfiksIzraz)getChild(0);
            pi.provjeri();
            if(!pi.isLizraz() || !TipoviUtility.castableImplicit(pi.getTip(), "int")) greska();

            tip = pi.getTip();
        }
    }
}