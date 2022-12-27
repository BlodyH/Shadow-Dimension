import bagel.Image;

/**
 * The class {@code NavecFire} inherits from the class {@code Fire} and represents the fire of the navec
 */
public class NavecFire extends Fire{
    private final String NAVEC_FIRE_IMAGE_FILE = "res/navec/navecFire.png";

    /**
     * This is the constructor of the class {@code NavecFire}, deciding its image to render
     * @param demonX The x position of the enemy
     * @param demonY The y position of the enemy
     * @param playerX The x position of the player
     * @param playerY The y position of the player
     * @param enemyImage The image of the enemy
     */
    public NavecFire(double demonX, double demonY, double playerX, double playerY, Image enemyImage){
        super(demonX, demonY, playerX, playerY, enemyImage);
        setImage(NAVEC_FIRE_IMAGE_FILE);
        this.damage = 20;

    }
    /**
     * Print out the damage information on the screen
     * @param player The player in the game
     */
    protected void damageLog(Player player){
        System.out.println("Navec inflicts 20 damage points on Fae. Fae's current health: " + player.getHealth() + "/" + player.getMAX_HEALTH());
    }
}
