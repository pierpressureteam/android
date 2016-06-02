package nl.hr.shiptogether;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAcount extends AppCompatActivity {

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
                }
                //todo save to database

            }
        });
    }


    private void notifyUser(String text){
        Toast.makeText(getApplicationContext(),text,  Toast.LENGTH_LONG).show();

    }
}
