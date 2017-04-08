
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;



/**
 * Model for the space game stores the game data and updates
 * the view every time it changes state.
 *
 * Created by Michael Vue, Ethan Cassel-Mace, Hannah Barnstone, && Quinn Mayville
 */
public class GameModel {

    private ShipSprite spaceship;
    private FuelIndicatorSprite fuelIndicator;
    private ArrayList<AsteroidSprite> obstacleList = new ArrayList<>();
    private ArrayList<BonusSprite> bonusList = new ArrayList<>();
    private ArrayList<LifeIndicatorSprite> lifeIndicatorList = new ArrayList<>();
    private double time = 0;
    private int score = 0;
    private double immuneTime = -5;
    private double imageSwitchTime = -5;
    //These can be changed to manipulate when the first generation of each occurs
    //The lower the number, the earlier it happens
    private double lastObstacleGenerationTime = -5;
    private double lastFuelGenerationTime = -1;
    private double lastBonusGenerationTime = -1;

    private gameView view;
    private Random randomNumberGenerator = new Random();
    private boolean acceleratingPositive = false;
    private boolean acceleratingNegative = false;

    //Images used for sprites
    private Image shipImage1;
    private Image shipImage2;
    private Image immuneImage;
    private Image asteroidImage;
    private Image lifeImage;
    private Image lifeUsedImage;


    private int imageNumber = 1;
    private int asteroidScore = 1000;
    private int numAsteroids = 1;
    private boolean paused = false;
    private AnimationTimer gameTimer;
    private LongProperty lastUpdateTime = new SimpleLongProperty();
    private boolean sound;
    private int velocityY = 120;
    private int velocityScore = 2000;

    private LinkedList<Integer> highScoreList;




    public GameModel(boolean sound, LinkedList<Integer> highScoreList){
        this.sound = sound;
        shipImage1 = new Image("resources/toonship_1.png", 60, 80, true, true);
        shipImage2 = new Image("resources/toonship_2.png", 60, 80, true, true);
        immuneImage =new WritableImage(60,80);
        asteroidImage = new Image("resources/asteroid.gif", 70, 70, true, true);
        lifeImage = new Image("resources/heart.png", 30, 30, true, true);
        lifeUsedImage = new Image("resources/heartUsed.png", 30, 30, true, true);

        for(int i = 0; i < 4; i++){
            double positionX = 300 + i*50;
            LifeIndicatorSprite indicator = new LifeIndicatorSprite(positionX, 10, lifeImage, lifeUsedImage);
            lifeIndicatorList.add(indicator);
        }

        this.highScoreList = highScoreList;

        constructTimer();
    }

    public double getScore(){return score;}

    public ShipSprite getSpaceship() {
        return spaceship;
    }

    public FuelIndicatorSprite getFuelIndicator() { return fuelIndicator; }

    public ArrayList<AsteroidSprite> getObstacleList() {
        return obstacleList;
    }

    public ArrayList<LifeIndicatorSprite> getLifeIndicatorList() {
        return lifeIndicatorList;
    }


    public ArrayList<BonusSprite> getBonusList() { return bonusList; }

    /*
     * creates the necessary objects
     */
    public void initialize() {
        this.spaceship = new ShipSprite(250, 600, shipImage1, 100, 4);
        Image fuelIndicatorImage = new Image("resources/arrow.png",23,20,true,true);
        this.fuelIndicator = new FuelIndicatorSprite(516,393,fuelIndicatorImage);

    }

    public void setView(gameView view) {
        this.view = view;
    }


    /*
     * Turns on and off the life indicators
     */
    private void setLifeIndicators(int lives){
        for(int i = lifeIndicatorList.size()-1; i >= 0; i --){
            if(i >= lifeIndicatorList.size() - lives) {
                lifeIndicatorList.get(i).turnOn();
            }
            else{
                lifeIndicatorList.get(i).turnOff();
            }
        }
    }

