// GameMethods.java
// Arda Utku
// Jan 30
// This program contains all the methods for the game ConnectFour.java

import java.util.*;
import java.io.*;

// This class contains all the methods used in ConnectFour.java, and required to make the game of ConnectFour run
public class GameMethods 
{
    // Global constatns and variables 
    public static final int NUM_ROWS = 6;
    public static final int NUM_COLS = 7;
    public static final int BEAM_LENGTH = 4;
    public static final int MAX_MOVE_COUNT = (NUM_ROWS * NUM_COLS);

    public static final int HUMAN_PLAYER = 0;
    public static final int AI_PLAYER = 1;

    public static final int NOT_FINISHED = 0;
    public static final int PLAYER_HORIZONTAL_WIN = 1;
    public static final int PLAYER_VERTICAL_WIN = 2;
    public static final int PLAYER_DIAGONAL_WIN = 3;
    public static final int AI_HORIZONTAL_WIN = 4;
    public static final int AI_VERTICAL_WIN = 5;
    public static final int AI_DIAGONAL_WIN = 6;
    public static final int TIED_GAME = 7;

    public static final int ONE_SECOND = 1000;
    public static final int TWO_SECOND = 2000;

    public static final int DIFFICULTY_UNDIFINED = 0;
    public static final int DIFFICULTY_EASY = 1;
    public static final int DIFFICULTY_NORMAL = 2; 
    public static final int DIFFICULTY_HARD = 3; 

    public static final char EMPTY_SPOT = 'O';
    public static final char YELLOW_DISC = 'Y';
    public static final char RED_DISC = 'R';

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static int gameCount = 0;

    /*===============================================================================|
    | void printHelp                                                                 |
    |--------------------------------------------------------------------------------|
    | returns void                                                                   |
    |--------------------------------------------------------------------------------|
    | This method prints out the help menu with the basic rules of the game.         |
    ================================================================================*/
    public static void printHelp()
    {
        System.out.println("\nWelcome to the help menu. Here you can read the rules of the game.\n");
        System.out.println("The rules of Connect4 are relativly simple.\n");
        System.out.println("- In connect4 two player alternate turns placing a disc into an unfilled column in a 7 by 6 grid.\n");
        System.out.println("- To win the game you must line up four of your own discs horizontilly, verticaly, or diagonally.\n");
        System.out.println("- Your opponent will have the same objective, meaning you will have to worry about blocking your opponents discs");
        System.out.println("  To stop them from achiving a win while trying to achive a win yourself.\n");
        System.out.println("- If every spot on the board gets filled up and no one has won, the game will be a draw.\n");
        System.out.println("- You now know the basic rules of the game. Good luck in your games!\n");
    }

