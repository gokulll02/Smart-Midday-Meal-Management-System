package digitalmidday;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;

public class ParentModule extends JFrame {

    JTable table;
    JComboBox<String> issue, desc;
    JLabel status;
    JDateChooser dateChooser;

    public ParentModule(String schoolId,String username){

        setTitle("Parent Dashboard");
        setSize(650,500);
        setLayout(null);
        getContentPane().setBackground(new Color(240,255,240));

        JLabel title = new JLabel("PARENT DASHBOARD");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setBounds(220,10,300,30);
        add(title);
        
        JLabel welcome = new JLabel("Welcome, " + username);
        welcome.setBounds(20,30,250,30);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 15));
        welcome.setForeground(new Color(0,153,76));
        add(welcome);
        
        JButton logout = new JButton("Logout");
        logout.setBounds(500,10,100,30);   
        logout.setBackground(new Color(220,53,69));
        logout.setForeground(Color.WHITE);
        logout.setFocusPainted(false);
        add(logout);

        JButton viewMeal = new JButton("View Daily Meal");
        viewMeal.setBounds(230,55,180,30);
        add(viewMeal);

        table = new JTable();
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(50,100,550,150);
        add(sp);

        JLabel issueLabel = new JLabel("Issue:");
        issueLabel.setBounds(50,270,100,25);
        add(issueLabel);

        issue = new JComboBox<>(new String[]{
            "Food Quality","Stock Issue","Late Delivery"
        });
        issue.setBounds(150,270,200,25);
        add(issue);

        JLabel descLabel = new JLabel("Description:");
        descLabel.setBounds(50,310,100,25);
        add(descLabel);

        desc = new JComboBox<>();
        desc.setBounds(150,310,200,25);
        add(desc);

        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setBounds(50,350,100,25);
        add(dateLabel);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(150,350,200,25);
        add(dateChooser);

        JButton submit = new JButton("Send Feedback");
        submit.setBounds(200,390,180,30);
        add(submit);

        status = new JLabel("");
        status.setBounds(180,430,300,25);
        add(status);

        updateDesc();
        String parentUsername = username;

        issue.addActionListener(e -> updateDesc());

        viewMeal.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(
                    "SELECT meal_date, meal_name FROM meal WHERE school_id=?"
                );
                ps.setLong(1, Long.parseLong(schoolId));

                ResultSet rs = ps.executeQuery();

                DefaultTableModel model = new DefaultTableModel(
                        new String[]{"Date","Meal"}, 0
                );

                while(rs.next()){
                    model.addRow(new Object[]{
                        rs.getDate("meal_date"),
                        rs.getString("meal_name")
                    });
                }

                table.setModel(model);

            } catch(Exception ex){
                ex.printStackTrace();
            }
        });
        logout.addActionListener(e -> {

            int confirm = JOptionPane.showConfirmDialog(
                this,
                    "Are you sure you want to logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if(confirm == JOptionPane.YES_OPTION){
                new HomePage();   
                dispose();        
            }
        });

        submit.addActionListener(e -> {
            try {

                if(issue.getSelectedItem()==null ||
                   desc.getSelectedItem()==null ||
                   dateChooser.getDate()==null){

                    status.setForeground(Color.RED);
                    status.setText("Fill all fields!");
                    return;
                }

                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO feedback1(issue_type, description, date, school_id,parent_username) VALUES(?,?,?,?,?)"
                );

                ps.setString(1, issue.getSelectedItem().toString());
                ps.setString(2, desc.getSelectedItem().toString());
                ps.setDate(3, new java.sql.Date(dateChooser.getDate().getTime()));
                ps.setLong(4, Long.parseLong(schoolId));
                ps.setString(5, parentUsername);

                ps.executeUpdate();

                status.setForeground(new Color(0,128,0));
                status.setText("✔ Feedback Sent Successfully");

            } catch(Exception ex){
                status.setForeground(Color.RED);
                status.setText("✖ Error");
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }

    private void updateDesc(){

        desc.removeAllItems();

        if(issue.getSelectedItem()==null) return;

        String selected = issue.getSelectedItem().toString();

        if(selected.equals("Food Quality")){
            desc.addItem("Bad Taste");
            desc.addItem("Undercooked");
            desc.addItem("Overcooked");
        }
        else if(selected.equals("Stock Issue")){
            desc.addItem("Less Quantity");
            desc.addItem("Missing Items");
            desc.addItem("Incorrect Stock");
        }
        else{
            desc.addItem("Late Delivery");
            desc.addItem("Very Late");
            desc.addItem("Cold Food");
        }

        if(desc.getItemCount() > 0){
            desc.setSelectedIndex(0);
        }
    }
}