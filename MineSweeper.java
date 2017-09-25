import objectdraw.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import objectdraw.Location;
import objectdraw.WindowController;

/**
 * This program is a game that display a board which is divided into a grid and “mines” are hidden under
   some grid cells. The player’s task is to determine where all the mines are without causing
   them to explode. If the player clicks on a grid cell and it contains a mine, the mine ex-
   plodes, ending the game. To make the game remotely winnable, when a grid cell is clicked
   on and there is no mine, the cell is labeled with the number of neighboring cells that do
   have a mine. From this, the player can often learn more about the location of mines, and
   place a flag on the locations where mines are believed to exist. The goal of the game is to
   find all the mines without causing any of them to explode.  
 * The Minesweeper class extends WindowController. It is responsible for defining the begin
   method and the event handlers.
 *
 * @author Anh Chau Pham
 * @version April 21st, 2016
   
 */
public class MineSweeper extends WindowController implements ActionListener, MouseListener
{
    //The button that creates a new game
    private JButton newgame;
    
    //The label that displays the number of mines found
    private JLabel minesfound;
    
    //The 2D grids of different levels
    private Grid cellArray = new Grid(canvas);
       
    //The combobox that contains the difficulty levels for the game
    private JComboBox difficultlevel;
   
    //The number of cells flagged
    private int FLAGGED_CELLS= 0;

    /**
     * The main program to create the user interface. The program begins with the grid and the 
       GUI components including the new game button, the label, the panels, the combobox.
     * Add listener to the mouse and the components.
     */
    public void begin() 
    {
        //Constructt the 2D grid
        cellArray = new Grid(canvas);
        
        //Resize the window
        resize (440,580);
        
        //construct the new game button
        newgame = new JButton ("New Game");
        
        //Add the listener to the new game button
        newgame.addActionListener (this);
        
        //Construct the mines found label
        minesfound = new JLabel ("Mines found:");
        
        //Construct the panel of the newgame button
        JPanel buttonpanel = new JPanel();
        
        //Add the new game button to its panel
        buttonpanel.add(newgame);
        
        //construct the panel for the mines found label
        JPanel labelpanel = new JPanel();
        
         //Add the mines found label to its panel
        labelpanel.add(minesfound);
        
        //Construct the difficult levels combo box
        difficultlevel = new JComboBox();
        
        //Add the easy, medium and hard levels into the combo box
        difficultlevel.addItem("easy");
        difficultlevel.addItem("medium");
        difficultlevel.addItem("hard");
        
        //Add the listener to the combo box
        difficultlevel.addActionListener(this);
       
        //construct the panel for the combo box
        JPanel comboboxpanel = new JPanel() ;
        
        //add the combo box to its panel
        comboboxpanel.add(difficultlevel);
        
        
        //construct the common panel
        JPanel commonpanel = new JPanel();
        
        //add the three small panels into the common panels with the vertical orders
        commonpanel.setLayout(new BoxLayout(commonpanel, BoxLayout.Y_AXIS));
        commonpanel.add(buttonpanel);
        commonpanel.add(comboboxpanel);
        commonpanel.add(labelpanel);
   
        //add the common panel to the canvas
        add(commonpanel, BorderLayout.SOUTH);
        
        //display all mines on the grid
        cellArray.createmines();

        //add the mouse listener
        canvas.addMouseListener(this);
        
        //The initial number of cells flagged is zero when the program begins
        FLAGGED_CELLS = 0;
        
        //Set the text for the mines found label
        minesfound.setText ("Mines found:" + FLAGGED_CELLS + "/10");
         
        //Hide all of the mine when the program begins
        cellArray.hidemines();
    }
    
    /**
     * When the user clicks on the new game button, a new game will be constructed with the
       different location of the mines and the grid returns to its initial display.
       The user can choose the difficult levels in the combo box.
     */
    public void actionPerformed (ActionEvent evt)
    {
        Object objectChosen = evt.getSource() ;
        if (objectChosen == newgame ) 
        {
            setlevel();
        }
        else if (objectChosen == difficultlevel) 
        {
            setlevel();
        }
    }
    
    /**
     * Set the level for the difficult level combo box.
     * The harder the level, the bigger the grid and the grid has much more mines.
     */
    public void setlevel()
    {
        String selectedlevel = difficultlevel.getSelectedItem().toString();
        
        //Creat new game for the easy level
        if (selectedlevel.equals("easy") )
        {
            cellArray.newBoard();
            cellArray.createmines();      
            cellArray.hidemines();

            FLAGGED_CELLS = 0;
            minesfound.setText ("Mines found:" + FLAGGED_CELLS + "/10");
            
        }
        //Creat new game for the medium level
        else if (selectedlevel.equals("medium") )
        {
            cellArray.newmediumBoard();
            cellArray.createmoreminesmedium();
            cellArray.hidemines();
                
            FLAGGED_CELLS = 0;
            minesfound.setText ("Mines found:" + FLAGGED_CELLS + "/10");
        }
        //Creat new game for the hard level
        else if (selectedlevel.equals("hard") )
        {
            cellArray.newhardBoard();
            cellArray.createmoremineshard();
            cellArray.hidemines();
                
            FLAGGED_CELLS = 0;
            minesfound.setText ("Mines found:" + FLAGGED_CELLS + "/10"); 
        }
    }
    
    /**
     * Performs different tasks when the user right-clicks or left-clicks on the mouse.
     * When the user left-clicks on the mouse, if the cell doesn't contain a mine,
       the cell will reveal its neighbor counts, if the cell contains a mine, the mine will explode
       and the user loses the game.
     * When the user right-clicks, the cell will be flagged. The user can unflag the cell by
       right-clicking on it one more time.
     * If the number of cells revealed is 90 and the number of cells flagged is 10, the user wins
       the game!
     */
    public void mousePressed(MouseEvent event) 
    {
        //Get the location of the point clicked on
        Location point = new Location (event.getX(), event.getY());
     
        //Happens when the user right-clicks 
        if (event.getButton() == MouseEvent.BUTTON3 || 
        (event.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK)
        {
            //the cell is flagged
            cellArray.setflagged(point);
            
            //if the cell has already been flagged and the mines found is less than 10
            if ( cellArray.alreadyflaggeds()&& FLAGGED_CELLS < 10)
            {
                //the number of cells flagged increments
                FLAGGED_CELLS++;
            } 
            // if the cell has been flagged, unflagging it will decrease the number of cells flagged
            else 
            {
                FLAGGED_CELLS--;
            }
        
            //Set the text for the mines found label
            minesfound.setText ("Mines found:" + FLAGGED_CELLS + "/10");
        }
        else 
        {
            //Happens when the user left-click: if the cell doesn't contain a mine,
            //the cell will reveal its neighbor counts, if the cell contains a mine, the mine will explode
            //and the user loses the game.
            cellArray.countneighbor(point);
        }
        
        //If the mines found is equal to 10 and the cells revealed are 90, the user wins the game
        if (FLAGGED_CELLS == 10 && cellArray.wincounters() ) {
            cellArray.winning();
        }
    }

    //Methods that are part of the MouseListener interface.
    public void mouseClicked(MouseEvent arg0) 
    {
    }

    public void mouseReleased(MouseEvent arg0)
    {
    }

    public void mouseEntered(MouseEvent arg0)
    {
    }

    public void mouseExited(MouseEvent arg0)
    {
    } 
}
 
    


        
        
        
        