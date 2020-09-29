//Name: Eshan Danayakapura Jagadeesh
//UTA ID: 1001667159
//NET ID : EXD7159

import java.util.*;

/**
 * This is the AiPlayer class. It simulates a minimax player for the max connect four game. 
 *
 * 
 * 
 */

public class AiPlayer {

    public int depth_Level;
    public GameBoard gameShallow;

    /**
     *The Constructor Instantiates the AI player object
     * 
     * @param currentGame
     * 
     */
    public AiPlayer(int depth, GameBoard curr_game) {
        this.depth_Level = depth;
        this.gameShallow = curr_game;
    }

    /**
     * This method makes the decision to make a move for the computer using 
     * the min and max value from the below given two functions.
     * 
     * @param currentGame The GameBoard object that is currently being used to play the game.
     * @return an integer indicating which column the AiPlayer would like to play in.
     * @throws CloneNotSupportedException
     */
    public int findBestPlay(GameBoard curr_game) throws CloneNotSupportedException {
        int play_variable = Maxconnect4.INVALID;
        if (curr_game.getCurrentTurn() == Maxconnect4.ONE) {
            int v = Integer.MAX_VALUE;
            for (int i = 0; i < GameBoard.Number_of_Columns; i++) {
                if (curr_game.isValidPlay(i)) {
                    GameBoard nextMove = new GameBoard(curr_game.getGameBoard());
                    nextMove.playPiece(i);
                    int value = Max_Utility(nextMove, depth_Level, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    if (v > value) {
                    	play_variable = i;
                        v = value;
                    }
                }
            }
        } else {
            int v = Integer.MIN_VALUE;
            for (int i = 0; i < GameBoard.Number_of_Columns; i++) {
                if (curr_game.isValidPlay(i)) {
                    GameBoard nextMove = new GameBoard(curr_game.getGameBoard());
                    nextMove.playPiece(i);
                    int value = Min_Utility(nextMove, depth_Level, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    if (v < value) {
                    	play_variable = i;
                        v = value;
                    }
                }
            }
        }
        return play_variable;
    }

    /**
     * This method calculates the minimum utility value
     
     */

    private int Min_Utility(GameBoard gameBoard, int depth_level, int alpha_value, int beta_value)
        throws CloneNotSupportedException {
     
        if (!gameBoard.isBoardFull() && depth_level > 0) {
            int v = Integer.MAX_VALUE;
            for (int i = 0; i < GameBoard.Number_of_Columns; i++) {
                if (gameBoard.isValidPlay(i)) {
                    GameBoard board4NextMove = new GameBoard(gameBoard.getGameBoard());
                    board4NextMove.playPiece(i);
                    int value = Max_Utility(board4NextMove, depth_level - 1, alpha_value, beta_value);
                    if (v > value) {
                        v = value;
                    }
                    if (v <= alpha_value) {
                        return v;
                    }
                    if (beta_value > v) {
                        beta_value = v;
                    }
                }
            }
            return v;
        } else {
            // Terminal state where we return the utility value
            return gameBoard.getScore(Maxconnect4.TWO) - gameBoard.getScore(Maxconnect4.ONE);
        }
    }

    /**
     * This method calculates the maximum utility value.
     */

    private int Max_Utility(GameBoard gameBoard, int depth_level, int alpha_value, int beta_value)
        throws CloneNotSupportedException {
        
        if (!gameBoard.isBoardFull() && depth_level > 0) {
            int v = Integer.MIN_VALUE;
            for (int i = 0; i < GameBoard.Number_of_Columns; i++) {
                if (gameBoard.isValidPlay(i)) {
                    GameBoard next_board_move = new GameBoard(gameBoard.getGameBoard());
                    next_board_move.playPiece(i);
                    int value = Min_Utility(next_board_move, depth_level - 1, alpha_value, beta_value);
                    if (v < value) {
                        v = value;
                    }
                    if (v >= beta_value) {
                        return v;
                    }
                    if (alpha_value < v) {
                        alpha_value = v;
                    }
                }
            }
            return v;
        } else {
            // Terminal state where we return the utility value
            return gameBoard.getScore(Maxconnect4.TWO) - gameBoard.getScore(Maxconnect4.ONE);
        }
    }// end of class
//reference taken from google
}
