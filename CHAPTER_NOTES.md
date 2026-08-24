# Chapter Notes — Java Basics to Advance

One-file revision notes for every chapter in this repo.  
Run demos from the matching `Lesson*.java` (or `lessonNN/` folder). See [README.md](README.md) for how to compile.

---

## Part A — Core Java

### 01 — Hello World
- Entry point: `public static void main(String[] args)`
- `javac File.java` → bytecode; `java File` → run
- **01b:** `args` = command-line arguments (`java App hello`)

### 02 — Variables & data types
- Primitives: `byte short int long float double char boolean`
- Reference types: objects, arrays, Strings
- Declare → assign → use; type must match value

### 03 — Operators
- Arithmetic `+ - * / %`, comparison `== != < >`, logical `&& \|\| !`
- Assignment `+=`, increment `++` / `--`
- Prefer clear expressions over clever one-liners

### 04 — if / else
- Branch on conditions; nested if when needed
- `else if` chain for multiple cases
- Later: switch expressions (22b)

### 05 — Loops
- `for`, `while`, `do-while`, enhanced `for`
- Nested loops for tables / grids
- `break` / `continue` control flow

### 06 — Methods
- Reusable blocks: return type, name, parameters
- Call stack: each call pushes a **stack frame** (see 08b)
- Overloading: same name, different parameter lists

### 07 — Arrays
- Fixed size, same element type: `int[] a = new int[3]`
- Index from `0`; length via `.length`
- Prefer `ArrayList` (13) when size grows

### 08 — Classes & objects
- Class = blueprint; object = instance via `new`
- Fields + constructors + methods
- Multiple objects = multiple heap instances

### 08b — Stack vs Heap
| | STACK | HEAP |
|---|-------|------|
| Holds | locals, refs, frames | objects (`new`) |
| Shared? | **No** — per thread | **Yes** — threads in one JVM |
| Cleared | method returns | GC when unreachable |

- Copy primitive → new value; copy reference → same object (alias)

### 08c — Memory management
- One JVM process: **1 heap**, **N stacks** (N threads)
- **Class + static fields** → shared infrastructure
- **GC:** unreachable objects become eligible; no manual `free`
- `System.gc()` is only a **hint**
- Flags: `-Xms` / `-Xmx` (see also 36c)

**Shared among:** threads of **this** JVM  
**Not shared:** thread stacks; other OS processes

### 09 — Encapsulation
- Hide fields (`private`); expose via getters/setters
- Validate in setters / constructors
- Protects object invariants

### 10 — Inheritance
- `extends` — reuse + override
- `super(...)` calls parent constructor
- IS-A relationship; prefer composition when “has-a”

### 11 — Abstract & interfaces
- Abstract class: partial implementation + abstract methods
- Interface: contract (`implements`); default/static methods allowed
- **11b Sealed:** restrict which types may extend/implement (Java 17+)

### 12 — Exceptions
- Checked vs unchecked; `try / catch / finally`
- Prefer specific exceptions; don’t swallow errors
- Later: try-with-resources (28)

### 13 — Collections (overview)
- `List` / `Set` / `Map` / `Queue` instead of raw arrays
- Start: `ArrayList`, `HashMap`

### 13b–13m — Collections deep dive
| File | Focus |
|------|--------|
| 13b | How collections sit on the **heap** |
| 13c | **HashMap** — buckets, hashCode/equals |
| 13d | **LinkedHashMap** — insertion/access order, LRU |
| 13e | **ConcurrentHashMap** — thread-safe map |
| 13f | **PriorityQueue** — heap in an array |
| 13g | TreeMap, LinkedHashSet, ArrayDeque pick guide |
| 13h | Iterable / Iterator / ListIterator |
| 13i | **ArrayList** growth & internals |
| 13j | Set hierarchy (HashSet → TreeSet) |
| 13k | Legacy: Hashtable, Vector, Stack |
| 13l | **Deque** & ArrayDeque |
| 13m | **Comparable** vs **Comparator** |

