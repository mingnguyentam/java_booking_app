import java.awt.*;
import javax.swing.*;

public class BookingDialog extends JDialog {
    private JTextField customerNameField;
    private JTextField checkInField;
    private JTextField checkOutField;
    private JTextField nightsField;
    private JLabel totalPriceLabel;
    private Room room;
    private boolean confirmed = false;

    public BookingDialog(Frame parent, Room room, String currentCustomerName) {
        super(parent, "Book Room", true);
        this.room = room;
        setSize(500, 450);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = createFormPanel(currentCustomerName);
        JPanel buttonPanel = createButtonPanel();

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createFormPanel(String currentCustomerName) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel roomInfoLabel = new JLabel("Room: " + room.getRoomName() + " - " + room.getRoomType());
        roomInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        roomInfoLabel.setForeground(new Color(41, 128, 185));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(roomInfoLabel, gbc);

        JLabel priceInfoLabel = new JLabel(String.format("Price: %.0f VND per night", room.getPricePerNight()));
        priceInfoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(priceInfoLabel, gbc);

        JSeparator separator = new JSeparator();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel.add(separator, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel nameLabel = new JLabel("Customer Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(nameLabel, gbc);

        customerNameField = new JTextField(currentCustomerName, 20);
        customerNameField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(customerNameField, gbc);

        JLabel checkInLabel = new JLabel("Check-in Date:");
        checkInLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(checkInLabel, gbc);

        checkInField = new JTextField("2024-01-01", 20);
        checkInField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(checkInField, gbc);

        JLabel checkInHint = new JLabel("(Format: YYYY-MM-DD)");
        checkInHint.setFont(new Font("Arial", Font.ITALIC, 12));
        checkInHint.setForeground(Color.GRAY);
        gbc.gridx = 1;
        gbc.gridy = 5;
        panel.add(checkInHint, gbc);

        JLabel checkOutLabel = new JLabel("Check-out Date:");
        checkOutLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(checkOutLabel, gbc);

        checkOutField = new JTextField("2024-01-05", 20);
        checkOutField.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(checkOutField, gbc);

        JLabel checkOutHint = new JLabel("(Format: YYYY-MM-DD)");
        checkOutHint.setFont(new Font("Arial", Font.ITALIC, 12));
        checkOutHint.setForeground(Color.GRAY);
        gbc.gridx = 1;
        gbc.gridy = 7;
        panel.add(checkOutHint, gbc);

        JLabel nightsLabel = new JLabel("Number of Nights:");
        nightsLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(nightsLabel, gbc);

        nightsField = new JTextField("4", 20);
        nightsField.setFont(new Font("Arial", Font.PLAIN, 14));
        nightsField.addCaretListener(e -> updateTotalPrice());
        gbc.gridx = 1;
        gbc.gridy = 8;
        panel.add(nightsField, gbc);

        JLabel totalLabel = new JLabel("Total Price:");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 9;
        panel.add(totalLabel, gbc);

        totalPriceLabel = new JLabel("0 VND");
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalPriceLabel.setForeground(new Color(46, 204, 113));
        gbc.gridx = 1;
        gbc.gridy = 9;
        panel.add(totalPriceLabel, gbc);

        updateTotalPrice();

        return panel;
    }

    private void updateTotalPrice() {
        try {
            int nights = Integer.parseInt(nightsField.getText().trim());
            double total = room.getPricePerNight() * nights;
            totalPriceLabel.setText(String.format("%.0f VND", total));
        } catch (NumberFormatException e) {
            totalPriceLabel.setText("0 VND");
        }
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        JButton confirmButton = new JButton("Confirm Booking");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setBackground(new Color(46, 204, 113));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setBorderPainted(false);
        confirmButton.setPreferredSize(new Dimension(150, 35));
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setBackground(new Color(149, 165, 166));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        confirmButton.addActionListener(e -> {
            if (validateInput()) {
                confirmed = true;
                dispose();
            }
        });

        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        panel.add(confirmButton);
        panel.add(cancelButton);

        return panel;
    }

    private boolean validateInput() {
        if (customerNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter name", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (checkInField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter checkin date", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (checkOutField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter checkout date", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            int nights = Integer.parseInt(nightsField.getText().trim());
            if (nights <= 0) {
                JOptionPane.showMessageDialog(this, "Number of nights must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number of nights", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public String getCustomerName() {
        return customerNameField.getText().trim();
    }

    public String getCheckInDate() {
        return checkInField.getText().trim();
    }

    public String getCheckOutDate() {
        return checkOutField.getText().trim();
    }

    public int getNumberOfNights() {
        return Integer.parseInt(nightsField.getText().trim());
    }
}
