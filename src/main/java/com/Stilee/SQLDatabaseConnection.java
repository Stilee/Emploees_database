package com.Stilee;

import java.sql.*;


public  class SQLDatabaseConnection {

    Connection conn;
    String userName = "sa";
    String password = "password";
    String database = "Employees_test";
    String url =   "jdbc:sqlserver://localhost:1433;databaseName="+database+";user="+userName+";password="+password;
    int n =0;



    Object[][] data = new Object[12][3];

    public void connect() {
        try {
            conn = DriverManager.getConnection(url, userName, password);
            System.out.println("Successfully connected to database:" + database);



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getData(String query){
        //query ="select * from Departments";
        try {
            //Make statement
            Statement stmt = conn.createStatement();
            ResultSet myRes = stmt.executeQuery(query);

            //Process resultSet Objects
            while (myRes.next()) {
               // System.out.println(myRes.getString("Name") + ", " + myRes.getString("Lastname"));
                //System.out.println(myRes.getString("E.Name") + myRes.getString("E.LastName") + myRes.getString("Name"));
                dataToObj(myRes.getString("Name"),myRes.getString("LastName"),myRes.getString("Department"));

            }
        }
        catch (Exception e) {
        }
    }






    public void dataToObj(String name, String lastName, String department){
    //Name, LastName, Department, Salary, Hired, Dismissed

        data[n][0]=name;
        data[n][1]=lastName;
        data[n][2]=department;
        n++;

    }

    public Object[][] getData() {
        return data;
    }



    public void getDatabaseMetaData()
    {
        try {

            DatabaseMetaData dbmd = conn.getMetaData();
            String[] types = {"TABLE"};
            ResultSet rs = dbmd.getTables(null, null, "%", types);
            while (rs.next()) {
                System.out.println(rs.getString("TABLE_NAME"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("\n\n\n");
    }


}
