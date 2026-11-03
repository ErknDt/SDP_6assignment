package kz.aitu.visitor;

import java.util.Objects;

/** Immutable leaf of the file-system tree. */
public final class FileElement implements FileSystemElement {
    private final String name;
    private final long sizeInBytes;

    public FileElement(String name, long sizeInBytes) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("file name must not be blank");
        }
        if (sizeInBytes < 0) {
            throw new IllegalArgumentException("file size must not be negative");
        }
        this.sizeInBytes = sizeInBytes;
    }

    @Override
    public String getName() {
        return name;
    }

    public long getSizeInBytes() {
        return sizeInBytes;
    }

    @Override
    public void accept(FileSystemVisitor visitor) {
        Objects.requireNonNull(visitor, "visitor must not be null").visitFile(this);
    }
}
