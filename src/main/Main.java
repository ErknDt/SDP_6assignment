package kz.aitu.visitor;

import java.util.List;

/** Runs different operations on the same immutable file-system tree. */
public final class Main {
    private Main() { }

    public static void main(String[] args) {
        DirectoryElement project = buildSampleTree();

        SizeCalculatorVisitor sizeVisitor = new SizeCalculatorVisitor();
        project.accept(sizeVisitor);

        SearchVisitor searchVisitor = new SearchVisitor(".java");
        project.accept(searchVisitor);

        FileCountVisitor countVisitor = new FileCountVisitor();
        project.accept(countVisitor);

        System.out.println("=== File System Visitor Pattern ===");
        System.out.println("Root directory: " + project.getName());
        System.out.println("Total size: " + sizeVisitor.getTotalSizeInBytes() + " bytes");
        System.out.println("Directories visited: " + sizeVisitor.getVisitedDirectoryCount());
        System.out.println();
        System.out.println("Search query: .java");
        System.out.println("Matching files:");
        for (FileElement file : searchVisitor.getMatches()) {
            System.out.println("- " + file.getName() + " (" + file.getSizeInBytes() + " bytes)");
        }
        System.out.println();
        System.out.println("Count visitor: " + countVisitor.getFileCount()
                + " files, " + countVisitor.getDirectoryCount() + " directories");
    }

    public static DirectoryElement buildSampleTree() {
        DirectoryElement tests = new DirectoryElement("tests", List.of(
                new FileElement("VisitorTest.java", 1500)
        ));
        DirectoryElement src = new DirectoryElement("src", List.of(
                new FileElement("Main.java", 1200),
                new FileElement("Visitor.java", 800),
                tests
        ));
        DirectoryElement docs = new DirectoryElement("docs", List.of(
                new FileElement("design.pdf", 2500),
                new FileElement("notes.txt", 200)
        ));
        return new DirectoryElement("project", List.of(
                src,
                new FileElement("README.md", 500),
                docs
        ));
    }
}
