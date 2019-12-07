package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

/**
 * This program runs the Model for the Race Day program.
 * 
 * @author Conner Canning
 * @version 9 March 2019
 */
public class RaceModel implements PropertyChangeEnabledRaceControls {
    
    /** Template file for testing against a potential race file. */
    private static final File TEMPLATE_FILE = new File("./race_files/TEMPLATE.rce");
    
    /** Number of elements in a telemetry message. */
    private static final int THINGS_IN_TELEM = 5;
    
    /** Maximum racer ID number, exclusive. */
    private static final int MAX_RACER_NUMBER = 100;
    
    /** Common separation string for message values. */
    private static final String SEPARATOR = ":";
    
    /** Holds the racers from a loaded race file. */
    private final List<Racer> myRacers;
    
    /** Hold whether a racer has completed the race or not. */
    private final Map<Integer, Boolean> myFinished = new HashMap<Integer, Boolean>();
    
    /** Holds the race participant IDs. */
    private final List<Integer> myParticipantNumbers = new ArrayList<Integer>();
    
    /** Holds the messages from the loaded race file. */
    private final List<List<Message>> myMessages = new ArrayList<List<Message>>();
    
    /** Holds the model race time. */
    private int myTime;
    
    /** Manager for Property Change Listeners. */
    private final PropertyChangeSupport myPcs;

    /** Holds the race header information for loaded race. */
    private RaceHeader myHeader;
    
    /** Holds which racers are currently toggled off. */
    private final Set<Integer> myToggledOffRacers;

    /**
     * Constructs a RaceModel object.
     */
    public RaceModel() {
        myPcs = new PropertyChangeSupport(this);
        myTime = 0;
        myRacers = new ArrayList<>();
        myToggledOffRacers = new HashSet<>();
    }

    @Override
    public void loadRace(final File theRaceFile) throws IOException {
        myTime = 0;
        myPcs.firePropertyChange(PROPERTY_TIME, null, myTime);
        // ^ Fixes issue while loading race to a shorter race 
        // at current race time higher than max of new loading shorter race
        myPcs.firePropertyChange(PROPERTY_SENDING_STRING, null, 
                                 "File is loading! Please wait :)\n");
        final Scanner in = new Scanner(theRaceFile);
        final Scanner template = new Scanner(TEMPLATE_FILE);
        boolean valid = checkHeader(in, template);
        if (valid) {
            checkParticipants(in);
            valid = checkMessages(in);
        }
        if (!valid) {
            throw new IOException("Bad File.");
        }
        // Now that the race file is good, clear our old toggle list
        myToggledOffRacers.clear();
        // and set time equal to 0 in case it was higher in previous race
        // than this new race even allows
        changeTime(0);
    }

    /**
     * Determines if a file holds valid race messages.
     * 
     * @param theTest a scanner reading a file to be tested
     * @return true if the file only holds valid messages, false otherwise
     */
    private boolean checkMessages(final Scanner theTest) {
        boolean valid = true;
        String testLine;
        myPcs.firePropertyChange(PROPERTY_SENDING_STRING, null, 
                                 "Loading messages now, almost done!\n");
        while (valid && theTest.hasNextLine()) {
            testLine = theTest.nextLine();
            if (testLine.startsWith("$T")) {
                valid = telemCheck(testLine);
            } else if (testLine.startsWith("$L")) {
                valid = leadCheck(testLine);
            } else if (testLine.startsWith("$C")) {
                valid = crossCheck(testLine);
            } else {
                valid = false;
            }              
        }
//        changeTime(0);
        myPcs.firePropertyChange(PROPERTY_RACER_INFO, null, myRacers);
        // ^ because some listeners only need racer info
        myPcs.firePropertyChange(PROPERTY_HEADER_INFO, null, myHeader); 
        // ^ race file loaded timing + giving header info

        return valid;
    }

