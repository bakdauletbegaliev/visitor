package kz.aitu.visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class VisitorDispatchTest {
    @Test
    void fileAndDirectoryUseTheirOwnVisitOverloads() {
        FileNode file = new FileNode("plan.txt", 50);
        DirectoryNode directory = new DirectoryNode("plans");
        RecordingVisitor visitor = new RecordingVisitor();

        file.accept(visitor);
        directory.accept(visitor);

        assertEquals(1, visitor.fileVisits);
        assertEquals(1, visitor.directoryVisits);
    }

    @Test
    void walkerVisitsEveryNodeExactlyOnce() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode nested = new DirectoryNode("nested");
        root.addChild(new FileNode("a.txt", 10));
        root.addChild(nested);
        nested.addChild(new FileNode("b.txt", 20));
        RecordingVisitor visitor = new RecordingVisitor();

        new FileSystemWalker().walk(root, visitor);

        assertEquals(2, visitor.fileVisits);
        assertEquals(2, visitor.directoryVisits);
    }

    private static class RecordingVisitor implements Visitor {
        int fileVisits;
        int directoryVisits;

        @Override
        public void visit(FileNode file) {
            fileVisits++;
        }

        @Override
        public void visit(DirectoryNode directory) {
            directoryVisits++;
        }
    }
}
