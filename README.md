# Assignment 6 — Visitor Pattern (Java 17)

Individual assignment for Software Design Patterns (ShP-2216). Selected topic: **Option A — File System Visitor**.

## Purpose

Model a hierarchy of `FileElement` and `DirectoryElement` and run independent operations over the same tree using the Visitor behavioral design pattern. The project demonstrates **double dispatch** through `element.accept(visitor)` and `visitor.visitFile(this)` / `visitor.visitDirectory(this)`.

## Visitors

- `SizeCalculatorVisitor`: adds the sizes of all files, including nested files.
- `SearchVisitor`: finds files by a case-insensitive name fragment.
- `FileCountVisitor`: counts files and directories; demonstrates how to add a new operation without changing the element classes.

Directory recursion happens in `DirectoryElement.accept()`; each visitor receives the same nodes. There are **no `instanceof` or element-type checking chains**. The example works on a simulated file system (does not read or modify actual disks).

## Requirements

Java JDK **17+**, including Java 21 compiled with `--release 17`. No Maven, Gradle, or third-party libraries needed.

## Run in IntelliJ IDEA

1. Open this folder as a project.
2. Set Project SDK to JDK 17 (or newer) and language level 17.
3. Open `src/main/java/kz/aitu/visitor/Main.java`.
4. Right-click `Main.main()` and select **Run 'Main.main()'**.
5. To run tests, open `src/test/java/kz/aitu/visitor/VisitorPatternTest.java` and run its `main()`.

If IntelliJ does not automatically recognize the folders, mark `src/main/java` as **Sources Root** and `src/test/java` as **Test Sources Root**.

## Run from Windows PowerShell

```powershell
.\run.ps1
.\test.ps1
```

If your system blocks scripts, run the equivalent commands manually:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
$files = @(Get-ChildItem src/main/java -Recurse -Filter *.java | ForEach-Object FullName)
javac --release 17 -d out $files
java -cp out kz.aitu.visitor.Main

$allFiles = @(Get-ChildItem src/main/java,src/test/java -Recurse -Filter *.java | ForEach-Object FullName)
javac --release 17 -d out $allFiles
java -cp out kz.aitu.visitor.VisitorPatternTest
```

## Run on Linux/macOS

```bash
./run.sh
./test.sh
```

## Expected console output

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

Tests: `PASS: 9 tests`.

## Key classes

```text
src/main/java/kz/aitu/visitor/
  FileSystemElement.java     // Element interface
  FileElement.java           // Concrete leaf
  DirectoryElement.java      // Composite; recurses through children
  FileSystemVisitor.java     // Visitor interface
  SizeCalculatorVisitor.java // Visitor #1
  SearchVisitor.java         // Visitor #2
  FileCountVisitor.java      // Visitor #3 (extensibility)
  Main.java                  // Sample tree and live demo
src/test/java/kz/aitu/visitor/
  VisitorPatternTest.java    // 9 self-contained regression tests
docs/Visitor_UML.puml        // Editable PlantUML class diagram
docs/Visitor_UML.png         // UML image
docs/sample_output.txt       // Verified output from the application
```

## How double dispatch works

```java
FileSystemVisitor visitor = new SizeCalculatorVisitor();
FileSystemElement item = new FileElement("notes.txt", 100);
item.accept(visitor);                // Java selects the concrete element's accept()
// FileElement.accept calls visitor.visitFile(this); // then chooses visitFile(FileElement)
```

Adding a new visitor only requires a new class implementing `FileSystemVisitor`. Adding a **new element type**, however, requires updating the visitor contract and its implementations: this is the Visitor pattern's main trade-off.

## GitHub submission

This project includes local incremental commits; verify them with `git log --oneline`. Configure your own GitHub remote and identity before pushing:

```bash
git config user.name "YOUR_NAME"
git config user.email "YOUR_EMAIL"
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPOSITORY.git
git push -u origin main
```

The included report contains a **GitHub URL placeholder** that must be replaced with your actual repository URL before submission. Read and understand the solution, adapt it to your own work and course's academic-integrity rules, and prepare to demonstrate the `accept()` / `visit()` sequence in the defense.
