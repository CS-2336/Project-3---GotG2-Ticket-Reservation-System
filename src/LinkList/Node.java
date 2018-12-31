/*
 	Name: Naman Gangwani
 	NetID: nkg160030
 */

package LinkList;

public abstract class Node {
	int row, seat; // Stores the seat's row and seat values
	
	/** Overloaded constructor with row and seat keys **/
	public Node(int row, int seat)
	{
		this.row = row;
		this.seat = seat;
	}
	
	/** Getter method for row **/
	public int getRow()
	{
		return row;
	}
	
	/** Getter method for seat **/
	public int getSeat()
	{
		return seat;
	}
	
	/** Setter method for row **/
	public void setRow(int row)
	{
		this.row = row;
	}
	
	/** Setter method for seat **/
	public void setSeat(int seat)
	{
		this.seat = seat;
	}
}