package org.example;

import org.postgresql.util.PSQLException;

import java.sql.*;

public class LineupAttribute {
    String url;
    String user;
    String password;

    /**
     * initialize variable url, user, and password. create the table 'students' if not exist
     */
    public LineupAttribute(){
        url = "jdbc:postgresql://localhost:5432/Project";
        user = "postgres";
        password = "admin";
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE Students (\n" +
                    "    student_id SERIAL PRIMARY KEY,\n" +
                    "    first_name TEXT NOT NULL,\n" +
                    "    last_name TEXT NOT NULL,\n" +
                    "    email TEXT NOT NULL UNIQUE,\n" +
                    "    enrollment_date DATE NOT NULL\n" +
                    ");";
            statement.executeUpdate(query);
            query = "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES\n" +
                    "('John', 'Doe', 'john.doe@example.com', '2023-09-01'),\n" +
                    "('Jane', 'Smith', 'jane.smith@example.com', '2023-09-01'),\n" +
                    "('Jim', 'Beam', 'jim.beam@example.com', '2023-09-02');";
            statement.executeUpdate(query);

            connection.close();
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


}

