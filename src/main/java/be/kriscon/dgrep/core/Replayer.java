package be.kriscon.dgrep.core;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kristof Coninx <kristof.coninx at student.kuleuven.be>
 * @version 2.0
 */
public class Replayer implements IReplay {

    private int listenPort; //port to listen on
    private int destinationPort; //port to send to 
    private InetAddress listenAddress; //address to listen on
    private List<InetAddress> hosts;	//list of hosts to rebroadcast to.
    private int datagramSize; // size of datagram-data buffer.
    private boolean canRun;

    /**
     * Creates a replayer-Instance in a new thread.
     * @param listenAddress Adress to listen on
     * @param listenPort Port to listen on
     * @param destinationPort Port to replay to
     * @param hosts list of hosts to broadcast to
     */
    public Replayer(InetAddress listenAddress, int listenPort, int destinationPort, List<InetAddress> hosts) {
        this.listenAddress = listenAddress;
        this.listenPort = listenPort;
        this.destinationPort = destinationPort;
        this.hosts = hosts;
        datagramSize = 0xFFFF;
        this.canRun = true;
    }

    /**
     * Starts this replayer-instance
     */
    @Override
    public void start() {
        new Thread(this).start();
    }

    /**
     * Stops this replayer-instance
     */
    @Override
    public void stop() {
        this.setCanRun(false);
    }

    /**
     * {@inheritDoc}
     * 
     */
    @Override
    public void run() {
        try {
            byte[] data = new byte[datagramSize];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            DatagramSocket listensocket = new DatagramSocket(listenPort, listenAddress);
            listensocket.setSoTimeout(1500);
            DatagramSocket sendSocket = new DatagramSocket();
            System.out.println("Started listening and replaying...");
            while (isCanRun()) {
                try {
                    listensocket.receive(packet);
                    //System.out.println("packet received");
                    for (InetAddress sendTo : hosts) {
                        packet.setPort(destinationPort);
                        packet.setAddress(sendTo);
                        sendSocket.send(packet);
                    }
                } catch (SocketTimeoutException e) {
                }
            }
            listensocket.close();
            sendSocket.close();
        } catch (SocketException e) {
            Logger.getLogger(Replayer.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            Logger.getLogger(Replayer.class.getName()).log(Level.SEVERE, null, e);
        }
        System.out.println("Stopped listening and replaying gracefully...");
    }

    /**
     * @return the canRun
     */
    public boolean isCanRun() {
        return canRun;
    }

    /**
     * @param canRun the canRun to set
     */
    public void setCanRun(boolean canRun) {
        this.canRun = canRun;
    }
}