import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.LinkedList;

/*
 * Quinn Mayville, Michael Vue, Ethan Cassel-Mace, Hannah Barnstone
 *
 * The window for the main menu.
 */
public class mainMenuView extends Application {
    public static final double MAX_BUTTON_WIDTH = 180;
    private static final double SCENE_WIDTH = 550;
    private static final double SCENE_HEIGHT = 700;
    static Stage mainStage;
    private boolean sound = true;
    private Text soundText = new Text("Sound Currently: ON");
    private Text highScoreText = new Text();
    private LinkedList<Integer> highScoreList = new LinkedList<Integer>();

    //scene variables
    private Scene menuScene;
    private Scene instructionsScene;
    private Scene settingsScene;
    private Scene toggleSoundScene;


    /*
     * Creates JavaFX window and all respective button nodes for the home screen
     */
    @Override
    public void start(Stage primaryStage) {

        mainStage = primaryStage;

        //Initializes the high score list
        for (int i= 0; i < 3; i++) {
            highScoreList.add(0);
        }

        // Sets scene variables to the scene returned by their respective builder methods
        menuScene = buildMenuScene(mainStage);
        instructionsScene = buildInstructionsScene(mainStage);
        settingsScene = buildSettingsScene(mainStage);
        toggleSoundScene = buildToggleSoundScene(mainStage);

        // sets up and starts the stage
        primaryStage.setTitle("Space Adventurer");
        primaryStage.setScene(menuScene);
        primaryStage.setResizable(false); //could change so that it is resizable
        primaryStage.show();


    }

    /*
     * A helper function for building JavaFX scenes.
     * @Parameters(String, FontWeight, int)
     */
    private Node getTitleNode(String textString, FontWeight fontWeight, int fontSize) {

        Text text = new Text(textString);
        text.setFont(Font.font("Herculanum", fontWeight, fontSize));
        text.setFill(Color.BEIGE);

        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER);
        flowPane.getChildren().add(text);

        return flowPane;
    }

    /*
     * Builds the main menu scene with buttons
     */
    private Scene buildMenuScene(Stage mainStage) {

        BorderPane root = new BorderPane();

        root.setPadding(new Insets(50, 0, 20, 0));

        Node titlePane = getTitleNode("SPACE ADVENTURE", FontWeight.BOLD, 50);
        Node buttonPane = getMenuButtonsNode(mainStage);

        root.setTop(titlePane);
        root.setCenter(buttonPane);

        Scene menuScene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        //This is where you can add a custom background (look at stylesheet)
        menuScene.getStylesheets().addAll(this.getClass().getResource("stylesheet.css").toExternalForm());

        return menuScene;
    }

    /*
     * Obtains all children nodes (buttons) for the main menu scene
     */
    private Node getMenuButtonsNode(Stage mainStage) {

        VBox buttons = new VBox();
        buttons.setAlignment(Pos.CENTER);

        // Create new buttons
        Button startGame = new Button("Start");
        startGame.setMaxWidth(MAX_BUTTON_WIDTH);

        Button instructions = new Button("Instructions");
        instructions.setMaxWidth(MAX_BUTTON_WIDTH);

        Button settings = new Button("Settings");
        settings.setMaxWidth(MAX_BUTTON_WIDTH);

//        Event handlers for on button mouse clicks
        startGame.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {

                try {
                    new gameController(new GameModel(sound, highScoreList));
                    mainStage.close();

                } catch (Exception error) {

                    error.printStackTrace();

                }
            }
        });

        instructions.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                mainStage.setScene(instructionsScene);
            }

        });

        settings.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                mainStage.setScene(settingsScene);
            }

        });

