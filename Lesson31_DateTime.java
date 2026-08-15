// Lesson 31: Date & Time API (LocalDate, LocalDateTime)
// Compile: javac Lesson31_DateTime.java
// Run:     java Lesson31_DateTime

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Lesson31_DateTime {

    public static void main(String[] args) {

        // ===== Today / now =====
        LocalDate today = LocalDate.now();
        LocalTime nowTime = LocalTime.now();
        LocalDateTime now = LocalDateTime.now();

        System.out.println("Today: " + today);
        System.out.println("Time:  " + nowTime);
        System.out.println("Now:   " + now);

        // ===== Create specific dates =====
        LocalDate independence = LocalDate.of(1947, 8, 15);
        LocalDate birthday = LocalDate.of(2000, 5, 20);
        System.out.println("Independence Day: " + independence);

        // ===== Read parts =====
        System.out.println("Year:  " + today.getYear());
        System.out.println("Month: " + today.getMonth() + " (" + today.getMonthValue() + ")");
        System.out.println("Day:   " + today.getDayOfMonth());
        System.out.println("Weekday: " + today.getDayOfWeek());

        // ===== Add / subtract =====
        LocalDate nextWeek = today.plusDays(7);
        LocalDate lastMonth = today.minusMonths(1);
        System.out.println("Next week:  " + nextWeek);
        System.out.println("Last month: " + lastMonth);
        // LocalDate is immutable — today itself does not change

        // ===== Compare =====
        System.out.println("Is birthday before today? " + birthday.isBefore(today));
        System.out.println("Is independence after today? " + independence.isAfter(today));

        // ===== Difference =====
        long daysAlive = ChronoUnit.DAYS.between(birthday, today);
        Period age = Period.between(birthday, today);
        System.out.println("Days since birthday: " + daysAlive);
        System.out.println("Age: " + age.getYears() + " years, "
                + age.getMonths() + " months, "
                + age.getDays() + " days");

        // ===== Format / parse (display & read text) =====
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy");
        System.out.println("Formatted today: " + today.format(fmt));

        LocalDate parsed = LocalDate.parse("25 Dec 2025", fmt);
        System.out.println("Parsed: " + parsed);

        // ===== Useful check =====
        LocalDate exam = today.plusDays(10);
        long daysLeft = ChronoUnit.DAYS.between(today, exam);
        System.out.println("Exam in " + daysLeft + " days (" + exam + ")");
    }
}
