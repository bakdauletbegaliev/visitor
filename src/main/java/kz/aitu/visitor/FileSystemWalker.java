package kz.aitu.visitor;

import java.util.Objects;

public final class FileSystemWalker {
    public void walk(Element root, Visitor visitor) {
        Objects.requireNonNull(root, "Root cannot be null");
        Objects.requireNonNull(visitor, "Visitor cannot be null");
        visitor.reset();
        walkNode(root, visitor, "", 0);
    }

    private void walkNode(Element node, Visitor visitor, String path, int depth) {
        visitor.setContext(path, depth);
        node.accept(visitor);
        for (Element child : node.getChildren()) {
            String childPath = path.isEmpty() ? child.getName() : path + "/" + child.getName();
            walkNode(child, visitor, childPath, depth + 1);
        }
    }
}
