
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EventDetailsPage extends JFrame {

    private String status = "Not Registered";
    private JLabel statusLabel;

    public EventDetailsPage(
            String name,
            String description,
            String dateTime,
            String venue,
            String fee,
            String prizes,
            String paymentInfo) {

        setTitle(name + " - Event Details");
        setSize(800, 700);
        setMinimumSize(new Dimension(700, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        JLabel heading = new JLabel(name);
        heading.setFont(new Font("Arial", Font.BOLD, 26));

        JTextArea descriptionArea = new JTextArea(description);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(false);

        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.setBorder(
            BorderFactory.createTitledBorder("About the Event")
        );
        descriptionPanel.add(descriptionArea, BorderLayout.CENTER);

        JPanel info = new JPanel(new GridLayout(0, 1, 10, 10));
        info.setBorder(
            BorderFactory.createTitledBorder("Event Information")
        );

        addInfo(info, "Event:", name);
        addInfo(info, "Date and Time:", dateTime);
        addInfo(info, "Venue:", venue);
        addInfo(info, "Registration Fee:", fee);
        addInfo(info, "Prizes:", prizes);
        addInfo(info, "Payment Instructions:", paymentInfo);

        statusLabel = new JLabel("Registration Status: " + status);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton registerButton =
            new JButton("Register for This Event");
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (status.equals("Not Registered")) {
                    status = "Waiting for Payment Confirmation";
                    statusLabel.setText(
                        "Registration Status: " + status
                    );
                    registerButton.setEnabled(false);

                    JOptionPane.showMessageDialog(
                        EventDetailsPage.this,
                        "Your registration request has been submitted.\n\n"
                        + "Event: " + name + "\n"
                        + "Registration fee: " + fee + "\n"
                        + "Payment venue: Civil Junction\n"
                        + "Payment hours: 9:00 AM - 4:30 PM\n"
                        + "Payment deadline: 25 October 2026\n\n"
                        + "Waiting for Payment Confirmation.",
                        "Registration Submitted",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });

        JButton closeButton = new JButton("Back to Events");

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel bottom = new JPanel(new GridLayout(0, 1, 10, 10));
        bottom.add(statusLabel);
        bottom.add(registerButton);
        bottom.add(closeButton);

        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.add(descriptionPanel, BorderLayout.NORTH);
        center.add(info, BorderLayout.CENTER);

        main.add(heading, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);

        setContentPane(main);
        setVisible(true);
    }

    private void addInfo(
            JPanel panel, String label, String value) {

        JPanel row = new JPanel(new BorderLayout(10, 5));

        JLabel title = new JLabel(label);
        title.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel content = new JLabel(
            "<html><div style='width:480px'>"
            + escapeHtml(value)
            + "</div></html>"
        );
        content.setFont(new Font("Arial", Font.PLAIN, 14));

        row.add(title, BorderLayout.WEST);
        row.add(content, BorderLayout.CENTER);
        panel.add(row);
    }

    private String escapeHtml(String text) {
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;");
    }
}
