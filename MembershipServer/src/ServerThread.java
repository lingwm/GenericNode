import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Lingwei Meng on 2017/12/13.
 */
public class ServerThread implements Runnable{
    private Socket client = null;

    public ServerThread(Socket client) {
        this.client = client;
        new Thread(this).start();
    }
    public void run(){
        try {
            PrintStream outToClient = new PrintStream(client.getOutputStream());
            BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
            boolean loop=true;
            while (loop){
                String str = buf.readLine();

                if (str == null || "".equals(str)) {
                    outToClient.println("No input received");
                    loop = false;
                } else {
                    String[] command = str.split(",");
                    outToClient.println(operate(command));
                }
            }
            outToClient.close();
            buf.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * process input command
     * @param command
     * @return
     */
    private synchronized String operate(String[] command){
        String result="";
        if (command[0]!=null){
            switch (command[0]){
                case "put":
                    TCPServer.map.put(command[1],command[2]);
                    break;
                case "store":
                    Iterator iterator=TCPServer.map.entrySet().iterator();
                    while (iterator.hasNext()){
                        Map.Entry entry=(Map.Entry)iterator.next();
                        String key=(String)entry.getKey();
                        String val=(String)entry.getValue();
                        result+=key+","+val+";";
                    }
                    break;
                case "exit":
                    TCPServer.running=false;
                    result= "Server is shut down.";
                    break;
            }
        }
        return result;
    }
}
