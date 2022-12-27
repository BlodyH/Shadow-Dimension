/**
 * The interface {@code Movable} represents that an object could be moved around, controlled by the gamer
 */
public interface Movable {
    /**
     * The method that is to be implemented and override in any classes that implements {@code Movable}
     * @param moveX The new x position of the object
     * @param moveY The new y position of the object
     */
    void movement(double moveX, double moveY);
}
