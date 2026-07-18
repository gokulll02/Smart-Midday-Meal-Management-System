package digitalmidday;

import javax.swing.*;
import java.sql.*;

public class MealModule extends JFrame {

    public MealModule(String schoolId){

        setTitle("Meal Entry");
        setSize(400,300);
        setLayout(null);

        String[] meals = {
            "Vegetable Biriyani + Pepper Egg",
            "Tomato Rice + Pepper Egg",
            "Rice + Sambar + Boiled Egg",
            "Curry Leaf Rice + Masala Egg",
            "Keerai Sadham + Masala Egg + Fried Potato"
        };

        JComboBox meal = new JComboBox(meals);
        meal.setBounds(50,50,250,25);
        add(meal);
        JLabel l1 = new JLabel("Meal:");
        l1.setBounds(50,20,100,25);
        add(l1);

        JLabel l2 = new JLabel("Date:");
        l2.setBounds(50,80,100,25);
        add(l2);

        JLabel l3 = new JLabel("Students:");
        l3.setBounds(50,130,100,25);
        add(l3);
        JTextField date = new JTextField("");
        date.setBounds(50,100,150,25);
        add(date);

        JTextField students = new JTextField();
        students.setBounds(50,150,150,25);
        add(students);

        JButton add = new JButton("Add Meal");
        add.setBounds(50,200,150,30);
        add(add);

        add.addActionListener(e -> {
            try{
                CallableStatement cs = DBConnection.getConnection()
                .prepareCall("{call add_meal(?,?,?,?)}");

                cs.setString(1, meal.getSelectedItem().toString());
                cs.setString(2, date.getText());
                cs.setInt(3, Integer.parseInt(students.getText()));
                cs.setString(4, schoolId);

                cs.execute();

                JOptionPane.showMessageDialog(this,"Meal Added");

            }catch(Exception ex){
                JOptionPane.showMessageDialog(this,ex.getMessage());
            }
        });

        setVisible(true);
    }
}
