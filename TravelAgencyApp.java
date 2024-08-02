import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TravelAgencyApp extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public TravelAgencyApp() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Add panels for different views
        mainPanel.add(new LoginPage(this), "Login");
        mainPanel.add(new DashboardPage(this), "Dashboard");

        add(mainPanel);
        setTitle("Travel Agency Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void showCard(String card) {
        cardLayout.show(mainPanel, card);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TravelAgencyApp app = new TravelAgencyApp();
            app.setVisible(true);
        });
    }
}

class LoginPage extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorMessage;

    public LoginPage(TravelAgencyApp app) {
        setLayout(new BorderLayout());

        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(100, 150, 100, 150));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        errorMessage = new JLabel("", JLabel.CENTER);
        errorMessage.setForeground(Color.RED);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (validateLogin(username, password)) {
                    app.showCard("Dashboard");
                } else {
                    errorMessage.setText("Invalid username or password.");
                }
            }
        });

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(new JLabel());  // empty placeholder

        add(loginPanel, BorderLayout.CENTER);
        add(errorMessage, BorderLayout.SOUTH);
    }

    private boolean validateLogin(String username, String password) {
        // Hardcoded user data for simplicity
        return "admin".equals(username) && "password".equals(password);
    }
}

class DashboardPage extends JPanel {
    public DashboardPage(TravelAgencyApp app) {
        setLayout(new BorderLayout());

        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(1, 5));
        JButton packagesButton = new JButton("Packages");
        JButton bookingsButton = new JButton("Bookings");
        JButton customersButton = new JButton("Customers");
        JButton billingButton = new JButton("Billing");
        JButton exitButton = new JButton("Exit");

        packagesButton.addActionListener(e -> app.showCard("Packages"));
        bookingsButton.addActionListener(e -> app.showCard("Bookings"));
        customersButton.addActionListener(e -> app.showCard("Customers"));
        billingButton.addActionListener(e -> app.showCard("Billing"));
        exitButton.addActionListener(e -> System.exit(0));

        navPanel.add(packagesButton);
        navPanel.add(bookingsButton);
        navPanel.add(customersButton);
        navPanel.add(billingButton);
        navPanel.add(exitButton);

        add(navPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.add(new PackagesPage(app), "Packages");
        contentPanel.add(new BookingsPage(app), "Bookings");
        contentPanel.add(new CustomersPage(app), "Customers");
        contentPanel.add(new BillingPage(app), "Billing");

        add(contentPanel, BorderLayout.CENTER);
    }
}

class PackagesPage extends JPanel {
    private DefaultListModel<String> packageListModel;

    public PackagesPage(TravelAgencyApp app) {
        setLayout(new BorderLayout());

        packageListModel = new DefaultListModel<>();
        JList<String> packageList = new JList<>(packageListModel);
        JScrollPane scrollPane = new JScrollPane(packageList);

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add Package");
        JButton removeButton = new JButton("Remove Package");
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);

        addButton.addActionListener(e -> {
            JTextField nameField = new JTextField();
            JTextField durationField = new JTextField();
            JTextField costField = new JTextField();
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Package Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Duration:"));
            panel.add(durationField);
            panel.add(new JLabel("Cost:"));
            panel.add(costField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Enter Package Details",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String packageDetails = "Name: " + nameField.getText() + " | Duration: " + durationField.getText() + " days | Cost: $" + costField.getText();
                packageListModel.addElement(packageDetails);
            }
        });

        removeButton.addActionListener(e -> {
            int selectedIndex = packageList.getSelectedIndex();
            if (selectedIndex != -1) {
                packageListModel.remove(selectedIndex);
            }
        });

        add(buttonPanel, BorderLayout.SOUTH);
    }
}

class BookingsPage extends JPanel {
    private DefaultListModel<String> bookingListModel;

