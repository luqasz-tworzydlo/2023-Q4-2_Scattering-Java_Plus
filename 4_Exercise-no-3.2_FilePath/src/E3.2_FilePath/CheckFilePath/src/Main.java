import java.io.File;

class CheckFilePath {
    public static void main(String[] args) {
        String filePath = "C:\\Users\\luqasz\\SQLite DB Browser 3.1\\SQLiteDatabaseBrowserPortable\\6_SQLiteNewDatabase.db";
        File dbFile = new File(filePath);
        if (dbFile.exists()) {
            System.out.println("The file exists. / Plik istnieje.");
        } else {
            System.out.println("The file does not exist. / Plik nie istnieje.");
        }
    }
}
