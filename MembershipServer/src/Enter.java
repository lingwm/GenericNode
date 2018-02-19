/**
 * Created by Lingwei Meng on 2017/12/14.
 */
public class Enter {
    public static void main(String argv[]){
        if (argv[0].equals("ts")){
            TCPServer TCPserver=new TCPServer();
            TCPserver.run(argv[1]);
        }
    }
}
