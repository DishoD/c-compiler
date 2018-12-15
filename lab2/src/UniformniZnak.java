import java.util.Objects;

public class UniformniZnak {
    private String znak;
    private int redak;
    private String leksickaJedinka;

    public UniformniZnak(String uniformniZnak, int redak, String leksickaJedinka) {
        this.znak = uniformniZnak;
        this.redak = redak;
        this.leksickaJedinka = leksickaJedinka;
    }

    public String getZnak() {
        return znak;
    }

    public int getRedak() {
        return redak;
    }

    public String getLeksickaJedinka() {
        return leksickaJedinka;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UniformniZnak that = (UniformniZnak) o;
        return redak == that.redak &&
                Objects.equals(znak, that.znak) &&
                Objects.equals(leksickaJedinka, that.leksickaJedinka);
    }

    @Override
    public int hashCode() {
        return Objects.hash(znak, redak, leksickaJedinka);
    }
}
