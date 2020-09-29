ReadMe
Assignment-2


Name: Samarth Manjunath
UTA ID: 1001522809
Subject: Artificial Intelligence-1
Programming language used: Java


There are 3 classes:
1. MaxConnect4
2. GameBoard
3. AiPlayer


1. MaxConnect4


* InteractiveMode() handles computer's move for interactive mode
* OneMoveMode() handles the computer's move for one move mode
* printResult() prints the final result
* HumanTurn() manages the human move that is made during the interactive mode play
* printBoardAndScore() method displays current board state and score of each player.
* main() handles many initialization functions which are mentioned in the program


2. GameBoard


* setPieceValue() method set piece value for both human & computer(1 or 2).
* printGameBoard() method prints the GameBoard to the screen in a nice, pretty, readable format
* GameBoard(String inputFile) This constructor creates a GameBoard object based on the input file given as an argument.


3. AiPlayer
* findBestPlay() method makes the decision to make a move for the computer using
* the min and max value from the below given two functions.
* Min_Utility() method calculates the minimum utility value
* Max_Utility() method calculates the maximum utility value.


Compilation steps


javac -classpath . Maxconnect4.java


To run the program


One-Move mode
java Maxconnect4 one-move [input_file] [output_file] [depth]
Eg:java Maxconnect4 one-move input2.txt output1.txt 2




Interactive Mode
java Maxconnect4 interactive [input file] [computer-next / human-next] [depth]
Eg:java Maxconnect4 interactive input2.txt computer-next 2


To fetch the time
time java Maxconnect4 interactive [input file] [computer-next / human-next] [depth]
Eg:time java Maxconnect4 one-move input2.txt output1.txt 2