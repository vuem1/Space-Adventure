import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
/**
 * Quinn Mayville, Michael Vue, Ethan Cassel-Mace, Hannah Barnstone
 *
 * Sprite class is used to animate view objects. Sprite objects can move around and possibly intersect with each other.
 * they have an image representation, which is repeatedly drawn to the screen.
 */
public class Sprite
{
    private Image image;
    private double positionX;
    private double positionY;    
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;
    private static final double MAX_X = 460;
    private static final double MIN_X = 10;


    public Sprite()
    {
        positionX = 0;
        positionY = 0;    
        velocityX = 0;
        velocityY = 0;
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
    public void setImage(String filename)
    {
        Image i = new Image(filename);
        setImage(i);
    }

    /*
     * set position of sprite
     */
    public void setPosition(double x, double y)
    {
        positionX = x;
        positionY = y;
        boundX();
    }

    /*
     * sets x and y velocity of sprite
     */
    public void setVelocity(double x, double y)
    {
        velocityX = x;
        velocityY = y;
    }

    /*
     * change current velocity of sprite
     */
    public void addVelocity(double x, double y)
    {
        velocityX += x;
        velocityY += y;
    }

    /*
     * updates position based on velocity and the time that has elapsed since last position change.
     */
    public void update(double time)
    {
        positionX += velocityX * time;
        positionY += velocityY * time;
        boundX();
    }

    /*
     * renders the sprite image to the screen
     */
    public void render(GraphicsContext gc)
    {
        gc.drawImage( image, positionX, positionY );
    }

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
    public boolean intersects(Sprite s)
    {
        return s.getBoundary().intersects( this.getBoundary() );
    }

    /*
     * represents position and velocity as string for debugging.
     */
    public String toString()
    {
        return " Position: [" + positionX + "," + positionY + "]" 
        + " Velocity: [" + velocityX + "," + velocityY + "]";
    }

    /*
     * method which keeps the sprite within the requested x bound.
     */
    private void boundX() {
        if (positionX > MAX_X) {
            positionX = MAX_X;
        }
        if (positionX < MIN_X) {
            positionX = MIN_X;
        }
    }
}