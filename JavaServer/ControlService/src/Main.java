/**
 * Date:2020/02/15
 * Name:zt
 * Use for:Main function
 * @author ztdey
 */
public class Main {
    public static void main(String[] args) {
        TCPCore myTCPCore=new TCPCore();
        myTCPCore.setupCacheThreadPool();

        myTCPCore.ServerSocket=myTCPCore.setupServerSocket(4000);

        myTCPCore.makeThread();

    }
}
