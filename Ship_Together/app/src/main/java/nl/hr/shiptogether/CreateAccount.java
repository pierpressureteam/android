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
        button.setOnClickListener(new View.OnClickListener() {

            EditText name = (EditText) findViewById(R.id.txtUsername);
            EditText pass = (EditText) findViewById(R.id.txtPassword);
            EditText repeatPass = (EditText) findViewById(R.id.txtRepeatPassword);

            //todo check if username is availible

            //check if password is valid
            public void onClick(View v) {
                if(pass.getText().toString().length() <6){
                    notifyUser("Password needs to be at least 6 characters");
                }
                else if(!pass.getText().toString().equals(repeatPass.getText().toString())){
                    notifyUser("The repeat password is not the same as the password.");
                } else {
                    EditText username = (EditText) findViewById(R.id.txtUsername);
                    EditText password = (EditText) findViewById(R.id.txtPassword);

                    String sUsername = username.getText().toString();
                    String sPassword = password.getText().toString();

                    System.out.println(sUsername);
                    System.out.println(sPassword);

                    User user = new User(sUsername, sPassword, "email currently not being entered!");
                    SocketObjectWrapper sow = new SocketObjectWrapper(user, 2);

                    new NetworkHandler().execute(sow);
                }


            }
        });
    }

    private void notifyUser(String text){
        Toast.makeText(getApplicationContext(),text,  Toast.LENGTH_LONG).show();
    }

    class NetworkHandler extends AsyncTask<SocketObjectWrapper, Void, Boolean> {
        private Exception exception;
        SocketClient sc = new SocketClient();

        @Override
        protected Boolean doInBackground(SocketObjectWrapper... params) {
            Boolean success = false;
            SocketObjectWrapper sow = params[0];

            try {
                success = (boolean) sc.communicateWithSocket(sow);
                return success;

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("nope");
                return null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                System.out.println("Le nope");
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
