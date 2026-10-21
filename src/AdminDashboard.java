import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class AdminDashboard extends JFrame {

    private final Color NAVY = new Color(23, 43, 77);
    private final Color TEAL = new Color(15, 157, 145);
    private final Color LIGHT_BG = new Color(245, 247, 250);
    private final Color TEXT_COLOR = new Color(45, 55, 72);
    private final Color BORDER_COLOR = new Color(220, 226, 235);
    private final Color WHITE = Color.WHITE;
    private final Color RED = new Color(190, 60, 65);
    private final Color GREY = new Color(90, 105, 125);

    private String role;
    private String username;

    private JPanel contentPanel;

    private JTable eventTable;
    private DefaultTableModel eventModel;

    private JTable registrationTable;
    private DefaultTableModel registrationModel;

    private JTextField nameField;
    private JComboBox<String> categoryBox;
    private JTextArea descriptionArea;
    private JTextField dateField;
    private JTextField timeField;
    private JTextField venueField;
    private JTextField feeField;
    private JTextField prizesField;
    private JTextField paymentLocationField;
    private JTextField paymentHoursField;
    private JTextField paymentDeadlineField;

    private int selectedEventId = -1;

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color),
                BorderFactory.createEmptyBorder(9, 14, 9, 14)
        ));
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setForeground(TEXT_COLOR);
        table.setBackground(WHITE);
        table.setGridColor(BORDER_COLOR);
        table.setRowHeight(32);
        table.setSelectionBackground(new Color(214, 239, 235));
        table.setSelectionForeground(TEXT_COLOR);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setFillsViewportHeight(true);

        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );
        table.getTableHeader().setBackground(NAVY);
        table.getTableHeader().setForeground(WHITE);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setPreferredSize(
                new Dimension(0, 36)
        );
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBackground(WHITE);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEAL);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(7, 8, 7, 8)
        ));
    }

    private void styleHeading(JLabel heading) {
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setForeground(NAVY);
        heading.setBorder(
                BorderFactory.createEmptyBorder(0, 0, 10, 0)
        );
    }

    public AdminDashboard(String role, String username) {

        this.role = role;
        this.username = username;

        setTitle("College Event Management System - Admin");
        setSize(1200, 700);
        setMinimumSize(new Dimension(950, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(LIGHT_BG);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(
                BorderFactory.createEmptyBorder(18, 24, 18, 24)
        );

        JLabel titleLabel = new JLabel(
                "College Event Management System"
        );
        titleLabel.setForeground(WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel userLabel = new JLabel(
                "Welcome, " + username + "   "
        );
        userLabel.setForeground(WHITE);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        header.add(titleLabel, BorderLayout.WEST);
        header.add(userLabel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Sidebar
        JPanel sidebar = new JPanel(new GridLayout(4, 1, 5, 12));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBackground(LIGHT_BG);
        sidebar.setBorder(
                BorderFactory.createEmptyBorder(20, 12, 20, 12)
        );

        JButton dashboardButton = new JButton("Dashboard");
        JButton manageEventsButton = new JButton("Manage Events");
        JButton registrationsButton = new JButton("Registrations");
        JButton logoutButton = new JButton("Logout");

        styleButton(dashboardButton, NAVY);
        styleButton(manageEventsButton, TEAL);
        styleButton(registrationsButton, NAVY);
        styleButton(logoutButton, RED);

        sidebar.add(dashboardButton);
        sidebar.add(manageEventsButton);
        sidebar.add(registrationsButton);
        sidebar.add(logoutButton);

        add(sidebar, BorderLayout.WEST);

        // Main content area
        contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(LIGHT_BG);
        contentPanel.setBorder(
                BorderFactory.createEmptyBorder(22, 22, 22, 22)
        );

        add(contentPanel, BorderLayout.CENTER);

        dashboardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showDashboard();
            }
        });

        manageEventsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showManageEvents();
            }
        });

        registrationsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showRegistrations();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int choice = JOptionPane.showConfirmDialog(
                        AdminDashboard.this,
                        "Are you sure you want to logout?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION
                );

                if (choice == JOptionPane.YES_OPTION) {
                    dispose();
                    new Main();
                }
            }
        });

        showDashboard();
        setVisible(true);
    }

    private String formatDate(java.sql.Date date) {
        if (date == null) {
            return "";
        }

        return new SimpleDateFormat("dd-MM-yyyy").format(date);
    }

    private java.sql.Date parseDate(String dateText)
            throws ParseException {

        if (dateText == null) {
            throw new ParseException("Date cannot be null", 0);
        }

        String trimmed = dateText.trim();
        String[] patterns = {"dd-MM-yyyy", "yyyy-MM-dd", "dd/MM/yyyy"};

        for (String pattern : patterns) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(pattern);
                formatter.setLenient(false);
                java.util.Date parsedDate = formatter.parse(trimmed);
                return new java.sql.Date(parsedDate.getTime());
            } catch (ParseException ignored) {}
        }

        throw new ParseException("Invalid date format. Expected DD-MM-YYYY or YYYY-MM-DD", 0);
    }

    private void showDashboard() {

        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout(15, 15));

        JLabel heading = new JLabel("Admin Dashboard");
        styleHeading(heading);

        JLabel welcome = new JLabel(
                "<html><div style='width:520px;'>"
                + "<h2 style='color:#172B4D;'>Welcome to your dashboard!</h2>"
                + "<p style='font-size:14px;color:#2D3748;'>"
                + "Manage college events, view student registration requests, "
                + "and confirm offline payments after receiving them."
                + "</p>"
                + "<p style='font-size:13px;color:#0F9D91;'>"
                + "Use the navigation panel to get started."
                + "</p></div></html>"
        );

        welcome.setFont(new Font("Arial", Font.PLAIN, 16));
        welcome.setForeground(TEXT_COLOR);
        welcome.setVerticalAlignment(SwingConstants.TOP);
        welcome.setBorder(
                BorderFactory.createEmptyBorder(15, 10, 10, 10)
        );

        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(WHITE);
        welcomePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        welcomePanel.add(welcome, BorderLayout.NORTH);

        contentPanel.add(heading, BorderLayout.NORTH);
        contentPanel.add(welcomePanel, BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showManageEvents() {

        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout(12, 12));

        JLabel heading = new JLabel("Manage Events");
        styleHeading(heading);

        contentPanel.add(heading, BorderLayout.NORTH);

        String[] columns = {
            "ID", "Event Name", "Category", "Event Date",
            "Time", "Venue", "Fee", "Payment Venue",
            "Payment Hours", "Payment Deadline"
        };

        eventModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        eventTable = new JTable(eventModel);
        styleTable(eventTable);
        eventTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        int[] widths = {
            50, 170, 110, 110, 100, 150, 80, 170, 145, 135
        };

        for (int i = 0; i < widths.length; i++) {
            eventTable.getColumnModel().getColumn(i)
                    .setPreferredWidth(widths[i]);
        }

        JScrollPane scrollPane = new JScrollPane(eventTable);
        scrollPane.getViewport().setBackground(WHITE);
        scrollPane.setBorder(
                BorderFactory.createLineBorder(BORDER_COLOR)
        );

        JButton addButton = new JButton("Add Event");
        JButton editButton = new JButton("Edit Selected");
        JButton viewButton = new JButton("View Selected");
        JButton deleteButton = new JButton("Delete Selected");
        JButton refreshButton = new JButton("Refresh");

        styleButton(addButton, TEAL);
        styleButton(editButton, NAVY);
        styleButton(viewButton, NAVY);
        styleButton(deleteButton, RED);
        styleButton(refreshButton, GREY);

        JPanel buttonPanel =
                new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));

        buttonPanel.setBackground(LIGHT_BG);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showEventForm(false);
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadSelectedEventForEditing();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewSelectedEvent();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSelectedEvent();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadEvents();
            }
        });

        loadEvents();

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void loadEvents() {

        eventModel.setRowCount(0);

        String sql =
                "SELECT event_id, event_name, category, event_date, "
                + "event_time, venue, fee, payment_location, "
                + "payment_hours, payment_deadline "
                + "FROM events ORDER BY event_id";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {

                Object[] row = {
                    rs.getInt("event_id"),
                    rs.getString("event_name"),
                    rs.getString("category"),
                    formatDate(rs.getDate("event_date")),
                    rs.getString("event_time"),
                    rs.getString("venue"),
                    rs.getBigDecimal("fee"),
                    rs.getString("payment_location"),
                    rs.getString("payment_hours"),
                    formatDate(rs.getDate("payment_deadline"))
                };

                eventModel.addRow(row);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load events:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error closing database connection."
                );
            }
        }
    }

    private void showEventForm(boolean editing) {

        final JDialog dialog = new JDialog(
                this,
                editing ? "Edit Event" : "Add Event",
                true
        );

        dialog.setSize(580, 670);
        dialog.setMinimumSize(new Dimension(520, 600));
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(LIGHT_BG);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(WHITE);
        formPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 18, 15, 18)
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 7, 7, 7);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        nameField = new JTextField(20);
        styleTextField(nameField);

        categoryBox = new JComboBox<String>(new String[] {
            "Technical", "Cultural", "Sports", "Workshop", "Other"
        });
        categoryBox.setFont(new Font("Arial", Font.PLAIN, 14));
        categoryBox.setBackground(WHITE);

        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setForeground(TEXT_COLOR);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(
                BorderFactory.createEmptyBorder(6, 6, 6, 6)
        );

        dateField = new JTextField(20);
        styleTextField(dateField);

        timeField = new JTextField(20);
        styleTextField(timeField);

        venueField = new JTextField(20);
        styleTextField(venueField);

        feeField = new JTextField(20);
        styleTextField(feeField);

        prizesField = new JTextField(20);
        styleTextField(prizesField);

        paymentLocationField = new JTextField(20);
        styleTextField(paymentLocationField);

        paymentHoursField = new JTextField(20);
        styleTextField(paymentHoursField);

        paymentDeadlineField = new JTextField(20);
        styleTextField(paymentDeadlineField);

        int row = 0;

        addFormRow(formPanel, gbc, row++, "Event Name:", nameField);
        addFormRow(formPanel, gbc, row++, "Category:", categoryBox);

        addFormRow(
                formPanel, gbc, row++, "Description:",
                new JScrollPane(descriptionArea)
        );

        addFormRow(
                formPanel, gbc, row++, "Event Date (DD-MM-YYYY):",
                dateField
        );

        addFormRow(formPanel, gbc, row++, "Event Time:", timeField);
        addFormRow(formPanel, gbc, row++, "Event Venue:", venueField);
        addFormRow(formPanel, gbc, row++, "Fee:", feeField);
        addFormRow(formPanel, gbc, row++, "Prizes:", prizesField);

        addFormRow(
                formPanel, gbc, row++, "Payment Venue:",
                paymentLocationField
        );

        addFormRow(
                formPanel, gbc, row++, "Payment Hours:",
                paymentHoursField
        );

        addFormRow(
                formPanel, gbc, row++,
                "Payment Deadline (DD-MM-YYYY):",
                paymentDeadlineField
        );

        JScrollPane formScrollPane = new JScrollPane(formPanel);
        formScrollPane.setBorder(BorderFactory.createEmptyBorder());
        dialog.add(formScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel =
                new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        bottomPanel.setBackground(LIGHT_BG);
        bottomPanel.setBorder(
                BorderFactory.createEmptyBorder(0, 10, 5, 10)
        );

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        styleButton(saveButton, TEAL);
        styleButton(cancelButton, GREY);

        bottomPanel.add(saveButton);
        bottomPanel.add(cancelButton);

        dialog.add(bottomPanel, BorderLayout.SOUTH);

        if (editing) {
            fillFormFromSelectedRow();
        } else {
            nameField.setText("");
            categoryBox.setSelectedIndex(0);
            descriptionArea.setText("");
            dateField.setText("");
            timeField.setText("");
            venueField.setText("");
            feeField.setText("0");
            prizesField.setText("");
            paymentLocationField.setText("");
            paymentHoursField.setText("");
            paymentDeadlineField.setText("");
        }

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveEvent(editing, dialog);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        dialog.setVisible(true);
    }

    private void addFormRow(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String label,
            Component component) {

        JLabel formLabel = new JLabel(label);
        formLabel.setFont(new Font("Arial", Font.BOLD, 13));
        formLabel.setForeground(TEXT_COLOR);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.40;
        panel.add(formLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.60;
        panel.add(component, gbc);
    }

    private void loadSelectedEventForEditing() {

        int selectedRow = eventTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an event first."
            );
            return;
        }

        selectedEventId = Integer.parseInt(
                eventModel.getValueAt(selectedRow, 0).toString()
        );

        showEventForm(true);
    }

    private void fillFormFromSelectedRow() {

        String sql = "SELECT * FROM events WHERE event_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, selectedEventId);
            rs = ps.executeQuery();

            if (rs.next()) {

                nameField.setText(rs.getString("event_name"));
                categoryBox.setSelectedItem(rs.getString("category"));
                descriptionArea.setText(rs.getString("description"));

                dateField.setText(
                        formatDate(rs.getDate("event_date"))
                );

                timeField.setText(rs.getString("event_time"));
                venueField.setText(rs.getString("venue"));

                BigDecimal fee = rs.getBigDecimal("fee");
                feeField.setText(fee == null ? "0" : fee.toString());

                String prizes = rs.getString("prizes");
                prizesField.setText(prizes == null ? "" : prizes);

                paymentLocationField.setText(
                        rs.getString("payment_location")
                );

                paymentHoursField.setText(
                        rs.getString("payment_hours")
                );

                paymentDeadlineField.setText(
                        formatDate(rs.getDate("payment_deadline"))
                );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load event details:\n" + ex.getMessage()
            );

        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                // Connection cleanup
            }
        }
    }

    private void saveEvent(boolean editing, JDialog dialog) {

        String eventName = nameField.getText().trim();
        String category = categoryBox.getSelectedItem().toString();
        String description = descriptionArea.getText().trim();
        String eventDateText = dateField.getText().trim();
        String eventTime = timeField.getText().trim();
        String venue = venueField.getText().trim();
        String feeText = feeField.getText().trim();
        String prizes = prizesField.getText().trim();
        String paymentLocation = paymentLocationField.getText().trim();
        String paymentHours = paymentHoursField.getText().trim();
        String deadlineText = paymentDeadlineField.getText().trim();

        if (eventName.isEmpty()
                || eventDateText.isEmpty()
                || eventTime.isEmpty()
                || venue.isEmpty()
                || feeText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    dialog,
                    "Fill in the event name, date, time, venue and fee."
            );
            return;
        }

        java.sql.Date parsedEventDate;
        java.sql.Date parsedDeadline = null;
        BigDecimal fee;

        try {
            parsedEventDate = parseDate(eventDateText);
            String cleanFee = feeText.replaceAll("[^0-9.]", "");
            if (cleanFee.isEmpty()) cleanFee = "0";
            fee = new BigDecimal(cleanFee);

            if (fee.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Fee cannot be negative."
                );
                return;
            }

            if (!deadlineText.isEmpty()) {
                parsedDeadline = parseDate(deadlineText);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Use DD-MM-YYYY for dates, e.g. 25-10-2026, "
                    + "and enter a valid fee."
            );
            return;
        }

        String sql;

        if (editing) {
            sql = "UPDATE events SET event_name=?, category=?, "
                    + "description=?, event_date=?, event_time=?, "
                    + "venue=?, fee=?, prizes=?, payment_location=?, "
                    + "payment_hours=?, payment_deadline=? "
                    + "WHERE event_id=?";
        } else {
            sql = "INSERT INTO events (event_name, category, "
                    + "description, event_date, event_time, venue, "
                    + "fee, prizes, payment_location, payment_hours, "
                    + "payment_deadline) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        }

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);

            ps.setString(1, eventName);
            ps.setString(2, category);
            ps.setString(3, description);
            ps.setDate(4, parsedEventDate);
            ps.setString(5, eventTime);
            ps.setString(6, venue);
            ps.setBigDecimal(7, fee);
            ps.setString(8, prizes.isEmpty() ? "Exciting Prizes & Certificates" : prizes);
            ps.setString(9, paymentLocation);
            ps.setString(10, paymentHours);

            if (parsedDeadline == null) {
                ps.setNull(11, Types.DATE);
            } else {
                ps.setDate(11, parsedDeadline);
            }

            if (editing) {
                ps.setInt(12, selectedEventId);
            }

            int result = ps.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(
                        dialog,
                        editing
                                ? "Event updated successfully!"
                                : "Event added successfully!"
                );

                dialog.dispose();
                loadEvents();
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    dialog,
                    "Unable to save event:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                // Connection cleanup
            }
        }
    }

    private void viewSelectedEvent() {

        int selectedRow = eventTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an event first."
            );
            return;
        }

        int eventId = Integer.parseInt(
                eventModel.getValueAt(selectedRow, 0).toString()
        );

        String sql = "SELECT * FROM events WHERE event_id = ?";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, eventId);
            rs = ps.executeQuery();

            if (rs.next()) {

                String details =
                        "Event ID: " + rs.getInt("event_id")
                        + "\nEvent Name: " + rs.getString("event_name")
                        + "\nCategory: " + rs.getString("category")
                        + "\nDescription: " + rs.getString("description")
                        + "\nEvent Date: " + formatDate(rs.getDate("event_date"))
                        + "\nEvent Time: " + rs.getString("event_time")
                        + "\nVenue: " + rs.getString("venue")
                        + "\nFee: " + rs.getBigDecimal("fee")
                        + "\nPrizes: " + (rs.getString("prizes") == null ? "Not specified" : rs.getString("prizes"))
                        + "\nPayment Venue: " + rs.getString("payment_location")
                        + "\nPayment Hours: " + rs.getString("payment_hours")
                        + "\nPayment Deadline: "
                        + formatDate(rs.getDate("payment_deadline"));

                JTextArea textArea = new JTextArea(details, 15, 40);
                textArea.setEditable(false);
                textArea.setFont(new Font("Arial", Font.PLAIN, 14));
                textArea.setForeground(TEXT_COLOR);
                textArea.setBackground(LIGHT_BG);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setBorder(
                        BorderFactory.createEmptyBorder(12, 12, 12, 12)
                );

                JOptionPane.showMessageDialog(
                        this,
                        new JScrollPane(textArea),
                        "Event Details",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to view event:\n" + ex.getMessage()
            );

        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                // Connection cleanup
            }
        }
    }

    private void deleteSelectedEvent() {

        int selectedRow = eventTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an event first."
            );
            return;
        }

        int eventId = Integer.parseInt(
                eventModel.getValueAt(selectedRow, 0).toString()
        );

        String eventName =
                eventModel.getValueAt(selectedRow, 1).toString();

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete event \"" + eventName + "\"?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = "DELETE FROM events WHERE event_id = ?";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, eventId);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Event deleted successfully."
            );

            loadEvents();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to delete event. It may have registrations.\n"
                    + ex.getMessage()
            );

        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                // Connection cleanup
            }
        }
    }

    private void showRegistrations() {

        contentPanel.removeAll();
        contentPanel.setLayout(new BorderLayout(12, 12));

        JLabel heading =
                new JLabel("Student Registration Requests");
        styleHeading(heading);

        contentPanel.add(heading, BorderLayout.NORTH);

        String[] columns = {
            "Registration ID", "Student ID", "Student Name",
            "Event Name", "Registration Date", "Payment Status"
        };

        registrationModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        registrationTable = new JTable(registrationModel);
        styleTable(registrationTable);

        JButton confirmButton = new JButton("Confirm Payment");
        JButton refreshButton = new JButton("Refresh Registrations");

        styleButton(confirmButton, TEAL);
        styleButton(refreshButton, NAVY);

        JPanel buttonPanel =
                new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        buttonPanel.setBackground(LIGHT_BG);

        buttonPanel.add(confirmButton);
        buttonPanel.add(refreshButton);

        JScrollPane scrollPane = new JScrollPane(registrationTable);
        scrollPane.getViewport().setBackground(WHITE);
        scrollPane.setBorder(
                BorderFactory.createLineBorder(BORDER_COLOR)
        );

        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                confirmSelectedPayment();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadRegistrations();
            }
        });

        loadRegistrations();

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void loadRegistrations() {

        registrationModel.setRowCount(0);

        String sql =
                "SELECT r.registration_id, r.student_id, s.full_name, "
                + "e.event_name, r.registration_date, r.payment_status "
                + "FROM registrations r "
                + "JOIN students s ON r.student_id = s.student_id "
                + "JOIN events e ON r.event_id = e.event_id "
                + "ORDER BY r.registration_id DESC";

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {

                Object[] row = {
                    rs.getInt("registration_id"),
                    rs.getInt("student_id"),
                    rs.getString("full_name"),
                    rs.getString("event_name"),
                    formatDate(rs.getDate("registration_date")),
                    rs.getString("payment_status")
                };

                registrationModel.addRow(row);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to load registrations:\n" + ex.getMessage()
            );

        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                // Connection cleanup
            }
        }
    }

    private void confirmSelectedPayment() {

        int selectedRow = registrationTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a registration first."
            );
            return;
        }

        int registrationId = Integer.parseInt(
                registrationModel.getValueAt(selectedRow, 0).toString()
        );

        String currentStatus =
                registrationModel.getValueAt(selectedRow, 5).toString();

        if ("Confirmed".equalsIgnoreCase(currentStatus)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Payment has already been confirmed."
            );
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Have you received the offline payment for this registration?",
                "Confirm Payment",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        String sql =
                "UPDATE registrations SET payment_status = 'Confirmed' "
                + "WHERE registration_id = ?";

        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DBConnection.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, registrationId);

            int result = ps.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Payment confirmed successfully!\n"
                        + "The registration status has been updated."
                );

                loadRegistrations();

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Registration not found."
                );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to confirm payment:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                // Connection cleanup
            }
        }
    }
}