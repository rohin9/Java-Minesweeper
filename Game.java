package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.sql.Date;
import javax.swing.border.TitledBorder;
import minesweeper.Score.Time;


// This is the main controller class
public class Game implements MouseListener, ActionListener, WindowListener
{
    
    // "playing" indicates whether a game is running (true) or not (false).
    private boolean playing; 

    private Board board;

    private UI gui;
    
    private Score score;
    
    private int difficulty;
        
    //------------------------------------------------------------------//        
    
    
    public Game(int difficulty)
    {
        
        this.difficulty = difficulty;
        
        score = new Score();
        
        //https://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/nimbus.html
        UI.setLook("Nimbus");
                        
        createBoard();
        
        this.gui = new UI(board.getRows(), board.getCols(), board.getNumberOfMines(), this.difficulty);        
        this.gui.setButtonListeners(this);
        
        this.playing = false;
        
        gui.setVisible(true);
               
        gui.hideAll();     
    }
    //-------------------------------------------------//
    
    public int getDifficulty()
    {
        return this.difficulty;
    }        
    
    //-------------------------------------------------//
    public void setButtons()
    {
        Cell cells[][] = board.getCells();
        JButton buttons[][] = gui.getButtons();
        
                
        for( int y=0 ; y<board.getRows() ; y++ ) 
        {
            for( int x=0 ; x<board.getCols() ; x++ ) 
            {
                
                if (cells[x][y].getContent().equals("")){
                    buttons[x][y].setBackground(Color.lightGray);
                }
                if (cells[x][y].getContent().equals("F"))
                {
                    buttons[x][y].setText("!");
                    buttons[x][y].setBackground(Color.darkGray);               
                }
                else if (cells[x][y].getContent().equals("0"))
                {
                    buttons[x][y].setBackground(Color.lightGray);
                }
                else
                {
                    buttons[x][y].setBackground(Color.red);                    
                    buttons[x][y].setText(cells[x][y].getContent());
                    gui.setTextColor(buttons[x][y]);                                        
                }
            }
        } 
    }
    
    
    //------------------------------------------------------------------------------//
       
    public void createBoard()
    {
        
        //---------------------------//
        //---------Issue-------------//
        if (difficulty == 0)
        {
            int mines = 10;

            int r = 9;
            int c = 9;
            this.board = new Board(mines, r, c); 
        }
        else if (difficulty == 1)
        {
            int mines = 40;

            int r = 16;
            int c = 16;
            this.board = new Board(mines, r, c); 
        }
        else if (difficulty == 2)
        {
            int mines = 99;

            int r = 24;
            int c = 24;
            
            this.board = new Board(mines, r, c); 
        }
        //------------------------------//
        
//        int mines = 10;
//
//        int r = 9;
//        int c = 9;
//                
//        this.board = new Board(mines, r, c);        
    }
    
    

    //---------------------------------------------------------------//
    public void newGame()
    {                
        this.playing = false;        
                                
        createBoard();
        
        gui.interruptTimer();
        gui.resetTimer();        
        gui.initGame();
        gui.setMines(board.getNumberOfMines());
    }
    //------------------------------------------------------------------------------//
    
    //------------------------------------------------------------------------------//    
    private void endGame()
    {
        playing = false;
        showAll();
    }

    
    //-------------------------- Game Won and Lost ---------------------------------//
    