    /**
     * Determines if a line crossing message string is valid / formatted correctly.
     * Builds / stores a CrossingMessage object if the string holds a valid message.
     * 
     * @param theTestLine the string to be tested for a line crossing message
     * @return true if the line holds a valid message, false otherwise
     */
    private boolean crossCheck(final String theTestLine) {
        boolean valid = true;
        final int locationTime = 1;
        final int locationID = 2;
        final int locationLap = 3;
        final int locationFinish = 4;
        final int crossMessageSize = 5;
        final String[] sizeCheck = theTestLine.split(SEPARATOR);
        valid = sizeCheck.length == crossMessageSize;
        final int time = Integer.parseInt(sizeCheck[locationTime]);
        final int id = Integer.parseInt(sizeCheck[locationID]);
        final int lap = Integer.parseInt(sizeCheck[locationLap]);
        final boolean finish = Boolean.parseBoolean(sizeCheck[locationFinish]);
        if (valid) {
            valid = validID(id) 
                            && time >= 0
                            && lap >= 1;
        }
        if (valid) { // no way you grade for whether someone has finished twice or not right?
            if (sizeCheck[locationFinish].equals("true")) {
                myFinished.put(Integer.parseInt(sizeCheck[2]), true);   
            } else if (sizeCheck[locationFinish].equals("false")) {
                myFinished.put(Integer.parseInt(sizeCheck[2]), false);
            } else {
                // Code could be changed from object storage to just a check
                // if the string said "true" or "false", else valid = false. 
                // Keeping this implementation for possibility of double finish checking.
                valid = false;
            }
        }     //removed the check if they've finished twice because 
              //messages continue after racers have completed their race laps
        myMessages.get(time).add(new CrossingMessage(time, id, lap, finish));
        return valid;
    }

    /**
     * Determines if a leaderboard message string is valid / formatted correctly.
     * Builds / stores a LeaderboardMessage object if the string holds a valid message.
     * 
     * @param theTestLine the string to be tested for a leaderboard message
     * @return true if the line holds a valid message, false otherwise
     */
    private boolean leadCheck(final String theTestLine) {
        boolean valid = true; 
        // timestamp positive//
        // each racer does exist//
        // size of sizeCheck = n+2//
        // no repeat racers in this list//
        //System.out.println(theTestLine);                ///////////////
        final String[] sizeCheck = theTestLine.split(SEPARATOR);
        final int participants = myHeader.getNumParticipants();
        valid = (sizeCheck.length == participants + 2)
                        && Integer.parseInt(sizeCheck[1]) >= 0; 
                        // size of line is good and positive timestamp     
        int counter = 0;        
        
        while (valid && counter < participants) {
            valid = validID(Integer.parseInt(sizeCheck[counter + 2])); // racers are registered
            counter++;
        } 
        counter = 0;
        final ArrayList<Integer> noRepeats = new ArrayList<Integer>();
        while (valid && counter < participants) {          
            // repeating is cheating, no double leaderboard appearance!
            if (noRepeats.contains(Integer.parseInt(sizeCheck[counter + 2]))) {
                valid = false;
            }
            noRepeats.add(Integer.parseInt(sizeCheck[counter + 2]));
            counter++;
        }
        if (valid) {
            final String[] leaderboard = new String[sizeCheck.length - 2];
            System.arraycopy(sizeCheck, 2, leaderboard, 0, sizeCheck.length - 2);
//            for (int i = 0; i < sizeCheck.length - 2; i++) {
//                leaderboard[i] = sizeCheck[i + 2];
//            } // not as effecient as System.arraycopy
            myMessages.get(Integer.parseInt(sizeCheck[1])).
                add(new LeaderboardMessage(Integer.parseInt(sizeCheck[1]), leaderboard));
        } 
        return valid; 
        
    }


    /**
     * Determines if a telemetry message string is valid / formatted correctly.
     * Builds / stores a TelemetryMessage object if the string holds a valid message. 
     * 
     * @param theTest the string to be tested for a telemetry message
     * @return true if the line is holds a valid message, false otherwise
     */
    private boolean telemCheck(final String theTest) {
        boolean valid = true;
        final int locationTime = 1;
        final int locationID = 2;
        final int locationDist = 3;
        final int locationLap = 4;
        final int time;
        final int id;
        final double dist;
        final int lap;
        final String[] sizeCheck = theTest.split(SEPARATOR);
        valid = sizeCheck.length == THINGS_IN_TELEM; 
        if (valid) {
            time = Integer.parseInt(sizeCheck[locationTime]);
            id = Integer.parseInt(sizeCheck[locationID]);
            dist = Double.parseDouble(sizeCheck[locationDist]);
            lap = Integer.parseInt(sizeCheck[locationLap]);

            valid = validID(id) && time >= 0 && lap >= 0;

            if (valid) {
                final int placeholder = sizeCheck[locationDist].indexOf('.');
                valid = sizeCheck[locationDist].substring(placeholder).length() 
                                == locationDist;
                // rounded to 2 decimal places
                if (valid) {
                    myMessages.get(time).add(new TelemetryMessage(time, id, dist, lap));
                }
            }
        }
        return valid;
    }

    /**
     * Determines if a given racer ID int appeared in the file header.
     * Used to determine if a message contains information about racers 
     * who were not noted in the header of the file.
     * 
     * @param theInID the racer ID to be tested
     * @return true if the ID is registered, false otherwise
     */
    private boolean validID(final int theInID) {
        return myParticipantNumbers.contains(theInID);
    }
    
    
    

