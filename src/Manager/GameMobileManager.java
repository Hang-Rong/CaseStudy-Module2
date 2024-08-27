package Manager;

import model.GameMobile;
import saveData.DataFileManager;
import saveData.ReadAndWriteMobile;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class GameMobileManager implements iManager<GameMobile> {

    private List<GameMobile> list;
    private DataFileManager dataFileManager;
    private ReadAndWriteMobile readAndWriteMobile;

    public GameMobileManager(DataFileManager dataFileManager) {
        this.dataFileManager = dataFileManager;
        this.readAndWriteMobile = new ReadAndWriteMobile();
        this.list = readAndWriteMobile.readData(dataFileManager.getMobileFile());
    }

    @Override
    public void add(GameMobile gameMobile) {
        this.list.add(gameMobile);
        writeDataToFiles(List.of(
                dataFileManager.getMobileFile()
        ));
    }

    private void writeDataToFiles(List<String> filePaths) {
        for (String filePath : filePaths) {
            readAndWriteMobile.writeData(list, filePath);
        }
    }

    @Override
    public void removeById(int id, GameMobile gameMobile) {
        list.removeIf(gc -> gc.getGameID() == id);
        writeDataToFiles(List.of(
                dataFileManager.getMobileFile()
        ));
    }

    @Override
    public void update(int id, GameMobile updatedGameMobile) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getGameID() == id) {
                list.set(i, updatedGameMobile);
                writeDataToFiles(List.of(
                        dataFileManager.getMobileFile()
                ));
                break;
            }
        }
    }

    @Override
    public GameMobile getById(int id) {
        for (GameMobile gc : list) {
            if (gc.getGameID() == id) {
                return gc;
            }
        }
        return null;
    }

    @Override
    public GameMobile getByName(String name) {
        return list.stream()
                .filter(gc -> gc.getGameName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<GameMobile> getAll() {
        return this.list;
    }

    public void sortByName() {
        list.sort(Comparator.comparing(GameMobile::getGameName));
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
