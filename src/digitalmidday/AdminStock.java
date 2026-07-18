package digitalmidday;

import javax.swing.*;
import java.sql.*;

public class AdminStock extends JFrame {
    public AdminStock(String schoolId){
        setTitle("Admin Stock");
        setSize(400,400);
        setLayout(null);

        JLabel l1 = new JLabel("Item:");
        l1.setBounds(50,20,100,25);
        add(l1);

        String[] items = {"Rice","Egg","Oil","Dal","Tamarind","Salt"};
        JComboBox item = new JComboBox(items);
        item.setBounds(50,50,150,25);
        add(item);

        JLabel l2 = new JLabel("Quantity:");
        l2.setBounds(50,80,100,25);
        add(l2);

        JTextField qty = new JTextField();
        qty.setBounds(50,110,150,25);
        add(qty);
        JButton addBtn = new JButton("Add Stock");
        addBtn.setBounds(50,150,120,30);
        add(addBtn);

        JButton viewBtn = new JButton("View");
        viewBtn.setBounds(200,150,100,30);
        add(viewBtn);
        JTable table = new JTable();
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(50,200,300,120);
        add(sp);
        addBtn.addActionListener(e -> {

            String text = qty.getText().trim();

            if(text.isEmpty()){
                JOptionPane.showMessageDialog(this, "Quantity cannot be empty");
                return;
            }

            int quantity;
            try{
                quantity = Integer.parseInt(text);
            }catch(Exception ex){
                JOptionPane.showMessageDialog(this, "Enter valid number");
                return;
            }
            if(quantity <= 0){
                JOptionPane.showMessageDialog(this, "Enter positive quantity");
                return;
            }
            try{
                CallableStatement cs = DBConnection.getConnection()
                .prepareCall("{call add_stock(?,?,?)}");

                cs.setString(1, item.getSelectedItem().toString());
                cs.setInt(2, quantity);
                cs.setString(3, schoolId);

                cs.execute();

                JOptionPane.showMessageDialog(this,"Stock Added Successfully");

                qty.setText(""); // clear field

            }catch(Exception ex){
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        viewBtn.addActionListener(e -> {
            try{
                Connection con = DBConnection.getConnection();

                PreparedStatement ps = con.prepareStatement(
                "SELECT item_name, quantity FROM stock WHERE school_id=?");

                ps.setString(1, schoolId);
                ResultSet rs = ps.executeQuery();

                table.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][]{},
                    new String[]{"Item","Stock"}
                ));

                javax.swing.table.DefaultTableModel model =
                (javax.swing.table.DefaultTableModel)table.getModel();

                while(rs.next()){
                    model.addRow(new Object[]{
                        rs.getString(1),
                        rs.getInt(2)
                    });
                }

            }catch(Exception ex){
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }
}
