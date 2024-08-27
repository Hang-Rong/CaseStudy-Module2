package view;

import Manager.GamePCsManager;
import model.GameConsoles;
import model.GamePCs;
import saveData.DataFileManager;
import saveData.ReadAndWritePC;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PCsGameMenu {

    private Scanner scanner;
    private GamePCsManager gamePCsManager;
    private DataFileManager dataFileManager;
    private ReadAndWritePC readAndWritePC;

    public PCsGameMenu(Scanner scanner, GamePCsManager gamePCsManager, DataFileManager dataFileManager) {
        this.scanner = scanner;
        this.gamePCsManager = gamePCsManager;
        this.dataFileManager = dataFileManager;
        this.readAndWritePC = new ReadAndWritePC();
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n--- Manage Game PCs ---");
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
                    addGamePC();
                    break;
                case 2:
                    removeGamePC();
                    break;
                case 3:
                    updateGamePC();
                    break;
                case 4:
                    displayItemsInChunks();
                    break;
                case 5:
                    viewGamePCByName();
                    break;
                case 6:
                    viewGamePCsById();
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

    private void viewGamePCsById() {
        int gameID = getValidGameID("Enter Game ID to view: ");

        GamePCs gamePC = gamePCsManager.getById(gameID);
        if (gamePC != null) {
            System.out.println("Game PCs Details:");
            System.out.println("ID: " + gamePC.getGameID());
            System.out.println("Name: " + gamePC.getGameName());
            System.out.println("Live Service: " + gamePC.getLiveServiceOrNot());
            System.out.println("Date: " + gamePC.getGameDate());
            System.out.println("Publisher: " + gamePC.getGamePublisher());
            System.out.println("Info: " + gamePC.getGameInfo());
            System.out.println("Store: " + gamePC.getGameStore());
        } else {
            System.out.println("No game found with ID " + gameID);
        }
    }

    private void displayItemsInChunks() {
        int chunkSize = 6;
        int pageNumber = 1;
        List<GamePCs> allGameConsoles = gamePCsManager.getAll();
        int totalItems = allGameConsoles.size();

        while (true) {
            int start = (pageNumber - 1) * chunkSize;
            int end = Math.min(start + chunkSize, totalItems);

            if (start >= totalItems) {
                System.out.println("No more items to display.");
                break;
            }

            System.out.println("Displaying items " + (start + 1) + " to " + end + " of " + totalItems + ":");
            for (int i = start; i < end; i++) {
                System.out.println(allGameConsoles.get(i));
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
        gamePCsManager.sortByName();
        System.out.println("Game listed sorted by name.");
        viewAllGamePCs();
    }

    private void addGamePC() {
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

        GamePCs newGamePCs = new GamePCs(gameID, gameName, liveServiceOrNot, gameDate, gamePublisher, gameInfo, gamePlatform);
        gamePCsManager.add(newGamePCs);

        System.out.println("Game PC added successfully.");
        displayWriteMenu();
    }

    private void displayWriteMenu() {
        System.out.println("\n--- Is this game cross-platform?: ---");
        System.out.println("1. PC Only");
        System.out.println("2. and Console");
        System.out.println("3. and Mobile");
        System.out.println("4. and Console and Mobile \n");

        int choice = getValidIntegerInput();
        if (choice == -1) return; // Invalid input, try again

        // Determine which files to write based on user choice
        List<String> filesToWrite = determineFilesToWrite(choice);
        if (filesToWrite.isEmpty()) {
            System.out.println("Invalid choice. No files will be written.");
            return;
        }

        // Write data to the selected files
        gamePCsManager.writeDataToSelectedFiles(filesToWrite);

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
                filesToWrite.add(dataFileManager.getPcFile());
                break;
            case 2:
                filesToWrite.add(dataFileManager.getConsoleFile());
                filesToWrite.add(dataFileManager.getPcFile());
                break;
            case 3:
                filesToWrite.add(dataFileManager.getMobileFile());
                filesToWrite.add(dataFileManager.getPcFile());
                break;
            case 4:
                filesToWrite.add(dataFileManager.getConsoleFile());
                filesToWrite.add(dataFileManager.getMobileFile());
                filesToWrite.add(dataFileManager.getPcFile());
                break;
            default:
                break;
        }
        return filesToWrite;
    }


    private void removeGamePC() {
        int gameID = getValidGameID("Enter Game ID: ");

        GamePCs gamePCs = gamePCsManager.getById(gameID);
        if (gamePCs != null) {
            gamePCsManager.removeById(gameID, gamePCs);
            System.out.println("Game removed successfully.");
        } else {
            System.out.println("Game not found.");
        }
    }

    private void updateGamePC() {
        int gameID = getValidGameID("Enter Game ID to update: ");
        if (gameID == -1) return;

        GamePCs existingGamePC = gamePCsManager.getById(gameID);

        if (existingGamePC != null) {
            updateGamePCDetails(existingGamePC);
            gamePCsManager.update(gameID, existingGamePC);
            System.out.println("Game console updated successfully.");
        } else {
            System.out.println("Game console not found.");
        }
    }

    private void updateGamePCDetails(GamePCs gamePCs) {
        System.out.print("Enter new Game Name (or ~ to keep current): ");
        updateField(scanner.nextLine(), gamePCs::setGameName, gamePCs.getGameName());

        System.out.print("Enter new Live Service (Yes/No) (or ~ to keep current): ");
        updateField(scanner.nextLine(), gamePCs::setLiveServiceOrNot, gamePCs.getLiveServiceOrNot());

        System.out.print("Enter new Game Date (or ~ to keep current): ");
        updateField(scanner.nextLine(), gamePCs::setGameDate, gamePCs.getGameDate());

        System.out.print("Enter new Game Publisher (or ~ to keep current): ");
        updateField(scanner.nextLine(), gamePCs::setGamePublisher, gamePCs.getGamePublisher());

        System.out.print("Enter new Game Info (or ~ to keep current): ");
        updateField(scanner.nextLine(), gamePCs::setGameInfo, gamePCs.getGameInfo());

        System.out.print("Enter new Game Store (or ~ to keep current): ");
        updateField(scanner.nextLine(), gamePCs::setGameStore, gamePCs.getGameStore());
    }

    private void updateField(String newValue, java.util.function.Consumer<String> updateMethod, String currentValue) {
        if (!newValue.equals("~")) {
            updateMethod.accept(newValue);
        }
    }

    private void viewAllGamePCs() {
        List<GamePCs> allGamePCs = gamePCsManager.getAll();
        if (allGamePCs.isEmpty()) {
            System.out.println("No game found.");
        } else {
            for (GamePCs gamePCs : allGamePCs) {
                System.out.println(gamePCs);
            }
        }
    }

    private void viewGamePCByName() {
        System.out.print("Enter Game Name to search: ");
        String gameName = scanner.nextLine();
        GamePCs gamePC = gamePCsManager.getByName(gameName);
        if (gamePC != null) {
            System.out.println(gamePC);
        } else {
            System.out.println("Game console not found.");
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
