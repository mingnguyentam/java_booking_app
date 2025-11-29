import java.util.Scanner;

public class App {
    private static RoomManager roomManager;
    private static BookingManager bookingManager;
    private static Scanner scanner;

    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();

        roomManager = new RoomManager();
        bookingManager = new BookingManager(roomManager);
        scanner = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println("   WELCOME TO ROOM BOOKING SYSTEM");
        System.out.println("========================================");

        mainMenu();
        scanner.close();
        DatabaseManager.closeConnection();
    }

    private static void mainMenu() {
        while (true) {
            System.out.println("\n========== MAIN MENU ==========");
            System.out.println("1. Manage Room");
            System.out.println("2. Booking Room");
            System.out.println("0. Exit");
            System.out.println("===============================");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    manageRoomMenu();
                    break;
                case 2:
                    bookingRoomMenu();
                    break;
                case 0:
                    System.out.println("\nThank you for using Room Booking System. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private static void manageRoomMenu() {
        while (true) {
            System.out.println("\n========== MANAGE ROOM MENU ==========");
            System.out.println("1. View List Room");
            System.out.println("2. Add Room");
            System.out.println("3. Edit Room");
            System.out.println("4. Delete Room");
            System.out.println("0. Back to Main Menu");
            System.out.println("======================================");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    roomManager.viewAllRooms();
                    break;
                case 2:
                    roomManager.addRoom(scanner);
                    break;
                case 3:
                    roomManager.editRoom(scanner);
                    break;
                case 4:
                    roomManager.deleteRoom(scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private static void bookingRoomMenu() {
        while (true) {
            System.out.println("\n========== BOOKING ROOM MENU ==========");
            System.out.println("1. View Room Available");
            System.out.println("2. Booking Room");
            System.out.println("3. Show My Room");
            System.out.println("0. Back to Main Menu");
            System.out.println("=======================================");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    bookingManager.viewAvailableRooms();
                    break;
                case 2:
                    bookingManager.bookRoom(scanner);
                    break;
                case 3:
                    bookingManager.viewMyBookings();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }
}
