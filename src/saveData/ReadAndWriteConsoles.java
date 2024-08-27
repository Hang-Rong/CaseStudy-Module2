package saveData;

import model.GameConsoles;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadAndWriteConsoles {

    private static final String HEADER = "GameID,GameName,LiveService,GameDate,GamePublisher,GameInfo,GameDevice";

    // Write data to the specified CSV file
    public void writeData(List<GameConsoles> list, String filePath) {
        System.out.println("Writing data to: " + filePath); // Debugging line

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(HEADER); // Write header
            writer.newLine();

            for (GameConsoles gc : list) {
                // Directly join fields with commas without escaping
                String line = String.join(",",
                        String.valueOf(gc.getGameID()),
                        gc.getGameName(),
                        gc.getLiveServiceOrNot(),
                        gc.getGameDate(),
                        gc.getGamePublisher(),
                        gc.getGameInfo(),
                        gc.getGamePlatform());
                System.out.println("Writing line: " + line); // Debugging line
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing data to " + filePath + ": " + e.getMessage());
        }
    }

    // Read data from the specified CSV file
    public List<GameConsoles> readData(String filePath) {
        List<GameConsoles> gameConsolesList = new ArrayList<>();

        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("File does not exist. Creating file: " + filePath);
            try {
                file.createNewFile();  // Create the file if it doesn't exist
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
            return gameConsolesList;  // Return an empty list if the file does not exist
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeaderSkipped = false;

            while ((line = br.readLine()) != null) {
                if (!isHeaderSkipped) {
                    isHeaderSkipped = true; // Skip header
                    continue;
                }
                try {
                    // Use a simple CSV split
                    String[] parts = line.split(",", -1);
                    if (parts.length == 7) {
                        int gameID = Integer.parseInt(parts[0].trim());
                        String gameName = parts[1].trim();
                        String liveServiceOrNot = parts[2].trim();
                        String gameDate = parts[3].trim();
                        String gamePublisher = parts[4].trim();
                        String gameInfo = parts[5].trim();
                        String gamePlatform = parts[6].trim();

                        GameConsoles gameConsole = new GameConsoles(gameID, gameName, liveServiceOrNot, gameDate, gamePublisher, gameInfo, gamePlatform);
                        gameConsolesList.add(gameConsole);
                    } else {
                        System.out.println("Skipping invalid line (incorrect number of fields): " + line);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid line (NumberFormatException): " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return gameConsolesList;
    }

    // Helper method to parse CSV lines with proper handling of commas and quotes
    private String[] parseCSVLine(String line) {
        List<String> fields = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder currentField = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
                currentField.append(c);
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField.setLength(0);
            } else {
                currentField.append(c);
            }
        }
        fields.add(currentField.toString()); // Add last field

        return fields.toArray(new String[0]);
    }

    // Helper method to escape CSV values
    private String escapeCSV(String value) {
        if (value == null || value.isEmpty()) {
            return "\"\"";
        }
        String escapedValue = value.replace("\"", "\"\"");
        return "\"" + escapedValue + "\"";
    }
}
