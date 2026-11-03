package kz.aitu.visitor;

/** An element that can be processed by a FileSystemVisitor. */
public interface FileSystemElement {
    String getName();
    void accept(FileSystemVisitor visitor);
}
