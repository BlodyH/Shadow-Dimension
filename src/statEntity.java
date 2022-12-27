import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * The abstract class {@statEntity} is a parent class for stationary objects {@code Wall}, {@code Tree} and {@code SinkHole}
 * that are going to appear in the scene
 */
public abstract class statEntity {
    private double x;
    private double y;
    private Image showImage;

    /**
     * This is the constructor to construct a stationary entity with the given position
     * @param x This is the x position of the entity
     * @param y This is the y position of the entity
     * @param imageFile This is the image to be rendered for this entity
     */
    public statEntity(double x, double y, String imageFile){
        this.x = x;
        this.y = y;
        this.showImage = new Image(imageFile);
    }

    /**
     * Get the x position of the entity
     * @return {@code x} position
     */
    public double getX(){
        return this.x;
    }

    /**
     * Get ther y position of the entity
     * @return {@code y} position
     */
    public double getY(){
        return this.y;
    }

    /**
     * Render the entity in the scene
     */
    public void update(){showImage.drawFromTopLeft(getX(), getY());}

    /**
     * This returns the Bounding Box of the entity which is used for later detection of colliding.
     * @return The rectangle representing the bounding box of the entity
     */
    public Rectangle getBoundingBox(){
        return showImage.getBoundingBoxAt(new Point(this.getX() + showImage.getWidth()/2, this.getY() + showImage.getHeight()/2));
    };

}
