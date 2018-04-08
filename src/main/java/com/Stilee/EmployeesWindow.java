package com.Stilee;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class EmployeesWindow extends JFrame{

    SQLDatabaseConnection database = new SQLDatabaseConnection();
    Object[][] data = database.getData();

    DefaultTableModel tableModel = new DefaultTableModel(0,3);
    JTable table = new JTable(tableModel){
        public boolean isCellEditable(int row, int column){
            return  false;
        }
    };


    Box mainBox = Box.createHorizontalBox();
    Box controlBox = Box.createVerticalBox();

    JPanel loadDboPanel = new JPanel();
    JPanel namePanel = new JPanel();
    JPanel lastNamePanel = new JPanel();
    JPanel departmentPanel = new JPanel();
    JPanel addDelPanel = new JPanel();


    JComboBox dboComboBox;

    JTextField nameField;
    JTextField lastNameField;
    JTextField departmentField;

    JButton addEmployee;
    JButton deleteEmployee;
    JButton editEmployee;

    EmployeesWindow(){
        setSize(700,500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setLocationRelativeTo(null);


        String[] columnNames = {"First Name",
               "Last Name",
               "Department",
               };
        tableModel.setColumnIdentifiers(columnNames);


        database.connect();
        database.getData("select * FROM Employees");


        for(int i = 0; i<database.data.length;i++){
            tableModel.addRow(data[i]);
        }



        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(new Dimension(350,500));
        JScrollPane scrollPane = new JScrollPane(table);
        TableListener tableListener = new TableListener();
        table.getSelectionModel().addListSelectionListener(tableListener);




        String[] dbo ={};
        dboComboBox= new JComboBox(dbo);
        dboComboBox.setPreferredSize(new Dimension(120, 20));


        ComboBoxActionListener dboComboBoxListener = new ComboBoxActionListener();
        dboComboBox.addActionListener(dboComboBoxListener);

        JLabel dboSelectLabel = new JLabel("Select Database");
        JButton dboSelectButton = new JButton("Load");

        JLabel nameLabel = new JLabel("Name");
        nameField = new JTextField("");
        nameField.setPreferredSize(new Dimension(120, 20));
        namePanel.add(nameLabel,nameField);
        namePanel.add(nameField);

        JLabel lastNameLabel = new JLabel("Last Name");
        lastNameField = new JTextField("");
        lastNameField.setPreferredSize(new Dimension(120, 20));
        lastNamePanel.add(lastNameLabel,lastNameField);
        lastNamePanel.add(lastNameField);

        JLabel departmentLabel = new JLabel("Department");
        departmentField = new JTextField("");
        departmentField.setPreferredSize(new Dimension(120, 20));
        departmentPanel.add(departmentLabel);
        departmentPanel.add(departmentField);

        editEmployee = new JButton("Edit");
        EditEmployeeListener editEmployeeListener = new EditEmployeeListener();
        editEmployee.addActionListener(editEmployeeListener);


        addEmployee = new JButton("Add Employee");
        AddEmployeeListener addEmployeeListener = new AddEmployeeListener();
        addEmployee.addActionListener(addEmployeeListener);


        deleteEmployee = new JButton("Delete Employee");
        DelEmployeeListener delEmployeeListener = new DelEmployeeListener();
        deleteEmployee.addActionListener(delEmployeeListener);

        mainBox.add(controlBox);
        mainBox.add(scrollPane);

        loadDboPanel.add(dboSelectLabel);
        loadDboPanel.add(dboComboBox);
        loadDboPanel.add(dboSelectButton);

        addDelPanel.add(editEmployee);
        addDelPanel.add(addEmployee);
        addDelPanel.add(deleteEmployee);

        controlBox.add(loadDboPanel);
        controlBox.add(namePanel);
        controlBox.add(lastNamePanel);
        controlBox.add(departmentPanel);
        controlBox.add(addDelPanel);

        this.add(mainBox);

        setVisible(true);

    }


    private class ComboBoxActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    private class TableListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (table.getSelectedRow() > -1) {
                // print first column value from selected row
           //     System.out.println(table.getValueAt(table.getSelectedRow(), 0).toString());
                nameField.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
                lastNameField.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
                departmentField.setText(table.getValueAt(table.getSelectedRow(), 2).toString());
            }

        }
    }

    private class AddEmployeeListener implements  ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {


            //database.getData("select * FROM Employees where Department = 14;");
            tableModel.addRow(new Object[]{nameField.getText(),lastNameField.getText(),departmentField.getText()});
          //  tableModel.fireTableDataChanged();
            //table.repaint();
            System.out.println("Employee added");
        }
    }

    private class DelEmployeeListener implements  ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            tableModel.removeRow(table.getSelectedRow());
            System.out.println("Employee deleted");
        }
    }

    private class EditEmployeeListener implements  ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            tableModel.insertRow(table.getSelectedRow()+1,new Object[]{nameField.getText(),lastNameField.getText(),departmentField.getText()});
            tableModel.removeRow(table.getSelectedRow());
            System.out.println("Employee edited");
        }
    }


}