    /*===============================================================================|
    | void playGame                                                                  |
    |--------------------------------------------------------------------------------|
    | returns void                                                                   |
    |--------------------------------------------------------------------------------|
    | This method runs the playable portion of the game                              |
    ================================================================================*/
    public static void playGame()
    {
        // Variables and scanner
        int playerWins = 0;
        int playerLosses = 0;
        int playerTies = 0;
        int moveCount = 0;
        int gameStatus = NOT_FINISHED;
        int difficultyLevel = DIFFICULTY_UNDIFINED;
        double firstMove = 0;
        
        String playerName = "";
        String fileName = "";
        String playerCheck = "";
        String difficulty = "";

        boolean fileReady = false;

        char[][] board = new char[NUM_ROWS][NUM_COLS];

        Scanner sc = new Scanner(System.in);

        System.out.print("\nAre you a returning player? Y / N: ");
        playerCheck = sc.next();

        // Do while that runs until a file has been properly loaded or created 
        do
        {
            // Changes input to caps to simplify switch and case
            switch(playerCheck.toUpperCase())
            {
                // Loads up entered player's name's statistics file
                case "Y":
                {
                    System.out.print("\nPlease enter your name here to load up your statistics: ");
                    playerName = sc.next();

                    // Creates the name of the file that will be searched for to load up
                    fileName = playerName + ".txt";

                    // Try and catch that reads player's statistics file
                    try 
                    {
                        Scanner fileScanner = new Scanner(new File(fileName));

                        playerWins = fileScanner.nextInt();
                        playerLosses = fileScanner.nextInt();
                        playerTies = fileScanner.nextInt();

                        System.out.printf("%nWelcome back %s!%n", playerName);

                        // Prints player's statistics file
                        printStatistics(playerWins, playerLosses, playerTies);

                        // Delays print, in this case by one second
                        delayPrint(ONE_SECOND);
                        
                        // Allows the do while to end
                        fileReady = true;

                    }
                    // In case of file reading or writing exception, sends the player back to the starting menu 
                    catch (IOException e)
                    {
                        System.out.println("\nProblem reading or writing " + fileName + ". \nPlease check that the file exists or try another name.\n");

                        // Resets player to the start of the game, this game does not count
                        gameCount--;

                        // Sends player back to starting menu
                        return;
                    }

                    break;

                }
                // Creates a new profile for the given name 
                case "N":
                {
                    System.out.print("Please enter your name here and a new profile will be created for you: ");
                    playerName = sc.next();

                    // If statement that checks for a valid name 
                    if (playerName.length() > 0)
                    {
                        // Creates a file based on the enterd name 
                        fileName = playerName + ".txt";

                        // Initialization of all statistics to 0
                        fileReady = writeStatistics(fileName, playerWins, playerLosses, playerTies);
                    }
                    else 
                    {
                        System.out.println("Invalid name entered.");
                    }

                    break;
                }
                // In case of typo 
                default:
                {
                    System.out.println("Invalid input! Please type \"Y\" if you are a new player, or \"N\" if you are a returning player");
                    playerCheck = sc.next();

                    break;
                }
            }
        } while (fileReady == false); 

        // Do while that runs until a valid input has been inputed 
        do
        {
            // Asks the user to choose a difficulty 
            System.out.println("Please choose the level of difficulty for your next game:");
            System.out.println("Please enter \"easy\" for easy difficulty ");
            System.out.println("Please enter \"normal\" for normal difficulty ");
            System.out.print("Please enter \"hard\" for hard difficulty\nInput: ");
            difficulty = sc.next();

            // Sets diffiuclty level accordingly, will be used to run the chosen difficulty of computer's move 
            switch(difficulty.toUpperCase())
            {
                case "EASY":
                {
                    difficultyLevel = DIFFICULTY_EASY;
                    
                    break;
                }
                case "NORMAL":
                {
                    difficultyLevel = DIFFICULTY_NORMAL;
                    
                    break;
                }
                case "HARD":
                {
                    difficultyLevel = DIFFICULTY_HARD;
                    
                    break;
                }
                default:
                {
                    break;
                }
            }

        } while (difficultyLevel == DIFFICULTY_UNDIFINED);

        // Runs the played portion of the game

        // Clears the board
        resetBoard(board);
        // Prints cleared board 
        printBoard(board);

        System.out.println("\nDetermining who will play first...");

        delayPrint(ONE_SECOND);

        firstMove = Math.random();

        // If statment that allows first move to either player or computer based on randomly rolled number 
        if (firstMove < 0.5)
        {
            System.out.println("\nComputer plays first.");

            computerMove(board, difficultyLevel);

            printBoard(board);

            moveCount++;
        }
        else 
        {
            System.out.println("\nYou play first.");
        }

        // Do while that runs until a win by player or computer, or a tie, is achived 
        do 
        {
            delayPrint(ONE_SECOND);

            // Calls the playerMove method which prompts for and plays the players move
            playerMove(board);

            // Increases move count used to keep track of tie in case of full board 
            moveCount++;

            // Prints the updated board
            printBoard(board);

            delayPrint(ONE_SECOND);

            // Checks if player has won
            gameStatus = checkWin(board, HUMAN_PLAYER, BEAM_LENGTH, moveCount);

            // If statement that runs the computers move if player has not yet won
            if (gameStatus == NOT_FINISHED)
            {
                // Calls computerMove method runs the computer's move based on difficulty level set by player 
                computerMove(board, difficultyLevel);

                moveCount++;

                delayPrint(ONE_SECOND);
    
                // Checks if computer has won
                gameStatus = checkWin(board, AI_PLAYER, BEAM_LENGTH, moveCount);
            }

            printBoard(board);

        } while (gameStatus == NOT_FINISHED);

        // Method that prints the according results once the game has ended 
        showResults(gameStatus, playerWins, playerLosses, playerTies, fileName);
    }

    /*===============================================================================|
    | void resetBoard(char[][] board)                                                |
    |--------------------------------------------------------------------------------|
    | returns void                                                                   |
    |--------------------------------------------------------------------------------|
    | Clears the playing board                                                       |
    ================================================================================*/
    public static void resetBoard(char[][] board)
    {
        for (int i = 0; i < NUM_ROWS; i++)
        {
            for (int j = 0; j < NUM_COLS; j++)
            {
                board[i][j] = EMPTY_SPOT;
            }
        }
    }

