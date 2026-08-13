// Lesson 5: Loops (repeat work)
// Compile: javac Lesson05_Loops.java
// Run:     java Lesson05_Loops

public class Lesson05_Loops {
    public static void main(String[] args) {

        // ===== for loop: when you know how many times =====
        // for (start; condition; update)
        System.out.println("--- for: 1 to 5 ---");
        for (int i = 1; i <= 5; i++) {
            System.out.println("i = " + i);
        }

        // ===== while: repeat while condition is true =====
        System.out.println("--- while ---");
        int n = 3;
        while (n > 0) {
            System.out.println("n = " + n);
            n--; // IMPORTANT: update, or infinite loop!
        }

        // ===== do-while: runs at least once =====
        System.out.println("--- do-while ---");
        int x = 0;
        do {
            System.out.println("x = " + x);
            x++;
        } while (x < 3);

        // ===== Nested loop: multiplication table (1..3) =====
        System.out.println("--- nested for ---");
        for (int row = 1; row <= 3; row++) {
            for (int col = 1; col <= 3; col++) {
                System.out.print(row * col + "\t");
            }
            System.out.println(); // new line after each row
        }

        // ===== break: stop the loop early =====
        System.out.println("--- break ---");
        for (int i = 1; i <= 10; i++) {
            if (i == 4) {
                break; // exit loop completely
            }
            System.out.println(i);
        }

        // ===== continue: skip this iteration =====
        System.out.println("--- continue (skip even) ---");
        for (int i = 1; i <= 6; i++) {
            if (i % 2 == 0) {
                continue; // skip rest of this round
            }
            System.out.println(i);
        }

        // ===== Enhanced for (for-each) — preview =====
        System.out.println("--- for-each ---");
        int[] numbers = {10, 20, 30};
        for (int value : numbers) {
            System.out.println(value);
        }

        // ===== Practical: sum 1 to 10 =====
        int sum = 0;
        for (int i = 1; i <= 10; i++) {
            sum += i;
        }
        System.out.println("Sum 1..10 = " + sum);
    }
}
