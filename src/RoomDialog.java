import javax.swing.*;
import java.awt.*;

public class RoomDialog extends JDialog {
    private JTextField nameField;
    private JComboBox<String> typeComboBox;
    private JTextField priceField;
    private boolean confirmed = false;

    public RoomDialog(Frame parent, String title, Room room) {
        super(parent, title, true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = createFormPanel(room);
        JPanel buttonPanel = createButtonPanel();

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createFormPanel(Room room) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("Room Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);

        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        if (room != null) {
            nameField.setText(room.getRoomName());
        }
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(nameField, gbc);

        JLabel typeLabel = new JLabel("Room Type:");
        typeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(typeLabel, gbc);

        String[] types = {"Standard", "Deluxe", "Suite", "Executive"};
        typeComboBox = new JComboBox<>(types);
        typeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        if (room != null) {
            typeComboBox.setSelectedItem(room.getRoomType());
        }
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(typeComboBox, gbc);

        JLabel priceLabel = new JLabel("Price/Night ($):");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(priceLabel, gbc);

        priceField = new JTextField(20);
        priceField.setFont(new Font("Arial", Font.PLAIN, 14));
        if (room != null) {
            priceField.setText(String.valueOf(room.getPricePerNight()));
        }
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(priceField, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBackground(new Color(46, 204, 113));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setPreferredSize(new Dimension(100, 35));
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(new Color(149, 165, 166));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        saveButton.addActionListener(e -> {
            if (validateInput()) {
                confirmed = true;
                dispose();
            }
        });

        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        panel.add(saveButton);
        panel.add(cancelButton);

        return panel;
    }

    private boolean validateInput() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter room name", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            double price = Double.parseDouble(priceField.getText().trim());
            if (price <= 0) {
                JOptionPane.showMessageDialog(this, "Price must be greater than 0", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getRoomName() {
        return nameField.getText().trim();
    }

    public String getRoomType() {
        return (String) typeComboBox.getSelectedItem();
    }

    public double getPrice() {
        return Double.parseDouble(priceField.getText().trim());
    }
}