    /**
     * Helper method to determine if a given file is properly formatted
     * to give racer information, throws an exception if the file does not
     * properly format racer information.
     * 
     * @param theTest scanner reading test file
     * @return true if the test file is formatted correctly, false otherwise
     */
    private boolean checkParticipants(final Scanner theTest) {
        String testLine;
        boolean valid = true;
        try {
            myRacers.clear();
            for (int i = 0; i < myHeader.getNumParticipants(); i++) {
                testLine = theTest.nextLine();
                eachParticipantLine(testLine);
            }

        } catch (final IOException e) {
            valid = false;
        } catch (final NumberFormatException e) {
            valid = false;
        } catch (final StringIndexOutOfBoundsException e) {
            valid = false;
        }   // I THINK that's all exceptions
        return valid;
    }
    
    /**
     * Builds Racer object if the given string is formatted correctly
     * to represent a race participant. Throws an exception if the 
     * given string is not formatted correctly.
     * 
     * @param theLine the line with an intended participant
     * @throws IOException if the line is not formatted correctly
     */
    private void eachParticipantLine(final String theLine) throws IOException {
        final String[] lineHolder;
        final int racerNumber;
        final String racerName;
        final double startDistance;
        if (theLine.charAt(0) == '#') {
            final String line = theLine.substring(1);
            lineHolder = line.split(SEPARATOR);
            racerNumber = Integer.parseInt(lineHolder[0]);
            if (racerNumber < MAX_RACER_NUMBER && racerNumber > 0) {
                myParticipantNumbers.add(racerNumber);
            } else {
                throw new IOException("this one here @4444");
            }
            racerName = lineHolder[1];
            startDistance = Double.parseDouble(lineHolder[2]);
            myRacers.add(new Racer(racerNumber, racerName, startDistance));
        } else {
            throw new IOException("this one @33333");
        }
    }
    
    /**
     * Checks the first 7 header lines of a document.
     * 
     * @param theTest Scanner of file to be tested for validity
     * @param theKey Scanner of template file
     * @return true if the files are similar, false otherwise
     * @throws IOException if there is any issue or error with file comparison
     */
    private boolean checkHeader(final Scanner theTest, final Scanner theKey) {
        boolean valid;
        String testLine;
        String templateLine;
        try {
            myPcs.firePropertyChange(PROPERTY_SENDING_STRING, null, 
                                     "Loading race information, please wait.\n");
            
            testLine = theTest.nextLine();
            templateLine = theKey.nextLine();
            final boolean checkName = 
                            testLine.substring(0, templateLine.length()).equals(templateLine);
            final String name = testLine.substring(templateLine.length());
            
            testLine = theTest.nextLine();
            templateLine = theKey.nextLine();
            final boolean checkTrack = 
                            testLine.substring(0, templateLine.length()).equals(templateLine);
            final String track = testLine.substring(templateLine.length());
            
            testLine = theTest.nextLine();
            templateLine = theKey.nextLine();
            final boolean checkWidth = 
                            testLine.substring(0, templateLine.length()).equals(templateLine);
            final int width = Integer.parseInt(testLine.substring(templateLine.length()));
            
            testLine = theTest.nextLine();
            templateLine = theKey.nextLine();
            final boolean checkHeight = 
                            testLine.substring(0, templateLine.length()).equals(templateLine);
            final int height = Integer.parseInt(testLine.substring(templateLine.length()));

            testLine = theTest.nextLine();
            templateLine = theKey.nextLine();
            final boolean checkDistance = 
                            testLine.substring(0, templateLine.length()).equals(templateLine);
            final int distance = Integer.parseInt(testLine.substring(templateLine.length()));

            testLine = theTest.nextLine();
            templateLine = theKey.nextLine();
            final boolean checkTime = 
                            testLine.substring(0, templateLine.length()).equals(templateLine);
            final int time = Integer.parseInt(testLine.substring(templateLine.length()));
            
            // Cyclic complexity workaround :(
            buildMessageArray(time);
            
            testLine = theTest.nextLine();
            templateLine = theKey.nextLine();
            final boolean checkParticipants = 
                            testLine.substring(0, templateLine.length()).equals(templateLine);
            final int numPart = Integer.parseInt(testLine.substring(templateLine.length()));
            
            myHeader = new RaceHeader(name, track, width, height, distance, time, numPart);
            
            valid = checkTrack && checkName && checkTime && checkWidth 
                            && checkHeight && checkDistance && checkTime 
                            && checkParticipants;

        } catch (final NumberFormatException e) {
            valid = false;
        } catch (final NoSuchElementException e) {
            valid = false;
        }
        return valid;
    }
    
