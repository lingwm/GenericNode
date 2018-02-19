import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Lingwei Meng on 2017/12/8.
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
            int count;
            switch (command[0]){
                case "put":
                    count=0;
                    while (count<10){    //try dput transaction at most 10 times
                        //send dput1 to each node
                        System.out.println("send dput1 to other nodes...");
                        for (int i=0;i<TCPServer.membership.size();i++){
                            String ip=TCPServer.membership.get(i)[0];
                            int port = Integer.valueOf(TCPServer.membership.get(i)[1]);
                            if (ip.equals(TCPServer.ip)&&port==TCPServer.port){continue;}  //do not send to itself
                            String dput1_str="dput1,"+command[1]+","+command[2];
                            Socket client;
                            try {
                                client = new Socket(ip, port);
                                client.setSoTimeout(1000);
                                PrintStream outToServer = new PrintStream(client.getOutputStream());
                                BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
                                outToServer.println(dput1_str);
                                String feedback = buf.readLine();
                                if (!feedback.equals("OK")){
                                    System.out.println("dput1 abort");
                                    sendAbort("dputabort");
                                    count++;
                                    continue;
                                }
                                System.out.println("get a dput1 ACK");
                                client.close();
                            }catch (IOException e){
                                e.printStackTrace();
                            }

                        }
                        // if all other nodes say "OK" to dput1, then start to send dput2
                        System.out.println("send dput2 to other nodes...");
                        for (int i=0;i<TCPServer.membership.size();i++){
                            String ip=TCPServer.membership.get(i)[0];
                            int port = Integer.valueOf(TCPServer.membership.get(i)[1]);
                            if (ip.equals(TCPServer.ip)&&port==TCPServer.port){continue;}  //do not send to itself
                            String dput2_str="dput2,"+command[1]+","+command[2];
                            Socket client;
                            try {
                                client = new Socket(ip, port);
                                client.setSoTimeout(1000);
                                PrintStream outToServer = new PrintStream(client.getOutputStream());
                                BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
                                outToServer.println(dput2_str);
                                String feedback = buf.readLine();
                                if (!feedback.equals("DONE")){
                                    System.out.println("dput2 abort");
                                    sendAbort("dputabort");
                                    count++;
                                    continue;
                                }
                                System.out.println("get a dput2 ACK");
                                client.close();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        //if everything is OK
                        TCPServer.map.put(command[1],command[2]);
                        result="put "+command[1]+" "+command[2];
                        count=10;
                    }
                    break;
                case "del":
                    count=0;
                    while (count<10){    //try ddel transaction at most 10 times
                        //send ddel1 to each node
                        System.out.println("send ddel1 to other nodes...");
                        for (int i=0;i<TCPServer.membership.size();i++){
                            String ip=TCPServer.membership.get(i)[0];
                            int port = Integer.valueOf(TCPServer.membership.get(i)[1]);
                            if (ip.equals(TCPServer.ip)&&port==TCPServer.port){continue;}  //do not send to itself
                            String ddel1_str="ddel1,"+command[1];
                            Socket client;
                            try {
                                client = new Socket(ip, port);
                                client.setSoTimeout(1000);
                                PrintStream outToServer = new PrintStream(client.getOutputStream());
                                BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
                                outToServer.println(ddel1_str);
                                String feedback = buf.readLine();
                                if (!feedback.equals("OK")){
                                    System.out.println("ddel1 abort");
                                    sendAbort("ddelabort");
                                    count++;
                                    continue;
                                }
                                System.out.println("get a ddel1 ACK");
                                client.close();
                            }catch (IOException e){
                                e.printStackTrace();
                            }

                        }
                        // if all other nodes say "OK" to ddel1, then start to send ddel2
                        System.out.println("send ddel2 to other nodes...");
                        for (int i=0;i<TCPServer.membership.size();i++){
                            String ip=TCPServer.membership.get(i)[0];
                            int port = Integer.valueOf(TCPServer.membership.get(i)[1]);
                            if (ip.equals(TCPServer.ip)&&port==TCPServer.port){continue;}  //do not send to itself
                            String ddel2_str="ddel2,"+command[1];
                            Socket client;
                            try {
                                client = new Socket(ip, port);
                                client.setSoTimeout(1000);
                                PrintStream outToServer = new PrintStream(client.getOutputStream());
                                BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
                                outToServer.println(ddel2_str);
                                String feedback = buf.readLine();
                                if (!feedback.equals("DONE")){
                                    System.out.println("ddel2 abort");
                                    sendAbort("ddelabort");
                                    count++;
                                    continue;
                                }
                                System.out.println("get a ddel2 ACK");
                                client.close();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                        //if everything is OK
                        TCPServer.map.remove(command[1]);
                        result="delete "+command[1];
                        count=10;
                    }
                    break;
                case "dput1":case "ddel1":
                    if (!TCPServer.pairLock.contains(command[1])){
                        result="OK";
                        TCPServer.pairLock.add(command[1]);
                    }else {
                        result="Key "+command[1]+" is locked";
                    }
                    break;
                case "dput2":
                    TCPServer.map.put(command[1],command[2]);
                    result="DONE";
                    TCPServer.pairLock.remove(command[1]);
                    break;
                case "ddel2":
                    TCPServer.map.remove(command[1]);
                    result="DONE";
                    TCPServer.pairLock.remove(command[1]);
                    break;
                case "dputabort":
                    if (TCPServer.pairLock.contains(command[1])){
                        TCPServer.pairLock.remove(command[1]);
                    }
                    if (TCPServer.map.containsKey(command[1])){
                        TCPServer.map.remove(command[1]);
                    }
                    break;
                case "ddelabort":
                    if (TCPServer.map.containsKey(command[1])){
                        TCPServer.map.remove(command[1]);
                    }
                    break;
                case "get":
                    result="Get "+command[1]+" = "+TCPServer.map.get(command[1]);
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
                    result= "Server is shutting down...";
                    break;
            }
        }
        return result;
    }

    /**
     * send abort message to other nodes
     * @param abort
     */
    private void sendAbort(String abort){
        for (int i=0;i<TCPServer.membership.size();i++){
            String ip=TCPServer.membership.get(i)[0];
            int port = Integer.valueOf(TCPServer.membership.get(i)[1]);
            if (ip.equals(TCPServer.ip)&&(port==TCPServer.port)){continue;}  //do not send to itself
            Socket client;
            try {
                client = new Socket(ip, port);
                client.setSoTimeout(1000);
                PrintStream outToServer = new PrintStream(client.getOutputStream());
                outToServer.println(abort);
                client.close();
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }
}
