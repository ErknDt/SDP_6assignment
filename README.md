# Assignment 6 — Visitor Pattern

**Course:** Software Design Patterns (ShP-2216)  
**Language:** Java 17  
**Topic:** File System Visitor (Option A)

## Overview

This project demonstrates the **Visitor** behavioral design pattern using an in-memory file-system tree. The tree contains files and directories. Different operations are performed on the same structure without adding operation-specific logic to the element classes.

The application does **not** access or modify the computer's real file system.

## Implemented Visitors

| Visitor | Responsibility |
| --- | --- |
| `SizeCalculatorVisitor` | Calculates the total size of files in the tree, including nested files. |
| `SearchVisitor` | Finds files whose names contain a given text, ignoring case. |
| `FileCountVisitor` | Counts files and directories in the tree. |

The third visitor demonstrates extensibility: a new operation can be implemented without changing `FileElement` or `DirectoryElement`.

## Project Structure

```text
SDP_Assignment6_Visitor_Pattern/
├── src/
│   ├── main/
│   │   ├── FileSystemElement.java
│   │   ├── FileElement.java
│   │   ├── DirectoryElement.java
│   │   ├── FileSystemVisitor.java
│   │   ├── SizeCalculatorVisitor.java
│   │   ├── SearchVisitor.java
│   │   ├── FileCountVisitor.java
│   │   └── Main.java
│   └── test/
│       └── VisitorPatternTest.java
├── .gitignore
└── README.md
```

The files declare the Java package `kz.aitu.visitor`. Their direct placement under `src/main` and `src/test` is supported by the manual PowerShell compilation command below.

`FileSystemElement` defines `accept(visitor)`. `FileElement` and `DirectoryElement` implement this interface, while `FileSystemVisitor` defines the operations supported by visitors.

## How the Visitor Pattern Works

1. The client creates a visitor and passes it to an element using `accept()`.
2. The concrete element calls the matching method on the visitor, such as `visitFile(this)` or `visitDirectory(this)`.
3. `DirectoryElement` also passes the visitor to its children, allowing nested elements to be processed.

For example:

```java
SizeCalculatorVisitor visitor = new SizeCalculatorVisitor();
project.accept(visitor);
long totalSize = visitor.getTotalSizeInBytes();
```

The call from `accept()` to the correct `visit...()` method demonstrates **double dispatch**. The implementation does not use `instanceof` checks to distinguish element types.

## Requirements

- JDK 17 or newer (the project can be compiled for Java 17 with `--release 17`).
- IntelliJ IDEA or a terminal with `javac` and `java` available.
- No external libraries, Maven, or Gradle are required.

## Run in IntelliJ IDEA

1. Open the project folder.
2. Select a configured Project SDK (JDK 17 or newer).
3. Mark `src/main` as **Sources Root** and `src/test` as **Test Sources Root**, if IntelliJ has not done so automatically.
4. Open `src/main/Main.java` and run `Main.main()`.
5. To run the tests, open `src/test/VisitorPatternTest.java` and run its `main()`.

## Run from Windows PowerShell

Run the following commands from the project root:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
$files = @(Get-ChildItem -Path src/main,src/test -Recurse -Filter *.java | ForEach-Object FullName)
javac --release 17 -d out $files
```

Run the application:

```powershell
java -cp out kz.aitu.visitor.Main
```

Run the tests:

```powershell
java -cp out kz.aitu.visitor.VisitorPatternTest
```

The `out/` directory contains generated class files and should not be committed to Git.

## Example Output

```text
=== File System Visitor Pattern ===
Root directory: project
Total size: 6700 bytes
Directories visited: 4

Search query: .java
Matching files:
- Main.java (1200 bytes)
- Visitor.java (800 bytes)
- VisitorTest.java (1500 bytes)

Count visitor: 6 files, 4 directories
```

**Test result:**

```text
PASS: 9 tests
```

## Design Notes

- **Single Responsibility:** Each visitor performs one operation.
- **Open/Closed Principle:** New visitors can be introduced without modifying the existing element classes.
- **Encapsulation:** Visitors use the elements' public methods to access the information required for their operation.
- **Trade-off:** Adding a new element type would require updating the visitor interface and all concrete visitors.

The example is intentionally small so that the roles of elements, visitors, and the double-dispatch mechanism are easy to inspect and demonstrate.