**Map pick:** HashMap (fast) · LinkedHashMap (order) · TreeMap (sorted) · ConcurrentHashMap (threads)

### 14 — Scanner
- Read keyboard / tokens; watch `nextInt` + leftover newline
- **14b:** Scanner over a `String` (no typing)
- **14c:** why `nextLine()` after `nextDouble()`

### 15 — Files (basics)
- Read/write text files; close streams
- Prefer try-with-resources / NIO.2 (28, 28c)

### 16 — Mini project: Student Grade Book
- Combines classes, collections, I/O practice

### 17 — Strings
- Immutable; `+` vs `StringBuilder` for loops
- Common APIs: `substring`, `split`, `equals` (not `==` for content)

### 18 — static vs instance
- `static` → belongs to **class** (shared)
- Instance → belongs to **object**
- `main` is static so JVM can start without an instance

### 19 — Packages (`lesson19/`)
- `package` organizes code; `import` brings types in
- Demo: library app under `lesson19/app` + `model`

### 20 — Generics
- `List<String>` — compile-time type safety
- Type erasure at runtime
- **20b:** `@SafeVarargs` / heap pollution warning

### 21 — Enums
- Fixed named constants (`Day.MONDAY`)
- Can have fields/methods; safer than magic strings

### 22 — Records
- Compact immutable data carrier; auto equals/hashCode/toString
- **22b:** switch expressions + pattern matching
- **22c:** old POJO vs record
- **22d Local record:** inside **one method** only
- **22e Nested record:** `Outer.Inner` (e.g. `Order.LineItem`)

| Style | Scope |
|-------|--------|
| Top-level | Project-wide |
| Nested | Belongs to outer type |
| Local | One method |

### 23 — Lambda
- Short function as value: `(a, b) -> a + b`
- Works with functional interfaces (`Runnable`, `Comparator`, …)
- `forEach` on collections

### 24 — Streams
- Pipeline: `filter` → `map` → `collect` / `forEach`
- Lazy until terminal op; don’t mutate shared state in streams
- **24b:** same task with vs without stream

---

## Part B — Concurrency, I/O, APIs, design

### 25 — Threads
- `Thread` / `Runnable` — parallel work
- **25b:** each thread **own stack**; **shared heap**
- **25c:** `ThreadLocal` — per-thread copy
- **25d–e:** ExecutorService pools (fixed, cached, single, scheduled)
- **25f:** ForkJoinPool — work stealing
- **25g:** deep dive map of 25–27
- **25h:** daemon threads (JVM can exit while they run)
- **25i:** priority (hint only)
- **25j:** `wait` / `notify` / `notifyAll`
- **25k:** user thread keeps JVM alive after main exits
- **25l:** `join()` — wait for another thread
- **25m:** `ThreadPoolExecutor(core, max, keepAlive, queue)`
  - Flow: create to **core** → **queue** → grow to **max** → **reject**
  - keepAlive: idle **extra** threads (above core) die after timeout

### 26 — synchronized
- Shared heap data → race conditions without locking
- `synchronized` = intrinsic monitor lock (one thread in critical section)

### 27 — Atomics, async, locks
| File | Idea |
|------|------|
| 27 | synchronized methods + `AtomicInteger` |
| 27b | `CompletableFuture` async |
| 27c–d | `volatile` (visibility) vs Atomic (atomic updates) |
| 27e | thread-safe collection versions |
| 27f | What is a **lock**? |
| 27g | `synchronized` as built-in lock |
| 27h | `ReentrantLock` |
| 27i | `ReadWriteLock` |
| 27j | `StampedLock` |
| 27k | `Semaphore` (N permits) |
| 27l | CAS / lock-free |
| 27m | `Condition` + fair locks |
| 27n | **Virtual threads** (Java 21) — cheap threads for blocking I/O |

### 28 — File I/O (modern)
- try-with-resources auto-closes
- **28b:** append without overwrite
- **28c:** NIO.2 `Path` / `Files`

