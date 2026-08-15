// File location: lesson19/app/LibraryApp.java
package lesson19.app;

// Import classes from another package
import lesson19.model.Book;

public class LibraryApp {
    public static void main(String[] args) {
        Book b1 = new Book("Clean Code", "Robert Martin");
        Book b2 = new Book("Effective Java", "Joshua Bloch");

        System.out.println("=== Mini Library ===");
        System.out.println("1. " + b1);
        System.out.println("2. " + b2);
        System.out.println("Title of first book: " + b1.getTitle());
    }
}
