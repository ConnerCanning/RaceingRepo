package view;

import static model.PropertyChangeEnabledRaceControls.PROPERTY_TIME;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_RACER_INFO;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_NEW_MESSAGES;

import controller.TimeLabel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import model.LeaderboardMessage;
import model.Message;
import model.RaceModel;
import model.Racer;

/**
 * This program represents a view window for our race.
 * 
 * @author Conner Canning
 * @version 16 March 2019
 */
public final class ViewMainGUI extends JPanel implements PropertyChangeListener {
    
    /** A generated serial version UID for object Serialization. */
    private static final long serialVersionUID = 7202274548442597626L; 
    
    /** The icon size for the window icon for this view window. */
    private static final int ICON_SIZE = 24;
    
    /** The maximum possible number of racers in a race. */
    private static final int MAX_RACERS = 10;
    
    /** Padding for the border around leaderboard panel. */
    private static final Insets LEAD_BORDER = new Insets(0, 0, 0, 1);
    
    /** Text to accompany the race time. */
    private static final String TIME_TEXT = "Time: ";
    
    /** Text representing the title of this race view. */
    private static final String RACE_TRACK_NAME = "Race Track";
    
    /** Text representing the title of the leaderboard. */
    private static final String LEAD_TEXT = " Leaderboard Here:  ";

    /** Links racers with colors for consistent color on scoreboard and race track panel. */
    private final Map<Racer, Color> myMap = new HashMap<>();
    
    /** A race track panel to display the race. */
    private final RaceTrack myRaceTrack;
    
    /** A leaderboard panel to display the race leaderboard. */
    private final JPanel myLeaderboard;
    
    /** A status bar panel to display the race status. */
    private final JPanel myStatusBar;
    
    /** A label to hold the race time. */
    private final JLabel myTimeLabel = new JLabel();
    
    /** A label to hold race participants. */
    private final JLabel myParticipantLabel = new JLabel();   
    
    /**
     * Constructs a new ViewMainGUI which builds the view window for races.
     * 
     * @param theRaceModel a race model for our view window to display.
     */
    public ViewMainGUI(final RaceModel theRaceModel) {
        super(new BorderLayout());

        myRaceTrack = new RaceTrack(myMap);
        myLeaderboard = buildLeaderboard();
        myStatusBar = buildStatusBar();
        
        theRaceModel.addPropertyChangeListener(this);
        theRaceModel.addPropertyChangeListener(myRaceTrack);

        createAndShowGUI();
    }

    /**
     * Creates and shows the race view GUI.
     */
    public void createAndShowGUI() {
        final JFrame window = new JFrame(RACE_TRACK_NAME);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        this.add(myRaceTrack, BorderLayout.CENTER);
        this.add(myLeaderboard, BorderLayout.EAST);
        this.add(myStatusBar, BorderLayout.SOUTH);
        this.setOpaque(true);
        window.setContentPane(this);
        
        final ImageIcon icon;
        final Image image;
        icon = new ImageIcon("./images/flag.png");
        image = icon.getImage().getScaledInstance
                        (ICON_SIZE, ICON_SIZE, java.awt.Image.SCALE_SMOOTH);
        window.setIconImage(image);
        
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
        
        window.revalidate();        
    }

    /**
     * Builds a leaderboard panel which shows racers in sorted leaderboard order.
     * 
     * @return a JPanel with a race leaderboard
     */
    private JPanel buildLeaderboard() {
        final JPanel leaderboard = new JPanel(new GridLayout(11, 1));
        leaderboard.setBorder(new EmptyBorder(LEAD_BORDER));
        final JButton butt = new JButton(LEAD_TEXT);
        butt.setEnabled(false);
        
        leaderboard.add(butt);
        return leaderboard;
    }
    
    /**
     * Builds a race status bar panel with time and participant sections.
     * 
     * @return a JPanel representing a status bar
     */
    private JPanel buildStatusBar() {
        final JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(Color.GRAY);
        myTimeLabel.setText(TIME_TEXT + "00:00:000");
        myParticipantLabel.setText("Participant: ");
        statusBar.add(myTimeLabel, BorderLayout.EAST);
        statusBar.add(myParticipantLabel, BorderLayout.WEST);
        return statusBar;        
    }

