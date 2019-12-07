package model;

/**
 * This program represents a Racer in a race.
 * 
 * @author Conner Canning
 * @version 7 March 2019
 */
public final class Racer {

    /** The id for this racer. */
    private final int myID;
    
    /** The name of this racer. */
    private final String myName;
    
    /** The starting distance of this racer. */
    private final double myStartDistance;
    
    /**
     * Creates a new Racer object. 
     * 
     * @param theID the racer's id
     * @param theName the racer's name
     * @param theStartDistance the starting distance for this racer
     */
    public Racer(final int theID, final String theName, final double theStartDistance) {
        myID = theID;
        myName = theName;
        myStartDistance = theStartDistance;
    }
    
    /**
     * Returns this racer's id. 
     * 
     * @return this racer's id
     */
    public int getID() {
        return myID;
    }
    
    /**
     * Returns this racer's name.
     * 
     * @return this racer's name
     */
    public String getName() {
        return myName;
    }
    
    /**
     * Returns the starting distance for this racer. 
     * 
     * @return start distance for this racer
     */
    public double getStartDistance() {
        return myStartDistance;
    }
    
    
}
