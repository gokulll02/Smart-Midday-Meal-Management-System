package digitalmidday;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CookSignup extends JFrame {

    public CookSignup(String schoolId){

        setTitle("Cook Signup");
        setSize(300,250);
        setLayout(null);

        JLabel u = new JLabel("Username:");
        u.setBounds(30,50,80,25);
        add(u);

        JTextField user = new JTextField();
        user.setBounds(120,50,120,25);
        add(user);

        JLabel p = new JLabel("Password:");
        p.setBounds(30,90,80,25);
        add(p);

        JPasswordField pass = new JPasswordField();
        pass.setBounds(120,90,120,25);
        add(pass);

        JButton signup = new JButton("Signup");
        signup.setBounds(100,140,100,30);
        add(signup);

        signup.addActionListener(e -> {
            try{
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(
                "INSERT INTO cook(username,password_hash,school_id) VALUES(?,?,?)");

                ps.setString(1,user.getText());
                ps.setString(2,PasswordUtil.hashPassword(new String(pass.getPassword())));
                ps.setString(3,schoolId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,"Signup Success ✅");
                dispose();

            }catch(Exception ex){ ex.printStackTrace(); }
        });

        setVisible(true);
    }
}
