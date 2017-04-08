import javafx.scene.image.Image;

/**
 * Sprite class for space objects (bonuses, obstacles)
 *
 * Created by quinnmayville on 3/13/17.
 */
public class SpaceObjectSprite extends AbstractSprite {
    //Don't think we need these max and min x but im leaving it for now
    private static final double MAX_X = 460;
    private static final double MIN_X = 0;
    private double maxY;

    private boolean isBelowScreen;

    public SpaceObjectSprite(double positionX, double positionY, double velocityY, Image image, double maxY) {

        super(positionX, positionY, image);
        setVelocityY(velocityY);
        this.maxY = maxY;
        this.isBelowScreen = getPositionY() > maxY;


    }

    @Override
    public void updatePositionY(double time) {
        super.updatePositionY(time);
        if(getPositionY() > maxY){
            isBelowScreen = true;
        }
    }

    public boolean isBelowScreen() {
        return isBelowScreen;
    }


}
