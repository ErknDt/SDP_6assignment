package kz.aitu.visitor;

import java.util.List;
import java.util.Objects;

/** Immutable composite: visits itself and then recursively visits its children. */
public final class DirectoryElement implements FileSystemElement {
    private final String name;
    private final List<FileSystemElement> children;

    public DirectoryElement(String name, List<FileSystemElement> children) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("directory name must not be blank");
        }
        this.children = List.copyOf(Objects.requireNonNull(children, "children must not be null"));
    }

    @Override
    public String getName() {
        return name;
    }

    /** Read-only view; visitors never receive a mutable child collection. */
    public List<FileSystemElement> getChildren() {
        return children;
    }

    @Override
    public void accept(FileSystemVisitor visitor) {
        Objects.requireNonNull(visitor, "visitor must not be null").visitDirectory(this);
        for (FileSystemElement child : children) {
            child.accept(visitor);
        }
    }
}
