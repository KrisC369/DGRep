package be.kriscon.dgrep.core;

/**
 * Interface for the replay instances.
 *
 * @author Kristof Coninx <kristof.coninx at student.kuleuven.be>
 */
public interface IReplay extends Runnable {
    /**
     * Starts a replay session.
     */
    public void start();
    /**
     * Stops a replay session.
     */
    public void stop();
    
}
