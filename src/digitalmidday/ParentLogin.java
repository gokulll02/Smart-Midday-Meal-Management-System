package digitalmidday;

import javax.swing.*;
import java.sql.*;
import java.awt.*;

public class ParentLogin extends JFrame {

    public ParentLogin(String schoolId){

        setTitle("Parent Login");
        setSize(350,300);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel title = new JLabel("Parent Login");
        title.setBounds(110,10,150,30);
        title.setFont(new Font("Arial",Font.BOLD,16));
        add(title);

        JLabel l1 = new JLabel("Username:");
        l1.setBounds(40,60,80,25);
        add(l1);

        JTextField user = new JTextField();
        user.setBounds(140,60,150,25);
        add(user);

        JLabel l2 = new JLabel("Password:");
        l2.setBounds(40,100,80,25);
        add(l2);

        JPasswordField pass = new JPasswordField();
        pass.setBounds(140,100,150,25);
        add(pass);

        JLabel status = new JLabel("");
        status.setBounds(90,135,200,25);
        add(status);

        JButton login = new JButton("Login");
        login.setBounds(60,170,100,30);
        login.setBackground(new Color(0,153,76));
        login.setForeground(Color.WHITE);
        add(login);

        JButton signup = new JButton("Signup");
        signup.setBounds(180,170,100,30);
        signup.setBackground(Color.GRAY);
        signup.setForeground(Color.WHITE);
        add(signup);

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
                "SELECT * FROM parent_login WHERE username=? AND password_hash=? AND school_id=?");

                ps.setString(1,user.getText());
                ps.setString(2,PasswordUtil.hashPassword(new String(pass.getPassword())));
                ps.setLong(3,sid);

                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    status.setText("✅ Login Success");
                    status.setForeground(new Color(0,128,0));

                    new ParentModule(schoolId,user.getText());
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

        signup.addActionListener(e -> new ParentSignup(schoolId));

        setVisible(true);
    }
}
