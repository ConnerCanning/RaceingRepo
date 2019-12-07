package application;

import controller.ControllerMainGUI;
import java.awt.EventQueue;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * This program launches a race which creates a new controller and model.
 * 
 * @author Conner Canning
 * @version 2 February 2019
 */
public final class Main {

    /**
     * private non default constructor for this utility class.
     */
    private Main() {
    }

    /**
     * Runs the gui for the race.
     * 
     * @param theArgs not isued
     */
    public static void main(final String[] theArgs) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (final UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (final IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (final InstantiationException ex) {
            ex.printStackTrace();
        } catch (final ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        UIManager.put("swing.boldMetal", Boolean.FALSE);

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ControllerMainGUI.createAndShowGUI();

            }
        });
    }
}
