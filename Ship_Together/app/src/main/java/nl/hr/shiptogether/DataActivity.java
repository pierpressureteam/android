package nl.hr.shiptogether;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

import socketclient.SocketClient;


public class DataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
    }
    /*
    public void connectToServer() throws IOException, ClassNotFoundException {
        Object object = 1;
        SocketClient socketClient = new SocketClient();
        Object socketObject = socketClient.communicateWithSocket(object);

    }
    */
}
