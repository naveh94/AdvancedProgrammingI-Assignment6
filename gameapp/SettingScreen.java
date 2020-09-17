package gameapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import mechanics.Game;
import mechanics.GameSettings;

import java.io.IOException;
import java.util.Map;


public class SettingScreen extends VBox {

    private ChoiceBox<Integer> sizeChoice;
    private ChoiceBox<Game.Player> playerChoice;
    private ChoiceBox<String> player1ColorChoice;
    private ChoiceBox<String> player2ColorChoice;
    private Map<String, Color> colorMap;
    private Label changesSaved;
    private Button apply;
    private GameSettings gameSettings;

    /**
     * Creates a new SettingScreen screen, with given referance to GameSettings object.
     * @param gameSettings GameSettings
     */
    public SettingScreen(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmls/SettingScreen.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        draw();
    }

    /**
     * Draw the SettingScreen.
     */
    public void draw() {
        this.setPrefSize(400, 400);

        HBox titlePane = new HBox();
        titlePane.setPrefSize(400, 40);
        titlePane.setStyle("-fx-alignment: CENTER");
        Label header = new Label("Game Settings:");
        header.setStyle("-fx-font-weight: BOLD");
        titlePane.getChildren().add(header);
        this.getChildren().add(0, titlePane);

        HBox sizePane = new HBox();
        sizePane.setPrefSize(400, 40);
        sizePane.setStyle("-fx-alignment: CENTER");
        sizePane.setSpacing(10);
        sizePane.getChildren().add(0, new Label("Game Size:"));
        sizeChoice = new ChoiceBox<>();
        setSizeChoice();
        sizePane.getChildren().add(1, sizeChoice);
        this.getChildren().add(1, sizePane);

        HBox startingPlayerPane = new HBox();
        startingPlayerPane.setPrefSize(400, 40);
        startingPlayerPane.setStyle("-fx-alignment: CENTER");
        startingPlayerPane.setSpacing(10);
        startingPlayerPane.getChildren().add(0, new Label("Starting Player:"));
        playerChoice = new ChoiceBox<>();
        setPlayerChoice();
        startingPlayerPane.getChildren().add(1, playerChoice);
        this.getChildren().add(2, startingPlayerPane);

        player1ColorChoice = new ChoiceBox<>();
        player2ColorChoice = new ChoiceBox<>();
        setColorChoice();
        HBox player1ColorPane = new HBox();
        player1ColorPane.setPrefSize(400, 40);
        player1ColorPane.setStyle("-fx-alignment: CENTER");
        player1ColorPane.setSpacing(10);
        player1ColorPane.getChildren().add(0, new Label("Player 1 Color:"));
        player1ColorPane.getChildren().add(1, player1ColorChoice);
        HBox player2ColorPane = new HBox();
        player2ColorPane.setPrefSize(400, 40);
        player2ColorPane.setStyle("-fx-alignment: CENTER");
        player2ColorPane.setSpacing(10);
        player2ColorPane.getChildren().add(0, new Label("Player 2 Color:"));
        player2ColorPane.getChildren().add(1, player2ColorChoice);
        this.getChildren().add(3, player1ColorPane);
        this.getChildren().add(4, player2ColorPane);

        HBox buttonsPane = new HBox();
        buttonsPane.setPrefSize(400, 40);
        buttonsPane.setStyle("-fx-alignment: CENTER");
        buttonsPane.setSpacing(10);
        this.apply = new Button("Apply Changes");
        buttonsPane.getChildren().add(0, apply);
        changesSaved = new Label("");

        apply.setOnAction(event -> {
            changesSaved.setText("");
            if (this.gameSettings.getBoardWidth() != this.sizeChoice.getValue()) {
                gameSettings.setBoardHeight(sizeChoice.getValue());
                gameSettings.setBoardWidth(sizeChoice.getValue());
                changesSaved.setText(changesSaved.getText()
                        + "Size changed to " + sizeChoice.getValue() + ". \n");
            }
            if (this.gameSettings.getStartingPlayer() != this.playerChoice.getValue()) {
                this.gameSettings.setStartingPlayer(this.playerChoice.getValue());
                changesSaved.setText(changesSaved.getText()
                        + "Starting Player changed to " + playerChoice.getValue() + ". \n");
            }
            String p1Choice = player1ColorChoice.getValue();
            String p2Choice = player2ColorChoice.getValue();
            if (p1Choice.equals(p2Choice)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Both players have the same color!");
                alert.setHeaderText("Both players have the same color!");
                alert.setContentText("You must choose different color for each player.");
                alert.showAndWait();
            } else {
                if (GameSettings.fromString(p1Choice) != gameSettings.getPlayer1Color()) {
                    gameSettings.setPlayer1Color(player1ColorChoice.getValue());
                    changesSaved.setText(changesSaved.getText()
                            + "Player 1 Color changed to " + player1ColorChoice.getValue() + ". \n");
                }
                if (GameSettings.fromString(p2Choice) != gameSettings.getPlayer2Color()) {
                    gameSettings.setPlayer2Color(p2Choice);
                    changesSaved.setText(changesSaved.getText()
                            + "Player 2 Color changed to " + player2ColorChoice.getValue() + ". \n");
                }
            }
            gameSettings.saveToFile();
        });
        this.getChildren().add(5, buttonsPane);

        HBox changesPane = new HBox();
        changesPane.setPrefSize(400, 100);
        changesPane.setStyle("-fx-alignment: CENTER");
        changesPane.getChildren().add(0, changesSaved);
        changesPane.setSpacing(10);
        this.getChildren().add(6, changesPane);
    }

