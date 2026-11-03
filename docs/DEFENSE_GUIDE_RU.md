# Assignment 6 — шпаргалка к защите Visitor Pattern

## Что делает проект (30 секунд)

«Я реализовал Visitor Pattern для виртуальной файловой системы. Структура состоит из FileElement и DirectoryElement. На одной структуре я запускаю три независимых операции: подсчёт размера файлов, поиск по названию и подсчёт количества файлов/папок. Чтобы добавить новую операцию, не нужно менять классы FileElement и DirectoryElement».

## Как объяснить UML

- **FileSystemElement** — интерфейс Element, объявляет `accept(FileSystemVisitor)`.
- **FileElement, DirectoryElement** — Concrete Elements, реализуют интерфейс.
- **FileSystemVisitor** — интерфейс Visitor, объявляет `visitFile(FileElement)` и `visitDirectory(DirectoryElement)`.
- **SizeCalculatorVisitor, SearchVisitor, FileCountVisitor** — Concrete Visitors, каждый реализует оба метода.
- **DirectoryElement** — Composite/объектная структура: содержит список `FileSystemElement` и рекурсивно вызывает `accept()` у детей.

## Double dispatch по шагам

```java
FileSystemElement element = new FileElement("a.txt", 100);
FileSystemVisitor visitor = new SizeCalculatorVisitor();
element.accept(visitor);       // шаг 1: вызывается FileElement.accept()
// Внутри FileElement.accept():
visitor.visitFile(this);      // шаг 2: метод для FileElement выбран, затем вызван нужный visitor
```

В Java перегрузка `visitFile`/`visitDirectory` определяется по статическому типу `this` внутри конкретного элемента, а реализация интерфейса Visitor выбирается динамически. Никакой `instanceof` для различения File и Directory не требуется.

При `DirectoryElement.accept()` сначала вызывается `visitDirectory(this)`, а затем для каждого ребёнка `child.accept(visitor)`. Поэтому вложенные файлы тоже будут обработаны.

## Важные вопросы преподавателя

**Зачем нужен Visitor?** Новые операции можно добавлять отдельными классами, не изменяя существующие элементы.

**Почему не сделать calculateSize() прямо в FileElement?** Тогда при каждой новой операции пришлось бы менять модели, а их ответственность стала бы разрастаться.

**Почему интерфейс Visitor содержит два visit-метода?** Потому что у каждого конкретного типа элемента своя обработка, выбираемая без проверок типа.

**Плюсы?** (1) Добавление новых операций без изменения элементов. (2) Отдельные классы операций поддерживают Single Responsibility. (3) Нет type-checking chains по элементам. (4) Все операции работают на одной и той же структуре.

**Минусы?** (1) Добавление нового типа элемента потребует изменить Visitor-интерфейс и все Visitor-классы. (2) Visitor-методам может понадобиться доступ к данным элементов, усложняя инкапсуляцию. (3) Каждая операция требует отдельного класса и хранения состояния.

**Как здесь решена проблема инкапсуляции?** Поля private, открыты только нужные read-only getters, список детей копируется через `List.copyOf()`.

**Что такое объектная структура?** Дерево проекта: каталоги содержат другие каталоги и файлы. За рекурсивный обход отвечает `DirectoryElement.accept()`.

**Почему работает с Java 17?** Код компилируется с `javac --release 17`; для этого не нужны сторонние библиотеки.

**Visitor vs Strategy?** Strategy подменяет один выбранный алгоритм работы контекста, а Visitor описывает разные операции над фиксированной иерархией типов через методы `visitX`.

**Visitor vs Facade?** Facade упрощает интерфейс подсистемы, а Visitor добавляет независимые операции поверх структуры объектов.

**Какой основной паттерн ещё здесь есть?** Composite: `DirectoryElement` содержит `List<FileSystemElement>`, в котором могут быть как листья FileElement, так и поддиректории DirectoryElement.

## Быстрый live coding — новый посетитель

Если преподаватель попросит написать Visitor, который считает количество файлов больше 1000 байт, можно создать новый класс:

```java
package kz.aitu.visitor;

public final class LargeFileVisitor implements FileSystemVisitor {
    private int count;

    @Override
    public void visitFile(FileElement file) {
        if (file.getSizeInBytes() > 1000) {
            count++;
        }
    }

    @Override
    public void visitDirectory(DirectoryElement directory) {
        // Обход дочерних элементов уже выполняется в DirectoryElement.accept().
    }

    public int getCount() { return count; }
}
```

Использование в Main:

```java
LargeFileVisitor large = new LargeFileVisitor();
project.accept(large);
System.out.println("Large files: " + large.getCount()); // 3
```

В примере размер больше 1000 байт имеют Main.java (1200), VisitorTest.java (1500) и design.pdf (2500). Классы FileElement и DirectoryElement трогать не нужно.

## Запуск / проверка

В IntelliJ IDEA: запускаем `Main.main()` и `VisitorPatternTest.main()`.

В PowerShell из папки проекта:

```powershell
.\run.ps1
.\test.ps1
```

Ожидаемый размер: **6700 bytes**. Поиск `.java` находит **3 файла**. Третий Visitor считает **6 файлов и 4 директории**. Проверка: `PASS: 9 tests`.
