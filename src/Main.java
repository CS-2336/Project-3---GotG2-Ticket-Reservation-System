/*
 	Name: Naman Gangwani
 	NetID: nkg160030
 	
 	Recursive Function Call: Line 123
 	Recursive Function Definition: Line 281
 */

import LinkList.*;
import java.util.Scanner;
import java.io.*;

public class Main {
	
	public static LinkedList[][] auditoriums = new LinkedList[3][2]; // Keeps track of empty and reserved seats for all auditoriums
	public static int[][] numRowsAndSeats = new int[3][2]; // Keeps track of the number of rows and seats in each auditoriums
	
	public static void main(String[] args) throws FileNotFoundException
	{
		Scanner input = new Scanner(System.in);
		
		// Initializes all LinkedLists and reads in all the files
		for (int i = 0; i < auditoriums.length; i++)
		{
			for (int j = 0; j < auditoriums[0].length; j++)
				auditoriums[i][j] = new LinkedList();
			readAuditorium(new Scanner(new File("A"+(i+1)+".txt")), i);
		}
		
		int menuChoice; // User choice for auditorium
		do
		{
			System.out.println("1. Reserve Seats\n2. View Auditorium\n3. Exit\n");
			do
			{
				menuChoice = Integer.parseInt(validateInput("Enter a menu option number: ", int.class, input));
				if (menuChoice < 1 || menuChoice > 3)
					System.out.println("\n** Please only choose options 1-3.");
			} while (menuChoice < 1 || menuChoice > 3); // Prompts user to enter a menu choice until it is valid
			
			int chosen = 0; // Keeps track of the user's choice of auditorium
			if (menuChoice != 3) // If the user does not choose to exit
			{
				System.out.println("\n1. Auditorium 1\n2. Auditorium 2\n3. Auditorium 3\n");
				do
				{
					chosen = Integer.parseInt(validateInput("Pick an auditorium: ", int.class, input));
					if (chosen < 1 || chosen > 3)
						System.out.println("\n** Please only choose auditoriums 1-3.");
				} while (chosen < 1 || chosen > 3); // Prompts user to enter an auditorium number until it is valid

				displayAuditorium(chosen - 1); // Displays the chosen auditorium
			}
			
			if (menuChoice == 1)
			{
				int chosenRow; // Keeps track of the user's choice of row
				do
				{
					chosenRow = Integer.parseInt(validateInput("Enter a row number: ", int.class, input));
					// Checks to see if it's outside the bounds of the auditorium
					if (chosenRow < 1 || chosenRow > numRowsAndSeats[chosen - 1][0])
						System.out.println("\n** Please only choose a valid row number.");
				} while (chosenRow < 1 || chosenRow > numRowsAndSeats[chosen - 1][0]); // Prompts user to enter a row number until it is valid
				
				int chosenColumn; // Keeps track of the user's starting seat
				do
				{
					chosenColumn = Integer.parseInt(validateInput("Enter a starting seat number: ", int.class, input));
					// Checks to see if it's outside the bounds of the auditorium
					if (chosenColumn < 1 || chosenColumn > numRowsAndSeats[chosen - 1][1]) 
						System.out.println("\n** Please only choose a valid starting seat number.");
				} while (chosenColumn < 1 || chosenColumn > numRowsAndSeats[chosen - 1][1]); // Prompts user to enter a starting seat number until it is valid
				
				int numTickets; // Keeps track of the number of sequential seats the user wants to reserve
				do
				{
					System.out.print("Enter the number of tickets: ");
					numTickets = input.nextInt();
					// Checks to see if the user is requesting a valid amount of seats based the open seats in the row
					if (numTickets < 0)
						System.out.println("\n** Please only enter a positive number of tickets.");
				} while (numTickets < 0); // Prompts user to enter a number of tickets until it is valid
				
				int[] seats = getSeats(chosenRow, chosenColumn, numTickets, chosen - 1);
				if (seats != null) // If there are available seats
				{
					if (seats[0] == chosenRow && seats[1] == chosenColumn) // If the user-inputed seats are valid
					{
						reserveSeats(seats[0], seats[1], numTickets, chosen - 1); // Reserve those seats
						System.out.println("\nSeat(s) successfully reserved!");
					} else
					{
						// Prompts user to accept the offer for best available seats
						if (numTickets > 1)
							System.out.println("\nBest available seats: Row "+seats[0]+"; Seats "+seats[1]+"-"+(seats[1] + numTickets - 1));
						else
							System.out.println("\nBest available seats: Row "+seats[0]+"; Seat "+seats[1]);
						System.out.print("Would you like to reserve these seats? (Y/N): ");
						String answer = input.next();
						if (answer.toUpperCase().equals("Y"))
						{
							reserveSeats(seats[0], seats[1], numTickets, chosen - 1); // Reserves the seats if the user answers Y
							System.out.print("\nSeat(s) successfully reserved!");
						}
					}
				}
				else
					System.out.print("\nCould not find best available seat(s).");
			}
			
			if (menuChoice != 3)
				System.out.println("\nReturning to menu...\n");
		} while (menuChoice != 3); // Continues offering options until the user chooses to exit
		
		int[] total = new int[2]; // Keeps track of total number of 
		System.out.println("\n\t      Seats Reserved    Open Seats    Ticket Sales\n");
		for (int i = 1; i <= 3; i++) // Loops through all the files
		{
			PrintWriter auditorium = new PrintWriter("A"+i+".txt");
			auditorium.print(""); // Clears the current file
			auditorium.flush();
			updateFile(auditorium, new DoubleLinkNode(1, 1), i - 1); // Updates the file starting from row 1, seat 1
			auditorium.close();
			// Prints auditorium number, its number of reserved seats, number of open seats, and ticket sales in dollars
			System.out.printf("Auditorium "+i+"\t    "+auditoriums[i - 1][1].getSize()+"\t\t    "+auditoriums[i - 1][0].getSize()+"\t\t$%.2f\n", (double)(auditoriums[i - 1][1].getSize()*7));
			total[0] = total[0] + auditoriums[i - 1][1].getSize();
			total[1] = total[1] + auditoriums[i - 1][0].getSize();
		}
		
		// Prints the total number of seats reserved, open seats, and ticket sales in dollars
		System.out.printf("\n  TOTAL \t    "+total[0]+"\t\t    "+total[1]+"\t\t$%.2f", (double)(total[0]*7));
		input.close();
	}
	
