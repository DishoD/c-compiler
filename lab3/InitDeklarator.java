public class InitDeklarator extends NezavrsniZnak {
    private String ntip;

    public InitDeklarator(Node parent) {
        super(parent, "<init_deklarator>");
    }

    public String getNtip() {
        return ntip;
    }

    public void setNtip(String ntip) {
        this.ntip = ntip;
    }

    @Override
    public void provjeri() {
        if(provjera == null) {
            provjera = children.size() == 1 ? new V1() : new V2();
        }

        provjera.provjeri();
    }

    @Override
    public String parse() {
        //<init_deklarator> ::= <izravni_deklarator>
        if(children.size() == 1) return "";

        //<init_deklarator> ::= <izravni_deklarator> OP_PRIDRUZI <inicijalizator>
        IzravniDeklarator id = (IzravniDeklarator)getChild(0);
        Inicijalizator init = (Inicijalizator)getChild(2);
        StringBuilder sb = new StringBuilder();

        if(init.isCharArray()) {
            sb.append(init.parse())
                    .append(id.parse())
                    .append(GeneratorKoda.pushConstant(init.getBrElem()))
                    .append(GeneratorKoda.copyArray());

            return sb.toString();
        }


        if(init.getBrElem() == 1) {
            String t = TipoviUtility.getT(init.getTip());
            if(t.equals("int") || t.equals("char")) {
                sb.append(id.parse())
                        .append(init.parse())
                        .append(GeneratorKoda.varAssign());
                return sb.toString();
            }
        }

        sb.append(init.parse())
                .append(GeneratorKoda.pushRegister("R7"))
                .append(id.parse())
                .append(GeneratorKoda.pushConstant(init.getBrElem()))
                .append(GeneratorKoda.copyArray())
                .append(GeneratorKoda.freeMemoryOnStack(init.getBrElem() * 4));

        return sb.toString();
    }

    /**
     * <init_deklarator> ::= <izravni_deklarator>
     */
    private class V1 implements Provjerljiv {

        @Override
        public void provjeri() {
            IzravniDeklarator id = (IzravniDeklarator)getChild(0);
            id.setNtip(ntip);
            id.provjeri();

            if(!id.isFunction()){
                String tip = id.getTip();
                if(TipoviUtility.isConst(tip) || TipoviUtility.isConstArray(tip)) greska();
            }
        }
    }

    /**
     * <init_deklarator> ::= <izravni_deklarator> OP_PRIDRUZI <inicijalizator>
     */
    private class V2 implements Provjerljiv {

        @Override
        public void provjeri() {
            IzravniDeklarator id = (IzravniDeklarator)getChild(0);
            id.setNtip(ntip);
            id.provjeri();

            Inicijalizator init = (Inicijalizator)getChild(2);
            init.provjeri();
            if(TipoviUtility.isT(id.getTip()) || TipoviUtility.isConstT(id.getTip())) {
                if(!TipoviUtility.castableImplicit(init.getTip(), TipoviUtility.getT(id.getTip()))) greska();
            } else if(TipoviUtility.isConstArray(id.getTip()) || TipoviUtility.isArray(id.getTip())) {
                if(init.getBrElem() <= 0) greska();
                if(init.getBrElem() > id.getBrElem()) greska();
                String T = TipoviUtility.getT(id.getTip());
                for(String U : init.getTipovi()) {
                    if(!TipoviUtility.castableImplicit(U, T)) greska();
                }
            } else {
                greska();
            }
        }
    }
}
