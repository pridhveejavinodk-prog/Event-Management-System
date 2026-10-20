
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {

    public Main() {
        setTitle("College Event Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel title = new JLabel(
            "College Event Management System",
            JLabel.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel roleLabel = new JLabel("Select Role:");
        String[] roles = {"Admin", "Student"};
        JComboBox roleBox = new JComboBox(roles);

        JLabel userLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.add(roleLabel);
        form.add(roleBox);
        form.add(userLabel);
        form.add(usernameField);
        form.add(passLabel);
        form.add(passwordField);
        form.add(new JLabel(""));
        form.add(loginButton);

        setLayout(new BorderLayout(10, 15));
        add(title, BorderLayout.NORTH);
        add(form, BorderLayout.CENTER);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String role = (String) roleBox.getSelectedItem();
                String username = usernameField.getText().trim();
                String password =
                    new String(passwordField.getPassword());

                if ("Admin".equals(role)
                        && username.equals("admin")
                        && password.equals("admin123")) {

                    JOptionPane.showMessageDialog(
                        Main.this,
                        "Admin login successful!"
                    );

                } else if ("Student".equals(role)) {

                    String name = "";
                    String studentId = "";

                    if (username.equals("student1")
                            && password.equals("devan123")) {
                        name = "DEVANARAYANAN";
                        studentId = "RIT001";

                    } else if (username.equals("student2")
                            && password.equals("ashikh123")) {
                        name = "ASHIKH S";
                        studentId = "RIT002";

                    } else if (username.equals("student3")
                            && password.equals("pridhveeja123")) {
                        name = "PRIDHVEEJA VINOD K";
                        studentId = "RIT003";

                    } else if (username.equals("student4")
                            && password.equals("alameen123")) {
                        name = "AL AMEEN";
                        studentId = "RIT004";
                    }

                    if (!name.isEmpty()) {
                        dispose();

                        StudentDashboard dashboard =
                            new StudentDashboard(
                                name,
                                studentId,
                                "Computer Science & Engineering",
                                "S3"
                            );

                        dashboard.setVisible(true);

                    } else {
                        JOptionPane.showMessageDialog(
                            Main.this,
                            "Invalid username or password!",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }

                } else {
                    JOptionPane.showMessageDialog(
                        Main.this,
                        "Invalid username or password!",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}
