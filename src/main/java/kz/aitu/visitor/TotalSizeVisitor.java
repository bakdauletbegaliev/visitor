package kz.aitu.visitor;

public final class TotalSizeVisitor implements Visitor {
    private long totalBytes;

    @Override
    public void visit(FileNode file) {
        totalBytes = Math.addExact(totalBytes, file.getSizeBytes());
    }

    @Override
    public void visit(DirectoryNode directory) {
    }

    @Override
    public void reset() {
        totalBytes = 0;
    }

    public long getTotalBytes() {
        return totalBytes;
    }
}
