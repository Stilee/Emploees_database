package com.Stilee;

import java.sql.*;

public class Main {


    public static void main(String[] args) throws ClassNotFoundException {

        EmployeesWindow window = new EmployeesWindow();
        SQLDatabaseConnection database = new SQLDatabaseConnection();
        database.connect();
        database.getData("select * FROM Employees");


    }
}
