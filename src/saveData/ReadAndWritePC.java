package saveData;

import model.GameMobile;
import model.GamePCs;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadAndWritePC {

    private static final String HEADER = "GameID,GameName,LiveService,GameDate,GamePublisher,GameInfo,GameStore";
    // Write data to the specified CSV file
    public void writeData(List<GamePCs> list, String filePath) {
        System.out.println("Writing data to: " + filePath); // Debugging line

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(HEADER); // Write header
            writer.newLine();

            for (GamePCs gp : list) {
                // Directly join fields with commas without escaping
                String line = String.join(",",
                        String.valueOf(gp.getGameID()),
                        gp.getGameName(),
                        gp.getLiveServiceOrNot(),
                        gp.getGameDate(),
                        gp.getGamePublisher(),
                        gp.getGameInfo(),
                        gp.getGameStore());
                System.out.println("Writing line: " + line); // Debugging line
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing data to " + filePath + ": " + e.getMessage());
        }
    }

    // Read data from the specified CSV file
    public List<GamePCs> readData(String filePath) {
        List<GamePCs> gamePCsList = new ArrayList<>();

        File file = new File(filePath);
        // Check if the file exists before reading
        if (!file.exists()) {
            System.out.println("File does not exist. Creating file: " + filePath);
            try {
                file.createNewFile();  // Create the file if it doesn't exist
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
            return gamePCsList;  // Return an empty list if the file does not exist
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
                        String gameStore = parts[6].trim();

                        GamePCs gamePCs = new GamePCs(gameID, gameName, liveServiceOrNot, gameDate, gamePublisher, gameInfo, gameStore);
                        gamePCsList.add(gamePCs);
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
        return gamePCsList;
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

