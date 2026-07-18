package digitalmidday;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.HashSet;

public class ReportModule extends JFrame {

    JTable table;
    JLabel totalLabel;

    public ReportModule(String schoolId){

        setTitle("Reports");
        setSize(900,650);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240,248,255));

        JLabel title = new JLabel("REPORTS");
        title.setBounds(300,10,300,30);
        title.setFont(new Font("Arial",Font.BOLD,20));
        title.setForeground(new Color(0,102,204));
        add(title);

        JLabel fromLabel = new JLabel("From:");
        fromLabel.setBounds(50,60,80,25);
        add(fromLabel);

        JDateChooser fromDate = new JDateChooser();
        fromDate.setBounds(100,60,150,25);
        add(fromDate);

        JLabel toLabel = new JLabel("To:");
        toLabel.setBounds(280,60,50,25);
        add(toLabel);

        JDateChooser toDate = new JDateChooser();
        toDate.setBounds(320,60,150,25);
        add(toDate);

        JButton load = new JButton("Load Report");
        load.setBounds(500,60,150,30);
        load.setBackground(new Color(0,153,76));
        load.setForeground(Color.WHITE);
        add(load);

        JButton pdf = new JButton("Download PDF");
        pdf.setBounds(670,60,160,30);
        pdf.setBackground(new Color(0,102,204));
        pdf.setForeground(Color.WHITE);
        add(pdf);

        table = new JTable();
        table.setRowHeight(25);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(50,120,800,400);
        add(sp);

        totalLabel = new JLabel("Total Students: 0");
        totalLabel.setBounds(50,540,300,25);
        totalLabel.setFont(new Font("Arial",Font.BOLD,14));
        totalLabel.setForeground(new Color(0,102,204));
        add(totalLabel);

        load.addActionListener(e -> {
            try{

                if(fromDate.getDate()==null || toDate.getDate()==null){
                    JOptionPane.showMessageDialog(this,"❌ Select dates");
                    return;
                }

                String from = new SimpleDateFormat("yyyy-MM-dd").format(fromDate.getDate());
                String to = new SimpleDateFormat("yyyy-MM-dd").format(toDate.getDate());

                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(
                "SELECT m.meal_date, m.meal_name, m.total_students, " +
                "u.item_name, u.quantity_used " +
                "FROM meal m LEFT JOIN used_stock u " +
                "ON m.meal_date = u.meal_date AND m.school_id=u.school_id " +
                "WHERE m.school_id=? AND m.meal_date BETWEEN ? AND ? " +
                "ORDER BY m.meal_date");

                ps.setLong(1, Long.parseLong(schoolId));
                ps.setString(2, from);
                ps.setString(3, to);

                ResultSet rs = ps.executeQuery();

                DefaultTableModel model = new DefaultTableModel(
                new String[]{"Date","Meal","Students","Item","Used Qty"},0);

                HashSet<String> countedDates = new HashSet<>();
                int totalStudents = 0;

                while(rs.next()){

                    String date = rs.getString("meal_date");
                    if(!countedDates.contains(date)){
                        totalStudents += rs.getInt("total_students");
                        countedDates.add(date);
                    }

                    model.addRow(new Object[]{
                        date,
                        rs.getString("meal_name"),
                        rs.getInt("total_students"),
                        rs.getString("item_name"),
                        rs.getDouble("quantity_used")
                    });
                }

                table.setModel(model);
                totalLabel.setText("Total Students: " + totalStudents);

            }catch(Exception ex){
                ex.printStackTrace();
            }
        });

        // ===== PDF BUTTON =====
        pdf.addActionListener(e -> {
            PDFReport.generateReport(table, schoolId);
        });

        setVisible(true);
    }
}
