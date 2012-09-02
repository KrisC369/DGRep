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

/**
 *
 * @author Kristof Coninx <kristof.coninx at student.kuleuven.be>
 */
public class DatagramReplayer implements ReplayerAPI {
    
    private String configLocation; //the path to the config file
    private int listenPortStart; // first port to listen on
    private int listenPortEnd; // last port to listen on
    private InetAddress listenAddress; //address to listen on
    private List<InetAddress> hosts;	//list of hosts to rebroadcast to.
    private Set<IReplay> runningThreads;

    /**
     * Default constructor 
     * sets variables and reads config
     */
    public DatagramReplayer(String url) {
        runningThreads = new HashSet<IReplay>();
        configLocation = url;
        hosts = new ArrayList<InetAddress>();
        try {
            readcfg(url);
            System.out.println("Config read...");
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't read config!");
        }
    }

    /**
     * Reads Config file 
     * @param cfg the config file to read from 
     * @throws FileNotFoundException if file not found.
     */
    @SuppressWarnings("empty-statement")
    private void readcfg(String url) throws FileNotFoundException {
        Scanner in;
        in = new Scanner(DatagramReplayer.class.getResourceAsStream(url));
            
        String line = in.nextLine();
        while(in.hasNext() && line.contains("#")){
            line = in.nextLine();
        }
            
        listenPortStart = Integer.parseInt(line);
        line = in.nextLine();
        listenPortEnd = Integer.parseInt(line);
        line = in.nextLine();
        try {
            listenAddress = InetAddress.getByName(line);
        } catch (UnknownHostException e) {
            System.out.println("error in listenAddress");
        }
        line = in.nextLine();
        assert (line.contains("#"));
        while (in.hasNext()) {
            line = in.nextLine();
            try {
                hosts.add(InetAddress.getByName(line));
            } catch (UnknownHostException e) {
                System.out.println("host not found " + line);
            }
        }
        in.close();
    }

    /**
     * Starts listening on the set range of sockets and rebroadcasts any packet received to list of hosts.
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
     * @param args
     */
    public static void main(String[] args) {
        DatagramReplayer replay = new DatagramReplayer("/configuration");
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
        return true;
    }
    
    @Override
    public boolean readConfig() {
        try {
            readConfig();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean writeConfig() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