    public BookingsPage(TravelAgencyApp app) {
        setLayout(new BorderLayout());

        bookingListModel = new DefaultListModel<>();
        JList<String> bookingList = new JList<>(bookingListModel);
        JScrollPane scrollPane = new JScrollPane(bookingList);

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton bookPackageButton = new JButton("Book Package");
        JButton manageBookingButton = new JButton("Manage Booking");
        buttonPanel.add(bookPackageButton);
        buttonPanel.add(manageBookingButton);

        bookPackageButton.addActionListener(e -> {
            String packageDetails = JOptionPane.showInputDialog("Enter package details to book:");
            if (packageDetails != null && !packageDetails.trim().isEmpty()) {
                String customerDetails = JOptionPane.showInputDialog("Enter customer details:");
                if (customerDetails != null && !customerDetails.trim().isEmpty()) {
                    String booking = "Booking ID: " + System.currentTimeMillis() + " | Package: " + packageDetails + " | Customer: " + customerDetails;
                    bookingListModel.addElement(booking);
                }
            }
        });

        manageBookingButton.addActionListener(e -> {
            int selectedIndex = bookingList.getSelectedIndex();
            if (selectedIndex != -1) {
                String booking = bookingListModel.get(selectedIndex);
                String[] options = {"Modify", "Cancel"};
                int choice = JOptionPane.showOptionDialog(this, "Manage Booking", "Options", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                if (choice == 0) {
                    String newDetails = JOptionPane.showInputDialog("Modify booking details:", booking);
                    if (newDetails != null && !newDetails.trim().isEmpty()) {
                        bookingListModel.set(selectedIndex, newDetails);
                    }
                } else if (choice == 1) {
                    bookingListModel.remove(selectedIndex);
                }
            }
        });

        add(buttonPanel, BorderLayout.SOUTH);
    }
}

class CustomersPage extends JPanel {
    private DefaultListModel<String> customerListModel;

    public CustomersPage(TravelAgencyApp app) {
        setLayout(new BorderLayout());

        customerListModel = new DefaultListModel<>();
        JList<String> customerList = new JList<>(customerListModel);
        JScrollPane scrollPane = new JScrollPane(customerList);

        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addCustomerButton = new JButton("Add Customer");
        JButton viewCustomerButton = new JButton("View Customer");
        buttonPanel.add(addCustomerButton);
        buttonPanel.add(viewCustomerButton);

        addCustomerButton.addActionListener(e -> {
            String customerDetails = JOptionPane.showInputDialog("Enter customer details:");
            if (customerDetails != null && !customerDetails.trim().isEmpty()) {
                customerListModel.addElement(customerDetails);
            }
        });

        viewCustomerButton.addActionListener(e -> {
            int selectedIndex = customerList.getSelectedIndex();
            if (selectedIndex != -1) {
                String customer = customerListModel.get(selectedIndex);
                JOptionPane.showMessageDialog(this, customer, "Customer Details", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        add(buttonPanel, BorderLayout.SOUTH);
    }
}

class BillingPage extends JPanel {
    private DefaultListModel<String> billingListModel;

    public BillingPage(TravelAgencyApp app) {
        setLayout(new BorderLayout());

        billingListModel = new DefaultListModel<>();
        JList<String> billingList = new JList<>(billingListModel);
        JScrollPane scrollPane = new JScrollPane(billingList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton generateInvoiceButton = new JButton("Generate Invoice");
        JButton viewInvoiceButton = new JButton("View Invoice");
        buttonPanel.add(generateInvoiceButton);
        buttonPanel.add(viewInvoiceButton);

        generateInvoiceButton.addActionListener(e -> {
            String bookingDetails = JOptionPane.showInputDialog("Enter booking details to generate invoice:");
            if (bookingDetails != null && !bookingDetails.trim().isEmpty()) {
                String invoice = "Invoice ID: " + System.currentTimeMillis() + " | Booking Details: " + bookingDetails + " | Total: $" + (Math.random() * 1000 + 500);
                billingListModel.addElement(invoice);
            }
        });

        viewInvoiceButton.addActionListener(e -> {
            int selectedIndex = billingList.getSelectedIndex();
            if (selectedIndex != -1) {
                String invoice = billingListModel.get(selectedIndex);
                JOptionPane.showMessageDialog(this, invoice, "Invoice Details", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        add(buttonPanel, BorderLayout.SOUTH);
    }
}
