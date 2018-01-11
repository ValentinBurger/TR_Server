package robotmodel;

/**
 * 
 * @author promet
 *
 */

public enum Position 
{
	ENTREE		(0),
	STOCKAGE	(1),
	CONFLICT	(2),
	SORTIE		(3);
	
	private final int ID;
	
	Position(int pID) 
	{
		this.ID = pID;
	}
	
	public int getID() { return this.ID; }
	
}