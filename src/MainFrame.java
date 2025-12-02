import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private RoomManagementPanel roomManagementPanel;
    private BookingPanel bookingPanel;
    private RoomManager roomManager;
    private BookingManager bookingManager;

    public MainFrame() {
        DatabaseManager.initializeDatabase();
        roomManager = new RoomManager();
        bookingManager = new BookingManager(roomManager);

        setTitle("Room Booking App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel menuPanel = createMenuPanel();
        add(menuPanel, BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        JPanel aboutPanel = createAboutPanel();
        roomManagementPanel = new RoomManagementPanel(roomManager);
        bookingPanel = new BookingPanel(bookingManager, roomManager);

        contentPanel.add(aboutPanel, "ABOUT");
        contentPanel.add(roomManagementPanel, "ROOM_MANAGEMENT");
        contentPanel.add(bookingPanel, "BOOKING");

        add(contentPanel, BorderLayout.CENTER);

        cardLayout.show(contentPanel, "ABOUT");
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("ROOM BOOKING APP", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(44, 62, 80));
        menuPanel.setPreferredSize(new Dimension(200, 0));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        String[] menuItems = {"About", "Room Management", "Booking"};
        String[] menuCards = {"ABOUT", "ROOM_MANAGEMENT", "BOOKING"};

        for (int i = 0; i < menuItems.length; i++) {
            JButton menuButton = createMenuButton(menuItems[i], menuCards[i]);
            menuPanel.add(menuButton);
            menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        menuPanel.add(Box.createVerticalGlue());

        JButton exitButton = createMenuButton("Exit", null);
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to exit?",
                    "Exit Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                DatabaseManager.closeConnection();
                System.exit(0);
            }
        });
        menuPanel.add(exitButton);

        return menuPanel;
    }

    private JButton createMenuButton(String text, String cardName) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 73, 94));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setMaximumSize(new Dimension(180, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 73, 94));
            }
        });

        if (cardName != null) {
            button.addActionListener(e -> {
                cardLayout.show(contentPanel, cardName);
                if (cardName.equals("ROOM_MANAGEMENT")) {
                    roomManagementPanel.refreshTable();
                } else if (cardName.equals("BOOKING")) {
                    bookingPanel.refreshTables();
                }
            });
        }

        return button;
    }

    private JPanel createAboutPanel() {
        JPanel aboutPanel = new JPanel(new GridBagLayout());
        aboutPanel.setBackground(Color.WHITE);

        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(Color.WHITE);

        JLabel welcomeLabel = new JLabel("Welcome to Room Booking App");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel introduce = new JLabel("This application was developed by");
        introduce.setFont(new Font("Arial", Font.BOLD, 16));
        introduce.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel developer1 = new JLabel("Nguyen Minh Tam - K24DTCN627");
        developer1.setFont(new Font("Arial", Font.BOLD, 16));
        developer1.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel developer2 = new JLabel("Trieu Duc Hoang - K24DTCN598");
        developer2.setFont(new Font("Arial", Font.BOLD, 16));
        developer2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instructionLabel = new JLabel("Select an option from the menu to get started");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionLabel.setForeground(Color.GRAY);
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        welcomePanel.add(introduce);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        welcomePanel.add(developer1);
        welcomePanel.add(developer2);
        welcomePanel.add(Box.createRigidArea(new Dimension(0, 30)));
        welcomePanel.add(instructionLabel);

        aboutPanel.add(welcomePanel);

        return aboutPanel;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}
