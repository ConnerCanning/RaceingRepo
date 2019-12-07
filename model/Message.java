package model;

/**
 * Defines behaviors for Message Objects. 
 * 
 * @author Conner Canning
 * @version 7 March 2019
 */
public interface Message {

    /** The beginning string which identifies a Telemetry Message. */
    String TELEM_START = "$T";
    
    /** The beginning string which identifies Leaderboard Message. */
    String LEAD_START = "$L";
    
    /** The beginning string which identifies a Finish Line Message. */
    String CROSSING_START = "$C";
    
    /**
     * Returns a String representation of this Message.
     * 
     * @return String representing this Message
     */
    String toString();
    
    /**
     * Returns the type of message this message is.
     * 
     * @return String identifying this message
     */
    String getType();

}
