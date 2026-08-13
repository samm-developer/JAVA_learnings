// Lesson 3: Operators
// Compile: javac Lesson03_Operators.java
// Run:     java Lesson03_Operators

public class Lesson03_Operators {
    public static void main(String[] args) {

        // ===== Arithmetic =====
        int a = 10;
        int b = 3;

        System.out.println("a + b = " + (a + b)); // 13
        System.out.println("a - b = " + (a - b)); // 7
        System.out.println("a * b = " + (a * b)); // 30
        System.out.println("a / b = " + (a / b)); // 3  (int division truncates)
        System.out.println("a % b = " + (a % b)); // 1  (remainder)

        // For real division, use double
        System.out.println("10 / 3.0 = " + (10 / 3.0)); // 3.333...

        // ===== Assignment shortcuts =====
        int score = 50;
        score += 10; // score = score + 10 → 60
        score -= 5;  // 55
        score *= 2;  // 110
        score /= 10; // 11
        System.out.println("score = " + score);

        // ===== Increment / Decrement =====
        int n = 5;
        n++; // n = n + 1 → 6
        n--; // n = n - 1 → 5
        System.out.println("n = " + n);

        // Prefix vs postfix (know the difference)
        int x = 5;
        System.out.println("x++ = " + (x++)); // prints 5, then x becomes 6
        System.out.println("now x = " + x);    // 6
        System.out.println("++x = " + (++x)); // x becomes 7, then prints 7

        // ===== Comparison (result is boolean) =====
        System.out.println("a == b : " + (a == b)); // equal?
        System.out.println("a != b : " + (a != b)); // not equal?
        System.out.println("a > b  : " + (a > b));
        System.out.println("a < b  : " + (a < b));
        System.out.println("a >= b : " + (a >= b));
        System.out.println("a <= b : " + (a <= b));

        // ===== Logical =====
        boolean hasId = true;
        boolean isAdult = true;
        boolean isBanned = false;

        System.out.println("canEnter (AND): " + (hasId && isAdult));     // both true
        System.out.println("hasAccess (OR): " + (hasId || isBanned));    // at least one true
        System.out.println("not banned (NOT): " + (!isBanned));          // flips true/false

        // ===== String + (concatenation) =====
        String first = "Java";
        String second = "Rocks";
        System.out.println(first + " " + second); // Java Rocks
        System.out.println("Score is " + 100);    // number becomes text when joined
    }
}
