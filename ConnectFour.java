// ConnectFour.java
// Arda Utku
// Jan 30
// This program runs the game Connect4 

import java.util.*;

public class ConnectFour 
{
    public static void main(String[] args) 
    {
        // Variables and scanner
        String command = "";
        boolean endGame = false;
        boolean gamesPlayed = false; 

        Scanner sc = new Scanner(System.in);

        // Welcome message 
        System.out.printf("%nWelcome to Arda's Connect4!%n%n");

        // Calls printInstructions method from the GameMethods class, which prints the initial instructions for the player to proccede 
        GameMethods.printInstructions(gamesPlayed);

        // Do while that runs the menu until "exit" is typed by user
        do
        {
            command = sc.next();
            
            // Switch and case that runs based on user input 
            switch (command.toUpperCase())
            {
                // "Start" runs the entire playable part of the game
                case "START":
                {
                    // Calls playGame method from the GameMethods class, which runs the main game 
                    GameMethods.playGame();

                    // Increase game count, used to keep track of save file numbers 
                    GameMethods.gameCount++;

                    // Remains true after the first time the game is played, causeing the instructions menu to display "play again"
                    gamesPlayed = true;

                    // Calls delayPrint method from the GameMethods class, which delays the output by one or two seconds to give the user time to read what is outputed 
                    GameMethods.delayPrint(GameMethods.ONE_SECOND);

                    GameMethods.printInstructions(gamesPlayed);

                    break;
                }
                case "EXIT":
                {
                    // Game is exited and ends when true
                    endGame = true;

                    break;
                }
                case "HELP":
                {
                    // Calls printHelp method from the GameMethods class, which prints the help menu
                    GameMethods.printHelp();

                    GameMethods.delayPrint(GameMethods.ONE_SECOND);

                    GameMethods.printInstructions(gamesPlayed);

                    break;
                }
                case "REPLAY":
                {
                    // Calls replay method from the GameMethods class, which gives the player the option to choose and watch the replay of any of the games played in their current session
                    GameMethods.replayGame();

                    GameMethods.printInstructions(gamesPlayed);

                    break;
                }
                default:
                {
                    // In case of typos
                    System.out.println("\nInvalid input! Please try again.");

                    GameMethods.printInstructions(gamesPlayed);

                    break;
                }
            }
        } while (endGame == false);

        // Calls replay method from the GameMethods class, which clears all current session replay files, only occurs when game is closed properly by typing exit in game
        GameMethods.clearSaveFiles();
        
        // Exit message 
        System.out.println("\nThanks for playing Coonect4! Goodbye."); 
    }
}