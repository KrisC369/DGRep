package be.kriscon.dgrep;

import be.kriscon.dgrep.core.IReplay;
import be.kriscon.dgrep.core.Replayer;
import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kristof Coninx <kristof.coninx at student.kuleuven.be>
 */
public class DatagramReplayer implements ReplayerAPI {

    /*the path to the config file*/
    private String configLocation;
    /* first port to listen on*/
    private int listenPortStart;
    /* last port to listen on*/
    private int listenPortEnd;
    /*address to listen on*/
    private InetAddress listenAddress;
    /* list of hosts to rebroadcast to.*/
    private List<InetAddress> hosts;
    /* the collection of all threads running*/
    private Set<IReplay> runningThreads;
    /* The logger for this class. */
    private static final Logger LOGGER = Logger.getLogger("DatagramReplayer.class");

    /**
     * Default constructor sets variables and reads config
     */
    public DatagramReplayer(String url) {
        runningThreads = new HashSet<IReplay>();
        configLocation = url;
        hosts = new ArrayList<InetAddress>();
    }

    /**
     * Reads Config file
     *
     * @param cfg the config file to read from
     * @throws FileNotFoundException if file not found.
     */
    @SuppressWarnings("empty-statement")
    private void readcfg(String url) throws FileNotFoundException {
        Scanner in;
        in = new Scanner(DatagramReplayer.class.getResourceAsStream(url));

        String line = in.nextLine();
        while (in.hasNext() && line.contains("#")) {
            line = in.nextLine();
        }

        listenPortStart = Integer.parseInt(line);
        line = in.nextLine();
        listenPortEnd = Integer.parseInt(line);
        line = in.nextLine();
        try {
            listenAddress = InetAddress.getByName(line);
        } catch (UnknownHostException e) {
            LOGGER.log(Level.INFO, "error in listenAddress");
        }
        line = in.nextLine();
        assert (line.contains("#"));
        while (in.hasNext()) {
            line = in.nextLine();
            try {
                hosts.add(InetAddress.getByName(line));
            } catch (UnknownHostException e) {
                LOGGER.log(Level.INFO, "host not found {0}", line);
            }
        }
        LOGGER.log(Level.INFO, "Config read...");
        in.close();
    }

    /**
     * Starts listening on the set range of sockets and rebroadcasts any packet
     * received to list of hosts.
     */
    private void startListen() {
        Replayer current;
        for (int p = listenPortStart; p <= listenPortEnd; p++) {
            current = new Replayer(listenAddress, p, p, hosts);
            current.start();
            runningThreads.add(current);
        }
    }

    /**
     * start main.
     *
     * @param args
     */
    public static void main(String[] args) {
        DatagramReplayer replay = new DatagramReplayer("/configuration");
        replay.readConfig();
        replay.startListen();
    }

    @Override
    public void addDestinationHosts(Collection<InetAddress> hosts) {
        for (InetAddress h : hosts) {
            this.hosts.add(h);
        }
    }

    @Override
    public boolean startReplay() {
        try {
            startListen();
        } catch (Error err) {
            return false;
        } catch (Exception exc) {
            return false;
        }
        return true;
    }

    @Override
    public boolean stopReplay() {
        for (IReplay thread : runningThreads) {
            thread.stop();
        }
        resetRunningList();
        return true;
    }

    @Override
    public boolean readConfig() {
        try {
            readcfg(configLocation);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean writeConfig() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void resetRunningList() {
        this.runningThreads = new HashSet<IReplay>();
    }
}
