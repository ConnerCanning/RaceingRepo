package controller;

import static model.PropertyChangeEnabledRaceControls.PROPERTY_HEADER_INFO;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_NEW_MESSAGES;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_SENDING_STRING;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_TIME;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import model.PropertyChangeEnabledRaceControls;
import model.RaceHeader;
import model.RaceModel;
import model.Racer;
import view.ViewMainGUI;

/**
 * This program runs the Controller GUI for the Race Day program.
 *
 * @author Conner Canning
 * @version 21 February 2019
 */
public class ControllerMainGUI extends JPanel implements PropertyChangeListener {
    
    /** The separator for formatted time. */
    public static final String SEPARATOR = ":";
    
    /** The number of milliseconds in a second. */
    public static final int MILLIS_PER_SEC = 1000;
    
    /** The number of seconds in a minute. */
    public static final int SEC_PER_MIN = 60;
    
    /** The number of minute in a hour. */
    public static final int MIN_PER_HOUR = 60;
    
    /** The number of seconds in ten seconds. */
    public static final int TEN_SECONDS = 10;
    
    /** The number of rows which display the racer toggles. */
    public static final int NUM_ROWS = 3;
    
    /** The number of rows which display the racer toggles. */
    public static final int NUM_COLS = 5;
    
    /** A formatter to require at least 1 digit, leading 0. */
    public static final DecimalFormat ONE_DIGIT_FORMAT = new DecimalFormat("0");
    
    /** A formatter to require at least 2 digits, leading 0s. */
    public static final DecimalFormat TWO_DIGIT_FORMAT = new DecimalFormat("00");
    
    /** A formatter to require at least 3 digits, leading 0s. */
    public static final DecimalFormat THREE_DIGIT_FORMAT = new DecimalFormat("000");
    
    /** The speed multiplier for the timer on one_times mode. */
    private static final int NORMAL_SPEED = 1;
    
    /** The speed multiplier for timer on four_times mode. */
    private static final int FAST_SPEED = 4;
    
    /** Start text for the start/stop button. */
    private static final String BUTTON_TEXT_START = "Start";
    
    /** Start icon text for the start/stop button. */
    private static final String BUTTON_ICON_START = "./images/ic_play.png"; 
    
    /** Stop text for the start/stop button. */
    private static final String BUTTON_TEXT_STOP = "Stop"; 
    
    /** Stop icon text for the start/stop button. */
    private static final String BUTTON_ICON_STOP = "./images/ic_pause.png"; 
    
    /** Restart text for the restart button. */
    private static final String BUTTON_TEXT_RESTART = "Restart"; 
    
    /** Restart icon text for the restart button. */
    private static final String BUTTON_ICON_RESTART = "./images/ic_restart.png";
    
    /** One times text for the one times/ four times button. */
    private static final String BUTTON_TEXT_ONE_TIMES = "Times One"; 
    
    /** One times icon text for the one times/ four times button. */
    private static final String BUTTON_ICON_ONE_TIMES = "./images/ic_one_times.png";
    
    /** Four times text for the one times/ four times button. */
    private static final String BUTTON_TEXT_FOUR_TIMES = "Times Four"; 
    
    /** Four times icon text for the one times/ four times button. */
    private static final String BUTTON_ICON_FOUR_TIMES = "./images/ic_four_times.png";
    
    /** Single race text for the no loop/ loop button. */
    private static final String BUTTON_TEXT_SINGLE = "Single Race"; 
    
    /** Single race icon text for the no loop/ loop button. */
    private static final String BUTTON_ICON_SINGLE = "./images/ic_repeat.png";
    
    /** Loop text for the no loop/ loop button. */
    private static final String BUTTON_TEXT_LOOP = "Loop Race"; 
    
    /** Loop icon text for the no loop/ loop button. */
    private static final String BUTTON_ICON_LOOP = "./images/ic_repeat_color.png";
    
    /** Clear text for the clear button. */
    private static final String BUTTON_TEXT_CLEAR = "Clear"; 
    
    /** Clear icon text for the clear button. */
    private static final String BUTTON_ICON_CLEAR = "./images/ic_clear.png";
    
    /** Padding for the border around tabbed pane panel component. */
    private static final Insets TEXT_BORDER = new Insets(10, 10, 10, 10);
    
    /** Padding for the border around slider panel component. */
    private static final Insets SLIDE_BORDER = new Insets(15, 20, 10, 0);
    
    /** Dimension of square JToolBar icons. */
    private static final int ICON_SIZE = 24;
    
