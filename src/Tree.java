import bagel.*;
import bagel.util.Rectangle;

/**
 * {@code Tree} Class inheritances the {@code statEntity} class
 */
public class Tree extends statEntity{
    // construct the wall with the given rendering image

    /**
     * This is the constructor of the {@code Tree} class
     * @param x The x position of the tree
     * @param y The y position of the tree
     * @param imageFile The image of the tree
     */
    public Tree(double x, double y, String imageFile) {
        super(x, y, imageFile);
    }

}
