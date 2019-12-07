package model;

/**
 * This program represents a leaderboard message from a race.
 * 
 * @author Conner Canning
 * @version 7 March 2019
 */
public final class LeaderboardMessage extends AbstractMessage {
    
    /** The message type for all line crossing messages, defined in Message interface. */
    public static final String MY_TYPE = Message.LEAD_START;

    /** A common string which separates elements of a crossing message. */
    private static final String SEPARATOR = ":";
    
    /** Stores the order of racers that this leaderboard message describes. */
    private final String[] myOrder;
    

    /**
     * Constructs a LeaderboardMessage object. 
     * 
     * @param theTimeStamp the time for this leaderboard message
     * @param theIDs the ordered array containing racer ids in their race positions
     */
    public LeaderboardMessage(final int theTimeStamp, final String[] theIDs) {
        super(MY_TYPE, theTimeStamp);
        myOrder = theIDs.clone();
    }
    
    /**
     * Returns an array holding the order of racers described by this leaderboard message. 
     * 
     * @return string array holding order of racers
     */
    public String[] getOrder() {
        return myOrder.clone();
    }
    
    /**
     * Returns a String representation of this LeaderboardMessage.
     * 
     * @return a string representing this message
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        for (final String s : myOrder) {
            sb.append(SEPARATOR);
            sb.append(s);
        }
        return sb.toString();
    }
    
    
}
