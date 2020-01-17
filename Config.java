
public class Config {

    /**
     * A set of caves to search through.  The first one is provided.
     * TODO add at least 2 more caves.
     */
	public static final char[][][] CAVES= new char[][][] {
	    { 
    		{ ' ', ' ', ' ', ' ', ' ' }, 
    		{ ' ', 'b', ' ', 'p', ' ' }, 
    		{ ' ', 'p', 'w', ' ', ' ' },
    		{ ' ', ' ', 'b', ' ', ' ' }, 
    		{ ' ', ' ', ' ', 'b', 'p' } 
    	},
	    { 
            { 'b', ' ', 'b', ' ', 'b' }, 
            { ' ', ' ', ' ', 'p', ' ' }, 
            { ' ', 'p', ' ', ' ', ' ' },
            { 'w', ' ', ' ', ' ', 'p' }, 
            { ' ', ' ', ' ', ' ', ' ' } 
        },
	    { 
            { ' ', ' ', 'p', ' ', 'b' }, 
            { ' ', ' ', ' ', 'p', ' ' }, 
            { ' ', 'p', ' ', ' ', 'p' },
            { ' ', ' ', ' ', 'w', ' ' }, 
            { ' ', 'b', ' ', 'b', ' ' }, 
            { ' ', ' ', ' ', ' ', 'p' }
        }
	};

	/**
	 * The methods in WumpusCaves should refer to these constants,
	 * e.g., Config.PIT, and not the literals themselves.
	 */
	public static final char PIT = 'p';
	public static final char WUMPUS = 'w';
	public static final char BAT = 'b';
	public static final String GRAB_EQUIPMENT = "g";
	
	
	/**
	 * The number of perceptions and the indexes in a perceptions
	 * array.
	 */
	public static final int NUM_PERCEPTIONS = 3;
	public static final int PERCEIVE_PIT = 0;
	public static final int PERCEIVE_BAT = 1;
	public static final int PERCEIVE_CHILD = 2;
	
	/**
	 * indices for location arrays
	 */
	public static final int RESCUE_LOCATION = 0;
	public static final int CHILD_LOCATION = 1;
	public static final int X_DIRECTION = 1;
	public static final int Y_DIRECTION = 0;
	public static final int DIRECTION_MAX = 0;
	public static final int X_COORDINATE = 0;
	public static final int Y_COORDINATE = 1;
	
    /**
     * Random number generator SEED. Passed to the random number generator
     * to get repeatable random numbers which can aid with debugging.
     */
    public static final int SEED = 123;





}
