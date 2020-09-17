package gameapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import mechanics.Game;
import mechanics.GameSettings;

import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    @FXML
    private HBox root;
    @FXML
    private Button start;
    @FXML
    private Button exit;
    @FXML
    private Button settings;
    @FXML
    private Label scoreTitle;
    @FXML
    private Label currentTurn;
    @FXML
    private Label player1Score;
    @FXML
    private Label player2Score;

    private boolean gameOn;

    private GameSettings gameSettings;

    /**
     * @
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.gameSettings = GameSettings.loadFromFile();
        SettingScreen settingScreen = new SettingScreen(gameSettings);
        GameBoard gameBoard = new GameBoard(player1Score, player2Score, currentTurn);
        Game game = new Game();
        this.gameOn = false;
        this.exit.setVisible(false);
        this.currentTurn.setVisible(false);
        this.scoreTitle.setVisible(false);
        this.scoreTitle.setStyle("-fx-font-weight: BOLD");
        this.player1Score.setVisible(false);
        this.player2Score.setVisible(false);

        start.setOnAction(event -> {
            gameBoard.setPrefWidth(root.getWidth() - 120);
            gameBoard.setPrefHeight(root.getHeight());
            root.getChildren().add(0, gameBoard);
            root.setOnKeyPressed(gameBoard.getOnKeyPressed());
            game.initialize(gameSettings);
            this.gameOn = true;
            game.start();
            gameBoard.setGame(game);
            gameBoard.draw(gameSettings.getPlayer1Color(), gameSettings.getPlayer2Color());

            String cTurn = (game.getCurrentTurn() == Game.Player.PLAYER1) ? "Player 1" : "Player 2";
            this.currentTurn.setText("Turn: " + cTurn);
            this.player1Score.setText("Player 1: " + game.getScore(Game.Player.PLAYER1));
            this.player2Score.setText("Player 2: " + game.getScore(Game.Player.PLAYER2));
            this.currentTurn.setVisible(true);
            this.scoreTitle.setVisible(true);
            this.player1Score.setVisible(true);
            this.player2Score.setVisible(true);
            this.start.setVisible(false);
            this.settings.setVisible(false);
            this.exit.setText("End Game");
            this.exit.setVisible(true);
        });

        settings.setOnAction( event -> {
            this.settings.setVisible(false);
            this.exit.setText("Close");
            this.exit.setVisible(true);
            this.start.setVisible(false);
            settingScreen.resetChangeLabel();
            this.root.getChildren().add(0, settingScreen);
        });

        exit.setOnAction( event -> {
            game.endGame();
            this.currentTurn.setVisible(false);
            this.scoreTitle.setVisible(false);
            this.player1Score.setVisible(false);
            this.player2Score.setVisible(false);
            this.start.setVisible(true);
            this.settings.setVisible(true);
            this.exit.setVisible(false);
            this.gameOn = false;
            this.root.getChildren().remove(0);
        });

        root.widthProperty().addListener((observable, oldValue, newValue) -> {
            double boardNewWidth = newValue.doubleValue() - 120;
            gameBoard.setPrefWidth(boardNewWidth);
            if (this.gameOn) {
                gameBoard.draw(gameSettings.getPlayer1Color(), gameSettings.getPlayer2Color());
            }
        });
        root.heightProperty().addListener(((observable, oldValue, newValue) -> {
            gameBoard.setPrefHeight(newValue.doubleValue());
            if (this.gameOn) {
                gameBoard.draw(gameSettings.getPlayer1Color(), gameSettings.getPlayer2Color());
            }
        }));
    }
}
