package mechanics;

import javafx.scene.paint.Color;

import java.io.*;
import java.util.Map;

public class GameSettings implements Serializable {

    private static final int DEFAULT_WIDTH = 8;
    private static final int DEFAULT_HEIGHT = 8;
    private static final Game.Player DEFAULT_STARTING_PLAYER = Game.Player.PLAYER1;
    private static final String DEFAULT_PLAYER1_COLOR = "Black";
    private static final String DEFAULT_PLAYER2_COLOR = "White";

    private int boardWidth;
    private int boardHeight;
    private Game.Player startingPlayer;
    private String player1Color;
    private String player2Color;
    private static transient Map<String, Color> colorMap = Map.of("Black", Color.BLACK,
            "White", Color.WHITE,
            "Green", Color.GREEN,
            "Purple", Color.PURPLE,
            "Yellow", Color.YELLOW,
            "Blue", Color.BLUE,
            "Red", Color.RED,
            "Brown", Color.BROWN,
            "Orange", Color.ORANGE,
            "Pink", Color.PINK);

    /**
     * Creates a new GameSettings object with default settings.
     */
    private GameSettings() {
        this.boardWidth = DEFAULT_WIDTH;
        this.boardHeight = DEFAULT_HEIGHT;
        this.startingPlayer = DEFAULT_STARTING_PLAYER;
        this.player1Color = DEFAULT_PLAYER1_COLOR;
        this.player2Color = DEFAULT_PLAYER2_COLOR;
    }

    /**
     * Returns board's width.
     * @return int
     */
    public int getBoardWidth() {
        return boardWidth;
    }

    /**
     * Set board's width.
     * @param boardWidth int
     */
    public void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }

    /**
     * Return's board's height.
     * @return int
     */
    public int getBoardHeight() {
        return boardHeight;
    }

    /**
     * Set the board's height.
     * @param boardHeight int
     */
    public void setBoardHeight(int boardHeight) {
        this.boardHeight = boardHeight;
    }

    /**
     * Returns starting player.
     * @return Player
     */
    public Game.Player getStartingPlayer() {
        return startingPlayer;
    }

    /**
     * Set starting player.
     * @param startingPlayer Player
     */
    public void setStartingPlayer(Game.Player startingPlayer) {
        this.startingPlayer = startingPlayer;
    }

    /**
     * Returns player 1 color.
     * @return Color
     */
    public Color getPlayer1Color() {
        return colorMap.get(player1Color);
    }

    /**
     * Set's player 1 color from string containing color's name.
     * @param player1Color String
     */
    public void setPlayer1Color(String player1Color) {
        this.player1Color = player1Color;
    }

    /**
     * Returns player 2 color.
     * @return Color
     */
    public Color getPlayer2Color() {
        return colorMap.get(player2Color);
    }

    /**
     * Set player 2 color from string containing color's name.
     * @param player2Color String
     */
    public void setPlayer2Color(String player2Color) {
        this.player2Color = player2Color;
    }

    /**
     * Creates a GameSettings object from an external config.ser file.
     * @return GameSettings object
     */
    public static GameSettings loadFromFile() {
        GameSettings gameSettings;
        try {
            FileInputStream fileIn = new FileInputStream("config.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            gameSettings = (GameSettings)in.readObject();
            in.close();
            fileIn.close();
            return gameSettings;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Couldn't load settings from file. Loading default settings.");
        gameSettings = new GameSettings();
        return gameSettings;
    }

    /**
     * Save's current setting to an external file.
     * @return true if saved, else returns false.
     */
    public boolean saveToFile() {
        try {
            FileOutputStream fileOut = new FileOutputStream("config.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Translate's string containing color's name to a Color.
     * @param colorName String
     * @return Color
     */
    public static Color fromString(String colorName) {
        return colorMap.get(colorName);
    }
}