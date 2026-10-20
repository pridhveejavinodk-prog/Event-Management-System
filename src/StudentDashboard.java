
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StudentDashboard extends JFrame {

    private final String[] events = {
        "Tech Fest",
        "Arts Fest",
        "Football Tournament",
        "Java Workshop",
        "Coding Competition"
    };

    private final String[] categories = {
        "Technical",
        "Cultural",
        "Sports",
        "Workshop",
        "Technical"
    };

    private final String[] descriptions = {
        "A technical festival featuring coding contests, project exhibitions and technical activities.",
        "A cultural festival featuring music, dance, artistic performances and exciting competitions.",
        "A college football tournament for participating teams.",
        "A practical workshop introducing Java programming concepts.",
        "A coding competition to test programming and problem-solving skills."
    };

    private final String[] dates = {
        "25 October 2026",
        "28 October 2026",
        "30 October 2026",
        "2 November 2026",
        "5 November 2026"
    };

    private final String[] times = {
        "10:00 AM - 4:00 PM",
        "10:00 AM - 4:00 PM",
        "9:00 AM - 3:00 PM",
        "10:00 AM - 1:00 PM",
        "10:00 AM - 2:00 PM"
    };

    private final String[] venues = {
        "Main Block",
        "College Auditorium",
        "College Ground",
        "Computer Lab",
        "CSE Lab"
    };

    private final String[] prizes = {
        "Prizes to be announced",
        "Prizes to be announced",
        "Prizes to be announced",
        "Certificates for participants",
        "Prizes to be announced"
    };

    private final String[] fees = {
        "Rs. 100",
        "Rs. 500",
        "Rs. 50",
        "Rs. 100",
        "Rs. 75"
    };

    private final DefaultListModel eventModel =
        new DefaultListModel();

    private final JList eventList = new JList(eventModel);

    private JTextField searchField;

    public StudentDashboard(
            String name,
            String id,
            String department,
            String semester) {

        setTitle("College Event Management - Student Dashboard");
        setSize(850, 650);
        setMinimumSize(new Dimension(700, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        JLabel heading = new JLabel("Student Dashboard");
        heading.setFont(new Font("Arial", Font.BOLD, 27));

        JPanel profile = new JPanel(new GridLayout(0, 1, 5, 5));
        profile.setBorder(
            BorderFactory.createTitledBorder("Student Profile")
        );

        profile.add(new JLabel("Name: " + name));
        profile.add(new JLabel("Student ID: " + id));
        profile.add(new JLabel("Department: " + department));
        profile.add(new JLabel("Semester: " + semester));

        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.add(heading, BorderLayout.NORTH);
        top.add(profile, BorderLayout.CENTER);

        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 15));
        searchField.setToolTipText("Enter event name or category");

        JButton searchButton = new JButton("Search");

        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.add(
            new JLabel("Search Events:"),
            BorderLayout.WEST
        );
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        eventList.setFont(new Font("Arial", Font.PLAIN, 17));
        eventList.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );
        eventList.setFixedCellHeight(35);

        JButton detailsButton = new JButton("View Details");
        detailsButton.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel eventPanel = new JPanel(new BorderLayout(10, 10));
        eventPanel.setBorder(
            BorderFactory.createTitledBorder("Available Events")
        );

        eventPanel.add(searchPanel, BorderLayout.NORTH);
        eventPanel.add(
            new JScrollPane(eventList),
            BorderLayout.CENTER
        );
        eventPanel.add(detailsButton, BorderLayout.SOUTH);

        main.add(top, BorderLayout.NORTH);
        main.add(eventPanel, BorderLayout.CENTER);
        add(main);

        showEvents("");

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showEvents(searchField.getText());
            }
        });

        searchField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showEvents(searchField.getText());
            }
        });

        detailsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openSelectedEvent();
            }
        });
    }

    private void showEvents(String keyword) {
        eventModel.clear();
        String searchText = keyword.trim().toLowerCase();

        for (int i = 0; i < events.length; i++) {
            if (events[i].toLowerCase().contains(searchText)
                    || categories[i].toLowerCase().contains(searchText)) {
                eventModel.addElement(events[i]);
            }
        }
    }

    private void openSelectedEvent() {
        String selected = (String) eventList.getSelectedValue();

        if (selected == null) {
            JOptionPane.showMessageDialog(
                this,
                "Please select an event first.",
                "No Event Selected",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        for (int i = 0; i < events.length; i++) {
            if (events[i].equals(selected)) {

                String paymentLocation =
                    "Pay at Civil Junction; 9:00 AM - 4:30 PM";

                String paymentDeadline =
                    "Payment deadline: 25 October 2026";

                EventDetailsPage detailsPage = new EventDetailsPage(
                    events[i] + " - " + categories[i],
                    descriptions[i],
                    dates[i] + " | " + times[i],
                    venues[i],
                    fees[i],
                    "Prizes: " + prizes[i],
                    paymentLocation + " | " + paymentDeadline
                );

                return;
            }
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
