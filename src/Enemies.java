import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/**
 * The class {@code Enemies} is the abstract parent class of {@code Demon} ad {@code Navec}
 * It provides the basic methods that an enemy should have
 */
public abstract class Enemies implements Movable{
    protected double x;
    protected double y;
    private static int TOP_LEFT_X;
    private static int TOP_LEFT_Y;
    private static int BOTTOM_RIGHT_X;
    private static int BOTTOM_RIGHT_Y;
    protected final double MIN_SPEED = 0.2;
    protected final double MAX_SPEED = 0.7;
    protected final double RAND = new Random().nextDouble();
    protected double step = MIN_SPEED + RAND*(MAX_SPEED - MIN_SPEED);
    protected double initialStep = step;

    protected final static DrawOptions COLOUR = new DrawOptions();
    protected final static Colour GREEN = new Colour(0, 0.8, 0.2);
    protected final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    protected final static Colour RED = new Colour(1, 0, 0);
    protected final static double HIGH_HEALTH = 65;
    protected final static double LOW_HEALTH = 35;
    protected int MAX_HEALTH;

    protected final static int INVINCIBLE_DURATION = 3000;
    protected final static double FPS = 60;

    protected final int UP = 0;
    protected final int DOWN = 1;
    protected final int LEFT = 2;
    protected final int RIGHT = 3;
    protected final static int DIRECTIONS = 4;
    protected int moveDirection = new Random().nextInt(DIRECTIONS);

    protected Image showImage;
    protected int health;
    protected boolean isInvincible = false;
    protected double invincibleCount = 0;
    protected final static int FONT_SIZE = 15;
    protected final static String FONT_FILE = "res/frostbite.ttf";
    protected final static Font FONT = new Font(FONT_FILE, FONT_SIZE);
    protected boolean isAggressive;

    /**
     * This is the contructor of the class {@code Enemies}
     * @param x The x position of the enemy
     * @param y The y position of the enemy
     */
    public Enemies(double x, double y){
        this.x = x;
        this.y = y;
    }

    /**
     * This method sets up the boundary of the game such that the enemies should never get out of
     * @param TOP_LEFT_X the top left x position of the boundary
     * @param TOP_LEFT_Y the top left y position of the boundary
     * @param BOTTOM_RIGHT_X the bottom right x position of the boundary
     * @param BOTTOM_RIGHT_Y the bottom right y position of the boundary
     */
    public void setBoundary(int TOP_LEFT_X, int TOP_LEFT_Y, int BOTTOM_RIGHT_X, int BOTTOM_RIGHT_Y){
        // set the boundary for the enemies
        this.TOP_LEFT_X = TOP_LEFT_X;
        this.TOP_LEFT_Y = TOP_LEFT_Y;
        this.BOTTOM_RIGHT_X = BOTTOM_RIGHT_X;
        this.BOTTOM_RIGHT_Y = BOTTOM_RIGHT_Y;
    }

    /**
     * Set the x position to the given value
     * @param x the new x position
     */
    public void setX(double x){ this.x = x; }
    /**
     * Set the y position to the given value
     * @param y the new y position
     */
    public void setY(double y){ this.y = y; }

    /**
     * Get the x position of the enemy
     * @return the {@code x} position
     */
    public double getX(){ return this.x; }
    /**
     * Get the y position of the enemy
     * @return the {@code y} position
     */
    public double getY(){ return this.y; }

    /**
     * Get the invincible state of the enemy
     * @return true if the enemy is under invincible state
     */
    public boolean getInvincible(){ return isInvincible; }

    /**
     * Set up the invincible state of the enemy
     * @param isInvincible the new invincible state, true or false
     */
    public void setInvincible(boolean isInvincible){ this.isInvincible = isInvincible; }

    /**
     * Get the step size of the enemy
     * @return the {@code step} of the moving speed
     */
    public double getStep(){ return this.step; }

    /**
     * Set up the {@code step} of the moving speed to the given value
     * @param step The new moving {@code step}
     */
    public void setStep(double step){ this.step = step; }

    /**
     * Set up the {@code direction} of the enemy to the given direction
     * @param moveDirection The new moving {@code direction}
     */
    public void setDirection(int moveDirection){ this.moveDirection = moveDirection; }

