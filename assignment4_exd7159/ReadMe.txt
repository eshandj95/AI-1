Name: Eshan Danayakapura Jagadeesh
UTA ID: 1001667159
NET ID : EXD7159

Programming language used: Java

There are 3 classes:
1. MaxConnect4
2. GameBoard
3. AiPlayer


1. MaxConnect4

main() this function handles many initialization functions which are mentioned in the program
InteractiveMode() handles computer's move for interactive mode
OneMoveMode() handles the computer's move for one move mode
printFinalResult() prints the final result
HumanTurn() manages the human move that is made during the interactive mode play
printScoreBoard() method displays current board state and score of each player.

2. GameBoard
printGameBoard() method prints the GameBoard to the screen in a nice, pretty, readable format
setPieceValue() method set piece value for both human & computer(1 or 2).
GameBoard() This constructor creates a GameBoard object based on the input file given as an argument.


3. AiPlayer
findBestPlay() method makes the decision to make a move for the computer using
Min_Utility() method calculates the minimum utility value
Max_Utility() method calculates the maximum utility value.


Compilation steps
javac Maxconnect4.java Gameboard.java Aiplayer.java

To run the program

One-Move mode
java Maxconnect4 one-move [input_file] [output_file] [depth]
Eg:java Maxconnect4 one-move input.txt output1.txt 2


Interactive Mode
java Maxconnect4 interactive [input file] [computer-next / human-next] [depth]
Eg:java Maxconnect4 interactive input.txt computer-next 2