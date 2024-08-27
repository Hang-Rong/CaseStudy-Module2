package view;

import Manager.GameMobileManager;
import model.GameMobile;
import model.GamePCs;
import saveData.DataFileManager;
import saveData.ReadAndWriteMobile;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MobileGameMenu {

    private Scanner scanner;
    private GameMobileManager gameMobileManager;
    private DataFileManager dataFileManager;
    private ReadAndWriteMobile readAndWriteMobile;

    public MobileGameMenu(Scanner scanner, GameMobileManager gameMobileManager, DataFileManager dataFileManager) {
        this.scanner = scanner;
        this.gameMobileManager = gameMobileManager;
        this.dataFileManager = dataFileManager;
        this.readAndWriteMobile = new ReadAndWriteMobile();
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n--- Manage Game Mobile ---");
            System.out.println("1. Add a Game");
            System.out.println("2. Remove a Game");
            System.out.println("3. Update a Game");
            System.out.println("4. View All listed Game");
            System.out.println("5. View listed Game by Name");
            System.out.println("6. Find game by Id");
            System.out.println("7. Sort by Name");
            System.out.println("8. Back to Home Page \n");
            System.out.print("Choose an option: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    addGameMobile();
                    break;
                case 2:
                    removeGameMobile();
                    break;
                case 3:
                    updateGameMobile();
                    break;
                case 4:
                    displayItemsInChunks();
                    break;
                case 5:
                    viewGameMobileByName();
                    break;
                case 6:
                    viewGameMobileById();
                    break;
                case 7:
                    sortByName();
                    System.out.println("Game listed sorted by name.");
                    break;
                case 8:
                    return; // Back to Home Page
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    private void viewGameMobileById() {
        int gameID = getValidGameID("Enter Game ID to view: ");

        GameMobile gameMobile = gameMobileManager.getById(gameID);
        if (gameMobile != null) {
            System.out.println("Game Console Details:");
            System.out.println("ID: " + gameMobile.getGameID());
            System.out.println("Name: " + gameMobile.getGameName());
            System.out.println("Live Service: " + gameMobile.getLiveServiceOrNot());
            System.out.println("Date: " + gameMobile.getGameDate());
            System.out.println("Publisher: " + gameMobile.getGamePublisher());
            System.out.println("Info: " + gameMobile.getGameInfo());
            System.out.println("Platform: " + gameMobile.getGameStore());
        } else {
            System.out.println("No game found with ID " + gameID);
        }
    }

    private void displayItemsInChunks() {
        int chunkSize = 6;
        int pageNumber = 1;
        List<GameMobile> allGameMobile = gameMobileManager.getAll();
        int totalItems = allGameMobile.size();

        while (true) {
            int start = (pageNumber - 1) * chunkSize;
            int end = Math.min(start + chunkSize, totalItems);

            if (start >= totalItems) {
                System.out.println("No more items to display.");
                break;
            }

            System.out.println("Displaying items " + (start + 1) + " to " + end + " of " + totalItems + ":");
            for (int i = start; i < end; i++) {
                System.out.println(allGameMobile.get(i));
            }

            System.out.print("Enter 'n' for next page, 'p' for previous page, or 'q' to quit: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("n")) {
                if (end < totalItems) {
                    pageNumber++;
                } else {
                    System.out.println("This is the last page.");
                }
            } else if (input.equals("p")) {
                if (pageNumber > 1) {
                    pageNumber--;
                } else {
                    System.out.println("This is the first page.");
                }
            } else if (input.equals("q")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'n', 'p', or 'q'.");
            }
        }
    }

    private void sortByName() {
        gameMobileManager.sortByName();
        viewAllGameMobile();
    }

    private void addGameMobile() {
        int gameID = getValidGameID("Enter Game ID: ");
        if (gameID == -1) return;

        System.out.print("Enter Game Name: ");
        String gameName = scanner.nextLine();

        System.out.print("Enter Live Service (Yes/No): ");
        String liveServiceOrNot = scanner.nextLine();

        System.out.print("Enter Game Date: ");
        String gameDate = scanner.nextLine();

        System.out.print("Enter Game Publisher: ");
        String gamePublisher = scanner.nextLine();

        System.out.print("Enter Game Info: ");
        String gameInfo = scanner.nextLine();

        System.out.print("Enter Store: ");
        String gamePlatform = scanner.nextLine();

        GameMobile newGameMobile = new GameMobile(gameID, gameName, liveServiceOrNot, gameDate, gamePublisher, gameInfo, gamePlatform);
        gameMobileManager.add(newGameMobile);

        System.out.println("Game Mobile added successfully.");
        displayWriteMenu();
    }

    private void displayWriteMenu() {
        System.out.println("\n--- Is this game cross-platform?: ---");
        System.out.println("1. Mobile Only");
        System.out.println("2. and PCs");
        System.out.println("3. and Console");
        System.out.println("4. and PCs and Console \n");

        int choice = getValidIntegerInput();
        if (choice == -1) return; // Invalid input, try again

        // Determine which files to write based on user choice
        List<String> filesToWrite = determineFilesToWrite(choice);
        if (filesToWrite.isEmpty()) {
            System.out.println("Invalid choice. No files will be written.");
            return;
        }

        // Write data to the selected files
        gameMobileManager.writeDataToSelectedFiles(filesToWrite);

        System.out.println("Data written to the selected file(s).");
    }

    private int getValidIntegerInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return -1;
        }
    }

    private List<String> determineFilesToWrite(int choice) {
        List<String> filesToWrite = new ArrayList<>();
        switch (choice) {
            case 1:
                filesToWrite.add(dataFileManager.getMobileFile());
                break;
            case 2:
                filesToWrite.add(dataFileManager.getPcFile());
                filesToWrite.add(dataFileManager.getMobileFile());
                break;
            case 3:
                filesToWrite.add(dataFileManager.getConsoleFile());
                filesToWrite.add(dataFileManager.getMobileFile());
                break;
            case 4:
                filesToWrite.add(dataFileManager.getConsoleFile());
                filesToWrite.add(dataFileManager.getPcFile());
                filesToWrite.add(dataFileManager.getMobileFile());
                break;
            default:
                break;
        }
        return filesToWrite;
    }


    private void removeGameMobile() {
        int gameID = getValidGameID("Enter Game ID: ");

        GameMobile gameMobile = gameMobileManager.getById(gameID);
        if (gameMobile != null) {
            gameMobileManager.removeById(gameID, gameMobile);
            System.out.println("Game removed successfully.");
        } else {
            System.out.println("Game not found.");
        }
    }

    private void updateGameMobile() {
        int gameID = getValidGameID("Enter Game ID to update: ");
        if (gameID == -1) return;

        GameMobile existingGameMobile = gameMobileManager.getById(gameID);

        if (existingGameMobile != null) {
            updateGameMobileDetails(existingGameMobile);
            gameMobileManager.update(gameID, existingGameMobile);
            System.out.println("Game console updated successfully.");
        } else {
            System.out.println("Game console not found.");
        }
    }

    private void updateGameMobileDetails(GameMobile gameMobile) {
        System.out.print("Enter new Game Name (or ~ to keep current): ");
        updateField(scanner.nextLine(), gameMobile::setGameName, gameMobile.getGameName());

        System.out.print("Enter new Live Service (Yes/No) (or ~ to keep current): ");
        updateField(scanner.nextLine(), gameMobile::setLiveServiceOrNot, gameMobile.getLiveServiceOrNot());

        System.out.print("Enter new Game Date (or ~ to keep current): ");
        updateField(scanner.nextLine(), gameMobile::setGameDate, gameMobile.getGameDate());

        System.out.print("Enter new Game Publisher (or ~ to keep current): ");
        updateField(scanner.nextLine(), gameMobile::setGamePublisher, gameMobile.getGamePublisher());

        System.out.print("Enter new Game Info (or ~ to keep current): ");
        updateField(scanner.nextLine(), gameMobile::setGameInfo, gameMobile.getGameInfo());

        System.out.print("Enter new Game Store (or ~ to keep current): ");
        updateField(scanner.nextLine(), gameMobile::setGameStore, gameMobile.getGameStore());
    }

    private void updateField(String newValue, java.util.function.Consumer<String> updateMethod, String currentValue) {
        if (!newValue.equals("~")) {
            updateMethod.accept(newValue);
        }
    }

    private void viewAllGameMobile() {
        List<GameMobile> allGameMobile = gameMobileManager.getAll();
        if (allGameMobile.isEmpty()) {
            System.out.println("No game found.");
        } else {
            for (GameMobile gameMobile : allGameMobile) {
                System.out.println(gameMobile);
            }
        }
    }

    private void viewGameMobileByName() {
        System.out.print("Enter Game Name to search: ");
        String gameName = scanner.nextLine();
        GameMobile gameMobile = gameMobileManager.getByName(gameName);
        if (gameMobile != null) {
            System.out.println(gameMobile);
        } else {
            System.out.println("Game not found.");
        }
    }

    // Helper method to get a valid Game ID from user input
    private int getValidGameID(String prompt) {
        int gameID = -1;
        while (true) {
            System.out.print(prompt);
            try {
                gameID = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Game ID must be a number.");
            }
        }
        return gameID;
    }
}
