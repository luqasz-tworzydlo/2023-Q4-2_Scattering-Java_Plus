import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.sql.*;
import java.util.List;
import java.net.http.*;
import java.net.URI;
import java.io.IOException;

//////////////////////////////////////////////////
//
// => Imię i nazwisko: Łukasz Tworzydło
// => Numer albumu: gd29623
// => Nr. kierunku: INIS5_PR2
// => Przedmiot: Przetwarzanie rozproszone [laboratoria]
//
//////////////////////////////////////////////////
//
// Łukasz Tworzydło - nr albumu: gd29623 [zadanie nr 3]
//
//////////////////////////////////////////////////
// wykorzystano JSON Placeholder API,
// SQLite DB oraz 4 biblioteki:
// (1) sqlite-jdbc-3.44.1.0.jar
// (2) gson-2.10.1.jar
// (3) slf4j-api-2.0.9.jar
// (4) slf4j-simple-2.0.9.jar
//////////////////////////////////////////////////

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

            Post wpisDoAktualizacji = new Post(1, "TytułZaktualizowany", "TekstZaktualizowany", 95);
            aktualizujWpisWbazieDanych(wpisDoAktualizacji);

            usunWpisZbazyDanych(2);

        } catch (IOException | InterruptedException e) {
            System.err.println("=> Wystąpił błąd podczas zapytania HTTP: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("=> Wystąpił błąd w Bazie Danych: " + e.getMessage());
        }
    }

    // GET => pobranie wpisów z API i ich przekształcenie (parsowanie)
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
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver (nie znaleziono sterownika): " + e.getMessage());
            throw new SQLException("SQLite JDBC driver (nie naleziono sterownika)", e);
        }
        return DriverManager.getConnection(DATABASE_URL);
    }

    // POST => zapisanie wpisów z lokalnej bazie danych SQLite
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

    public static class Post {
        private int id;
        private String title;
        private String body;
        private int userId;

        public Post() {}

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

    // PUT => aktualizacja wpiszu / wpisów w lokalnej bazie danych SQLite
    private static void aktualizujWpisWbazieDanych(Post post) throws SQLException {
        String sql = "UPDATE wpisy SET title = ?, body = ?, userId = ? WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, post.getTitle());
            pstmt.setString(2, post.getBody());
            pstmt.setInt(3, post.getUserId());
            pstmt.setInt(4, post.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("=> Wystąpił błąd podczas aktualizacji wpisu w bazie danych: " + e.getMessage());
            throw e;
        }
    }

    // DELETE - usunięcie wpisu / wpisów z lokalnej bazy danych SQLite
    private static void usunWpisZbazyDanych(int postId) throws SQLException {
        String sql = "DELETE FROM wpisy WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, postId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("=> Wystąpił błąd podczas usuwania wpisu z bazy danych: " + e.getMessage());
            throw e;
        }
    }
}
