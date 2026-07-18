package digitalmidday;

import javax.swing.*;
import java.sql.*;

public class IssueModule extends JFrame {

    public IssueModule(String schoolId){

        setTitle("Issue Module");
        setSize(350,300);
        setLayout(null);
        JLabel l1 = new JLabel("Issue Type:");
        l1.setBounds(50,20,100,25);
        add(l1);

        JLabel l2 = new JLabel("Description:");
        l2.setBounds(50,80,100,25);
        add(l2);
        String[] types = {"Food Quality","Stock Issue","Late Delivery"};
        JComboBox type = new JComboBox(types);
        type.setBounds(50,50,150,25);
        add(type);

        String[] desc = {"Poor Taste","Less Quantity","Delay","Bad Quality","Missing Items"};
        JComboBox description = new JComboBox(desc);
        description.setBounds(50,100,150,25);
        add(description);

        JButton submit = new JButton("Submit");
        submit.setBounds(50,150,100,30);
        add(submit);

        submit.addActionListener(e -> {
            try{
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(
                "INSERT INTO issue(issue_type,description,school_id) VALUES(?,?,?)");

                ps.setString(1, type.getSelectedItem().toString());
                ps.setString(2, description.getSelectedItem().toString());
                ps.setString(3, schoolId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,"Issue Submitted");

            }catch(Exception ex){ ex.printStackTrace(); }
        });

        setVisible(true);
    }
}
