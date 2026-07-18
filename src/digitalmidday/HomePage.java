package digitalmidday;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class HomePage extends JFrame {

    public HomePage(){

        setTitle("Mid-day Meal Management System");
        setSize(480, 520);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(UITheme.BG_MAIN);
        JPanel bg = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x1E6FD9, true));
                g2.fillOval(300, -80, 260, 260);
                g2.setColor(new Color(0x1DBF73, true));
                g2.fillOval(-60, 350, 200, 200);
            }
        };
        bg.setOpaque(false);
        bg.setBounds(0, 0, 480, 520);
        bg.setLayout(null);
        add(bg);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("logo.png"));
            Image img = icon.getImage().getScaledInstance(72, 72, Image.SCALE_SMOOTH);
            JLabel logo = new JLabel(new ImageIcon(img));
            logo.setBounds(204, 30, 72, 72);
            add(logo);
        } catch (Exception ignored) {}

        JLabel title = new JLabel("MID-DAY MEAL", SwingConstants.CENTER);
        title.setBounds(40, 115, 400, 32);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(UITheme.TEXT_HEADING);
        add(title);

        JLabel sub = new JLabel("Management System", SwingConstants.CENTER);
        sub.setBounds(40, 148, 400, 22);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        sub.setForeground(UITheme.TEXT_MUTED);
        add(sub);

        JPanel card = UITheme.makeCard();
        card.setBounds(60, 190, 360, 290);
        add(card);

        JLabel schoolLbl = UITheme.makeLabel("School ID");
        schoolLbl.setBounds(20, 14, 100, 22);
        card.add(schoolLbl);

        JTextField school = UITheme.makeField();
        school.setText("33260403601");
        school.setBounds(20, 38, 320, 36);
        card.add(school);

        JLabel divider = new JLabel();
        divider.setOpaque(true);
        divider.setBackground(UITheme.BORDER);
        divider.setBounds(20, 85, 320, 1);
        card.add(divider);

        JLabel choose = UITheme.makeMuted("Login as:");
        choose.setBounds(20, 96, 200, 20);
        card.add(choose);

        JButton admin  = UITheme.makeSuccess(" Admin");
        JButton parent = UITheme.makePrimary("Parent");
        JButton cook   = UITheme.makeWarning("Cook");

        admin .setBounds(20, 124, 320, 42);
        parent.setBounds(20, 176, 320, 42);
        cook  .setBounds(20, 228, 320, 42);

        card.add(admin);
        card.add(parent);
        card.add(cook);

        // ── Footer ────────────────────────────────────────────────────────
        JLabel footer = UITheme.makeMuted("© Digital Midday Meal System");
        footer.setBounds(0, 492, 480, 20);
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        add(footer);

        admin .addActionListener(e -> new AdminLogin(school.getText()));
        parent.addActionListener(e -> new ParentLogin(school.getText()));
        cook  .addActionListener(e -> new CookLogin(school.getText()));

        setVisible(true);
    }
}
