import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

/**
 * The class {@code Player} is the player that the gamer controls in the game Shadow Dimension
 * She can move and attack, will die when health reaches 0
 * Avoid the sinkholes in the game and fight the demons and navec!
 */
public class Player implements Movable{
    private double x;
    private double y;
    private static int TOP_LEFT_X;
    private static int TOP_LEFT_Y;
    private static int BOTTOM_RIGHT_X;
    private static int BOTTOM_RIGHT_Y;
    private int health;
    private final int MAX_HEALTH = 100;
    private Image showImage;
    private boolean win;
    private double fromX;
    private double fromY;
    private final double STEP = 2;
    private final static int HEALTH_X = 20;
    private final static int HEALTH_Y = 25;
    private final static int HEALTH_FONT_SIZE = 30;
    private final static String FONT_FILE = "res/frostbite.ttf";
    private final static String playerLeft = "res/fae/faeLeft.png";
    private final static String playerRight = "res/fae/faeRight.png";
    private final static String playerAttackLeft = "res/fae/faeAttackLeft.png";
    private final static String playerAttackRight = "res/fae/faeAttackRight.png";
    private Font HealthFont = new Font(FONT_FILE, HEALTH_FONT_SIZE);
    private final Image leftImage = new Image(playerLeft);
    private final Image rightImage = new Image(playerRight);
    private final Image leftAttackImage = new Image(playerAttackLeft);
    private final Image rightAttackImage = new Image(playerAttackRight);
    private final static int COOL_DOWN = 2000;
    private final static int ATTACK_DURATION = 1000;
    private final static int INVINCIBLE_DURATION = 3000;
    private final static double FPS = 60;
    private final static int DAMAGE = 20;
    private double attackCount = 0;
    private double coolDownCount = 0;
    private double invincibleCount = 0;
    private boolean isAttack = false;
    private boolean isCoolDown = false;
    private boolean isInvincible = false;
    private boolean faceRight = true;

    private final static DrawOptions COLOUR = new DrawOptions();
    private final static Colour GREEN = new Colour(0, 0.8, 0.2);
    private final static Colour ORANGE = new Colour(0.9, 0.6, 0);
    private final static Colour RED = new Colour(1, 0, 0);
    private final static double HIGH_HEALTH = 65;
    private final static double LOW_HEALTH = 35;

    /**
     * This is the constructor of the {@code Player}, deciding its position, initial image and the health points
     * @param x The x position of the player
     * @param y The y position of the player
     */
    public Player(double x, double y){
        this.showImage = rightImage;
        this.x = x;
        this.y = y;
        this.fromX =x;
        this.fromY = y;
        this.health = MAX_HEALTH;
        this.win = false;
    }

    /**
     * This method sets up the boundary of the game such that the {@code Player} should never get out of
     * @param TOP_LEFT_X the top left x position of the boundary
     * @param TOP_LEFT_Y the top left y position of the boundary
     * @param BOTTOM_RIGHT_X the bottom right x position of the boundary
     * @param BOTTOM_RIGHT_Y the bottom right y position of the boundary
     */
    public void setBoundary(int TOP_LEFT_X, int TOP_LEFT_Y, int BOTTOM_RIGHT_X, int BOTTOM_RIGHT_Y){
        this.TOP_LEFT_X = TOP_LEFT_X;
        this.TOP_LEFT_Y = TOP_LEFT_Y;
        this.BOTTOM_RIGHT_X = BOTTOM_RIGHT_X;
        this.BOTTOM_RIGHT_Y = BOTTOM_RIGHT_Y;
    }

    /**
     * Get the x position of the {@code Player}
     * @return The x position of the Player
     */
    public double getX(){
        return this.x;
    }
    /**
     * Get the y position of the {@code Player}
     * @return The y position of the Player
     */
    public double getY(){
        return this.y;
    }
    /**
     * Get the current {@code health} of the {@code Player}
     * @return The health point
     */
    public int getHealth() {
        return health;
    }
    /**
     * Get the Maximum health of the {@code Player}
     * @return The {@code MAX_HEALTH} of the player
     */
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Get the attack state of the {@code Player}
     * @return true if the player is under attack state
     */
    public boolean getAttack() {
        return isAttack;
    }

    /**
     * Change the attack state of the {@code Player}
     * @param attack the new attack state
     */
    public void setAttack(boolean attack) {
        isAttack = attack;
    }

