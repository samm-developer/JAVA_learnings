package lesson37;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Fetch todo 1 and todo 5 in parallel using threads.
 *
 * From lesson37-maven:
 *   mvn -q compile exec:java -Dexec.mainClass=lesson37.AppParallel
 */
public class AppParallel {
    public static void main(String[] args) throws InterruptedException {
        Gson gson = new Gson();

        AtomicReference<Todo> todo1Ref = new AtomicReference<>();
        AtomicReference<Todo> todo5Ref = new AtomicReference<>();
        AtomicReference<Exception> errorRef = new AtomicReference<>();

        long start = System.nanoTime();

        Thread t1 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " fetching todo 1...");
                todo1Ref.set(fetchTodo(gson, 1));
                System.out.println(Thread.currentThread().getName() + " done todo 1");
            } catch (Exception e) {
                errorRef.compareAndSet(null, e);
            }
        }, "Todo-1-Thread");

        Thread t2 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " fetching todo 5...");
                todo5Ref.set(fetchTodo(gson, 5));
                System.out.println(Thread.currentThread().getName() + " done todo 5");
            } catch (Exception e) {
                errorRef.compareAndSet(null, e);
            }
        }, "Todo-5-Thread");

        // Start both at the same time
        t1.start();
        t2.start();

        // Wait for both to finish
        t1.join();
        t2.join();

        double ms = (System.nanoTime() - start) / 1_000_000.0;

        if (errorRef.get() != null) {
            System.out.println("Request failed: " + errorRef.get().getMessage());
            return;
        }

        System.out.println("--- results ---");
        System.out.println("todo 1 title: " + todo1Ref.get().title);
        System.out.println("todo 5 title: " + todo5Ref.get().title);
        System.out.println("Time taken (parallel): " + ms + " ms");
    }

    static Todo fetchTodo(Gson gson, int id) throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/todos/" + id))
                .timeout(Duration.ofSeconds(20))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        if (response.statusCode() != 200) {
            throw new IllegalStateException("Bad status for id=" + id + ": " + response.statusCode());
        }
        return gson.fromJson(response.body(), Todo.class);
    }
}
