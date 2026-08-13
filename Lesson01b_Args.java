// Lesson 1b: Why String[] args exists
// Compile: javac Lesson01b_Args.java
// Run:     java Lesson01b_Args
// Or:      java Lesson01b_Args Shashwat 25

public class Lesson01b_Args {
    public static void main(String[] args) {
        // args is an array of Strings from the command line
        System.out.println("Number of arguments: " + args.length);

        if (args.length == 0) {
            System.out.println("No arguments passed.");
            System.out.println("Try: java Lesson01b_Args Shashwat 25");
            return;
        }

        System.out.println("Hello, " + args[0] + "!");

        if (args.length >= 2) {
            System.out.println("You said your age is: " + args[1]);
        }
    }
}
