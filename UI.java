package minesweeper;


import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;


public class UI extends JFrame
{
    // The buttons
    private JButton[][] buttons;
    private JButton resetButton;
    
    // Number of Buttons in Grid
    private int rows;
    private int cols;
    
    // Labels 
    private JLabel minesLabel;
    private int mines;
    
    private JLabel timePassedLabel;    
    private Thread timer;
    private int timePassed;
    private boolean stopTimer;
    
    // Frame settings
    private int difficulty;
    private final String FRAME_TITLE = "MinesweeperJavaGame - Final Project";
    
    private int FRAME_WIDTH = 500;
    private int FRAME_HEIGHT = 550;
    private int FRAME_LOC_X = 620;
    private int FRAME_LOC_Y = 100;
    
    
    // Menu Bar and Items
    
    private final JMenuBar menuBar;
    private final JMenu game;
    private final JMenu help;
    private final JMenuItem newGame;
    private final JMenuItem statistics;
    private final JMenuItem about;
    private final JMenuItem exit;
    private final JMenuItem rules;
    
    
    
    //---------------------------------------------------------------//
    public UI(int r, int c, int m, int d)
    {                
        this.difficulty = d; 
        
        this.rows = r;
        this.cols = c;
        
        buttons = new JButton [rows][cols];
        
        if (difficulty == 0)
        {
            FRAME_WIDTH = 500;
            FRAME_HEIGHT = 550;
            FRAME_LOC_X = 620;
            FRAME_LOC_Y = 100;
        }
        else if (difficulty == 1)
        {
            FRAME_WIDTH = 700;
            FRAME_HEIGHT = 750;
            FRAME_LOC_X = 620;
            FRAME_LOC_Y = 100;
        }
        
        else if (difficulty == 2)
        {
            FRAME_WIDTH = 900;
            FRAME_HEIGHT = 950;
            FRAME_LOC_X = 620;
            FRAME_LOC_Y = 50;   
        }
        // Set frame
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setTitle(FRAME_TITLE);
        setLocation(FRAME_LOC_X, FRAME_LOC_Y);
               
        // The layout of the frame:

        JPanel gameBoard;        
        JPanel tmPanel;   
        
        
                        
                
        //-------------Time, Reset Button and Mine Counter------------------------//
        
        JPanel timePassedPanel = new JPanel();
        timePassedPanel.setLayout(new BorderLayout(10,0));
        
        // Initialize the time passed label.
        this.timePassedLabel = new JLabel ("  0  " , SwingConstants.CENTER);
        timePassedLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
                
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        
        //Timer Label//
        timePassedLabel.setBorder(loweredetched);
        timePassedLabel.setBackground(Color.black);
        timePassedLabel.setForeground(Color.red);
        timePassedLabel.setOpaque(true);
        
        JLabel iT = new JLabel("Time",SwingConstants.CENTER);
        iT.setFont(new Font("Tahoma", Font.BOLD, 20));

        timePassedPanel.add(iT, BorderLayout.WEST);
        timePassedPanel.add(timePassedLabel, BorderLayout.CENTER);
        timePassedPanel.setOpaque(false);
        
        this.timePassed = 0;
        this.stopTimer = true;

        
        
        //Reset Button//
        
        JPanel resetPanel = new JPanel();
        resetPanel.setPreferredSize(new Dimension(0,70));
              
        this.resetButton = new JButton("☻");
        resetButton.setFont(new Font("Tahoma", Font.BOLD, 50));
        resetButton.setForeground(new Color(255, 255, 0));
        resetButton.setName("resetButton");
        
        resetButton.setBorder(loweredetched);
        resetButton.setBackground(Color.GRAY);
        resetButton.setOpaque(true);
        
        resetPanel.add(resetButton, BorderLayout.CENTER);
        timePassedPanel.setOpaque(false);
        
        //-----------------Mine Counter----------------//
        
        JPanel minesPanel = new JPanel();
        minesPanel.setLayout(new BorderLayout(10,0));
        
        //Mines Left Label//
        this.minesLabel = new JLabel ("  0  " , SwingConstants.CENTER);
        minesLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        minesLabel.setBorder(loweredetched);
        minesLabel.setBackground(Color.black);
        minesLabel.setForeground(Color.red);
        
        minesLabel.setOpaque(true);
        setMines(m);
        
        JLabel mT = new JLabel("*", SwingConstants.CENTER);
        mT.setFont(new Font("Tahoma", Font.BOLD, 40));

        minesPanel.add(minesLabel, BorderLayout.WEST);
        minesPanel.add(mT, BorderLayout.CENTER);
        minesPanel.setOpaque(false);
        
        // Build the "tmPanel"
        tmPanel = new JPanel();
        tmPanel.setLayout(new BorderLayout(0,20));
        
        //Add Timer
        tmPanel.add(timePassedPanel, BorderLayout.EAST);
        //Add Mine Counter
        tmPanel.add(minesPanel, BorderLayout.WEST);
        //Add Reset Button
        tmPanel.add(resetPanel, BorderLayout.CENTER);
        tmPanel.setOpaque(false);
        
        //--------------------------------------------//
        
        
        
        
        //----------------Game Board---------------------//
        // Build the "gameBoard".
        gameBoard = new JPanel();
        gameBoard.setLayout(new GridLayout(rows,cols,0,0));
        
        for( int y=0 ; y<rows ; y++ ) 
        {
            for( int x=0 ; x<cols ; x++ ) 
            {
                // Set button text.
                buttons[x][y] = new JButton("");

                // Set button name (x,y).
                buttons[x][y].setName(Integer.toString(x) + "," + Integer.toString(y));
                //buttons[x][y].setFont(new Font("Tahoma", Font.BOLD, 24));
                
                buttons[x][y].setBorder(BorderFactory.createLineBorder(Color.black, 1, true));

                // Add this button to the gameboard.
                gameBoard.add(buttons[x][y]);
            }
        }
        //------------------------------------------------//
        
        //------------------Menu--------------------------//
        
        menuBar = new JMenuBar();
        
        //------------------Option Menu-------------------//
        
        game = new JMenu("Game");

        newGame = new JMenuItem("New Game");
        statistics = new JMenuItem("Statistics");
        exit = new JMenuItem("Exit");

        newGame.setName("New Game");
        statistics.setName("Statistics");
        exit.setName("Exit");

        game.add(newGame);
        game.add(statistics);
        game.add(exit);
        
        //---------------------Help Menu-------------------//
        
        help = new JMenu("Help");
        
        rules = new JMenuItem("Rules");
        about = new JMenuItem("About");
        
        rules.setName("Rules");
        about.setName("About");
        
        help.add(rules);
        help.add(about);
        
        //-------------------------------------------------//
        
        menuBar.add(game);  
        menuBar.add(help);
        
        
        //-------------------------------------------------//
               
        //Places Items on the JFrame
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout(0,10));
        p.add(gameBoard, BorderLayout.CENTER);
        p.add(tmPanel, BorderLayout.NORTH);
    