    /**
     * Get whether the attack state is under cooldown
     * @return true if it is under cooldown
     */
    public boolean getCoolDown() {
        return isCoolDown;
    }

    /**
     * Change the state of cooldown
     * @param coolDown the new state of the cooldown
     */
    public void setCoolDown(boolean coolDown) {
        isCoolDown = coolDown;
    }

    /**
     * Get whether the player is under Invincible state
     * @return true if player is under invincible state
     */
    public boolean getInvincible() {
        return isInvincible;
    }

    /**
     * Change the invincible of the player
     * @param invincible the new invincible state
     */
    public void setInvincible(boolean invincible) {
        isInvincible = invincible;
    }

    /**
     * Get the {@code MAX_HEALTH} of the {@code player}
     * @return the {@code MAX_HEALTH}
     */
    public int getMAX_HEALTH() {
        return MAX_HEALTH;
    }

    /**
     * Get the {@code showImage} of the player
     * @return the {@code showImage} of the player
     */
    public Image getImage() {
        return showImage;
    }

    /**
     * Inflict the {@code damage} on the {@code Player}
     * @param damage the damage applied to the {@code Player}
     */
    public void receiveDamage(int damage){
        // set the health by minuse the received damage
        this.setHealth(this.getHealth() - damage);
        // never go below 0
        if (this.getHealth() < 0){
            this.setHealth(0);
        }
    }

    /**
     * Set the point where the player comes from
     * used when a player in committing an invalid movement
     */
    private void setFrom(){
        fromX = x;
        fromY = y;
    }

    /**
     * Move the {@code Player} around
     * @param moveX How much change should be applied to the x position
     * @param moveY How much change should be applied to the y position
     */
    @Override
    public void movement(double moveX, double moveY) {
        // If the movement is valid, commit it
        if (validMove(x + moveX, y + moveY)) {
            x += moveX;
            y += moveY;
        }

    }


    /**
     * Performs a player update
     * This includes: movement
     *                rendering
     *                collision detection
     *                rendering health
     * @param input  The input of the gamer such as which key was pressed by the gamer
     * @param demon  An arraylist of the {@code Demon} that appear in the game
     * @param entities  An arraylist of the {@code statEntity} that appear in the game, include {@code Wall} or {@code Tree}
     * @param sinkhole  An arraylist of the {@code SinkHole} that appear in the game
     * @param navec The boss {@code Navec} in the game
     */
    public void update(Input input, ArrayList<statEntity> entities, ArrayList<SinkHole> sinkhole, ArrayList<Demon> demon, Navec navec){
        // perform the movement update of the player
        if (input.isDown(Keys.UP)){
            // store original position
            setFrom();
            movement(0, -STEP);
        }else if (input.isDown(Keys.DOWN)){
            setFrom();
            movement(0, STEP);
        }else if (input.isDown(Keys.LEFT)){
            setFrom();
            movement(-STEP, 0);
            faceRight = false;
            showImage = leftImage;
        }else if (input.isDown(Keys.RIGHT)){
            setFrom();
            movement(STEP, 0);
            showImage = rightImage;
        }
        // turn to attack state in level 1 when it is not at cooldown
        if (input.wasPressed(Keys.A) && !getCoolDown() && navec != null){
            setAttack(true);
        }
        if (getAttack()){
            // change the rendering image in attack stat
            if (faceRight){
                showImage = rightAttackImage;
            }else {
                showImage = leftAttackImage;
            }
            demonCollision(demon, navec);
            attackCount++;
        }
        if (attackCount / (FPS/1000) > ATTACK_DURATION){
            // out of the attack duration and go into cooldown
            setAttack(false);
            setCoolDown(true);
            attackCount = 0;
            if (faceRight){
                showImage = rightImage;
            }else{
                showImage = leftImage;
            }
        }
        if (getCoolDown()){
            // time the cool down
            coolDownCount++;
        }
        if (coolDownCount / (FPS/1000) > COOL_DOWN){
            // finished cooldown
            setCoolDown(false);
            coolDownCount = 0;
        }
        if (getInvincible()){
            // time the invincible time
            invincibleCount++;
        }
        if (invincibleCount / (FPS/1000) > INVINCIBLE_DURATION){
            setInvincible(false);
            invincibleCount = 0;
        }


        showImage.drawFromTopLeft(x, y);
        entityCollision(entities);
        holeCollision(sinkhole);
        drawHealth();



    }