### 29 — Sorting
- `Comparable` (natural order) vs `Comparator` (external)
- `Collections.sort` / `list.sort`

### 30 — Optional
- Represent missing values without `null` chaos
- `of` / `ofNullable` / `orElse` / `ifPresent` / `map`

### 31 — Date & Time
- Prefer `java.time` (`LocalDate`, `LocalDateTime`, `Duration`)
- Immutable, timezone-aware types available (`ZonedDateTime`)

### 32 — Capstone: Task List
- Ties collections + I/O + OOP together

### 33 — Regex, annotations, reflection
- **33:** Pattern / Matcher for validate & find
- **33b:** custom annotations
- **33c:** reflection — inspect/load types at runtime (use sparingly)

### 34–36 — HTTP
- **34:** `HttpClient` GET
- **35:** POST body
- **36:** PUT & DELETE

### 36b–36n — Design & JVM
| File | Topic |
|------|--------|
| 36b | Factory, Strategy, Builder demos |
| 36c | JVM: heap/stack/GC/classpath |
| 36d | **SOLID** |
| 36e | Patterns overview |
| 36f | Singleton |
| 36g | Strategy |
| 36h | Factory |
| 36i | Builder |
| 36j | Adapter |
| 36k | Decorator |
| 36l | Observer |
| 36m | Template Method |
| 36n | Facade |

**Creational:** Singleton, Factory, Builder  
**Structural:** Adapter, Decorator, Facade  
**Behavioral:** Strategy, Observer, Template Method

---

## Part C — Maven & Spring Boot

| Lesson | Folder | Notes |
|--------|--------|--------|
| 37 | `lesson37-maven` | `pom.xml`, `src/main/java`, build lifecycle |
| 38 | `lesson38-springboot` | `@SpringBootApplication`, embedded server |
| 39 | `lesson39-jdbc` | JDBC drivers, connections, SQL |
| 40 | `lesson40-springboot-db` | Spring + database config |
| 41 | `lesson41-springboot-jpa` | JPA / entities / repositories |
| 42 | `lesson42-springboot-validation` | Bean Validation (`@NotNull`, …) |
| 43 | `lesson43-springboot-security` | Spring Security basics |
| 44 | `lesson44-springboot-roles` | Roles / authorities |
| 45 | `lesson45-springboot-jwt` | JWT authentication |
| 46 | `lesson46-springboot-users-db` | Users persisted in DB |
| 47 | `lesson47-springboot-register` | Registration flow |
| 48 | `lesson48-springboot-testing` | Unit / slice / integration tests |
| 49 | `lesson49-springboot-relations` | Entity relations (1-N, N-N) |
| 50 | `lesson50-springboot-pagination` | Page / size / sort |
| 51 | `lesson51-springboot-service-layer` | Controller → Service → Repo |
| 52 | `lesson52-springboot-docker` | Containerize the app |
| 53 | `lesson53-springboot-actuator` | Health / metrics endpoints |

Run pattern:

```bash
cd lesson41-springboot-jpa
mvn spring-boot:run
```

---

## Ultra-short cheat sheet

```
STACK = per-thread locals/refs     HEAP = shared objects
static/class = shared              instance fields = per object
synchronized/lock = protect shared heap data
record = immutable data            local record = one method
HashMap fast / LinkedHashMap order / ConcurrentHashMap threads
ThreadPool: core → queue → max → reject
Virtual threads = many blocking tasks, cheap
Spring: Controller → Service → Repository → DB
```

---

## How to use this file

1. Skim the section before running the matching lesson.
2. After the lesson, add your own one-liners under that heading.
3. Before interviews, re-read **08b/08c**, **13**, **25–27**, **36**, and Spring **43–51**.

*Last aligned with repo lessons through 08c Memory Management, 22e Nested Record, 25m ThreadPoolExecutor, 27n Virtual Threads, 36n Facade, Spring lesson53.*
