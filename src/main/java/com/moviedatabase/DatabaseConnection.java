package com.moviedatabase;
//imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

// class for database connection 
public final class DatabaseConnection {
    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;

    // constructor
    public DatabaseConnection(String host, String port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }
    //reads table from database
    public DefaultTableModel readTable(String tableName) throws SQLException {
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + database; // makes url for the connection
        String sql = "SELECT * FROM " + tableName; // makes an sql query to get everything from the database


        // creates a connection with the query and returns the data 
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            ResultSetMetaData metadata = resultSet.getMetaData();
            int columnCount = metadata.getColumnCount();//gets number of collums for the table
            String[] columnNames = new String[columnCount]; 
            for (int column = 1; column <= columnCount; column++) { //gets he names of the collums
                columnNames[column - 1] = metadata.getColumnLabel(column);
            }
            
            // creates list to store data from the database
            List<Object[]> rows = new ArrayList<>();
            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int column = 1; column <= columnCount; column++) {
                    row[column - 1] = resultSet.getObject(column);
                }
                rows.add(row);
            }
            // for displaying a table in the gui
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            for (Object[] row : rows) {
                model.addRow(row);
            }
            return model;
        }
    }
   
}