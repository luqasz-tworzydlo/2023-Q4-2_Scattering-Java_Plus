import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.io.IOException;
import java.util.Map;

//////////////////////////////////////////////////
//
// => Imię i nazwisko: Łukasz Tworzydło
// => Numer albumu: gd29623
// => Nr. kierunku: INIS5_PR2
// => Przedmiot: Przetwarzanie rozproszone [laboratoria]
//
//////////////////////////////////////////////////
//
// Łukasz Tworzydło - nr albumu: gd29623 [zadanie nr 4]
//
//////////////////////////////////////////////////
// wykorzystano JSON Placeholder API i 6 bibliotek:
// (1) gson-2.10.1.jar
// (2) java-net-http-request-factory-1.0.1.jar
// (3) spring-beans-5.2.4.RELEASE.jar
// (4) spring-core-5.2.4.RELEASE
// (5) spring-jcl-5.2.4.RELEASE
// (6) spring-web-5.2.4.RELEASE.jar
//////////////////////////////////////////////////

class JSON_Placeholder_Client {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/posts";
    // {JSON} Placeholder Free fake API for testing and prototyping.
    // {JSON} Placeholder to strona z darmowym fałszywym API.
    // Link do strony to: https://jsonplaceholder.typicode.com/
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        try {
            // wykonaj metodę GET (GET request) aby wyświetlić wszystkie wpisy
            System.out.println("\nWykonywanie metody GET request dla wszystkich wpisów (posts)...");
            String getResponse = GET_pobierzWpisy();
            System.out.println(getResponse);

            // wykonaj metodę GETbyId (GETbyId request) aby wyświetlić jeden wybrany wpis
            System.out.println("\nWykonywanie metody GET request dla wybranego wpisu (id 7)...");
            String postDetails = GETbyId_pobierzWybranyWpis(7);
            System.out.println(postDetails);

            // wykonaj metodę POST (POST request) aby stworzyć nowy wpis
            System.out.println("\nWykonywanie metody POST request w celu stworzenia wpisu...");
            Post newPost = new Post(101, "JakisTamTytul", "Zawartosc_JakisTamTeks", 12);
            String postResponse = POST_stworzNowyWpis(newPost);
            System.out.println(postResponse);

            // wykonaj metodę PUT (PUT request) aby zaktualizować wybrany wpis
            System.out.println("\nWykonywanie metody PUT request w celu aktualizacji wpisu...");
            Post updatedPost = new Post(1, "ZaktualizowanyTytul", "ZaktualizowanyTekst", 3);
            String putResponse = PUT_zaktualizujWybranyWpis(updatedPost);
            System.out.println(putResponse);

            // wykonaj metodę PATCH (PATCH request) aby zaktualizować częściowo wybrany wpis (przykładowo tytuł)
            System.out.println("\nWykonywanie metody PATCH request w celu wykonania częściowej aktualizacji wpisu...");
            try {
                String patchResponse = PATCH_aktualizujCzesciowoWybranyWpis(3, "Aktualizacja_JakisNowyTytul");
                System.out.println(patchResponse);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            // wykonaj metodę DELETE (DELETE request) aby usunąć wybrany wpis
            System.out.println("\nWykonywanie metody DELETE request w celu usuniecia wybranego wpisu...");
            String deleteResponse = DELETE_usunWybranyWpis(2);
            System.out.println(deleteResponse);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // nadanie odpowiedniego formatu dla CRUD, dla metod GET (all), GET (Id), POST, PUT, PATCH, DELETE
    private static String formatWyswietleniaDanych(HttpResponse<String> response) {
        int statusCode = response.statusCode();
        String body = response.body();
        String statusText;

        // określ tekst status na podstawie kodu zwrotnego
        switch (statusCode) {
            case 200: statusText = "OK"; break;
            case 204: statusText = "No Content"; break;
            default: statusText = "Error"; break;
        }

        // określ tekst dla danych na podstawie stanu kodu oraz dostępnej treści
        String data;
        if (statusCode == 204 || (body != null && (body.trim().isEmpty() || "{}".equals(body.trim())))) {
            data = "No Content";
        } else {
            data = body;
        }

        return String.format("Status: %d, StatusText: %s, Data: %s", statusCode, statusText, data);
    }

    // GET => pobranie wpisów z API i nadanie odpowiedniego formatu (dla odpowiedzi)
    private static String GET_pobierzWpisy() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, BodyHandlers.ofString());
        return formatWyswietleniaDanych(getResponse);
    }

    // GET (by Id) => pobranie wybranego wpisu z API i nadanie odpowiedniego formatu (dla odpowiedzi)
    private static String GETbyId_pobierzWybranyWpis(int postId) throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + postId))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, BodyHandlers.ofString());
        return formatWyswietleniaDanych(getResponse);
    }

    // POST => stworzenie oraz zapisanie nowego wpisu i nadanie odpowiedniego formatu (dla odpowiedzi)
    private static String POST_stworzNowyWpis(Post post) throws IOException, InterruptedException {
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(gson.toJson(post)))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, BodyHandlers.ofString());
        return formatWyswietleniaDanych(postResponse);
    }

    // PUT => wykonanie aktualizacji wybranego wpisu i nadanie odpowiedniego formatu (dla odpowiedzi)
    private static String PUT_zaktualizujWybranyWpis(Post post) throws IOException, InterruptedException {
        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + post.getId()))
                .header("Content-Type", "application/json")
                .PUT(BodyPublishers.ofString(gson.toJson(post)))
                .build();

        HttpResponse<String> putResponse = client.send(putRequest, BodyHandlers.ofString());
        return formatWyswietleniaDanych(putResponse);
    }

    // PATCH => wykonanie częściowej aktualizacji wybranego wpisu (aktualizacja tytułu) i nadanie odpowiedniego formatu (dla odpowiedzi)
    private static String PATCH_aktualizujCzesciowoWybranyWpis(int postId, String newTitle) throws IOException, InterruptedException {
        // Create a JSON object with the field you want to patch
        String jsonPatch = gson.toJson(Map.of("title", newTitle));

        HttpRequest patchRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + postId))
                .header("Content-Type", "application/json")
                .method("PATCH", BodyPublishers.ofString(jsonPatch)) // Use method to specify PATCH
                .build();

        HttpResponse<String> patchResponse = client.send(patchRequest, BodyHandlers.ofString());
        return formatWyswietleniaDanych(patchResponse);
    }

    // DELETE => wykonanie operacji usunięcia wybranego wpisu i nadanie odpowiedniego formatu (dla odpowiedzi)
    private static String DELETE_usunWybranyWpis(int postId) throws IOException, InterruptedException {
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + postId))
                .DELETE()
                .build();

        HttpResponse<String> deleteResponse = client.send(deleteRequest, BodyHandlers.ofString());

        System.out.println("Raw Status Code: " + deleteResponse.statusCode());
        System.out.println("Raw Body: '" + deleteResponse.body() + "'");

        return formatWyswietleniaDanych(deleteResponse);
    }

    static class Post {
        int id;
        String title;
        String body;
        int userId;

        Post(int id, String title, String body, int userId) {
            this.id = id;
            this.title = title;
            this.body = body;
            this.userId = userId;
        }

        // poniżej jest tzw. getter
        // (dla id, title, body, userId)
        public int getId() {
            return this.id;
        }
        public String getTitle() {
            return this.title;
        }
        public String getBody() {
            return this.body;
        }
        public int getUserId() {
            return this.userId;
        }
    }
}
