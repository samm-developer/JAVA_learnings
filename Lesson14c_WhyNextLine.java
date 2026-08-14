// Demo: why sc.nextLine() is needed after nextDouble()
// Compile: javac Lesson14c_WhyNextLine.java
// Run:     java Lesson14c_WhyNextLine

import java.util.Scanner;

public class Lesson14c_WhyNextLine {
    public static void main(String[] args) {

        // Same typed answers for both demos:
        // height = 5.9  then Enter
        // city   = Delhi then Enter
        String typedByUser = "5.9\nDelhi\n";

        System.out.println("========== WITHOUT extra nextLine() ==========");
        withoutFix(typedByUser);

        System.out.println();
        System.out.println("========== WITH extra nextLine() ==========");
        withFix(typedByUser);
    }

    static void withoutFix(String typedByUser) {
        Scanner sc = new Scanner(typedByUser);

        double height = sc.nextDouble(); // takes 5.9, leaves \n behind
        System.out.println("height = [" + height + "]");

        // BUG: this nextLine() eats the leftover \n, NOT "Delhi"
        String city = sc.nextLine();
        System.out.println("city   = [" + city + "]   <-- EMPTY! skipped Delhi");

        sc.close();
    }

    static void withFix(String typedByUser) {
        Scanner sc = new Scanner(typedByUser);

        double height = sc.nextDouble(); // takes 5.9, leaves \n behind
        System.out.println("height = [" + height + "]");

        sc.nextLine(); // throw away leftover \n  <--- THIS LINE

        String city = sc.nextLine(); // now waits for / reads "Delhi"
        System.out.println("city   = [" + city + "]   <-- correct");

        sc.close();
    }
}
