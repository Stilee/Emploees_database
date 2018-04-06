package com.Stilee;

import java.sql.*;


public  class SQLDatabaseConnection {

    Connection conn;
    String userName = "sa";
    String password = "password";
    String database = "Employees_test";
    String url =   "jdbc:sqlserver://localhost:1433;databaseName="+database+";user="+userName+";password="+password;


    public void connect() {
        try {
            conn = DriverManager.getConnection(url, userName, password);
            System.out.println("Connected to database:" + database);



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void getData(String query){
        //query ="select * from Employees";
        try {
            //Make statement
            Statement stmt = conn.createStatement();
            ResultSet myRes = stmt.executeQuery(query);

            //Process resultSet Objects
            while (myRes.next()) {
                System.out.println(myRes.getString("Name") + ", " + myRes.getString("Lastname"));
            }
        }
        catch (Exception e) {
        }

    }


}
