package manager;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ProductManager {

    // Displays the Product Management Menu and handles user input for managing products
    public static void productMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean goBack = false;

        while (!goBack) {
            System.out.println("Product Management Menu:");
            System.out.println("1. Add Product");
            System.out.println("2. View Product Details");
            System.out.println("3. Update Product Information");
            System.out.println("4. Delete Product");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addProduct(scanner);  // Call method to add a new product
                    break;
                case 2:
                    viewProductDetails(scanner);  // Call method to view product details
                    break;
                case 3:
                    updateProduct(scanner);  // Call method to update product information
                    break;
                case 4:
                    deleteProduct(scanner);  // Call method to delete a product
                    break;
                case 5:
                    goBack = true;  // Exit the menu loop and return to the main menu
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");  // Handle invalid menu options
            }
        }
    }

    // Adds a new product to the database
    public static void addProduct(Scanner scanner) {
        System.out.print("Enter product name: ");
        String name = scanner.next();
        System.out.print("Enter product description: ");
        String description = scanner.next();
        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter quantity available: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter product category: ");
        String category = scanner.next();

        try (Connection conn = DBConnection.getConnection()) {
            // SQL query to insert a new product into the product table
            String sql = "INSERT INTO product (name, description, price, quantity_available, category) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setDouble(3, price);
            stmt.setInt(4, quantity);
            stmt.setString(5, category);
            stmt.executeUpdate();  // Execute the insert operation
            System.out.println("Product added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());  // Handle SQL exceptions
        }
    }

    // Retrieves and displays details of a specific product based on product ID
    public static void viewProductDetails(Scanner scanner) {
        System.out.print("Enter product ID to view details: ");
        int productId = scanner.nextInt();

        try (Connection conn = DBConnection.getConnection()) {
            // SQL query to select product details from the product table
            String sql = "SELECT * FROM product WHERE product_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();  // Execute the query and obtain results
            if (rs.next()) {
                // Display product details
                System.out.println("Product ID: " + rs.getInt("product_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Description: " + rs.getString("description"));
                System.out.println("Price: " + rs.getDouble("price"));
                System.out.println("Quantity Available: " + rs.getInt("quantity_available"));
                System.out.println("Category: " + rs.getString("category"));
            } else {
                System.out.println("Product not found.");  // Handle case where product is not found
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving product details: " + e.getMessage());  // Handle SQL exceptions
        }
    }

    // Updates the information of an existing product
    public static void updateProduct(Scanner scanner) {
        System.out.print("Enter product ID to update: ");
        int productId = scanner.nextInt();
        System.out.print("Enter new product name: ");
        String name = scanner.next();
        System.out.print("Enter new product description: ");
        String description = scanner.next();
        System.out.print("Enter new product price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter new quantity available: ");
        int quantity_available = scanner.nextInt();
        System.out.print("Enter new product category: ");
        String category = scanner.next();

        try (Connection conn = DBConnection.getConnection()) {
            // SQL query to update product details in the product table
            String sql = "UPDATE product SET name = ?, description = ?, price = ?, quantity_available = ?, category = ? WHERE product_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.setDouble(3, price);
            stmt.setInt(4, quantity_available);
            stmt.setString(5, category);
            stmt.setInt(6, productId);
            int rowsAffected = stmt.executeUpdate();  // Execute the update operation
            if (rowsAffected > 0) {
                System.out.println("Product updated successfully.");
            } else {
                System.out.println("Product not found.");  // Handle case where product is not found
            }
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());  // Handle SQL exceptions
        }
    }

    // Deletes a product from the database
    public static void deleteProduct(Scanner scanner) {
        System.out.print("Enter product ID to delete: ");
        int productId = scanner.nextInt();

        try (Connection conn = DBConnection.getConnection()) {
            // SQL query to delete a product from the product table
            String sql = "DELETE FROM product WHERE product_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            int rowsAffected = stmt.executeUpdate();  // Execute the delete operation
            if (rowsAffected > 0) {
                System.out.println("Product deleted successfully.");
            } else {
                System.out.println("Product not found.");  // Handle case where product is not found
            }
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());  // Handle SQL exceptions
        }
    }
}
