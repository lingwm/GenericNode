import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by Lingwei Meng on 2017/12/13.
 */
public class TCPServer{
    public static HashMap<String,String> map=new HashMap<>();
    public static boolean running=true;
    public  void run(String port){
        try {
            ServerSocket serverSocket=new ServerSocket(Integer.parseInt(port));
            System.out.println("setup server");
            InetAddress add=InetAddress.getLocalHost();
            System.out.println(add.getHostAddress());
            while (running){
                Socket clientSocket=serverSocket.accept();
                System.out.println("have a new request");
                new ServerThread(clientSocket);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}