//        Format the buttons to show up correctly in the main window
        buttons.setSpacing(30);
        buttons.setPadding(new Insets(0, 0, 400, 0));
        buttons.getChildren().addAll(startGame, instructions, settings);

        return buttons;
    }

    /*
     * Scene builder for the instructions page
     */
    private Scene buildInstructionsScene(Stage mainStage) {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(50, 0, 20, 0));

        Node titlePane = getTitleNode("INSTRUCTIONS", FontWeight.BOLD, 50);
        Node buttonPane = getInstructionsButtonsNode(mainStage);

        root.setTop(titlePane);
        root.setBottom(buttonPane);

        Scene InstructionsScene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        //This is where you can add a custom background (look at stylesheet)
        InstructionsScene.getStylesheets().addAll(this.getClass().getResource("stylesheet.css").toExternalForm());

        return InstructionsScene;

    }

    /*
     * Fill out the instructions scene with instructions related information
     * Creates a back button for going to the previous page.
     */
    private Node getInstructionsButtonsNode(Stage mainStage) {

        VBox buttons = new VBox();
        buttons.setAlignment(Pos.BOTTOM_CENTER);

        // Create new buttons
        Button backButton = new Button("Back");
        backButton.setMaxWidth(MAX_BUTTON_WIDTH);

        StackPane textTransparency = new StackPane();

//        Black transparency for easier reading on instructions page
        Rectangle blackTransparency = new Rectangle();
        blackTransparency.setHeight(360);
        blackTransparency.setWidth(520);
        blackTransparency.setFill(Color.web("black", 0.75));

//        Text for instructions page
        Text instructions = new Text();
        instructions.setCache(true);
        instructions.setX(50);
        instructions.setY(50);
        instructions.setFill(Color.ORANGERED);
        instructions.setFont(Font.font("copperplate", FontWeight.THIN, 15));
        instructions.setWrappingWidth(500);
        instructions.setTextAlignment(TextAlignment.JUSTIFY);
        instructions.setText("• The goal of the game is to fly your rocket ship as high into the sky as possible.\n" +
                "\n" +
                "• As your ship flies you must avoid obstacles through moving side to side by pressing the left and " +
                "right arrow keys on your keyboard.\n" +
                "\n" +
                "• If you fail to dodge an obstacle you will lose a life.\n" +
                "\n" +
                "• By steering your ship through hearts dispersed throughout your journey you can gain extra lives. \n" +
                "\n" +
                "• Your rocket ship has a limited quantity of fuel that diminishes over time. You can gain extra fuel by " +
                "steering your ship through fuel tanks dispersed throughout your journey. \n" +
                "\n" +
                "• If you either run out of lives or fuel, you lose the game. The distance traveled prior to running out" +
                " of fuel/lives is your score for the game.\n");

        textTransparency.getChildren().add(blackTransparency);
        textTransparency.getChildren().add(instructions);

//        Event handler for button on instructions page
        backButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                mainStage.setScene(menuScene);
            }

        });

        buttons.setSpacing(10);
        buttons.setPadding(new Insets(50, 0, 600, 0));
        buttons.getChildren().addAll(textTransparency, backButton);

        return buttons;
    }

    /*
     * Scene builder for settings menu
     */
    private Scene buildSettingsScene(Stage mainStage) {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(50, 0, 20, 0));

        Node titlePane = getTitleNode("SETTINGS", FontWeight.BOLD, 50);
        Node buttonPane = getSettingsButtonsNode(mainStage);

        root.setTop(titlePane);
        root.setCenter(buttonPane);

        Scene settingsScene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        //This is where you can add a custom background (look at stylesheet)
        settingsScene.getStylesheets().addAll(this.getClass().getResource("stylesheet.css").toExternalForm());

        return settingsScene;
    }

    /*
     * Creates option buttons for settings menu
     */
    private Node getSettingsButtonsNode(Stage mainStage) {

        VBox homeButtons = new VBox();
        homeButtons.setAlignment(Pos.CENTER);

        // Create new buttons
        Button toggleSound = new Button("Toggle Sound");
        toggleSound.setMaxWidth(280);

        Button back = new Button("Back");
        back.setMaxWidth(MAX_BUTTON_WIDTH);

        StackPane textTransparency = new StackPane();

        Rectangle blackTransparency = new Rectangle();
        blackTransparency.setHeight(160);
        blackTransparency.setWidth(500);
        blackTransparency.setFill(Color.web("black", 0.75));


        highScoreText.setCache(true);
        highScoreText.setX(50);
        highScoreText.setY(50);
        highScoreText.setFill(Color.ORANGERED);
        highScoreText.setFont(Font.font("copperplate", FontWeight.EXTRA_BOLD, 25));
        highScoreText.setWrappingWidth(500);
        highScoreText.setTextAlignment(TextAlignment.JUSTIFY);
        highScoreText.setText("\n• High Score: " + highScoreList.get(0) + "\n" +
                "\n" +
                "• High Score 2: " + highScoreList.get(1) + "\n" +
                "\n" +
                "• High Score 3: " + highScoreList.get(2) + "\n");

        textTransparency.getChildren().add(blackTransparency);
        textTransparency.getChildren().add(highScoreText);



        toggleSound.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                mainStage.setScene(toggleSoundScene);
            }
        });

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                mainStage.setScene(menuScene);
            }
        });

        homeButtons.setSpacing(30);
        homeButtons.setPadding(new Insets(0, 0, 400, 0));
        homeButtons.getChildren().addAll(textTransparency,toggleSound, back);

        return homeButtons;
    }
    /*
         * Scene builder for turning sound on or off
         */
    private Scene buildToggleSoundScene(Stage mainStage) {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(50, 0, 20, 0));

        Node titlePane = getTitleNode("Toggle Sound", FontWeight.BOLD, 50);
        Node buttonPane = getToggleSoundButtonsNode(mainStage);

        root.setTop(titlePane);
        root.setCenter(buttonPane);

        Scene toggleSoundScene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

        //This is where you can add a custom background (look at stylesheet)
        toggleSoundScene.getStylesheets().addAll(this.getClass().getResource("stylesheet.css").toExternalForm());

        return toggleSoundScene;
    }

    /*
     * Creates button to turn sound off or on
     */
    private Node getToggleSoundButtonsNode(Stage mainStage) {
        VBox homeButtons = new VBox();
        homeButtons.setAlignment(Pos.CENTER);


        // Create new buttons
        Button soundOn = new Button("On");
        Button soundOff = new Button("Off");
        soundOn.setMaxWidth(200);
        soundOff.setMaxWidth(200);

        soundOn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!sound) {
                    sound = true;
                    soundText.setText("Sound Currently: ON");
                }
            }

        });

        soundOff.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (sound) {
                    sound = false;
                    soundText.setText("Sound Currently: OFF");
                }
            }

        });

        soundText.setFont(Font.font("Herculanum", FontWeight.NORMAL, 20));
        soundText.setFill(Color.BEIGE);


        Button back = new Button("Back");
        back.setMaxWidth(MAX_BUTTON_WIDTH);

        back.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                mainStage.setScene(menuScene);
            }

        });

        homeButtons.setSpacing(30);
        homeButtons.setPadding(new Insets(0, 0, 400, 0));
        homeButtons.getChildren().addAll(soundText, soundOn, soundOff, back);

        return homeButtons;
    }

    /*
     * Sets the sound.
     */
    public void setSound(boolean sound) {
        this.sound = sound;
        if (!sound) {
            soundText.setText("Sound Currently: OFF");
        } else {
            soundText.setText("Sound Currently: ON");
        }
    }

    /*
     * Sets the high score list to the given list
     */
    public void setHighScoreList(LinkedList<Integer> newHighScoreList) {
        highScoreList = newHighScoreList;
        highScoreText.setText("\n• High Score: " + highScoreList.get(0) + "\n" +
                "\n" +
                "• High Score 2: " + highScoreList.get(1) + "\n" +
                "\n" +
                "• High Score 3: " + highScoreList.get(2) + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}