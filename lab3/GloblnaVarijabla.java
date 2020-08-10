public class GloblnaVarijabla extends Varijabla implements GlobalniParametar {
    private static int varCount = 0;

    private static String getNewLabel() {
        return "V" + varCount++;
    }

    private String labela;

    public GloblnaVarijabla(String idn, String type, int brElem, Djelokrug pripadaDjelokrugu) {
        super(idn, type, brElem, pripadaDjelokrugu);

        labela = getNewLabel();
    }


    @Override
    public String getLabela() {
        return labela;
    }
}