        p.setOpaque(false);  
        
        setLayout(new BorderLayout());
        JLabel background = new JLabel();
        
        add(background);        
        
        background.setLayout(new BorderLayout(0,0));
        
        background.add(menuBar,BorderLayout.NORTH);
        background.add(p, BorderLayout.CENTER);        
               
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
	

    //---------Timer-----------//
    
    // Starts the timer
    // https://www.codegrepper.com/code-examples/java/sleep+timer+java
    //
    public void startTimer()
    {        
        stopTimer = false;
        
        timer = new Thread() {
                @Override
                public void run()
                {
                    while(!stopTimer)
                    {
                        timePassed++;

                        // Update the time passed label.
                        timePassedLabel.setText("  " + timePassed + "  ");

                        // Wait 1 second.
                        try{
                            sleep(1000); 
                        }
                        catch(InterruptedException ex){}
                    }
                }
        };                

       timer.start();
    }

    
    public void interruptTimer()
    {
        stopTimer = true;
                
        try 
        {
            if (timer!= null)
                timer.join();
        } 
        catch (InterruptedException ex) 
        {

        }        
    }
    
    public void resetTimer()
    {
        timePassed = 0;
        timePassedLabel.setText("  " + timePassed + "  ");        
    }

    public void setTimePassed(int t)
    {
        timePassed = t;
        timePassedLabel.setText("  " + timePassed + "  ");                
    }
    
