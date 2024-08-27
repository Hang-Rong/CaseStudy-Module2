package model;

public class GameConsoles {
    private int gameID;
    private String gameName;
    private String liveServiceOrNot;
    private String gameDate;
    private String gamePublisher;
    private String gameInfo;
    private String gamePlatform;

    public GameConsoles(int gameID, String gameName, String liveServiceOrNot, String gameDate, String gamePublisher, String gameInfo, String gamePlatform) {
        this.gameID = gameID;
        this.gameName = gameName;
        this.liveServiceOrNot = liveServiceOrNot;
        this.gameDate = gameDate;
        this.gamePublisher = gamePublisher;
        this.gameInfo = gameInfo;
        this.gamePlatform = gamePlatform;
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
    public String getGamePlatform() { return gamePlatform; }
    public void setGamePlatform(String gamePlatform) { this.gamePlatform = gamePlatform; }

    @Override
    public String toString() {
        return "ID: " + gameID + ", Name: " + gameName + ", Type: " + liveServiceOrNot + ", Date: " + gameDate + ", Publisher: " + gamePublisher + ", Platform: " + gamePlatform;
    }
}
