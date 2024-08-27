
import Manager.UserManagement;
import view.HomePageMenu;

import java.util.Scanner;

public class Main {
    private static final String USER_FILE_PATH = "userData/users.csv";
    private static UserManagement userManagement = new UserManagement(USER_FILE_PATH);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("--- Login Screen ---");
                System.out.println("1. Login");
                System.out.println("2. Register");
                System.out.println("3. Exit");
                System.out.print("Choose an option: ");

                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        handleLogin(scanner);
                        break;
                    case 2:
                        handleRegistration(scanner);
                        break;
                    case 3:
                        System.out.println("Exiting the application. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void handleLogin(Scanner scanner) {
        try {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (userManagement.authenticateUser(username, password)) {
                System.out.println("Login successful!");
                HomePageMenu homePageMenu = new HomePageMenu();
                homePageMenu.displayHomePageMenu(scanner, username);
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred during login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void handleRegistration(Scanner scanner) {
        try {
            System.out.print("Enter username (at least 3 characters): ");
            String username = scanner.nextLine();

            if (username.length() < 3) {
                System.out.println("Username must be at least 3 characters long.");
                return;
            }

            System.out.print("Enter password (more than 5 characters): ");
            String password = scanner.nextLine();

            if (password.length() <= 5) {
                System.out.println("Password must be more than 5 characters long.");
                return;
            }

            if (userManagement.registerUser(username, password)) {
                System.out.println("Registration successful!");
            } else {
                System.out.println("Username already exists. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred during registration: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
