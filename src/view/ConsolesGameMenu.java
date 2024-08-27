package view;

import Manager.GameConsolesManager;
import model.GameConsoles;
import saveData.DataFileManager;
import saveData.ReadAndWriteConsoles;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsolesGameMenu {

    private Scanner scanner;
    private GameConsolesManager gameConsolesManager;
    private DataFileManager dataFileManager;
    private ReadAndWriteConsoles readAndWriteConsoles;

    public ConsolesGameMenu(Scanner scanner, GameConsolesManager gameConsolesManager, DataFileManager dataFileManager) {
        this.scanner = scanner;
        this.gameConsolesManager = gameConsolesManager;
        this.dataFileManager = dataFileManager;
        this.readAndWriteConsoles = new ReadAndWriteConsoles();
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n--- Manage Game Consoles ---");
            System.out.println("1. Add Game Console");
            System.out.println("2. Remove Game Console");
            System.out.println("3. Update Game Console");
            System.out.println("4. View All Game Consoles");
            System.out.println("5. View Game Console by Name");
            System.out.println("6. Find Game by ID");
            System.out.println("7. Sort by Name");
            System.out.println("8. Back to Home Page \n");
            System.out.print("Choose an option: ");

            int choice = getValidIntegerInput();
            if (choice == -1) continue; // Invalid input, try again

            switch (choice) {
                case 1:
                    addGameConsole();
                    break;
                case 2:
                    removeGameConsole();
                    break;
                case 3:
                    updateGameConsole();
                    break;
                case 4:
                    displayItemsInChunks();
                    break;
                case 5:
                    viewGameConsoleByName();
                    break;
                case 6:
                    viewGameConsoleById();
                    break;
                case 7:
                    sortByName();
                    break;
                case 8:
                    return; // Back to Home Page
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    private int getValidIntegerInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return -1;
        }
    }

    private void viewGameConsoleById() {
        int gameID = getValidGameID("Enter Game ID to view: ");
        GameConsoles gameConsole = gameConsolesManager.getById(gameID);
        if (gameConsole != null) {
            displayGameConsoleDetails(gameConsole);
        } else {
            System.out.println("No game console found with ID " + gameID);
        }
    }

    private void displayGameConsoleDetails(GameConsoles gameConsole) {
        System.out.println("Game Console Details:");
        System.out.println("ID: " + gameConsole.getGameID());
        System.out.println("Name: " + gameConsole.getGameName());
        System.out.println("Live Service: " + gameConsole.getLiveServiceOrNot());
        System.out.println("Date: " + gameConsole.getGameDate());
        System.out.println("Publisher: " + gameConsole.getGamePublisher());
        System.out.println("Info: " + gameConsole.getGameInfo());
        System.out.println("Platform: " + gameConsole.getGamePlatform());
    }

    private void displayItemsInChunks() {
        int chunkSize = 6;
        int pageNumber = 1;
        List<GameConsoles> allGameConsoles = gameConsolesManager.getAll();
        int totalItems = allGameConsoles.size();

        while (true) {
            int start = (pageNumber - 1) * chunkSize;
            int end = Math.min(start + chunkSize, totalItems);

            if (start >= totalItems) {
                System.out.println("No more items to display.");
                break;
            }

            displayGameConsoles(allGameConsoles.subList(start, end));

            String input = getPagingInput();
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

    private void displayGameConsoles(List<GameConsoles> gameConsoles) {
        System.out.println("Displaying items:");
        for (GameConsoles gameConsole : gameConsoles) {
            System.out.println(gameConsole);
        }
    }

    private String getPagingInput() {
        System.out.print("Enter 'n' for next page, 'p' for previous page, or 'q' to quit: ");
        return scanner.nextLine().trim().toLowerCase();
    }

    private void sortByName() {
        gameConsolesManager.sortByName();
        System.out.println("Game consoles sorted by name.");
        viewAllGameConsoles(); // Display sorted list
    }

    private void addGameConsole() {
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

        System.out.print("Enter Device: ");
        String gamePlatform = scanner.nextLine();

        GameConsoles newGameConsole = new GameConsoles(gameID, gameName, liveServiceOrNot, gameDate, gamePublisher, gameInfo, gamePlatform);
        gameConsolesManager.add(newGameConsole);

        System.out.println("Game console added successfully.");
        displayWriteMenu();
    }

    private void displayWriteMenu() {
        System.out.println("\n--- Is this game cross-platform?: ---");
        System.out.println("1. Consoles Only");
        System.out.println("2. and PCs");
        System.out.println("3. and Mobile");
        System.out.println("4. and PCs and Mobile \n");

        int choice = getValidIntegerInput();
        if (choice == -1) return; // Invalid input, try again

        // Determine which files to write based on user choice
        List<String> filesToWrite = determineFilesToWrite(choice);
        if (filesToWrite.isEmpty()) {
            System.out.println("Invalid choice. No files will be written.");
            return;
        }

        // Write data to the selected files
        gameConsolesManager.writeDataToSelectedFiles(filesToWrite);

        System.out.println("Data written to the selected file(s).");
    }

    private List<String> determineFilesToWrite(int choice) {
        List<String> filesToWrite = new ArrayList<>();
        switch (choice) {
            case 1:
                filesToWrite.add(dataFileManager.getConsoleFile());
                break;
            case 2:
                filesToWrite.add(dataFileManager.getPcFile());
                filesToWrite.add(dataFileManager.getConsoleFile());
                break;
            case 3:
                filesToWrite.add(dataFileManager.getMobileFile());
                filesToWrite.add(dataFileManager.getConsoleFile());
                break;
            case 4:
                filesToWrite.add(dataFileManager.getPcFile());
                filesToWrite.add(dataFileManager.getMobileFile());
                filesToWrite.add(dataFileManager.getConsoleFile());
                break;
            default:
                break;
        }
        return filesToWrite;
    }

    private void removeGameConsole() {
        int gameID = getValidGameID("Enter Game ID: ");
        if (gameID == -1) return;

        GameConsoles gameConsole = gameConsolesManager.getById(gameID);
        if (gameConsole != null) {
            gameConsolesManager.removeById(gameID, gameConsole);
            System.out.println("Game console removed successfully.");
        } else {
            System.out.println("Game console not found.");
        }
    }

    private void updateGameConsole() {
        int gameID = getValidGameID("Enter Game ID to update: ");
        if (gameID == -1) return;

        GameConsoles existingGameConsole = gameConsolesManager.getById(gameID);

        if (existingGameConsole != null) {
            updateGameConsoleDetails(existingGameConsole);
            gameConsolesManager.update(gameID, existingGameConsole);
            System.out.println("Game console updated successfully.");
        } else {
            System.out.println("Game console not found.");
        }
    }

    private void updateGameConsoleDetails(GameConsoles gameConsole) {
        System.out.print("Enter new Game Name (or ~ to keep current): ");
        updateField(scanner.nextLine(), gameConsole::setGameName, gameConsole.getGameName());

        System.out.print("Enter new Live Service (Yes/No) (or ~ to keep current): ");
        updateField(scanner.nextLine(), gameConsole::setLiveServiceOrNot, gameConsole.getLiveServiceOrNot());

        System.out.print("Enter new Game Date (or ~ to keep current): ");
        updateField(scanner.nextLine(), gameConsole::setGameDate, gameConsole.getGameDate());

        System.out.print("Enter new Game Publisher (or ~ to keep current): ");
        updateField(scanner.nextLine(), gameConsole::setGamePublisher, gameConsole.getGamePublisher());

        System.out.print("Enter new Game Info (or ~ to keep current): ");
        updateField(scanner.nextLine(), gameConsole::setGameInfo, gameConsole.getGameInfo());

        System.out.print("Enter new Game Platform (or ~ to keep current): ");
        updateField(scanner.nextLine(), gameConsole::setGamePlatform, gameConsole.getGamePlatform());
    }

    private void updateField(String newValue, java.util.function.Consumer<String> updateMethod, String currentValue) {
        if (!newValue.equals("~")) {
            updateMethod.accept(newValue);
        }
    }

    private void viewAllGameConsoles() {
        List<GameConsoles> allGameConsoles = gameConsolesManager.getAll();
        if (allGameConsoles.isEmpty()) {
            System.out.println("No game consoles found.");
        } else {
            displayGameConsoles(allGameConsoles);
        }
    }

    private void viewGameConsoleByName() {
        System.out.print("Enter Game Name to search: ");
        String gameName = scanner.nextLine();
        GameConsoles gameConsole = gameConsolesManager.getByName(gameName);
        if (gameConsole != null) {
            displayGameConsoleDetails(gameConsole);
        } else {
            System.out.println("Game console not found.");
        }
    }

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
