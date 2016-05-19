package nl.hr.shiptogether;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LandingActivity extends AppCompatActivity implements View.OnClickListener {

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                SocketHandler socketHandler = new SocketHandler();
                socketHandler.socketConnection();
            }
        }).start();
    }

    public void openAcountCreation(){
        Intent intent = new Intent(this, CreateAcount.class);
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

        if(String.valueOf(username.getText()).equals("username") && String.valueOf(password.getText()).equals("password")){
            System.out.println("Gefeliciteerd, u bent ingelogd!");

            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        } else {
            System.out.println("Probeer het nog maar eens een keer.");
        }
    }
}