    /*===============================================================================|
    | void printBoard(char[][] board)                                                |
    |--------------------------------------------------------------------------------|
    | returns void                                                                   |
    |--------------------------------------------------------------------------------|
    | This method prints the updated playing board used in the game                  |
    ================================================================================*/
    public static void printBoard(char[][] board)
    {
        String icon = "";

        System.out.println("");

        System.out.printf("        ");

        // Prints column headers
        for (int i = 0; i < NUM_COLS; i++)
        {
            System.out.printf("  Col:%d", (i+1));
        }

        System.out.println();

        // Prints each row, row 0 is at the bottom
        for (int i = NUM_ROWS - 1; i >= 0; i--)
        {
            System.out.print("    |");

            // Prints what is in each column for the current row 
            for (int j = 0; j < NUM_COLS; j++)
            {
                // Prints the symbol (empty, yellow for player, or red for computer) in each spot on the board based on the 2d board array which updates throughout the game
                switch (board[i][j])
                {
                    case EMPTY_SPOT:
                    {
                        // Change print color to white
                        System.out.print(ANSI_WHITE);

                        // Prints unicode character for an empty dotted circle
                        icon = "\u25CC";

                        break;
                    }
                    case YELLOW_DISC:
                    {
                        // Changes print color to yellow
                        System.out.print(ANSI_YELLOW);

                        // Prints unicode character for a full circle 
                        icon = "\u25CF";

                        break;
                    }
                    case RED_DISC:
                    {
                        // Changes print color to red
                        System.out.print(ANSI_RED);

                        // Prints unicode character for a full circle 
                        icon = "\u25CF";

                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                // Resets print color 
                System.out.print("      " + icon + ANSI_RESET);
            }
            System.out.println("      |");
        }
    }

    /*===============================================================================|
    | void copyBoard(char[][] board, char[][] simulatedBoard)                        |
    |--------------------------------------------------------------------------------|
    | returns void                                                                   |
    |--------------------------------------------------------------------------------|
    | char[][] board - The game board                                                |
    |--------------------------------------------------------------------------------|
    | char[][] simulatedBoard - Simulated board used for computer to see future moves|
    |--------------------------------------------------------------------------------|
    | Copies the current board onto the simulated board                              |
    ================================================================================*/
    public static void copyBoard(char[][] board, char[][] simulatedBoard)
    {
        // For loop that copys every index from the board onto the simulated board 
        for (int i = 0; i < NUM_ROWS; i++)
        {
            for (int j = 0; j < NUM_COLS; j++)
            {
                simulatedBoard[i][j] = board[i][j];
            }
        }
    }

    /*===============================================================================|
    | void playerMove(char[][] board)                                                |
    |--------------------------------------------------------------------------------|
    | returns void                                                                   |
    |--------------------------------------------------------------------------------|
    | char[][] board - The game board                                                |
    |--------------------------------------------------------------------------------|
    | Asks the player for a move to play                                             |
    ================================================================================*/
    public static void playerMove(char[][] board)
    {
        // Variables and scanner
        int colNum = 0;
        boolean validMove = false;

        Scanner sc = new Scanner(System.in);

        // Do while that runs until the player plays a valid move 
        do
        {
            // Try and catch cathes any InputMismatchExceptions
            try 
            {
                // User move input 
                System.out.print("\n\nPlease enter a column number to fill: ");
                colNum = sc.nextInt();
    
                // While loop that runs until player enters a valid col number
                while (colNum < 1 || colNum > 7)
                {
                    System.out.print("\n\nInvalid input! Please enter a number between 1 and 7 to choose the acording column here: ");
                    colNum = sc.nextInt();
                }
        
                // calls playMove to check for a valid move and assigns validMove accordingly 
                validMove = playMove(board, (colNum - 1), YELLOW_DISC);
    
                if (validMove == false)
                {
                    System.out.println("\nSorry that column is full, please place your disc in a diffrent column.");
                }
            }
            catch (InputMismatchException e)
            {
                System.out.println("\nPlease make sure you enter an integer.");

                sc.next(); // Clears the invalid scanner input 
            }

        } while (validMove == false);

        // Writes the move into a replay file to allow user to watch a replay of the game later after the game is finished 
        writeMove((colNum - 1), YELLOW_DISC);
    }

    /*===============================================================================|
    | void computerMove(char[][] board, int difficultyLevel)                         |
    |--------------------------------------------------------------------------------|
    | returns void                                                                   |
    |--------------------------------------------------------------------------------|
    | char[][] board - The game board                                                |
    |--------------------------------------------------------------------------------|
    | difficultyLevel - The difficulty level for the computer set by the player      |
    |--------------------------------------------------------------------------------|
    | Choses the chosen difficulty of ai to play a move                              |
    ================================================================================*/
    public static void computerMove(char[][] board, int difficultyLevel)
    {
        // Case and switch that runs the difficulty of ai chosen by player
        switch(difficultyLevel)
        {
            case DIFFICULTY_EASY:
            {
                // Calls the easy ai which runs a move
                computerMoveEasy(board);
                
                break;
            }
            case DIFFICULTY_NORMAL:
            {
                // Calls the normal ai which runs a move
                computerMoveNormal(board, true);
                
                break;
            }
            case DIFFICULTY_HARD:
            {
                // Calls the hard ai which runs a move
                computerMoveHard(board);
                
                break;
            }
            default:
            {
                break;
            }
        }
    }

    /*===============================================================================|
    | void computerMoveEasy(char[][] board)                                          |
    |--------------------------------------------------------------------------------|
    | returns void                                                                   |
    |--------------------------------------------------------------------------------|
    | char[][] board - The game board                                                |
    |--------------------------------------------------------------------------------|
    | Runs the easy ai which plays an easy difficulty move (random move)             |
    ================================================================================*/
    public static void computerMoveEasy(char[][] board)
    {
        // Variables 
        int col;
        boolean validMove;

        // Do while that runs until a valid random move is picked
        do 
        {
            // Rolls a random column number
            col = (int)(Math.random() * NUM_COLS);

            // Checks if move is valid 
            validMove = playMove(board, col, RED_DISC);

        } while (validMove == false);

        // Writes move into replay file
        writeMove(col, RED_DISC);

        System.out.printf("%nThe computer places a disc in column %d...%n", (col + 1));
    }

    /*===============================================================================|
    | void computerMoveNormal(char[][] board, boolean runBeam)                       |
    |--------------------------------------------------------------------------------|
    | returns void                                                                   |
    |--------------------------------------------------------------------------------|
    | char[][] board - The game board                                                |
    |--------------------------------------------------------------------------------|
    | boolean runBeam - If true ai plays a simulated move                            |
    |--------------------------------------------------------------------------------|
    | Runs the normal ai which plays a medium difficulty move                        |
    | ai first checks if there is a winning move for it or the player and plays it or|
    | blocks it, if no such move is found plays a smart random move                  |
    ================================================================================*/
    public static void computerMoveNormal(char[][] board, boolean runBeam)
    {
        // Variables
        boolean validMove = false;
        int move = 0;
        int[] smartMoveOrder = new int[NUM_COLS];

        // Calls method which rolls a smart random move 
        randomizeSmartMove(smartMoveOrder);

        // If true, has the ai play a simulated move for itself and the player to find or block a winning move 
        if (runBeam)
        {
            // Simulates play by checking every spot for a winning move 
            move = simulatePlay(board, AI_PLAYER, RED_DISC, BEAM_LENGTH);

            // If no winning move is found looks to block a winning move for player
            if (move < 0)
            {
                move = simulatePlay(board, HUMAN_PLAYER, YELLOW_DISC, BEAM_LENGTH);
            }
        }

        // If win or block with for 4 in a row is not found, plays a smart random move (prioritises center of board but can still have a chance to play off center)
        if (move < 0)
        {
            // Keeps checking columns based on order assigned in smartMoveOrder until it finds a valid move (unfilled column)
            for (int j = 0; j < NUM_COLS; j++)
            {
                move = smartMoveOrder[j];
    
                validMove = playMove(board, move, RED_DISC);
    
                if (validMove)
                {
                    // Breaks out of the for loop once it finds a valid move 
                    break;
                }
            }
        }
        else 
        {
            // Plays the winning or blocking move 
            playMove(board, move, RED_DISC);
        }

        // Write move into rpelay file
        writeMove(move, RED_DISC);
    
        // Output for the move the computer just played 
        System.out.printf("%nThe computer places a disc in column %d...%n", (move + 1));
    }

    /*===============================================================================|
    | void computerMoveHard(char[][] board)                                          |
    |--------------------------------------------------------------------------------|
    | returns void                                                                   |
    |--------------------------------------------------------------------------------|
    | char[][] board - The game board                                                |
    |--------------------------------------------------------------------------------|
    | Runs the hard ai which plays an hard difficulty move.                          |
    ================================================================================*/
    public static void computerMoveHard(char[][] board)
    {
        // Variables
        int move = 0;
        int currentBeamLength = BEAM_LENGTH;

        // First check for a winning move
        // If there was no winning move simulate to check for player winning move, and block it 
        // If there is no such move simulate playing to achive 3 in a row for AI
        // If no such move simiulate playing to block 3 in a row for player
        // If no such move check to achive 2 in a row for itself or block 2 in a row for the player
        // If no such move try a deafult smart move starting from the center columns 
        // Do while that runs until a move is found 
        do
        {
            // Checks ai move based on current beam length 
            move = simulatePlay(board, AI_PLAYER, RED_DISC, currentBeamLength);

            // Checks player move to try and block based on current beam length if ai move isnt found
            if (move < 0)
            {
                move = simulatePlay(board, HUMAN_PLAYER, YELLOW_DISC, currentBeamLength);

                // If no move with beam length is found for either player decreases beam length
                if (move < 0)
                {
                    currentBeamLength--;
                }
            }
        } while ((currentBeamLength > 1) && (move < 0));

        // If no hard diffiuclty move is found calls computerMoveNormal which will play a default smart move prioritising center of the board
        if (move < 0)
        {
            computerMoveNormal(board, false);

            return;
        }

        // Plays the found move
        playMove(board, move, RED_DISC);

        writeMove(move, RED_DISC);

        System.out.printf("%nThe computer places a disc in column %d...%n", (move + 1));

    }

    /*===============================================================================|
    | boolean playMove(char[][] board, int col, char disc)                           |
    |--------------------------------------------------------------------------------|
    | returns boolean                                                                |
    |--------------------------------------------------------------------------------|
    | char[][] board - The game board                                                |
    |--------------------------------------------------------------------------------|
    | int col - Column number                                                        |
    |--------------------------------------------------------------------------------|
    | char disc - Disc symbol                                                        |
    |--------------------------------------------------------------------------------|
    | Plays the move by assigning the according player symbol into the board         |
    ================================================================================*/
    public static boolean playMove(char[][] board, int col, char disc)
    {
        // For loop that assigns the according player symbol into the board 
        for (int i = 0; i < NUM_ROWS; i++)
        {
            if (board[i][col] == EMPTY_SPOT)
            {
                board[i][col] = disc;
                
                return true;
            }
        }

        // Returns false if the move cannot be made 
        return false;
    }

    /*===============================================================================|
    | simulatePlay(char[][] board, int player, char disc, int winningBeamLength)     |
    |--------------------------------------------------------------------------------|
    | returns int                                                                    |
    |--------------------------------------------------------------------------------|
    | char[][] board - The game board                                                |
    |--------------------------------------------------------------------------------|
    | int player - Number that represents weather human or computer is playing       |
    |--------------------------------------------------------------------------------|
    | char disc - Disc symbol                                                        |
    |--------------------------------------------------------------------------------|
    | int winningBeamLength - Length of the beam used as a best condition            |
    |--------------------------------------------------------------------------------|
    | Simulates a board with future moves to find the best possible move              |
    ================================================================================*/
    public static int simulatePlay(char[][] board, int player, char disc, int winningBeamLength)
    {
        // Variables
        int result = 0;
        boolean validMove = false;

        char[][] simulatedBoard = new char[NUM_ROWS][NUM_COLS];
        int[] smartMoveOrder = new int[NUM_COLS];

        // Randimizes smart move order to prevent computer from only stacking column 4 if smart move is required
        randomizeSmartMove(smartMoveOrder);

        // For loop that simulates playing the disc for each column and evaluating its results to find a best move 
        for (int j = 0; j < NUM_COLS; j++)
        {
            // Make a copy of the current board 
            copyBoard(board, simulatedBoard);

            // Plays the next potential move on the simulation board if its valid  
            validMove = playMove(simulatedBoard, smartMoveOrder[j], disc);

            if (validMove)
            {
                // Check if there is a winner in the current simulation board 
                result = checkWin(simulatedBoard, player, winningBeamLength, 1);

                if (result != NOT_FINISHED)
                {
                    // The current move under consideration is a winning move so return it 
                    return smartMoveOrder[j];
                }
            }
        }

        // Returns if no winning move was found 
        return -1;
    }

    /*===============================================================================|
    | int checkWin(char board[][], int player, int beamLength, int moveCount)        |
    |--------------------------------------------------------------------------------|
    | returns int                                                                    |
    |--------------------------------------------------------------------------------|
    | char board[][] - Game board                                                    |
    |--------------------------------------------------------------------------------|
    | int player - Integer that represents either player or computer                 |
    |--------------------------------------------------------------------------------|
    | int beamLength - Length of beam that checks ahead 4, 3, or 2 spaces            |
    |--------------------------------------------------------------------------------|
    | int moveCount - Counter for the moves                                          |
    |--------------------------------------------------------------------------------|
    | This method checks if either player or computer has a beam of beamLength       |
    | in any oriantation and returns an integer based on the type of win.            |
    ================================================================================*/
    public static int checkWin(char board[][], int player, int beamLength, int moveCount)
    {
        // Variables
        int counter = 0;
        char disc = YELLOW_DISC;

        // Assigns player based on weather human or computer is making the move that is being checked 
        if (player == HUMAN_PLAYER)
        {
            disc = YELLOW_DISC;
        } 
        else if (player == AI_PLAYER)
        {
            disc = RED_DISC;
        }

        // For loop that checks for horizontal win
        for (int i = 0; i < NUM_ROWS; i++)
        {
            for (int j = 0; j < NUM_COLS; j++)
            {
                counter = 0;

                for (int k = j; k < (beamLength + j); k++)
                {
                    if ((k < NUM_COLS) && (board[i][k] == disc))
                    {
                        // Counter increase for every consecutive disc of the same color 
                        counter++;
                    }

                    if (counter == beamLength)
                    {
                        if (disc == YELLOW_DISC)
                        {
                            return PLAYER_HORIZONTAL_WIN;
                        }
                        else if (disc == RED_DISC)
                        {
                            return AI_HORIZONTAL_WIN;
                        }
                    }
                }
            }
        }

        // For loop that checks vertical win
        for (int j = 0; j < NUM_COLS; j++)
        {
            for (int i = 0; i < NUM_ROWS; i++)
            {
                counter = 0;

                for (int k = i; k < (beamLength + i); k++)
                {
                    if ((k < NUM_ROWS) && (board[k][j] == disc))
                    {
                        counter++;
                    }

                    if (counter == beamLength)
                    {
                        if (disc == 'Y')
                        {
                            return PLAYER_VERTICAL_WIN;
                        }
                        else if (disc == 'R')
                        {
                            return AI_VERTICAL_WIN;
                        }
                    }
                }
            }
        }

        // Checks positive diagonal win
        for (int i = 0; i < NUM_ROWS; i++)
        {
            for (int j = 0; j < NUM_COLS; j++)
            {
                counter = 0;

                for (int k = 0; k < beamLength; k++)
                {
                    if (((i + k) < NUM_ROWS) && ((j + k) < NUM_COLS) && (board[i + k][j + k] == disc))
                    {
                        counter++;
                    }

                    if (counter == beamLength)
                    {
                        if (disc == YELLOW_DISC)
                        {
                            return PLAYER_DIAGONAL_WIN;
                        }
                        else if (disc == RED_DISC)
                        {
                            return AI_DIAGONAL_WIN;
                        }
                    }
                }
            }
        }

        // Checks negative diagonal win
        for (int i = 0; i < NUM_ROWS; i++)
        {
            for (int j = 0; j < NUM_COLS; j++)
            {
                counter = 0;

                for (int k = 0; k < beamLength; k++)
                {
                    if (((i + k) < NUM_ROWS) && ((j - k) >= 0) && (board[i + k][j - k] == disc))
                    {
                        counter++;
                    }

                    if (counter == beamLength)
                    {
                        if (disc == YELLOW_DISC)
                        {
                            return PLAYER_DIAGONAL_WIN;
                        }
                        else if (disc == RED_DISC)
                        {
                            return AI_DIAGONAL_WIN;
                        }
                    }
                }
            }
        }

        // If this is the last possible move its a tied game 
        if (moveCount == MAX_MOVE_COUNT)
        {
            return TIED_GAME;
        }

        // If no one has won
        return NOT_FINISHED;
    }

    /*===============================================================================|
    | void randomizeSmartMove(int[] smartMoveOrder)                                  |
    |--------------------------------------------------------------------------------|
    | returns void                                                                   |
    |--------------------------------------------------------------------------------|
    | int[] smartMoveOrder - Priority of columns for making a better move            |
    |--------------------------------------------------------------------------------|
    | Statistically it is better to make a move in the center of the board           |
    ================================================================================*/
    public static void randomizeSmartMove(int[] smartMoveOrder)
    {
        // Variable 
        int roll;

        // Default smart move order
        smartMoveOrder[0] = 3;
        smartMoveOrder[1] = 2;
        smartMoveOrder[2] = 4;
        smartMoveOrder[3] = 1;
        smartMoveOrder[4] = 5;
        smartMoveOrder[5] = 0;
        smartMoveOrder[6] = 6;

        // Adds randimization to smart move order to spice up the gameplay 
        roll = (int)(Math.random() * 100) + 1;

        // Assigns a different ordering for the first smart move choice in accordance to the roll
        if (roll <= 20)
        {
            smartMoveOrder[0] = 4;
            smartMoveOrder[2] = 3;
        }
        else if (roll <= 40)
        {
            smartMoveOrder[0] = 5;
            smartMoveOrder[4] = 3;
        }
        else if (roll <= 60)
        {
            smartMoveOrder[0] = 2;
            smartMoveOrder[1] = 3;
        }
        else if(roll <= 80)
        {
            smartMoveOrder[0] = 1;
            smartMoveOrder[3] = 3;
        }
    }

    /*===============================================================================|
    | void printStatistics(int playerWins, int playerLosses, int playerTies)         |
    |--------------------------------------------------------------------------------|
    | returns void                                                                   |
    |--------------------------------------------------------------------------------|
    | int playerWins - Number of wins                                                |
    |--------------------------------------------------------------------------------|
    | int playerLosses - Number of losses                                            |
    |--------------------------------------------------------------------------------|
    | int playerTies - Number of ties                                                |
    |--------------------------------------------------------------------------------|
    | Prints the game statistics                                                     |
    ================================================================================*/
    public static void printStatistics(int playerWins, int playerLosses, int playerTies)
    {
        // Vatiables 
        int totalGames = playerWins + playerLosses + playerTies;
        double winLoss = 0;

        System.out.printf("You currently have %d wins, %d losses, %d ties, ", playerWins, playerLosses, playerTies);

        // Only prints win ratio if player has at least one game played
        if (totalGames > 0)
        {
            winLoss = 100.0 * ((double)playerWins / (double)totalGames); 

            System.out.printf("and a win-loss ratio of %.2f%%.%n%n", winLoss);
        }
        else 
        {
            System.out.print("and you do not have a win-loss ratio yet.\n\n");
        }
    }

    /*=========================================================================================|
    |boolean writeStatistics(String fileName, int playerWins, int playerLosses, int playerTies)|
    |------------------------------------------------------------------------------------------|
    | returns boolean                                                                          |
    |------------------------------------------------------------------------------------------|
    | String fileName - Statistics file name                                                   |
    |------------------------------------------------------------------------------------------|
    | int playerWins - Number of wins                                                          |
    |------------------------------------------------------------------------------------------|
    | int playerLosses - Number of losses                                                      |
    |------------------------------------------------------------------------------------------|
    | int playerTies - Number of ties                                                          |
    |------------------------------------------------------------------------------------------|
    | Wrties the game statistics into a file                                                   |
    ==========================================================================================*/
    public static boolean writeStatistics(String fileName, int playerWins, int playerLosses, int playerTies)
    {
        // Catches any file reading or writing errors 
        try 
        {
            // Reads player statistics from given name's file 
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, false)));

            out.println(playerWins);
            out.println(playerLosses);
            out.println(playerTies);

            out.close();

            return true;
        }
        catch (IOException e)
        {
            System.out.println(e + " Problem reading or writing " + fileName + ". Please check that the file exists or try another name.");
        }