    /*
     * Constructs the timer that manages the game. Inside this method, everything that needs to be repeated or checked
     * for at 60 fps takes place. 
     */
    public void constructTimer() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long currentTime) {
                if (lastUpdateTime.get() > 0) {

                    final double elapsedTime = (currentTime - lastUpdateTime.get()) / 1_000_000_000.0;
                    //increment time
                    time += elapsedTime;

                    //Checks if spaceship should still be immune
                    if (spaceship.isImmune() && immuneTime + 1.5 < time) {
                        removeImmunity();
                    }

                    //Flicker flames or flicker immunity
                    if (!spaceship.isImmune()) {

                        flickerImage(shipImage1, shipImage2, .15);
                    } else {
                        flickerImage(immuneImage, shipImage2, .2);
                    }

                    //accelerate or decelerate ship
                    if (acceleratingPositive) {
                        moveShipRight();
                    }

                    if (acceleratingNegative) {
                        moveShipLeft();
                    }

                    if (!acceleratingNegative && !acceleratingNegative){
                        slowDownShip();
                    }

                    //Move spaceship and fuel indicator
                    spaceship.updatePositionX(elapsedTime);
                    fuelIndicator.updatePositionY(elapsedTime);

                    updateFuel();
                    checkFuel();

                    //Move existing obstacles
                    for (AsteroidSprite obstacle : obstacleList) {
                        obstacle.updatePositionY(elapsedTime);
                    }


                    //Move existing bonuses
                    for (BonusSprite bonus : bonusList) {
                        bonus.updatePositionY(elapsedTime);
                    }

                    //Check for collisions with obstacles and remove obstacles below screen
                    checkObstacleCollisions();

                    //Check for collisions with bonuses and remove bonuses below screen
                    checkBonusCollisions();

                    //Increase number of asteroids generated
                    if (score > asteroidScore && numAsteroids < 6){
                        numAsteroids += 1;
                        asteroidScore += 3000;
                    }

                    //Increase velocity
                    if (score > velocityScore) {
                        velocityY += 2;
                        velocityScore += 250;
                    }


                    //Generate asteroids every 2 time-units
                    if (lastObstacleGenerationTime + 2 < time) {
                        generateObstacles();
                        lastObstacleGenerationTime = time;
                    }

                    //Generate fuel every 7.4 time-units
                    if (lastFuelGenerationTime + 7.4 < time) {
                        generateFuel();
                        lastFuelGenerationTime = time;
                    }

                    //Generate bonus every 24.7 time-units
                    if (lastBonusGenerationTime + 24.7 < time) {
                        generateBonus();
                        lastBonusGenerationTime = time;
                    }


                    //increment score and update view
                    score = score + 1;
                    view.update();
                }
                lastUpdateTime.set(currentTime);
            }
        };
    }

    /*
     * Starts the game timer.
     */
    public void startGame() {
        gameTimer.start();
    }

    /*
     * Flickers between two given images at a given rate.
     */
    private void flickerImage(Image image1, Image image2, double flickerTime) {
        if (imageSwitchTime + flickerTime < time) {
            if (imageNumber == 1) {
                spaceship.setImage(image2);
                imageNumber = 2;
            } else {
                spaceship.setImage(image1);
                imageNumber = 1;
            }
            imageSwitchTime = time;
        }
    }


    /*
     * Generates bonuses.
     */
    private void generateBonus() {
        //TODO make more bonuses
        //Temporary implementation only generates life bonuses. Was thinking we could randomize which bonus it generates
        int positionX = randomNumberGenerator.nextInt(460);
        BonusSprite bonus = new BonusSprite(positionX, -100, velocityY, lifeImage, 700, "life");
        if (noIntersect(bonus)) {
            bonusList.add(bonus);
        } else {
            generateBonus();
        }
    }


    /*
     * Generates fuel
     */
    private void generateFuel(){
        int positionX = randomNumberGenerator.nextInt(460);
        Image fuelImage = new Image("resources/fuel.png", 30, 50, true, true);
        BonusSprite fuelBonus = new BonusSprite(positionX, -100, velocityY, fuelImage, 700, "fuel");
        if (noIntersect(fuelBonus)) {
            bonusList.add(fuelBonus);
        } else {
            generateFuel();
        }
    }

    /*
     * Returns true if the given bonus does not intersect any asteroids or bonuses.
     */
    //TODO check if this method works. I saw a bonus intersect with an asteroid, but when I purposely caused an intersection to test this then the method prevented it
    private boolean noIntersect(BonusSprite bonus) {
        for (AsteroidSprite obstacle : obstacleList) {
            if (bonus.intersects(obstacle)) {
                return false;
            }
        }
        for (BonusSprite otherBonus : bonusList) {
            if (bonus.intersects(otherBonus)) {
                return false;
            }
        }
        return true;
    }

    /*
     * Checks whether bonuses are below screen or if spaceship has collided with them
     */
    private void checkBonusCollisions() {
        Iterator<BonusSprite> bonusIterator = bonusList.iterator();
        while (bonusIterator.hasNext()) {
            BonusSprite bonus = bonusIterator.next();
            if (bonus.isBelowScreen()) {
                bonusIterator.remove();
            }
            if (spaceship.intersects(bonus)) {
                giveBonus(bonus.getBonusType());
                setLifeIndicators(spaceship.getLives());

                bonusIterator.remove();
            }
        }
    }

    /*
     * Performs the specified bonus action
     */
    private void giveBonus(String bonusType) {
        if (bonusType.equals("life")) {
            spaceship.changeLives(1);
            if (sound) {
                String songFile = new File("src/resources/life.mp3").toURI().toString();
                Media media = new Media(songFile);
                MediaPlayer mp = new MediaPlayer(media);
                mp.play();
            }
        }
        if (bonusType.equals("fuel")) {
            spaceship.setFuel(spaceship.getFuel() + 20);
            if (sound) {
                String songFile = new File("src/resources/fuel.mp3").toURI().toString();
                Media media = new Media(songFile);
                MediaPlayer mp = new MediaPlayer(media);
                mp.play();
            }
        }
    }

    /*
     * Remove obstacles that are off screen and check for collisions
     */
    private void checkObstacleCollisions() {
        Iterator<AsteroidSprite> obstacleIterator = obstacleList.iterator();
        while (obstacleIterator.hasNext()) {
            AsteroidSprite obstacle = obstacleIterator.next();
            if (obstacle.isBelowScreen()) {
                obstacleIterator.remove();
            }
            if (spaceship.intersects(obstacle) && !spaceship.isImmune()) {
                collision(gameTimer);
                setLifeIndicators(spaceship.getLives());
            }
        }
    }
    /*
     * Handles collisions with obstacles
     */
    private void collision(AnimationTimer gameTimer) {
        spaceship.changeLives(-1);
        if (spaceship.getLives() <= 0) {
            gameOver(gameTimer);
        } else {
            spaceship.setImmune(true);
            spaceship.setImage(immuneImage);
            imageNumber = 1;
            immuneTime = time;
            imageSwitchTime = time;
            if (sound) {
                String songFile = new File("src/resources/collision.mp3").toURI().toString();
                Media media = new Media(songFile);
                MediaPlayer mp = new MediaPlayer(media);
                mp.play();
            }
        }
    }

    /*
     * Updates the fuel and fuel indicator
     */
    private void updateFuel() {
        spaceship.updateFuel(-.04);
        double fuel = spaceship.getFuel();
        fuelIndicator.setPositionY(693 - (3 * fuel));
    }

    /*
     * Checks whether the fuel is empty
     */
    private void checkFuel() {
        if (spaceship.getFuel() <= 0) {
            gameOver(gameTimer);
        }
    }

    /*
     * Makes the ship not immune
     */
    private void removeImmunity() {
        spaceship.setImage(shipImage1);
        spaceship.setImmune(false);
    }

    /*
     * Ends the game.
     */
    private void gameOver(AnimationTimer gameTimer) {
        Image explosion = new Image("resources/explosion.png", 60, 80, true, true);
        if (sound) {
            String songFile = new File("src/resources/gameover.mp3").toURI().toString();
            Media media = new Media(songFile);
            MediaPlayer mp = new MediaPlayer(media);
            mp.play();
        }
        spaceship.setImage(explosion);
        gameTimer.stop();
        checkScore();

        //Brief pause before transition to new scene
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(2500),
                ae -> view.gameOver(sound, highScoreList)));
        timeline.play();
    }

    /*
     * Checks to see whether score is a new high score.
     */
    private void checkScore() {
        for (int i = 0; i < 3; i++) {
            if ((score + 1) > highScoreList.get(i)) {
                highScoreList.add(i, (score + 1));
                highScoreList.remove(3);
                return;
            }
        }
    }

    /*
     * Pauses and unpauses the animation timer.
     */
    public void pause() {
        if (paused) {
            lastUpdateTime = new SimpleLongProperty();
            gameTimer.start();
            paused = false;
        } else {
            gameTimer.stop();
            paused = true;
        }
    }

    /*
     * Generates obstacle sprites that are added to obstacleList
     */
    private void generateObstacles() {
        int xIncrement = 460/numAsteroids;
        int positionY = -100;
        int counter = 0;

        for (int i =0; i< numAsteroids; i++) {
            int newPositionX = (randomNumberGenerator.nextInt(xIncrement)+ (counter*xIncrement));
            positionY = (positionY - 150);
            AsteroidSprite asteroid = new AsteroidSprite(newPositionX , positionY, velocityY, asteroidImage, 700);
            obstacleList.add(asteroid);
            counter++;
        }
    }

    public boolean getSound() {
        return sound;
    }

    public LinkedList<Integer> getHighScoreList() { return  highScoreList;}


    public void startAccelerationPositive(){
        acceleratingPositive = true;
    }

    public void startAccelerationNegative(){
        acceleratingNegative = true;
    }

    public void stopAccelerationPositive(){
        acceleratingPositive = false;
    }

    public void stopAccelerationNegative(){
        acceleratingNegative = false;
    }

    public void moveShipRight() {
        spaceship.addVelocityX(10);
    }

    public void moveShipLeft() {
        spaceship.addVelocityX(-10);
    }

    public void slowDownShip() {
        spaceship.slowDownX(2.5);
    }

    public void stopShipMovement() {
        spaceship.setVelocityX(0);
    }

}