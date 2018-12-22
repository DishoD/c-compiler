import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PrototipFunkcije {
    public static final ArrayList<String> VOID_PARAMETER = new ArrayList<>();

    private String idn;
    private String returnType;
    private List<String> parameterTypes;

    public PrototipFunkcije(String idn, String returnType, List<String> pareterTypes) {
        this.idn = idn;
        this.returnType = returnType;
        this.parameterTypes = pareterTypes;
    }

    public String getIdn() {
        return idn;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<String> getPareterTypes() {
        return parameterTypes;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrototipFunkcije that = (PrototipFunkcije) o;
        return idn.equals(that.idn) &&
                returnType.equals(that.returnType) &&
                parameterTypes.equals(that.parameterTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idn, returnType, parameterTypes);
    }
}
