package nl.hr.shiptogether;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import objectslibrary.SocketObjectWrapper;
import objectslibrary.User;
import socketclient.SocketClient;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acount);

        final Button button = (Button) findViewById(R.id.btnCreateAcc);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {

                EditText name = (EditText) findViewById(R.id.txtUsername);
                EditText pass = (EditText) findViewById(R.id.txtPassword);
                EditText repeatPass = (EditText) findViewById(R.id.txtRepeatPassword);

                public void onClick(View v) {
                    if (pass.getText().toString().length() < 6) {
                        notifyUser("Password needs to be at least 6 characters");
                    } else if (!pass.getText().toString().equals(repeatPass.getText().toString())) {
                        notifyUser("The repeat password is not the same as the password.");
                    } else {
                        String sUsername = name.getText().toString();
                        String sPassword = pass.getText().toString();

                        System.out.println(sUsername);
                        System.out.println(sPassword);

                        User user = new User(sUsername, sPassword);
                        SocketObjectWrapper sow = new SocketObjectWrapper(user, 2);

                        new NetworkHandler().execute(sow);
                    }
                }
            });
        }
    }

    private void notifyUser(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    class NetworkHandler extends AsyncTask<SocketObjectWrapper, Void, Boolean> {
        SocketClient sc = new SocketClient();

        @Override
        protected Boolean doInBackground(SocketObjectWrapper... params) {
            SocketObjectWrapper sow = params[0];

            try {
                Object receivedObj = sc.communicateWithSocket(sow);
                if (receivedObj != null) {
                    return true;
                } else {
                    return false;
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(Boolean success) {
            if (success) {
                Intent intent = new Intent(getApplicationContext(), LandingActivity.class);
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Username and/or e-mail are already taken.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
