// Lesson 36d: SOLID principles (clean OOP design)
// Compile: javac Lesson36d_SOLID.java
// Run:     java Lesson36d_SOLID
//
// Place: after OOP + Lesson 36b (design patterns).
//
// S — Single Responsibility
// O — Open/Closed
// L — Liskov Substitution
// I — Interface Segregation
// D — Dependency Inversion

public class Lesson36d_SOLID {

    public static void main(String[] args) {
        singleResponsibility();
        openClosed();
        liskov();
        interfaceSegregation();
        dependencyInversion();
        summary();
    }

    // =========================================================================
    // S — Single Responsibility: one class = one reason to change
    // =========================================================================
    static void singleResponsibility() {
        System.out.println("=== S: Single Responsibility ===");
        System.out.println("""
                BAD:  UserService saves user AND sends email AND writes logs
                GOOD: UserRepository saves | EmailSender sends | Logger logs
                """);

        // BAD — one class does everything
        class BadUserService {
            void register(String email) {
                System.out.println("  [BAD] save " + email + " to DB");
                System.out.println("  [BAD] send welcome email");
                System.out.println("  [BAD] write audit log");
            }
        }

        // GOOD — each class one job
        class UserRepository {
            void save(String email) {
                System.out.println("  [GOOD] DB save: " + email);
            }
        }
        class EmailSender {
            void sendWelcome(String email) {
                System.out.println("  [GOOD] email to: " + email);
            }
        }
        class AuditLog {
            void log(String msg) {
                System.out.println("  [GOOD] log: " + msg);
            }
        }
        class GoodUserService {
            final UserRepository repo = new UserRepository();
            final EmailSender mail = new EmailSender();
            final AuditLog log = new AuditLog();

            void register(String email) {
                repo.save(email);
                mail.sendWelcome(email);
                log.log("registered " + email);
            }
        }

        new BadUserService().register("a@x.com");
        new GoodUserService().register("b@x.com");
        System.out.println();
    }

    // =========================================================================
    // O — Open/Closed: open for extension, closed for modification
    // =========================================================================
    static void openClosed() {
        System.out.println("=== O: Open/Closed ===");
        System.out.println("""
                BAD:  add discount type → edit if/else inside DiscountService
                GOOD: add new Discount class — no change to existing code
                """);

        // BAD
        class BadDiscount {
            double apply(String type, double price) {
                if (type.equals("PERCENT10")) return price * 0.9;
                if (type.equals("FLAT50")) return price - 50;
                // adding "BOGO" means editing THIS method ❌
                return price;
            }
        }

        // GOOD — strategy / polymorphism
        interface Discount {
            double apply(double price);
        }
        class Percent10 implements Discount {
            public double apply(double price) {
                return price * 0.9;
            }
        }
        class Flat50 implements Discount {
            public double apply(double price) {
                return price - 50;
            }
        }
        // NEW type = NEW class — DiscountService untouched ✅
        class BogoHalf implements Discount {
            public double apply(double price) {
                return price / 2;
            }
        }
        class Checkout {
            double pay(double price, Discount d) {
                return d.apply(price);
            }
        }

        Checkout c = new Checkout();
        System.out.println("  BAD  percent: " + new BadDiscount().apply("PERCENT10", 100));
        System.out.println("  GOOD percent: " + c.pay(100, new Percent10()));
        System.out.println("  GOOD flat:    " + c.pay(100, new Flat50()));
        System.out.println("  GOOD bogo:    " + c.pay(100, new BogoHalf()) + " (extended, no edit to Checkout)");
        System.out.println();
    }