    /** Row dimension of JTextArea which displays race information. */
    private static final int ROWS = 10;
    
    /** Row dimension of JTextArea which displays race information. */
    private static final int COLUMNS = 50;
    
    /** The milliseconds at which the timer ticks. */
    private static final int TIMER_FREQUENCY = 29;
    
    /** A generated serial version UID for object Serialization. */
    private static final long serialVersionUID = -5833167480206379948L;
    
    /** The race model. */
    private final PropertyChangeEnabledRaceControls myModel;
    
    /** A Swing Timer object for advancing race time. */
    private final Timer mySwingTimer;
    
    /** A list of button race control actions such as play, pause, and loop. */
    private final List<Action> myActions = new ArrayList<Action>();

    /** A label to display race time. */
    private final TimeLabel myTimeLabel;
    
    /** A slider which both displays and controls race progress. */
    private final JSlider mySlider;
    
    /** A toolbar which holds buttons that control the race. */
    private final JToolBar myToolbar = new JToolBar();
    
    /** JMenu which holds buttons that control the race. */
    private final JMenu myControlsMenu;
    
    /** A text area which displays race information from the race model. */
    private final JTextArea myText;
    
    /** A scrolling pane to hold the text area which displays race information. */
    private final JScrollPane myScroll;

    /** Multiplier for rate at which timer ticks. */
    private int myMultiplier = 1;
    
    /** Holds whether the race will loop or not. Race will loop if this is true. */
    private boolean myLoop;
    
    /** Holds race participants so they may be toggled on or off in the model. */
    private JPanel myParticipantPanel;

    /** Tabbed pane which holds race information and race participant panels. */
    private JTabbedPane myTappedPane;
    

    /**
     * Creates a ControllerMainGUI object which controls a race given a race model.
     * 
     * @param theModel the race model that gives race information to the controller
     */
    public ControllerMainGUI(final RaceModel theModel) {       
        super(new BorderLayout());
        myModel = theModel;
        mySwingTimer = new Timer(TIMER_FREQUENCY, this::handleTimer);
//        myMultiplier = 1;        
        mySlider = buildSlider(1);
        myTimeLabel = new TimeLabel();
//        myToolbar = new JToolBar();
//        myActions = new ArrayList<>();
        myControlsMenu = new JMenu("Controls");
        myText = new JTextArea(ROWS, COLUMNS);
        myScroll = new JScrollPane(myText);
        myLoop = false;
        finishSetUp();
    }
    
    
    /**
     * Helper method which finishes the controller gui setup. 
     */
    private void finishSetUp() {
        final JPanel inner = new JPanel(new BorderLayout());
        inner.add(buildTabbedPane(), BorderLayout.SOUTH);
        inner.add(mySlider, BorderLayout.CENTER);
        inner.add(myTimeLabel, BorderLayout.EAST);
        add(inner, BorderLayout.CENTER);
        add(myToolbar, BorderLayout.SOUTH);

        // Building race control buttons
        myActions.add(new RestartAction(BUTTON_TEXT_RESTART));
        myActions.add(new StartAction(BUTTON_TEXT_START));
        myActions.add(new SpeedAction(BUTTON_TEXT_ONE_TIMES));
        myActions.add(new LoopAction(BUTTON_TEXT_SINGLE));
        myActions.add(new ClearAction(BUTTON_TEXT_CLEAR));
      
        for (final Action a : myActions) {
            // Add JMenuItem to the Controls JMenu 
            final JMenuItem item = new JMenuItem(a);
            item.setEnabled(false);
            myControlsMenu.add(item);
            
            // Add control button to the JToolBar
            final JButton button = new JButton(a);
            button.setHideActionText(true); 
            button.setEnabled(false);
            myToolbar.add(button);
        }
                
        myModel.addPropertyChangeListener(myTimeLabel);
        myModel.addPropertyChangeListener(this);
    }
    

