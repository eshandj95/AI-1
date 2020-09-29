//Name: Eshan Danayakapura Jagadeesh
//UTA ID: 1001667159
//NET ID : EXD7159
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class GameBoard implements Cloneable {
    private int[][] playBoard;
    private int pieceCount;
    private int currentTurn;
    private Maxconnect4.PLAYER_TYPE first_turn;
    private Maxconnect4.MODE game_mode;

    public static final int Number_of_Columns = 7;
    public static final int Number_of_Rows = 6;
    public static final int Maximum_move = 42;

    public GameBoard(String inputFile) {
        this.playBoard = new int[Number_of_Rows][Number_of_Columns];
        this.pieceCount = 0;
        int counter = 0;
        BufferedReader input = null;
        String gameData = null;

        // opening the input file
        try {
            input = new BufferedReader(new FileReader(inputFile));
        } catch (IOException e) {
            System.out.println("\nProblem opening the input file!\nTry again." + "\n");
            e.printStackTrace();
        }

        // reading the game data from the input file
        for (int i = 0; i < Number_of_Rows; i++) {
            try {
                gameData = input.readLine();

                // read each piece from input file
                for (int j = 0; j < Number_of_Columns; j++) {

                    this.playBoard[i][j] = gameData.charAt(counter++) - 48;

                    // sanity checking
                    if (!((this.playBoard[i][j] == 0) || (this.playBoard[i][j] == Maxconnect4.ONE) || (this.playBoard[i][j] == Maxconnect4.TWO))) {
                        System.out.println("\nProblems!\n The piece read " + "from the input file was not 1, 2 or 0");
                        this.exit_function(0);
                    }

                    if (this.playBoard[i][j] > 0) {
                        this.pieceCount++;
                    }
                }
            } catch (Exception e) {
                System.out.println("\nProblem reading the input file!\n" + "Try again.\n");
                e.printStackTrace();
                this.exit_function(0);
            }

            // reset the counter to zero
            counter = 0;

        } 
        try {
            gameData = input.readLine();
        } catch (Exception e) {
            System.out.println("\nProblem reading the next turn!\n" + "Try again.\n");
            e.printStackTrace();
        }

        this.currentTurn = gameData.charAt(0) - 48;

        // to ensure the turn corresponds to the number of pc's played already
        if (!((this.currentTurn == Maxconnect4.ONE) || (this.currentTurn == Maxconnect4.TWO))) {
            System.out.println("Problems!\n The current turn read is not a " + "1 or 2!");
            this.exit_function(0);
        } else if (this.getCurrentTurn() != this.currentTurn) {
            System.out.println("Problems!\n the current turn read does not " + "correspond to the number of pieces played!");
            this.exit_function(0);
        }
    } 

    //This method below  set piece value for both human & computer 1 or 2 

    public void setPieceValue() {
        if ((this.currentTurn == Maxconnect4.ONE && first_turn == Maxconnect4.PLAYER_TYPE.COMPUTER)
            || (this.currentTurn == Maxconnect4.TWO && first_turn == Maxconnect4.PLAYER_TYPE.HUMAN)) {
            Maxconnect4.COMPUTER_PAWN = Maxconnect4.ONE;
            Maxconnect4.HUMAN_PAWN = Maxconnect4.TWO;
        } else {
            Maxconnect4.HUMAN_PAWN = Maxconnect4.ONE;
            Maxconnect4.COMPUTER_PAWN = Maxconnect4.TWO;
        }
        
        System.out.println("Human plays as : " + Maxconnect4.HUMAN_PAWN + " , Computer plays as : " + Maxconnect4.COMPUTER_PAWN);
        
    }
    
    
    // This method creates a clone for object of GameBoard class. Use clone() methid as it processes faster     
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    // This constructor creates a GameBoard object

    public GameBoard(int masterGame[][]) {

        this.playBoard = new int[Number_of_Rows][Number_of_Columns];
        this.pieceCount = 0;

        for (int i = 0; i < Number_of_Rows; i++) {
            for (int j = 0; j < Number_of_Columns; j++) {
                this.playBoard[i][j] = masterGame[i][j];

                if (this.playBoard[i][j] > 0) {
                    this.pieceCount++;
                }
            }
        }
    } // end GameBoard

 // to get the score
    public int getScore(int player) {
        // resets the scores
        int playerScore = 0;

        // check horizontally
        for (int i = 0; i < Number_of_Rows; i++) {
            for (int j = 0; j < 4; j++) {
                if ((this.playBoard[i][j] == player) && (this.playBoard[i][j + 1] == player)
                    && (this.playBoard[i][j + 2] == player) && (this.playBoard[i][j + 3] == player)) {
                    playerScore++;
                }
            }
        }

        // to  check vertically
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < Number_of_Columns; j++) {
                if ((this.playBoard[i][j] == player) && (this.playBoard[i + 1][j] == player)
                    && (this.playBoard[i + 2][j] == player) && (this.playBoard[i + 3][j] == player)) {
                    playerScore++;
                }
            }
        } 

        // check diagonally backward slash 
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if ((this.playBoard[i][j] == player) && (this.playBoard[i + 1][j + 1] == player)
                    && (this.playBoard[i + 2][j + 2] == player) && (this.playBoard[i + 3][j + 3] == player)) {
                    playerScore++;
                }
            }
        }

        // check diagonally for forward slash
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if ((this.playBoard[i + 3][j] == player) && (this.playBoard[i + 2][j + 1] == player)
                    && (this.playBoard[i + 1][j + 2] == player) && (this.playBoard[i][j + 3] == player)) {
                    playerScore++;
                }
            }
        }// end player score check

        return playerScore;
    } // end getScore

    public int getUnBlockedThrees(int player) {
    
        // to reset the scores
        int playerScore = 0;

        // check horizontally
        for (int i = 0; i < Number_of_Rows; i++) {
            for (int j = 0; j < 4; j++) {
                if ((this.playBoard[i][j] == player) && (this.playBoard[i][j + 1] == player)
                    && (this.playBoard[i][j + 2] == player)
                    && (this.playBoard[i][j + 3] == player || this.playBoard[i][j + 3] == 0)) {
                    playerScore++;
                }
            }
        } 
        // check vertically
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < Number_of_Columns; j++) {
                if ((this.playBoard[i][j] == player) && (this.playBoard[i + 1][j] == player)
                    && (this.playBoard[i + 2][j] == player)
                    && (this.playBoard[i + 3][j] == player || this.playBoard[i + 3][j] == 0)) {
                    playerScore++;
                }
            }
        } 

        // check diagonally  backslash 
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if ((this.playBoard[i][j] == player) && (this.playBoard[i + 1][j + 1] == player)
                    && (this.playBoard[i + 2][j + 2] == player)
                    && (this.playBoard[i + 3][j + 3] == player || this.playBoard[i + 3][j + 3] == 0)) {
                    playerScore++;
                }
            }
        }

        // check diagonally forward slash
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if ((this.playBoard[i + 3][j] == player) && (this.playBoard[i + 2][j + 1] == player)
                    && (this.playBoard[i + 1][j + 2] == player)
                    && (this.playBoard[i][j + 3] == player || this.playBoard[i][j + 3] == 0)) {
                    playerScore++;
                }
            }
        }// end player score check

        return playerScore;
    }

    // a method that determines if a play is valid or not. 
     
    public boolean isValidPlay(int column) {

        if (!(column >= 0 && column < 7)) {
            return false;
        } else if (this.playBoard[0][column] > 0) {
           
            return false;
        } else {
            // column is NOT full and the column is within bounds
            return true;
        }
    }

    // the method gets the current turn
     
    public int getCurrentTurn() {
        return (this.pieceCount % 2) + 1;
    }

    // this method returns the number of pieces that have been played on the board
    public int getPieceCount() {
        return this.pieceCount;
    }

    // this method returns the whole gameboard
     
    public int[][] getGameBoard() {
        return this.playBoard;
    }

    

    // checks whether Gameboard is full or no 
    
    boolean isBoardFull() {
        return (this.getPieceCount() >= GameBoard. Maximum_move);
    }

    //This method plays a piece on the game board.
    
    public boolean playPiece(int column) {

        // check if the column choice is a valid play
        if (!this.isValidPlay(column)) {
            return false;
        } else {

            // starting at the bottom of the board,
            // place the piece into the first empty spot
            for (int i = 5; i >= 0; i--) {
                if (this.playBoard[i][column] == 0) {
                    this.playBoard[i][column] = getCurrentTurn();
                    this.pieceCount++;
                    return true;
                }
            }
            // the pgm shouldn't get here
            System.out.println("Something went wrong with playPiece()");

            return false;
        }
    } // end playPiece

    // this method removes the top piece from the game board
 
    public void removePiece(int column) {

        // starting looking at the top of the game board
        for (int i = 0; i < Number_of_Rows; i++) {
            if (this.playBoard[i][column] > 0) {
                this.playBoard[i][column] = 0;
                this.pieceCount--;

                break;
            }
        }
    }  


    // this method prints the GameBoard to an output file 

    public void printGameBoardToFile(String outputFile) {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(outputFile));

            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    output.write(this.playBoard[i][j] + 48);
                }
                output.write("\r\n");
            }

            // write the current turn
            output.write(this.getCurrentTurn() + "\r\n");
            output.close();

        } catch (IOException e) {
            System.out.println("\nProblem writing to the output file!\n" + "Try again.");
            e.printStackTrace();
        }
    }

    //this method prints the GameBoard to the screen
     
    public void printGameBoard() {
        System.out.println(" -----------------");

        for (int i = 0; i < Number_of_Rows; i++) {
            System.out.print(" | ");
            for (int j = 0; j < Number_of_Columns; j++) {
                System.out.print(this.playBoard[i][j] + " ");
            }

            System.out.println("| ");
        }

        System.out.println(" -----------------");
    } 

    
     

    //This is the exit function

    private void exit_function(int value) {
        System.out.println("exiting from GameBoard.java!\n\n");
        System.exit(value);
    }
 
    //set first turn
    public void setFirstTurn(Maxconnect4.PLAYER_TYPE turn) {
        
        first_turn = turn;
        setPieceValue();
    }

    public Maxconnect4.PLAYER_TYPE getFirstTurn() {
        
        return first_turn;
    }
    //set game mode between the two options
    public void setGameMode(Maxconnect4.MODE mode) {
      
        game_mode = mode;
    }

    public Maxconnect4.MODE getGameMode() {
        
        return game_mode;
    }

} // end GameBoard class
