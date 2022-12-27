import java.util.ArrayList;

/**
 * The class {@code TimeScaleControl} controls the speed of the enemies in the game
 * Their speed could be increased or decreased in a certain range, with each time
 * changing by 50%
 */
public class TimeScaleControl {
    private final static int MAX_COUNT = 3;
    private final static int MIN_COUNT = -3;
    private final static double SIZE = 0.5;
    private int count;

    /**
     * This is the constructor of the {@code TimeScaleControl}, setting initial control count to 0
     */
    public TimeScaleControl(){
        this.count = 0;
    }

    /**
     * The method {@code increaseScale} increases the speed of the enemies
     * @param demon An arraylist containing all the demons in the game
     * @param navec The navec in the game
     */
    public void increaseScale(ArrayList<Demon> demon, Navec navec){
        // if the timescale is within range, speed up
        if (count < MAX_COUNT){
            this.count++;
            setSpeed(demon, navec);
            System.out.println("Sped up, Speed: " + count);
        }

    }
    /**
     * The method {@code decreaseScale} decreases the speed of the enemies
     * @param demon An arraylist containing all the demons in the game
     * @param navec The navec in the game
     */
    public void decreaseScale(ArrayList<Demon> demon, Navec navec) {
        // if the timescale is within range, speed down
        if (count > MIN_COUNT){
            this.count--;
            setSpeed(demon, navec);
            System.out.println("Slowed down, Speed: " + count);
        }
    }

    /**
     * The method {@code setSpeed} controls the speed of the enemies in the game
     * multiply by 1.5 to the times of the timescale count if increasing
     * multiply by 0.5 to the times of the timescale count if decreasing
     * @param demon An arraylist containing all the demons in the game
     * @param navec The navec in the game
     */
    private void setSpeed(ArrayList<Demon> demon, Navec navec){
        // loop through all demons
        for (Demon demons: demon){
            if (count > 0) {
                // if the game is sped up, increase their speed
                demons.setStep(demons.getInitialStep() * Math.pow(1 + SIZE, count));
            }else {
                // if the game is sped down, decrease their speed
                demons.setStep(demons.getInitialStep() * Math.pow(SIZE, -count));
            }

        }
        if (count > 0) {
            // set up the speed for the navec
            navec.setStep(navec.getInitialStep() * Math.pow(1 + SIZE, count));
        }else{
            navec.setStep(navec.getInitialStep() * Math.pow(SIZE, -count));
        }
    }

}
