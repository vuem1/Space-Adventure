import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.LinkedList;

/*
 * Quinn Mayville, Michael Vue, Ethan Cassel-Mace, Hannah Barnstone
 *
 * The window for the actual gameplay. Updates via access to the model and
 * has a scrolling background.
 */
public class gameView extends Application{

    static Stage gameStage = new Stage();
    private Scene gameScene;


    private gameController controller;
    private GameModel model;
    private GraphicsContext graphics;

    private int canvasWidth = 550;
    private int canvasHeight = 700;

    //Values that control speed of scrolling background
    private int backgroundWidth = canvasWidth;
    private int backgroundHeight = 0;
    private int backgroundXCoord = 0;
    private double backgroundYCoord = -5280;
    private double spaceBound = -800;
    private double earthMovement = 50;
    private double spaceMovement = 2;

    //Images used in background
    private Image background;
    private Image space;
    private Image fuelGauge;

    private String score;


    public gameView(gameController controller, GameModel model) {
        super();
        this.model = model;
        this.controller = controller;
    }

    /*
     * Creates JavaFX window and sets up canvas for drawing images.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        //Used to make the primaryStage publicly available to mainMenuView so that it can be called.
        gameView.gameStage = primaryStage;

        //Creates window and canvas for drawing images
        setup();

        gameStage.setResizable(false); //could change so that it is resizable
        gameStage.show();


}

    /*
     * Constructs the images to be used in the background.
     */
    private void createImages() {
        background = new Image("resources/gamebackground.png", backgroundWidth, backgroundHeight, true, true);
        fuelGauge = new Image("resources/bar.png", 300, 300, true, true);
        space = new Image("resources/starryPlanetBackGround.png", backgroundWidth, backgroundHeight, true, true);
        //Sets fill color and font/font size for score
        graphics.setFill(Color.YELLOW);
        graphics.setFont(Font.font("Calibri", FontWeight.NORMAL, 20));
    }
    /*
     * Preprocess the game window gui and constructs key handlers for left and right movement
     */

