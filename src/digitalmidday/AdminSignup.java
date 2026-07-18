package digitalmidday;

import javax.swing.*;
import java.sql.*;

public class AdminSignup extends JFrame {

    public AdminSignup(String schoolId){

        setTitle("Adminstrator Signup");
        setSize(300,250);
        setLayout(null);

        JLabel l1 = new JLabel("Username:");
        l1.setBounds(30,50,80,25);
        add(l1);

        JTextField user = new JTextField();
        user.setBounds(120,50,120,25);
        add(user);

        JLabel l2 = new JLabel("Password:");
        l2.setBounds(30,90,80,25);
        add(l2);

        JPasswordField pass = new JPasswordField();
        pass.setBounds(120,90,120,25);
        add(pass);

        JButton create = new JButton("Create");
        create.setBounds(100,130,100,30);
        add(create);

        create.addActionListener(e -> {
            try{
                Connection con = DBConnection.getConnection();

                PreparedStatement check = con.prepareStatement(
                "SELECT * FROM admin WHERE username=?");
                check.setString(1,user.getText());

                if(check.executeQuery().next()){
                    JOptionPane.showMessageDialog(this,"Username Exists");
                    return;
                }

                PreparedStatement ps = con.prepareStatement(
                "INSERT INTO admin(username,password_hash,school_id) VALUES(?,?,?)");

                ps.setString(1,user.getText());
                ps.setString(2,PasswordUtil.hashPassword(new String(pass.getPassword())));
                ps.setString(3,schoolId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,"Account Created");

            }catch(Exception ex){ ex.printStackTrace(); }
        });

        setVisible(true);
    }
}
