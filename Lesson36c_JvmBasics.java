// Lesson 36c: JVM basics (heap, stack, GC, classpath) — concepts + tiny demos
// Compile: javac Lesson36c_JvmBasics.java
// Run:     java Lesson36c_JvmBasics
// Extra:   java -Xmx64m Lesson36c_JvmBasics
//
// Place: end of core Java, before Maven/Spring (Lesson 37+).

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class Lesson36c_JvmBasics {
    public static void main(String[] args) {
        System.out.println("=== How a Java program runs ===");
        System.out.println("1) javac File.java  → File.class (bytecode)");
        System.out.println("2) java File        → JVM loads bytecode and runs it");
        System.out.println();

        System.out.println("=== Memory (simplified) ===");
        System.out.println("STACK  : method calls, local primitives, references");
        System.out.println("HEAP   : objects created with 'new' (shared)");
        System.out.println("GC     : deletes unreachable heap objects automatically");
        System.out.println();

        // Local variable 'x' lives on the STACK frame of main
        int x = 10;
        // The Integer object lives on the HEAP; 'box' on the stack holds a reference
        Integer box = Integer.valueOf(x);
        System.out.println("stack int x=" + x + ", heap Integer=" + box);

        System.out.println();
        System.out.println("=== Runtime info ===");
        Runtime rt = Runtime.getRuntime();
        System.out.println("Java version : " + System.getProperty("java.version"));
        System.out.println("Max heap     : " + (rt.maxMemory() / (1024 * 1024)) + " MB");
        System.out.println("Total heap   : " + (rt.totalMemory() / (1024 * 1024)) + " MB");
        System.out.println("Free heap    : " + (rt.freeMemory() / (1024 * 1024)) + " MB");
        System.out.println("Processors   : " + rt.availableProcessors());
        System.out.println("JVM name     : " + ManagementFactory.getRuntimeMXBean().getVmName());

        System.out.println();
        System.out.println("=== Classpath idea ===");
        System.out.println("JVM finds .class / jars using classpath.");
        System.out.println("Example: java -cp out:lib/app.jar com.demo.Main");
        System.out.println("Maven/Gradle set this for you.");

        System.out.println();
        System.out.println("=== GC demo (objects become unreachable) ===");
        List<byte[]> junk = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            junk.add(new byte[1_000_000]); // ~1MB each on heap
        }
        System.out.println("Allocated ~5MB worth of arrays; free now ~"
                + (rt.freeMemory() / (1024 * 1024)) + " MB");
        junk = null; // drop reference → GC may reclaim later
        System.gc(); // only a HINT to the JVM (not guaranteed immediate)
        System.out.println("After null + System.gc() hint; free ~"
                + (rt.freeMemory() / (1024 * 1024)) + " MB");

        System.out.println();
        System.out.println("Useful flags: -Xms64m -Xmx256m  (min/max heap)");
    }
}
