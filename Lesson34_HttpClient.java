// Lesson 34: HttpClient (call a web API)
// Compile: javac Lesson34_HttpClient.java
// Run:     java Lesson34_HttpClient
// Needs internet access.

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class Lesson34_HttpClient {
    public static void main(String[] args) {
        // 1) Create a reusable client
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        // 2) Build a GET request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/todos/1"))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            // 3) Send and get response body as String
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("Status code: " + response.statusCode());
            System.out.println("Body:");
            System.out.println(response.body());

            // Tiny parse without a JSON library (just for learning)
            String body = response.body();
            String title = extractJsonString(body, "title");
            String completed = extractJsonRaw(body, "completed");
            System.out.println("--- parsed ---");
            System.out.println("title: " + title);
            System.out.println("completed: " + completed);

            // 4) Another GET — list users (first item only shown briefly)
            HttpRequest usersReq = HttpRequest.newBuilder()
                    .uri(URI.create("https://jsonplaceholder.typicode.com/users/1"))
                    .GET()
                    .build();

            HttpResponse<String> usersRes = client.send(
                    usersReq,
                    HttpResponse.BodyHandlers.ofString()
            );
            System.out.println("--- user 1 ---");
            System.out.println("Status: " + usersRes.statusCode());
            System.out.println("name: " + extractJsonString(usersRes.body(), "name"));
            System.out.println("email: " + extractJsonString(usersRes.body(), "email"));

        } catch (Exception e) {
            System.out.println("Request failed: " + e.getMessage());
            System.out.println("Check your internet connection.");
        }
    }

    // Very small helpers — real apps use a JSON library (Jackson/Gson)
    static String extractJsonString(String json, String key) {
        String needle = "\"" + key + "\": \"";
        int start = json.indexOf(needle);
        if (start < 0) {
            needle = "\"" + key + "\":\""; // no space variant
            start = json.indexOf(needle);
        }
        if (start < 0) return "(not found)";
        start += needle.length();
        int end = json.indexOf('"', start);
        return json.substring(start, end);
    }

    static String extractJsonRaw(String json, String key) {
        String needle = "\"" + key + "\": ";
        int start = json.indexOf(needle);
        if (start < 0) {
            needle = "\"" + key + "\":";
            start = json.indexOf(needle);
        }
        if (start < 0) return "(not found)";
        start += needle.length();
        int end = start;
        while (end < json.length() && ",}".indexOf(json.charAt(end)) == -1) {
            end++;
        }
        return json.substring(start, end).trim();
    }
}