    /**
     * Resets the Label announcing saved changes.
     */
    public void resetChangeLabel() {
        this.changesSaved.setText("");
    }

    /**
     * Initialize sizeChoice ChoiceBox inserting options, and initializing value.
     */
    private void setSizeChoice() {
        for (int i = 4; i <= 20; i++) {
            sizeChoice.getItems().add(i);
        }
        this.sizeChoice.getSelectionModel().select(gameSettings.getBoardHeight() - 4);
    }

    /**
     * Initializing playerChoice ChoiceBox, inserting options, and initializing value.
     */
    private void setPlayerChoice() {
        this.playerChoice.getItems().add(Game.Player.PLAYER1);
        this.playerChoice.getItems().add(Game.Player.PLAYER2);
        this.playerChoice.setValue(gameSettings.getStartingPlayer());
    }

    /**
     * Initializing player1ColorChoice and player2ColorChoice ChoiceBoxes,
     * inserting options, and initializing value.
     */
    private void setColorChoice() {
        this.colorMap = Map.of("Black", Color.BLACK, "White", Color.WHITE, "Green", Color.GREEN,
                "Purple", Color.PURPLE, "Yellow", Color.YELLOW, "Blue", Color.BLUE, "Red", Color.RED,
                "Brown", Color.BROWN, "Orange", Color.ORANGE, "Pink", Color.PINK);
        Map<Color, String> inverseColorMap = Map.of( Color.BLACK, "Black", Color.WHITE, "White", Color.GREEN,
                "Green", Color.PURPLE,"Purple", Color.YELLOW, "Yellow", Color.BLUE, "Blue", Color.RED,
                "Red", Color.BROWN, "Brown", Color.ORANGE, "Orange", Color.PINK, "Pink");
        for (String s : colorMap.keySet()) {
            this.player1ColorChoice.getItems().add(s);
            this.player2ColorChoice.getItems().add(s);
        }
        this.player1ColorChoice.setValue(inverseColorMap.get(gameSettings.getPlayer1Color()));
        this.player2ColorChoice.setValue(inverseColorMap.get(gameSettings.getPlayer2Color()));
    }
}
