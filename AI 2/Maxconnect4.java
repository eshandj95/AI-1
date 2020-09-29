//Name: Eshan Danayakapura Jagadeesh
//UTA ID: 1001667159
//NET ID : EXD7159

import java.util.Scanner;

public class Maxconnect4 {

    public static Scanner input_stream = null;
    public static GameBoard current_play_mode = null;
    public static AiPlayer aiPlayer = null;

    public static final int ONE = 1;
    public static final int TWO = 2;
    public static int HUMAN_PAWN;
    public static int COMPUTER_PAWN;
    public static int INVALID = 99;
    public static final String FILEPATH_PREFIX = "../";
    public static final String COMPUTER_OPT_FILE = "computer.txt";
    public static final String HUMAN_OPT_FILE = "human.txt";

    public enum MODE {
        INTERACTIVE,
        ONE_MOVE
    };

    public enum PLAYER_TYPE {
        HUMAN,
        COMPUTER
    };

    public static void main(String[] args) throws CloneNotSupportedException {
        // check for the number of arguments which has been passed by the user in the command line
        if (args.length != 4) {
            System.out.println("Looks like you have entered wrong number of argumemnts. 4 arguments are needed:\n"
                + "Command should be: java Maxconnect4 interactive [input file] [computer-next / human-next] [depth]\n"
                + " or:  java Maxconnect4 one-move [input_file] [output_file] [depth]\n");

            exit_function(0);
        }

        // Assign the inputs to the respective variables
        String game_mode = args[0].toString(); // the game mode which can be either interactive or one-move mode
        String input_File = args[1].toString(); // the input game file that has to be provided which will determine the initial board state of the game.
        int depth_Level = Integer.parseInt(args[3]); // the depth level of the ai search which the computer can look into while searching for the best move.

        // create and initialize the game board as per the provided input file
        current_play_mode = new GameBoard(input_File);

        // create the Ai Player object
        aiPlayer = new AiPlayer(depth_Level, current_play_mode);

        if (game_mode.equalsIgnoreCase("interactive")) {
            current_play_mode.setGameMode(MODE.INTERACTIVE);
            if (args[2].toString().equalsIgnoreCase("computer-next") || args[2].toString().equalsIgnoreCase("C")) {
                // if computer next has been pressed, let the computer make the next move
                current_play_mode.setFirstTurn(PLAYER_TYPE.COMPUTER);
                InteractiveMode();
                //if human next has been pressed, let the human make the next move
            } else if (args[2].toString().equalsIgnoreCase("human-next") || args[2].toString().equalsIgnoreCase("H")){
                current_play_mode.setFirstTurn(PLAYER_TYPE.HUMAN);
                HumanTurn();
            } else {
                System.out.println("\n" + "The value of next turn not recognized \n Please try again! \n");
                exit_function(0);
            }

            if (current_play_mode.isBoardFull()) {
                System.out.println("\nThe Board is Full\n\nGame Over!!!");
                exit_function(0);
            }

        } else if (!game_mode.equalsIgnoreCase("one-move")) {
            System.out.println("\n" + game_mode + " is an unrecognized game mode. Enter either interactive or one-move \n Please try again. \n");
            exit_function(0);
        } else {
            // /////////// one-move mode ///////////
            current_play_mode.setGameMode(MODE.ONE_MOVE);
            String outputFileName = args[2].toString(); // the output game file that has been provided in the command line
            OneMoveMode(outputFileName);
        }
    } 
  
    //main() ends here
    
    
    /**
     * InteractiveMode() handles computer's move for interactive mode
     * 
     */
    private static void InteractiveMode() throws CloneNotSupportedException {

        printBoardAndScore();

        System.out.println("\n Please wait \n Computer's turn:\n");

        int playColumn = INVALID; // the players choice of column to play

        // AI play - random play
        playColumn = aiPlayer.findBestPlay(current_play_mode);

        if (playColumn == INVALID) {
            System.out.println("\n\nThe Board is Full\n\nGame Over.");
            return;
        }

        // play the piece
        current_play_mode.playPiece(playColumn);

        System.out.println("move: " + current_play_mode.getPieceCount() + " , Player: Computer , Column: " + (playColumn + 1));

        current_play_mode.printGameBoardToFile(COMPUTER_OPT_FILE);

        if (current_play_mode.isBoardFull()) {
            printBoardAndScore();
            printResult();
        } else {
            HumanTurn();
        }
    }
    
