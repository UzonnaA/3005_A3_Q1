package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //Code to connect to PostgreSQL
        String url = "jdbc:postgresql://localhost:5432/3004_A3";
        String user = "postgres";
        String password = "uzonna";

        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            if (connection != null) {
                System.out.println("Connected to the database!");
            }

            Scanner scanner = new Scanner(System.in); // Create a Scanner object to read input

            while (true) {
                System.out.println("Commands: getAllStudents(), addStudent(), deleteStudent(), updateStudent(), exit");
                String input = scanner.nextLine(); // Read user input

                if (input.equalsIgnoreCase("exit")) {
                    break; //Exit the program
                } else if (input.equalsIgnoreCase("getAllStudents()")) {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM students;");
                    while (resultSet.next()) {
                        System.out.print(resultSet.getString("student_id") + " \t");
                        System.out.print(resultSet.getString("first_name") + " \t");
                        System.out.print(resultSet.getString("last_name") + " \t");
                        System.out.print(resultSet.getString("email") + " \t");
                        System.out.println(resultSet.getString("enrollment_date"));
                    }
                    //This line checks to ensure the input is (int, string, string, string, date)
                } else if (input.matches("addStudent\\((\\d+),\\s*([^,]+),\\s*([^,]+),\\s*([^,]+),\\s*(\\d{4}-\\d{2}-\\d{2})\\)")) {
                    // Extract details from the command
                    // We have studentID as an integer
                    String[] parts = input.substring(11, input.length() - 1).split(",\\s*");
                    String studentId = parts[0];
                    String firstName = parts[1];
                    String lastName = parts[2];
                    String email = parts[3];
                    String enrollmentDateStr = parts[4];



                    // Convert the enrollmentDate string to a java.sql.Date object
                    java.sql.Date enrollmentDate = java.sql.Date.valueOf(enrollmentDateStr);

                    // SQL statement to insert a new student
                    String sql = "INSERT INTO students (student_id, first_name, last_name, email, enrollment_date) VALUES (?, ?, ?, ?, ?);";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setInt(1, Integer.parseInt(studentId));
                    preparedStatement.setString(2, firstName);
                    preparedStatement.setString(3, lastName);
                    preparedStatement.setString(4, email);
                    preparedStatement.setDate(5, enrollmentDate);

                    //Rows changed ensures something actually happened
                    int rowsChanged = preparedStatement.executeUpdate();
                    if (rowsChanged > 0) {
                        System.out.println("Student added successfully.");
                    } else {
                        System.out.println("Failed to add the student.");
                    }
                } else if (input.matches("deleteStudent\\((\\d+)\\)")) {
                    //Get the student ID from the command
                    String studentIDStr = input.substring(input.indexOf('(') + 1, input.indexOf(')')).trim();
                    int studentID = Integer.parseInt(studentIDStr);

                    //SQL statement to delete a student based on Student ID
                    String sql = "DELETE FROM students WHERE student_id = ?;";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setInt(1, studentID);

                    int rowsChanged = preparedStatement.executeUpdate();
                    if (rowsChanged > 0) {
                        System.out.println("Student with ID " + studentID + " deleted successfully.");
                    } else {
                        System.out.println("No student found with ID " + studentID + ", or failed to delete.");
                    }
                }
                else if (input.matches("updateStudentEmail\\((\\d+),\\s*([^)]+)\\)")) {
                    //get the student ID and new email from the command
                    String[] parts = input.substring(input.indexOf('(') + 1, input.indexOf(')')).split(",\\s*");
                    int studentID = Integer.parseInt(parts[0]);
                    String newEmail = parts[1].trim();

                    //SQL statement to update the student's email
                    String sql = "UPDATE students SET email = ? WHERE student_id = ?;";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, newEmail);
                    preparedStatement.setInt(2, studentID);

                    int rowsChanged = preparedStatement.executeUpdate();
                    if (rowsChanged > 0) {
                        System.out.println("Email for student ID " + studentID + " updated successfully to " + newEmail + ".");
                    } else {
                        System.out.println("Failed to update email for student ID " + studentID + ".");
                    }
                }

                else {
                    System.out.println("Invalid command");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}