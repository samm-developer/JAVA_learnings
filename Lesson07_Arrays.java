// Lesson 7: Arrays (fixed-size list of same type)
// Compile: javac Lesson07_Arrays.java
// Run:     java Lesson07_Arrays

public class Lesson07_Arrays {
    public static void main(String[] args) {

        // ===== Create arrays =====
        int[] scores = {85, 90, 78, 92, 88};          // with values
        String[] fruits = new String[3];              // empty slots (size 3)
        fruits[0] = "Apple";
        fruits[1] = "Banana";
        fruits[2] = "Mango";

        // ===== Index starts at 0 =====
        System.out.println("First score: " + scores[0]);
        System.out.println("Last score:  " + scores[scores.length - 1]);
        System.out.println("Length:      " + scores.length);

        // ===== Update a value =====
        scores[2] = 80;
        System.out.println("Updated scores[2]: " + scores[2]);

        // ===== Loop with index =====
        System.out.println("--- scores (index loop) ---");
        for (int i = 0; i < scores.length; i++) {
            System.out.println("scores[" + i + "] = " + scores[i]);
        }

        // ===== for-each (when you don't need index) =====
        System.out.println("--- fruits (for-each) ---");
        for (String fruit : fruits) {
            System.out.println(fruit);
        }

        // ===== Practical: sum & average =====
        int sum = 0;
        for (int score : scores) {
            sum += score;
        }
        double average = (double) sum / scores.length;
        System.out.println("Sum = " + sum);
        System.out.println("Average = " + average);

        // ===== Find max =====
        int max = scores[0];
        for (int i = 1; i < scores.length; i++) {
            if (scores[i] > max) {
                max = scores[i];
            }
        }
        System.out.println("Max score = " + max);

        // ===== Pass array to a method =====
        printArray(scores);
        System.out.println("Sum via method = " + arraySum(scores));

        // ===== 2D array (rows x columns) =====
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6}
        };
        System.out.println("--- 2D array ---");
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                System.out.print(matrix[row][col] + " ");
            }
            System.out.println();
        }

        // Common mistake reminder:
        // scores[5] would crash → ArrayIndexOutOfBoundsException
        // valid indexes for length 5 are 0..4
    }

    public static void printArray(int[] arr) {
        System.out.print("Array: ");
        for (int value : arr) {
            System.out.print(value + " ");
        }
        System.out.println();
    }

    public static int arraySum(int[] arr) {
        int total = 0;
        for (int value : arr) {
            total += value;
        }
        return total;
    }
}