    /** 
     * Helper method which instantiates the message holding data structure 
     * when the required size is known. 
     * 
     * @param theSize the number of messages that need to be stored
     */
    private void buildMessageArray(final int theSize) {
        // Clear myMessages from any previous file loadings
        myMessages.clear();
        // Build a large enough array to hold messages for every possible millisecond
        for (int i = 0; i < theSize + 1; i++) {
            myMessages.add(new ArrayList<Message>());
        }
    }

    @Override
    public void advance() {
        advance(1);
    }

    @Override
    public void advance(final int theMillisecond) {
        changeTime(myTime + theMillisecond);
    }

    @Override
    public void moveTo(final int theMillisecond) {
        if (theMillisecond < 0) {
            throw new IllegalArgumentException("ERROR_MESSAGE");
        }
        changeTime(theMillisecond);
    }
    
    /**
     * Helper method to change the value of time and notify observers. 
     * 
     * @param theMillisecond the time to change to
     */
    private void changeTime(final int theMillisecond) {
        final int old = myTime;
        myTime = theMillisecond;
        if (theMillisecond > myHeader.getTime()) {
            myTime = myHeader.getTime();
            myPcs.firePropertyChange(PROPERTY_RACE_COMPLETE, false, true);
        }
        myPcs.firePropertyChange(PROPERTY_TIME, old, myTime);
        
        if (old < myTime) {
            for (int i = old; i < myTime; i++) {
                final int numMess = myMessages.get(i).size();
                for (int j = 0; j < numMess; j++) {
                    final Message mess = myMessages.get(i).get(j);
                    if (isToggledOnMessage(mess)) {
                        myPcs.firePropertyChange(PROPERTY_NEW_MESSAGES, null,
                                             myMessages.get(i).get(j));    
                    }
                }                
            }
        } else {
            handleGoingBackwards(old);
        }    
    }
    
    /**
     * Helper method which handles the model time being changed backwards and
     * notifies observes. Property Change behavior is different when going 
     * backwards, only "the new most recent leaderboard and telemetry message" get fired. 
     * 
     * @param theOldTime the time the model held before changeTime was called
     */
    private void handleGoingBackwards(final int theOldTime) {
        boolean foundTelem = false;
        boolean foundLead = false;
        for (int i = myTime; i < theOldTime; i++) {
            for (final Message m : myMessages.get(i)) {
                if (!foundTelem && m.getType() == Message.TELEM_START 
                                && isToggledOnMessage(m)) {
                    myPcs.firePropertyChange(PROPERTY_NEW_MESSAGES, null, m);
                    foundTelem = true;
                }
                if (!foundLead && m.getType().equals(Message.LEAD_START)) {
                    myPcs.firePropertyChange(PROPERTY_NEW_MESSAGES, null, m);
                    foundLead = true;
                }
            }
        }
    }
    
    /**
     * Determines if a message should be shown to the controller.
     * Telemetry messages about toggled off racers should not be shown,
     * so this method will return false for those messages.
     * 
     * @param theMessage to be tested
     * @return false if the message should be censored, true otherwise
     */
    private boolean isToggledOnMessage(final Message theMessage) {
        boolean toReturn = true;
        if (theMessage.getType().equals(Message.TELEM_START)) {
            final TelemetryMessage telemMessage = (TelemetryMessage) theMessage;
            if (myToggledOffRacers.contains(telemMessage.getID())) {
                toReturn = false;
            }
        }
        return toReturn;
    }

    @Override
    public void toggleParticipant(final int theParticpantID, final boolean theToggle) {
        if (theToggle) {
            myToggledOffRacers.remove(theParticpantID);
        } else {
            myToggledOffRacers.add(theParticpantID);
        }
    }

    @Override
    public void addPropertyChangeListener(final PropertyChangeListener theListener) {
        myPcs.addPropertyChangeListener(theListener);
    }

    @Override
    public void addPropertyChangeListener(final String thePropertyName,
                                          final PropertyChangeListener theListener) {
        myPcs.addPropertyChangeListener(thePropertyName, theListener);
    }

    @Override
    public void removePropertyChangeListener(final PropertyChangeListener theListener) {
        myPcs.removePropertyChangeListener(theListener);
    }

    @Override
    public void removePropertyChangeListener(final String thePropertyName,
                                             final PropertyChangeListener theListener) {
        myPcs.removePropertyChangeListener(thePropertyName, theListener);
    }

}
