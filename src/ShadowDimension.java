import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;

/**
 * Skeleton Code for SWEN20003 Project 2, Semester 2, 2022
 *
 * Please enter your name below
 * @author Yuchen Luo
 */

public class ShadowDimension extends AbstractGame {
    /**
     * Class {@code ShadowDimension} is the main body of the game Shadow Dimension
     * It extends the class {@code AbstractGame} from bagel library
     *This root class controls all the rendering and interactive functionalities
     */
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static int TITLE_X = 260;
    private final static int TITLE_Y = 250;
    private final static int LEVEL_1_TITLE_X = 350;
    private final static int LEVEL_1_TITLE_Y = 350;
    private static int TOP_LEFT_X;
    private static int TOP_LEFT_Y;
    private static int BOTTOM_RIGHT_X;
    private static int BOTTOM_RIGHT_Y;
    /**
     * Messages to be rendered on the screen
     */
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static String GAME_START = "PRESS SPACE TO START";
    private final static String GAME_INSTRUCT = "USE ARROW TO FIND GATE";
    private final static String LEVEL_UP_MESSAGE = "LEVEL COMPLETE!";
    private final static String LEVEL_1_INSTRUCTION_1 = "PRESS A TO ATTACK";
    private final static String LEVEL_1_INSTRUCTION_2 = "DEFEAT NAVEC TO WIN";
    private final static String WIN = "CONGRATULATIONS!";
    private final static String LOSE = "GAME OVER!";
    private final static String WALL_IMAGE = "res/wall.png";
    private final static String TREE_IMAGE = "res/tree.png";
    private final static String SINKHOLE_IMAGE = "res/sinkhole.png";
    private final static String WORLD_FILE = "res/level0.csv";
    private final static String WORLD_FILE_1 = "res/level1.csv";
    private final static String FONT_FILE = "res/frostbite.ttf";
    private final static int FONT_SIZE = 75;
    private final static int SMALL_FONT_SIZE = 40;
    private final static int FONT_Y = 402;
    private final Font FONT;
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private final Image BACKGROUND_IMAGE_1 = new Image("res/background1.png");

    // Tells whether the game has started or not
    private boolean gameOn;

    // Tells whether the game is ended or not
    private boolean gameEnd;
    //Tells whether the game is Win or not
    private boolean gameWin;
    private Player player;
    private ArrayList<statEntity> Wall = new ArrayList<statEntity>();
    private ArrayList<statEntity> Tree = new ArrayList<statEntity>();
    private ArrayList<SinkHole> sinkHole = new ArrayList<SinkHole>();
    private ArrayList<Demon> demon = new ArrayList<Demon>();
    private boolean levelUp = false;
    private boolean hasLevelUp = false;
    private int levelUpCount = 0;
    private final static double FPS = 60;
    private final static int LEVEL_UP_DURATION = 3000;
    private TimeScaleControl timeScaleControl = new TimeScaleControl();

    private Navec navec;

