package model;

/**
 * This program represents a line crossing message from a race.
 * 
 * @author Conner Canning
 * @version 7 March 2019
 */
public class CrossingMessage extends AbstractMessage {
    
    /** The message type for all line crossing messages, defined in Message interface. */
    public static final String MY_TYPE = Message.CROSSING_START;

    /** A common string which separates elements of a crossing message. */
    private static final String SEPARATOR = ":";

    /** The racer id for the racer this crossing message describes. */
    private final int myID;
    
    /** The lap being started by a racer which this crossing message describes. */
    private final int myLap;
    
    /** Stores whether the racer this crossing message describes has completed the race. */
    private final boolean myFinish;
        
    /**
     * Constructs a line crossing message object.
     * 
     * @param theTimeStamp the time for this crossing
     * @param theID the id for the crossing racer
     * @param theLap the lap this racer is just starting
     * @param theFinish if this racer is finished with the race, true means yes
     */
    public CrossingMessage(final int theTimeStamp, final int theID, final int theLap, 
                           final boolean theFinish) {
        super(MY_TYPE, theTimeStamp);
        myID = theID;
        myLap = theLap;
        myFinish = theFinish;
    }
    
    /**
     * Returns the racer ID.
     * 
     * @return the racer id
     */
    public int getID() {
        return myID;
    }
    
    /**
     * Returns the lap the racer is beginning being described by this crossing message.
     * 
     * @return the lap the racer is now beginning
     */
    public int getLap() {
        return myLap;
    }
    
    /**
     * Returns if the racer has completed the race or not.
     * 
     * @return true if completed, false otherwise
     */
    public boolean isFinished() {
        return myFinish;
    }
    
    /**
     * Returns a string representation for this Message.
     * 
     * @return the string representing this crossing message
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(SEPARATOR);
        sb.append(this.getID());
        sb.append(SEPARATOR);
        sb.append(this.getLap());
        sb.append(SEPARATOR);
        sb.append(this.isFinished());
        return sb.toString();
    }

    
    
}
