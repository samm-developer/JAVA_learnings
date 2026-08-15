// Lesson 35: HTTP POST (send data to an API)
// Compile: javac Lesson35_HttpPost.java
// Run:     java Lesson35_HttpPost
// Needs internet access.

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class Lesson35_HttpPost {
    public static void main(String[] args) {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        // JSON body we will send
        String jsonBody = """
                {
                  "title": "Learn Java HttpClient",
                  "body": "POST example from Lesson 35",
                  "userId": 1
                }
                """;

        // Build POST request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
                .timeout(Duration.ofSeconds(10))
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("Status code: " + response.statusCode());
            // 201 Created is common for successful POST on this API
            System.out.println("Response body:");
            System.out.println(response.body());

            System.out.println("---");
            System.out.println("id: " + extractNumber(response.body(), "id"));
            System.out.println("title: " + extractJsonString(response.body(), "title"));

            // Compare: GET vs POST
            System.out.println();
            System.out.println("GET  = ask for data");
            System.out.println("POST = send data to create/submit something");

        } catch (Exception e) {
            System.out.println("POST failed: " + e.getMessage());
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

    static String extractNumber(String json, String key) {
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
