/*----------------------------------------------------------------
 *  Author:   K. Walsh
 *  Editor:   Trishala Manandhar
 *  Email:    kwalsh@holycross.edu
 *  Written:  7/13/2015
 *  Updated:  12/7/2020
 *  
 *  Each Cell object manages information about and draws a
 *  single "cell" of the game grid. 
 *----------------------------------------------------------------*/

import GUI.*;
import java.awt.Color;

/**
 * A <i>Cell</i> object holds all information about the state of a single cell
 * of the minesweeper game board. This includes:
 *   - whether a mine is hidden in this cell or not
 *   - how many of its neighboring cells contain mines
 *   - whether it has been revealed yet or is still hidden
 * Each Cell object knows how to draw itself in a graphical window, and it will
 * draw itself in different styles depending on all the above state information.
 */
public class Cell extends Widget {

    /**
     * Size of one cell when it is drawn on the screen, in pixels.
     */
    public static final int SIZE = 20;

    /**
     * Whether a mine is hidden in this cell or not.
     */
    protected boolean isMine;

    /**
     * Whether this cell is "revealed" or not.
     */
    protected boolean isRevealed;

    /**
     * Count of how many neighboring cells have mines.
     */
    protected int neighborMineCount;

    /**
     * Constructor: Initialize a cell to be drawn at the given x, y coordinates
     * on the screen. The cell will be blank. That is, it will not be a mine,
     * and it will have no neighboring mines so a neighbor mine count of zero.
     */
    public Cell(int x, int y) {
        super(x, y, SIZE, SIZE);
        this.isMine = false;
        this.isRevealed = false;
        this.neighborMineCount = 0;
    }

    /**
     * Hide a mine in this cell by changing the isMine variable to true.
     */
    public void plantMine() {
        isMine = true;
    }

    /**
     * Returns true if a mine is hidden in this cell, otherwise returns false.
     */
    public boolean isMine() {
        return isMine;
    }

    /**
     * Increment the neighbor mine count variable by one. 
     */
    public void incrementNeighborMineCount() {
        neighborMineCount++;
    }

    /**
     * Set the neighbor mine count variable to a given value.
     */
    public void setNeighborMineCount(int count) {
        neighborMineCount = count;
    }

    /**
     * Returns the value of the neighbor mine count variable.
     */
    public int getNeighborMineCount() {
        return neighborMineCount;
    }

    /**
     * Change this cell so that it is "revealed" by setting isRevealed to true.
     */
    public void reveal() {
        isRevealed = true;
    }

    /**
     * Returns true if this cell is "revealed", otherwise returns false.
     */
    public boolean isRevealed() {
        return isRevealed;
    }

    /**
     * Hide a mine in this cell by changing the isMine variable to true.
     */
    public void makeMine() {
        isMine = true;
    }

    /**
     * Change this cell so that it shows the mine that is hiding in it.
     */
    public void showMine() {
        if (isMine)
            isRevealed = true;
    }

    /**
     * Check whether there are neighboring mines.
     */
    public boolean coastIsClear() {
        return (neighborMineCount == 0);
    }

    /**
     * Paint this cell on the canvas. Don't call this directly, it is called by
     * the GUI system automatically. This function should draw something on the
     * canvas. Usually the drawing should stay within the bounds (x, y, width,
     * height) which are protected member variables of GUI.Widget, which this
     * class extends.
     * @param canvas the canvas on which to draw.
     */
    public void repaint(GUI.Canvas canvas) {
        // The following two lines draw the background cells for the game.
        canvas.setPenColor(Canvas.GRAY); 
        canvas.raisedBevelRectangle(x,y,SIZE,SIZE,4.0);
        // The following lines changes the cell ones it is revealed
        if (isRevealed) {
            canvas.setPenColor(Canvas.LIGHT_GRAY);
            canvas.sunkenBevelRectangle(x,y,SIZE,SIZE,1.0);
            canvas.square(x,y,SIZE);// This creates lines that divide the cell when revealed
            // The if statement puts a flower as a mine if it is a mine, 
            // The else statement draws the number for the neighborMineCount
            // and it also sets even numbers Red and Odd numbers Green
            if (isMine) {
                canvas.pictureCentered(x+ width/2.0, y + height/2.0, "flower.png", width-3, height-3);
            }  
            else if (neighborMineCount != 0) {
                if (neighborMineCount % 2 == 0) {
                    canvas.setPenColor(Canvas.RED);
                }
                else {
                    canvas.setPenColor(Canvas.GREEN);
                }
                canvas.text(x+width/2.0,y + height/2.0,""+neighborMineCount);
            } // end of else if
        } // end of isRevealed
            
    } // end of repaint   
} // end of Cell
