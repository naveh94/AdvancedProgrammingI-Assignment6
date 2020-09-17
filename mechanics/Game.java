package mechanics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {

    public enum Player { PLAYER1, PLAYER2 }

    private Board board;
    private Player currentTurn;
    private List<Point> availableMoves;
    private Map<Player, Board.Mark> playerMarkMap;
    private Map<Player, Counter> scoreMap;
    private boolean isGameOn;
    private int turnsWithoutMoves;

    /**
     * Creates a new game object.
     */
    public Game() {
        this.isGameOn = false;
        this.currentTurn = Player.PLAYER1;
        this.playerMarkMap = new HashMap<>();
        this.scoreMap = new HashMap<>();
        this.scoreMap.put(Player.PLAYER1, new Counter(0));
        this.scoreMap.put(Player.PLAYER2, new Counter(0));
        this.availableMoves = new ArrayList<>();
    }

    /**
     * Initializing game with given settings.
     * @param gameSettings setting object.
     */
    public void initialize(GameSettings gameSettings) {
        this.board = new Board(gameSettings.getBoardWidth(), gameSettings.getBoardHeight());
        this.currentTurn = gameSettings.getStartingPlayer();
        initializeTurnMap();
    }

    /**
     * Start the game.
     */
    public void start() {
        isGameOn = true;
        this.updateAvailableMoves();
        this.updateScore();
        turnsWithoutMoves = 0;
    }

    /**
     * End the game.
     */
    public void endGame() {
        this.isGameOn = false;
        this.board = null;
        this.availableMoves.clear();
        this.playerMarkMap.clear();
        this.currentTurn = null;
    }

    /**
     * Initializing the player to mark map.
     */
    private void initializeTurnMap() {
        playerMarkMap.put(Player.PLAYER1, Board.Mark.BLACK);
        playerMarkMap.put(Player.PLAYER2, Board.Mark.WHITE);
    }

    /**
     * Play given move.
     * @param move Point
     * @return true if move was played, false if game isn't on, or move wasn't available.
     */
    public boolean playMove(Point move) {
        if (!isGameOn) {
            return false;
        }
        if (this.availableMoves.contains(move)) {
            this.checkMove(move, true);
            this.switchTurn();
            updateAvailableMoves();
            updateScore();
            return true;
        }
        return false;
    }

    /**
     * Get the current score of given Player.
     * @param player Player
     * @return int.
     */
    public int getScore(Player player) {
        return scoreMap.get(player).getCount();
    }

    /**
     * Update the availableMoves list with all available moves for current player.
     */
    private void updateAvailableMoves() {
        if (!isGameOn) {
            return;
        }
        this.availableMoves.clear();
        for (int i = 0; i < this.board.getBoardHeight(); i++) {
            for (int j = 0; j < this.board.getBoardWidth(); j++) {
                Point p = new Point(i, j);
                if (this.board.getMark(p) == Board.Mark.BLANK) {
                    if (checkMove(p, false)) {
                        this.availableMoves.add(p);
                    }
                }
            }
        }
        if (this.turnsWithoutMoves > 1) {
            return;
        }
        if (this.availableMoves.isEmpty()) {
            turnsWithoutMoves ++;
            switchTurn();
            updateAvailableMoves();
        } else {
            turnsWithoutMoves = 0;
        }
    }

    /**
     * Check if a move is available. If flip is true, it also play the given move.
     * @param move Point
     * @param flip boolean
     * @return if move is available, returns true.
     */
    private boolean checkMove(Point move, boolean flip) {
        int flippables = 0, temp_flippables;
        for (Point.Direction dir : Point.directions) {
            temp_flippables = checkDirection(move, dir);
            flippables += temp_flippables;
            if (flip) {
                flip(move, dir, temp_flippables);
            }
        }
        if (flippables > 0) {
            return true;
        }
        return false;
    }

    /**
     * Flip given amount of coins in given direction, starting from given Point.
     * @param move starting point
     * @param dir Direction
     * @param slots number of coins to flip.
     */
    private void flip(Point move, Point.Direction dir, int slots) {
        Point move_copy = new Point(move);
        for (int i = 0; i <= slots; i++) {
            board.setMark(move_copy.getX(), move_copy.getY(), this.playerMarkMap.get(this.currentTurn));
            move_copy = move_copy.moveDirection(dir);
        }
    }

    /**
     * Check given direction and return the number of coins that will be flipped in this direction if
     * move was played.
     * @param move Point
     * @param dir Direction
     * @return int
     */
    private int checkDirection(Point move, Point.Direction dir) {
        Point move_copy = new Point(move);
        int count = 0;
        while (true) {
            move_copy = move_copy.moveDirection(dir);
            // Scan found an empty slot or reached a border = no coin will be flipped.
            if (move_copy.getX() < 0
                    || move_copy.getX() >= this.board.getBoardWidth()
                    || move_copy.getY() < 0
                    || move_copy.getY() >= this.board.getBoardHeight()
                    || this.board.getMark(move_copy) == Board.Mark.BLANK) {
                return 0;
            }
            // Scan reached another coin of current player = every slot counted will be flipped.
            if (board.getMark(move_copy) == this.playerMarkMap.get(this.currentTurn)) {
                return count;
            }
            // Scan found an opponent's coin = raise count and keep scanning.
            if (board.getMark(move_copy) == this.playerMarkMap.get(this.getOppositePlayer(this.currentTurn))) {
                count++;
            }
        }
    }

    /**
     * Return the board's width.
     * @return int
     */
    public int getWidth() {
        return this.board.getBoardWidth();
    }

    /**
     * Returns the board's height.
     * @return int
     */
    public int getHeight() {
        return this.board.getBoardHeight();
    }

    /**
     * Return the mark of given point
     * @param i point's x parameter
     * @param j point's y parameter
     * @return Mark
     */
    public Board.Mark getMark(int i, int j) {
        return this.board.getMark(new Point(i, j));
    }

    /**
     * Get the opposite player of given Player.
     * @param player Player
     * @return Player
     */
    public Player getOppositePlayer(Player player) {
        if (player == Player.PLAYER1) {
            return Player.PLAYER2;
        }
        return Player.PLAYER1;
    }

    /**
     * Switch currentTurn to opposite player.
     */
    private void switchTurn() {
        this.currentTurn = getOppositePlayer(this.currentTurn);
    }

    /**
     * Return a constant reference to availableMoves list.
     * @return List
     */
    public final List<Point> getAvailableMoves() {
        return availableMoves;
    }

    /**
     * Updates current score of both players.
     */
    private void updateScore() {
        scoreMap.get(Player.PLAYER1).resetCount();
        scoreMap.get(Player.PLAYER2).resetCount();
        Board.Mark current;
        for (int i = 0; i < this.board.getBoardWidth(); i++) {
            for (int j = 0; j < this.board.getBoardHeight(); j++) {
                current = board.getMark(new Point(i, j));
                if (current == playerMarkMap.get(Player.PLAYER1)) {
                    scoreMap.get(Player.PLAYER1).raiseCount();
                } else if (current == playerMarkMap.get(Player.PLAYER2)) {
                    scoreMap.get(Player.PLAYER2).raiseCount();
                }
            }
        }
    }

    /**
     * Return value of gameOver
     * @return boolean
     */
    public boolean isGameOver() {
        return (this.turnsWithoutMoves > 1);
    }

    /**
     * Returns currentTurn
     * @return Player
     */
    public Player getCurrentTurn() {
        return currentTurn;
    }
}
