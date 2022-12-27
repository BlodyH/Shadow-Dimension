import bagel.*;
import bagel.util.Rectangle;

/**
 * {@code SinkHole} class inheritances the {@code statEntity} class
 */
public class SinkHole extends statEntity{
    private final static int DAMAGE = 30;
    //Controls whether a sinkhole has been stepped on and thus eliminated from the scene
    private boolean exist = true;
    // construct the sinkhole using the given rendering image

    /**
     * This is the constructor of the SinkHole
     * @param x The x position of the sinkhole
     * @param y The y position of the sinkhole
     * @param imageFile The image of the sinkhole
     */
    public SinkHole(double x, double y, String imageFile) {
        super(x, y, imageFile);
    }

    /**
     * Get the damage of the hole
     * @return the damage of a hole
     */
    public int getDamage(){return DAMAGE;}

    /**
     * Get whether the hole still exists
     * @return true if the hole has not been stepped on and still exists
     */
    public boolean isExist(){return exist;}

    /**
     * If a hole is stepped on, it no longer exists, make it eliminate
     */
    public void eliminate(){exist = false;}

}