    private void setup() {
        gameStage.setTitle("Space Adventurer");
        Group root = new Group();
        gameScene = new Scene(root, canvasWidth, canvasHeight, Color.BLACK);
//        gameScene.getStylesheets().addAll(this.getClass().getResource("gameViewStyle.css").toExternalForm());
        gameStage.setScene(gameScene);
        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        root.getChildren().add(canvas);
        graphics = canvas.getGraphicsContext2D();
        createImages();

        gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.RIGHT) {
                    controller.rightArrowKey();
                } else if (event.getCode() == KeyCode.LEFT) {
                    controller.leftArrowKey();
                } else if (event.getCode() == KeyCode.P) {
                    controller.pKey();
                }
            }
        });

        gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.RIGHT) {
                controller.rightArrowKeyReleased();
                } else if (event.getCode() == KeyCode.LEFT) {
                controller.leftArrowKeyReleased();
                }
            }
        });
    }


    /*
     * Updates the view to match the model's current state
     */
    public void update(){
        graphics.clearRect(0, 0, canvasWidth, canvasHeight);
        drawBackgroundImages(background, fuelGauge);
        drawAsteroids();
        drawSpaceship();
        drawFuelIndicator();
        drawBonus();
        drawScore();
        drawLifeIndicators();
    }

    /*
     * Draws the bonuses on the canvas based on the model's state
     */
    private void drawBonus() {
        ArrayList<BonusSprite> bonusList = model.getBonusList();
        for (BonusSprite bonus : bonusList) {
            Image obstacleImage = bonus.getImage();
            double bonusPositionX = bonus.getPositionX();
            double bonusPositionY = bonus.getPositionY();
            graphics.drawImage(obstacleImage, bonusPositionX, bonusPositionY);
        }
    }

    /*
     * Draws the fuel indicator on the canvas based on the model's state
     */
    private void drawFuelIndicator() {
        FuelIndicatorSprite fuelIndicator = model.getFuelIndicator();
        Image fuelIndicatorImage = fuelIndicator.getImage();
        double fuelIndicatorPositionX = fuelIndicator.getPositionX();
        double fuelIndicatorPositionY = fuelIndicator.getPositionY();
        graphics.drawImage( fuelIndicatorImage, fuelIndicatorPositionX, fuelIndicatorPositionY);
    }

    /*
     * Draws the life indicators on the canvas based on the model's state
     */
    private void drawLifeIndicators() {
        ArrayList<LifeIndicatorSprite> indicators = model.getLifeIndicatorList();
        for (LifeIndicatorSprite indicator : indicators) {
            Image indicatorImage = indicator.getImage();
            double indicatorPositionX = indicator.getPositionX();
            double indicatorPositionY = indicator.getPositionY();
            graphics.drawImage(indicatorImage, indicatorPositionX, indicatorPositionY);
        }
    }


    /*
     * Draws the spaceship on the canvas based on the model's state
     */
    public void drawSpaceship() {
        ShipSprite spaceship = model.getSpaceship();
        Image spaceshipImage = spaceship.getImage();
        double spaceshipPositionX = spaceship.getPositionX();
        double spaceshipPositionY = spaceship.getPositionY();
        graphics.drawImage( spaceshipImage, spaceshipPositionX, spaceshipPositionY);
    }

    /*
     * Draws the asteroids on the canvas based on the model's state
     */
    public void drawAsteroids() {
        ArrayList<AsteroidSprite> obstacleList = model.getObstacleList();
        for (AsteroidSprite obstacle : obstacleList) {
            Image obstacleImage = obstacle.getImage();
            double obstaclePositionX = obstacle.getPositionX();
            double obstaclePositionY = obstacle.getPositionY();
            graphics.drawImage(obstacleImage, obstaclePositionX, obstaclePositionY);
        }
    }



    /*
     * Draws the background images.
     */
    private void drawBackgroundImages(Image background, Image fuelGauge) {

        if (backgroundYCoord < -5100){
            graphics.drawImage(background, backgroundXCoord, backgroundYCoord);
            backgroundYCoord = backgroundYCoord + earthMovement;
            //System.out.println(backgroundYCoord);
        }
        else if (backgroundYCoord < 0){
            graphics.drawImage(background, backgroundXCoord, backgroundYCoord);
            backgroundYCoord = backgroundYCoord + earthMovement/2;

        }
        else if (backgroundYCoord > -5101 && spaceBound < -200){
            graphics.drawImage(space, backgroundXCoord, spaceBound);
            spaceBound = spaceBound + spaceMovement;
            System.out.println(spaceBound);
        }
        else {
            graphics.drawImage(space, backgroundXCoord, spaceBound);
        }

        graphics.drawImage(fuelGauge,530, 400);
    }

    /*
     * Draws the score on the screen
     */
    private void drawScore(){
        score = "Score: " + model.getScore();
        graphics.fillText(score, 5, 20);
    }

    /*
     * Changes the scene once the game is over
     */
    public void gameOver(boolean sound, LinkedList<Integer> highScoreList) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(50, 0, 20, 0));

        VBox gameOverButtons = new VBox();

        Text text = new Text(score);
        text.setFont(Font.font("Herculanum",FontWeight.BOLD , 30));
        text.setFill(Color.BEIGE);


        gameOverButtons.setAlignment(Pos.CENTER);
        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.setMaxWidth(280);
        Button playAgainButton = new Button("Play Again");
        playAgainButton.setMaxWidth(280);


        mainMenuButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                mainMenu(sound, highScoreList);
            }
        });

        playAgainButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                newGame();
            }
        });
        gameOverButtons.setSpacing(30);
        gameOverButtons.setPadding(new Insets(0, 0, 400, 0));
        gameOverButtons.getChildren().addAll(text,playAgainButton, mainMenuButton);
        root.setTop(gameOverButtons);

        Scene gameOverScene = new Scene(root, 550, 700);
        gameOverScene.getStylesheets().addAll(this.getClass().getResource("stylesheet.css").toExternalForm());
        

        gameStage.setScene(gameOverScene);
    }

    /*
     * Starts a new game within the game window
     */
    private void newGame() {
        gameStage.close();
        gameController newGame = new gameController(new GameModel(model.getSound(),model.getHighScoreList()));
    }

    /*
     * Sets the current stage to the main menu
     */
    private void mainMenu(boolean sound, LinkedList<Integer> highScoreList) {
        mainMenuView menu = new mainMenuView();
        menu.start(menu.mainStage);
        menu.setSound(sound);
        menu.setHighScoreList(highScoreList);
        gameStage.close();
    }

}