import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by Lingwei Meng on 2017/11/6.
 */
public class TCPServer{
    public static HashMap<String,String> map=new HashMap<>();
    public static List<String[]> membership = new ArrayList<String[]>();
    public static List<String> pairLock = new ArrayList<String>();
    public static boolean running=true;
    public static int port;
    public static String ip;
    public static String membershipServer;

    public TCPServer(String port,String membershipServer){
        this.port=Integer.parseInt(port);
        this.membershipServer=membershipServer;
    }

    public void run(){
        try {
            ServerSocket serverSocket=new ServerSocket(port);
            System.out.println("setup server");
            //add this server node to the nodes membership server
            updateNodes();
            //start the nodes membership discovery
            new TCPDiscovery();
            System.out.println("update nodes membership");
            //refresh and update the nodes membership

            while (running){
                Socket clientSocket=serverSocket.accept();
                new ServerThread(clientSocket);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * add the address of this server to nodelist server
     */
    public void updateNodes(){
        try {
            InetAddress add=InetAddress.getLocalHost();
            ip=add.getHostAddress();
        }catch (UnknownHostException e){
            e.printStackTrace();
        }
        String port="";
        port+=TCPServer.port;
        BufferedReader br = null;
        try {
            Socket client=new Socket(TCPServer.membershipServer,4410);
            PrintStream outToServer = new PrintStream(client.getOutputStream());
            outToServer.println("put,"+ip+","+port);
            client.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}


