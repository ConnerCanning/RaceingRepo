package model;

/**
 * This program represents a telemetry message from a race.
 * 
 * @author Conner Canning
 * @version 7 March 2019
 */
public final class TelemetryMessage extends AbstractMessage {
    
    /** The message type for all line crossing messages, defined in Message interface. */
    private static final String MY_TYPE = Message.TELEM_START;
    
    /** A common string which separates elements of a crossing message. */
    private static final String SEPARATOR = ":";

    
    /** The racer ID for this message. */
    private final int myID;
    
    /** The distance this racer has traveled this lap. */
    private final double myDist;
    
    /** The lap this racer is completing. */
    private final int myLap;
       
    /**
     * Constructs a new TelemetryMessage.
     * 
     * @param theTimeStamp the time for this message
     * @param theID the racer ID this message describes
     * @param theDist the distance the racer has run this lap
     * @param theLap the lap the racer is completing 
     */
    public TelemetryMessage(final int theTimeStamp, final int theID, 
                            final double theDist, final int theLap) {
        super(MY_TYPE, theTimeStamp);
        myID = theID;
        myDist = theDist;
        myLap = theLap;
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
     * Returns the distance this racer has traveled this lap in this message.
     * 
     * @return the distance the racer has traveled
     */
    public double getDist() {
        return myDist;
    }
    
    /**
     * Returns the lap the racer is completing in this TelemetryMessage.
     * 
     * @return the lap the racer is completing
     */
    public int getLap() {
        return myLap;
    }
    
    /**
     * Returns a string representation for this Message.
     * 
     * @return a string representing this message
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(SEPARATOR);
        sb.append(this.getID());
        sb.append(SEPARATOR);
        sb.append(this.getDist());
        sb.append(SEPARATOR);
        sb.append(this.getLap());
        return sb.toString();
    }
}
