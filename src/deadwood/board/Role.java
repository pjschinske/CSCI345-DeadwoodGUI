package deadwood.board;

import deadwood.Player;

public class Role {
    private String name;
    private String line;
    private int requiredRank;
    private int x;
    private int y;
    private int sceneX;
    private int sceneY;
    private int width;
    private int height;
    private Player player;

    public Role(String name, String line, int requiredRank, int x, int y, int width, int height) {
        this.name = name;
        this.line = line;
        this.requiredRank = requiredRank;
        this.x = x;
        this.y = y;
        sceneX = x;
        sceneY = y;
        this.width = width;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public String getLine() {
        return line;
    }

    public int getRequiredRank() {
        return requiredRank;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean hasPlayer() {
        return player != null;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSceneX() {
        return sceneX;
    }

    public int getSceneY() {
        return sceneY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    void setSceneLocation(int cardX, int cardY) {
        sceneX = x + cardX;
        sceneY = y + cardY;
    }
}
