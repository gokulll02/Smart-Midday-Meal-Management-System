package digitalmidday;

import javax.swing.*;
import java.sql.*;

public class ParentSignup extends JFrame {

    public ParentSignup(String schoolId){

        setTitle("Parent Signup");
        setSize(300,300);
        setLayout(null);

        JLabel l0 = new JLabel("Student ID:");
        l0.setBounds(20,30,80,25);
        add(l0);

        JTextField studentId = new JTextField();
        studentId.setBounds(120,30,120,25);
        add(studentId);

        JLabel l1 = new JLabel("Username:");
        l1.setBounds(20,70,80,25);
        add(l1);

        JTextField user = new JTextField();
        user.setBounds(120,70,120,25);
        add(user);

        JLabel l2 = new JLabel("Password:");
        l2.setBounds(20,110,80,25);
        add(l2);

        JPasswordField pass = new JPasswordField();
        pass.setBounds(120,110,120,25);
        add(pass);

        JButton create = new JButton("Create");
        create.setBounds(100,160,100,30);
        add(create);

        create.addActionListener(e -> {
            try{
                Connection con = DBConnection.getConnection();

                PreparedStatement checkStudent = con.prepareStatement(
                "SELECT * FROM student WHERE student_id=?");
                checkStudent.setString(1,studentId.getText());

                if(!checkStudent.executeQuery().next()){
                    JOptionPane.showMessageDialog(this,"Invalid Student");
                    return;
                }

                PreparedStatement ps = con.prepareStatement(
                "INSERT INTO parent_login(username,password_hash,student_id,school_id) VALUES(?,?,?,?)");

                ps.setString(1,user.getText());
                ps.setString(2,PasswordUtil.hashPassword(new String(pass.getPassword())));
                ps.setString(3,studentId.getText());
                ps.setString(4,schoolId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,"Account Created");

            }catch(Exception ex){ ex.printStackTrace(); }
        });

        setVisible(true);
    }
}