    /**
     * Get the maximum health of the enemy
     * @return The {@MAX_HEALTH} of the enemy
     */
    public int getMaxHealth() {
        return MAX_HEALTH;
    }

    /**
     * Get the current health of the enemy
     * @return {@code health} of the enemy
     */
    public int getHealth() {
        return health;
    }

    /**
     * Get the un-scaled moving speed of the enemy
     * @return the initial speed of the enemy
     */
    public double getInitialStep(){ return initialStep; }

    /**
     * Apply the damage to the enemy, turn the enemy into Invincible state
     * @param damage how muc {@code damage} the enemy receives
     */
    public void receiveDamage(double damage){
        // receive the damage and inflict on health, turn into invincible state
        this.health -= damage;
        setInvincible(true);
    }

    /**
     * Render the current {@code health} of the enemy on the top
     */
    protected void drawHealth(){
        // calculate the health as a percentage
        double percentageHealth = ((double) health/MAX_HEALTH) * 100;
        // draw different color according to health
        if (percentageHealth <= LOW_HEALTH){
            COLOUR.setBlendColour(RED);
        }
        else if (percentageHealth <= HIGH_HEALTH){
            COLOUR.setBlendColour(ORANGE);
        }
        else {
            COLOUR.setBlendColour(GREEN);
        }
        FONT.drawString(Math.round(percentageHealth) + "%", getX(), getY(), COLOUR);
    }

    /**
     * Get whether the enemy is dead
     * @return true if the enemy is dead
     */
    public boolean isDead(){
        return (health <= 0);
    }

    /**
     * Get the bounding box of the enemy for later check of collision
     * @return the bounding box as a {@code Rectangle}
     */
    public Rectangle getBoundingBox(){
        // produce the bounding box of the enemy
        return this.showImage.getBoundingBoxAt(new Point(this.getX() + this.showImage.getWidth()/2, this.getY() + this.showImage.getHeight()/2));
    }

    /**
     * Move the {@code Enemies} around
     * @param moveX How much change should be applied to the x position
     * @param moveY How much change should be applied to the y position
     */
    @Override
    public void movement(double moveX, double moveY) {
        setX(getX() + moveX);
        setY(getY() + moveY);

    }

    /**
     * Change the direction to the opposite if the monster collides with a {@code statEntity}
     */
    protected void Rebound(){
        // change to the opposite direction of motion
        if (moveDirection == UP){
            setDirection(DOWN);
        }
        else if (moveDirection == DOWN){
            setDirection(UP);
        }
        else if (moveDirection == LEFT){
            setDirection(RIGHT);
        }
        else if (moveDirection == RIGHT){
            setDirection(LEFT);
        }
    }

    /**
     * Check whether the enemy is moving towards outside of the boundary
     * @param x the new x position of the enemy
     * @param y the new y position of the enemy
     * @return true if the enemy is still inside the boundary
     */
    protected boolean validMove(double x, double y){
        // check whether a movement is valid within the boundary
        if (x < TOP_LEFT_X || y < TOP_LEFT_Y || x > BOTTOM_RIGHT_X || y > BOTTOM_RIGHT_Y){
            return false;
        }
        return true;

    }

    /**
     * Check whether the enemy is colliding with any {@code statEntity}
     * @param entities An arraylist of {@code Tree} or {@code Wall}
     * @param sinkHoles An arraylist of {@code SinkHole}
     * @return true if they collide
     */
    protected boolean collision(ArrayList<statEntity> entities, ArrayList<SinkHole> sinkHoles){
        // produce the boundary box of the enemy
        Rectangle monsterBox = getBoundingBox();
        for (statEntity entity: entities){
            // if collide with trees or walls, rebound
            Rectangle entityBox = entity.getBoundingBox();
            if (monsterBox.intersects(entityBox)){
                return true;
            }
        }
        for (SinkHole holes: sinkHoles){
            // if collide with sinkholes, rebound
            Rectangle holeBox = holes.getBoundingBox();
            if (monsterBox.intersects(holeBox) && holes.isExist()){
                return true;
            }
        }
        return false;
    }


}
