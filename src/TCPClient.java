import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * TCP Client
 * Created by Lingwei Meng on 2017/11/6.
 */
public class TCPClient {
    /**
     * operation:store,exit
     * @param url
     * @param port
     * @param operation
     * @throws Exception
     */
    public void request(String url,String port,String operation) throws Exception{
        String sentence=operation + "\n";
        //System.out.println("OUT TO SERVER="  + sentence);
        Socket clientSocket=connect(url,port);
        //to server
        DataOutputStream outToServer=new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes(sentence);
        //from server
        BufferedReader inFromServer=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String temp;
//        while ((temp=inFromServer.readLine())!=null){
//            System.out.println(temp);
//        }
        do
        {
            temp = inFromServer.readLine();
            if ((temp != null) && (temp.length() > 0)){
                String[] strs=temp.split(";");
                for (String str:strs){
                    System.out.println(str);
                }
            }

        }
        while (inFromServer.ready());


        clientSocket.close();
    }

    /**
     * operation: get,delete
     * @param url
     * @param port
     * @param operation
     * @param key
     * @throws Exception
     */
    public void request(String url,String port,String operation,String key) throws Exception{
        String sentence=operation+","+key + "\n";
        //System.out.println("OUT TO SERVER="  + sentence);
        Socket clientSocket=connect(url,port);
        //to server
        DataOutputStream outToServer=new DataOutputStream(clientSocket.getOutputStream());
        //System.out.println("write bytes to socket");
        outToServer.writeBytes(sentence);
        //System.out.println("read from socket");
        //from server
        BufferedReader inFromServer=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        //System.out.println("processing input");
        String temp;
//        while ((temp=inFromServer.readLine())!=null){
//            System.out.println(temp);
//        }
        do
        {
            temp = inFromServer.readLine();
            //System.out.println("Read=" + temp);
            if ((temp != null) && (temp.length() > 0))
                System.out.println(temp);
        }
        while (inFromServer.ready());

        clientSocket.close();
    }

    /**
     * operation: put
     * @param url
     * @param port
     * @param operation
     * @param key
     * @param value
     * @throws Exception
     */
    public void request(String url,String port,String operation,String key,String value) throws Exception{
        String sentence=operation+","+key+","+value;
        //System.out.println("OUT TO SERVER="  + sentence);
        Socket clientSocket=connect(url,port);
        //to server
        DataOutputStream outToServer=new DataOutputStream(clientSocket.getOutputStream());
        outToServer.writeBytes(sentence+"\n");
        //from server
        BufferedReader inFromServer=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String temp;
//        while ((temp=inFromServer.readLine())!=null){
//            System.out.println(temp);
//        }

        do
        {
            temp = inFromServer.readLine();
            if ((temp != null) && (temp.length() > 0))
                System.out.println(temp);
        }
        while (inFromServer.ready());

        clientSocket.close();
    }

    /**
     * setup connection with server
     * @param url
     * @param port
     * @return
     * @throws Exception
     */
    private Socket connect(String url,String port)throws Exception{
        int portNumber = Integer.parseInt(port);
        Socket clientSocket=new Socket(url,portNumber);
        return  clientSocket;
    }


}
