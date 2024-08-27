package Manager;

import model.GamePCs;
import saveData.DataFileManager;
import saveData.ReadAndWritePC;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class GamePCsManager implements iManager<GamePCs> {

    private List<GamePCs> list;
    private DataFileManager dataFileManager;
    private ReadAndWritePC readAndWritePC;

    public GamePCsManager(DataFileManager dataFileManager) {
        this.dataFileManager = dataFileManager;
        this.readAndWritePC = new ReadAndWritePC();
        this.list = readAndWritePC.readData(dataFileManager.getPcFile());
    }

    @Override
    public void add(GamePCs gamePCs) {
        this.list.add(gamePCs);
    }
    public void writeDataToSelectedFiles(List<String> filePaths) {
        for (String filePath : filePaths) {
            System.out.println("Writing data to file: " + filePath); // Debugging line
            readAndWritePC.writeData(list, filePath);
        }
    }

    private void writeDataToFiles(List<String> filePaths) {
        for (String filePath : filePaths) {
            System.out.println("Writing data to file: " + filePath); // Debugging line
            readAndWritePC.writeData(list, filePath);
        }
    }

    @Override
    public void removeById(int id, GamePCs gamePCs) {
        list.removeIf(gp -> gp.getGameID() == id);
        writeDataToFiles(List.of(
                dataFileManager.getPcFile()
        ));
    }

    @Override
    public void update(int id, GamePCs updatedGamePCs) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getGameID() == id) {
                list.set(i, updatedGamePCs);
                writeDataToFiles(List.of(
                        dataFileManager.getPcFile()
                ));
                break;
            }
        }
    }

    @Override
    public GamePCs getById(int id) {
        for (GamePCs gp : list) {
            if (gp.getGameID() == id) {
                return gp;
            }
        }
        return null;
    }

    @Override
    public GamePCs getByName(String name) {
        return list.stream()
                .filter(gp -> gp.getGameName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<GamePCs> getAll() {
        return this.list;
    }

    public void sortByName() {
        list.sort(Comparator.comparing(GamePCs::getGameName));
    }

    public void displayItemsInChunks() {
        Scanner scanner = new Scanner(System.in);
        int startIndex = 0;
        int chunkSize = 5;

        while (startIndex < list.size()) {
            int endIndex = Math.min(startIndex + chunkSize, list.size());
            for (int i = startIndex; i < endIndex; i++) {
                System.out.println(list.get(i));
            }
            startIndex = endIndex;
            if (startIndex < list.size()) {
                System.out.print("Press Enter to see more...");
                scanner.nextLine();
            }
        }
    }
}
