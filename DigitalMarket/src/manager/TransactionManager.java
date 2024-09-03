package manager;

import java.sql.*;
import java.util.Scanner;
import db.DBConnection;

public class TransactionManager {

    // Displays the Transaction Management Menu and handles user input for managing transactions
    public static void transactionMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Process Transaction");
            System.out.println("2. View Transactions");
            System.out.println("3. Update Transaction Status");
            System.out.println("4. Refund Transaction");
            System.out.println("5. Back to Main Menu");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    processTransaction();  // Call method to process a new transaction
                    break;
                case 2:
                    viewTransactions();  // Call method to view all transactions
                    break;
                case 3:
                    updateTransactionStatus();  // Call method to update transaction status
                    break;
                case 4:
                    refundTransaction();  // Call method to refund a transaction
                    break;
                case 5:
                    return;  // Exit the menu loop and return to the main menu
                default:
                    System.out.println("Invalid choice. Please try again.");  // Handle invalid menu options
            }
        }
    }

    // Processes a new transaction: updates product quantity and inserts transaction details
    private static void processTransaction() {
        try (Connection connection = DBConnection.getConnection()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter product ID: ");
            int productId = scanner.nextInt();
            System.out.print("Enter seller ID: ");
            int sellerId = scanner.nextInt();
            System.out.print("Enter buyer ID: ");
            int buyerId = scanner.nextInt();
            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter transaction status: ");
            String status = scanner.nextLine();

            // Update product quantity in the database
            String updateProductQuery = "UPDATE Product SET quantity_available = quantity_available - ? WHERE product_id = ? AND quantity_available >= ?";
            try (PreparedStatement updateProductStmt = connection.prepareStatement(updateProductQuery)) {
                updateProductStmt.setInt(1, quantity);
                updateProductStmt.setInt(2, productId);
                updateProductStmt.setInt(3, quantity);
                int rowsAffected = updateProductStmt.executeUpdate();

                if (rowsAffected == 0) {
                    System.out.println("Insufficient quantity available or invalid product ID.");
                    return;
                }
            }

            // Insert transaction details into the database
            String query = "INSERT INTO Transaction (product_id, seller_id, buyer_id, quantity, transaction_date, status) VALUES (?, ?, ?, ?, NOW(), ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, productId);
                stmt.setInt(2, sellerId);
                stmt.setInt(3, buyerId);
                stmt.setInt(4, quantity);
                stmt.setString(5, status);
                stmt.executeUpdate();
                System.out.println("Transaction processed successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error processing transaction: " + e.getMessage());  // Handle SQL exceptions
        }
    }

    // Retrieves and displays all transactions from the database
    private static void viewTransactions() {
        try (Connection connection = DBConnection.getConnection()) {
            String query = "SELECT * FROM Transaction";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    System.out.println("Transaction ID: " + rs.getInt("transaction_id"));
                    System.out.println("Product ID: " + rs.getInt("product_id"));
                    System.out.println("Seller ID: " + rs.getInt("seller_id"));
                    System.out.println("Buyer ID: " + rs.getInt("buyer_id"));
                    System.out.println("Quantity: " + rs.getInt("quantity"));
                    System.out.println("Transaction Date: " + rs.getTimestamp("transaction_date"));
                    System.out.println("Status: " + rs.getString("status"));
                    System.out.println();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error viewing transactions: " + e.getMessage());  // Handle SQL exceptions
        }
    }

    // Updates the status of a specific transaction
    private static void updateTransactionStatus() {
        try (Connection connection = DBConnection.getConnection()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter transaction ID to update: ");
            int transactionId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new transaction status: ");
            String status = scanner.nextLine();

            // Update transaction status in the database
            String query = "UPDATE Transaction SET status = ? WHERE transaction_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, status);
                stmt.setInt(2, transactionId);
                stmt.executeUpdate();
                System.out.println("Transaction status updated successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating transaction status: " + e.getMessage());  // Handle SQL exceptions
        }
    }

    // Refunds a transaction by updating product quantity and deleting the transaction record
    private static void refundTransaction() {
        try (Connection connection = DBConnection.getConnection()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter transaction ID to refund: ");
            int transactionId = scanner.nextInt();

            // Retrieve transaction details
            String query = "SELECT * FROM Transaction WHERE transaction_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, transactionId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int productId = rs.getInt("product_id");
                        int quantity = rs.getInt("quantity");

                        // Update product quantity to reflect the refund
                        String updateProductQuery = "UPDATE Product SET quantity_available = quantity_available + ? WHERE product_id = ?";
                        try (PreparedStatement updateProductStmt = connection.prepareStatement(updateProductQuery)) {
                            updateProductStmt.setInt(1, quantity);
                            updateProductStmt.setInt(2, productId);
                            updateProductStmt.executeUpdate();
                        }

                        // Delete the transaction record
                        String deleteTransactionQuery = "DELETE FROM Transaction WHERE transaction_id = ?";
                        try (PreparedStatement deleteTransactionStmt = connection.prepareStatement(deleteTransactionQuery)) {
                            deleteTransactionStmt.setInt(1, transactionId);
                            deleteTransactionStmt.executeUpdate();
                            System.out.println("Transaction refunded successfully.");
                        }
                    } else {
                        System.out.println("Invalid transaction ID.");  // Handle case where transaction is not found
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error refunding transaction: " + e.getMessage());  // Handle SQL exceptions
        }
    }
}
