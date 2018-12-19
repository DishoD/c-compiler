import java.util.Objects;

public class Varijabla {
    private String idn;
    private String type;

    public Varijabla(String idn, String type) {
        this.idn = idn;
        this.type = type;
    }

    public String getIdn() {
        return idn;
    }

    public String getType() {
        return type;
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
