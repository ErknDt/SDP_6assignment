package kz.aitu.visitor;

/** Third visitor: demonstrates extension with no changes to element classes. */
public final class FileCountVisitor implements FileSystemVisitor {
    private int fileCount;
    private int directoryCount;

    @Override
    public void visitFile(FileElement file) {
        fileCount++;
    }

    @Override
    public void visitDirectory(DirectoryElement directory) {
        directoryCount++;
    }

    public int getFileCount() {
        return fileCount;
    }

    public int getDirectoryCount() {
        return directoryCount;
    }
}
