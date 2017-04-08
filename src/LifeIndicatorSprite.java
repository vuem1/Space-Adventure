import javafx.scene.image.Image;

/**
 * Sprite class for the life indicators.
 *
 * Quinn Mayville, Michael Vue, Ethan Cassel-Mace, Hannah Barnstone
 */
public class LifeIndicatorSprite extends AbstractSprite {
    private static final double MIN_Y = 400;
    private Image imageOn;
    private Image imageOff;
    private boolean on;

    public LifeIndicatorSprite(double PositionX, double PositionY, Image image, Image offImage) {
        super(PositionX, PositionY, image);
        imageOn = image;
        imageOff = offImage;
        on = true;
    }

    public void turnOff(){
        on = false;
        setImage(imageOff);
    }

    public void turnOn(){
        on = true;
        setImage(imageOn);
    }

    public boolean getState(){
        return on;
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