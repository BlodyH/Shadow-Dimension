import bagel.Image;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.Random;

/**
 * The class {@code Navec} is the boss of the game. It inherits from the abstract class
 * {@code Enemies} and performs individual methods such as attacking
 */
public class Navec extends Enemies{
    private final String navecLeft = "res/navec/navecLeft.png";
    private final String navecRight= "res/navec/navecRight.png";
    private final String navecInvincibleLeft = "res/navec/navecInvincibleLeft.PNG";
    private final String navecInvincibleRight = "res/navec/navecInvincibleRight.PNG";

    private final Image NAVEC_LEFT = new Image(navecLeft);
    private final Image NAVEC_RIGHT = new Image(navecRight);
    private final Image NAVEC_INVINCIBLE_LEFT = new Image(navecInvincibleLeft);
    private final Image NAVEC_INVINCIBLE_RIGHT = new Image(navecInvincibleRight);
    private final static int HEALTH = 80;
    private final static int ATTACK_RANGE = 200;
    private int directionRand = new Random().nextInt(2);

    private NavecFire navecFire;

    /**
     * This is the constructor of the {@code Navec}, deciding its image and health
     * @param x The x position of the navec
     * @param y The y position of the navec
     */
    public Navec(double x, double y){
        super(x, y);
        if (moveDirection == LEFT){
            showImage = NAVEC_LEFT;
        }else if (moveDirection == RIGHT){
            showImage = NAVEC_RIGHT;
        }else{
            if (directionRand == 0){
                showImage = NAVEC_LEFT;
            }else{
                showImage = NAVEC_RIGHT;
            }
        }
        MAX_HEALTH = HEALTH;
        health = MAX_HEALTH;
    }
    private void chooseImage(){
        // choose the image to render according to face direction and state
        if (moveDirection == LEFT){
            // render invincible image in invincible state
            if (getInvincible()){
                showImage = NAVEC_INVINCIBLE_LEFT;
            }else{
                showImage = NAVEC_LEFT;
            }
        }else{
            if (getInvincible()){
                showImage = NAVEC_INVINCIBLE_RIGHT;
            }else{
                showImage = NAVEC_RIGHT;
            }
        }
    }

    /**
     * This method performs a update on the state of each {@code Navec}, rendering it, moving it around and attacks
     * @param entities An arraylist of {@code statEntity} that appear in the game
     * @param sinkHoles An arraylist of {@code SinkHole} that appear in the game
     * @param player the {@code Player} in the game
     */
    public void update(ArrayList<statEntity> entities, ArrayList<SinkHole> sinkHoles, Player player){
        // make the movement
        if (moveDirection == UP){
            movement(0, getStep());
        }else if (moveDirection == DOWN){
            movement(0, -getStep());
        }else if (moveDirection == LEFT){
            movement(-getStep(), 0);
        }else if (moveDirection == RIGHT){
            movement(getStep(), 0);
        }
        // choose the image to render according to moving direction and state
        chooseImage();
        if (!validMove(getX(), getY()) || collision(entities, sinkHoles)){
            // if collides, rebound
            Rebound();
        }
        if (inAttackRange(player) && !isDead()){
            // if the player is inside the attack range, start fire
            navecFire = new NavecFire(getX() + showImage.getWidth()/2, getY() + showImage.getHeight()/2,
                    player.getX() + player.getImage().getWidth()/2, player.getY() + player.getImage().getHeight()/2,
                    this.showImage);
        }
        if (navecFire != null && inAttackRange(player) && !isDead()){
            // render the navecFire
            navecFire.update(player);
        }
        if (getInvincible()){
            // count invincible time
            invincibleCount++;
        }
        if (invincibleCount / (FPS/1000) > INVINCIBLE_DURATION){
            // out of the invincible state
            setInvincible(false);
            invincibleCount = 0;
        }
        if (!isDead()){
            // render the navec
            showImage.drawFromTopLeft(getX(), getY());
            drawHealth();
        }
    }

    private Point getCenter(double x, double y, Image showImage){
        // calculate the center of the demon according to the top left coordinate and the size of the image
        double centerX = x + showImage.getWidth()/2;
        double centerY = y + showImage.getHeight()/2;
        return new Point(centerX, centerY);
    }

    private boolean inAttackRange(Player player){
        // if the distance between the demon and player is less than the attack range, return true
        if (getCenter(getX(), getY(), this.showImage).distanceTo(getCenter(player.getX(), player.getY(), player.getImage())) <= ATTACK_RANGE){
            return true;
        }
        return false;
    }



}
