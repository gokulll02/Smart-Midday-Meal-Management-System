package digitalmidday;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UpdateStockFrame extends JFrame {

    public UpdateStockFrame(String schoolId){

        setTitle("Update Stock");
        setSize(350,300);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        JLabel title = new JLabel("UPDATE STOCK");
        title.setBounds(90,10,200,30);
        title.setFont(new Font("Arial",Font.BOLD,16));
        title.setForeground(new Color(0,102,204));
        add(title);

        JLabel itemLabel = new JLabel("Item:");
        itemLabel.setBounds(40,60,100,25);
        add(itemLabel);

        String items[] = {"Rice","Egg","Oil","Dal","Tamarind","Salt"};

        JComboBox<String> itemBox = new JComboBox<>(items);
        itemBox.setBounds(140,60,150,25);
        add(itemBox);

        JLabel qtyLabel = new JLabel("Quantity:");
        qtyLabel.setBounds(40,110,100,25);
        add(qtyLabel);

        JTextField qtyField = new JTextField();
        qtyField.setBounds(140,110,150,25);
        add(qtyField);

        JButton update = new JButton("Update");
        update.setBounds(100,170,120,30);
        update.setBackground(new Color(0,153,76));
        update.setForeground(Color.WHITE);
        add(update);

        update.addActionListener(e -> {
            try {

                String item = itemBox.getSelectedItem().toString();
                double qty = Double.parseDouble(qtyField.getText());

                if(qty <= 0){
                    JOptionPane.showMessageDialog(this,"❌ Invalid Quantity");
                    return;
                }

                Connection con = DBConnection.getConnection();

                PreparedStatement check = con.prepareStatement(
                "SELECT * FROM stock WHERE item_name=? AND school_id=?");

                check.setString(1,item);
                check.setLong(2, Long.parseLong(schoolId));

                ResultSet rs = check.executeQuery();

                if(rs.next()){
                    PreparedStatement ps = con.prepareStatement(
                    "UPDATE stock SET quantity = quantity + ? WHERE item_name=? AND school_id=?");

                    ps.setDouble(1,qty);
                    ps.setString(2,item);
                    ps.setLong(3, Long.parseLong(schoolId));
                    ps.executeUpdate();
                }else{
                    PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO stock(item_name,quantity,school_id) VALUES(?,?,?)");

                    ps.setString(1,item);
                    ps.setDouble(2,qty);
                    ps.setLong(3, Long.parseLong(schoolId));
                    ps.executeUpdate();
                }

                JOptionPane.showMessageDialog(this,"✅ Stock Updated");

            } catch(Exception ex){
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }
}
