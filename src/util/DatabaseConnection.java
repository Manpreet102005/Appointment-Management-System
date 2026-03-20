package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DatabaseConnection {
    public static Connection getConnection() {
        Scanner sc=new Scanner(System.in);
        String url = "jdbc:mysql://Manpreet:3306/appointment_db";
        String user = sc.nextLine();
        String password = sc.nextLine();
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
