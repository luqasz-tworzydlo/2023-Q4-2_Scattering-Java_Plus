import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

class JSON_Placeholder_FakeAPI {
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    // {JSON} Placeholder Free fake API for testing and prototyping.
    // {JSON} Placeholder to strona z darmowym fałszywym API.
    // Link do strony to: https://jsonplaceholder.typicode.com/

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        // Metoda GET ( zapytanie o pobranie wpisu )
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/posts"))
                .GET()
                .build();
        HttpResponse<String> getResponse = client.send(getRequest, BodyHandlers.ofString());
        System.out.println("\n=> Odpowiedź dla GET: " + getResponse.body());
        System.out.println("\n/// /// /// /// /// /// /// /// ///");

        // Metoda POST ( zapytanie o stworzenie nowego wpisu )
        String NowyWpisJSON = "{\"title\": \"tytulTestowy\", \"body\": \"tekstTest\", \"userId\": 1}";
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/posts"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(NowyWpisJSON))
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());
        System.out.println("\n=> Odpowiedź dla POST: " + postResponse.body());
        System.out.println("\n/// /// /// /// /// /// /// /// ///");

        // Metoda PUT ( zapytanie o aktualizację istniejącego, wybranego wpisu )
        String ZaktualizowanyWpisJSON = "{\"id\": 1, \"title\": \"tytulTestowy zaktualizowany\", \"body\": \"tekstTest zaktualizowany\", \"userId\": 1}";
        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/posts/1"))
                .header("Content-Type", "application/json")
                .PUT(BodyPublishers.ofString(ZaktualizowanyWpisJSON))
                .build();
        HttpResponse<String> putResponse = client.send(putRequest, BodyHandlers.ofString());
        System.out.println("\n=> Odpowiedź dla PUT: " + putResponse.body());
        System.out.println("\n/// /// /// /// /// /// /// /// ///");

        // Metoda DELETE ( zapytanie o usunięcie wybranego, konkretnego wpisu )
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/posts/1"))
                .DELETE()
                .build();
        HttpResponse<String> deleteResponse = client.send(deleteRequest, BodyHandlers.ofString());
        System.out.println("\n=> Odpowiedź dla DELETE: " + deleteResponse.body());
        System.out.println("\n/// /// /// /// /// /// /// /// ///");
    }
}
