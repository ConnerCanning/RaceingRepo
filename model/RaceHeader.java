package model;

import controller.TimeLabel;
import java.text.DecimalFormat;

/**
 * This program holds race information from the header of a race file.
 * 
 * @author Conner Canning
 * @version 9 March 2019
 */
public class RaceHeader {

    /** The separator for formatted. */
    public static final String SEPARATOR = ":";
    
    /** The number of milliseconds in a second. */
    public static final int MILLIS_PER_SEC = 1000;
    
    /** The number of seconds in a minute. */
    public static final int SEC_PER_MIN = 60;
    
    /** The number of minute in a hour. */
    public static final int MIN_PER_HOUR = 60;
        
    /** A formatter to require at least 1 digit, leading 0. */
    public static final DecimalFormat ONE_DIGIT_FORMAT = new DecimalFormat("0");
    
    /** A formatter to require at least 2 digits, leading 0s. */
    public static final DecimalFormat TWO_DIGIT_FORMAT = new DecimalFormat("00");
    
    /** A formatter to require at least 3 digits, leading 0s. */
    public static final DecimalFormat THREE_DIGIT_FORMAT = new DecimalFormat("000");
    
    /** New line character. */
    private static final String NEW_LINE = "\n";
    
    /** Educated guess at the likely required size of a StringBuilder used in toString(). */
    private static final int TO_STRING_SIZE = 80;
    
    /** The race name. */
    private final String myRaceName;
    
    /** The track type. */
    private final String myTrack;
    
    /** The track width. */
    private final int myWidth;

    /** The track height. */
    private final int myHeight;
    
    /** The track lap distance. */
    private final double myDistance;

    /** The total race time in milliseconds. */
    private final int myTime;
    
    /** The number of race participants. */
    private final int myParticipants;
    
    /**
     * Constructs a RaceHeader object with given information from a race file. 
     * 
     * @param theName the race name
     * @param theTrack the track type
     * @param theWidth the track width
     * @param theHeight the track height
     * @param theDist the track lap distance
     * @param theTime the total race time
     * @param theParticipants the number of participants
     */
    public RaceHeader(final String theName, final String theTrack, final int theWidth, 
                      final int theHeight, final double theDist, final int theTime, 
                      final int theParticipants) {
        myRaceName = theName;
        myTrack = theTrack;
        myWidth = theWidth;
        myHeight = theHeight;
        myDistance = theDist;
        myTime = theTime;
        myParticipants = theParticipants;
    }
    
    /**
     * Returns the number of race participants. 
     * 
     * @return number of race participants
     */
    public int getNumParticipants() {
        return myParticipants;
    }

    /**
     * Returns the total race time.
     * 
     * @return the total race time
     */
    public int getTime() {
        return myTime;
    }
    
    /**
     * Returns the width of the track.
     * 
     * @return the race track width
     */
    public int getWidth() {
        return myWidth;
    }
    
    /**
     * Returns the width of the track.
     * 
     * @return the race track width
     */
    public int getHeight() {
        return myHeight;
    }
    
    /**
     * Returns the distance in the track.
     * 
     * @return the track lap distance 
     */
    public double getDistance() {
        return myDistance;
    }
    
    /**
     * Returns a String representing this RaceHeader.
     * 
     * @return a String representing this RaceHeader
     */
    public String toString() {
        final StringBuilder sb = new StringBuilder(TO_STRING_SIZE);
        sb.append("Race Name: ");
        sb.append(myRaceName);
        sb.append(NEW_LINE);
        sb.append("Track type: ");
        sb.append(myTrack);
        sb.append(NEW_LINE);
        sb.append("Total time: ");
        sb.append(TimeLabel.formatTime(myTime));
        sb.append(NEW_LINE);
        sb.append("Lap distance: ");
        sb.append(myDistance);
        return sb.toString();
    }

}
