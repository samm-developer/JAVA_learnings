package lesson37;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Lesson 37 Mini Practice: also fetch /todos/5 and print title with Gson
 *
 * From folder lesson37-maven:
 *   mvn -q compile exec:java
 */
public class App {
    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        Gson gson = new Gson();

        Todo todo1 = fetchTodo(client, gson, 1);
        System.out.println("--- todo 1 ---");
        System.out.println(todo1);
        System.out.println("title: " + todo1.title);

        // Mini practice: fetch todo 5
        Todo todo5 = fetchTodo(client, gson, 5);
        System.out.println("--- todo 5 ---");
        System.out.println("title: " + todo5.title);
        System.out.println("completed: " + todo5.completed);
    }

    static Todo fetchTodo(HttpClient client, Gson gson, int id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/todos/" + id))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );

        System.out.println("Status for id=" + id + ": " + response.statusCode());
        return gson.fromJson(response.body(), Todo.class);
    }
}
