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
    public static final String COMP_OP_FILE = "computer.txt";
    public static final String HUMAN_OP_FILE = "human.txt";

    public enum MODE {
        INTERACTIVE,
        ONE_MOVE
    };

    public enum PLAYER_TYPE {
        HUMAN,
        COMPUTER
    };

    public static void main(String[] args) throws CloneNotSupportedException {
        //This is to check in the number of arguments which has been passed by the user
        if (args.length != 4) {
            System.out.println("Looks like you have entered wrong number of argumemnts. 4 arguments are needed:\n"
                + "Command should be: java Maxconnect4 interactive [input file] [computer-next / human-next] [depth]\n"
                + " or:  java Maxconnect4 one-move [input_file] [output_file] [depth]\n");

            exit_function(0);
        }

        // Assigning the inputs to the variables respectively 
        String game_mode = args[0].toString(); // Game mode
        String input_File = args[1].toString(); // Input file
        int depth_Level = Integer.parseInt(args[3]); // ai search depth level

        // To create and initialise the game board based on the input files provided
        current_play_mode = new GameBoard(input_File);

        // create the Ai Player object
        aiPlayer = new AiPlayer(depth_Level, current_play_mode);

        if (game_mode.equalsIgnoreCase("INTERACTIVE")) {
            current_play_mode.setGameMode(MODE.INTERACTIVE);
            if (args[2].toString().equalsIgnoreCase("COMPUTER-NEXT")) {
                // if computer next has been pressed, let the computer make the next move
                current_play_mode.setFirstTurn(PLAYER_TYPE.COMPUTER);
                InteractiveMode();
                //if human next has been pressed, let the human make the next move
            } else if (args[2].toString().equalsIgnoreCase("HUMAN-NEXT")){
                current_play_mode.setFirstTurn(PLAYER_TYPE.HUMAN);
                HumanTurn();
            } else {
                System.out.println("\n" + "Next turn's value is not recognized \n Please, Try again! \n");
                exit_function(0);
            }

            if (current_play_mode.isBoardFull()) {
                System.out.println("\nThe Board is Full\n\n Game Over!");
                exit_function(0);
            }

        } else if (!game_mode.equalsIgnoreCase("ONE-MOVE")) {
            System.out.println("\n" + game_mode + " is an unrecognized game mode. Enter either interactive or one-move \n Please try again. \n");
            exit_function(0);
        } else {

            current_play_mode.setGameMode(MODE.ONE_MOVE);
            String outputFileName = args[2].toString(); // the output game file that has been provided in the command line
            OneMoveMode(outputFileName);
        }
    } 
    //This function handles the computer's move for one move mode
    
    private static void OneMoveMode(String outputFileName) throws CloneNotSupportedException { 

        int playColumn = 99; // the players choice of column
        boolean playMade = false; // set to true after a play has been made

        System.out.print("\nMaxConnect-4 game:\n");

        System.out.print("Game state before move:\n");

        // print the current game board
        current_play_mode.printGameBoard();

        // print the current scores
        System.out.println("Score: Player_1: " + current_play_mode.getScore(ONE) + ", Player_2: " + current_play_mode.getScore(TWO)
            + "\n ");

        if (current_play_mode.isBoardFull()) {
            System.out.println("\n\nThe Board is Full\n\nGame Over.");
            return;
        }

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
        System.out.println("Score: Player_1: " + current_play_mode.getScore(ONE) + ", Player_2: " + current_play_mode.getScore(TWO)
            + "\n ");

        current_play_mode.printGameBoardToFile(outputFileName);

    }
    //InteractiveMode() handles computer's move for interactive mode

 private static void InteractiveMode() throws CloneNotSupportedException {

        print_Board_Score();

        System.out.println("\n Please wait \n Computer's turn :\n");

        int playColumn = INVALID; // the players choice of column to play

        // AI play - random play
        playColumn = aiPlayer.findBestPlay(current_play_mode);

        if (playColumn == INVALID) {
            System.out.println("\n\nThe Board is Full\n\nGame Over.");
            return;
        }

        // play the piece
        current_play_mode.playPiece(playColumn);

        System.out.println("MOVE: " + current_play_mode.getPieceCount() + " , PLAYER: COMPUTER , COLUMN: " + (playColumn + 1));

        current_play_mode.printGameBoardToFile(COMP_OP_FILE);

        if (current_play_mode.isBoardFull()) {
            print_Board_Score();
            print_Result();
        } else {
            HumanTurn();
        }
    }
    
    // The below function manages the human move that is made during the interactive mode play 
    
    private static void HumanTurn() throws CloneNotSupportedException {
        
        print_Board_Score();

        System.out.println("\n Human's turn:\nPlay your move here 1-7:");

        input_stream = new Scanner(System.in);

        int playColumn = INVALID;

        do {
            playColumn = input_stream.nextInt();
        } while (!isValidPlay(playColumn));

        // play the piece
        current_play_mode.playPiece(playColumn - 1);

        System.out.println("MOVE: " + current_play_mode.getPieceCount() + " , PLAYER: HUMAN , COLUMN: " + playColumn);
        
        current_play_mode.printGameBoardToFile(HUMAN_OP_FILE);

        if (current_play_mode.isBoardFull()) {
            print_Board_Score();
            print_Result();
        } else {
            InteractiveMode();
        }
    }


      // This method shows score and board state
    public static void print_Board_Score() {
        System.out.print("Game state :\n");

        // print the current game board
        current_play_mode.printGameBoard();

        // print the current scores
        System.out.println("SCORES: PLAYER 1  : " + current_play_mode.getScore(ONE) + ", PLAYER 2 : " + current_play_mode.getScore(TWO)
            + "\n ");
    }

    //Final res is printed here below method
    
    private static void print_Result() {
        int human_Score = current_play_mode.getScore(Maxconnect4.HUMAN_PAWN);
        int computer_Score = current_play_mode.getScore(Maxconnect4.COMPUTER_PAWN);
        
        System.out.println("\n The Final Result of the game is:");
       
        if(computer_Score>human_Score){
             System.out.println("\n Sorry, You Lost.");
            
            
        }else if(computer_Score<human_Score){
            
            System.out.println("\n Congratulations human! You won the game.");  
        }
         else {
            System.out.println("\n It's a tie! Well played");
        }
    }


    private static boolean isValidPlay(int playColumn) {
        if (current_play_mode.isValidPlay(playColumn - 1)) {
            return true;
        }
        System.out.println("Sorry, INVALID COLUMN , Please enter the values for the column only between 1 and 7");
        return false;
    }

      // exit func
    private static void exit_function(int value) {
        System.out.println("exiting from MaxConnectFour.java!\n\n");
        System.exit(value);
    }
} 