    //-----------------------------------------------------------//
    
    
    public void initGame()
    {
        hideAll();
        enableAll();
    }
    
    //------------------Helper Functions-----------------------//

    //Makes buttons clickable
    public void enableAll()
    {
        for( int x=0 ; x<cols ; x++ ) 
        {
            for( int y=0 ; y<rows ; y++ ) 
            {
                buttons[x][y].setEnabled(true);
            }
        }
    }

    //Makes buttons non-clickable
    public void disableAll()
    {
        for( int x=0 ; x<cols ; x++ ) 
        {
            for( int y=0 ; y<rows ; y++ ) 
            {
                buttons[x][y].setEnabled(false);
            }
        }
    }


    //Resets the content of all buttons
    public void hideAll()
    {
        for( int x=0 ; x<cols ; x++ ) 
        {
            for( int y=0 ; y<rows ; y++ ) 
            {
                buttons[x][y].setText("");                
                buttons[x][y].setBackground(new Color(150, 159, 168));               
            }
        }
    }

    
    //---------------Set Listeners--------------------------//
    
    public void setButtonListeners(Game game)
    {
        addWindowListener(game);
        
    
        // Set listeners for all buttons in the grid in gameBoard
        for( int x=0 ; x<cols ; x++ ) 
        {
            for( int y=0 ; y<rows ; y++ ) 
            {
                buttons[x][y].addMouseListener(game);
            }
        }
       
       //Set listeners for Smiley Reset Button
        resetButton.addMouseListener(game);
        
        // Set listeners for menu items in menu bar
        newGame.addActionListener(game);
        statistics.addActionListener(game);
        exit.addActionListener(game);
        rules.addActionListener(game);
        about.addActionListener(game);

    }
  
    
    //-----------------Getters and Setters--------------------//
    
    public JButton[][] getButtons()
    {
        return buttons;
    }
    
    public int getTimePassed()
    {
        return timePassed;
    }   

    

    //----------------------SET LOOK------------------------------//
    // https://docs.oracle.com/javase/7/docs/api/javax/swing/UIManager.html / https://www.geeksforgeeks.org/java-swing-look-feel/
    public static void setLook(String look)
    {
        try {

            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (look.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            
        } catch (Exception ex) { }            
    }

    //-------------------------------------------------------------//
    
    public void setMines(int m)
    {
        mines = m;
        minesLabel.setText("  " + Integer.toString(m) + "  ");
    }
    
    public void incMines()
    {
        mines++;
        setMines(mines);
    }
    
    public void decMines()
    {
        mines--;
        setMines(mines);
    }
    
    public int getMines()
    {
        return mines;
    }
    
    //--------------Add color to number on the squares---------------------//
    public void setTextColor(JButton b)
    {
        switch (b.getText()) {
            case "1":
                b.setFont(new Font("Tahoma", Font.PLAIN, 25));
                b.setForeground(new Color(0,0,255));
                break;
            case "2":
                b.setFont(new Font("Tahoma", Font.PLAIN, 25));
                b.setForeground(new Color(76,153,0));
                break;
            case "3":
                b.setFont(new Font("Tahoma", Font.PLAIN, 25));
                b.setForeground(new Color(255,0,0));
                break;
            case "4":
                b.setFont(new Font("Tahoma", Font.PLAIN, 25));
                b.setForeground(new Color(153,0,0));
                break;
            case "5":
                b.setFont(new Font("Tahoma", Font.PLAIN, 25));
                b.setForeground(new Color(153,0,153));
                break;        
            case "6":
                b.setFont(new Font("Tahoma", Font.PLAIN, 25));
                b.setForeground(new Color(96,96,96));
                break;
            case "7":
                b.setFont(new Font("Tahoma", Font.PLAIN, 25));
                b.setForeground(new Color(0,0,102));
                break;
            case "8":
                b.setFont(new Font("Tahoma", Font.PLAIN, 25));
                b.setForeground(new Color(153,0,76));
                break;
            default:
                break;
        }
    }
}