package model;

public class GamePCs {
    private int gameID;
    private String gameName;
    private String liveServiceOrNot;
    private String gameDate;
    private String gamePublisher;
    private String gameInfo;
    private String gameStore;

    public GamePCs(int gameID, String gameName, String liveServiceOrNot, String gameDate, String gamePublisher, String gameInfo, String gameStore) {
        this.gameID = gameID;
        this.gameName = gameName;
        this.liveServiceOrNot = liveServiceOrNot;
        this.gameDate = gameDate;
        this.gamePublisher = gamePublisher;
        this.gameInfo = gameInfo;
        this.gameStore = gameStore;
    }


    public int getGameID() { return gameID; }
    public void setGameID(int gameID) { this.gameID = gameID; }
    public String getGameName() { return gameName; }
    public void setGameName(String gameName) { this.gameName = gameName; }
    public String getLiveServiceOrNot() { return liveServiceOrNot; }
    public void setLiveServiceOrNot(String liveServiceOrNot) { this.liveServiceOrNot = liveServiceOrNot; }
    public String getGameDate() { return gameDate; }
    public void setGameDate(String gameDate) { this.gameDate = gameDate; }
    public String getGamePublisher() { return gamePublisher; }
    public void setGamePublisher(String gamePublisher) { this.gamePublisher = gamePublisher; }
    public String getGameInfo() { return gameInfo; }
    public void setGameInfo(String gameInfo) { this.gameInfo = gameInfo; }
    public String getGameStore() { return gameStore; }
    public void setGameStore(String gameStore) { this.gameStore = gameStore; }

    @Override
    public String toString() {
        return "ID: " + gameID + ", Name: " + gameName + ", Type: " + liveServiceOrNot + ", Date: " + gameDate + ", Publisher: " + gamePublisher + ", Store: " + gameStore;
    }
}

