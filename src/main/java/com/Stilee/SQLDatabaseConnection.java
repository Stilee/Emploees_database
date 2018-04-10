package com.Stilee;

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

    Object[][] data;

    public void connect() {
        try {
            conn = DriverManager.getConnection(url, userName, password);
            System.out.println("Successfully connected to database:" + database);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadData(String query){
        try {
            stmt = conn.createStatement();
            myRes = stmt.executeQuery(query);

            String numOfRows = "select count(*) from Employees";
            makeQuery(numOfRows);

            try {
                while (myRes.next()) {
                    System.out.println(myRes.getInt(1));
                    data = new Object[myRes.getInt(1)][4];
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            makeQuery(query);
            while (myRes.next()) {
                dataToObj(myRes.getString("Name"),myRes.getString("LastName"),myRes.getString("Department"), myRes.getString("SSN"));
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

    public void dataToObj(String name, String lastName, String department, String id){
        data[n][0]=name;
        data[n][1]=lastName;
        data[n][2]=department;
        data[n][3]=id;
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
}
