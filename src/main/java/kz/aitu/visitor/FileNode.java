package kz.aitu.visitor;

import java.util.List;

public final class FileNode extends AbstractNode {
    private final long sizeBytes;

    public FileNode(String name, long sizeBytes) {
        super(name);
        if (sizeBytes < 0) {
            throw new IllegalArgumentException("File size cannot be negative");
        }
        this.sizeBytes = sizeBytes;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    @Override
    public List<Element> getChildren() {
        return List.of();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
