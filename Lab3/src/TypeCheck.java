import java.util.*;

public class TypeCheck {
    static private Map<String, List<String>> castMap = new HashMap<String, List<String>>();

    static {
        createCastMap (getStartingMap());
    }

    private boolean castableImplicit (String type1, String type2) {

        if (castMap.get(type1).contains(type2))
            return true;

        return false;
    }

    static private void createCastMap (Map<String, List<String>> startingMap) {
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

    static private Map<String, List<String>> getStartingMap () {
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

    public static void main (String[] args) {
        createCastMap (getStartingMap());
    }
}

