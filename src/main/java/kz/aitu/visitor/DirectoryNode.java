package kz.aitu.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class DirectoryNode extends AbstractNode {
    private final List<Element> children = new ArrayList<>();

    public DirectoryNode(String name) {
        super(name);
    }

    public void addChild(AbstractNode child) {
        Objects.requireNonNull(child, "Child cannot be null");
        if (child.getParent() != null) {
            throw new IllegalArgumentException("Child already has a parent");
        }
        for (DirectoryNode current = this; current != null; current = current.getParent()) {
            if (current == child) {
                throw new IllegalArgumentException("Cycle is not allowed");
            }
        }
        for (Element existing : children) {
            if (existing.getName().equals(child.getName())) {
                throw new IllegalArgumentException("Duplicate child name: " + child.getName());
            }
        }
        child.setParent(this);
        children.add(child);
    }

    @Override
    public List<Element> getChildren() {
        return List.copyOf(children);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
