import java.awt.*;
import objectdraw.*;

/**
 * This class remembers information about an individual cell and manage an individual cell in a maze.
 * 
 * @author Anh Chau Pham
 * @version April 21st, 2016
 */
public class GridCell 
{
    //The depiction of the cell
    private FilledRect cell;
    
    //The depiction of the mine 
    private FilledOval mine;
    
    //The text that appears when the user loses the game
    private Text losetext;
    
    //The text that appears when the user wins the game
    private Text wintext;
    
    //The number of neighbors that contains mines
    private Text neighbortext;
    
    //The x corordinate of the left top corner of the cell
    private int aleft;
    
    //The y corordinate of the left top corner of the cell
    private int atop;
    
    //The size of the cell
    private int acellsize;
    
    //The canvas that contains the cell
    private DrawingCanvas acanvas;

    //Remember if a cell contains a mine
    private boolean hadmine = false; 
    
    //Remember if a cell is flagged
    private boolean isflagged = false;
    
    //Remember if a cell have been left-clicked on to reveal its neighbors
    private boolean turnedwhite = false;

    /**
     *  Creates a new cell at the given location and size on the canvas.
     */
    public GridCell (int left, int top, int cellsize, DrawingCanvas canvas) 
    {   
        //The body of the cell
        cell = new FilledRect (left, top, cellsize, cellsize, canvas);
    
        //Set the cell color to light gray
        cell.setColor(Color.LIGHT_GRAY);
    
        //The outline for the grid
        FramedRect outline = new FramedRect (left, top, cellsize, cellsize, canvas);
    
        //Set the outline color to black
        outline.setColor (Color.BLACK);
   
        //The x corordinate of the left top corner of the cell
        aleft = left;
    
        //The y corordinate of the left top corner of the cell
        atop = top;
    
        //The size of the cell
        acellsize = cellsize;
    
        //The canvas that contains the cell
        acanvas = canvas;
    }

    /**
     * Add the mine into the cell.
     */
    public void setmine() 
    {
        //The size of the mine
        int minesize = 10;
   
        //The body of the mine
        mine = new FilledOval (aleft+acellsize/2-minesize/2, atop+acellsize/2-minesize/2, minesize, minesize,acanvas);
       
        //Remember that this cell have a mine
        hadmine = true;
    }
    
    /**
     * Return true if the cell have a mine in it.
     */
    public boolean havemine()
    {
        return hadmine;
    }

    /**
     * Return true if the flag is gotten.
     */
    public boolean getisflagged() 
    {
        return this.isflagged;
    }

    /**
    * Check whether the cell has been flagged by being right-clicked.
    */
    public void setisflagged (boolean isflagged) 
    {
        this.isflagged = isflagged;
    }

    /**
     * Return true if the cell have been left-clicked on to reveal its neighbors.
     * When this happens, the cell turns to white and the number of its neighbors is displayed.
     */
    public boolean revealed() 
    {
        return turnedwhite;
    }

    /**
     * Display the text that counts the number of neighbors that contain mines.
     */
    public void setneighbortext(int count)
    {
        //If that neighbor contains more than zero mine, display the text
        if (count > 0) 
        {
            //construct the text
            neighbortext = new Text(count,20, 20, acanvas);
    
            //get the width of the text
            double  textsize = neighbortext.getWidth();
    
            //move the text to the center of the cell
            neighbortext.moveTo(aleft+acellsize/2-textsize/2, atop+acellsize/2-textsize/2);   
        }
        //If that neighbor does not contain a mine, don't display anything
        else if (count == 0) 
        {
            //no text displayed
        }
    }

    /**
     * Turn the color of the cell to blue. Happens when the cell is flagged.
     */
    public void turnblue()
    {
        //set the color blue for the cell
        cell.setColor(Color.BLUE);
    }
    
    /**
     * Turn the color of the cell to white. Happens when the neighbor count is displayed in the cell.
     */
    public void turnwhite()
    {
        //set the color white for the cell
        cell.setColor(Color.WHITE);
        
        //The cell has been left-clicked on to reveal its neighbors.
        turnedwhite = true;
    }
    
    /**
     * Turn the color of the cell to gray. This is the initial color of each cell.
     * Happens when the cell is unflagged.
     */
    public void turngray()
    {
        //set the color light gray for the cell
        cell.setColor(Color.LIGHT_GRAY);
    }
    
    /**
     * Display what happen when the user clicks on a mine and that mine explodes.
     * The cell that contain the mine clicked on turns to red. 
     * A text appears notifying the user that she loses the game.
     */
    public void explode() 
    {
        //Set the color red for the clicked cell 
        cell.setColor(Color.RED);
   
        //construct the losing text
        losetext = new Text ("YOU LOSE!", 20, 20, acanvas);
        
        //get the width of the text
        double textsize = losetext.getWidth();
   
        //set the text to bold 
        losetext.setBold();
   
        //set the font size for the text 
        losetext.setFontSize(50);
   
        //set the color green for the text
        losetext.setColor(Color.GREEN);
  
        //move the text to the desired location
        losetext.moveTo( 20, acellsize*3+20);
    }

    /**
     * Display what happen when the user wins the game.
     * A text appears notifying the user that she wins the game.
     */
    public void setwintext() 
    {
        //construct the winning text
        wintext = new Text ("YOU WIN!", 20, 20, acanvas) ;
    
        //get the width of the text
        double textsize = wintext.getWidth();
    
        //set the text to bold 
        wintext.setBold();
    
        //set the font size for the text 
        wintext.setFontSize(50);
    
        //set the color yellow for the text
        wintext.setColor(Color.YELLOW);

        //move the text to the desired location
        wintext.moveTo(20, acellsize*5+20);
    }

    /**
     * Hide the mine
     */
    public void hidemine()
    {
        mine.hide();
    }

    /**
     * Show the mine
     */
    public void showmine() 
    {
        mine.show();
    }
}