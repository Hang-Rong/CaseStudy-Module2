package Manager;

import model.GameConsoles;
import saveData.ReadAndWriteConsoles;
import saveData.DataFileManager;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class GameConsolesManager implements iManager<GameConsoles> {

    private List<GameConsoles> list;
    private DataFileManager dataFileManager;
    private ReadAndWriteConsoles readAndWriteConsoles;

    public GameConsolesManager(DataFileManager dataFileManager) {
        this.dataFileManager = dataFileManager;
        this.readAndWriteConsoles = new ReadAndWriteConsoles();
        this.list = readAndWriteConsoles.readData(dataFileManager.getConsoleFile()); // Initialize with console file
    }

    @Override
    public void add(GameConsoles gameConsoles) {
        this.list.add(gameConsoles);
        writeDataToFiles(List.of(
                dataFileManager.getConsoleFile()
        ));
    }

    private void writeDataToFiles(List<String> filePaths) {
        for (String filePath : filePaths) {
            readAndWriteConsoles.writeData(list, filePath);
        }
    }

    @Override
    public void removeById(int id, GameConsoles gameConsoles) {
        list.removeIf(gc -> gc.getGameID() == id);
        writeDataToFiles(List.of(
                dataFileManager.getConsoleFile()
        ));
    }

    @Override
    public void update(int id, GameConsoles updatedGameConsoles) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getGameID() == id) {
                list.set(i, updatedGameConsoles);
                writeDataToFiles(List.of(
                        dataFileManager.getConsoleFile()
                ));
                break;
            }
        }
    }

    @Override
    public GameConsoles getById(int id) {
        for (GameConsoles gc : list) {
            if (gc.getGameID() == id) {
                return gc;
            }
        }
        return null;
    }

    @Override
    public GameConsoles getByName(String name) {
        return list.stream()
                .filter(gc -> gc.getGameName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<GameConsoles> getAll() {
        return this.list;
    }

    public void sortByName() {
        list.sort(Comparator.comparing(GameConsoles::getGameName));
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
