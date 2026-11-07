package kz.aitu.visitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class VisitorOperationsTest {
    private final FileSystemWalker walker = new FileSystemWalker();

    private DirectoryNode makeTree() {
        DirectoryNode root = new DirectoryNode("project");
        DirectoryNode source = new DirectoryNode("src");
        DirectoryNode utilities = new DirectoryNode("utils");
        DirectoryNode images = new DirectoryNode("images");
        root.addChild(new FileNode("readme.md", 100));
        root.addChild(source);
        source.addChild(new FileNode("Zebra.java", 800));
        source.addChild(utilities);
        utilities.addChild(new FileNode("Alpha.java", 1200));
        root.addChild(images);
        images.addChild(new FileNode("photo.jpg", 2000));
        return root;
    }

    @Test
    void sizeIncludesAllNestingLevels() {
        TotalSizeVisitor visitor = new TotalSizeVisitor();
        walker.walk(makeTree(), visitor);
        assertEquals(4100L, visitor.getTotalBytes());
    }

    @Test
    void fileRootIsCountedOnce() {
        TotalSizeVisitor visitor = new TotalSizeVisitor();
        walker.walk(new FileNode("only.txt", 25), visitor);
        assertEquals(25L, visitor.getTotalBytes());
    }

    @Test
    void searchCombinesNameAndInclusiveMinimumSize() {
        SearchVisitor visitor = new SearchVisitor(".java", 800);
        walker.walk(makeTree(), visitor);
        assertEquals(List.of("src/Zebra.java", "src/utils/Alpha.java"), visitor.getMatches());
    }

    @Test
    void searchReturnsSortedPathsRegardlessOfInsertionOrder() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode second = new DirectoryNode("zeta");
        DirectoryNode first = new DirectoryNode("alpha");
        root.addChild(second);
        second.addChild(new FileNode("data.txt", 10));
        root.addChild(first);
        first.addChild(new FileNode("data.txt", 10));
        SearchVisitor visitor = new SearchVisitor("data", 10);
        walker.walk(root, visitor);
        assertEquals(List.of("alpha/data.txt", "zeta/data.txt"), visitor.getMatches());
    }

    @Test
    void searchFromFileRootReturnsItsName() {
        SearchVisitor visitor = new SearchVisitor("DATA", 5);
        walker.walk(new FileNode("data.csv", 5), visitor);
        assertEquals(List.of("data.csv"), visitor.getMatches());
    }

    @Test
    void statisticsCountRootAndMaximumDepth() {
        StatisticsVisitor visitor = new StatisticsVisitor();
        walker.walk(makeTree(), visitor);
        assertEquals(4, visitor.getFileCount());
        assertEquals(4, visitor.getDirectoryCount());
        assertEquals(3, visitor.getMaximumDepth());
    }

    @Test
    void statisticsFromFileRootHaveDepthZero() {
        StatisticsVisitor visitor = new StatisticsVisitor();
        walker.walk(new FileNode("one.txt", 10), visitor);
        assertEquals(1, visitor.getFileCount());
        assertEquals(0, visitor.getDirectoryCount());
        assertEquals(0, visitor.getMaximumDepth());
    }

    @Test
    void emptyDirectoryAndNoMatchesWork() {
        DirectoryNode root = new DirectoryNode("empty");
        TotalSizeVisitor total = new TotalSizeVisitor();
        SearchVisitor search = new SearchVisitor(".java", 0);
        StatisticsVisitor stats = new StatisticsVisitor();
        walker.walk(root, total);
        walker.walk(root, search);
        walker.walk(root, stats);
        assertEquals(0L, total.getTotalBytes());
        assertTrue(search.getMatches().isEmpty());
        assertEquals(0, stats.getFileCount());
        assertEquals(1, stats.getDirectoryCount());
        assertEquals(0, stats.getMaximumDepth());
    }

    @Test
    void sameVisitorCanRunTwiceWithoutAccumulation() {
        DirectoryNode tree = makeTree();
        TotalSizeVisitor size = new TotalSizeVisitor();
        SearchVisitor search = new SearchVisitor(".java", 0);
        StatisticsVisitor stats = new StatisticsVisitor();
        for (int run = 0; run < 2; run++) {
            walker.walk(tree, size);
            walker.walk(tree, search);
            walker.walk(tree, stats);
            assertEquals(4100L, size.getTotalBytes());
            assertEquals(2, search.getMatches().size());
            assertEquals(4, stats.getFileCount());
        }
    }

    @Test
    void sameVisitorCanRunOnDifferentRoots() {
        TotalSizeVisitor size = new TotalSizeVisitor();
        walker.walk(makeTree(), size);
        walker.walk(new FileNode("small.txt", 7), size);
        assertEquals(7L, size.getTotalBytes());
    }

    @Test
    void newChildChangesAllResults() {
        DirectoryNode root = new DirectoryNode("root");
        TotalSizeVisitor size = new TotalSizeVisitor();
        StatisticsVisitor stats = new StatisticsVisitor();
        walker.walk(root, size);
        root.addChild(new FileNode("new.txt", 60));
        walker.walk(root, size);
        walker.walk(root, stats);
        assertEquals(60L, size.getTotalBytes());
        assertEquals(1, stats.getFileCount());
        assertEquals(1, stats.getMaximumDepth());
    }
}
