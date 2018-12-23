import java.util.*;

/**
 * Pomocni razred za rukovanje tipovima.
 * Sadrzi pomocne funkcije vezane za tipove, pretvorbu, provjere, itd.
 */
public final class TipoviUtility {
    private static Map<String, List<String>> castMap = new HashMap<String, List<String>>();

    static {
        createCastMap (getStartingMap());
    }


    /**
     * Provjerava moze li se tip1 implicitno castati u tip2.
     */
    public static boolean castableImplicit (String type1, String type2) {
        if(type1.equals(type2)) return true;

        if (castMap.containsKey(type1))
            if (castMap.get(type1).contains(type2))
                return true;

        return false;
    }

    /**
     * Iz pocetnih castova koje smo zadali u startingMap trazi castove
     * koji iz njih proizlaze te iz njih stvara castMap.
     */
    private static void createCastMap (Map<String, List<String>> startingMap) {
        List<String> castableTo = new ArrayList<>();
        Deque<String> dequeue = new LinkedList<>();

        for (String type : startingMap.keySet()) {
            dequeue.push(type);
            castableTo.add(type);

            while (!dequeue.isEmpty()) {
                String currentType = dequeue.pop();

                if (startingMap.containsKey(currentType)) {
                    for (String castType : startingMap.get(currentType)) {
                        if (!castableTo.contains(castType)) {
                            dequeue.push(castType);
                            castableTo.add(castType);
                        }
                    }
                }
            }

            castMap.put(type, castableTo);
            castableTo = new ArrayList<>();
        }
    }

    /**
     * Metoda u kojoj zadajemo pocetne castove.
     */
    private static Map<String, List<String>> getStartingMap () {
        Map<String, List<String>> startingMap = new HashMap<>();
        List<String> listToAdd = new ArrayList<>();

        listToAdd.add("char");
        startingMap.put("const(char)", listToAdd);

        listToAdd = new ArrayList<>();
        listToAdd.add("int");
        startingMap.put("const(int)", listToAdd);

        listToAdd = new ArrayList<>();
        listToAdd.add("const(char)");
        listToAdd.add("int");
        startingMap.put("char", listToAdd);

        listToAdd = new ArrayList<>();
        listToAdd.add("const(int)");
        startingMap.put("int", listToAdd);

        listToAdd = new ArrayList<>();
        listToAdd.add("niz(const(char))");
        startingMap.put("niz(char)", listToAdd);

        listToAdd = new ArrayList<>();
        listToAdd.add("niz(const(int))");
        startingMap.put("niz(int)", listToAdd);

        return startingMap;
    }

    public static boolean isConst(String tip) {
        return tip.startsWith("const(");
    }

    public static boolean isArray(String tip) {
        return tip.startsWith("niz(");
    }

    public static boolean isConstArray(String tip) {
        return tip.startsWith("niz(const(");
    }

    public  static boolean isT(String tip) {
        return  tip.equals("int") || tip.equals("char");
    }

    public  static boolean isConstT(String tip) {
        return  tip.equals("const(int)") || tip.equals("const(char)");
    }

    public static String getT(String tip) {
        if(tip.startsWith("niz(")) {
            tip = tip.substring(4, tip.length() - 1);
        }
        if(tip.startsWith("const(")) {
            tip = tip.substring(6, tip.length() - 1);
        }

        return  tip;
    }

    public static String toConst(String tip) {
        return "const(" + tip + ")";
    }

    public static String toArray(String tip) {
        return "niz(" + tip + ")";
    }

    public static boolean castableExplicit(String tip1, String tip2) {
        if(TipoviUtility.isArray(tip1) || TipoviUtility.isArray(tip2)) return false;
        if(castableImplicit(tip1, tip2)) return true;
        return castableImplicit(tip1, "int") && castableImplicit(tip2, "char");
    }
}
