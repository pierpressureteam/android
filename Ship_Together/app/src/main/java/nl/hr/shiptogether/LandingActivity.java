package nl.hr.shiptogether;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import socketclient.SocketClient;
import objectslibrary.User;
import objectslibrary.SocketObjectWrapper;

public class LandingActivity extends AppCompatActivity implements View.OnClickListener {

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
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Username or password is incorrect", Toast.LENGTH_SHORT);
                toast.show();
            }

        }
    }

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loginButton = (Button) findViewById(R.id.button);
        loginButton.setOnClickListener(LandingActivity.this);

        final Button accountCreateBtn = (Button) findViewById(R.id.button2);
        accountCreateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openAcountCreation();
            }
        });

    }

    public void openAcountCreation() {
        Intent intent = new Intent(this, CreateAccount.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_landing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, managementActivity.class);

            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        EditText username = (EditText) findViewById(R.id.editText);
        EditText password = (EditText) findViewById(R.id.editText2);

        SocketClient sc = new SocketClient();

        String sUsername = username.getText().toString();
        String sPassword = password.getText().toString();

        System.out.println(sUsername);
        System.out.println(sPassword);

        User user = new User(sUsername, sPassword, "email currently not being validated!");
        SocketObjectWrapper sow = new SocketObjectWrapper(user, 1);

        new NetworkHandler().execute(sow);

    }

}


