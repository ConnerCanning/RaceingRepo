package controller;

import static model.PropertyChangeEnabledRaceControls.PROPERTY_HEADER_INFO;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.PropertyChangeEnabledRaceControls;

/**
 * This program builds the JMenuBar for a race controller. 
 * 
 * @author Conner Canning
 * @version 9 March 2019
 */
public class MenuGUI extends JPanel implements PropertyChangeListener {
    
    /**  
     * A generated serial version UID for object Serialization. 
     */
    private static final long serialVersionUID = 8385732728740433466L;
    
    /** Info about the programmer. */
    private static final String MY_ABOUT = "Conner Canning\n Winter 2019\n TCSS 305";

    /** Text displayed when there's a failure to load a file. */
    private static final Object FILE_ERROR_TEXT = "Error loading file!";
    
    /** A size for the icon which appears in the about section. */
    private static final int ICON_SIZE = 45;
    
    /** JFileChooser for choosing file. */
    private final JFileChooser myFileChooser = new JFileChooser(new File(
                            System.getProperty("user.dir")));
    
    /** The race model. */
    private final PropertyChangeEnabledRaceControls myRaceModel;
    
    /** Holds / displays the race header information. */
    private final JMenuItem myInfoItem;    
    
    /** Holds / displays the about programmer information. */
    private final JMenuItem myAboutItem;

    /** User chosen file to read race from. */
    private File myChosenFile;
    
    /** Holds header information about the loaded race. */
    private String myInfo = "";
    
    /**
     * Creates a MenuGUI object to be used when creating race controller.
     * 
     * @param theRaceModel race model to communicate race information
     */
    public MenuGUI(final PropertyChangeEnabledRaceControls theRaceModel) {
        super();
        myRaceModel = theRaceModel;
        myRaceModel.addPropertyChangeListener(this);
        myInfoItem = new JMenuItem("Race Info...");
        myAboutItem = new JMenuItem("About...");        
    }
    
    
    /**
     * Builds JMenuBar with race information and control options, given
     * a JMenu which holds the race control features.
     * 
     * @param theControls a JMenu holding race control options
     * @return a completed JMenuBar with file, control, and help options
     */
    public JMenuBar buildMenu(final JMenu theControls) {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(buildFile());
        menuBar.add(theControls);
        menuBar.add(buildHelp());
        return menuBar;
    }
    
    /**
     * Builds the JMenu which holds help options for race controller.
     * 
     * @return the JMenu with help options
     */
    private JMenu buildHelp() {
        final JMenu menu = new JMenu("Help");
        
        myInfoItem.addActionListener(e -> JOptionPane.showMessageDialog(this, myInfo));
        
        final ImageIcon icon;
        final Image image;
        icon = new ImageIcon("./images/flag.png");
        image = icon.getImage().getScaledInstance
                        (ICON_SIZE, ICON_SIZE, java.awt.Image.SCALE_SMOOTH);
        final ImageIcon iconB = new ImageIcon(image);
        myAboutItem.addActionListener(e -> JOptionPane.showMessageDialog
                          (this, MY_ABOUT, "About", JOptionPane.INFORMATION_MESSAGE, iconB));

        menu.add(myInfoItem);
        myInfoItem.setEnabled(false);
        
        menu.add(myAboutItem);
       
        return menu;
    }


    /**
     * Builds file menu options for race controller.
     * 
     * @return a JMenu with file options such as Load File and Exit
     */
    public JMenu buildFile() {
        final JMenu menu = new JMenu("File");
        
        final JMenuItem item = new JMenuItem("Load file...");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theEvent) {
                chooseFile();
            }
        });
        menu.add(item);
        
        menu.addSeparator();
        
        final JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));
        menu.add(exit);

        return menu;
    }
    
    /**
     * Helper method which selects a user chosen file and sends the file
     * to the race model to be loaded into race information for the controller. 
     */
    private void chooseFile() {
        myFileChooser.setDialogTitle("Choose a race file!");
        final int result = myFileChooser.showOpenDialog(getParent());
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                myChosenFile = myFileChooser.getSelectedFile();
                myRaceModel.loadRace(myChosenFile);
            } catch (final IOException e) {
//                JOptionPane.showMessageDialog(MenuGUI.this, FILE_ERROR_TEXT);
                JOptionPane.showMessageDialog(MenuGUI.this, FILE_ERROR_TEXT, 
                                              "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    /**
     * Turns on JMenu features when hears that there is race header information being sent.
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (theEvent.getPropertyName().equals(PROPERTY_HEADER_INFO)) {
            myInfoItem.setEnabled(true);
            myInfo = theEvent.getNewValue().toString();
                        
        }
    }

    
}
