package manager;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class SellerManager {

    // Displays the Seller Management Menu and handles user input for managing sellers
    public static void sellerMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean goBack = false;

        while (!goBack) {
            System.out.println("Seller Management Menu:");
            System.out.println("1. Add Seller");
            System.out.println("2. View Seller Details");
            System.out.println("3. Update Seller Information");
            System.out.println("4. Delete Seller");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addSeller(scanner);  // Call method to add a new seller
                    break;
                case 2:
                    viewSellerDetails(scanner);  // Call method to view seller details
                    break;
                case 3:
                    updateSeller(scanner);  // Call method to update seller information
                    break;
                case 4:
                    deleteSeller(scanner);  // Call method to delete a seller
                    break;
                case 5:
                    goBack = true;  // Exit the menu loop and return to the main menu
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");  // Handle invalid menu options
            }
        }
    }

    // Adds a new seller to the database
    public static void addSeller(Scanner scanner) {
        System.out.print("Enter seller name: ");
        String name = scanner.next();
        System.out.print("Enter seller email: ");
        String email = scanner.next();
        System.out.print("Enter seller phone number: ");
        String phoneNumber = scanner.next();

        try (Connection conn = DBConnection.getConnection()) {
            // SQL query to insert a new seller into the seller table
            String sql = "INSERT INTO seller (name, email, phone_number) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phoneNumber);
            stmt.executeUpdate();  // Execute the insert operation
            System.out.println("Seller added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding seller: " + e.getMessage());  // Handle SQL exceptions
        }
    }

    // Retrieves and displays details of a specific seller based on seller ID
    public static void viewSellerDetails(Scanner scanner) {
        System.out.print("Enter seller ID to view details: ");
        int sellerId = scanner.nextInt();

        try (Connection conn = DBConnection.getConnection()) {
            // SQL query to select seller details from the seller table
            String sql = "SELECT * FROM seller WHERE seller_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();  // Execute the query and obtain results
            if (rs.next()) {
                // Display seller details
                System.out.println("Seller ID: " + rs.getInt("seller_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Phone Number: " + rs.getString("phone_number"));
            } else {
                System.out.println("Seller not found.");  // Handle case where seller is not found
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving seller details: " + e.getMessage());  // Handle SQL exceptions
        }
    }

    // Updates the information of an existing seller
    public static void updateSeller(Scanner scanner) {
        System.out.print("Enter seller ID to update: ");
        int sellerId = scanner.nextInt();
        System.out.print("Enter new seller name: ");
        String name = scanner.next();
        System.out.print("Enter new seller email: ");
        String email = scanner.next();
        System.out.print("Enter new seller phone number: ");
        String phoneNumber = scanner.next();

        try (Connection conn = DBConnection.getConnection()) {
            // SQL query to update seller details in the seller table
            String sql = "UPDATE seller SET name = ?, email = ?, phone_number = ? WHERE seller_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phoneNumber);
            stmt.setInt(4, sellerId);
            int rowsAffected = stmt.executeUpdate();  // Execute the update operation
            if (rowsAffected > 0) {
                System.out.println("Seller updated successfully.");
            } else {
                System.out.println("Seller not found.");  // Handle case where seller is not found
            }
        } catch (SQLException e) {
            System.out.println("Error updating seller: " + e.getMessage());  // Handle SQL exceptions
        }
    }

    // Deletes a seller from the database
    public static void deleteSeller(Scanner scanner) {
        System.out.print("Enter seller ID to delete: ");
        int sellerId = scanner.nextInt();

        try (Connection conn = DBConnection.getConnection()) {
            // SQL query to delete a seller from the seller table
            String sql = "DELETE FROM seller WHERE seller_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sellerId);
            int rowsAffected = stmt.executeUpdate();  // Execute the delete operation
            if (rowsAffected > 0) {
                System.out.println("Seller deleted successfully.");
            } else {
                System.out.println("Seller not found.");  // Handle case where seller is not found
            }
        } catch (SQLException e) {
            System.out.println("Error deleting seller: " + e.getMessage());  // Handle SQL exceptions
        }
    }
}
