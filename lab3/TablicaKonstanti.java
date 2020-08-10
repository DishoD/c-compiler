import java.util.HashMap;
import java.util.Map;

public final class TablicaKonstanti {
    private static int constCount = 0;
    private static String getNewLabel() {
        return "K" + constCount++;
    }

    private static Map<Integer, String> konstanteInt = new HashMap<>();
    private static Map<String, String> konstanteString = new HashMap<>();

    private TablicaKonstanti() {}

    public static String getLabelaKonstante(int k) {
        String ret = konstanteInt.get(k);

        if(ret == null) {
            ret = getNewLabel();
            konstanteInt.put(k, ret);
        }

        return ret;
    }

    public static String getLabelaKonstante(String k) {
        String ret = konstanteString.get(k);

        if(ret == null) {
            ret = getNewLabel();
            konstanteString.put(k, ret);
        }

        return ret;
    }

    public static Map<Integer, String> getKonstanteInt() {
        return konstanteInt;
    }

    public static Map<String, String> getKonstanteString() {
        return konstanteString;
    }
}
