import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.Random;

/**
 * The class {@code Demon} represents the normal demons in the game, it inherits from the abstract class
 * {@code Enemies}, and includes individual methods for attacking and others
 */
public class Demon extends Enemies{
    private final String demonLeft = "res/demon/demonLeft.png";
    private final String demonRight= "res/demon/demonRight.png";
    private final String demonInvincibleLeft = "res/demon/demonInvincibleLeft.PNG";
    private final String demonInvincibleRight = "res/demon/demonInvincibleRight.PNG";
    private final Image DEMON_LEFT = new Image(demonLeft);
    private final Image DEMON_RIGHT = new Image(demonRight);
    private final Image DEMON_INVINCIBLE_LEFT = new Image(demonInvincibleLeft);
    private final Image DEMON_INVINCIBLE_RIGHT = new Image(demonInvincibleRight);
    private final static int HEALTH = 40;
    private final static int ATTACK_RANGE = 150;
    private int directionRand = new Random().nextInt(2);
    private Fire fire;


    /**
     * This is the constructor for the class {@code Demon}
     * @param x The x position of the demon
     * @param y The y position of the demon
     */
    public Demon(double x, double y){
        super(x, y);
        isAggressive = new Random().nextBoolean();
        if (moveDirection == LEFT){
            showImage = DEMON_LEFT;
        }else if (moveDirection == RIGHT){
            showImage = DEMON_RIGHT;
        }else{
            if (directionRand == 0){
                showImage = DEMON_LEFT;
            }else{
                showImage = DEMON_RIGHT;
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
                showImage = DEMON_INVINCIBLE_LEFT;
            }else{
                showImage = DEMON_LEFT;
            }
        }else{
            if (getInvincible()){
                showImage = DEMON_INVINCIBLE_RIGHT;
            }else{
                showImage = DEMON_RIGHT;
            }
        }
    }


    /**
     * This method performs a update on the state of each {@code Demon}, rendering it, moving it around and attacks
     * @param entities An arraylist of {@code statEntity} that appear in the game
     * @param sinkHoles An arraylist of {@code SinkHole} that appear in the game
     * @param player the {@code Player} in the game
     */
    public void update(ArrayList<statEntity> entities, ArrayList<SinkHole> sinkHoles, Player player){
        // if the demon is aggressive, it can move
        if (isAggressive == true){
            if (moveDirection == UP){
                movement(0, getStep());
            }else if (moveDirection == DOWN){
                movement(0, -getStep());
            }else if (moveDirection == LEFT){
                movement(-getStep(), 0);
            }else if (moveDirection == RIGHT){
                movement(getStep(), 0);
            }
            chooseImage();
        }else {
            // else, just decide his face direction and rendering image
            chooseImage();
        }
        if (!validMove(getX(), getY()) || collision(entities, sinkHoles)){
            // if collides with stationary entities, rebound
            Rebound();
        }
        if (inAttackRange(player) && !isDead()){
            // render the fire if the player is within attack range and the enemy is still alive
            fire = new Fire(getX() + showImage.getWidth()/2, getY() + showImage.getHeight()/2,
                    player.getX() + player.getImage().getWidth()/2, player.getY() + player.getImage().getHeight()/2,
                    this.showImage);
        }
        if (fire != null && inAttackRange(player) && !isDead()){
            // render the fire
            fire.update(player);
        }
        if (getInvincible()){
            // count invincible time
            invincibleCount++;
        }
        if (invincibleCount / (FPS/1000) > INVINCIBLE_DURATION){
            // out of invincible duration
            setInvincible(false);
            invincibleCount = 0;
        }
        if (!isDead()){
            // render the demon
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
