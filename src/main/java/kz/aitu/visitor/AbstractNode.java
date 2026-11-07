package kz.aitu.visitor;

public abstract class AbstractNode implements Element {
    private final String name;
    private DirectoryNode parent;

    protected AbstractNode(String name) {
        if (name == null || name.isBlank() || name.contains("/") || name.contains("\\")
                || name.equals(".") || name.equals("..")) {
            throw new IllegalArgumentException("Invalid node name");
        }
        this.name = name;
    }

    @Override
    public final String getName() {
        return name;
    }

    public final DirectoryNode getParent() {
        return parent;
    }

    final void setParent(DirectoryNode parent) {
        this.parent = parent;
    }
}
