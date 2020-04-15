import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Date:2020/04/14
 * Name:zt
 * Use for:tcp core main body of this program
 *
 * @author ztdey
 */
public class TCPCore {
    /**
     * this is the main body of the project is user for set up tcp service for blinker on Arduino
     * use tcp to send & receive message to control device
     */


    private ExecutorService ThreadPool_TCP;

    public ServerSocket ServerSocket = null;

    private int task_num = 0;
    private int task_max = 10;
    public boolean checkpoint = true;

    public TCPCore() {

    }

    public void setupCacheThreadPool() {
        ThreadPool_TCP = Executors.newFixedThreadPool(10);
        System.out.println("setupCacheThreadPool:set up!");
    }


    /**
     * this is function for set upo server
     *
     * @param port
     * @return
     */
    public ServerSocket setupServerSocket(int port) {
        ServerSocket myServerSocket = null;
        try {
            myServerSocket = new ServerSocket(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myServerSocket;
    }

    /**
     * this is function for set upo server
     *
     * @param myServerSocket
     * @return
     */
    public Socket setupSocket(ServerSocket myServerSocket) {
        Socket mySocket = null;
        try {
            System.out.println("setupSocket:set up server socket!");
            mySocket = myServerSocket.accept();
            System.out.println("setupSocket:connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mySocket;
    }

    /**
     * Main service function
     *
     * @return
     */
    public boolean service_Thread(ServerSocket targetSocket) {
        this.task_num--;
        boolean checkpoint = true;
        String message;
        String sayHello = "connected";
        Socket mySocket = setupSocket(targetSocket);
        User myUser = new User(mySocket);
        myUser.init();
        myUser.Write(sayHello);
        message = myUser.Read();
        String device_name = null;
        if (message.equals(null)) {
            System.out.println("Wrong!");
            try{
                myUser.shutdown();
            }catch(Exception e){
                e.printStackTrace();
            }
            return false;
        }
        device_name=message;
        myUser.Write("complete");
        myUser.Write(device_name);
        System.out.println("Service_Thread:handshake complete");
        while (checkpoint) {
            message = myUser.Read();
            System.out.println("Message:"+device_name+":"+message);
            if (!message.equals(null)) {
                //check out turn off socket command at first
                String respondence_end = "#END";
                if (message.equals(respondence_end)) {
                    try {
                        myUser.shutdown();//shut down socket
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //use shell to run script
                try {
                    System.out.println("Shell command engage");
                    Process ps = Runtime.getRuntime().exec("sudo "+message);
                    ps.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                myUser.Write("Finish");
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public Runnable service_runnable = new Runnable() {
        @Override
        public void run() {
            service_Thread(ServerSocket);
        }
    };


    public void makeThread() {

        while (task_num <= task_max) {
            Thread service_thread = new Thread(service_runnable);
            System.out.println("makeThread:lost task number:"+(task_max-task_num));
            ThreadPool_TCP.submit(service_thread);
            System.out.println("makeThread():thread submit");
            this.task_num++;
        }
    }


}
