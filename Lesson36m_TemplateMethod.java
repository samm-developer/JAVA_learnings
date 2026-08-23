// Lesson 36m: Template Method pattern (Behavioral)
// Compile: javac LessonConsole.java Lesson36m_TemplateMethod.java
// Run:     java Lesson36m_TemplateMethod
//
// After: Lesson 36l (Observer)
//
// ========== CHEAT SHEET ==========
// Problem:  PDF and HTML reports duplicate fetch/export steps — only format differs
// Solution: base class defines generate() skeleton; subclasses override hook methods
// Spring:   JdbcTemplate (fixed query flow, you supply RowMapper callback)

public class Lesson36m_TemplateMethod {

    public static void main(String[] args) {
        problem();
        solution();
        summary();
    }

    static void problem() {
        LessonConsole.heading("=== PROBLEM: duplicated steps in each report class ===");
        new BadPdfReport().generate();
        new BadHtmlReport().generate();
        System.out.println("  fetch + export copied in both classes ❌");
        System.out.println();
    }

    static void solution() {
        LessonConsole.heading("=== SOLUTION: Template Method — skeleton in base, hooks in subclasses ===");
        Report pdf = new PdfReport();
        Report html = new HtmlReport();
        pdf.generate();
        html.generate();
        System.out.println("  ✅ shared steps live once in Report.generate()");
        System.out.println();
    }

    static void summary() {
        LessonConsole.heading("=== Summary: Template Method ===");
        System.out.println("""
                When:     algorithm steps are same, only some steps differ
                How:      final generate() in base calls abstract format()/export()
                Hook:     optional override like fetchData() with default body
                Next:     Lesson 36n Facade
                """);
    }

    // --- PROBLEM: copy-paste workflow ---
    static class BadPdfReport {
        void generate() {
            System.out.println("  [pdf] fetch data");
            System.out.println("  [pdf] format as PDF");
            System.out.println("  [pdf] export file.pdf");
        }
    }

    static class BadHtmlReport {
        void generate() {
            System.out.println("  [html] fetch data");
            System.out.println("  [html] format as HTML");
            System.out.println("  [html] export index.html");
        }
    }

    // --- SOLUTION ---
    static abstract class Report {
        final void generate() {
            fetchData();
            format();
            export();
        }

        void fetchData() {
            System.out.println("  [template] fetch data");
        }

        abstract void format();

        abstract void export();
    }

    static class PdfReport extends Report {
        void format() {
            System.out.println("  [pdf] format as PDF");
        }

        void export() {
            System.out.println("  [pdf] export file.pdf");
        }
    }

    static class HtmlReport extends Report {
        void format() {
            System.out.println("  [html] format as HTML");
        }

        void export() {
            System.out.println("  [html] export index.html");
        }
    }
}
