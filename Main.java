import java.io.IOException;

public class Main {
    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */

    public static boolean DEBUG = false;

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.

        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    new GUIframe(1400, 800);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}