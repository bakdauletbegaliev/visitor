package kz.aitu.visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class SearchVisitor implements Visitor {
    private final String nameContains;
    private final long minimumBytes;
    private final List<String> matches = new ArrayList<>();
    private String currentPath;

    public SearchVisitor(String nameContains, long minimumBytes) {
        if (minimumBytes < 0) {
            throw new IllegalArgumentException("Minimum size cannot be negative");
        }
        this.nameContains = Objects.requireNonNull(nameContains, "Search text cannot be null")
                .toLowerCase(Locale.ROOT);
        this.minimumBytes = minimumBytes;
    }

    @Override
    public void setContext(String path, int depth) {
        currentPath = path;
    }

    @Override
    public void visit(FileNode file) {
        boolean nameMatches = file.getName().toLowerCase(Locale.ROOT).contains(nameContains);
        if (nameMatches && file.getSizeBytes() >= minimumBytes) {
            matches.add(currentPath.isEmpty() ? file.getName() : currentPath);
        }
    }

    @Override
    public void visit(DirectoryNode directory) {
    }

    @Override
    public void reset() {
        matches.clear();
        currentPath = "";
    }

    public List<String> getMatches() {
        List<String> result = new ArrayList<>(matches);
        Collections.sort(result);
        return result;
    }
}
