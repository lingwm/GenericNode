/**
 * recognize arguments syntax and call functions
 * print help output
 * Created by Lingwei Meng on 2017/11/6.
 */
public class Enter {
    public static void main(String argv[]) throws Exception{
        int arg=argv.length;
        switch (arg){
            case 3: //TCP&UDP server
                switch (argv[0]){
                    case "ts":
                        //argv[2] is address of membership server
                        TCPServer TCPserver=new TCPServer(argv[1],argv[2]);
                        TCPserver.run();
                }
                break;
            case 4:
                switch (argv[0]){
                    case "tc":
                        TCPClient TCPclient=new TCPClient();
                        TCPclient.request(argv[1],argv[2],argv[3]);
                }
                break;
            case 5:
                switch (argv[0]){
                    case "tc":  //TCP get,delete
                        TCPClient TCPclient=new TCPClient();
                        TCPclient.request(argv[1],argv[2],argv[3],argv[4]);
                }
                break;
            case 6: //TCP&UDP put
                switch (argv[0]){
                    case "tc":
                        TCPClient TCPclient=new TCPClient();
                        TCPclient.request(argv[1],argv[2],argv[3],argv[4],argv[5]);
                }
                break;
            default:
                printHelp();
                break;
        }
    }

    private static void printHelp(){
        System.out.println("Usage:\n" +
                "Client:\n" +
                "uc/tc <address> <port> put <key> <msg>  UDP/TCP CLIENT: Put an object into store\n" +
                "uc/tc <address> <port> get <key>  UDP/TCP CLIENT: Get an object from store by key\n" +
                "uc/tc <address> <port> del <key>  UDP/TCP CLIENT: Delete an object from store by key\n" +
                "uc/tc <address> <port> store  UDP/TCP CLIENT: Display object store\n" +
                "uc/tc <address> <port> exit  UDP/TCP CLIENT: Shutdown server\n" +
                "rmic <address> put <key> <msg>  RMI CLIENT: Put an object into store\n" +
                "rmic <address> get <key>  RMI CLIENT: Get an object from store by key\n" +
                "rmic <address> del <key>  RMI CLIENT: Delete an object from store by key\n" +
                "rmic <address> store  RMI CLIENT: Display object store\n" +
                "rmic <address> exit  RMI CLIENT: Shutdown server\n" +
                "Server:\n" +
                "us/ts <port>  UDP/TCP/TCP-and-UDP SERVER: run server on <port>.\n" +
                "tus <tcpport> <udpport>  TCP-and-UDP SERVER: run servers on <tcpport> and <udpport> sharing same key-value store.\n" +
                "alls <tcpport> <udpport>  TCP, UDP, and RMI SERVER: run servers on <tcpport> and <udpport> sharing same key-value store.\n" +
                "rmic  RMI Server.\n");
    }
}
