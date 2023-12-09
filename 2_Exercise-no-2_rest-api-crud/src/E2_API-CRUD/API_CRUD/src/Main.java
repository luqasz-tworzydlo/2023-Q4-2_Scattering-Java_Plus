import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

class JsonPlaceholderExample {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        // GET request to retrieve posts
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/posts"))
                .GET()
                .build();
        HttpResponse<String> getResponse = client.send(getRequest, BodyHandlers.ofString());
        System.out.println("GET Response: " + getResponse.body());

        // POST request to create a new post
        String newPostJson = "{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/posts"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(newPostJson))
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());
        System.out.println("POST Response: " + postResponse.body());

        // PUT request to update an existing post (id 1 for example)
        String updatedPostJson = "{\"id\": 1, \"title\": \"foo updated\", \"body\": \"bar updated\", \"userId\": 1}";
        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/posts/1"))
                .header("Content-Type", "application/json")
                .PUT(BodyPublishers.ofString(updatedPostJson))
                .build();
        HttpResponse<String> putResponse = client.send(putRequest, BodyHandlers.ofString());
        System.out.println("PUT Response: " + putResponse.body());

        // DELETE request to delete a post (id 1 for example)
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/posts/1"))
                .DELETE()
                .build();
        HttpResponse<String> deleteResponse = client.send(deleteRequest, BodyHandlers.ofString());
        System.out.println("DELETE Response: " + deleteResponse.body());
    }
}
