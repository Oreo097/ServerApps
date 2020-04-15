import java.io.*;
import java.net.Socket;

/**
 * Date:2020/04/14
 * Name:zt
 * Use for:set user object
 */
public class User {

    public Socket mySocket;


    private OutputStream ostream;
    private PrintWriter pwriter;
    private InputStream istream;
    private BufferedReader breader;




    public User(Socket mySocket){
        this.mySocket=mySocket;
    }







    public void initReceive() {//init receive function
        try {
            istream = mySocket.getInputStream();
            breader = new BufferedReader(new InputStreamReader(istream));
            System.out.println("initReceive:function Receive inited");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initSend() {//init send function
        try {
            ostream = mySocket.getOutputStream();
            pwriter = new PrintWriter(ostream);
            System.out.println("initSend:function Send inited");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void init(){
        initSend();
        initReceive();
    }

    public void Write(String message){//write to socket
        try {
            pwriter.write(message+ "\n");
            pwriter.flush();// clear buffer
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String Read(){//read from socket
        String message=null;
        try {
            message = breader.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return message;
    }

    public void shutdown() throws IOException {
        mySocket.shutdownInput();
        mySocket.shutdownOutput();
        mySocket.close();
    }


}
