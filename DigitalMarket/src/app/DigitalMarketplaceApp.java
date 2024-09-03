package app;

import manager.ProductManager;
import manager.SellerManager;
import manager.TransactionManager;

import java.util.Scanner;

public class DigitalMarketplaceApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        
        while (!exit) {
            System.out.println("Welcome to Digital Marketplace");
            System.out.println("1. Manage Products");
            System.out.println("2. Manage Sellers");
            System.out.println("3. Manage Transactions");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    ProductManager.productMenu();
                    break;
                case 2:
                    SellerManager.sellerMenu();
                    break;
                case 3:
                    TransactionManager.transactionMenu();
                    break;
                case 4:
                    exit = true;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
        scanner.close();
    }
}
