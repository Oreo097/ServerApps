import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Date:2020/04/14
 * Name:zt
 * Use for:tcp core main body of this program
 */
public class TCPCORE {
    /**
     * this is the main body of the project is user for set up tcp service for blinker on Arduino
     * use tcp to send & receive message to control device
     */
    public String message;


    private ExecutorService ThreadPool_TCP;//thread pool used for tcp service
    private ServerSocket Blinker_ServerSocket=null;
    private ServerSocket Other_serverSocket=null;

    public static void main(String[] args) {


    }

    public void setupCacheThreadPool() {
        ThreadPool_TCP = Executors.newFixedThreadPool(10);
        System.out.println("setupCacheThreadPool:set up!");
    }



    public ServerSocket setupServerScoekt(int port) {//this is function for set upo server
        ServerSocket myServerSocket=null;
        try{
            myServerSocket=new ServerSocket(port);
        }catch(Exception e){
            e.printStackTrace();
        }
        return myServerSocket;
    }

    public Socket setupSocket(ServerSocket myServerSocket) {//set up socket to communicate
        Socket mySocket = null;
        try {
            System.out.println("setupSocket_blinker:set up server socket!");
            mySocket = myServerSocket.accept();
            System.out.println("setupSocket:connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mySocket;
    }

    public boolean service_Thread(){//main service thread
        boolean checkpoint=true;
        String message;
        String sayHello="connected";
        Socket myScoket=setupSocket(Blinker_ServerSocket);
        USER myUser=new USER(myScoket);
        myUser.init();
        myUser.Write(sayHello);
        message=myUser.Read();
        if(!message.equals("connected")){
            return false;
        }
        while(checkpoint){
            message=myUser.Read();

        }
        return true;
    }



}
