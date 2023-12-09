import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.sql.*;
import java.util.List;
import java.net.http.*;
import java.net.URI;
import java.io.IOException;

class JSON_Placeholder_FakeAPI_SQLite_DB {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    // {JSON} Placeholder Free fake API for testing and prototyping.
    // {JSON} Placeholder to strona z darmowym fałszywym API.
    // Link do strony to: https://jsonplaceholder.typicode.com/

    private static final String DATABASE_URL = "jdbc:sqlite:C:\\Users\\luqasz\\SQLite DB Browser 3.1\\SQLiteDatabaseBrowserPortable\\6_SQLiteNewDatabase.db";
    // odnośnik, ścieżka do naszej bazy danych SQLite, do której będą zapisywane dane z API
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) {
        try {
            List<Post> wpisy = pobierzWpisyOdAPI();
            zapiszWpisyDoBazyDanych(wpisy);
        } catch (IOException | InterruptedException e) {
            System.err.println("=> Wystąpił błąd podczas zapytania HTTP: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("=> Wystąpił błąd w Bazie Danych: " + e.getMessage());
        }
    }

    // pobranie wpisów z API i ich przekształcenie (parsowanie)
    private static List<Post> pobierzWpisyOdAPI() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/posts"))
                .GET()
                .build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        return parseJsonToPosts(getResponse.body());
    }

    // przekształcanie danych z JSON
    private static List<Post> parseJsonToPosts(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<Post>>(){}.getType());
    }

    // połączenie z bazą danych
    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    private static void zapiszWpisyDoBazyDanych(List<Post> wpisy) throws SQLException {
        // wstawienie nowego wpisu do tabeli wpisy (jeśli wpis z takim samym id już istnieje, wykonaj aktualizację)
        String sql = "INSERT INTO wpisy(id, title, body, userId) VALUES(?,?,?,?) ON CONFLICT(id) DO UPDATE SET title=excluded.title, body=excluded.body, userId=excluded.userId";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Post post : wpisy) {
                pstmt.setInt(1, post.getId());
                pstmt.setString(2, post.getTitle());
                pstmt.setString(3, post.getBody());
                pstmt.setInt(4, post.getUserId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("=> Wystąpił błąd podczas zapisywania wpisów do bazy danych: " + e.getMessage());
            throw e;
        }
    }

    // klasa Post (odnosząca się do tworzenia nowych wpisów)
    public static class Post {
        private int id;
        private String title;
        private String body;
        private int userId;

        public Post() {
            // konstruktor bez argumentów jest wymagany dla Gson'a
        }

        public Post(int id, String title, String body, int userId) {
            this.id = id;
            this.title = title;
            this.body = body;
            this.userId = userId;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getBody() {
            return body;
        }

        public int getUserId() {
            return userId;
        }
    }
}
