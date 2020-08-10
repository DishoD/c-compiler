import java.util.Objects;

public class Varijabla {
    protected String idn;
    protected String type;
    protected int brElem;
    protected boolean lizraz;
    protected Djelokrug pripadaDjelokrugu;

    public Varijabla(String idn, String type, int brElem, Djelokrug pripadaDjelokrugu) {
        this.idn = idn;
        this.type = type;
        this.brElem = brElem;
        lizraz = type.equals("char") || type.equals("int");
        this.pripadaDjelokrugu = pripadaDjelokrugu;
    }

    public Varijabla(Varijabla var) {
        this.idn = var.idn;
        this.type = var.type;
        this.brElem = var.brElem;
        lizraz = var.lizraz;
        this.pripadaDjelokrugu = var.pripadaDjelokrugu;
    }

    public int getBrElem() {
        return brElem;
    }

    public String getIdn() {
        return idn;
    }

    public String getType() {
        return type;
    }

    public boolean isLizraz() {
        return lizraz;
    }

    public Djelokrug getPripadaDjelokrugu() {
        return pripadaDjelokrugu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Varijabla varijabla = (Varijabla) o;
        return idn.equals(varijabla.idn) &&
                type.equals(varijabla.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idn, type);
    }
}
