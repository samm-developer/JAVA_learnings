# Java Basics to Advance

A hands-on Java curriculum: standalone `Lesson*.java` files for core Java, then Maven/Spring Boot modules for backend development.

**Goal:** learn by running small demos — problem → solution → cheat sheet style.

---

## Requirements

- **Java 21+** (records, sealed classes, virtual threads, pattern matching)
- Optional: **Maven 3.9+** for `lesson37` onward
- Optional: Docker for `lesson52`

Check version:

```bash
java -version
javac -version
```

---

## How to run a lesson

Most lessons are a single file in the repo root:

```bash
# Simple lesson
javac Lesson01_HelloWorld.java && java Lesson01_HelloWorld

# Lessons that use colored headings
javac LessonConsole.java Lesson08c_MemoryManagement.java && java Lesson08c_MemoryManagement
```

Maven / Spring modules:

```bash
cd lesson38-springboot
mvn spring-boot:run
```

---

## Repo layout

| Path | What it is |
|------|------------|
| `Lesson01_…` … `Lesson36n_…` | Core Java demos (compile with `javac`) |
| `LessonConsole.java` | Shared colored section headings |
| `lesson19/` | Package demo (Library app) |
| `lesson37-maven/` … `lesson53-…/` | Maven / Spring Boot projects |
| [`CHAPTER_NOTES.md`](CHAPTER_NOTES.md) | **All chapter notes in one file** |

---

## Learning path (chapters)

### Part A — Core Java (fundamentals)

| # | Topic | Files |
|---|--------|--------|
| 1 | Hello World & `args` | `Lesson01`, `01b` |
| 2–7 | Variables, operators, if/else, loops, methods, arrays | `02`–`07` |
| 8 | Classes & objects + **memory** | `08`, `08b` Stack/Heap, `08c` Memory Mgmt |
| 9–11 | Encapsulation, inheritance, abstract/interface, sealed | `09`–`11b` |
| 12 | Exceptions | `12` |
| 13 | Collections (deep dive series) | `13`, `13b`–`13m` |
| 14–16 | Scanner, files, mini grade book | `14`–`16` |
| 17–18 | Strings, static vs instance | `17`, `18` |
| 19 | Packages | `lesson19/` |
| 20–24 | Generics, enums, records, lambda, streams | `20`–`24b` |

### Part B — Concurrency & I/O

| # | Topic | Files |
|---|--------|--------|
| 25 | Threads, pools, daemon, join, ThreadPoolExecutor | `25`, `25b`–`25m` |
| 26–27 | Synchronized, atomics, locks, virtual threads | `26`, `27`–`27n` |
| 28–31 | Files / NIO.2, sorting, Optional, DateTime | `28`–`31` |
| 32–33 | Capstone tasks, regex, annotations, reflection | `32`–`33c` |
| 34–36 | HTTP client + design patterns + JVM/SOLID | `34`–`36n` |

### Part C — Maven & Spring Boot

| # | Folder | Focus |
|---|--------|--------|
| 37 | `lesson37-maven` | Maven project layout |
| 38 | `lesson38-springboot` | First Spring Boot app |
| 39 | `lesson39-jdbc` | JDBC |
| 40–41 | `…-db`, `…-jpa` | DB + JPA |
| 42 | `…-validation` | Bean Validation |
| 43–47 | security → JWT → users → register | Auth series |
| 48–51 | testing, relations, pagination, service layer | API structure |
| 52–53 | Docker, Actuator | Ops |

---

## Study tips

1. Run the lesson, then read the printed diagram / PROBLEM → SOLUTION blocks.
2. Prefer **Java 21** so newer demos compile.
3. Use [`CHAPTER_NOTES.md`](CHAPTER_NOTES.md) for quick revision before interviews.
4. After core Java, follow Spring folders in order (`37` → `53`).

---

## Quick revision map

```
Basics → OOP → Collections → Modern Java (records/streams)
      → Threads & locks → Files/HTTP → Patterns
      → Maven → Spring Boot → Security/JWT → Docker
```

---

## License / use

Personal learning repo — edit freely, run demos, add your own notes in `CHAPTER_NOTES.md`.
