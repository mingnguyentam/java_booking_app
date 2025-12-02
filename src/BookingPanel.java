import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BookingPanel extends JPanel {
    private BookingManager bookingManager;
    private RoomManager roomManager;
    private JTable availableRoomsTable;
    private JTable myBookingsTable;
    private DefaultTableModel availableRoomsModel;
    private DefaultTableModel myBookingsModel;
    private String currentCustomerName = "Guest";
    private JLabel customerLabel;

    public BookingPanel(BookingManager bookingManager, RoomManager roomManager) {
        this.bookingManager = bookingManager;
        this.roomManager = roomManager;
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initializeComponents();
    }

    private void initializeComponents() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Booking Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        customerLabel = new JLabel("Customer: " + currentCustomerName);
        customerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        customerLabel.setForeground(new Color(52, 73, 94));
        headerPanel.add(customerLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel availableRoomsPanel = createAvailableRoomsPanel();
        JPanel myBookingsPanel = createMyBookingsPanel();

        tabbedPane.addTab("Available Rooms", availableRoomsPanel);
        tabbedPane.addTab("My Bookings", myBookingsPanel);

        add(tabbedPane, BorderLayout.CENTER);

        refreshTables();
    }

    private JPanel createAvailableRoomsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        String[] columns = {"Room ID", "Room Name", "Room Type", "Price/Night"};
        availableRoomsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        availableRoomsTable = new JTable(availableRoomsModel);
        availableRoomsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        availableRoomsTable.setRowHeight(30);
        availableRoomsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        availableRoomsTable.getTableHeader().setBackground(new Color(46, 204, 113));
        availableRoomsTable.getTableHeader().setReorderingAllowed(false);
        availableRoomsTable.getTableHeader().setResizingAllowed(false);
        availableRoomsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(availableRoomsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton bookButton = createStyledButton("Book Room", new Color(52, 152, 219));
        JButton refreshButton = createStyledButton("Refresh", new Color(149, 165, 166));
        JButton sortLowToHighButton = createStyledButton("Price: Low to High", new Color(95, 106, 196));
        JButton sortHighToLowButton = createStyledButton("Price: High to Low", new Color(142, 68, 173));

        bookButton.addActionListener(e -> bookRoom());
        refreshButton.addActionListener(e -> refreshAvailableRooms());
        sortLowToHighButton.addActionListener(e -> sortAvailableRoomsByPrice(true));
        sortHighToLowButton.addActionListener(e -> sortAvailableRoomsByPrice(false));

        buttonPanel.add(bookButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(sortLowToHighButton);
        buttonPanel.add(sortHighToLowButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMyBookingsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);

        String[] columns = {"Booking ID", "Room ID", "Room Name", "Check-in", "Check-out", "Nights", "Total Price"};
        myBookingsModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        myBookingsTable = new JTable(myBookingsModel);
        myBookingsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        myBookingsTable.setRowHeight(30);
        myBookingsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        myBookingsTable.getTableHeader().setBackground(new Color(155, 89, 182));
        myBookingsTable.getTableHeader().setReorderingAllowed(false);
        myBookingsTable.getTableHeader().setResizingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(myBookingsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton changeCustomerButton = createStyledButton("Change Customer", new Color(52, 152, 219));
        JButton refreshButton = createStyledButton("Refresh", new Color(149, 165, 166));

        changeCustomerButton.addActionListener(e -> changeCustomer());
        refreshButton.addActionListener(e -> refreshMyBookings());

        buttonPanel.add(changeCustomerButton);
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    public void refreshTables() {
        refreshAvailableRooms();
        refreshMyBookings();
    }

    private void refreshAvailableRooms() {
        availableRoomsModel.setRowCount(0);
        ArrayList<Room> rooms = roomManager.getRooms();
        for (Room room : rooms) {
            if (room.isAvailable()) {
                Object[] row = {
                    room.getRoomId(),
                    room.getRoomName(),
                    room.getRoomType(),
                    String.format("%.0f VND", room.getPricePerNight())
                };
                availableRoomsModel.addRow(row);
            }
        }
    }

    private void refreshMyBookings() {
        myBookingsModel.setRowCount(0);
        String sql = "SELECT * FROM bookings WHERE customer_name = ?";
        java.sql.Connection conn = DatabaseManager.getConnection();

        try (java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, currentCustomerName);

            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int roomId = rs.getInt("room_id");
                    Room room = roomManager.findRoomById(roomId);
                    String roomName = room != null ? room.getRoomName() : "Unknown";

                    Object[] row = {
                        rs.getInt("booking_id"),
                        roomId,
                        roomName,
                        rs.getString("check_in_date"),
                        rs.getString("check_out_date"),
                        rs.getInt("number_of_nights"),
                        String.format("%.0f VND", rs.getDouble("total_price"))
                    };
                    myBookingsModel.addRow(row);
                }
            }

        } catch (java.sql.SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void bookRoom() {
        int selectedRow = availableRoomsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to book", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int roomId = (int) availableRoomsModel.getValueAt(selectedRow, 0);
        Room room = roomManager.findRoomById(roomId);

        if (room == null || !room.isAvailable()) {
            JOptionPane.showMessageDialog(this, "Room is not available!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        BookingDialog dialog = new BookingDialog((Frame) SwingUtilities.getWindowAncestor(this), room, currentCustomerName);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            String customerName = dialog.getCustomerName();
            String checkIn = dialog.getCheckInDate();
            String checkOut = dialog.getCheckOutDate();
            int nights = dialog.getNumberOfNights();
            double totalPrice = room.getPricePerNight() * nights;

            String insertBookingSql = "INSERT INTO bookings (room_id, customer_name, check_in_date, check_out_date, number_of_nights, total_price) VALUES (?, ?, ?, ?, ?, ?)";
            String updateRoomSql = "UPDATE rooms SET is_available = 0 WHERE room_id = ?";
            java.sql.Connection conn = DatabaseManager.getConnection();

            try (java.sql.PreparedStatement bookingStmt = conn.prepareStatement(insertBookingSql);
                 java.sql.PreparedStatement roomStmt = conn.prepareStatement(updateRoomSql)) {

                bookingStmt.setInt(1, roomId);
                bookingStmt.setString(2, customerName);
                bookingStmt.setString(3, checkIn);
                bookingStmt.setString(4, checkOut);
                bookingStmt.setInt(5, nights);
                bookingStmt.setDouble(6, totalPrice);
                bookingStmt.executeUpdate();

                roomStmt.setInt(1, roomId);
                roomStmt.executeUpdate();

                currentCustomerName = customerName;
                bookingManager.setCurrentCustomerName(customerName);
                customerLabel.setText("Customer: " + currentCustomerName);

                JOptionPane.showMessageDialog(this,
                        String.format("Booking confirmed!\n\nRoom: %s\nCustomer: %s\nTotal: %.0f VND",
                                room.getRoomName(), customerName, totalPrice),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                refreshTables();

            } catch (java.sql.SQLException e) {
                JOptionPane.showMessageDialog(this, "Error booking room: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void changeCustomer() {
        String newName = JOptionPane.showInputDialog(this, "Enter customer name:", currentCustomerName);
        if (newName != null && !newName.trim().isEmpty()) {
            currentCustomerName = newName.trim();
            bookingManager.setCurrentCustomerName(currentCustomerName);
            customerLabel.setText("Customer: " + currentCustomerName);
            refreshMyBookings();
        }
    }

    private void sortAvailableRoomsByPrice(boolean lowToHigh) {
        ArrayList<Room> rooms = roomManager.getRooms();
        ArrayList<Room> availableRooms = new ArrayList<>();

        for (Room room : rooms) {
            if (room.isAvailable()) {
                availableRooms.add(room);
            }
        }

        availableRooms.sort((r1, r2) -> {
            if (lowToHigh) {
                return Double.compare(r1.getPricePerNight(), r2.getPricePerNight());
            } else {
                return Double.compare(r2.getPricePerNight(), r1.getPricePerNight());
            }
        });

        availableRoomsModel.setRowCount(0);
        for (Room room : availableRooms) {
            Object[] row = {
                room.getRoomId(),
                room.getRoomName(),
                room.getRoomType(),
                String.format("%.0f VND", room.getPricePerNight())
            };
            availableRoomsModel.addRow(row);
        }
    }
}
