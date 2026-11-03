package kz.aitu.visitor;

/** One visit method for each concrete element type (double dispatch). */
public interface FileSystemVisitor {
    void visitFile(FileElement file);
    void visitDirectory(DirectoryElement directory);
}
