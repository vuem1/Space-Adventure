import javafx.scene.image.Image;

/**
 * Sprite class for bonus objects
 *
 * Quinn Mayville, Michael Vue, Ethan Cassel-Mace, Hannah Barnstone
 */

public class BonusSprite extends SpaceObjectSprite {
    private String bonusType;


    public BonusSprite(double positionX, double positionY, double velocityY,Image image, double maxY, String bonusType) {
        super(positionX,positionY,velocityY,image,maxY);
        this.bonusType = bonusType;
    }
    /*
     * returns bonus type, meaning which bunus you get.
     */
    public String getBonusType() {
        return bonusType;
    }
}
