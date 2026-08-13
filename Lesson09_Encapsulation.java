// Lesson 9: Encapsulation (hide data, control access)
// Compile: javac Lesson09_Encapsulation.java
// Run:     java Lesson09_Encapsulation

class BankAccount {
    // private = only THIS class can touch these fields directly
    private String owner;
    private double balance;

    public BankAccount(String owner, double openingBalance) {
        this.owner = owner;
        // use setter logic so invalid values are blocked
        setBalance(openingBalance);
    }

    // ===== Getters (read) =====
    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }

    // ===== Setters (write, with rules) =====
    public void setOwner(String owner) {
        if (owner == null || owner.isBlank()) {
            System.out.println("Owner name cannot be empty.");
            return;
        }
        this.owner = owner;
    }

    private void setBalance(double balance) {
        if (balance < 0) {
            System.out.println("Balance cannot be negative. Setting 0.");
            this.balance = 0;
            return;
        }
        this.balance = balance;
    }

    // Safer than letting outsiders do account.balance = ...
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Deposit must be positive.");
            return;
        }
        balance += amount;
        System.out.println("Deposited " + amount + ". Balance: " + balance);
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdraw must be positive.");
            return;
        }
        if (amount > balance) {
            System.out.println("Insufficient funds!");
            return;
        }
        balance -= amount;
        System.out.println("Withdrew " + amount + ". Balance: " + balance);
    }

    public void display() {
        System.out.println(owner + " | Balance: " + balance);
    }
}

public class Lesson09_Encapsulation {
    public static void main(String[] args) {
        BankAccount acc = new BankAccount("Shashwat", 1000);

        acc.display();

        // Allowed: controlled methods
        acc.deposit(500);
        acc.withdraw(200);
        acc.withdraw(5000); // blocked by rule

        // Read via getter
        System.out.println("Current balance: " + acc.getBalance());

        acc.setOwner("Shashwat M.");
        acc.display();

        // Not allowed (would not compile if uncommented):
        // acc.balance = 999999;
        // System.out.println(acc.balance);

        BankAccount bad = new BankAccount("Test", -50); // opening balance fixed to 0
        bad.display();
    }
}
