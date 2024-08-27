package view;

import Manager.GameConsolesManager;
import Manager.GameMobileManager;
import Manager.GamePCsManager;
import model.GameMobile;
import saveData.DataFileManager;
import model.GameConsoles;

import java.util.Scanner;

public class HomePageMenu {

    public void displayHomePageMenu(Scanner scanner, String username) {
        DataFileManager dataFileManager = new DataFileManager(username);

        // Initialize GameConsolesManager with DataFileManager
        GameConsolesManager gameConsolesManager = new GameConsolesManager(dataFileManager); // Create DataFileManager instance
        GamePCsManager gamePCsManager = new GamePCsManager(dataFileManager);
        GameMobileManager gameMobileManager = new GameMobileManager(dataFileManager);

        while (true) {
            System.out.println("\n--- Home Page ---");
            System.out.println("1. Manage Game Consoles");
            System.out.println("2. Manage Game PCs");
            System.out.println("3. Manage Game Mobile/Portable");
            System.out.println("4. Log Out");
            System.out.print("Choose an option: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (choice == 1) {
                ConsolesGameMenu consolesGameMenu = new ConsolesGameMenu(scanner, gameConsolesManager, dataFileManager);
                consolesGameMenu.displayMenu();
            } else if (choice == 2) {
                PCsGameMenu pcsGameMenu = new PCsGameMenu(scanner, gamePCsManager, dataFileManager);
                pcsGameMenu.displayMenu();
            } else if (choice == 3) {
                MobileGameMenu mobileGameMenu = new MobileGameMenu(scanner, gameMobileManager, dataFileManager);
                mobileGameMenu.displayMenu();
        } else if (choice == 4) {
                System.out.println("Logging out...");
                break;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
    }
}


