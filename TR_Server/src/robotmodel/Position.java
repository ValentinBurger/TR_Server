package robotmodel;

/**
 * 
 * @author promet
 *
 */

/**
 * Enum of position in the scheduler
 */
public enum Position 
{
	ENTREE		(0),
	STOCKAGE	(1),
	CONFLICT	(2),
	SORTIE		(3);
	
	private final int ID;
	
	/**
	 * Default constructor
	 * @param pID
	 */
	Position(int pID) 
	{
		this.ID = pID;
	}
	
	/**
	 * get enum ID
	 * @return
	 */
	public int getID() { return this.ID; }
	
}