    /**
     * OneMoveMode() handles the computer's move for one move mode
     * 
     * 
     */
    private static void OneMoveMode(String outputFileName) throws CloneNotSupportedException { //output file name has been used to save the current state of the game
        // TODO Auto-generated method stub

        // variables to keep up with the game
        int playColumn = 99; // the players choice of column to play
        boolean playMade = false; // set to true once a play has been made

        System.out.print("\nMaxConnect-4 game:\n");

        System.out.print("Game state before move:\n");

        // print the current game board
        current_play_mode.printGameBoard();

        // print the current scores
        System.out.println("Score: Player-1 = " + current_play_mode.getScore(ONE) + ", Player-2 = " + current_play_mode.getScore(TWO)
            + "\n ");

        if (current_play_mode.isBoardFull()) {
            System.out.println("\n\nThe Board is Full\n\nGame Over.");
            return;
        }

        // ****************** this chunk of code makes the computer play

        int current_player = current_play_mode.getCurrentTurn();

        // AI play - random play
        playColumn = aiPlayer.findBestPlay(current_play_mode);

        if (playColumn == INVALID) {
            System.out.println("\nI can't play.\nThe Board is Full\n\nGame Over.");
            return;
        }

        // play the piece
        current_play_mode.playPiece(playColumn);

        // display the current game board
        System.out.println("move " + current_play_mode.getPieceCount() + ": Player " + current_player + ", column "
            + (playColumn + 1));

        System.out.print("Game state after move:\n");

        // print the current game board
        current_play_mode.printGameBoard();

        // print the current scores
        System.out.println("Score: Player-1 = " + current_play_mode.getScore(ONE) + ", Player-2 = " + current_play_mode.getScore(TWO)
            + "\n ");

        current_play_mode.printGameBoardToFile(outputFileName);

        // ************************** end computer play

    }

    /**
     * Final result is printed in this method.
     * 
     */
    
    private static void printResult() {
        int human_score = current_play_mode.getScore(Maxconnect4.HUMAN_PAWN);
        int comp_score = current_play_mode.getScore(Maxconnect4.COMPUTER_PAWN);
        
        System.out.println("\n Final Result of the game:");
       
        if(comp_score>human_score){
        	 System.out.println("\n You lost! Better luck next time.");
        	
        	
        }else if(comp_score<human_score){
        	
        	System.out.println("\n Congratulations human! You won this game.");	
        }
         else {
            System.out.println("\n Game is tie! Well played");
        }
    }

    /**
     * HumanTurn() manages the human move that is made during the interactive mode play 
     */
    private static void HumanTurn() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        printBoardAndScore();

        System.out.println("\n Human's turn:\nKindly play your move here(1-7):");

        input_stream = new Scanner(System.in);

        int playColumn = INVALID;

        do {
            playColumn = input_stream.nextInt();
        } while (!isValidPlay(playColumn));

        // play the piece
        current_play_mode.playPiece(playColumn - 1);

        System.out.println("move: " + current_play_mode.getPieceCount() + " , Player: Human , Column: " + playColumn);
        
        current_play_mode.printGameBoardToFile(HUMAN_OPT_FILE);

        if (current_play_mode.isBoardFull()) {
            printBoardAndScore();
            printResult();
        } else {
            InteractiveMode();
        }
    }

    private static boolean isValidPlay(int playColumn) {
        if (current_play_mode.isValidPlay(playColumn - 1)) {
            return true;
        }
        System.out.println("Invalid column , Please enter column value between 1 to 7.");
        return false;
    }

    /**
     * This method displays current board state and score of each player.
     * 
     */
    public static void printBoardAndScore() {
        System.out.print("Game state :\n");

        // print the current game board
        current_play_mode.printGameBoard();

        // print the current scores
        System.out.println("Score: Player-1 = " + current_play_mode.getScore(ONE) + ", Player-2 = " + current_play_mode.getScore(TWO)
            + "\n ");
    }

    /**
     * This method is used when to exit the program prematurely.
     * 
     * @param value an integer that is returned to the system when the program exits.
     */
    private static void exit_function(int value) {
        System.out.println("exiting from MaxConnectFour.java!\n\n");
        System.exit(value);
    }
} // end of class connectFour