import objectdraw.*;
import java.awt.*;

/**
 * A panel that displays a 2D grid of colored cells. This panels contains 10 arrays of 10 arrays 
 * of cells.
 * This class encapsulate the 2D arrat that represents the 2D space of the game.
 * 
 * @author Anh Chau Pham
 * @version April 21st, 2016
 */
public class Grid
{
    // The grid contains GridCells
    private GridCell[][] cell;
    
    //Size of the space of the grid for the easy, medium and hard levels
    private static final int easy_rows = 10;
    private static final int easy_columns = 10;
    private static final int medium_rows = 15;
    private static final int medium_columns = 15;
    private static final int hard_rows = 20;
    private static final int hard_columns = 20;
    
    //Size of a single cell in the grid
    private static final int cellsize = 20;
    
    //Minimum amount of blank space between grid and canvas
    private static final int inset = 10;
    
    // the drawing canvas
    private DrawingCanvas canvas;
    
    // Border around entire grid
    private FilledRect gridoutline;
    
    //The total number of the cells in the grid
    private static final int numCells = 100;
    
    //The random number to get the randomlocation for the mines
    private RandomIntGenerator minegenerator;
    
    //The total quantity of the mines for easy, medium and hard levels
    private static final int easymine_quantity = 10;
    private static final int mediummine_quantity = 50;
    private static final int hardmine_quantity = 120;
    
    //Return true if all of the mines have been displayed
    private boolean minesfound = false;
    
    //Return true if the cell has already been flagged
    private boolean alreadyflagged = false;
    
    //The number of the left-side clicks
    private int clickcounter = 0;
    
    //The number of the neighbors that contain mines
    private int neighborscount;

    /**
     * Create and display a new grid.
     */
    public Grid(DrawingCanvas canvas) 
    {
        this.canvas = canvas;
        gridoutline = new FilledRect (inset, inset, easy_columns*cellsize, easy_rows*cellsize, canvas);
        newBoard();
    }
        
     /**
      * Generate a new grid (Easy level)
      */   
      public void newBoard() 
      {
        //Remove the old grid
        canvas.clear();
        
        //Create the new grid
        gridoutline = new FilledRect (inset, inset, easy_columns*cellsize, easy_rows*cellsize, canvas);
        
        //Construct 10 'rows' and 'columns' of cells
        cell = new GridCell[easy_rows][easy_columns];
            for (int i = 0; i <easy_rows; i++)
            {
                for (int j = 0; j < easy_columns; j++)
                {
                    cell[i][j]  = new GridCell(inset + j*cellsize, inset+ i*cellsize, cellsize, canvas) ;
                }
            }
        //The initial number of left-side click count
        clickcounter = 0;
    }
   
    /**
     *  Add ten mines in random location on the grid
     */
    public void createmines()
    {
        //The random location of the mines including random rows and random columns
        minegenerator = new RandomIntGenerator (0,9);
        
        for( int t = 0; t < easymine_quantity; t++) 
        {
            int a = minegenerator.nextValue();
            int b = minegenerator.nextValue(); 
            //If a cell already has a mine, do not put another mine on top of it.
            if(!cell[a][b].havemine()) 
                {
                    cell[a][b].setmine();
                }
                else if (cell[a][b].havemine())
                {
                    t--;
                }
            }
            //The mines haven't been displayed
            minesfound = false;
    }

    /**
     * Determine which row in the grid the user clicked on
     * @param point the x, y coordinate
     * @return
     */
    private int getRow (Location point) 
    {
        return ((int)(point.getY() - inset)) / cellsize;
    }

    /**
     * Determine which column in the grid the user clicked on
     * @param point the x, y coordinate
     * @return
     */
    private int getCol (Location point) 
    {
        return ((int)(point.getX() - inset)) / cellsize;
    }
    
    /**
     * Performs different tasks if the cell has been left-clicked on.
     */
    public int countneighbor(Location point)  
    {
        //Translate x, y locations to row, col positions
        int row = getRow(point);
        int col = getCol(point);
        
        //The initial number of neighbors count 
        int  counter = 0;
            
        //If the cell has a mine, don't display any number on that cell
        //The mine is exploded, all of the mines are shown and the user loses the game
        if (cell[row][col].havemine()) 
        {
            cell[row][col].setneighbortext(-1);
            explode(point);
        }
            
        //If the cell doesn't have a mine, count its neighbors that have mines
        //The neighbors can be left, right, above, below or diagonal to the cell clicked on.
        
        else 
        {
            for (int i = row -1 ; i <= row+1; i ++)
            {
                for (int j = col-1; j <= col +1; j++)
                {   
                    if(i>=0 && i< 10)
                    {
                        if(j>=0 && j<10)
                        {
                            if(cell[i][j].havemine())
                            {
                                counter++;
                            }           
                        }
                    }             
                }                
            }       
            
            //If the cell has been revealed its neighbors, don't increment the number of clicks
            if (!cell[row][col].revealed()) {
                clickcounter++;
            }
         
            //The cell's color is white when being left-clicked on 
            cell[row][col].turnwhite();  
        
            //The cell reveals its neighbors count when being left-clicked on
            cell[row][col].setneighbortext(counter);  
        }
        //return the number of neighbors count
        return counter;
    }

