package model;

/**
 * This program represents a parent class for message objects which share common behaviors.
 * 
 * @author Conner Canning
 * @version 7 March 2019
 */
public abstract class AbstractMessage implements Message {

    /** Time stamp of the message. */
    private final int myTimeStamp;
    
    /** The type of message. */
    private final String myType;
    
    /**
     * Constructs an AbstractMessage. 
     * Message type is one of three found in Message interface, 
     * Telemety, Leaderboard, or Crossing. Message type is identified 
     * by string constants found in Message interface. 
     * 
     * @param theType the message type for this message
     * @param theTimeStamp the time stamp for this message
     */
    public AbstractMessage(final String theType, final int theTimeStamp) {
        myType = theType;
        myTimeStamp = theTimeStamp;
    }
    
    /**
     * Returns the time stamp for this message.
     * 
     * @return the time stamp for this message
     */
    public int getTime() {
        return myTimeStamp;
    }
    
    /**
     * Returns the type of message this message is.
     * 
     * @return the message type for this message
     */
    public String getType() {
        return myType;
    }
    
    /**
     * Returns String representation for this message. 
     * 
     * @return the String representation for this message
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(myType);
        sb.append(':');
        sb.append(myTimeStamp);
        return sb.toString();
    }
}
