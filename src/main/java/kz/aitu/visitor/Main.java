package kz.aitu.visitor;

public final class Main {
    public static void main(String[] args) {
        DirectoryNode project = new DirectoryNode("project");
        DirectoryNode source = new DirectoryNode("src");
        DirectoryNode utilities = new DirectoryNode("utils");
        DirectoryNode assets = new DirectoryNode("assets");
        DirectoryNode empty = new DirectoryNode("empty");

        project.addChild(new FileNode("notes.txt", 120));
        project.addChild(source);
        source.addChild(new FileNode("Main.java", 2400));
        source.addChild(utilities);
        utilities.addChild(new FileNode("Helper.java", 800));
        project.addChild(assets);
        assets.addChild(new FileNode("logo.png", 1500));
        project.addChild(empty);

        FileSystemWalker walker = new FileSystemWalker();

        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        walker.walk(project, sizeVisitor);
        System.out.println("Total size: " + sizeVisitor.getTotalBytes() + " bytes");

        SearchVisitor searchVisitor = new SearchVisitor(".java", 800);
        walker.walk(project, searchVisitor);
        System.out.println("Java files (at least 800 bytes): " + searchVisitor.getMatches());

        StatisticsVisitor statisticsVisitor = new StatisticsVisitor();
        walker.walk(project, statisticsVisitor);
        System.out.println("Files: " + statisticsVisitor.getFileCount());
        System.out.println("Directories: " + statisticsVisitor.getDirectoryCount());
        System.out.println("Maximum depth: " + statisticsVisitor.getMaximumDepth());
    }
}
