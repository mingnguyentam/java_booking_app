import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class BookingManager {
    private RoomManager roomManager;
    private String currentCustomerName;

    public BookingManager(RoomManager roomManager) {
        this.roomManager = roomManager;
        this.currentCustomerName = "Guest";
    }

    public void setCurrentCustomerName(String name) {
        this.currentCustomerName = name;
    }

    public void viewAvailableRooms() {
        System.out.println("\n========== AVAILABLE ROOMS ==========");
        boolean hasAvailable = false;
        for (Room room : roomManager.getRooms()) {
            if (room.isAvailable()) {
                System.out.println(room);
                hasAvailable = true;
            }
        }
        if (!hasAvailable) {
            System.out.println("No rooms available at the moment.");
        }
        System.out.println("====================================");
    }

    public void bookRoom(Scanner scanner) {
        System.out.println("\n========== BOOK A ROOM ==========");

        viewAvailableRooms();

        System.out.print("\nEnter your name: ");
        String customerName = scanner.nextLine();
        setCurrentCustomerName(customerName);

        System.out.print("Enter room ID to book: ");
        int roomId = scanner.nextInt();
        scanner.nextLine();

        Room room = roomManager.findRoomById(roomId);
        if (room == null) {
            System.out.println("Room not found!");
            return;
        }

        if (!room.isAvailable()) {
            System.out.println("Sorry, this room is already booked!");
            return;
        }

        System.out.print("Enter check-in date (e.g., 2024-01-01): ");
        String checkIn = scanner.nextLine();

        System.out.print("Enter check-out date (e.g., 2024-01-05): ");
        String checkOut = scanner.nextLine();

        System.out.print("Enter number of nights: ");
        int nights = scanner.nextInt();
        scanner.nextLine();

        double totalPrice = room.getPricePerNight() * nights;

        String insertBookingSql = "INSERT INTO bookings (room_id, customer_name, check_in_date, check_out_date, number_of_nights, total_price) VALUES (?, ?, ?, ?, ?, ?)";
        String updateRoomSql = "UPDATE rooms SET is_available = 0 WHERE room_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement bookingStmt = conn.prepareStatement(insertBookingSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement roomStmt = conn.prepareStatement(updateRoomSql)) {

            bookingStmt.setInt(1, roomId);
            bookingStmt.setString(2, customerName);
            bookingStmt.setString(3, checkIn);
            bookingStmt.setString(4, checkOut);
            bookingStmt.setInt(5, nights);
            bookingStmt.setDouble(6, totalPrice);
            bookingStmt.executeUpdate();

            ResultSet rs = bookingStmt.getGeneratedKeys();
            int bookingId = 0;
            if (rs.next()) {
                bookingId = rs.getInt(1);
            }

            roomStmt.setInt(1, roomId);
            roomStmt.executeUpdate();

            Booking booking = new Booking(bookingId, roomId, customerName, checkIn, checkOut, nights, totalPrice);

            System.out.println("\n========== BOOKING CONFIRMED ==========");
            System.out.println(booking);
            System.out.println("=======================================");

        } catch (SQLException e) {
            System.err.println("Error booking room: " + e.getMessage());
        }
    }

    public void viewMyBookings() {
        System.out.println("\n========== MY BOOKINGS ==========");
        String sql = "SELECT * FROM bookings WHERE customer_name = ?";
        boolean hasBookings = false;

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, currentCustomerName);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                hasBookings = true;
                Booking booking = new Booking(
                        rs.getInt("booking_id"),
                        rs.getInt("room_id"),
                        rs.getString("customer_name"),
                        rs.getString("check_in_date"),
                        rs.getString("check_out_date"),
                        rs.getInt("number_of_nights"),
                        rs.getDouble("total_price")
                );
                System.out.println(booking);

                Room room = roomManager.findRoomById(booking.getRoomId());
                if (room != null) {
                    System.out.println("  Room: " + room.getRoomName() + " (" + room.getRoomType() + ")");
                }
                System.out.println();
            }

            if (!hasBookings) {
                System.out.println("You have no bookings.");
            }

        } catch (SQLException e) {
            System.err.println("Error viewing bookings: " + e.getMessage());
        }

        System.out.println("=================================");
    }

    public ArrayList<Booking> getBookings() {
        ArrayList<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Booking booking = new Booking(
                        rs.getInt("booking_id"),
                        rs.getInt("room_id"),
                        rs.getString("customer_name"),
                        rs.getString("check_in_date"),
                        rs.getString("check_out_date"),
                        rs.getInt("number_of_nights"),
                        rs.getDouble("total_price")
                );
                bookings.add(booking);
            }

        } catch (SQLException e) {
            System.err.println("Error getting bookings: " + e.getMessage());
        }

        return bookings;
    }
}
