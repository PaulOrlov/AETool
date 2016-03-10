package eyetrackerconnecter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paulorlov
 */
public class IViewX extends EyeTrackerDevice {

    private int recivePort, sendPort;
    private InetAddress sendIP;
    private ArrayList<FixationListener> fllist = new ArrayList<>();
    private ArrayList<UpdateDevice> udlist = new ArrayList<>();
    private static final String ET_STR = "ET_STR\n";
    private static final String ET_FIX = "ET_FIX\n";
    private static final String ET_INC = "ET_INC\n";
    private static final String ET_REC = "ET_REC\n";
    private static final String ET_FRM = "ET_FRM \"%ET %SX %SY\" \n";
    private boolean isTrackerRunning = true;
    private Fixation currentFixation;

    public void setSendIP(String sendIP) {
        try {
            this.sendIP = InetAddress.getByName(sendIP);
        } catch (UnknownHostException ex) {
            Logger.getLogger(IViewX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setRecivePort(int recivePort) {
        this.recivePort = recivePort;
    }

    public void setSendPort(int sendPort) {
        this.sendPort = sendPort;
    }

    @Override
    public String getSendIP() {
        return sendIP.getHostName();
    }

    @Override
    public int getRecivePort() {
        return recivePort;
    }

    @Override
    public int getSendPort() {
        return sendPort;
    }

    public void startRecord() {
        udpSender(ET_STR);
    }

    public void startParsFixations() {
        udpSender(ET_FIX);
    }

    public void addFixationListener(FixationListener fl) {
        fllist.add(fl);
    }

    public void addUpdateDeviceListener(UpdateDevice ud) {
        udlist.add(ud);
    }

    public void sendNextTrial() {
        udpSender(ET_INC);
    }

    public void startTracker() {
        udpSender(ET_FRM);
        udpSender(ET_REC);
        currentFixation = new Fixation();
        
        Thread thr = new Thread() {
            @Override
            public void run() {
                try {
                    DatagramSocket clientSocket = new DatagramSocket(recivePort);
                    while (isTrackerRunning) {
                        byte[] receiveData = new byte[1024];
                        DatagramPacket receive = new DatagramPacket(receiveData, receiveData.length);
                        clientSocket.receive(receive);
                        String mRes = new String(receive.getData());
                        //System.out.println(mRes);
                        String[] parts = mRes.split(" ");
                        if (parts[0].equals("ET_SPL")) {
                            int xl = Integer.parseInt(parts[2].trim());
                            int xr = Integer.parseInt(parts[3].trim());
                            int yl = Integer.parseInt(parts[4].trim());
                            int yr = Integer.parseInt(parts[5].trim());
                            
                            upDateGazePosition(xl, yl, xr, yr);
                        } else if (parts[0].equals("ET_FIX")) {
                            currentFixation.endFixation();
                            currentFixation = new Fixation();
                        } else if (parts[0].equals("ET_EFX")){
                            currentFixation.endFixation();
                        } 
                    }
                    clientSocket.close();
                } catch (SocketException ex) {

                } catch (IOException ex) {

                }
            }
        };

        thr.start();
        
        //fixation duration thread
        
        Thread thrFX = new Thread() {
            @Override
            public void run() {
                while (isTrackerRunning) {
                    currentFixation.updateDuration();
                    for(FixationListener fl : fllist){
                        fl.fixationEvent(currentFixation);
                    }
                }
            }
        };
        thrFX.start(); 

    }

    private void udpSender(String send) {
        try {
            DatagramSocket serverSocket = new DatagramSocket();
            byte[] m = send.getBytes();
            DatagramPacket request = new DatagramPacket(m, m.length, sendIP, sendPort);
            serverSocket.send(request);
            serverSocket.close();
        } catch (SocketException ex) {
            Logger.getLogger(IViewX.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(IViewX.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveDataOnTracker() {

    }

    public void stopTracker() {
        isTrackerRunning = false;
    }
    
    private void upDateGazePosition(int xl, int yl, int xr, int yr) {
        double gazeX = ((double) xl + (double) xr) / 2.0;
        double gazeY = ((double) yl + (double) yr) / 2.0;
        UpdateDeviceEvent event = new UpdateDeviceEvent((int)gazeX, (int)gazeY);
        udlist.stream().unordered().forEach(listener -> listener.updateDeviceEvent(event));
    }

}
