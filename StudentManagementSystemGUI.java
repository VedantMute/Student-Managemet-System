// Created by Vedant Mute

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentManagementSystemGUI extends JFrame {
    // JDBC URL, username, and password
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/student_management";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "#orange#7711";

    private JTextField nameField, rollNoField, divisionField, phoneNumberField, addressField;
    private DefaultTableModel tableModel;

    public StudentManagementSystemGUI() {
        super("Student Management System");

        // Creating GUI components
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(55, 55, 55, 55)); // Padding around the entire panel

        // Heading Label
        JLabel heading = new JLabel("STUDENT MANAGEMENT SYSTEM", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(heading, BorderLayout.PAGE_START);

        // Panel for input fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));
        inputPanel.setBorder(new EmptyBorder(16, 16, 16, 16)); // Padding around the input fields

        nameField = new JTextField(20);
        rollNoField = new JTextField(10);
        divisionField = new JTextField(20);
        phoneNumberField = new JTextField(15);
        addressField = new JTextField(30);

        inputPanel.add(new JLabel("Student Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Roll Number:"));
        inputPanel.add(rollNoField);
        inputPanel.add(new JLabel("Division:"));
        inputPanel.add(divisionField);
        inputPanel.add(new JLabel("Phone Number:"));
        inputPanel.add(phoneNumberField);
        inputPanel.add(new JLabel("Address:"));
        inputPanel.add(addressField);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);

                    String name = nameField.getText();
                    int rollNo = Integer.parseInt(rollNoField.getText());
                    String division = divisionField.getText();
                    String phoneNumber = phoneNumberField.getText();
                    String address = addressField.getText();

                    String sql = "INSERT INTO students (name, roll_no, division, phone_number, address) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, name);
                    preparedStatement.setInt(2, rollNo);
                    preparedStatement.setString(3, division);
                    preparedStatement.setString(4, phoneNumber);
                    preparedStatement.setString(5, address);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Student information successfully added to the database.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to add student information.");
                    }

                    preparedStatement.close();
                    connection.close();

                    // Refresh the table after inserting data
                    refreshTable();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        inputPanel.add(submitButton);

        panel.add(inputPanel, BorderLayout.LINE_START);

        // Panel for the table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        tableModel.addColumn("Name");
        tableModel.addColumn("Roll No");
        tableModel.addColumn("Division");
        tableModel.addColumn("Phone Number");
        tableModel.addColumn("Address");

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setBorder(new EmptyBorder(16, 16, 16, 16)); // Padding around the table

        panel.add(tablePanel, BorderLayout.CENTER);

        this.add(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);

        // Fetch data and display in the table
        refreshTable();
    }

    private void refreshTable() {
        // Clear the table before populating it again
        tableModel.setRowCount(0);

        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM students");

            while (resultSet.next()) {
                Object[] row = {
                        resultSet.getString("name"),
                        resultSet.getInt("roll_no"),
                        resultSet.getString("division"),
                        resultSet.getString("phone_number"),
                        resultSet.getString("address")
                };
                tableModel.addRow(row);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentManagementSystemGUI());
    }
}
// Created by Vedant Mute.
// Uploaded on GitHub.
