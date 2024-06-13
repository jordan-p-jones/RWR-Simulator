package rwr.sim;

/**
 * Enum to represent different threat states an RWR
 * contact can be in.
 */
public enum Threat
{
	LOW("LOW"),
	HIGH("HGH"),
	LOCK("LCK"),
	LAUNCH("LCH");
	
	private final String code;
	
	private Threat( final String code )
	{
		this.code = code;
	}
	
	/**
     * Simple Getter for code property.
     *
     * @return the code
     */
    public String getCode()
    {
        return this.code;
    }
}