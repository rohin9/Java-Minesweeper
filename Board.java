package minesweeper;

public class Board 
{
    private int numberOfMines;	
    private final Cell cells[][];

    private final int rows;
    private final int cols;

        
    //---------------------------------------------//
    
    public Board(int numberOfMines, int r, int c)
    {
        this.rows = r;
        this.cols = c;
        this.numberOfMines = numberOfMines;

        cells = new Cell[rows][cols];

        //Step 1: First create a board with empty Cells
        createEmptyCells();         

        //Step 2: Then set mines randomly at cells
        setMines();

        //Step 3: Then set the number of surrounding mines("neighbors") at each cell
        setSurroundingMinesNumber();
    }


    //------------------------------------------------------------------//
    //Step 1//
    public void createEmptyCells()
    {
        for (int x = 0; x < cols; x++)
        {
            for (int y = 0; y < rows; y++)
            {
                cells[x][y] = new Cell();
            }
        }
    }

    //------------------------------------------------------------------//
    //Step 2//
    public void setMines()
    {
        int x,y;
        boolean hasMine;
        int currentMines = 0;                

        while (currentMines != numberOfMines)
        {
            // Generate a random x coordinate (between 0 and cols)
            x = (int)Math.floor(Math.random() * cols);

            // Generate a random y coordinate (between 0 and rows)
            y = (int)Math.floor(Math.random() * rows);

            hasMine = cells[x][y].getMine();

            if(!hasMine)
            {		
                cells[x][y].setMine(true);
                currentMines++;	
            }			
        }
    }
    //------------------------------------------------------------------//

    //------------------------------------------------------------------//
    //Step 3//
    public void setSurroundingMinesNumber()
    {	
        for(int x = 0 ; x < cols ; x++) 
        {
            for(int y = 0 ; y < rows ; y++) 
            {
                cells[x][y].setSurroundingMines(calculateNeighbors(x,y));                        
            }
        }
    }
    //------------------------------------------------------------------//	

    //--------------------- Helper Functions ---------------------------//        

    //Calculates the number of surrounding mines ("neighbors")
    public int calculateNeighbors(int xCo, int yCo)
    {
        int neighbors = 0;

        // Check the neighbors (the columns xCo - 1, xCo, xCo + 1)
        for(int x=makeValidCoordinateX(xCo - 1); x<=makeValidCoordinateX(xCo + 1); x++) 
        {
            // Check the neighbors (the rows yCo - 1, yCo, yCo + 1).
            for(int y=makeValidCoordinateY(yCo - 1); y<=makeValidCoordinateY(yCo + 1); y++) 
            {
                // Skip (xCo, yCo), since that's no neighbor.
                if(x != xCo || y != yCo)
                    if(cells[x][y].getMine())   // If the neighbor contains a mine, neighbors++.
                        neighbors++;
            }
        }

        return neighbors;
    }

    //------------------------------------------------------------------//	

    //Simply makes a coordinate a valid one (i.e within the boundaries of the Board)
    public int makeValidCoordinateX(int i)
    {
        if (i < 0)
            i = 0;
        else if (i > cols-1)
            i = cols-1;

        return i;
    }	
    
    //Simply makes a coordinate a valid one (i.e within the boundaries of the Board)
    public int makeValidCoordinateY(int i)
    {
        if (i < 0)
            i = 0;
        else if (i > rows-1)
            i = rows-1;

        return i;
    }	
    
    //------------------------------------------------------------------//	        


    //--------- Getters and Setters -------------//
    public void setNumberOfMines(int numberOfMines)
    {
        this.numberOfMines = numberOfMines;
    }

    public int getNumberOfMines()
    {
        return numberOfMines;
    }

    public Cell[][] getCells()
    {
        return cells;
    }
    
    public int getRows()
    {
        return rows;
    }
    
    public int getCols()
    {
        return cols;
    }
    
}