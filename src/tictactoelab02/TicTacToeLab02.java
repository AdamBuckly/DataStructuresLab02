package tictactoelab02;

import tictactoelab02.StdDraw;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * THIS IS THE NUMBER VERSION OF THE GAME.
 * 
 * 
 * This class, with the help of the StdDraw class, allows a user
 * to play a game of Tic-Tac-Toe using Swing GUI. (exclusively a player
 * vs. computer game).
 * 
 * Note: The player is the "X" character and the Computer is the "O" character.
 * 
 * References: Slides from Data Structures module on Moodle & Classes.
 * 
 * @author Adam Buckley (Student I.D: 20062910).
 * @version 1.
 * @date 15/01/2016.
 */

public class TicTacToeLab02{

	// This flag is to control the level of debug messages generated
	final static boolean DEBUG = true;

	final static int EMPTY = 0;
	final static int X_SHAPE = 1;
	final static int O_SHAPE = -1;
	final static Random random = new Random();
	
	//array of booleans, the 5 usable numbers for the computer.
	boolean[] booleanArray = new boolean[5];

	public static void main(String[] args) 
	{
		StartOrNotPopUp();
		overallGameActions();
	}

	/**
	 * This contains the execution of the actual game itself.
	 * This method is always called in the main method and if
	 * a user decides to play again then this method is called again
	 * via the gameEndOptions() method).
	 */
	public static void overallGameActions()
	{
		// Allocate identifiers to represent game state
		// Using an array of int so that summing along a row, a column or a
		// diagonal is a easy test for a win
		//Frame frame = new JFrame("Game Of Tic-Tac-Toe");

		int[][] board = new int[3][3];

		// Initializing variables within the main method.
		int row = 0;
		int col = 0;
		int move = 0;
		boolean boardFull;
		int playerWon;
		
		//array of booleans, the 5 usable numbers for the computer.
		boolean[] booleanArray = new boolean[5];

		// Setup graphics and draw empty board.
		StdDraw.setPenRadius(0.04);							// draws thicker lines.
		StdDraw.line(0, 0.33, 1, 0.33);
		StdDraw.line(0, 0.66, 1, 0.66);
		StdDraw.line(0.33, 0, 0.33, 1.0);
		StdDraw.line(0.66, 0, 0.66, 1.0);

		StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 64)); // Font Size: Large.

		do {
			if(move % 2 ==0)
			{
				System.out.println("\tHuman move ...");

				displayUserMovePrompt();

				// use mouse position to get slot
				boolean mousePressed = false;
				do {
					if (StdDraw.mousePressed()) {

						col = (int) (StdDraw.mouseX() * 3);	

						row = (int) (StdDraw.mouseY() * 3);

						mousePressed = true;
					}
				}while(!mousePressed || board[row][col] != EMPTY);
				board[row][col] = X_SHAPE;   // valid move (empty slot)
				StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 64)); // Font SIZE!
			}
			else
			{
				System.out.println("\tComputer move ...");

				displayCompMovePrompt();

				int[] ComputerToWinPositions = computerToWin(booleanArray, board);

				if (ComputerToWinPositions != null)
				{
					row = ComputerToWinPositions[0];
					col = ComputerToWinPositions[1];
				}
				else if (ComputerToWinPositions == null)
				{	
					int[] ComputerToBlockPositions = computerToBlock(booleanArray, board);
					if (ComputerToBlockPositions != null)
					{
						row = ComputerToBlockPositions[0];
						col = ComputerToBlockPositions[1];
					}
					else
					{
						int[] ComputerRandomPositions = getEmptySquareRandom(booleanArray, board);
						row = ComputerRandomPositions[0];
						col = ComputerRandomPositions[1];
					}
				}
				
				board[row][col] = O_SHAPE;   // valid move (empty slot)
				//keep a time of 650ms for the computer to make a move
				//(helps the player experience feel more authentic).
				try {
					Thread.sleep(650);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			// Below: Update screen to reflect board change
			// move is the move number, col is the current move's column, row 
			//is the current move's row

			double x = col * .33 + 0.15;
			double y = row * .33 + 0.15;

			//Directly below: board reflecting the change: if the move int value is an even
			//number then the user's X value position choice is reflected but it move is an
			//odd number the computer's (random) position choice is reflected on this update.

			StdDraw.text(x, y, (move % 2 == 0 ? "X" : "O"));
			move++;
			System.out.println(move);
			boardFull = isBoardFull(board);
			//playerWon = hasSomeoneWon(board);
			// Below: While the board isn't full and neither the user or the computer have won:
		}while (!boardFull && playerWon == 0);

		/*switch (playerWon) 
		{
		case EMPTY:
			//JOptionPane.showMessageDialog(null, "The Game is Over: It is a draw");
			gameEndOptions(playerWon, board);
			break;
		case X_SHAPE:
			gameEndOptions(playerWon, board);
			break;
		case O_SHAPE:
			gameEndOptions(playerWon, board);
			break;
		}*/
	}
	
	/**
	 * This method allows a pop up to appear right before the very first game
	 * is about to be played. This pop-up gives a user an option to play the game
	 * (yes button being pressed) or not play the game (no or cancel buttons being pressed).
	 */
	public static void StartOrNotPopUp()
	{
		final JFrame frame = new JFrame("JOptionPane Demo");
		int i = JOptionPane.showOptionDialog(frame,
				"Would You Like The Tic-Tac-Toe Game To begin?",
				"Tic-Tac-Toe: Video Game",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE, // icon
				null,
				null,
				null);

		// Code directly below: if my i int is equals to 0 (user click user action) then the game 
		// does not terminate. However if i is not equals to 0 then the game does terminate.
		if (i != 0)
		{
			System.exit(0);
		}
	}

	public static boolean isBoardFull(int[][] board)
	{

		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				if(board[row][col] == EMPTY)
				{
					return false;
				}
			}
		}
		System.out.println("the board is full");
		return true;
	}

	public static boolean hasSomebodyWon(int[][] board)
	{
		for (int row = 0; row < 3; row++)
		{
			int sum = board[row][0] + board[row][1] + board[row][2];
			if ((sum == 15) && (board[row][0] != EMPTY && board[row][1] != EMPTY && board[row][2] != EMPTY))
			{
				return true;
			}
		}

		for (int col = 0; col < 3; col++)
		{
			int sum = board[0][col] + board[1][col] + board[2][col];
			if ((sum == 15) && (board[0][col] != EMPTY && board[1][col] != EMPTY && board[2][col] != EMPTY))
			{
				return true;
			}
		}

		int sum = board[0][0] + board[1][1] + board[2][2];
		int sum2 = board[0][2] + board[1][1] + board[2][0];

		if ((sum == 15 || sum2 == 15) && (board[0][0] != EMPTY && board[1][1] != EMPTY && board[2][2] != EMPTY))
		{
			return true;
		}

		if ((sum == 15 || sum2 == 15) && (board[0][2] != EMPTY && board[1][1] != EMPTY && board[2][0] != EMPTY))
		{
			return true;
		}

		return false;
	}

	public static int[] getEmptySquare(int[][] board)
	{
		int row = 0;
		int col = 0;

		do {
			//below: rn which is an object of type Random will generate a random
			//number between 0 and 2 (completely inclusive) for the eventually chosen
			//row (horizontal) and column (vertial).
			row = random.nextInt(3);
			col = random.nextInt(3);
			//below: while that specific square is empty, an int array is returned (result)
			//that contains the row in question (position 0 in int array) and the column 
			//(position 1 in int array).
		}while (board[row][col] != EMPTY);
		int[] chosenEmptySquare = {row, col};
		return chosenEmptySquare;
	}

	public static void displayUserMovePrompt()
	{
		StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 20)); // Font SIZE!
		StdDraw.setPenColor(Color.WHITE);
		//StdDraw.rectangle(0.5, 0.03, 0.2, 0.2);
		//StdDraw.text(0.5, 0.03, "Comp Move");
		StdDraw.text(0.5, 0.03, "Comp Move");
		StdDraw.setPenColor();
		StdDraw.text(0.5, 0.03, "User Move");
		StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 64)); // Font SIZE!
	}

	public static void displayCompMovePrompt()
	{
		StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 20)); // Font SIZE!
		StdDraw.setPenColor(Color.WHITE);
		//StdDraw.rectangle(0.5, 0.03, 0.2, 0.2);
		//StdDraw.text(0.5, 0.03, "User Move");
		StdDraw.text(0.5, 0.03, "User Move");
		StdDraw.setPenColor();
		StdDraw.text(0.5, 0.03, "Comp Move");
		StdDraw.setFont(new Font("SansSerif", Font.PLAIN, 64)); // Font SIZE!
	}

	public static int choicePopUp(int[][] board)
	{
		int number;
		do 
		{
			String s = JOptionPane.showInputDialog(null, "Enter your number choice:");
			number = Integer.parseInt(s);
		}
		while (number % 2 == 1 || (hasANumberBeenUsed(board, number)) || (number >= 10) || (number==0));

		return number;
	}

	public static int pickingNumberComp(boolean[] booleanArray, int[][] board)
	{
		int number;

		do 
		{
			number = random.nextInt(9) + 1;
		}
		while (number % 2 == 0 || hasANumberBeenUsed(board, number));

		booleanArray[(number-1)/(2)] = false;
		return number;
	}

	public static boolean hasANumberBeenUsed(int[][] board, int chosenNumber)
	{
		for (int row = 0; row < 3; row++)
		{
			for (int col = 0; col < 3; col++)
			{
				if (board[row][col] == chosenNumber)
				{
					return true;
				}
			}
		}
		return false;
	}

	public static int[] computerToWin(boolean booleanArray[], int board[][])
	{
		{
			for (int i = 0; i < 5; i++)
			{
				// Below: check: if the position in the array (from 0 to 4), if the value in the boolean array
				// is true then continue within:
				if (booleanArray[i])
				{
					// Get the actual number
					int number = (i*2) + 1;

					for (int row = 0; row < 3; row++)
					{
						// Below: check to see if exactly 1 of the squares is empty and two are occupied
						if (!(board[row][0] != 0 || board[row][1] != 0 || board[row][2] != 0))
						{
							int sum = board[row][0] + board[row][1] + board[row][2];

							for (int col = 0; col < 3; col++)
							{
								// Below: if the two cells in question and the candidate (candidate = number 
								// to potentially maybe be put in) equal to 15 then go within:
								if (sum + number == 15)
								{
									// Return that specific row,col combination cause this will let the computer win
									int[] itelligentChosenEmptySquare = {row, col};
									return itelligentChosenEmptySquare;
								}
							}
						}
					}
				}

				for (int j = 0; j < 5; j++)
				{
					// Below: check: if the position in the array (from 0 to 4), if the value in the boolean array
					// is true then continue within:
					if (booleanArray[j])
					{
						// Get the actual number
						int number = (j*2) + 1;

						for (int col = 0; col < 3; col++)
						{
							// Below: check to see if exactly 1 of the squares is empty and two are occupied
							if (!(board[0][col] != 0 || board[1][col] != 0 || board[2][col] != 0))
							{
								int sum = board[0][col] + board[1][col] + board[2][col];

								for (int row = 0; row < 3; row++)
								{
									// Below: if the two cells in question and the candidate (candidate = number 
									// to potentially maybe be put in) equal to 15 then go within:
									if (sum + number == 15)
									{
										// Return that specific row,col combination cause this will let the computer win
										int[] itelligentChosenEmptySquare = {row, col};
										return itelligentChosenEmptySquare;
									}
								}
							}
						}
					}
				}








			}
		}

		
		
		
		{
			for (int row = 0; row < 3; row++)
			{
				int sum = board[row][0] + board[row][1] + board[row][2];
				if (sum == -2)
				{
					for (int col = 0; col < 3; col++)
					{
						while (board[row][col] == EMPTY)
						{
							int[] itelligentChosenEmptySquare = {row, col};
							return itelligentChosenEmptySquare;
						}		
					}
				}
			}

			for (int col = 0; col < 3; col++)
			{
				int sum = board[0][col] + board[1][col] + board[2][col];
				if (sum == -2)
				{
					for (int row = 0; row < 3; row++)
					{
						while (board[row][col] == EMPTY)
						{
							int[] itelligentChosenEmptySquare = {row, col};
							return itelligentChosenEmptySquare;
						}		
					}
				}
			}
		}

		int sum = board[0][0] + board[1][1] + board[2][2];
		int sum2 = board[0][2] + board[1][1] + board[2][0];

		if (sum == -2)
		{
			if (board[0][0] == EMPTY || board[1][1] == EMPTY || board [2][2] == EMPTY)
			{
				for (int i = 0; i < 3; i++)
					if (board[i][i] == EMPTY)
					{
						int[] itelligentChosenEmptySquare = {i, i};
						return itelligentChosenEmptySquare;
					}
			}

			if (sum2 == -2)
			{
				if (board[0][2] == EMPTY || board[1][1] == EMPTY || board [2][0] == EMPTY)
				{
					for (int j = 0; j < 3; j++)
						for (int k = 0; k < 3; k++)
						{
							while (j+k == 2)
							{
								if (board[j][k] == EMPTY)
								{
									int[] itelligentChosenEmptySquare = {j, k};
									return itelligentChosenEmptySquare;
								}
							}
						}
				}
			}
		}
		return null;
	}
}