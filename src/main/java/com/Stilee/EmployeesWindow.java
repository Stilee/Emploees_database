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
    Object[][] data;

    DefaultTableModel tableModel = new DefaultTableModel(0,4);
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

    JButton addEmployee;
    JButton deleteEmployee;
    JButton editEmployee;

    EmployeesWindow(){
        setSize(700,500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setLocationRelativeTo(null);

        initiateConnectrion();

        table.setFillsViewportHeight(true);
        table.setPreferredScrollableViewportSize(new Dimension(350,500));
        JScrollPane scrollPane = new JScrollPane(table);
        TableListener tableListener = new TableListener();
        table.getSelectionModel().addListSelectionListener(tableListener);
        table.removeColumn(table.getColumnModel().getColumn(3));

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

    private void initiateConnectrion(){
        database.connect();
        String[] columnNames = {"First Name",
                "Last Name",
                "Department",
                "ID"
        };
        tableModel.setColumnIdentifiers(columnNames);

        String query="select E.Name, E.LastName, D.Name as department, E.SSN as SSN from  Employees as E, Departments as D where E.Department=D.Code;";
        database.loadData(query);
        data = database.getData();

        for(int i = 0; i<database.data.length;i++){
            tableModel.addRow(data[i]);
        }
    }

    private String  getDepartmentID(String department){
        String departmentID="";
        for(int i=0; i<database.keyWithNames[1].length; i++){
            if(department.equals(database.keyWithNames[1][i])){
                departmentID=database.keyWithNames[0][i];
            }
        }
        return departmentID;
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
        @Override
        public void actionPerformed(ActionEvent e) {
        if(nameField.getText().isEmpty() || lastNameField.getText().isEmpty()){
            System.out.println("Error, empty Fields");
            }else{
                String department ="";
                long id =(long)(Math.random()*1000000000);
                tableModel.addRow(new Object[]{nameField.getText(),lastNameField.getText(),departmentComboBox.getSelectedItem(),id});
                department=getDepartmentID(departmentComboBox.getSelectedItem().toString());

                //TODO unique ID
                String query ="Insert into employees values"
                        +"("+id+","
                        +"'"+nameField.getText()+"',"
                        +"'"+lastNameField.getText()+"',"
                        +department+")";

                database.makeQuery(query);
                System.out.println("Employee added");
            }
        }
    }

    private class DelEmployeeListener implements  ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(table.getSelectedRow()==-1){
                System.out.println("Error, select record");
            }else{
                String query="delete from Employees where ssn="+table.getModel().getValueAt(table.getSelectedRow(),3).toString();

                database.makeQuery(query);
                tableModel.removeRow(table.getSelectedRow());
                System.out.println("Employee deleted");
            }
        }
    }

    private class EditEmployeeListener implements  ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            if(table.getSelectedRow()==-1){
                System.out.println("Error, select record to edit");
            }else{
                String query="UPDATE Employees SET "
                        + "Name = '"+ nameField.getText() +"',"
                        + "LastName = '"+ lastNameField.getText() +"',"
                        + "Department = '"+ getDepartmentID(departmentComboBox.getSelectedItem().toString()) +"'"
                        +" WHERE ssn="+table.getModel().getValueAt(table.getSelectedRow(),3).toString();      ////BŁąD

                database.makeQuery(query);

                tableModel.insertRow(table.getSelectedRow()+1,new Object[]{nameField.getText(),lastNameField.getText(),departmentComboBox.getSelectedItem(),table.getModel().getValueAt(table.getSelectedRow(),3).toString()});
                tableModel.removeRow(table.getSelectedRow());
                System.out.println("Employee edited");
            }
        }
    }
}


