import bagel.*;
import bagel.util.Rectangle;

/**
 * {@code Wall} Class inheritances the {@code statEntity} class
 */
public class Wall extends statEntity{
    // construct the wall with the given rendering image

    /**
     * This is the constructor of the wall
     * @param x The x position of the wall
     * @param y The y position of the wall
     * @param imageFile The image of the wall
     */
    public Wall(double x, double y, String imageFile) {
        super(x, y, imageFile);
    }

}