    /**
     * Builds a map which links racers to randomly selected colors. 
     * 
     * @param theRacers list of racers to be linked to colors
     */
    private void buildMapRacerColor(final List<Racer> theRacers) {
        final Random rand = new Random();
        final int maxColorValue = 256;
        myMap.clear();
        for (final Racer r : theRacers) {
            myMap.put(r, new Color(rand.nextInt(maxColorValue), 
                                   rand.nextInt(maxColorValue), rand.nextInt(maxColorValue)));
        } // considered only light colors for readability but the enjoy dark colors for variety
    }

    /**
     * Helper method takes a leaderboard message and updates the leaderboard
     * by calling updateLeaderboard after formatting the leaderboard message
     * information into workable data.
     * 
     * @param theMessage a leaderboard message to update the view leaderboard with
     */
    private void incomingLeadMessage(final Message theMessage) {
        final LeaderboardMessage message = (LeaderboardMessage) theMessage;
        final String[] orderedIDs = message.getOrder();
        final ArrayList<Integer> orderedIntIDs = new ArrayList<Integer>();
        for (final String s : orderedIDs) {
            orderedIntIDs.add(Integer.parseInt(s));
        }
        // Now we have an ordered ArrayList of racer IDs
        // want to call updateLeaderBoard on the ordered list of racers
        final ArrayList<Racer> orderedRacers = new ArrayList<>();
        final Set<Racer> setOfRacers = myMap.keySet();
        for (final int id : orderedIntIDs) {
            for (final Racer r : setOfRacers) {
                if (r.getID() == id) {
                    orderedRacers.add(r);
                }   // if the ID in the message matches one of the racers, 
            }       // add this racer to our ordered list of racers
        }           // this method because returned set from map is unsorted
        updateLeaderboard(orderedRacers);
    }
    
    /**
     * Updates the leaderboard panel given a sorted list of racers.
     * 
     * @param theRaceLeaders sorted list of racers, first to last
     */
    private void updateLeaderboard(final List<Racer> theRaceLeaders) {
        myLeaderboard.removeAll();
        myLeaderboard.revalidate();
        final JButton butt = new JButton(LEAD_TEXT);
        butt.setEnabled(false);
        myLeaderboard.add(butt);
        for (final Racer r : theRaceLeaders) {
            final JButton nameButton = new JButton(r.getName());
            nameButton.setBackground(myMap.get(r));
            myLeaderboard.add(nameButton);
        }
        // This next part is about to make empty buttons. 
        // Needed because frame won't remove old button pixels, even though
        // the window/frame/panel recognize that there are no buttons there 
        // and the map building buttons contains the correct number of elements. 
        if (theRaceLeaders.size() < MAX_RACERS) {
            for (int i = 0; i < MAX_RACERS - theRaceLeaders.size(); i++) {
                final JButton deadButton = new JButton();
                deadButton.setEnabled(false);
                myLeaderboard.add(deadButton);
            }       
        } // revalidate / removeAll aren't fixing it, so I blame swing. Have buttons.
    }
    
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (theEvent.getPropertyName().equals(PROPERTY_TIME)) {
            myTimeLabel.setText(TIME_TEXT + TimeLabel.formatTime
                                ((int) theEvent.getNewValue()));
        }
        
        if (theEvent.getPropertyName().equals(PROPERTY_RACER_INFO)) {
            final ArrayList<Racer> racers = (ArrayList<Racer>) theEvent.getNewValue();
            buildMapRacerColor(racers);
            updateLeaderboard(racers);   
        }
        
        if (theEvent.getPropertyName().equals(PROPERTY_NEW_MESSAGES)) {
            final Message m = (Message) theEvent.getNewValue();
            if (m.getType().equals(Message.LEAD_START)) {
                incomingLeadMessage(m);
            }
        }

        this.revalidate();
    }
    
}
