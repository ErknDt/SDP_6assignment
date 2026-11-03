package kz.aitu.visitor;

import java.util.ArrayList;
import java.util.List;

/** Dependency-free test harness; throws AssertionError if a test fails. */
public final class VisitorPatternTest {
    private static int passed;

    private VisitorPatternTest() { }

    public static void main(String[] args) {
        testTotalSizeInNestedTree();
        testCaseInsensitiveSearch();
        testSearchWithNoMatches();
        testEmptyDirectory();
        testThirdVisitorExtension();
        testVisitorsIndependent();
        testChildListIsDefensiveCopy();
        testInvalidInputs();
        testOverflowIsRejected();
        System.out.println("PASS: " + passed + " tests");
    }

    private static void testTotalSizeInNestedTree() {
        SizeCalculatorVisitor visitor = new SizeCalculatorVisitor();
        Main.buildSampleTree().accept(visitor);
        equals(6700L, visitor.getTotalSizeInBytes(), "nested total size");
        equals(4, visitor.getVisitedDirectoryCount(), "directory visits");
        pass();
    }

    private static void testCaseInsensitiveSearch() {
        SearchVisitor visitor = new SearchVisitor(".JAVA");
        Main.buildSampleTree().accept(visitor);
        List<String> names = visitor.getMatches().stream().map(FileElement::getName).toList();
        equals(List.of("Main.java", "Visitor.java", "VisitorTest.java"), names,
                "case-insensitive search results");
        equals(4, visitor.getVisitedDirectoryCount(), "search directory visits");
        pass();
    }

    private static void testSearchWithNoMatches() {
        SearchVisitor visitor = new SearchVisitor(".png");
        Main.buildSampleTree().accept(visitor);
        equals(0, visitor.getMatches().size(), "no matches");
        pass();
    }

    private static void testEmptyDirectory() {
        DirectoryElement empty = new DirectoryElement("empty", List.of());
        SizeCalculatorVisitor size = new SizeCalculatorVisitor();
        FileCountVisitor count = new FileCountVisitor();
        empty.accept(size);
        empty.accept(count);
        equals(0L, size.getTotalSizeInBytes(), "empty directory size");
        equals(0, count.getFileCount(), "empty directory file count");
        equals(1, count.getDirectoryCount(), "empty directory count");
        pass();
    }

    private static void testThirdVisitorExtension() {
        FileCountVisitor visitor = new FileCountVisitor();
        Main.buildSampleTree().accept(visitor);
        equals(6, visitor.getFileCount(), "number of files");
        equals(4, visitor.getDirectoryCount(), "number of directories");
        pass();
    }

    private static void testVisitorsIndependent() {
        DirectoryElement tree = Main.buildSampleTree();
        SizeCalculatorVisitor size = new SizeCalculatorVisitor();
        SearchVisitor search = new SearchVisitor("readme");
        tree.accept(size);
        tree.accept(search);
        equals(6700L, size.getTotalSizeInBytes(), "size after separate visits");
        equals(List.of("README.md"),
                search.getMatches().stream().map(FileElement::getName).toList(),
                "search after separate visits");
        pass();
    }

    private static void testChildListIsDefensiveCopy() {
        List<FileSystemElement> source = new ArrayList<>();
        source.add(new FileElement("first.txt", 9));
        DirectoryElement directory = new DirectoryElement("safe", source);
        source.add(new FileElement("second.txt", 7));
        equals(1, directory.getChildren().size(), "defensive constructor copy");
        expectThrows(UnsupportedOperationException.class,
                () -> directory.getChildren().add(new FileElement("third.txt", 4)));
        pass();
    }

    private static void testInvalidInputs() {
        expectThrows(IllegalArgumentException.class, () -> new FileElement(" ", 1));
        expectThrows(IllegalArgumentException.class, () -> new FileElement("bad", -1));
        expectThrows(IllegalArgumentException.class,
                () -> new DirectoryElement(" ", List.of()));
        expectThrows(IllegalArgumentException.class, () -> new SearchVisitor(" "));
        expectThrows(NullPointerException.class, () -> new FileElement(null, 0));
        expectThrows(NullPointerException.class, () -> new DirectoryElement("x", null));
        pass();
    }

    private static void testOverflowIsRejected() {
        DirectoryElement tree = new DirectoryElement("huge", List.of(
                new FileElement("one", Long.MAX_VALUE),
                new FileElement("two", 1)
        ));
        expectThrows(ArithmeticException.class,
                () -> tree.accept(new SizeCalculatorVisitor()));
        pass();
    }

    private static void equals(Object expected, Object actual, String description) {
        if (!expected.equals(actual)) {
            throw new AssertionError(description + ": expected " + expected + ", got " + actual);
        }
    }

    private static void expectThrows(Class<? extends Throwable> type, Runnable task) {
        try {
            task.run();
        } catch (Throwable exception) {
            if (type.isInstance(exception)) {
                return;
            }
            throw new AssertionError("Expected " + type.getSimpleName()
                    + " but got " + exception.getClass().getSimpleName(), exception);
        }
        throw new AssertionError("Expected " + type.getSimpleName() + " to be thrown");
    }

    private static void pass() {
        passed++;
    }
}
