package digitalmidday;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class CookModule extends JFrame {

    public CookModule(String schoolId,String username){

        setTitle("Cook Dashboard");
        setSize(850,650);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        
        JLabel welcome = new JLabel("Welcome, " + username);
        welcome.setBounds(20,10,250,30);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 15));
        welcome.setForeground(new Color(0,153,76));
        add(welcome);
        
        JButton logout = new JButton("Logout");
        logout.setBounds(680,10,100,30);   
        logout.setBackground(new Color(220,53,69));
        logout.setForeground(Color.WHITE);
        logout.setFocusPainted(false);
        add(logout);
        
        JLabel title = new JLabel("COOK DASHBOARD");
        title.setBounds(320,10,300,30);
        title.setFont(new Font("Arial",Font.BOLD,20));
        title.setForeground(new Color(255,102,0));
        add(title);

        JTable mealTable = new JTable();
        JScrollPane sp1 = new JScrollPane(mealTable);
        sp1.setBounds(50,60,750,180);
        add(sp1);

        JButton loadMeals = new JButton("Load Meals");
        loadMeals.setBounds(50,250,150,30);
        loadMeals.setBackground(new Color(0,102,204));
        loadMeals.setForeground(Color.WHITE);
        add(loadMeals);

        JLabel itemLabel = new JLabel("Item:");
        itemLabel.setBounds(50,300,100,25);
        add(itemLabel);

        String items[] = {"Rice","Egg","Oil","Dal","Tamarind","Salt"};
        JComboBox<String> itemBox = new JComboBox<>(items);
        itemBox.setBounds(120,300,150,25);
        add(itemBox);

        JLabel qtyLabel = new JLabel("Quantity:");
        qtyLabel.setBounds(300,300,100,25);
        add(qtyLabel);

        JTextField qtyField = new JTextField();
        qtyField.setBounds(380,300,150,25);
        add(qtyField);

        JButton addItem = new JButton("Add Item");
        addItem.setBounds(560,300,120,30);
        add(addItem);

        DefaultTableModel usedModel = new DefaultTableModel(
                new String[]{"Item","Quantity"},0);

        JTable usedTable = new JTable(usedModel);
        JScrollPane sp2 = new JScrollPane(usedTable);
        sp2.setBounds(50,340,350,150);
        add(sp2);

        JTable stockTable = new JTable();
        JScrollPane sp3 = new JScrollPane(stockTable);
        sp3.setBounds(420,340,380,150);
        add(sp3);

        JButton loadStock = new JButton("View Stock");
        loadStock.setBounds(420,500,180,30);
        add(loadStock);

        JButton send = new JButton("Send Used Stock");
        send.setBounds(250,520,200,40);
        send.setBackground(new Color(0,153,76));
        send.setForeground(Color.WHITE);
        add(send);        
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
        loadMeals.addActionListener(e -> {
            try{
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(
                "SELECT meal_date, meal_name, total_students FROM meal WHERE school_id=?");

                ps.setLong(1, Long.parseLong(schoolId));

                ResultSet rs = ps.executeQuery();

                DefaultTableModel model = new DefaultTableModel(
                new String[]{"Date","Meal","Students"},0);

                while(rs.next()){
                    model.addRow(new Object[]{
                        rs.getString("meal_date"),
                        rs.getString("meal_name"),
                        rs.getInt("total_students")
                    });
                }

                mealTable.setModel(model);

            }catch(Exception ex){ ex.printStackTrace(); }
        });
loadStock.addActionListener(e -> {
    try{
        Connection con = DBConnection.getConnection();

        PreparedStatement ps = con.prepareStatement(
        "SELECT item_name, quantity, " +
        "check_low_stock(item_name, quantity) AS status " +
        "FROM stock WHERE school_id=?");

        ps.setLong(1, Long.parseLong(schoolId));

        ResultSet rs = ps.executeQuery();

        DefaultTableModel model = new DefaultTableModel(
        new String[]{"Item","Available","Status"},0);

        while(rs.next()){

            String item = rs.getString("item_name");
            double qty = rs.getDouble("quantity");
            String status = rs.getString("status");

            if(status.equals("LOW STOCK")){

                JOptionPane.showMessageDialog(
                    this,
                    "⚠ LOW STOCK ALERT!\n\n" +
                    "Item: " + item +
                    "\nRemaining Quantity: " + qty
                );

                SMSUtil.sendSMS(item, qty);
            }

            model.addRow(new Object[]{
                item,
                qty,
                status
            });
        }

        stockTable.setModel(model);

        stockTable.setDefaultRenderer(
            Object.class,
            new DefaultTableCellRenderer(){

                @Override
                public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int col){

                    Component c =
                    super.getTableCellRendererComponent(
                        table,value,isSelected,
                        hasFocus,row,col
                    );

                    String status =
                    table.getValueAt(row,2).toString();

                    if(status.equals("LOW STOCK")){
                        c.setBackground(new Color(255,204,204));
                    }else{
                        c.setBackground(Color.WHITE);
                    }

                    return c;
                }
        });

    }catch(Exception ex){
        ex.printStackTrace();
    }
});
        addItem.addActionListener(e -> {
            try{
                double qty = Double.parseDouble(qtyField.getText());

                if(qty <= 0){
                    JOptionPane.showMessageDialog(this,"❌ Invalid Quantity");
                    return;
                }

                usedModel.addRow(new Object[]{
                        itemBox.getSelectedItem().toString(),
                        qty
                });

                qtyField.setText("");

            }catch(Exception ex){
                JOptionPane.showMessageDialog(this,"❌ Enter valid number");
            }
        });

        send.addActionListener(e -> {
            try{
                int row = mealTable.getSelectedRow();

                if(row == -1){
                    JOptionPane.showMessageDialog(this,"❌ Select Meal Row");
                    return;
                }

                if(usedModel.getRowCount() == 0){
                    JOptionPane.showMessageDialog(this,"❌ Add at least one item");
                    return;
                }

                String date = mealTable.getValueAt(row,0).toString();

                Connection con = DBConnection.getConnection();

                for(int i=0;i<usedModel.getRowCount();i++){

                    String item = usedModel.getValueAt(i,0).toString();
                    double qty = Double.parseDouble(
                            usedModel.getValueAt(i,1).toString()
                    );

                    PreparedStatement check = con.prepareStatement(
                    "SELECT quantity FROM stock WHERE item_name=? AND school_id=?");

                    check.setString(1,item);
                    check.setLong(2,Long.parseLong(schoolId));

                    ResultSet rs = check.executeQuery();

                    if(rs.next()){
                        double available = rs.getDouble("quantity");

                        if(qty > available){
                            JOptionPane.showMessageDialog(this,
                            "❌ Not enough stock for " + item +
                            "\nAvailable: " + available);
                            return;
                        }
                    }else{
                        JOptionPane.showMessageDialog(this,
                        "❌ Item not found in stock: " + item);
                        return;
                    }

                    PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO used_stock(item_name,quantity_used,meal_date,school_id) VALUES(?,?,?,?)");

                    ps.setString(1,item);
                    ps.setDouble(2,qty);
                    ps.setString(3,date);
                    ps.setLong(4,Long.parseLong(schoolId));

                    ps.executeUpdate();
                }

                JOptionPane.showMessageDialog(this,"✅ Sent Successfully");
                usedModel.setRowCount(0);

            }catch(Exception ex){
                JOptionPane.showMessageDialog(this,"❌ Error");
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }
}
