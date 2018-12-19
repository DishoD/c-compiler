/**
 * Uniformni znak kao list u stablu
 */
public class UniformniZnak extends Node{
    /**
     * naziv uniformnog znaka
     */
    private String token;

    /**
     * redak u kojem se nalazi uniformni znak
     */
    private int redak;

    /**
     * string iz ulaza
     */
    private String grupiraniZnakovi;

    public UniformniZnak(Node parent, String token, int redak, String grupiraniZnakovi) {
        super(parent);
        this.token = token;
        this.redak = redak;
        this.grupiraniZnakovi = grupiraniZnakovi;
        super.children = null;
    }

    public String getToken() {
        return token;
    }

    public int getRedak() {
        return redak;
    }

    public String getGrupiraniZnakovi() {
        return grupiraniZnakovi;
    }

    @Override
    public String toString() {
        return String.format("%s(%d,%s)", token, redak, grupiraniZnakovi);
    }
}
