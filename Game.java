/*----------------------------------------------------------------
 *  Author:   K. Walsh
 *  Editor:   Trishala Manandhar
 *  Email:    kwalsh@holycross.edu
 *  Written:  7/13/2015
 *  
 *  Minesweeper game. This class implements the game window and most
 *  of the game logic.
 *----------------------------------------------------------------*/

import GUI.*;

/**
 * A <i>Game</i> object manages all information about a minesweeper game as it
 * is being played and displayed on the screen. This includes information about
 * all of the cells (this is stored in a 2-D array of Cell objects), how many
 * flags have been planted, how many mines have been deployed, etc. Game extends
 * Window, so it can be drawn on the screen. It also extends EventListener so it
 * can respond to user interaction.
 */
public class Game extends Window implements EventListener {

    /**
     * Number of cells tall the game board will be.
     */
    public static final int NUM_ROWS = 20;

    /**
     * Number of cells wide the game board will be.
     */
    public static final int NUM_COLS = 30;

    // Example game screen layout:
    // +---------------------------------------------------------+
    // |      M A R G I N = 50                                   |
    // | M  + - - - - - - - - - - - - - - - - - - - - - - - + M  |
    // | A  |                                               | A  |
    // | R  |                                               | R  |
    // | G  |                Grid of Cells                  | G  |
    // | I  |                                               | I  |
    // | N  |                                               | N  |
    // | =  |       600 = NUM_COLS * Cell.SIZE wide         | =  |
    // | 50 |                      by                       | 50 |
    // |    |       400 = NUM_ROWS * Cell.SIZE tall         |    |
    // |    |                                               |    |
    // |    |                                               |    |
    // |    |                                               |    |
    // |    + - - - - - - - - - - - - - - - - - - - - - - - +    |
    // |            SPACE     S   SPACE   S    SPACE             |
    // |    + - - - - - - - + P + - - - + P + - - - - - - - +    |
    // |    |    Status     | A | Timer | A |     Help      |    |
    // |    |       Box     | C |       | C |      Box      |    |
    // |    + - - - - - - - + E + - - - + E + - - - - - - - +    |
    // |     M A R G I N = 50                                    |
    // +-- ------------------------------------------------------+

    /**
     * Width of the game window, in pixels.
     * Equal to 2*MARGIN + GRID_WIDTH
     * or 2*MARGIN + 2*SPACE + StatusBox.WIDTH, Timer.WIDTH, HelpBox.WIDTH,
     * whichever is larger.
     */
    public static final int WIDTH = 700;

    /**
     * Height of the game window, in pixels.
     * Equal to 2*MARGIN + SPACE
     *     + GRID_HEIGHT
     *     + max(StatusBox.Height, Timer.HEIGHT, HelpBox.HEIGHT)
     */
    public static final int HEIGHT = 600; 

    /**
     * Width of the grid part of the window, in pixels.
     * Equal to NUM_COLS * Cell.SIZE.
     */
    public static final int GRID_WIDTH = NUM_COLS * Cell.SIZE;

    /**
     * Height of the grid part of the window, in pixels.
     * Equal to NUM_ROWS * Cell.SIZE.
     */
    public static final int GRID_HEIGHT = NUM_ROWS * Cell.SIZE;

    /**
     * Margin around the edges of the canvas.
     */
    private static final int MARGIN = 50;

    /**
     * Space between elements on the canvas.
     */
    private static final int SPACE = 25;

    // A 2-D array of Cell objects to keep track of the board state.
    private Cell[][] cells = new Cell[NUM_ROWS][NUM_COLS];

    private int numMines = 0;    // number of mines deployed
    private int numRevealed = 0; // number of cells revealed so far

    // Whether or not the game has been won.
    private boolean gameWon = false;

    // Whether or not the game has been lost
    private boolean gameLost = false;

    // Name of the user playing the game.
    private String username;

    // The difficulty level of the game, used for tracking top scores.
    private String difficulty;

    // The status box that appears in the bottom left.
    private StatusBox status;

    // The timer that appears in the bottom middle.
    private Timer timer;
    
    // The help box that appears in the bottom right.
    private HelpBox help;

