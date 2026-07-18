package digitalmidday;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CookLogin extends JFrame {

    public CookLogin(String schoolId){

        setTitle("Cook Login");
        setSize(400,300);
        setLayout(null);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(255,248,240));

        JLabel title = new JLabel("COOK LOGIN");
        title.setBounds(130,20,200,30);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setForeground(new Color(255,102,0));
        add(title);
        
        JLabel l1 = new JLabel("Username:");
        l1.setBounds(50,80,100,25);
        l1.setFont(new Font("Arial", Font.BOLD, 12));
        add(l1);

        JTextField user = new JTextField();
        user.setBounds(150,80,170,30);
        user.setBorder(BorderFactory.createLineBorder(new Color(255,102,0),2));
        add(user);

        JLabel l2 = new JLabel("Password:");
        l2.setBounds(50,120,100,25);
        l2.setFont(new Font("Arial", Font.BOLD, 12));
        add(l2);

        JPasswordField pass = new JPasswordField();
        pass.setBounds(150,120,170,30);
        pass.setBorder(BorderFactory.createLineBorder(new Color(255,102,0),2));
        add(pass);

        JLabel status = new JLabel("");
        status.setBounds(90,155,250,25);
        add(status);

        JButton login = new JButton("Login");
        login.setBounds(70,200,110,35);
        login.setBackground(new Color(255,140,0));
        login.setForeground(Color.WHITE);
        login.setFocusPainted(false);
        add(login);
        
        JButton signup = new JButton("Signup");
        signup.setBounds(200,200,110,35);
        signup.setBackground(Color.GRAY);
        signup.setForeground(Color.WHITE);
        add(signup);
        
        signup.addActionListener(e -> new CookSignup(schoolId));

        login.addActionListener(e -> {
            try{
                if(user.getText().isEmpty() || pass.getPassword().length == 0){
                    status.setText("❌ Enter all fields");
                    status.setForeground(Color.RED);
                    return;
                }

                long sid = Long.parseLong(schoolId);

                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM cook WHERE username=? AND password_hash=? AND school_id=?");

                ps.setString(1,user.getText());
                ps.setString(2,PasswordUtil.hashPassword(new String(pass.getPassword())));
                ps.setLong(3,sid);

                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    status.setText("✅ Login Success");
                    status.setForeground(new Color(0,128,0));

                    new CookModule(schoolId, user.getText());
                    dispose();
                }else{
                    status.setText("❌ Invalid Login");
                    status.setForeground(Color.RED);
                }

            }catch(NumberFormatException ex){
                status.setText("❌ Invalid School ID");
                status.setForeground(Color.RED);
            }
            catch(Exception ex){
                ex.printStackTrace();
                status.setText("❌ Error");
                status.setForeground(Color.RED);
            }
        });

        setVisible(true);
    }
}