    public void gameWon()
    {
        score.incCurrentStreak();
        score.incCurrentWinningStreak();
        score.incGamesWon();
        score.incGamesPlayed();
        
        gui.interruptTimer();
        endGame();
        //----------------------------------------------------------------//
        
        
        JDialog dialog = new JDialog(gui, Dialog.ModalityType.DOCUMENT_MODAL);
        
        //-------- Win Message -----------//
        JLabel message = new JLabel("Congratulations, You Win!", SwingConstants.CENTER);
                
        //----------Statistics-----------//
        JPanel statistics = new JPanel();
        statistics.setLayout(new GridLayout(6,1,0,10));
        
        ArrayList<Time> bTimes = score.getBestTimes();
        
        if (bTimes.isEmpty() || (bTimes.get(0).getTimeValue() > gui.getTimePassed()))
        {
            statistics.add(new JLabel("    You have the fastest time!    "));
        }
        
        score.addTime(gui.getTimePassed(), new Date(System.currentTimeMillis()));
                
        JLabel time = new JLabel("Time:  " + Integer.toString(gui.getTimePassed()) + " seconds            Date:  " + new Date(System.currentTimeMillis()));
        
        JLabel bestTime = new JLabel();
        
        
        if (bTimes.isEmpty())
        {
            bestTime.setText("Best Time:  ---                  Date:  ---");
        }
        else
        {
            bestTime.setText("Best Time: " + bTimes.get(0).getTimeValue() + " seconds            Date:  " + bTimes.get(0).getDateValue());
        }
        
        JLabel gPlayed = new JLabel("Games Played: " + score.getGamesPlayed());
        JLabel gWon = new JLabel("Games Won: " + score.getGamesWon());
        JLabel gPercentage = new JLabel("Win Percentage: " + score.getWinPercentage() + "%");
        
        statistics.add(time);
        statistics.add(bestTime);
        statistics.add(gPlayed);
        statistics.add(gWon);
        statistics.add(gPercentage);
        
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);        
        statistics.setBorder(loweredetched);
        
        
        //--------Buttons----------//
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1,2,10,0));
        
        JButton exit = new JButton("Exit");
        JButton playAgain = new JButton("Play Again");

        
        exit.addActionListener((ActionEvent e) -> {
            dialog.dispose();
            windowClosing(null);
        });        
        playAgain.addActionListener((ActionEvent e) -> {
            dialog.dispose();            
            newGame();
        });        
        
        
        buttons.add(exit);
        buttons.add(playAgain);
        
        //--------Dialog-------------//
        
        JPanel c = new JPanel();
        c.setLayout(new BorderLayout(20,20));
        c.add(message, BorderLayout.NORTH);
        c.add(statistics, BorderLayout.CENTER);
        c.add(buttons, BorderLayout.SOUTH);
        
        c.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    dialog.dispose();
                    newGame();
            }
            }
        );

        dialog.setTitle("Game Won!");
        dialog.add(c);
        dialog.pack();
        dialog.setLocationRelativeTo(gui);
        dialog.setVisible(true);                        
    }
    
    public void gameLost()
    {
        score.decCurrentStreak();
        score.incCurrentLosingStreak();
        score.incGamesPlayed();
        
        gui.interruptTimer();
        
        endGame();
        
        //----------------------------------------------------------------//

        JDialog dialog = new JDialog(gui, Dialog.ModalityType.DOCUMENT_MODAL);
        
        //--------Losing Message---------//
        JLabel message = new JLabel("You lost. Play Again?", SwingConstants.CENTER);
                
        //---------Statistics-----------//
        JPanel statistics = new JPanel();
        statistics.setLayout(new GridLayout(5,1,0,10));
        
        JLabel time = new JLabel("  Time:  " + Integer.toString(gui.getTimePassed()) + " seconds");
        
        JLabel bestTime = new JLabel();
        
        ArrayList<Time> bTimes = score.getBestTimes();
        
        if (bTimes.isEmpty())
        {
            bestTime.setText("                        ");
        }
        else
        {
            bestTime.setText("  Best Time:  " + bTimes.get(0).getTimeValue() + " seconds            Date:  " + bTimes.get(0).getDateValue());
        }
        
        JLabel gPlayed = new JLabel("  Games Played:  " + score.getGamesPlayed());
        JLabel gWon = new JLabel("  Games Won:  " + score.getGamesWon());
        JLabel gPercentage = new JLabel("  Win Percentage:  " + score.getWinPercentage() + "%");
        
        statistics.add(time);
        statistics.add(bestTime);
        statistics.add(gPlayed);
        statistics.add(gWon);
        statistics.add(gPercentage);
        
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);        
        statistics.setBorder(loweredetched);
        
        
        //--------Buttons----------//
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1,3,2,0));
        
        JButton exit = new JButton("Exit");
        JButton playAgain = new JButton("Play Again");

        
        exit.addActionListener((ActionEvent e) -> {
            dialog.dispose();
            windowClosing(null);
        }); 
               
        playAgain.addActionListener((ActionEvent e) -> {
            dialog.dispose();            
            newGame();
        });        
        
        
        buttons.add(exit);
        buttons.add(playAgain);
        
        //--------Dialog-------------//
        
        JPanel c = new JPanel();
        c.setLayout(new BorderLayout(20,20));
        c.add(message, BorderLayout.NORTH);
        c.add(statistics, BorderLayout.CENTER);
        c.add(buttons, BorderLayout.SOUTH);
        
        c.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                dialog.dispose();
                newGame();
            }
            }
        );
        
        dialog.setTitle("Game Over");
        dialog.add(c);
        dialog.pack();
        dialog.setLocationRelativeTo(gui);
        dialog.setVisible(true);        
    }
    
    
    //--------------------------------Score Board--------------------------------------//
    public void showScore()
    {
        //----------------------------------------------------------------//
                
        JDialog dialog = new JDialog(gui, Dialog.ModalityType.DOCUMENT_MODAL);

        //-----Best Time--------//
        
        JPanel bestTimes = new JPanel();
        bestTimes.setLayout(new GridLayout(5,1));
        
        ArrayList<Time> bTimes = score.getBestTimes();
        
        for (int i = 0; i < bTimes.size(); i++)
        {
            JLabel t = new JLabel("  " + bTimes.get(i).getTimeValue() + "           " + bTimes.get(i).getDateValue());            
            bestTimes.add(t);
        }
        
        if (bTimes.isEmpty())
        {
            JLabel t = new JLabel("                               ");            
            bestTimes.add(t);
        }
        
        TitledBorder b = BorderFactory.createTitledBorder("Best Times");
        b.setTitleJustification(TitledBorder.LEFT);

        bestTimes.setBorder(b);
                
        //-----Statistics-----------//
        JPanel statistics = new JPanel();
        
        statistics.setLayout(new GridLayout(6,1,0,10));        
        
        JLabel gPlayed = new JLabel("  Games Played:  " + score.getGamesPlayed());
        JLabel gWon = new JLabel("  Games Won:  " + score.getGamesWon());
        JLabel gPercentage = new JLabel("  Win Percentage:  " + score.getWinPercentage() + "%");
        JLabel lWin = new JLabel("  Longest Winning Streak:  " + score.getLongestWinningStreak());
        JLabel lLose = new JLabel("  Longest Losing Streak:  " + score.getLongestLosingStreak());
        JLabel currentStreak = new JLabel("  Current Streak:  " + score.getCurrentStreak());

        
        statistics.add(gPlayed);
        statistics.add(gWon);
        statistics.add(gPercentage);
        statistics.add(lWin);
        statistics.add(lLose);
        statistics.add(currentStreak);
                        
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);        
        statistics.setBorder(loweredetched);
        
        
        //--------Buttons----------//
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1,2,10,0));
        
        JButton close = new JButton("Close");
        JButton reset = new JButton("Reset");

        
        close.addActionListener((ActionEvent e) -> {
            dialog.dispose();
        });        
        reset.addActionListener((ActionEvent e) -> {

            int option = JOptionPane.showConfirmDialog(null, "Do you want to reset all your statistics to zero?", 
                            "Reset Statistics", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            switch(option) 
            {
                case JOptionPane.YES_OPTION:      

                    score.resetScore();
                    dialog.dispose();
                    showScore();
                    break;

                case JOptionPane.NO_OPTION: 
                    break;
            }
        });        
        
        buttons.add(close);
        buttons.add(reset);
        
        if (score.getGamesPlayed() == 0)
            reset.setEnabled(false);
        
        //--------Dialog-------------//
        
        JPanel c = new JPanel();
        c.setLayout(new BorderLayout(20,20));
        c.add(bestTimes, BorderLayout.WEST);
        c.add(statistics, BorderLayout.CENTER);        
        c.add(buttons, BorderLayout.SOUTH);
        
        c.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        dialog.setTitle("Statistics");
        dialog.add(c);
        dialog.pack();
        dialog.setLocationRelativeTo(gui);
        dialog.setVisible(true);                        
    }
    
    //------------------------------------------------------------------------------//
    //------------------------------Rules for Minesweeper---------------------------//
    public void showRules()
    {
        JFrame rules = new JFrame(); 
        rules.pack();
        //Long Message Box to show Minesweeper Rules
        JOptionPane.showMessageDialog(rules,"Here are the rules for the game Minesweeper: \n"
            + "Section From: https://info.lite.games/en/support/solutions/articles/60000688724-minesweeper-world-rules \n\n"
            + "Goal of the Game:\nIn order to win, reveal all the 'safe' squares (Squares with numbers) on the board "
            + "without exploding a mine. \nFor a bigger challenge try to uncover every square as fast as possible."
            + "\n\nHow to Play:\nLeft Click on any unrevealed square to start the game. The timer starts "
            + "once the first square has been revealed.\nNumbers on the squares indicate the number of surrounding mines "
            + " (this includes all 8 squares surrounding \nit in a 3×3 grid). Based on these numbers and how their 3×3 grids overlap,"
            + " you can identify or suspect under which \nsquares mines are hidden. Numbers range from 1 (1 mine nearby) to 8 "
            + "(8 mines nearby).\n\nPlace a flag by right clicking "
            + "the square to mark it as dangerous. Right click again to remove the flag. Reveal \nall safe squares "
            + "without mines to win the game.\n\nThe timer is in the top right, and the number of mines remaining is in the top left."
            + "\nClick the smiley face to restart, or use the menu options.\nThis program also keeps track of your stats in "
            + "your current session which can be found under Option>Statistics.\n\nLegend:\n! = Flag\n* = Mine\nX = "
            + "Incorrectly Marked Mine\n", "Rules", JOptionPane.INFORMATION_MESSAGE);
    }
    
    //------------------------------------------------------------------------------//
    //--------------------------------------About-----------------------------------//
    public void showAbout()
    {
        JFrame about = new JFrame(); 
        about.pack();
        JOptionPane.showMessageDialog(about,"Minesweeper Clone Created by Rohin O'Connor", "About", JOptionPane.INFORMATION_MESSAGE);
    }
    //------------------------------------------------------------------------------//
    
    //------------------------ Shows the "solution" of the game --------------------//
    private void showAll()
    {
        String cellSolution;
        
        Cell cells[][] = board.getCells();
        JButton buttons[][] = gui.getButtons();

        for (int x=0; x<board.getCols(); x++ ) 
        {
            for (int y=0; y<board.getRows(); y++ ) 
            {
                cellSolution = cells[x][y].getContent();

                // Is the cell still unrevealed
                if( cellSolution.equals("") ) 
                {
                    
                    // Get Neighbors
                    cellSolution = Integer.toString(cells[x][y].getSurroundingMines());

                    // Check for Mine
                    if(cells[x][y].getMine()) 
                    {
                        cellSolution = "M";
                        
                        //Mine Properties
                        buttons[x][y].setText("*");
                        buttons[x][y].setFont(new Font("Tahoma", Font.BOLD, 35));
                        buttons[x][y].setForeground(Color.black);
                        
                        //Modified so only clicked mine turns red
                        buttons[x][y].setBackground(Color.lightGray);   
                                                 
                    }
                    else
                    {
                        if(cellSolution.equals("0"))
                        {
                            buttons[x][y].setText("");                           
                            buttons[x][y].setBackground(Color.lightGray);
                        }
                        else
                        {
                            buttons[x][y].setBackground(Color.lightGray);
                            buttons[x][y].setText(cellSolution);
                            gui.setTextColor(buttons[x][y]);
                        }
                    }
                }

                // If cell is already flagged
                else if( cellSolution.equals("F") ) 
                {
                    // Checks if Box is correctly flagged
                    if(!cells[x][y].getMine()) 
                    {
                        //Mark with "X" to represent miss
                        buttons[x][y].setText("X");
                        buttons[x][y].setFont(new Font("Tahoma", Font.BOLD, 20));
                        buttons[x][y].setForeground(Color.red);
                        buttons[x][y].setBackground(Color.lightGray);
                        
                    }
                    //Correctly flagged
                    else
                    {
                        buttons[x][y].setText("*");
                        buttons[x][y].setFont(new Font("Tahoma", Font.BOLD, 35));
                        buttons[x][y].setForeground(Color.black);
                        buttons[x][y].setBackground(new Color(35, 171, 32));
                    }
                }
                
            }
        }
    }
    
    //--------------------------------------------------------------------------//
    
    public boolean isFinished()
    {
        boolean isFinished = true;
        String cellSolution;

        Cell cells[][] = board.getCells();
        
        for( int x = 0 ; x < board.getCols() ; x++ ) 
        {
            for( int y = 0 ; y < board.getRows() ; y++ ) 
            {
                // If a game is solved, the content of each Cell should match the value of its surrounding mines
                cellSolution = Integer.toString(cells[x][y].getSurroundingMines());
                
                if(cells[x][y].getMine()) 
                    cellSolution = "F";

                // Compare the player's "answer" to the solution.
                if(!cells[x][y].getContent().equals(cellSolution))
                {
                    //This cell is not solved yet
                    isFinished = false;
                    break;
                }
            }
        }

        return isFinished;
    }

    //-----------------------------------------------------------------------//
    //Check the game to see if its finished or not
    private void checkGame()
    {		
        if(isFinished()) 
        {            
            gameWon();
        }
    }
   
    //-----------------------------------------------------------------------//
       
    
    /*
     * If a player clicks on a zero, all surrounding cells ("neighbors") must revealed.
     * This method is recursive: if a neighbor is also a zero, its neighbors will also be revealed.
     * Help from https://www.geeksforgeeks.org/cpp-implementation-minesweeper-game/ with 
     * better description.
     */
    public void findZeroes(int xCo, int yCo)
    {
        int neighbors;
        
        Cell cells[][] = board.getCells();
        JButton buttons[][] = gui.getButtons();

        // Columns
        for(int x = board.makeValidCoordinateX(xCo - 1) ; x <= board.makeValidCoordinateX(xCo + 1) ; x++) 
        {			
            // Rows
            for(int y = board.makeValidCoordinateY(yCo - 1) ; y <= board.makeValidCoordinateY(yCo + 1) ; y++) 
            {
                // Only unrevealed cells need to be revealed.
                if(cells[x][y].getContent().equals("")) 
                {
                    // Get the neighbors of the current (neighboring) cell.
                    neighbors = cells[x][y].getSurroundingMines();

                    // Reveal the neighbors of the current (neighboring) cell
                    cells[x][y].setContent(Integer.toString(neighbors));

                    if (!cells[x][y].getMine())                        
                    
                    // Is this (neighboring) cell a "zero" cell itself?
                    if(neighbors == 0)
                    {                        
                        // Yes
                        buttons[x][y].setBackground(Color.lightGray);
                        buttons[x][y].setText("");
                        findZeroes(x, y);
                    }
                    else
                    {
                        // No
                        buttons[x][y].setBackground(Color.lightGray);
                        buttons[x][y].setText(Integer.toString(neighbors));
                        gui.setTextColor(buttons[x][y]);                        
                    }
                }
            }
        }
    }
    //-----------------------------------------------------------------------------//
    
    //Quit Controller
    @Override
    public void windowClosing(WindowEvent e) 
    {
        //If the user has started playing
        if (playing)
        {

            int quit = JOptionPane.showConfirmDialog(null, "Are you sure you want to Quit?", 
                            "Quit Game?", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
            

            //Deals with Quit Prompt
            switch(quit) 
            {
                //Yes Option
                case JOptionPane.YES_OPTION:
                    
                    gui.interruptTimer();
                    
                    JDialog dialog = new JDialog(gui, Dialog.ModalityType.DOCUMENT_MODAL);               
                    dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
                    
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>(){
                       @Override
                       protected Void doInBackground() throws Exception 
                       {               
                            return null;
                       }
                       
                       @Override
                       protected void done(){
                           dialog.dispose();                           
                       }                       
                    };
                            
                    worker.execute();
                    dialog.setVisible(true);
                                                           
                    System.exit(0);
                    break;
                
                //No Option           
                case JOptionPane.NO_OPTION:
                    break;      
            }
        }
        //If the user hasn't started playing yet
        else
        { 
            System.exit(0);
        }
    }  
    //-----------------------------------------------------------------------//
    
    //Menu Controller//
    
    @Override
    public void actionPerformed(ActionEvent e) {        
        JMenuItem menuItem = (JMenuItem) e.getSource();
                
        
        //If the user clicks "New Game"
        if (menuItem.getName().equals("New Game"))
        {
            if (playing)
            {
                
                int startNew = JOptionPane.showConfirmDialog(null, "Start a New Game?", 
                                "New Game?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
                //New Game Dialog Options
                switch(startNew) 
                {
                    //Start a New Game
                    case JOptionPane.YES_OPTION:      
                        
                        // Initialize the new game.
                        newGame();
                        score.incGamesPlayed();
                        break;
                    
                    //Resume
                    case JOptionPane.NO_OPTION: break;
                }
            }
        }
        //If the user clicks "Exit"
        else if (menuItem.getName().equals("Exit"))
        {
            windowClosing(null);
        }
        
        //If the user clicks "Statistics"
        else if (menuItem.getName().equals("Statistics"))
        {
            showScore();
        }    
        //If the user clicks "Rules"
        else if (menuItem.getName().equals("Rules"))
        {
            showRules();
        }
        //If the user clicks "About"
        else if (menuItem.getName().equals("About"))
        {
            showAbout();
        }

    }
    
    
    //--------------------------------------------------------------------------//
    
    //Mouse Click Listener -- https://docs.oracle.com/javase/tutorial/uiswing/events/mouselistener.html --
    @Override
    public void mouseClicked(MouseEvent e)
    {
        // Start timer on first click
        if(!playing)
        {
            gui.startTimer();
            playing = true;
        }
        
        if (playing)
        {   
            //On Click for Smiley Reset Button
            JButton resetButton = (JButton)e.getSource();
            
            if (resetButton.getName().equals("resetButton"))
            {
                score.incGamesPlayed();
                newGame();
            }
            //---------------------------------
            
            //Get the button's name
            JButton button = (JButton)e.getSource();
            
            //Try for Error for Smiley Face reset Button
            try
            {
                //------------- Mine Grid ---------------//
                
                //Get coordinates (button.getName().equals("x,y")).
                String[] co = button.getName().split(",");

                int x = Integer.parseInt(co[0]);
                int y = Integer.parseInt(co[1]);

                // Get cell information.
                boolean isMine = board.getCells()[x][y].getMine();
                int neighbors = board.getCells()[x][y].getSurroundingMines();




                // Left Click
                if (SwingUtilities.isLeftMouseButton(e)) 
                {
                    if (!board.getCells()[x][y].getContent().equals("F"))
                    {

                        //Mine is clicked.
                        if(isMine) 
                        {  
                            //clicked mine
                            button.setText("*");
                            button.setFont(new Font("Tahoma", Font.BOLD, 35));
                            button.setForeground(Color.black);
                            button.setBackground(Color.red);

                            board.getCells()[x][y].setContent("M");

                            gameLost();
                        }
                        else 
                        {
                            // The player has clicked on a number.
                            board.getCells()[x][y].setContent(Integer.toString(neighbors));
                            button.setText(Integer.toString(neighbors));
                            gui.setTextColor(button);

                            if( neighbors == 0 ) 
                            {
                                // Show all surrounding cells.
                                button.setBackground(Color.lightGray);
                                button.setText("");
                                findZeroes(x, y);
                            } 
                            else 
                            {
                                button.setBackground(Color.lightGray);
                            }
                        }
                    }
                }
                // Right Click
                else if (SwingUtilities.isRightMouseButton(e)) 
                {
                    if(board.getCells()[x][y].getContent().equals("F")) 
                    {   
                        board.getCells()[x][y].setContent("");
                        button.setText("");
                        button.setBackground(Color.lightGray);

                        gui.incMines();
                    }
                    else if (board.getCells()[x][y].getContent().equals("")) 
                    {
                        board.getCells()[x][y].setContent("F");
                        button.setBackground(Color.lightGray);	

                        button.setText("!");
                        button.setFont(new Font("Tahoma", Font.BOLD, 20));
                        button.setForeground(Color.black);

                        gui.decMines();
                    }
                }

                checkGame();
            } catch (NumberFormatException t){
                
            }
        }
    }


    
    //--------------------- Empty Functions -------------------------------//
    
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }    

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }
    
    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}