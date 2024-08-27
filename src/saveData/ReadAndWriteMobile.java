package saveData;

import model.GameMobile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadAndWriteMobile {
    // Write data to the specified CSV file
    public void writeData(List<GameMobile> list, String filePath) {
        System.out.println("Writing data to: " + filePath); // Debugging line

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (GameMobile gc : list) {
                String line = escapeCSV(String.valueOf(gc.getGameID())) + "," +
                        escapeCSV(gc.getGameName()) + "," +
                        escapeCSV(gc.getLiveServiceOrNot()) + "," +
                        escapeCSV(gc.getGameDate()) + "," +
                        escapeCSV(gc.getGamePublisher()) + "," +
                        escapeCSV(gc.getGameInfo()) + "," +
                        escapeCSV(gc.getGameStore());
                System.out.println("Writing line: " + line); // Debugging line
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing data to " + filePath + ": " + e.getMessage());
        }
    }

    // Read data from the specified CSV file
    public List<GameMobile> readData(String filePath) {
        List<GameMobile> gameMobileList = new ArrayList<>();

        File file = new File(filePath);
        // Check if the file exists before reading
        if (!file.exists()) {
            System.out.println("File does not exist. Creating file: " + filePath);
            try {
                file.createNewFile();  // Create the file if it doesn't exist
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
            return gameMobileList;  // Return an empty list if the file does not exist
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip header if present
            br.readLine();

            while ((line = br.readLine()) != null) {
                try {
                    // Use a CSV parser for robustness
                    String[] parts = line.split(",");
                    if (parts.length == 7) {
                        int gameID = Integer.parseInt(parts[0].trim());
                        String gameName = parts[1].trim();
                        String liveServiceOrNot = parts[2].trim();
                        String gameDate = parts[3].trim();
                        String gamePublisher = parts[4].trim();
                        String gameInfo = parts[5].trim();
                        String gameStore = parts[6].trim();

                        GameMobile gameMobile = new GameMobile(gameID, gameName, liveServiceOrNot, gameDate, gamePublisher, gameInfo, gameStore);
                        gameMobileList.add(gameMobile);
                    } else {
                        System.out.println("Skipping invalid line (incorrect number of fields): " + line);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid line (NumberFormatException): " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gameMobileList;
    }

    // Helper method to escape CSV values
    private String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        String escapedValue = value.replace("\"", "\"\"");
        return "\"" + escapedValue + "\"";
    }
}

