package mechanics;

public class Board {

    public enum Mark {BLANK, BLACK, WHITE}

    private Mark[][] board;
    private int boardWidth;
    private int boardHeight;

    /**
     * Creates a new board object using given width and height parameters.
     * @param width the board width.
     * @param height the board height.
     */
    public Board(int width, int height) {
        this.boardHeight = height;
        this.boardWidth = width;
        this.board = new Mark[width][height];
        initialize();
    }

    /**
     * Initializing the board for a new game.
     */
    private void initialize() {
        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                this.board[i][j] = Mark.BLANK;
            }
        }
        this.board[boardHeight / 2][boardWidth / 2] = Mark.WHITE;
        this.board[boardHeight / 2 - 1][boardWidth / 2 - 1] = Mark.WHITE;
        this.board[boardHeight / 2][boardWidth / 2 - 1] = Mark.BLACK;
        this.board[boardHeight / 2 - 1][boardWidth / 2] = Mark.BLACK;
    }

    /**
     * Returns the board's height
     * @return int
     */
    public int getBoardHeight() {
        return boardHeight;
    }

    /**
     * Returns the board's width.
     * @return int
     */
    public int getBoardWidth() {
        return boardWidth;
    }

    /**
     * Return the mark on given point on the board.
     * @param p Point
     * @return Mark
     */
    public Mark getMark(Point p) {
        return board[p.getX()][p.getY()];
    }

    /**
     * Set the mark for given point on the board to given mark.
     * @param i block's x parameter
     * @param j block's y parameter
     * @param mark Mark
     */
    public void setMark(int i, int j, Board.Mark mark) {
        this.board[i][j] = mark;
    }
}
