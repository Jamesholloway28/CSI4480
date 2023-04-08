package com.mycompany.sql_testing;

import java.sql.*;
import com.mycompany.sql_testing.DatabaseConnection;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserDAO {

    public User checkLogin(String email, String password) throws SQLException {

        try {
            // Initialize the database
            Connection con = DatabaseConnection.initializeDatabase();

            CallableStatement cstmt = null;

            // Call the stored procedure to check login
            cstmt = con.prepareCall("{call checkLogin(?, ?, ?)}");
            cstmt.setString(1, email);
            cstmt.setString(2, password);
            cstmt.registerOutParameter(3, Types.BOOLEAN);

            cstmt.execute();

            boolean loginResult = cstmt.getBoolean(3);

            System.out.println("\nSTATEMENT PASSED TO DB: " + cstmt);

            Statement myStmt = null;
            ResultSet myRs = null;

            myStmt = con.createStatement();
            myRs = myStmt.executeQuery("SELECT userEmail, userPassword FROM USER");

            //process result
            while (myRs.next()) {
                System.out.println("");
                System.out.println(myRs.getString("userEmail"));
                System.out.println(myRs.getString("userPassword"));
            }

            User user = new User();

            if (loginResult) {
                System.out.println("TRUE MATCH");

                user.setEmail(email);
                user.setPassword(password);

                System.out.println(user.getEmail() + user.getPassword());

                return user;
            } else {
                System.out.println("User not in database, return null");
                user = null;
            }

            System.out.print("\n---------------------------\nConnection estd\n---------------------------\n");

            System.out.println("USER EQUALS: " + user);

            con.close();

            return user;

        } catch (Exception e) {
            System.out.println("\n--------------------------\nCRITICAL ERROR \n--------------------------\n");
            System.out.println(e);

            System.out.println("Email entered: " + email);
            System.out.println("Password entered: " + password);
            return null;
        }

    }
}