    /**
     * This constructor {@code ShadowDimension} construct the game
     * initialises the font to be used in the game and reads in the World file provided
     */
    public ShadowDimension() {

        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        this.FONT = new Font(FONT_FILE, FONT_SIZE);
        readCSV(WORLD_FILE);
        gameWin = false;
        gameEnd = false;
        gameOn = false;
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    /**
     * Method {@code readCSV} used to read file and create objects
     * @param filename of the world file to be read.
     */
    private void readCSV(String filename) {

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            if ((line = reader.readLine()) != null) {
                // separate the line components
                String[] values = line.split(",");

                // read in the initial position of the player
                if (values[0].equals("Fae")) {
                    player = new Player(Integer.parseInt(values[1]), Integer.parseInt(values[2]));
                }
            }

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                // read in the walls and put into the wall arraylist
                if (values[0].equals("Wall")) {
                    Wall.add(new Wall(Double.parseDouble(values[1]), Double.parseDouble(values[2]), WALL_IMAGE));
                }
                // read in the holes and put into the hole arraylist
                if (values[0].equals("Sinkhole")) {
                    sinkHole.add(new SinkHole(Double.parseDouble(values[1]), Double.parseDouble(values[2]), SINKHOLE_IMAGE));
                }
                // read in the trees and put into the tree arraylist
                if (values[0].equals("Tree")) {
                    Tree.add(new Tree(Double.parseDouble(values[1]), Double.parseDouble(values[2]), TREE_IMAGE));
                }
                // read in the demons and put into the demon arraylist
                if (values[0].equals("Demon")) {
                    demon.add(new Demon(Double.parseDouble(values[1]), Double.parseDouble(values[2])));
                }
                // read in the navec
                if (values[0].equals("Navec")) {
                    this.navec = new Navec(Double.parseDouble(values[1]), Double.parseDouble(values[2]));
                }
                // read in the boundaries of the game
                if (values[0].equals("TopLeft")) {
                    TOP_LEFT_X = Integer.parseInt(values[1]);
                    TOP_LEFT_Y = Integer.parseInt(values[2]);
                }
                if (values[0].equals("BottomRight")) {
                    BOTTOM_RIGHT_X = Integer.parseInt(values[1]);
                    BOTTOM_RIGHT_Y = Integer.parseInt(values[2]);
                }

            }
            // deliver the boundaries to the Player
            player.setBoundary(TOP_LEFT_X, TOP_LEFT_Y, BOTTOM_RIGHT_X, BOTTOM_RIGHT_Y);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     * @param input This is the input from the gamer eg: what key did he press on
     */
    @Override
    protected void update(Input input) {
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
        // if the game is not started, render the initial messages
        if (gameOn == false) {
            gameInitialise(input);
        } else {
            // if the game is ended and has not won, render the LOSE message
            if (gameEnd == true && gameWin == false) {
                gameEnding(LOSE);
            }
            // if passed level 0 and haven't passed the three second to render "LEVEL COMPLETE!"
            if (levelUp && !hasLevelUp){
                // count the three seconds
                levelUpCount++;
                gameEnding(LEVEL_UP_MESSAGE);
            }
            if (levelUpCount / (FPS/1000) > LEVEL_UP_DURATION){
                // has passed three seconds, go into initialisation of Level 1
                hasLevelUp = true;
                sinkHole = new ArrayList<SinkHole>();
                // read in the world file
                readCSV(WORLD_FILE_1);
                gameOn = false;
                levelUpCount = 0;
            }
            // if the game is won, render the WIN message
            if (gameWin == true) {
                gameEnding(WIN);
            }
            // else, keep updating the game process
            if (gameEnd == false && gameWin == false && gameOn == true) {
                // in level 0
                if (!levelUp) {
                    BACKGROUND_IMAGE.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
                    // render the entities
                    for (statEntity walls : Wall) {
                        walls.update();
                    }
                    for (SinkHole hole : sinkHole) {
                        if (hole.isExist() == true) {
                            hole.update();
                        }
                    }
                    // produce the update of the player
                    player.update(input, Wall, sinkHole, demon, navec);
                    // determine whether the game has ended or not
                    if (player.Dead()) {
                        gameEnd = true;
                    }
                    if (player.passLevel0() || input.wasPressed(Keys.W)) {
                        levelUp = true;
                    }
                }
                // in level 1
                else if (levelUp & hasLevelUp){
                    BACKGROUND_IMAGE_1.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);

                    // render the entities
                    for (statEntity trees : Tree) {
                        trees.update();
                    }
                    for (SinkHole hole : sinkHole) {
                        if (hole.isExist() == true) {
                            hole.update();
                        }
                    }
                    // render the enemies
                    for (Demon demons: demon){
                        // give the demons the boundary
                        demons.setBoundary(TOP_LEFT_X, TOP_LEFT_Y, BOTTOM_RIGHT_X, BOTTOM_RIGHT_Y);
                        if (!demons.isDead()) {
                            demons.update(Tree, sinkHole, player);
                        }
                    }
                    // give the navec the boudnary and render it
                    navec.setBoundary(TOP_LEFT_X, TOP_LEFT_Y, BOTTOM_RIGHT_X, BOTTOM_RIGHT_Y);
                    navec.update(Tree, sinkHole, player);
                    // produce the update of the player
                    player.update(input, Tree, sinkHole, demon, navec);
                    // control the timescale
                    if (input.wasPressed(Keys.L)){
                        timeScaleControl.increaseScale(demon, navec);
                    }
                    if (input.wasPressed(Keys.K)){
                        timeScaleControl.decreaseScale(demon, navec);
                    }
                    // determine whether the game has ended or not
                    if (player.Dead()) {
                        gameEnd = true;
                    }
                    if (navec.isDead()) {
                        gameWin = true;
                    }
                }
            }


        }

    }

    /**
     * The method {@code gameInitialise} controls the rendering of the messages before the game is actually started
     * before each level
     * (before pressing SPACE)
     * @param input This is the input from the gamer eg: what key did he press on
     */
    private void gameInitialise(Input input) {
        // initialisation for level 0
        if (!levelUp) {
            Font messageFont = new Font(FONT_FILE, SMALL_FONT_SIZE);
            FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            messageFont.drawString(GAME_START, TITLE_X + 90, TITLE_Y + 190);
            messageFont.drawString(GAME_INSTRUCT, TITLE_X + 90, TITLE_Y + 190 + 40);
            if (input.wasPressed(Keys.SPACE)) {
                gameOn = true;
            }
        }
        // initialisation for level 1
        else {
            Font messageFont = new Font(FONT_FILE, SMALL_FONT_SIZE);
            messageFont.drawString(GAME_START, LEVEL_1_TITLE_X, LEVEL_1_TITLE_Y);
            messageFont.drawString(LEVEL_1_INSTRUCTION_1, LEVEL_1_TITLE_X, LEVEL_1_TITLE_Y + 40);
            messageFont.drawString(LEVEL_1_INSTRUCTION_2, LEVEL_1_TITLE_X, LEVEL_1_TITLE_Y + 80);
            if (input.wasPressed(Keys.SPACE)) {
                gameOn = true;
            }
        }
    }



    /**
     * The method {@code gameEnding} controls which message to be rendered at the ending of each level
     * @param endMessage This is the message to be rendered
     */
    private void gameEnding(String endMessage) {
        FONT.drawString(endMessage, (Window.getWidth() / 2.0 - (FONT.getWidth(endMessage) / 2.0)), FONT_Y);
    }

}