    /**
     * Translate x, y locations to row, col positions for the explode method.
     */
    public void explode (Location point)
    {
        if(minesfound)
        {
            return;
        }
        if ( gridoutline.contains(point))
        {
            int row = getRow(point);
            int col = getCol(point);
            explode(row, col);
        }
    }
 
    /**
     * Explode when the user clicks on a mine. All of the mines are shown.
     * The clicked cell turn red and a text shown up notifying that the users loses the game.
     */
    public void explode(int row, int col)
    {   
        if (cell[row][col].havemine()) 
        {
            //All of the mines are shown
            showmines();
            minesfound = true;
            
            //The cell turns red and the losing text appears
            cell[row][col].explode();   
        }
    }
     
    /**
     * Flagging the cell by right-clicking on it. 
     * The cell turns blue when it is flagged on. The cell turns to its initial color (gray) by being un-flagged.
     * The user can unflag a cell by right-click again on a flagged cell again.
     */       
    public void setflagged(Location point)
    {
        //Translate x, y locations to row, col positions
        int row = getRow(point);
        int col = getCol(point);
    
        //If the cell has been flagged, right-clicking on it will turn the cell's color back to gray
        if(cell[row][col].getisflagged()) 
        {
            cell[row][col].setisflagged(false);
            cell[row][col].turngray();
            //The cell hasn't been flagged
            alreadyflagged = false;
        }
        //If the cell hasn't been flag, right-clicking on it will turn the cell's color to blue
        else 
        { 
            cell[row][col].setisflagged(true);
            cell[row][col].turnblue();
            //The cell has been flagged
            alreadyflagged = true;
        }
    }

    /**
     * Return true if the cell has been flagged
     */
    public boolean alreadyflaggeds() 
    {
        return alreadyflagged;
    }

    /**
     * Count the number of left-side clicks of the mouse. The user left-clicked on the cell to reveal its neighbor
     * count.
     */
    public boolean wincounters()
    {
        //If the user has already revealed 90 cells that don't have mines. She can win the game if already flagged 10 mines.
        if (clickcounter == 90)
        {
            return true;
        }
        //If the user hasn't revealed 90 cells that don't have mines, she cannot win the game yet.
        else
        {
            return false;
        }
    }

    /**
     * Show all of the mines placed on the grid.
     */
    public void showmines()
    {
        //Check the grid. If the cell has a mine, show it. Show all of the mines.
        for (int i = 0; i < easy_rows; i++)
        {
            for (int j =0; j < easy_columns; j++) 
            {
                if(cell[i][j].havemine())
                {
                    cell[i][j].showmine();
                }
            }
        }
    }

    /**
     * Hide all of the mines placed on the grid.
     */
    public void hidemines()
    {
        //Check the grid. If the cell has a mine, hide it. Hide all of the mines.
        for (int i = 0; i < easy_rows; i++) 
        {
            for (int j =0; j < easy_columns; j++)
            {
                if (cell[i][j].havemine())
                {
                    cell[i][j].hidemine();
                }
            }
        }
    }
    
    /**
     * Happens when the user wins the game. A winning text appears on the canvas.
     */
    public void winning() 
    {
        for (int i = 0; i < easy_rows; i++) {
            for (int j =0; j < easy_columns; j++) 
            {
                cell[i][j].setwintext();
            }
        }
    }

    /**
     * EXTRA CREDIT: Generate a new grid (Medium level)
     */
    public void newmediumBoard()
    {
        canvas.clear();
        gridoutline = new FilledRect (inset, inset, medium_columns*cellsize, medium_rows*cellsize, canvas);   
        cell = new GridCell[medium_rows][medium_columns];
        for (int i = 0; i < medium_rows; i++)
        {
            for (int j = 0; j < medium_columns; j++)
            {
                cell[i][j]  = new GridCell(inset + j*cellsize, inset+ i*cellsize, cellsize, canvas) ;
            }
        }
    }
    
    /**
     * EXTRA CREDIT: Generate a new grid (Hard level)
     */
     public void newhardBoard()
     {
       canvas.clear();
       gridoutline = new FilledRect (inset, inset, hard_columns*cellsize, hard_rows*cellsize, canvas);
       cell = new GridCell[hard_rows][hard_columns];
       for (int i = 0; i < hard_rows; i++)
       {
           for (int j = 0; j < hard_columns; j++)
           {
               cell[i][j]  = new GridCell(inset + j*cellsize, inset+ i*cellsize, cellsize, canvas) ;
            }
        }
    }
   
    /**
     * EXTRA CREDIT: Add fifty mines in random location on the grid (Medium level)
     */
     public void createmoreminesmedium() 
     {
        minegenerator = new RandomIntGenerator (0,14);
       
        for( int t = 0; t < mediummine_quantity; t++)
        {
            int a = minegenerator.nextValue();
            int b = minegenerator.nextValue();
            if(!cell[a][b].havemine())
            {
                cell[a][b].setmine();
            }
        }
        minesfound = false;
    }
   
    /**
    * EXTRA CREDIT: Add ninety mines in random location on the grid (Hard Level)
    */
    public void createmoremineshard()
    {
        minegenerator = new RandomIntGenerator (0,19);
        for( int t = 0; t < hardmine_quantity; t++) 
        {
            int a = minegenerator.nextValue();
            int b = minegenerator.nextValue(); 
            if(!cell[a][b].havemine()) 
            {
              cell[a][b].setmine();
            }      
        }
        minesfound = false;
    }
}

