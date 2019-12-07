package view;

import static model.PropertyChangeEnabledRaceControls.PROPERTY_HEADER_INFO;
import static model.PropertyChangeEnabledRaceControls.PROPERTY_NEW_MESSAGES;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import model.Message;
import model.RaceHeader;
import model.Racer;
import model.TelemetryMessage;
import track.VisibleRaceTrack;

/**
 * This program builds a race track panel to display a race.
 * 
 * @author Conner Canning
 * @version 16 March 2019
 */
public final class RaceTrack extends JPanel implements PropertyChangeListener {
    
    /** A generated serial version UID for object Serialization. */
    private static final long serialVersionUID = 8385732728740430466L;
    
    /** The size of the Race Track Panel. */
    private static final Dimension PANEL_SIZE = new Dimension(500, 400);
    
    /** The x and y location of the Track. */
    private static final int OFF_SET = 40;

    /** The stroke width in pixels. */
    private static final int STROKE_WIDTH = 25;

    /** The size of participants moving around the track. */
    private static final int OVAL_SIZE = 20;

    /** Links racers with colors for consistent color on scoreboard and race track panel. */
    private final Map<Racer, Color> myRacerColorMap;

    /** Links racers to their circles which go around the race track. */
    private final Map<Racer, Ellipse2D> myRacerCircleMap;
    
    /** The visible track. */
    private VisibleRaceTrack myTrack;   
    
    /**
     * Constructs a new RaceTrack to display a model race.
     * 
     * @param theRacerColorMap a map which links racers to colors for 
     * consistency across the JPanels in the race view window
     */
    public RaceTrack(final Map<Racer, Color> theRacerColorMap) {
        super();
        this.setBorder(BorderFactory.createTitledBorder("Race Track"));
        this.setPreferredSize(PANEL_SIZE);
        myRacerColorMap = theRacerColorMap;
        myRacerCircleMap = new HashMap<>();
    }
    
    
    /**
     * Sets up the race track given race header information. 
     * 
     * @param theHeader the race header with track information
     */
    private void setupComponents(final RaceHeader theHeader) {
        final int width = (int) PANEL_SIZE.getWidth() - (OFF_SET * 2);
        final int height = width / 5 * theHeader.getWidth();
        
        final int x = OFF_SET;
        final int y = (int) PANEL_SIZE.getHeight()  / 2 - height / 2;

        myTrack = new VisibleRaceTrack(x, y, width, height, (int) theHeader.getDistance());
        revalidate();
    }
    
    /**
     * Sets up the racer circles in their starting positions.
     */
    private void racerStartingPositions() {
        myRacerCircleMap.clear();   // clear previous race loads
        for (final Racer r : myRacerColorMap.keySet()) {
            myRacerCircleMap.put(r, makeCircle(r.getStartDistance()));
        }
    }
    
    /**
     * Helper method which initializes a new Ellipse2D circle at specified 
     * race distance.
     * 
     * @param theDistance the distance the circle should be located
     * @return a new circle representing a racer at given distance
     */
    private Ellipse2D makeCircle(final double theDistance) {
        final Point2D newPoint = myTrack.getPointAtDistance(theDistance);
        return new Ellipse2D.Double(newPoint.getX() - OVAL_SIZE / 2,
                                    newPoint.getY() - OVAL_SIZE / 2,
                                    OVAL_SIZE, OVAL_SIZE);
    }
    
    /**
     * Paints the VisibleTrack with all racer circles.
     * 
     * @param theGraphics the graphics context to use for painting
     */
    @Override
    public void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        if (myTrack != null) {
            g2d.setPaint(Color.BLACK);
            g2d.setStroke(new BasicStroke(STROKE_WIDTH));
            g2d.draw(myTrack);
            
            g2d.setStroke(new BasicStroke(1));
            for (final Racer r : myRacerCircleMap.keySet()) {
                // Set the color to racer's individual color
                g2d.setPaint(myRacerColorMap.get(r));
                // Now paint the racer specific circle
                g2d.fill(myRacerCircleMap.get(r));
            }
        }
    }
    
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        
        if (PROPERTY_HEADER_INFO.equals(theEvent.getPropertyName())) {
            final RaceHeader header = (RaceHeader) theEvent.getNewValue();
            setupComponents(header);
            racerStartingPositions();
        }
        
        if (PROPERTY_NEW_MESSAGES.equals(theEvent.getPropertyName())) {
            final Message m = (Message) theEvent.getNewValue();
            if (Message.TELEM_START.equals(m.getType())) {                
                // If the message is telemetry data, check the message ID
                // against all my racer IDs. If the message is for one of my
                // racers, update their circle to new location specified by message
                final TelemetryMessage telem = (TelemetryMessage) m;
                final int telemID = telem.getID();
                for (final Racer r : myRacerCircleMap.keySet()) {
                    if (telemID == r.getID()) {
                        myRacerCircleMap.put(r, makeCircle(telem.getDist()));
                    }
                }
            }
        }
        
        repaint();
    }
    

}
