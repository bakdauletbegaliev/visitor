package kz.aitu.visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TreeIntegrityTest {
    @Test
    void rejectsNegativeFileSize() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode("bad.txt", -1));
    }

    @Test
    void rejectsInvalidNames() {
        assertThrows(IllegalArgumentException.class, () -> new DirectoryNode(""));
        assertThrows(IllegalArgumentException.class, () -> new FileNode("a/b.txt", 1));
        assertThrows(IllegalArgumentException.class, () -> new DirectoryNode(".."));
    }

    @Test
    void rejectsDuplicateNamesWithinOneDirectory() {
        DirectoryNode root = new DirectoryNode("root");
        root.addChild(new FileNode("same.txt", 3));
        assertThrows(IllegalArgumentException.class, () -> root.addChild(new FileNode("same.txt", 4)));
        assertEquals(1, root.getChildren().size());
    }

    @Test
    void allowsSameNameInDifferentDirectories() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode left = new DirectoryNode("left");
        DirectoryNode right = new DirectoryNode("right");
        root.addChild(left);
        root.addChild(right);
        left.addChild(new FileNode("same.txt", 1));
        right.addChild(new FileNode("same.txt", 2));
        assertEquals(2, root.getChildren().size());
    }

    @Test
    void rejectsMultipleParentOwnership() {
        DirectoryNode a = new DirectoryNode("a");
        DirectoryNode b = new DirectoryNode("b");
        FileNode file = new FileNode("one.txt", 3);
        a.addChild(file);
        assertThrows(IllegalArgumentException.class, () -> b.addChild(file));
        assertEquals(a, file.getParent());
    }

    @Test
    void rejectsAddingDirectoryToItself() {
        DirectoryNode root = new DirectoryNode("root");
        assertThrows(IllegalArgumentException.class, () -> root.addChild(root));
    }

    @Test
    void rejectsAncestorCycle() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode one = new DirectoryNode("one");
        DirectoryNode two = new DirectoryNode("two");
        root.addChild(one);
        one.addChild(two);
        assertThrows(IllegalArgumentException.class, () -> two.addChild(root));
        assertTrue(two.getChildren().isEmpty());
    }

    @Test
    void childCollectionCannotBeModifiedFromOutside() {
        DirectoryNode root = new DirectoryNode("root");
        root.addChild(new FileNode("one.txt", 3));
        assertThrows(UnsupportedOperationException.class,
                () -> root.getChildren().add(new FileNode("two.txt", 5)));
        assertEquals(1, root.getChildren().size());
    }

    @Test
    void nullChildIsRejected() {
        DirectoryNode root = new DirectoryNode("root");
        assertThrows(NullPointerException.class, () -> root.addChild(null));
    }
}
