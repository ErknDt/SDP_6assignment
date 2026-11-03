package kz.aitu.visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/** Finds files containing the given case-insensitive name fragment. */
public final class SearchVisitor implements FileSystemVisitor {
    private final String normalizedQuery;
    private final List<FileElement> matches = new ArrayList<>();
    private int visitedDirectoryCount;

    public SearchVisitor(String query) {
        Objects.requireNonNull(query, "query must not be null");
        if (query.isBlank()) {
            throw new IllegalArgumentException("query must not be blank");
        }
        normalizedQuery = query.toLowerCase(Locale.ROOT);
    }

    @Override
    public void visitFile(FileElement file) {
        if (file.getName().toLowerCase(Locale.ROOT).contains(normalizedQuery)) {
            matches.add(file);
        }
    }

    @Override
    public void visitDirectory(DirectoryElement directory) {
        visitedDirectoryCount++;
        // Only files are search results; accept() handles descendant traversal.
    }

    public List<FileElement> getMatches() {
        return List.copyOf(matches);
    }

    public int getVisitedDirectoryCount() {
        return visitedDirectoryCount;
    }
}
