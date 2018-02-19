import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by Lingwei Meng on 2017/12/13.
 */
public class TCPDiscovery implements Runnable{

    public TCPDiscovery(){
        new Thread(this).start();
    }

    public void run(){
        try {
            Socket client=new Socket(TCPServer.membershipServer,4410);
            PrintStream outToServer = new PrintStream(client.getOutputStream());
            BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
            boolean loop=true;
            while (loop){
                String message="store";
                outToServer.println(message);
                String temp=buf.readLine();
                if ((temp != null) && (temp.length() > 0)){
                    String[] s1=temp.split(";");
                    for (String str:s1){
                        if (!str.equals("")&&str!=null){
                            String[] s2=str.split(",");
                            boolean exist=false;
                            for (String[] sa:TCPServer.membership){
                                if (sa[0].equals(s2[0])&&sa[1].equals(s2[1])){
                                    exist=true;
                                }
                            }
                            if (!exist){
                                TCPServer.membership.add(s2);
                            }
                        }
                    }
                }
                Thread.sleep(5000);
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (InterruptedException e){}
    }
}
