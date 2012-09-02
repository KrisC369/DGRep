package be.kriscon.dgrep.core;

/**
 *
 * @author Kristof Coninx <kristof.coninx at student.kuleuven.be>
 */
public interface IReplay extends Runnable {

    public void start();
    
    public void stop();
    
}