    /**
     * Creates and shows the gui.
     */
    public static void createAndShowGUI() {
        final JFrame window = new JFrame("Race Day!");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        final RaceModel model = new RaceModel();
        
        final ControllerMainGUI pane = new ControllerMainGUI(model);
        pane.setOpaque(true);
        
        window.setContentPane(pane);
        window.setJMenuBar(pane.createJMenuBar());
        
        final ImageIcon icon;
        final Image image;
        icon = new ImageIcon("./images/flag.png");
        image = icon.getImage().getScaledInstance
                        (ICON_SIZE, ICON_SIZE, java.awt.Image.SCALE_SMOOTH);
        window.setIconImage(image);
        window.setResizable(false);
        window.pack();

        window.setVisible(true); 
        
        // Run the view application
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ViewMainGUI(model);
            }
        });
    }
    
    
    /**
     * This formats a positive integer into minutes, seconds, and milliseconds. 
     * 00:00:000
     * @author Charles Bryan
     * @param theTime the time to be formatted
     * @return the formated string. 
     */
    public static String formatTime(final long theTime) {
        long time = theTime;
        final long milliseconds = time % MILLIS_PER_SEC;
        time /= MILLIS_PER_SEC;
        final long seconds = time % SEC_PER_MIN;
        time /= SEC_PER_MIN;
        final long min = time % MIN_PER_HOUR;
        time /= MIN_PER_HOUR;
        return TWO_DIGIT_FORMAT.format(min) + SEPARATOR
                        + TWO_DIGIT_FORMAT.format(seconds) 
                        + SEPARATOR + THREE_DIGIT_FORMAT.format(milliseconds);
    }

    
    
    /**
     * Builds the slider that represents the race progress.
     * 
     * @param theMax the max value for slider
     * @return a JSlider that tracks race progress
     */
    private JSlider buildSlider(final int theMax) {
        final JSlider slider = new JSlider(0, theMax, 0);
        slider.setEnabled(false);
        slider.setBorder(new EmptyBorder(SLIDE_BORDER));
        slider.addChangeListener(e -> myModel.moveTo(slider.getValue()));
        return slider;
    }

    
    /**
     * Creates the tabbed pane in Controller GUI.
     * One tab has a text area for race info, the other 
     * tab is a panel which holds checkboxes that toggle racers.
     * 
     * @return a JTabbedPane which contains a race info and racer toggle
     */
    private JTabbedPane buildTabbedPane() {
        myTappedPane = new JTabbedPane();
        myTappedPane.add("Data Output Stream", myScroll);
        myText.setEditable(false);
        myScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        myParticipantPanel = new JPanel(new GridLayout(NUM_ROWS, NUM_COLS));
        myParticipantPanel.setPreferredSize(new Dimension(1, 1));

        myTappedPane.add("Race Participants", myParticipantPanel);
        
        myTappedPane.setEnabledAt(1, false);
        myTappedPane.setBorder(new EmptyBorder(TEXT_BORDER));
        return myTappedPane;
    }
 
    
    /**
     * Event handler for the timer. 
     * 
     * @author Charles Bryan
     * @param theEvent the fired event
     */
    private void handleTimer(final ActionEvent theEvent) { //NOPMD
        myModel.advance(TIMER_FREQUENCY * myMultiplier);
    }
    

    /**
     * Creates and returns a JMenuBar for a JFrame which houses the controller.
     * 
     * @return a JMenuBar with race controls and information 
     */
    private JMenuBar createJMenuBar() {
        return new MenuGUI(myModel).buildMenu(myControlsMenu);
    }
    

    /**
     * Changes controller features based on model information via PropertyChangeEvents.
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (theEvent.getPropertyName().equals(PROPERTY_HEADER_INFO)) {
            headerInfoPropertyChange(theEvent);
        }
        
        if (theEvent.getPropertyName().equals(PROPERTY_NEW_MESSAGES)) {
            myText.append(theEvent.getNewValue().toString());
            myText.append("\n");
        }
        
        if (theEvent.getPropertyName().equals(PROPERTY_SENDING_STRING)) {
            myText.append(theEvent.getNewValue().toString());
        }
        
        if (theEvent.getPropertyName().equals(PROPERTY_TIME)) {
            mySlider.setValue(Integer.parseInt(theEvent.getNewValue().toString()));
        }

        //"Too many static imports may lead to messy code." this is so much cleaner wow 
        if (theEvent.getPropertyName().equals(
                                  PropertyChangeEnabledRaceControls.PROPERTY_RACE_COMPLETE)) {
            if (myLoop) {
                myModel.moveTo(0);
            } else {
                ((JButton) myToolbar.getComponent(1)).doClick();
            }
        }
        
        if (theEvent.getPropertyName().equals(
                                  PropertyChangeEnabledRaceControls.PROPERTY_RACER_INFO)) {
            racerInfoPropertyChange(theEvent);
            
        }
    }
 
    /**
     * Helper method which handles header info property changes.
     * Created to reduce code complexity in propertyChange.
     * 
     * @param theEvent a Property Change Event holding header info
     */
    private void headerInfoPropertyChange(final PropertyChangeEvent theEvent) {
        if (theEvent.getPropertyName().equals(PROPERTY_HEADER_INFO)) {
            for (int i = 0; i < myActions.size(); i++) {
                myControlsMenu.getMenuComponent(i).setEnabled(true);
                myToolbar.getComponent(i).setEnabled(true);
            }
            myText.append("File load completed!\n");
            mySlider.setValue(0);
            mySlider.setMaximum(((RaceHeader) theEvent.getNewValue()).getTime());
            mySlider.setMajorTickSpacing(MILLIS_PER_SEC * SEC_PER_MIN);
            mySlider.setMinorTickSpacing(MILLIS_PER_SEC * TEN_SECONDS);
            mySlider.setPaintTicks(true);
            mySlider.setEnabled(true);
        }
    }
    
    
    /**
     * Helper method which handles racer info property changes.
     * Created to reduce code complexity in propertyChange.
     * 
     * @param theEvent a Property Change Event holding racer info
     */
    private void racerInfoPropertyChange(final PropertyChangeEvent theEvent) {
        
        if (theEvent.getPropertyName().equals(
                                  PropertyChangeEnabledRaceControls.PROPERTY_RACER_INFO)) {
            myTappedPane.setEnabledAt(1, true);
            myParticipantPanel.removeAll();
            final ArrayList<Racer> racers = (ArrayList<Racer>) theEvent.getNewValue();
            for (final Racer r : racers) { 
                final JCheckBox box = new JCheckBox(r.getName(), true);
                box.setName(Integer.toString(r.getID()));
    
                box.addActionListener(e -> {
                    if (box.isSelected()) {
                        myModel.toggleParticipant(Integer.parseInt(box.getName()), true);
                    } else {
                        myModel.toggleParticipant(Integer.parseInt(box.getName()), false);
                    }
                }); // multiple statements in one lambda expression sorry
                
                myParticipantPanel.add(box);
            }
        }        
    }
    
    
    
    

    
    
    /**
     * An action to encapsulate the Start Button.
     * @author Charles Bryan
     * @version Autumn 2018
     */
    class StartAction extends AbstractAction {
        
        /** A generated serial version UID for object Serialization. */
        private static final long serialVersionUID = 1234567890L;

        /**
         * Construct a StartAction. 
         * @param theText the title for the action
         */
        StartAction(final String theText) {
            super(theText);
            setIcon(new ImageIcon(BUTTON_ICON_START));
        }

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            if (mySwingTimer.isRunning()) {
                mySwingTimer.stop();
                putValue(Action.NAME, BUTTON_TEXT_START);
                setIcon(new ImageIcon(BUTTON_ICON_START));
                mySlider.setEnabled(true);
            } else {
                mySwingTimer.start();
                putValue(Action.NAME, BUTTON_TEXT_STOP);
                setIcon(new ImageIcon(BUTTON_ICON_STOP));
                mySlider.setEnabled(false);
            }
        }
        
        /**
         * Helper to set the Icon to both the Large and Small Icon values. 
         * @param theIcon the icon to set for this Action 
         */
        private void setIcon(final ImageIcon theIcon) {
            final ImageIcon icon = (ImageIcon) theIcon;
            final Image largeImage =
                icon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon largeIcon = new ImageIcon(largeImage);
            putValue(Action.LARGE_ICON_KEY, largeIcon);
            
            final Image smallImage =
                icon.getImage().getScaledInstance(12, -1, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon smallIcon = new ImageIcon(smallImage);
            putValue(Action.SMALL_ICON, smallIcon);
        }
    }

    /**
     * An action to encapsulate the Restart Button.
     * @author Conner Canning
     * @version 9 March 2019
     */
    class RestartAction extends AbstractAction {

        /** A generated serial version UID for object Serialization. */
        private static final long serialVersionUID = 1234577890L;

        /**
         * Constructs a RestartAction. 
         * @param theText the title for the action
         */
        RestartAction(final String theText) {
            super(theText);
            setIcon(new ImageIcon(BUTTON_ICON_RESTART));
        }

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            myModel.moveTo(0);
        }
        
        /**
         * Helper to set the Icon to both the Large and Small Icon values. 
         * @param theIcon the icon to set for this Action 
         */
        private void setIcon(final ImageIcon theIcon) {
            final ImageIcon icon = (ImageIcon) theIcon;
            final Image largeImage =
                icon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon largeIcon = new ImageIcon(largeImage);
            putValue(Action.LARGE_ICON_KEY, largeIcon);
            
            final Image smallImage =
                icon.getImage().getScaledInstance(12, -1, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon smallIcon = new ImageIcon(smallImage);
            putValue(Action.SMALL_ICON, smallIcon);
        }
    }

    /**
     * An action to encapsulate the Speed Button.
     * @author Conner Canning
     * @version 9 March 2019
     */
    class SpeedAction extends AbstractAction {

        /** A generated serial version UID for object Serialization. */
        private static final long serialVersionUID = 1236567890L;

        /**
         * Constructs a SpeedAction. 
         * @param theText the title for the action
         */
        SpeedAction(final String theText) {
            super(theText);
            setIcon(new ImageIcon(BUTTON_ICON_ONE_TIMES));
        }

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            if (myMultiplier == NORMAL_SPEED) {
                myMultiplier = FAST_SPEED;
                putValue(Action.NAME, BUTTON_TEXT_FOUR_TIMES);
                setIcon(new ImageIcon(BUTTON_ICON_FOUR_TIMES));
            } else {
                myMultiplier = NORMAL_SPEED;
                putValue(Action.NAME, BUTTON_TEXT_ONE_TIMES);
                setIcon(new ImageIcon(BUTTON_ICON_ONE_TIMES));
            }
        }
        
        /**
         * Helper to set the Icon to both the Large and Small Icon values. 
         * @param theIcon the icon to set for this Action 
         */
        private void setIcon(final ImageIcon theIcon) {
            final ImageIcon icon = (ImageIcon) theIcon;
            final Image largeImage =
                icon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon largeIcon = new ImageIcon(largeImage);
            putValue(Action.LARGE_ICON_KEY, largeIcon);
            
            final Image smallImage =
                icon.getImage().getScaledInstance(12, -1, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon smallIcon = new ImageIcon(smallImage);
            putValue(Action.SMALL_ICON, smallIcon);
        }
    }

    /**
     * An action to encapsulate the Loop Button.
     * @author Conner Canning
     * @version 9 March 2019
     */
    class LoopAction extends AbstractAction {

        /** A generated serial version UID for object Serialization. */
        private static final long serialVersionUID = 1324567890L;

        /**
         * Constructs a LoopAction. 
         * @param theText the title for the action
         */
        LoopAction(final String theText) {
            super(theText);
            setIcon(new ImageIcon(BUTTON_ICON_SINGLE));
        }

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            if (myLoop) {
                myLoop = false;
                putValue(Action.NAME, BUTTON_TEXT_SINGLE);
                setIcon(new ImageIcon(BUTTON_ICON_SINGLE));
            } else {
                myLoop = true;
                putValue(Action.NAME, BUTTON_TEXT_LOOP);
                setIcon(new ImageIcon(BUTTON_ICON_LOOP));
            }
        }
        
        /**
         * Helper to set the Icon to both the Large and Small Icon values. 
         * @param theIcon the icon to set for this Action 
         */
        private void setIcon(final ImageIcon theIcon) {
            final ImageIcon icon = (ImageIcon) theIcon;
            final Image largeImage =
                icon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon largeIcon = new ImageIcon(largeImage);
            putValue(Action.LARGE_ICON_KEY, largeIcon);
            
            final Image smallImage =
                icon.getImage().getScaledInstance(12, -1, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon smallIcon = new ImageIcon(smallImage);
            putValue(Action.SMALL_ICON, smallIcon);
        }
    }

    /**
     * An action to encapsulate the Clear Button.
     * @author Conner Canning
     * @version 9 March 2019
     */
    class ClearAction extends AbstractAction {

        /** A generated serial version UID for object Serialization. */
        private static final long serialVersionUID = 1234567890L;

        /**
         * Constructs a ClearAction. 
         * @param theText the title for the action
         */
        ClearAction(final String theText) {
            super(theText);
            setIcon(new ImageIcon(BUTTON_ICON_CLEAR));
        }

        @Override
        public void actionPerformed(final ActionEvent theEvent) {
            myText.setText("");
        }
        
        /**
         * Helper to set the Icon to both the Large and Small Icon values. 
         * @param theIcon the icon to set for this Action 
         */
        private void setIcon(final ImageIcon theIcon) {
            final ImageIcon icon = (ImageIcon) theIcon;
            final Image largeImage =
                icon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon largeIcon = new ImageIcon(largeImage);
            putValue(Action.LARGE_ICON_KEY, largeIcon);
            
            final Image smallImage =
                icon.getImage().getScaledInstance(12, -1, java.awt.Image.SCALE_SMOOTH);
            final ImageIcon smallIcon = new ImageIcon(smallImage);
            putValue(Action.SMALL_ICON, smallIcon);
        }
    }
    

    
}
