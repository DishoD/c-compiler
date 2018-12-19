import java.util.ArrayList;
import java.util.List;

/**
 * Predstavlja cvor u grafu.
 */
public class Node {
    protected Node parent;
    protected List<Node> children = new ArrayList<>();

    public Node(Node parent) {
        this.parent = parent;
    }

    public void addChild(Node node) {
        children.add(node);
    }

    protected Node getChild(int index) {
        return children.get(index);
    }
}
