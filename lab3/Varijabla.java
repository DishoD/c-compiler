import java.util.Objects;

public class Varijabla {
    private String idn;
    private String type;
    private int brElem;
    private boolean lizraz;

    public Varijabla(String idn, String type, int brElem) {
        this.idn = idn;
        this.type = type;
        this.brElem = brElem;
        lizraz = type.equals("char") || type.equals("int") ? true : false;
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
