package digitalmidday;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdminModule extends JFrame {

    public AdminModule(String schoolId,String username) {

        setTitle("Administrator Dashboard");
        setSize(800,650);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        
        JLabel welcome = new JLabel("Welcome, " + username );
        welcome.setBounds(20,10,250,30);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 15));
        welcome.setForeground(new Color(0,153,76));
        add(welcome);

        JLabel title = new JLabel("ADMINISTRATOR DASHBOARD");
        title.setBounds(280,10,350,30);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(new Color(0,102,204));
        add(title);

        JButton logout = new JButton("Logout");
        logout.setBounds(680,10,100,30);   
        logout.setBackground(new Color(220,53,69));
        logout.setForeground(Color.WHITE);
        logout.setFocusPainted(false);
        add(logout);

        JLabel mealLabel = new JLabel("Meal:");
        mealLabel.setBounds(50,60,100,25);
        add(mealLabel);

        String meals[] = {
            "Vegetable Biryani + Pepper Egg",
            "Tomato Rice + Pepper Egg",
            "Rice + Sambar + Boiled Egg",
            "Curry Leaf Rice + Masala Egg",
            "Keerai + Masala Egg + Fried Potato"
        };

        JComboBox<String> mealBox = new JComboBox<>(meals);
        mealBox.setBounds(150,60,220,25);
        add(mealBox);

        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setBounds(50,100,100,25);
        add(dateLabel);

        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setBounds(150,100,220,25);
        add(dateChooser);

        JLabel studentLabel = new JLabel("Students:");
        studentLabel.setBounds(50,140,100,25);
        add(studentLabel);

        JTextField students = new JTextField();
        students.setBounds(150,140,220,25);
        add(students);

        JButton addMeal = new JButton("Add Meal");
        addMeal.setBounds(150,180,120,30);
        addMeal.setBackground(new Color(0,153,76));
        addMeal.setForeground(Color.WHITE);
        add(addMeal);

        JLabel msg = new JLabel("");
        msg.setBounds(150,220,300,25);
        add(msg);

        JButton viewStock = new JButton("View Stock");
        viewStock.setBounds(450,60,150,30);
        viewStock.setBackground(new Color(70,130,180));
        viewStock.setForeground(Color.WHITE);
        add(viewStock);

        JButton viewUsed = new JButton("Used Stock");
        viewUsed.setBounds(450,100,150,30);
        viewUsed.setBackground(new Color(100,149,237));
        viewUsed.setForeground(Color.WHITE);
        add(viewUsed);

        JButton viewFeedback = new JButton("View Feedback");
        viewFeedback.setBounds(450,140,150,30);
        viewFeedback.setBackground(new Color(72,61,139));
        viewFeedback.setForeground(Color.WHITE);
        add(viewFeedback);

        JButton updateStock = new JButton("Update Stock");
        updateStock.setBounds(450,190,150,30);
        updateStock.setBackground(new Color(0,153,76));
        updateStock.setForeground(Color.WHITE);
        add(updateStock);

        JTable table = new JTable();
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(50,320,700,200);
        add(sp);

        JLabel responseLabel = new JLabel("Response:");
        responseLabel.setBounds(50,540,100,25);
        add(responseLabel);

        JTextField responseField = new JTextField();
        responseField.setBounds(150,540,300,25);
        add(responseField);

        JButton sendResponse = new JButton("Send Response");
        sendResponse.setBounds(470,540,150,30);
        sendResponse.setBackground(new Color(0,102,204));
        sendResponse.setForeground(Color.WHITE);
        add(sendResponse);
        
        JButton pdfBtn = new JButton("Download PDF");
        pdfBtn.setBounds(600,540,150,30);
        add(pdfBtn);
        
        JButton reports = new JButton("Reports");
        reports.setBounds(450,230,150,30);
        reports.setBackground(new Color(0,153,76));
        reports.setForeground(Color.WHITE);
        add(reports);

        reports.addActionListener(e -> new ReportModule(schoolId));

        addMeal.addActionListener(e -> {
            try {
                if (dateChooser.getDate() == null) {
                    msg.setText("❌ Select Date");
                    msg.setForeground(Color.RED);
                    return;
                }

                Calendar cal = Calendar.getInstance();
                cal.setTime(dateChooser.getDate());

                int day = cal.get(Calendar.DAY_OF_WEEK);

                if(day == Calendar.SATURDAY || day == Calendar.SUNDAY){
                    msg.setText("❌ Cannot add meal on Weekend");
                    msg.setForeground(Color.RED);
                    return;
                }

                int total = Integer.parseInt(students.getText());

                if (total <= 0 || total > 124) {
                    msg.setText("❌ Invalid student count");
                    msg.setForeground(Color.RED);
                    return;
                }

                String meal = mealBox.getSelectedItem().toString();

                String date = new SimpleDateFormat("yyyy-MM-dd")
                        .format(dateChooser.getDate());

                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(
                "{call add_meal(?,?,?,?)}\");");

                ps.setString(1, meal);
                ps.setString(2, date);
                ps.setInt(3, total);
                ps.setLong(4, Long.parseLong(schoolId));

                ps.executeUpdate();

                msg.setText("✅ Meal Added");
                msg.setForeground(new Color(0,128,0));

            } catch (Exception ex) {
                msg.setText("❌ Error");
                msg.setForeground(Color.RED);
                ex.printStackTrace();
            }
        });

        viewStock.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(
                "SELECT item_name, quantity FROM stock WHERE school_id=?");
                ps.setLong(1, Long.parseLong(schoolId));

                ResultSet rs = ps.executeQuery();

                DefaultTableModel model = new DefaultTableModel(
                new String[]{"Item", "Quantity"},0);

                while(rs.next()){
                    model.addRow(new Object[]{
                        rs.getString(1),
                        rs.getDouble(2)
                    });
                }

                table.setModel(model);

            } catch(Exception ex){ ex.printStackTrace(); }
        });

        viewUsed.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(
                "SELECT meal_date,item_name,quantity_used FROM used_stock WHERE school_id=?");

                ps.setLong(1, Long.parseLong(schoolId));
                ResultSet rs = ps.executeQuery();

                DefaultTableModel model = new DefaultTableModel(
                new String[]{"Date","Item_name","Used_Quantity"},0);

                while(rs.next()){
                    model.addRow(new Object[]{
                        rs.getString(1),
                        rs.getString(2),
                        rs.getDouble(3)
                    });
                }

                table.setModel(model);

            } catch(Exception ex){ ex.printStackTrace(); }
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
        viewFeedback.addActionListener(e -> {
            try {
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(
                "SELECT feedback_id,parent_username,issue_type,description,date FROM feedback1 WHERE school_id=?");

                ps.setLong(1, Long.parseLong(schoolId));

                ResultSet rs = ps.executeQuery();

                DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID","Parent","Issue","Description","Date"},0);

                while(rs.next()){
                    model.addRow(new Object[]{
                        rs.getInt("feedback_id"),
                        rs.getString("parent_username"),
                        rs.getString("issue_type"),
                        rs.getString("description"),
                        rs.getString("date")
                    });
                }

                table.setModel(model);

            } catch(Exception ex){ ex.printStackTrace(); }
        });
        sendResponse.addActionListener(e -> {
            try {
                int row = table.getSelectedRow();

                if(row == -1){
                    JOptionPane.showMessageDialog(this,"❌ Select feedback row");
                    return;
                }

                int feedbackId = Integer.parseInt(
                        table.getValueAt(row,0).toString()
                );

                String response = responseField.getText();

                if(response.isEmpty()){
                    JOptionPane.showMessageDialog(this,"❌ Enter response");
                    return;
                }

                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(
                "UPDATE feedback1 SET response=? WHERE feedback_id=?");

                ps.setString(1,response);
                ps.setInt(2,feedbackId);

                ps.executeUpdate();

                JOptionPane.showMessageDialog(this,"✅ Response Sent");

            } catch(Exception ex){
                ex.printStackTrace();
            }
        });

        updateStock.addActionListener(e -> {
            new UpdateStockFrame(schoolId); 
        });
        
        pdfBtn.addActionListener(e -> {
        PDFReport.generateReport(table, schoolId);
        JOptionPane.showMessageDialog(this, "PDF Downloaded ✅");
        });

        setVisible(true);
    }
}