	/** Method for validating input **/
	public static String validateInput(String message, Class<?> cls, Scanner input)
	{
		while (true) // Infinitely asks for input until valid input is given
		{
			try
			{
				System.out.print(message);
				String in = input.next();
				if (cls == int.class) // If it's looking for an int
				{
					int checkIfThrowsException = Integer.parseInt(in); // Tests with a case that could possibly throw an exception
					return String.valueOf(checkIfThrowsException);
				} else if (cls == double.class) // If it's looking for a double
				{
					double checkIfThrowsException = Double.parseDouble(in); // Tests with a case that could possibly throw an exception
					return String.valueOf(checkIfThrowsException);
				}
			} catch (Exception e)
			{
				// Notifies user to input only of the valid type
				System.out.println("\n** Please only enter a value of type "+cls.getName()+"."); 
			}
		}
	}
	
	/** Method for reading in a given auditorium file into its proper LinkedLists **/
	public static void readAuditorium(Scanner auditorium, int chosen)
	{
		int numRows = 0;
		int numSeats = 0;
		while (auditorium.hasNextLine()) // While there are lines to read
		{
			String line = auditorium.nextLine();
			if (!line.equals(""))
			{
				numRows++;
				numSeats = line.length();
				// Loops through all the characters in the line and adds to appropriate LinkedList
				for (int i = 0; i < numSeats; i++)
					if (line.charAt(i) == '#')
						auditoriums[chosen][0].addNode(new DoubleLinkNode(numRows, i + 1));
					else
						auditoriums[chosen][1].addNode(new DoubleLinkNode(numRows, i + 1));
			}
		}
		// Updates the maximum values of its row and seats
		numRowsAndSeats[chosen][0] = numRows;
		numRowsAndSeats[chosen][1] = numSeats;
	}
	
	/** Method for printing out the given auditorium to the console **/
	public static void displayAuditorium(int chosen)
	{
		System.out.print("\n  ");
		// Prints top row of numbers
		int columnNumber = 0;
		for (int i = 0; i < numRowsAndSeats[chosen][1]; i++) 
		{
			columnNumber+=1;
			if (columnNumber == 10)
				columnNumber = 0; // Resets back to 0 when it reaches 10 to prevent double digits in labeling
			System.out.print(columnNumber);
		}
		
		System.out.println();
		for (int i = 0; i < numRowsAndSeats[chosen][0]; i++) // For each row
		{
			System.out.print((i + 1)+" "); // Prints row number
			for (int j = 0; j < numRowsAndSeats[chosen][1]; j++) // For each seat in the row
				if (auditoriums[chosen][0].find(new DoubleLinkNode(i + 1, j + 1)) != null) // If the seat can be found in the emtpy seats LinkedList
					System.out.print("#"); // Print # if empty seat
				else
					System.out.print("."); // Print . if reserved seat
			System.out.println();
		}
		System.out.println();
	}
	
