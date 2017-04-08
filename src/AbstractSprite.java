import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

/**
 * Quinn Mayville, Michael Vue, Ethan Cassel-Mace, Hannah Barnstone
 *
 * Sprite class is used to animate view objects. Sprite objects can move around and possibly intersect with each other.
 * they have an image representation, which is repeatedly drawn to the screen.
 */
public abstract class AbstractSprite
{
    private Image image;
    private double positionX;
    private double positionY;
    private double velocityX = 0;
    private double velocityY = 0;
    private double width;
    private double height;



    public AbstractSprite(double positionX, double positionY, Image image)
    {
        this.positionX = positionX;
        this.positionY = positionY;
        setImage(image);
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public Image getImage() {
        return image;
    }

    /*
     * sets display image from image object for sprite
     */
    public void setImage(Image i)
    {
        image = i;
        width = i.getWidth();
        height = i.getHeight();
    }

    /*
     * sets display image from image filename for sprite
     */
    public void setImageString(String filename)
    {
        Image i = new Image(filename);
        setImage(i);
    }

    /*
     * set X position of sprite
     */
    public void setPositionX(double x) {
        positionX = x;
    }

    /*
     * set Y position of sprite
     */
    public void setPositionY(double y) {
        positionY = y;
    }

    /*
     * updates X position based on the time that has elapsed since last position change.
     */
    public void updatePositionX(double time)
    {
        positionX += velocityX * time;
    }

    /*
     * updates Y position based the time that has elapsed since last position change.
     */
    public void updatePositionY(double time)
    {
        positionY += velocityY * time;
    }

    /*
     * set X velocity of sprite
     */
    public void setVelocityX(double x)
    {
        velocityX = x;
    }


    /*
     * set Y velocity of sprite
     */
    public void setVelocityY(double y)
    {
        velocityY = y;
    }

    /*
     * change current X velocity of sprite
     */
    public void addVelocityX(double x)
    {
        velocityX += x;
    }

    /*
     * decrease velocity if positive, increase if negative
     */
    public void slowDownX(double x)
    {
        if(velocityX > 0){
            velocityX -= x;
        }
        else if(velocityX < 0){
            velocityX += x;
        }
    }

    /*
     * change current Y velocity of sprite
     */
    public void addVelocityY(double y)
    {
        velocityY += y;
    }


    //TODO override this method to get more accurate collisions (triangles, circles, etc)
    /*
     * gets the rectangular boundary of the sprite. this is used to detect colisions. Can be changed to
     * polygon in the future.
     */
    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(positionX,positionY,width,height);
    }

    /*
     * Checks for intersection between two sprites
     */
    public boolean intersects(AbstractSprite s)
    {
        return s.getBoundary().intersects( this.getBoundary() );
    }
}