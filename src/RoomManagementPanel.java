import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class RoomManagementPanel extends JPanel {
    private RoomManager roomManager;
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, refreshButton;

    public RoomManagementPanel(RoomManager roomManager) {
        this.roomManager = roomManager;
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        initializeComponents();
    }

    private void initializeComponents() {
        JLabel titleLabel = new JLabel("Room Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {"Room ID", "Room Name", "Room Type", "Price/Night", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        roomTable = new JTable(tableModel);
        roomTable.setFont(new Font("Arial", Font.PLAIN, 14));
        roomTable.setRowHeight(30);
        roomTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        roomTable.getTableHeader().setBackground(new Color(41, 128, 185));
        roomTable.getTableHeader().setReorderingAllowed(false);
        roomTable.getTableHeader().setResizingAllowed(false);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(roomTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        refreshTable();
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        addButton = createStyledButton("Add Room", new Color(46, 204, 113));
        editButton = createStyledButton("Edit Room", new Color(241, 196, 15));
        deleteButton = createStyledButton("Delete Room", new Color(231, 76, 60));
        refreshButton = createStyledButton("Refresh", new Color(52, 152, 219));

        addButton.addActionListener(e -> addRoom());
        editButton.addActionListener(e -> editRoom());
        deleteButton.addActionListener(e -> deleteRoom());
        refreshButton.addActionListener(e -> refreshTable());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        return buttonPanel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(130, 40));
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

    public void refreshTable() {
        tableModel.setRowCount(0);
        ArrayList<Room> rooms = roomManager.getRooms();
        for (Room room : rooms) {
            Object[] row = {
                room.getRoomId(),
                room.getRoomName(),
                room.getRoomType(),
                String.format("$%.2f", room.getPricePerNight()),
                room.isAvailable() ? "Available" : "Booked"
            };
            tableModel.addRow(row);
        }
    }

    private void addRoom() {
        RoomDialog dialog = new RoomDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Room", null);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            String name = dialog.getRoomName();
            String type = dialog.getRoomType();
            double price = dialog.getPrice();

            String sql = "INSERT INTO rooms (room_name, room_type, price_per_night, is_available) VALUES (?, ?, ?, 1)";
            java.sql.Connection conn = DatabaseManager.getConnection();
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, name);
                pstmt.setString(2, type);
                pstmt.setDouble(3, price);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Room added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();

            } catch (java.sql.SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding room: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to edit", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int roomId = (int) tableModel.getValueAt(selectedRow, 0);
        Room room = roomManager.findRoomById(roomId);

        if (room == null) {
            JOptionPane.showMessageDialog(this, "Room not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!room.isAvailable()) {
            JOptionPane.showMessageDialog(this,
                    "Cannot edit this room!\nThis room is currently booked/rented.\nYou can only edit rooms that are available.",
                    "Room Booked",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        RoomDialog dialog = new RoomDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Room", room);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            String name = dialog.getRoomName();
            String type = dialog.getRoomType();
            double price = dialog.getPrice();

            String sql = "UPDATE rooms SET room_name = ?, room_type = ?, price_per_night = ? WHERE room_id = ?";
            java.sql.Connection conn = DatabaseManager.getConnection();
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, name);
                pstmt.setString(2, type);
                pstmt.setDouble(3, price);
                pstmt.setInt(4, roomId);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Room updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();

            } catch (java.sql.SQLException e) {
                JOptionPane.showMessageDialog(this, "Error updating room: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to delete", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int roomId = (int) tableModel.getValueAt(selectedRow, 0);
        Room room = roomManager.findRoomById(roomId);

        if (room == null) {
            JOptionPane.showMessageDialog(this, "Room not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!room.isAvailable()) {
            JOptionPane.showMessageDialog(this,
                    "Cannot delete this room!\nThis room is currently booked/rented.\nYou can only delete rooms that are available.",
                    "Room Booked",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this room?\n" + room.toString(),
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM rooms WHERE room_id = ?";
            java.sql.Connection conn = DatabaseManager.getConnection();
            try (java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, roomId);
                pstmt.executeUpdate();

                JOptionPane.showMessageDialog(this, "Room deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();

            } catch (java.sql.SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting room: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