	/** Method for retrieving the seats **/
	public static int[] getSeats(int row, int seat, int numTickets, int chosen)
	{
		int[] reservation = {row, seat}; // User inputed seats
		DoubleLinkNode chosenSeat = auditoriums[chosen][0].find(new DoubleLinkNode(row, seat));
		if (chosenSeat != null) // If the chosen seat isn't already reserved
		{
			int numSeats = 1; // Keeps track of number of seats to match with number of tickets
			DoubleLinkNode curr = chosenSeat;
			// Searches through empty seats LinkedList until number of tickets is reached or chosen seats are not available
			while (curr.getNext() != null && curr.getNext().getRow() == curr.getRow() && curr.getNext().getSeat() == curr.getSeat() + 1 && numSeats < numTickets)
			{
				numSeats++;
				curr = curr.getNext(); // Increments to the next seat
			}
			
			if (numSeats == numTickets) // If the chosen seat is already valid and doesn't need best available seats
				return reservation; // Return the chosen seats
			else
				return getBestAvailable(numTickets, chosen); // Finds and returns best available seats
		} else
			return getBestAvailable(numTickets, chosen); // Finds and returns best available seats
	}
	
	/** Method for getting the best available seats in a given auditorium based on the number of tickets **/
	public static int[] getBestAvailable(int numTickets, int chosen)
	{
		int[] reservation = null;
		DoubleLinkNode curr = auditoriums[chosen][0].getHead();
		int centerSeat = (int) Math.ceil((double) numRowsAndSeats[chosen][1]/2); // Finds the center seat
		int centerRow = (int) Math.ceil((double) numRowsAndSeats[chosen][0]/2); // Finds the center row
		double distanceFromCenterSeat = 100000000, distanceFromCenterRow = 100000000;
		while (curr != null)
		{
			DoubleLinkNode curr2 = curr;
			int numSeats = 1;
			while (curr2.getNext() != null && curr2.getNext().getRow() == curr2.getRow() && curr2.getNext().getSeat() == curr2.getSeat() + 1 && numSeats < numTickets)
			{
				numSeats++;
				curr2 = curr2.getNext();
			}
			
			// If current seat matches given criteria and is closest to the center of the auditorium (seat-wise and row-wise)
			if (numSeats == numTickets) 
				if (Math.sqrt(Math.pow(centerSeat - curr.getSeat(), 2) + Math.pow(centerRow - curr.getRow(), 2)) <= distanceFromCenterSeat && Math.abs(centerRow - curr.getRow()) <= distanceFromCenterRow)
				{
					distanceFromCenterSeat = Math.sqrt(Math.pow(centerSeat - curr.getSeat(), 2) + Math.pow(centerRow - curr.getRow(), 2)); // Updates distance from center seat to that of the current best seat
					distanceFromCenterRow = Math.abs(centerRow - curr.getRow()); // Updates distance from center row to that of the current best seat
					// Updates reservation to the current best seats
					reservation = new int[2];
					reservation[0] = curr.getRow();
					reservation[1] = curr.getSeat();
				}
			curr = curr.getNext();
		}
		return reservation; // Null is returned if no seats were found in availability based on given criteria
	}
	
	/** Method for reserving seats based on given starting seat and number of tickets in a given auditorium **/
	public static void reserveSeats(int row, int seat, int numTickets, int chosen)
	{
		for (int i = 0; i < numTickets; i++)
			auditoriums[chosen][1].addNode(auditoriums[chosen][0].deleteNode(new DoubleLinkNode(row, seat + i))); // Deletes seat from empty seats and adds it to reserved seats
	}
	
	/** Method to recursively update the given auditorium file **/
	public static void updateFile(PrintWriter auditorium, DoubleLinkNode n, int chosen)
	{
		if (auditoriums[chosen][0].find(n) != null) // If current seat is in the empty seats LinkedList
			auditorium.print("#");
		else // Otherwise, if current seat is in the reserved seats LinkedList
			auditorium.print(".");
		
		int row = n.getRow(), seat = n.getSeat() + 1; // Increments to the next seat
		// Moves to next row if all seats in the current row have been printed
		if (seat > numRowsAndSeats[chosen][1])
		{
			auditorium.println();
			row++;
			seat = 1;
		}
		
		if (row <= numRowsAndSeats[chosen][0]) // If it hasn't already printed the last seat
			updateFile(auditorium, new DoubleLinkNode(row, seat), chosen); // Print the next seat
	}
}