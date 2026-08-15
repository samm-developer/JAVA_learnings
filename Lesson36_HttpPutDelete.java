// Lesson 36: HTTP PUT & DELETE
// Compile: javac Lesson36_HttpPutDelete.java
// Run:     java Lesson36_HttpPutDelete
// Needs internet access.

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class Lesson36_HttpPutDelete {
    public static void main(String[] args) {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        try {
            // ===== PUT: update an existing resource =====
            // Update post id=1
            String updateJson = """
                    {
                      "id": 1,
                      "title": "Updated by Lesson 36",
                      "body": "This replaces the old post content",
                      "userId": 1
                    }
                    """;

            HttpRequest putRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .PUT(HttpRequest.BodyPublishers.ofString(updateJson, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> putResponse = client.send(
                    putRequest,
                    HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("=== PUT (update) ===");
            System.out.println("Status: " + putResponse.statusCode()); // often 200
            System.out.println("Body:   " + putResponse.body());
            System.out.println("title:  " + extractJsonString(putResponse.body(), "title"));

            // ===== DELETE: remove a resource =====
            HttpRequest deleteRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/posts/1"))
                    .timeout(Duration.ofSeconds(10))
                    .DELETE()
                    .build();

            HttpResponse<String> deleteResponse = client.send(
                    deleteRequest,
                    HttpResponse.BodyHandlers.ofString()
            );

            System.out.println();
            System.out.println("=== DELETE ===");
            System.out.println("Status: " + deleteResponse.statusCode()); // often 200
            System.out.println("Body:   " + deleteResponse.body()); // usually {}

            // ===== Cheat sheet =====
            System.out.println();
            System.out.println("=== HTTP methods ===");
            System.out.println("GET    → read");
            System.out.println("POST   → create");
            System.out.println("PUT    → update/replace");
            System.out.println("DELETE → remove");

        } catch (Exception e) {
            System.out.println("Request failed: " + e.getMessage());
        }
    }

    static String extractJsonString(String json, String key) {
        String needle = "\"" + key + "\": \"";
        int start = json.indexOf(needle);
        if (start < 0) {
            needle = "\"" + key + "\":\"";
            start = json.indexOf(needle);
        }
        if (start < 0) return "(not found)";
        start += needle.length();
        int end = json.indexOf('"', start);
        return json.substring(start, end);
    }
}
