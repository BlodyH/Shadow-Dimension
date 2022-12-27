import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * The class {@code Fire} is the attack presented by the {@code Enemies} which could cause the {@code Player}
 * lose health. The fire is rendered in one direction according to the relative position of the
 * {@code Demon} and {@code Player}
 */
public class Fire {
    protected double demonX;
    protected double demonY;
    protected double playerX;
    protected double playerY;
    protected Image enemyImage;
    protected final String FIRE_IMAGE_FILE = "res/demon/demonFire.png";
    protected Image fireImage;
    protected final int TOP_LEFT = 0;
    protected final int BOT_LEFT = 1;
    protected final int TOP_RIGHT = 2;
    protected final int BOT_RIGHT = 3;
    protected int damage;
    protected int direction;
    protected double drawX;
    protected double drawY;
    protected double rotationAngle;
    public DrawOptions rotation;

    /**
     * This is the constructor of the class {@code Fire}, deciding its image to render
     * @param demonX The x position of the enemy
     * @param demonY The y position of the enemy
     * @param playerX The x position of the player
     * @param playerY The y position of the player
     * @param enemyImage The image of the enemy
     */
    public Fire(double demonX, double demonY, double playerX, double playerY, Image enemyImage){
        /**
         * coordinates of center of demons and players
         */
        this.demonX = demonX;
        this.demonY = demonY;
        this.playerX = playerX;
        this.playerY = playerY;
        this.enemyImage = enemyImage;
        this.fireImage = new Image(FIRE_IMAGE_FILE);
        this.damage = 10;
    }

    /**
     * Set the image of the fire to a new one
     * @param fireImage The new Image file tobe used
     */
    public void setImage(String fireImage){
        this.fireImage = new Image(fireImage);
    }

    /**
     * get the top left x position of the fire , used for rendering
     * @return The top left x position of the fire
     */
    public double getDrawX() {
        return drawX;
    }

    /**
     * get the top left y position of the fire , used for rendering
     * @return The top left y position of the fire
     */
    public double getDrawY() {
        return drawY;
    }

    /**
     * Get the x position of the {@code Demon}
     * @return the x position of the demon
     */
    public double getDemonX() {
        return demonX;
    }
    /**
     * Get the y position of the {@code Demon}
     * @return the y position of the demon
     */
    public double getDemonY() {
        return demonY;
    }
    /**
     * Get the x position of the {@code Player}
     * @return the x position of the player
     */
    public double getPlayerX() {
        return playerX;
    }
    /**
     * Get the y position of the {@code Player}
     * @return the y position of the player
     */
    public double getPlayerY() {
        return playerY;
    }

    /**
     * Get the direction of the {@code Player} relative to the {@code Demon} and thus the direction
     * from which the fire should be rendered
     * @return the direction of the fire
     */
    protected int getDirection(){
        // according to the position of the demon and player, decide where the fire would be rendered from
        if ((getPlayerX() <= getDemonX()) && (getPlayerY() <= getDemonY())){
            return TOP_LEFT;
        }else if ((getPlayerX() <= getDemonX()) && (getPlayerY() > getDemonY())){
            return BOT_LEFT;
        }else if ((getPlayerX() > getDemonX()) && (getPlayerY() <= getDemonY())){
            return TOP_RIGHT;
        }else{
            return BOT_RIGHT;
        }
    }

    /**
     * When rendering the {@code Fire} from different directions, ned to rotate the image to fit.
     * Decide the rotation of the {@Fire}
     */
    protected void getRotation(){
        // set the rotation of the fire according to the rendering direction
        if (direction == TOP_LEFT){
            // set up the top left coordinate to render
            drawX = demonX - enemyImage.getWidth()/2 - fireImage.getWidth();
            drawY = demonY - enemyImage.getHeight()/2 - fireImage.getHeight();
            // calculate the rotation angle
            rotationAngle = 0;
            rotation = new DrawOptions();
            rotation.setRotation(rotationAngle);
        }else if (direction == BOT_LEFT){
            // set up the top left coordinate to render
            drawX = demonX - enemyImage.getWidth()/2 - fireImage.getWidth();
            drawY = demonY + enemyImage.getHeight()/2;
            // calculate the rotation angle
            rotationAngle = 3*Math.PI/2;
            rotation = new DrawOptions();
            rotation.setRotation(rotationAngle);
        }else if (direction == TOP_RIGHT){
            // set up the top left coordinate to render
            drawX = demonX + enemyImage.getWidth()/2;
            drawY = demonY - enemyImage.getHeight()/2 - fireImage.getHeight();
            // calculate the rotation angle
            rotationAngle = Math.PI/2;
            rotation = new DrawOptions();
            rotation.setRotation(rotationAngle);
        }else{
            // set up the top left coordinate to render
            drawX = demonX + enemyImage.getWidth()/2;
            drawY = demonY + enemyImage.getHeight()/2;
            // calculate the rotation angle
            rotationAngle = Math.PI;
            rotation = new DrawOptions();
            rotation.setRotation(rotationAngle);
        }
    }

    /**
     * This method performs a update of the state of the {@code Fire}, rendering it and then
     * check whether it collides with the player to inflict damgage
     * @param player The player in the game
     */
    public void update(Player player){
        // set the direction of the fire
        direction = getDirection();
        // set the rotation of the rendering
        getRotation();
        // render the fre
        fireImage.drawFromTopLeft(drawX, drawY, rotation);
        // if the player collides with the fire, get damage and turn into invincible state
        if (playerCollision(player) && !player.getInvincible()){
            player.receiveDamage(damage);
            player.setInvincible(true);
            damageLog(player);

        }

    }

    /**
     * Print out the damage information on the screen
     * @param player The player in the game
     */
    protected void damageLog(Player player){
        System.out.println("Demon inflicts 10 damage points on Fae. Fae's current health: " + player.getHealth() + "/" + player.getMAX_HEALTH());
    }

    /**
     * Checks whether the {@code Player} collides with the {@code Fire}
     * @param player The player in the game
     * @return true if they collides
     */
    protected boolean playerCollision(Player player){
        // produce the player bounding box
        Rectangle playerBox = player.getBoundingBox();
        if (playerBox.intersects(this.getBoundingBox())){
            // if collide, return true
            return true;
        }
        return false;
    }

    /**
     * Get the bounding box of the fire to check for collision
     * @return A {@code Rectangle} representing the bounding box
     */
    protected Rectangle getBoundingBox(){
        // produce the bounding box of the fire
        return this.fireImage.getBoundingBoxAt(new Point(this.getDrawX() + this.fireImage.getWidth()/2, this.getDrawY() + this.fireImage.getHeight()/2));
    }
}
