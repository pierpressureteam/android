package nl.hr.shiptogether;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Roy van den Heuvel on 04/05/2016.
 */
public class SocketHandler {
    public SocketHandler(){}

    public void socketConnection(){
        try {

            String message = "DIT IS EEN TEST, GEEN PANIEK!";

            String serverIp = "145.24.222.149";
            Socket client = new Socket(serverIp, 4444);  //connect to server
            PrintWriter printwriter = new PrintWriter(client.getOutputStream(), true);
            printwriter.write(message);  //write the message to output stream

            printwriter.flush();
            printwriter.close();
            client.close();   //closing the connection

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
