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


    JComboBox departmentComboBox;

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
        String query="select E.Name, E.LastName, D.Name as department from  Employees as E, Departments as D where E.Department=D.Code;";
        database.getData(query);

        //database.getData("select * FROM Employees");

        for(int i = 0; i<database.data.length;i++){
            tableModel.addRow(data[i]);
        }



        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(new Dimension(350,500));
        JScrollPane scrollPane = new JScrollPane(table);
        TableListener tableListener = new TableListener();
        table.getSelectionModel().addListSelectionListener(tableListener);


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
        departmentComboBox = new JComboBox(database.keyWithNames[1]);
        departmentComboBox.setPreferredSize(new Dimension(120, 20));
        departmentPanel.add(departmentLabel);
        departmentPanel.add(departmentComboBox);

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
                nameField.setText(table.getValueAt(table.getSelectedRow(), 0).toString());
                lastNameField.setText(table.getValueAt(table.getSelectedRow(), 1).toString());

                for(int i=0; i<database.keyWithNames[1].length; i++){
                    if(table.getValueAt(table.getSelectedRow(),2).equals(database.keyWithNames[1][i])){
                        departmentComboBox.setSelectedIndex(i);
                    }
                }
            }

        }
    }

    private class AddEmployeeListener implements  ActionListener{
        //TODO:Make sure Fields !empty
        @Override
        public void actionPerformed(ActionEvent e) {
            String department ="";
            tableModel.addRow(new Object[]{nameField.getText(),lastNameField.getText(),departmentComboBox.getSelectedItem()});

            for(int i=0; i<database.keyWithNames[1].length; i++){
                if(departmentComboBox.getSelectedItem().equals(database.keyWithNames[1][i])){
                    department=database.keyWithNames[0][i];
                }
            }
            //TODO unique ID
            String query ="Insert into employees values"
                    +"("+(long)(Math.random()*1000000000)+","
                    +"\'"+nameField.getText()+"\',"
                    +"\'"+lastNameField.getText()+"\',"
                    +department+")";

            System.out.println(query);
            //database.getData(query);
            //COMMENTED TO AVOID CHANGING DATABASE

            System.out.println("Employee added");
        }
    }

    private class DelEmployeeListener implements  ActionListener{
        //TODO:Make sure row is selected!
        //TODO:Delete from SQL database
        @Override
        public void actionPerformed(ActionEvent e) {
            tableModel.removeRow(table.getSelectedRow());
            System.out.println("Employee deleted");
        }
    }

    private class EditEmployeeListener implements  ActionListener{
        //TODO:Make sure row is selected!
        //TODO: Edit in SQL database
        @Override
        public void actionPerformed(ActionEvent e) {
            tableModel.insertRow(table.getSelectedRow()+1,new Object[]{nameField.getText(),lastNameField.getText(),departmentComboBox.getSelectedItem()});
            tableModel.removeRow(table.getSelectedRow());
            System.out.println("Employee edited");
        }
    }


}


