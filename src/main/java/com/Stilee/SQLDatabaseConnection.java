package com.Stilee;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.sql.*;


public  class SQLDatabaseConnection {

    Connection conn;
    Statement stmt;
    ResultSet myRes;

    String[][] keyWithNames;
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
            stmt = conn.createStatement();
            myRes = stmt.executeQuery(query);
            makeQuery(query);

            //Process resultSet Objects
            while (myRes.next()) {
                dataToObj(myRes.getString("Name"),myRes.getString("LastName"),myRes.getString("Department"));
            }
        }
        catch (Exception e) {
        }

        getKeyName();

    }


    public void makeQuery(String query){
        try {
            myRes = stmt.executeQuery(query);
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

    private void getKeyName(){
        int n=0;
        makeQuery("select count(*) from departments");
        try {
            while (myRes.next()) {
                keyWithNames = new String[2][myRes.getInt(1)];
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        makeQuery("select * from departments");

        try {
            while (myRes.next()) {
                keyWithNames[0][n]=myRes.getString("Code");
                keyWithNames[1][n]=myRes.getString("Name");
                n++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


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