    /**
     * Determines whether a movement could be committed
     * The player should never move across a wall or out of the boundary
     */
    private boolean validMove(double x, double y){
        // if the player is outside the boundary, return false
        if (x < TOP_LEFT_X || y < TOP_LEFT_Y || x > BOTTOM_RIGHT_X || y > BOTTOM_RIGHT_Y){
            return false;
        }
        return true;
    }

    /**
     * Return the Bounding box of the {@code Player} for later collide detection
     * @return An {@code Rectangle} representing the bounding box of the {@code Player}
     */
    public Rectangle getBoundingBox(){
        return this.showImage.getBoundingBoxAt(new Point(this.getX() + this.showImage.getWidth()/2, this.getY() + this.showImage.getHeight()/2));
    }

    /**
     * Render the Health on the screen
     */
    private void drawHealth(){
        // calculate the health as a percentage
        double percentageHealth = ((double) health/MAX_HEALTH) * 100;
        // change color according to different health
        if (percentageHealth <= LOW_HEALTH){
            COLOUR.setBlendColour(RED);
        }
        else if (percentageHealth <= HIGH_HEALTH){
            COLOUR.setBlendColour(ORANGE);
        }
        else {
            COLOUR.setBlendColour(GREEN);
        }
        HealthFont.drawString(Math.round(percentageHealth) + "%", HEALTH_X, HEALTH_Y, COLOUR);
    }

    /**
     * Detects whether the player is colliding with a sinkhole
     * If so, inflict the damage on the player and remove the sinkhole
     */
    private void holeCollision(ArrayList<SinkHole> sinkhole){
        // Produce the bounding box of the player
        Rectangle playerBox = getBoundingBox();
        // Loop through all holes
        for (SinkHole hole: sinkhole) {
            //produce the bounding box of the hole
            Rectangle holeBox = hole.getBoundingBox();
            // If collides, eliminate the hole and inflict the damage on the player
            if (playerBox.intersects(holeBox) && hole.isExist()) {
                hole.eliminate();
                this.receiveDamage(hole.getDamage());
                System.out.println("Sinkhole inflicts " + hole.getDamage() + " damage points on Fae. " +
                        "Fae's current health: " + health + "/" + MAX_HEALTH);

                stay();

            }

        }
    }

    /**
     * Detects whether the player is colliding with a wall
     */
    private void entityCollision(ArrayList<statEntity> entities){
        // produce the bounding box of the player
        Rectangle playerBox = getBoundingBox();
        for (statEntity entity: entities){
            if (entity != null){
                // produce the bounding box of the wall
                Rectangle wallBox = entity.getBoundingBox();
                // If collides, cannot move in that direction
                if (playerBox.intersects(wallBox)) {
                    stay();
                }
            }
        }
    }


    private void demonCollision(ArrayList<Demon> demon, Navec navec){
        // produce the bounding box of the player and navec
        Rectangle playerBox = this.getBoundingBox();
        Rectangle navecBox = navec.getBoundingBox();
        if (playerBox.intersects(navecBox) && !navec.getInvincible()){
            // if collides, the navec receive damage as player is under attack state, navec turn into invincible state
            navec.receiveDamage(DAMAGE);
            navec.setInvincible(true);
            System.out.println("Fae inflicts " + DAMAGE + " damage points on Navec. Navec's current health: " + navec.getHealth() + "/" + navec.getMaxHealth());
        }
        for (Demon demons: demon){
            // produce the bounding box of the demon
            Rectangle demonBox = demons.getBoundingBox();
            if (playerBox.intersects(demonBox) && !demons.getInvincible() && !demons.isDead()){
                // if collides, the demon receive damage as player is under attack state, demon turn into invincible state
                demons.receiveDamage(DAMAGE);
                demons.setInvincible(true);
                System.out.println("Fae inflicts " + DAMAGE + " damage points on Demon. Demon's current health: " + demons.getHealth() + "/" + demons.getMaxHealth());
            }
        }
    }
    /**
     * Once the player is trying to move to somewhere invalid, it's coordinate should not be updated
     */
    private void stay(){
        // stay when moving to somewhere invalid
        x = fromX;
        y = fromY;
    }

    /**
     * Returns whether the {@code Player} is dead or not
     * @retun true if the {@code Player} has a {@code health} <= 0
     */
    public boolean Dead(){
        return (health <= 0);
    }

    /**
     * Returns whether the player has passed the winning point in level 0
     * @return true if she has passed
     */
    public boolean passLevel0(){
        return (x >= 950 && y >= 670);
    }



}
