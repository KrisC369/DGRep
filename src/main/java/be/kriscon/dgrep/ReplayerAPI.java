package be.kriscon.dgrep;

import java.net.InetAddress;
import java.util.Collection;

/**
 *
 * @author Kristof Coninx <kristof.coninx at student.kuleuven.be>
 */
public interface ReplayerAPI {
    public void addDestinationHosts(Collection<InetAddress> hosts);
    public boolean startReplay();
    public boolean stopReplay();
    public boolean readConfig();
    public boolean writeConfig();
}