    // =========================================================================
    // L — Liskov Substitution: subclass must be usable wherever parent is
    // =========================================================================
    static void liskov() {
        System.out.println("=== L: Liskov Substitution ===");
        System.out.println("""
                BAD:  Square extends Rectangle but setWidth breaks height expectation
                GOOD: shapes don't lie about their contracts
                """);

        class Rectangle {
            int w, h;
            void setWidth(int w) {
                this.w = w;
            }
            void setHeight(int h) {
                this.h = h;
            }
            int area() {
                return w * h;
            }
        }
        // BAD LSP: Square overrides in a way that surprises Rectangle users
        class BadSquare extends Rectangle {
            @Override
            void setWidth(int w) {
                this.w = w;
                this.h = w; // unexpected side effect for Rectangle code
            }
            @Override
            void setHeight(int h) {
                this.w = h;
                this.h = h;
            }
        }

        Rectangle r = new BadSquare();
        r.setWidth(5);
        r.setHeight(10); // user expects area 50; BadSquare → 100
        System.out.println("  BAD  Square-as-Rectangle area=" + r.area() + " (expected 50 if real Rectangle) ❌");

        // GOOD: separate types / common interface without broken inheritance
        interface Shape {
            int area();
        }
        class GoodRect implements Shape {
            final int w, h;
            GoodRect(int w, int h) {
                this.w = w;
                this.h = h;
            }
            public int area() {
                return w * h;
            }
        }
        class GoodSquare implements Shape {
            final int side;
            GoodSquare(int side) {
                this.side = side;
            }
            public int area() {
                return side * side;
            }
        }
        Shape s1 = new GoodRect(5, 10);
        Shape s2 = new GoodSquare(5);
        System.out.println("  GOOD Rect area=" + s1.area() + " Square area=" + s2.area() + " ✅");
        System.out.println();
    }

    // =========================================================================
    // I — Interface Segregation: many small interfaces > one fat interface
    // =========================================================================
    static void interfaceSegregation() {
        System.out.println("=== I: Interface Segregation ===");
        System.out.println("""
                BAD:  Worker must implement work()+eat()+sleep() even for Robot
                GOOD: Workable / Eatable split — Robot only implements Workable
                """);

        // BAD fat interface
        interface BadWorker {
            void work();
            void eat();
        }
        class RobotForced implements BadWorker {
            public void work() {
                System.out.println("  [BAD] robot works");
            }
            public void eat() {
                System.out.println("  [BAD] robot.eat() — forced empty/fake ❌");
            }
        }

        // GOOD segregated
        interface Workable {
            void work();
        }
        interface Eatable {
            void eat();
        }
        class Human implements Workable, Eatable {
            public void work() {
                System.out.println("  [GOOD] human works");
            }
            public void eat() {
                System.out.println("  [GOOD] human eats");
            }
        }
        class Robot implements Workable {
            public void work() {
                System.out.println("  [GOOD] robot works (no eat) ✅");
            }
        }

        new RobotForced().work();
        new RobotForced().eat();
        new Human().work();
        new Human().eat();
        new Robot().work();
        System.out.println();
    }

    // =========================================================================
    // D — Dependency Inversion: depend on abstractions, not concretions
    // =========================================================================
    static void dependencyInversion() {
        System.out.println("=== D: Dependency Inversion ===");
        System.out.println("""
                BAD:  OrderService hard-codes MySqlDatabase
                GOOD: OrderService depends on Database interface; inject MySql or Mongo
                """);

        // BAD — tightly coupled to MySql
        class MySqlDb {
            void save(String order) {
                System.out.println("  MySQL save " + order);
            }
        }
        class BadOrderService {
            MySqlDb db = new MySqlDb(); // locked to MySQL ❌
            void place(String order) {
                db.save(order);
            }
        }

        // GOOD — depend on abstraction
        interface Database {
            void save(String order);
        }
        class MySqlDatabase implements Database {
            public void save(String order) {
                System.out.println("  [GOOD] MySQL: " + order);
            }
        }
        class MongoDatabase implements Database {
            public void save(String order) {
                System.out.println("  [GOOD] Mongo: " + order);
            }
        }
        class GoodOrderService {
            final Database db; // abstraction ✅
            GoodOrderService(Database db) {
                this.db = db;
            }
            void place(String order) {
                db.save(order);
            }
        }

        new BadOrderService().place("ORD-1");
        new GoodOrderService(new MySqlDatabase()).place("ORD-2");
        new GoodOrderService(new MongoDatabase()).place("ORD-3"); // swap easy ✅
        System.out.println();
    }

    static void summary() {
        System.out.println("=== SOLID cheat sheet ===");
        System.out.println("""
                S  Single Responsibility   one class → one job
                O  Open/Closed             extend via new code, don't edit old
                L  Liskov Substitution     subclass must honor parent contract
                I  Interface Segregation   small interfaces, not fat ones
                D  Dependency Inversion    depend on interfaces, inject concretes

                You already use D in Spring: Controller → Service interface → impl
                """);
    }
}
