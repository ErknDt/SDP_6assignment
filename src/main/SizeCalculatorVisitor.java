package kz.aitu.visitor;

/** Calculates the total size of all file leaves, including nested directories. */
public final class SizeCalculatorVisitor implements FileSystemVisitor {
    private long totalSizeInBytes;
    private int visitedDirectoryCount;

    @Override
    public void visitFile(FileElement file) {
        totalSizeInBytes = Math.addExact(totalSizeInBytes, file.getSizeInBytes());
    }

    @Override
    public void visitDirectory(DirectoryElement directory) {
        visitedDirectoryCount++;
        // Directory has no intrinsic size; accept() traverses its children.
    }

    public long getTotalSizeInBytes() {
        return totalSizeInBytes;
    }

    public int getVisitedDirectoryCount() {
        return visitedDirectoryCount;
    }
}
