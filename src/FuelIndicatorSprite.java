
import javafx.scene.image.Image;

/**
 * Sprite class for the fuel indicator.
 *
 * Quinn Mayville, Michael Vue, Ethan Cassel-Mace, Hannah Barnstone
 */


public class FuelIndicatorSprite extends AbstractSprite {
    private static final double MIN_Y = 400;

    public FuelIndicatorSprite(double PositionX, double PositionY, Image image) {
        super(PositionX, PositionY, image);
    }

    private void boundY() {
        if (getPositionY() < MIN_Y) {
            setPositionY(MIN_Y);
        }
    }

    @Override
    public void setPositionY(double y) {
        super.setPositionY(y);
        boundY();
    }

    @Override
    public void updatePositionY(double time) {
        super.updatePositionY(time);
        boundY();
    }

}