        return false;
    }

    /*==================================================================================================|
    |void showResults(int gameStatus, int playerWins, int playerLosses, int playerTies, String fileName)|
    |---------------------------------------------------------------------------------------------------|
    | returns void                                                                                      |
    |---------------------------------------------------------------------------------------------------|
    | int gameStatus - The final state of the game                                                      |
    |---------------------------------------------------------------------------------------------------|
    | int playerWins - Number of wins                                                                   |
    |---------------------------------------------------------------------------------------------------|
    | int playerLosses - Number of losses                                                               |
    |---------------------------------------------------------------------------------------------------|
    | int playerTies - Number of ties                                                                   |
    |---------------------------------------------------------------------------------------------------|
    | String fileName - Statistics file name                                                            |
    |---------------------------------------------------------------------------------------------------|
    | Prints the winner of the game and an according message and updates the statistics                 |
    ===================================================================================================*/
    public static void showResults(int gameStatus, int playerWins, int playerLosses, int playerTies, String fileName)
    {
        // Switch and case that displays an according message based on the game result, and updates player win or loss counter accordingly  
        switch(gameStatus)
        {
            case PLAYER_HORIZONTAL_WIN:
            {
                System.out.println("\n\nYOU WIN! You have achived a horizontal 4 in a row.");
                playerWins++;

                break;
            }
            case PLAYER_VERTICAL_WIN:
            {
                System.out.println("\n\nYOU WIN! You have achived a vertical 4 in a row.");
                playerWins++;

                break;
            }
            case PLAYER_DIAGONAL_WIN:
            {
                System.out.println("\n\nYOU WIN! You have achived a diagonal 4 in a row.");
                playerWins++;

                break;
            }
            case AI_HORIZONTAL_WIN:
            {
                System.out.println("\n\nYou lose! The AI has achived a horizontal 4 in a row and defeated you.");
                playerLosses++;

                break;
            }
            case AI_VERTICAL_WIN:
            {
                System.out.println("\n\nYou lose! The AI has achived a vertical 4 in a row and defeated you.");
                playerLosses++;

                break;
            }
            case AI_DIAGONAL_WIN:
            {
                System.out.println("\n\nYou lose! The AI has achived a diagonal 4 in a row and defeated you.");
                playerLosses++;

                break;
            }
            case TIED_GAME:
            {
                System.out.println("\n\nDraw! The board has filled up without a winner, resulting in a draw.");
                playerTies++;

                break;
            }
            default:
            {
                break;
            }
        }

        System.out.println("\nHere are your new statistics: ");

        printStatistics(playerWins, playerLosses, playerTies);
        
        writeStatistics(fileName, playerWins, playerLosses, playerTies);
    }

    /*==================================================================================================|
    |void printInstructions(boolean again)                                                              |
    |---------------------------------------------------------------------------------------------------|
    | returns void                                                                                      |
    |---------------------------------------------------------------------------------------------------|
    |boolean again - Weather this is or isnt the first time the game has been played in current session |
    |---------------------------------------------------------------------------------------------------|
    | Prints prompts for the user to choose a menu option to acess                                      |
    ===================================================================================================*/
    public static void printInstructions(boolean again)
    {
        // Weather this is or isnt the first time the game has been played in current session
        if (again == false)
        {
            System.out.printf("Please enter \"start\" to start the game%n");
        }
        else 
        {
            System.out.printf("Please enter \"start\" if you would like to play again%n");
            System.out.printf("Please enter \"replay\" if you would like to watch a replay of any games played in your current session%n");
        }
        System.out.printf("Please enter \"exit\" if you would like to exit the game%n");
        System.out.printf("Please enter \"help\" if you would like to see the help menu%nInput: ");
    }

    /*===============================================================================|
    | void delayPrint(int ms)                                                        |
    |--------------------------------------------------------------------------------|
    | returns void                                                                   |
    |--------------------------------------------------------------------------------|
    | int ms - Time in miliseconds                                                   |
    |--------------------------------------------------------------------------------|
    | This method will delay the program output by either 1 or 2                     |
    | seconds to give the player time to read the output.                            |
    ================================================================================*/
    public static void delayPrint(int ms)
    {
        try
        {
            Thread.sleep(ms); // Delays print for as many seconds as requested based on what was passed into the method 
        }
        catch (Exception e)
        {
            // Ignore all excpetions
        }
    }

    /*==================================================================================================|
    |void replayGame()                                                                                  |
    |---------------------------------------------------------------------------------------------------|
    | returns void                                                                                      |
    |---------------------------------------------------------------------------------------------------|
    | Allows the user to choose and watch a replay of any of the games played in their current session  |
    ===================================================================================================*/
    public static void replayGame()
    {
        // Vriables
        int col;
        int player;
        int replayNum = 0;
        char symbol = YELLOW_DISC;
        String replayName = "";
        char[][] replayBoard = new char[NUM_ROWS][NUM_COLS];

        Scanner sc = new Scanner(System.in);

        // Do while that runs until a valid replay has been selected 
        do 
        {
            // Catches any InputMismatchException errors 
            try 
            {
                System.out.printf("%nYou have %d games played. Please enter the game number that you would like to view the replay of here: ", gameCount);
                replayNum = sc.nextInt();
            }
            catch (InputMismatchException e)
            {
                System.out.println("\nPlease make sure you enter an integer.");

                sc.next(); // Clears the invalid scanner input 
            }


        } while (replayNum > gameCount || replayNum < 1);

        // Chooses the replay file requested by user
        replayName = saveFileName((replayNum - 1));

        // End of replay is reached when an InputMismatchException occurs if so the game will print instructions again, also catches IO exceptions
        try
        {
            // Opens scanner
            Scanner replayScanner = new Scanner(new File(replayName));

            // Resets the board
            resetBoard(replayBoard);
    
            // Do while that reads from a replay save file and prints every single move played in the selected game, loop breaks when InputMismatchException which means the EOF is reached 
            do
            {
                // Reads integers from replay file which give the program info on what move was played and who played it
                col = replayScanner.nextInt();
                player = replayScanner.nextInt();
    
                // Outputs an according message based on what move was played 
                if (player == 0)
                {
                    symbol = YELLOW_DISC;
                    System.out.printf("%nPlayer played column %d%n", (col + 1));
                }
                else if(player == 1)
                {
                    symbol = RED_DISC;
                    System.out.printf("%nComputer played column %d%n", (col + 1)); 
                }
    
                // Plays the move and prints the board 
                playMove(replayBoard, col, symbol);

                printBoard(replayBoard);
    
                delayPrint(TWO_SECOND);

            } while(true);
        }
        catch (InputMismatchException e)
        {
            System.out.println("\nEnd of replay.\n");
        }
        catch (IOException e)
        {
            System.out.println(e + "  Problem reading " + replayName);
        }
        catch (NoSuchElementException e)
        {
            System.out.println("End of replay.");
        }
    }

    /*==================================================================================================|
    | void writeMove(int colNum, char symbol)                                                           |
    |---------------------------------------------------------------------------------------------------|
    | returns void                                                                                      |
    |---------------------------------------------------------------------------------------------------|
    | int colNum - COlumn number                                                                        |
    |---------------------------------------------------------------------------------------------------|
    | char symbol - Symbol that represents player or computer                                           |
    |---------------------------------------------------------------------------------------------------|
    | Writes every valid move played into a replay file                                                 | 
    ===================================================================================================*/
    public static void writeMove(int colNum, char symbol)
    {
        // Variables 
        int symbolNum = 0;
        String replayName = saveFileName(gameCount);

        // Assigns disc color based symbol passed in 
        if (symbol == YELLOW_DISC)
        {
            symbolNum = 0;
        }
        else if (symbol == RED_DISC)
        {
            symbolNum = 1;
        }

        // Catches IOException
        try 
        {
            // Opens writer which writes the column followed by a number that represents which player made the move
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(replayName, true)));

            out.print(colNum + " ");
            out.print(symbolNum + " ");
    
            out.close();
        }
        catch (IOException e)
        {
            System.out.println(e + " Problem reading or writing " + replayName + ". Please check that the file exists or try another name.");
        }
    }

    /*==================================================================================================|
    | String saveFileName(int count)                                                                    |
    |---------------------------------------------------------------------------------------------------|
    | returns String                                                                                    |
    |---------------------------------------------------------------------------------------------------|
    | int count - Count represents game number used to replay file save name                            |
    |---------------------------------------------------------------------------------------------------|
    | Creates the file name                                                                             | 
    ===================================================================================================*/
    public static String saveFileName(int count)
    {
        // Variable
        String replayName = "save" + count  + ".txt";

        // Returns the file name
        return replayName;
    }

    /*==================================================================================================|
    | void clearSaveFiles()                                                                             |
    |---------------------------------------------------------------------------------------------------|
    | returns void                                                                                      |
    |---------------------------------------------------------------------------------------------------|
    | Clears all replay files from the current session                                                  | 
    ===================================================================================================*/
    public static void clearSaveFiles()
    {
        // Variable 
        String replayName = "";

        // For loop that uses game count to go thorugh and erase every replay file from this session
        for (int i = 0; i < gameCount; i++)
        {
            try 
            {
                // Has replay name get assigned to every replay made in this session
                replayName = saveFileName(i);

                // Opens file with the same file name as one that already exits and reopens with with printer set to false which clears the file
                PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(replayName, false)));
    
                out.close();
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
        }
    }
}