    /**
     * Constructor: Initializes a new game, but does not deploy any mines, plant
     * any flags, etc. The difficulty is either "easy", "medium", or "hard", and
     * will be used to load the proper top scores file. Name is used as the
     * user's name.
     */
    public Game(String name, String difficulty) {
        super("Minesweeper!", WIDTH, HEIGHT);

        this.username = name;
        this.difficulty = difficulty;

        // Create the background
        setBackgroundColor(Canvas.DARK_GRAY);

        // Create a border around the grid
        Box border = new Box(MARGIN-1.5, MARGIN-1.5, GRID_WIDTH+3, GRID_HEIGHT+3);
        border.setBackgroundColor(null);
        border.setBorderColor(Canvas.BLACK);
        add(border);

        // The following are the info boxes at the bottom of the game
        help = new HelpBox(
                WIDTH - MARGIN - HelpBox.WIDTH,
                HEIGHT - MARGIN - HelpBox.HEIGHT);
        add(help);

        timer = new Timer(
                WIDTH - MARGIN - HelpBox.WIDTH - SPACE - Timer.WIDTH,
                HEIGHT - MARGIN - Timer.HEIGHT);
        add(timer);

        status = new StatusBox(this,
                WIDTH - MARGIN - HelpBox.WIDTH - SPACE - Timer.WIDTH - SPACE - StatusBox.WIDTH,
                HEIGHT - MARGIN - StatusBox.HEIGHT);
        add(status);
        
        // the following code initializes and add all of the cells to form the game grid.
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                 cells[row][col] = new Cell(MARGIN+Cell.SIZE*col, MARGIN+Cell.SIZE*row);
                 add(cells[row][col]);
            }
        } 
    } // end of Game
 

    /**
     * Get the number of mines that are deployed.
     */
    public int getNumMinesDeployed() {
        return numMines;
    }

    /**
     * Get the number of hidden cells remaining to be revealed.
     */
    public int getNumCellsRemaining() {
        return NUM_ROWS * NUM_COLS - numRevealed;
    }

    /**
     * Deploy the given number of mines. This gets called once during game
     * setup. The game doesn't actually begin officially until the user clicks
     * a cell, so the timer should not start yet.
     */
    public void deployMines(int mines) {
        // The following code deploys mines by calling makeMine() on some of
        // the cells, at random.Then makes sure to increment the neighbor mine count
        // once the mine has been deployed.
  
        while (numMines < mines) {
            int row = StdRandom.uniform(NUM_ROWS);
            int col = StdRandom.uniform(NUM_COLS);

            if (!cells[row][col].isMine) {
                cells[row][col].makeMine();
                numMines++;   
             
                if (row == 0 && col == 0) { 
                    cells[row+1][col].incrementNeighborMineCount();
                    cells[row][col+1].incrementNeighborMineCount();
                    cells[row+1][col+1].incrementNeighborMineCount();
                }// top left corner
                else if (row == NUM_ROWS-1 && col == 0) {
                    cells[row-1][col].incrementNeighborMineCount();
                    cells[row-1][col+1].incrementNeighborMineCount();
                    cells[row][col+1].incrementNeighborMineCount();
                }// bottom left corner
                else if (row == 0 && col == NUM_COLS-1) {
                    cells[row+1][col].incrementNeighborMineCount();
                    cells[row][col-1].incrementNeighborMineCount();
                    cells[row+1][col-1].incrementNeighborMineCount();
                }// top right corner
                else if (row == NUM_ROWS-1 && col == NUM_COLS-1) {
                    cells[row-1][col].incrementNeighborMineCount();
                    cells[row][col-1].incrementNeighborMineCount();
                    cells[row-1][col-1].incrementNeighborMineCount();
                } // bottom right corner
                else if (row > 0 && col==0) {
                    cells[row-1][col].incrementNeighborMineCount();
                    cells[row+1][col].incrementNeighborMineCount();
                    cells[row][col+1].incrementNeighborMineCount();
                    cells[row-1][col+1].incrementNeighborMineCount();
                    cells[row+1][col+1].incrementNeighborMineCount();     
                } // left side
                else if (row > 0 && col==NUM_COLS-1) {
                    cells[row-1][col].incrementNeighborMineCount();
                    cells[row+1][col].incrementNeighborMineCount();
                    cells[row][col-1].incrementNeighborMineCount();
                    cells[row-1][col-1].incrementNeighborMineCount();
                    cells[row+1][col-1].incrementNeighborMineCount();     
                }// right side
                else if (row == NUM_ROWS-1 && col > 0) {
                    cells[row-1][col].incrementNeighborMineCount();
                    cells[row][col-1].incrementNeighborMineCount();
                    cells[row][col+1].incrementNeighborMineCount();
                    cells[row-1][col+1].incrementNeighborMineCount();
                    cells[row-1][col-1].incrementNeighborMineCount();     
                }// bottom side
                else if (row == 0 && col > 0) {
                    cells[row][col-1].incrementNeighborMineCount();
                    cells[row+1][col].incrementNeighborMineCount();
                    cells[row][col+1].incrementNeighborMineCount();
                    cells[row+1][col+1].incrementNeighborMineCount();
                    cells[row+1][col-1].incrementNeighborMineCount();
                }// top side
                else {
                    cells[row-1][col].incrementNeighborMineCount();
                    cells[row+1][col].incrementNeighborMineCount();
                    cells[row][col+1].incrementNeighborMineCount();
                    cells[row-1][col+1].incrementNeighborMineCount();
                    cells[row+1][col+1].incrementNeighborMineCount();
                    cells[row][col-1].incrementNeighborMineCount();
                    cells[row-1][col-1].incrementNeighborMineCount();
                    cells[row+1][col-1].incrementNeighborMineCount();
                } // middle 
            }// end of the first if statement     
        } // while loop       

    } // end of deployMines

    /**
     * Respond to a mouse click. This function will be called each time the user
     * clicks on the game window. The x, y parameters indicate the screen
     * coordinates where the user has clicked, and the button parameters
     * indicates which mouse button was clicked (either "left", "middle", or
     * "right"). The function should update the game state according to what the
     * user has clicked.
     * @param x the x coordinate where the user clicked, in pixels.
     * @param y the y coordinate where the user clicked, in pixels.
     * @param button either "left", "middle", or "right".
     */
    public void mouseClicked(double x, double y, String button) {
        // User clicked the mouse, see what they want to do.

        // If game is over, then ignore the mouse click.
        if (gameWon || gameLost)
            return;

        // If the user middle-clicked, ignore it.
        if (!button.equals("left") && !button.equals("right"))
            return;

        // If the user clicked outside of the game grid, ignore it.
        if (x < MARGIN || y < MARGIN
                || x >= MARGIN + GRID_WIDTH || y >= MARGIN + GRID_HEIGHT) {
            return;
                }

        // Calculate which cell the user clicked.
        int row = (int)((y - MARGIN) / Cell.SIZE);
        int col = (int)((x - MARGIN) / Cell.SIZE);

        // The following code reacts to the left mouse clicks.
        if (button.equals("left")) {
            timer.startCounting();
            if (!cells[row][col].isRevealed()) {
                cells[row][col].reveal();
                numRevealed++;
            }

            if (cells[row][col].isMine) {
                timer.stopCounting();
                for (int r = 0; r < NUM_ROWS; r++) {
                    for (int c = 0; c < NUM_COLS; c++) {
                        cells[r][c].showMine();
                    }
                } 
                gameLost = true;
            }
            else if (getNumCellsRemaining() == numMines) {
                timer.stopCounting();
                gameWon = true;
            }
        } // end of the if statement on left mouse click
        StdOut.printf("You clicked row %d column %d with button %s\n", row, col, button);
    }// end of mouseClicked

    /**
     * Respond to key presses. This function will be called each time the user
     * presses a key. The parameter indicates the character the user pressed.
     * The function should update the game state according to what character the
     * user has pressed. 
     * @param c the character that was typed.
     */
    public void keyTyped(char c) {
        // User pressed a key, see what they want to do.
        switch (c) {
            case 'q': 
            case 'Q': 
                hide(); // user wants to quit
                break;
            default:
                break; // anything else is ignored
        }
    }

    /**
     * Paint the background for this window on the canvas. Don't call this
     * directly, it is called by the GUI system automatically. This function
     * should draw something on the canvas, if you like. Or the background can
     * be blank.
     * @param canvas the canvas on which to draw.
     */
    public void repaintWindowBackground(GUI.Canvas canvas) {  
    }
}
