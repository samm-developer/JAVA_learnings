package lesson37;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Lesson 37: Maven project + Gson JSON parsing
 *
 * From folder lesson37-maven:
 *   mvn -q compile exec:java
 */
public class App {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/todos/1"))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        System.out.println("Status: " + response.statusCode());
        System.out.println("Raw JSON: " + response.body());

        // Gson turns JSON text → Java object
        Gson gson = new Gson();
        Todo todo = gson.fromJson(response.body(), Todo.class);

        System.out.println("--- parsed with Gson ---");
        System.out.println(todo);
        System.out.println("title: " + todo.title);
        System.out.println("completed: " + todo.completed);

        // Java object → JSON text
        String backToJson = gson.toJson(todo);
        System.out.println("Back to JSON: " + backToJson);
    }
}
