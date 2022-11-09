    //////////////////////////////////////////////////////////////////////
    //                                                                  //
    //                      MinesweeperJavaGame                         //
    //                   Created by Rohin O'Connor                      //
    //                         Final Project                            //
    //                           CNIT 255                               //
    //                       December 12, 2021                          //
    //                                                                  //
    //////////////////////////////////////////////////////////////////////


/*
Changes made since the first Presentation:

Some edits have been made since the initial slides were made to better replicate a "Minesweeper Look"

Attempting to better replicate something like this: https://i.imgur.com/ZFQPR9a.png 

Aesthetical Changes:
    - Only the clicked mine will be highlighted red
    - Wrong flag "X" changed to red 
    - Replaced "Reset" button to "☻" 
    - Flipped Sides Timer and Bomb Counter were on
    - Modified Timer and Counter with black backgrounds and red numbers
    - Increased Number Size

Gameplay Changes:
    - Added Difficulties with Cell + Bomb variations Easy 9x9: 10 Mines (Default) - Medium 16x16: 40 Mines  - Hard 24x24: 99 Mines


Functionality Changes:
    - Added Menu
        - Added New Game Option
        - Added Statistics Option
        - Fixed Quit Option
        - Added Rules Help
        - Added About Option
    - Added a Proper Game Won/Lost Screen

*/

package minesweeper;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main 
{
    public static void main(String[] args) 
    {
        //--------------------------------Select Difficulty-----------------------------//    
        int difficulty = 0;
        //Difficulty Select
        JFrame difficultySelect = new JFrame();
        difficultySelect.pack();
        
        Object[] options = {"Beginner",
                            "Intermediate",
                            "Expert"};
        
        int difficultyOption = JOptionPane.showOptionDialog(null,
                                    "Choose a Difficulty",
                                    "Select Difficulty",
                                    JOptionPane.YES_NO_CANCEL_OPTION,
                                    JOptionPane.PLAIN_MESSAGE,
                                    null,
                                    options,
                                    null);
        switch(difficultyOption)
        {
            // Beginner Option
            case JOptionPane.YES_OPTION:
                difficulty = 0;
                break;
                
            // Intermediate Option
            case JOptionPane.NO_OPTION:
                difficulty = 1;
                break;
                
            // Expert Option
            case JOptionPane.CANCEL_OPTION:
                difficulty = 2;
                break;
        }       
        //------------------------------------------------------------------------------// 
        
        //Main Game
        Game game = new Game(difficulty);
        
        
    }   
  
}
