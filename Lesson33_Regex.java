// Lesson 33: Regex (regular expressions — find / validate text patterns)
// Compile: javac Lesson33_Regex.java
// Run:     java Lesson33_Regex

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lesson33_Regex {
    public static void main(String[] args) {

        // ===== 1) Simple contains-style match =====
        String text = "My email is shashwat@example.com and phone is 9876543210";

        // String.matches() checks the WHOLE string
        System.out.println("Only digits? " + "12345".matches("\\d+"));
        System.out.println("Only digits? " + "12a45".matches("\\d+"));

        // ===== 2) Useful pattern pieces =====
        // .  any one character
        // \\d digit 0-9
        // \\w word char (letter/digit/_)
        // \\s whitespace
        // +  one or more
        // *  zero or more
        // ?  optional (0 or 1)
        // [A-Z] character class
        // ^ start   $ end

        // ===== 3) Validate email (simple beginner pattern) =====
        String emailRegex = "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$";
        System.out.println("Email ok? " + "shashwat@example.com".matches(emailRegex));
        System.out.println("Email ok? " + "bad@@mail".matches(emailRegex));

        // ===== 4) Validate Indian mobile (10 digits, starts 6-9) =====
        String phoneRegex = "^[6-9]\\d{9}$";
        System.out.println("Phone ok? " + "9876543210".matches(phoneRegex));
        System.out.println("Phone ok? " + "12345".matches(phoneRegex));

        // ===== 5) Find all matches inside text =====
        Pattern phonePattern = Pattern.compile("\\b[6-9]\\d{9}\\b");
        Matcher phoneMatcher = phonePattern.matcher(text);
        System.out.println("--- phones in text ---");
        while (phoneMatcher.find()) {
            System.out.println("Found phone: " + phoneMatcher.group());
        }

        Pattern emailPattern = Pattern.compile("[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}");
        Matcher emailMatcher = emailPattern.matcher(text);
        System.out.println("--- emails in text ---");
        while (emailMatcher.find()) {
            System.out.println("Found email: " + emailMatcher.group());
        }

        // ===== 6) Replace =====
        String masked = text.replaceAll("[6-9]\\d{9}", "XXXXXXXXXX");
        System.out.println("Masked: " + masked);

        // ===== 7) Split with regex =====
        String csv = "Asha,92,B";
        String[] parts = csv.split(",");
        System.out.println("Split: " + parts[0] + " | " + parts[1] + " | " + parts[2]);

        // Split on any whitespace
        String sentence = "Java   is   fun";
        String[] words = sentence.split("\\s+");
        System.out.println("Words: " + String.join("-", words));

        // ===== 8) Groups (capture parts) =====
        Pattern datePattern = Pattern.compile("(\\d{2})-(\\d{2})-(\\d{4})");
        Matcher dateMatcher = datePattern.matcher("Exam on 25-12-2026");
        if (dateMatcher.find()) {
            System.out.println("Full:  " + dateMatcher.group(0));
            System.out.println("Day:   " + dateMatcher.group(1));
            System.out.println("Month: " + dateMatcher.group(2));
            System.out.println("Year:  " + dateMatcher.group(3));
        }
    }
}
