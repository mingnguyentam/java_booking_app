import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:sqlite:booking.db";
    private static Connection connection = null;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("SQLite JDBC driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found: " + e.getMessage());
            System.err.println("Please ensure sqlite-jdbc.jar is in the classpath.");
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
                System.out.println("Database connection established.");
            }
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String createRoomsTable = "CREATE TABLE IF NOT EXISTS rooms (" +
                    "room_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "room_name TEXT NOT NULL, " +
                    "room_type TEXT NOT NULL, " +
                    "price_per_night REAL NOT NULL, " +
                    "is_available INTEGER DEFAULT 1" +
                    ")";

            String createBookingsTable = "CREATE TABLE IF NOT EXISTS bookings (" +
                    "booking_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "room_id INTEGER NOT NULL, " +
                    "customer_name TEXT NOT NULL, " +
                    "check_in_date TEXT NOT NULL, " +
                    "check_out_date TEXT NOT NULL, " +
                    "number_of_nights INTEGER NOT NULL, " +
                    "total_price REAL NOT NULL, " +
                    "FOREIGN KEY (room_id) REFERENCES rooms(room_id)" +
                    ")";

            stmt.execute(createRoomsTable);
            stmt.execute(createBookingsTable);

            System.out.println("Database tables initialized successfully.");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing database: " + e.getMessage());
        }
    }
}
