// Lesson 17: Strings deep dive
// Compile: javac Lesson17_Strings.java
// Run:     java Lesson17_Strings

public class Lesson17_Strings {
    public static void main(String[] args) {
        String text = "  Java Programming  ";

        // ===== Length & characters =====
        System.out.println("Original: [" + text + "]");
        System.out.println("length(): " + text.length());
        System.out.println("charAt(2): " + text.charAt(2)); // first non-space 'J' is at index 2

        // ===== Trim / case =====
        String clean = text.trim(); // remove leading/trailing spaces
        System.out.println("trim(): [" + clean + "]");
        System.out.println("toUpperCase(): " + clean.toUpperCase());
        System.out.println("toLowerCase(): " + clean.toLowerCase());

        // ===== Compare (ALWAYS use equals, not ==) =====
        String a = "Java";
        String b = "java";
        System.out.println("equals: " + a.equals(b));                 // false
        System.out.println("equalsIgnoreCase: " + a.equalsIgnoreCase(b)); // true

        // ===== Contains / starts / ends =====
        System.out.println("contains(\"gram\"): " + clean.contains("gram"));
        System.out.println("startsWith(\"Java\"): " + clean.startsWith("Java"));
        System.out.println("endsWith(\"ing\"): " + clean.endsWith("ing"));

        // ===== substring =====
        // substring(start) or substring(start, end)  [end is exclusive]
        System.out.println("substring(0, 4): " + clean.substring(0, 4)); // Java
        System.out.println("substring(5): " + clean.substring(5));       // Programming

        // ===== indexOf / replace =====
        System.out.println("indexOf('a'): " + clean.indexOf('a')); // first 'a'
        System.out.println("replace: " + clean.replace("Java", "Python"));

        // ===== split (used in Lesson 16 CSV) =====
        String csv = "Shashwat,98,A";
        String[] parts = csv.split(",");
        System.out.println("--- split ---");
        for (String part : parts) {
            System.out.println(part);
        }

        // ===== + vs concat / joining =====
        String full = "Hello" + " " + "World";
        System.out.println("concat with +: " + full);

        // ===== Strings are immutable =====
        // Methods return a NEW string; original stays same unless you reassign
        String name = "Asha";
        name.toUpperCase();          // result thrown away
        System.out.println("after toUpperCase without assign: " + name); // still Asha
        name = name.toUpperCase();   // reassign
        System.out.println("after reassign: " + name); // ASHA

        // ===== Practical: simple email check =====
        String email = "user@example.com";
        boolean looksValid = email.contains("@")
                && email.contains(".")
                && !email.startsWith("@")
                && !email.endsWith("@");
        System.out.println("Email looks valid? " + looksValid);

        // ===== Practical: count vowels =====
        String word = "Programming";
        int vowels = 0;
        String lower = word.toLowerCase();
        for (int i = 0; i < lower.length(); i++) {
            char c = lower.charAt(i);
            if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
                vowels++;
            }
        }
        System.out.println("Vowels in '" + word + "': " + vowels);
    }
}
