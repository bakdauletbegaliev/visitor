# Visitor Pattern - Advanced File System

**Student:** Begaliev Bakdaulet  
**Group:** SE-2526  
**Course:** Software Design Patterns (ShP-2216)  
**Java:** 17 | **Build:** Maven  
**GitHub:** https://github.com/bakdauletbegaliev/visitor

## What does the project do?

It builds a small file system **in memory**. There are real-looking files and folders, but nothing is created on the computer's disk. Three independent visitors work with the same tree:

- `TotalSizeVisitor` adds all file sizes.
- `SearchVisitor` finds files by a case-insensitive name fragment **and** a minimum size (`>=`).
- `StatisticsVisitor` counts files, counts folders, and finds the deepest node.

## Commands

Run all automated tests from the project folder:

```bash
mvn test
```

Run the demo:

```bash
mvn -q compile exec:java
```

Requires **JDK 17+** and Maven 3.8+. Maven will download JUnit 5 on the first test run. The project builds with Java 17 language features even when a newer JDK is installed.

## Expected demo output

```text
Total size: 4820 bytes
Java files (at least 800 bytes): [src/Main.java, src/utils/Helper.java]
Files: 4
Directories: 5
Maximum depth: 3
```

## Example file tree

```text
project/                  depth 0
|-- notes.txt (120)       depth 1
|-- src/                 depth 1
|   |-- Main.java (2400) depth 2
|   `-- utils/           depth 2
|       `-- Helper.java (800) depth 3
|-- assets/              depth 1
|   `-- logo.png (1500)  depth 2
`-- empty/               depth 1
```

## How the Visitor pattern works

`FileSystemWalker.walk(root, visitor)` walks the tree in depth-first order. Each element calls its own `accept(visitor)` method. For a file, `accept` calls `visitor.visit(this)` and Java selects `visit(FileNode)`. For a directory, it selects `visit(DirectoryNode)`. **This is double dispatch:** first, Java chooses the correct `accept` implementation; then, Java chooses the matching overloaded `visit` method.

The walker alone controls traversal. It passes the current path and depth through `setContext`. Files return no children; folders return an unmodifiable snapshot of their children. There is no `instanceof`, cast, or reflection used to distinguish files from folders.

The walker calls `reset()` at the beginning of **every run**. The three visitors can be reused without accumulating old results. Search results are always sorted alphabetically. Paths are relative to the folder chosen as root and use `/`; when a file itself is the root, its own filename is returned. Depth begins at 0 for any chosen root.

## Design decisions and safety

- Filenames cannot be blank, `.`, `..`, or contain `/` or `\`.
- File sizes cannot be negative.
- Duplicate names in the same folder are rejected.
- A node can have only one parent; a parent can be assigned only through `addChild`.
- Self-parenting and ancestor cycles are rejected.
- There is no public remove/move operation. You can build the tree by adding new nodes, not by detaching nodes.
- `Math.addExact` reports overflow rather than returning a wrong total.

## Tests

JUnit 5 files: `VisitorDispatchTest`, `VisitorOperationsTest`, `TreeIntegrityTest`. They test both overloaded visit methods, nested totals, combined search conditions, root-relative sorted paths, statistics, empty folders, repeat execution, tree mutation protection and changed inputs.
