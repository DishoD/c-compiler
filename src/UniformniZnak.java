/**
 * Jedan redak u tablici uniformnih znakova.
 */
public class UniformniZnak {
    /**
     * naziv uniformnog znaka
     */
    public String token;

    /**
     * redak u kojem se nalazi uniformni znak
     */
    public int redak;

    /**
     * string iz ulaznog teksta koji je zadovoljio regularni izraz
     * za ovaj uniformni znak
     */
    public String grupiraniZnakovi;

    public UniformniZnak(String token, int redak, String grupiraniZnakovi) {
        this.token = token;
        this.redak = redak;
        this.grupiraniZnakovi = grupiraniZnakovi;
    }

    @Override
    public String toString() {
        return String.format("%s %d %s", token, redak, grupiraniZnakovi);
    }
}
