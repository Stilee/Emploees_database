package com.Stilee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeesWindow extends JFrame{

    EmployeesWindow(){


       String[] columnNames = {"First Name",
               "Last Name",
               "Sport",
               "# of Years",
               "Vegetarian"};


       Object[][] data = {
               {"Kathy", "Smith",
                       "Snowboarding", new Integer(5), new Boolean(false)},
               {"John", "Doe",
                       "Rowing", new Integer(3), new Boolean(true)},
               {"Sue", "Black",
                       "Knitting", new Integer(2), new Boolean(false)},
               {"Jane", "White",
                       "Speed reading", new Integer(20), new Boolean(true)},
               {"Joe", "Brown",
                       "Pool", new Integer(10), new Boolean(false)}
       };




        Box mainBox = Box.createHorizontalBox();
        Box controlBox = Box.createVerticalBox();
        JPanel loadDboPanel = new JPanel();



        JTable table = new JTable(data, columnNames);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);

        String[] dbo ={};
        JComboBox dboComboBox= new JComboBox(dbo);
        dboComboBox.setPreferredSize(new Dimension(120, 26));
        //dboComboBox.setSelectedIndex();

        ComboBoxActionListener dboComboBoxListener = new ComboBoxActionListener();
        dboComboBox.addActionListener(dboComboBoxListener);

        JLabel dboSelectLabel = new JLabel("Select Database");
        JButton dboSelectButton = new JButton("Load");

        setSize(700,500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);


        mainBox.add(controlBox);
        mainBox.add(scrollPane);

        loadDboPanel.add(dboSelectLabel);
        loadDboPanel.add(dboComboBox);
        loadDboPanel.add(dboSelectButton);

        controlBox.add(loadDboPanel);


        this.add(mainBox);

        setVisible(true);

    }


    private class ComboBoxActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }



}


