import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class RoomManager {

    public RoomManager() {
        addSampleRooms();
    }

    private void addSampleRooms() {
        Connection conn = DatabaseManager.getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM rooms")) {

            if (rs.next() && rs.getInt("count") == 0) {
                addRoomToDatabase("Deluxe Suite", "Suite", 150.0);
                addRoomToDatabase("Standard Room", "Standard", 80.0);
                addRoomToDatabase("Executive Room", "Executive", 120.0);
                System.out.println("Sample rooms added to database.");
            }
        } catch (SQLException e) {
            System.err.println("Error checking/adding sample rooms: " + e.getMessage());
        }
    }

    private void addRoomToDatabase(String name, String type, double price) {
        String sql = "INSERT INTO rooms (room_name, room_type, price_per_night, is_available) VALUES (?, ?, ?, 1)";
        Connection conn = DatabaseManager.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setDouble(3, price);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error adding room to database: " + e.getMessage());
        }
    }

    public void viewAllRooms() {
        String sql = "SELECT * FROM rooms";
        Connection conn = DatabaseManager.getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n========== ALL ROOMS ==========");
            boolean hasRooms = false;
            while (rs.next()) {
                hasRooms = true;
                Room room = new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_name"),
                        rs.getString("room_type"),
                        rs.getDouble("price_per_night")
                );
                room.setAvailable(rs.getInt("is_available") == 1);
                System.out.println(room);
            }
            if (!hasRooms) {
                System.out.println("No data available");
            }
            System.out.println("===============================");

        } catch (SQLException e) {
            System.err.println("Error viewing rooms: " + e.getMessage());
        }
    }

    public void addRoom(Scanner scanner) {
        System.out.println("\n========== ADD NEW ROOM ==========");
        System.out.print("Enter room name: ");
        String name = scanner.nextLine();

        System.out.print("Enter room type (Standard/Deluxe/Suite/Executive): ");
        String type = scanner.nextLine();

        System.out.print("Enter price per night: $");
        double price = scanner.nextDouble();
        scanner.nextLine();

        String sql = "INSERT INTO rooms (room_name, room_type, price_per_night, is_available) VALUES (?, ?, ?, 1)";
        Connection conn = DatabaseManager.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setDouble(3, price);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int roomId = rs.getInt(1);
                Room newRoom = new Room(roomId, name, type, price);
                System.out.println("\nRoom added successfully!");
                System.out.println(newRoom);
            }

        } catch (SQLException e) {
            System.err.println("Error adding room: " + e.getMessage());
        }
    }

    public void editRoom(Scanner scanner) {
        System.out.println("\n========== EDIT ROOM ==========");
        System.out.print("Enter room ID to edit: ");
        int roomId = scanner.nextInt();
        scanner.nextLine();

        Room room = findRoomById(roomId);
        if (room == null) {
            System.out.println("Room not found!");
            return;
        }

        if (!room.isAvailable()) {
            System.out.println("\n*** ERROR: Cannot edit this room! ***");
            System.out.println("This room is currently booked/rented.");
            System.out.println("You can only edit rooms that are available.");
            return;
        }

        System.out.println("Current room details:");
        System.out.println(room);

        System.out.print("\nEnter new room name (or press Enter to keep current): ");
        String name = scanner.nextLine();
        if (name.isEmpty()) {
            name = room.getRoomName();
        }

        System.out.print("Enter new room type (or press Enter to keep current): ");
        String type = scanner.nextLine();
        if (type.isEmpty()) {
            type = room.getRoomType();
        }

        System.out.print("Enter new price per night (or 0 to keep current): $");
        double price = scanner.nextDouble();
        scanner.nextLine();
        if (price == 0) {
            price = room.getPricePerNight();
        }

        String sql = "UPDATE rooms SET room_name = ?, room_type = ?, price_per_night = ? WHERE room_id = ?";
        Connection conn = DatabaseManager.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, type);
            pstmt.setDouble(3, price);
            pstmt.setInt(4, roomId);
            pstmt.executeUpdate();

            room.setRoomName(name);
            room.setRoomType(type);
            room.setPricePerNight(price);

            System.out.println("\nRoom updated successfully!");
            System.out.println(room);

        } catch (SQLException e) {
            System.err.println("Error updating room: " + e.getMessage());
        }
    }

    public void deleteRoom(Scanner scanner) {
        System.out.println("\n========== DELETE ROOM ==========");
        System.out.print("Enter room ID to delete: ");
        int roomId = scanner.nextInt();
        scanner.nextLine();

        Room room = findRoomById(roomId);
        if (room == null) {
            System.out.println("Room not found!");
            return;
        }

        if (!room.isAvailable()) {
            System.out.println("\n*** ERROR: Cannot delete this room! ***");
            System.out.println("This room is currently booked/rented.");
            System.out.println("You can only delete rooms that are available.");
            return;
        }

        System.out.println("Room to delete:");
        System.out.println(room);
        System.out.print("\nAre you sure you want to delete this room? (yes/no): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("yes")) {
            String sql = "DELETE FROM rooms WHERE room_id = ?";
            Connection conn = DatabaseManager.getConnection();
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, roomId);
                pstmt.executeUpdate();
                System.out.println("Room deleted successfully!");

            } catch (SQLException e) {
                System.err.println("Error deleting room: " + e.getMessage());
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    public Room findRoomById(int roomId) {
        String sql = "SELECT * FROM rooms WHERE room_id = ?";
        Connection conn = DatabaseManager.getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Room room = new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_name"),
                        rs.getString("room_type"),
                        rs.getDouble("price_per_night")
                );
                room.setAvailable(rs.getInt("is_available") == 1);
                return room;
            }

        } catch (SQLException e) {
            System.err.println("Error finding room: " + e.getMessage());
        }
        return null;
    }

    public ArrayList<Room> getRooms() {
        ArrayList<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        Connection conn = DatabaseManager.getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Room room = new Room(
                        rs.getInt("room_id"),
                        rs.getString("room_name"),
                        rs.getString("room_type"),
                        rs.getDouble("price_per_night")
                );
                room.setAvailable(rs.getInt("is_available") == 1);
                rooms.add(room);
            }

        } catch (SQLException e) {
            System.err.println("Error getting rooms: " + e.getMessage());
        }
        return rooms;
    }
}
