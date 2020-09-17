package gameapp;

import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import mechanics.Board;
import mechanics.Game;
import mechanics.Point;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameBoard extends GridPane {

    private Game game;
    private Label currentTurn;
    private Label player1Score;
    private Label player2Score;

    /**
     * Creates a new GameBoard object with given references to Labels from root Pane.
     * @param player1Score Label
     * @param player2Score Label
     * @param currentTurn Label
     */
    public GameBoard(Label player1Score, Label player2Score, Label currentTurn) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmls/GameBoard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        this.currentTurn = currentTurn;
        this.player1Score = player1Score;
        this.player2Score = player2Score;

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        game = null;
    }

    /**
     * Set's the game reference to given Game object.
     * @param game Game
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Draw the board while using given colors for players' coins.
     * @param player1Color Color
     * @param player2Color Color
     */
    public void draw(Color player1Color, Color player2Color) {
        this.getChildren().clear();
        int height = (int)this.getPrefHeight();
        int width = (int)this.getPrefWidth();

        int cellHeight = height / game.getHeight();
        int cellWidth = width / game.getWidth();

        for (int i = 0; i < game.getHeight(); i++) {
            for (int j = 0; j < game.getWidth(); j++) {
                if (i % 2 == j % 2) {
                    this.add(new Rectangle(cellWidth, cellHeight, Color.LIGHTBLUE), j, i);
                } else {
                    this.add(new Rectangle(cellWidth, cellHeight, Color.LIGHTCYAN), j, i);
                }
                if (game.getAvailableMoves().contains(new Point(i, j))) {
                    Button play = new Button();
                    play.setPrefSize(cellWidth, cellHeight);
                    final int I = i, J = j;
                    play.setId("play");
                    play.setOnAction(event -> {
                        if (this.game.playMove(new Point(I, J))) {
                            draw(player1Color, player2Color);
                            String cTurn = (game.getCurrentTurn() == Game.Player.PLAYER1) ? "Player 1" : "Player 2";
                            int p1Score = game.getScore(Game.Player.PLAYER1);
                            int p2Score = game.getScore(Game.Player.PLAYER2);
                            currentTurn.setText("Turn: " + cTurn);
                            player1Score.setText("Player 1: " + p1Score);
                            player2Score.setText("Player 2: " + p2Score);
                            if (game.isGameOver()) {
                                Alert gameOver = new Alert(Alert.AlertType.INFORMATION);
                                gameOver.setTitle("Game Over!");
                                if (p1Score > p2Score) {
                                    gameOver.setHeaderText("Player 1 won with a score of " + p1Score);
                                } else if (p1Score < p2Score) {
                                    gameOver.setHeaderText("Player 2 won with a score of " + p2Score);
                                } else {
                                    gameOver.setHeaderText("The game ended with a tie of " + p1Score);
                                }
                                gameOver.setContentText("Congratulations! We hope you enjoyed the game!");
                                gameOver.showAndWait();
                            }
                        }
                    });
                    this.add(play, j, i);
                }
                int cellMinSize = (cellWidth > cellHeight) ? cellHeight : cellWidth;
                List<Circle> coin = new ArrayList<>();
                if (game.getMark(i, j) == Board.Mark.BLACK){
                    coin.add(new Circle(cellMinSize / 2 - 2, player1Color));
                    coin.add(new Circle(cellMinSize / 2 - 3, player2Color));
                    coin.add(new Circle(cellMinSize / 2 - 5, player1Color));
                } else if (game.getMark(i, j) == Board.Mark.WHITE) {
                    coin.add(new Circle(cellMinSize / 2 - 2, player2Color));
                    coin.add(new Circle(cellMinSize / 2 - 3, player1Color));
                    coin.add(new Circle(cellMinSize / 2 - 5, player2Color));
                }
                for (Circle circle : coin) {
                    this.add(circle, j, i);
                    this.setHalignment(circle, HPos.CENTER);
                }
            }
        }
    }
}
