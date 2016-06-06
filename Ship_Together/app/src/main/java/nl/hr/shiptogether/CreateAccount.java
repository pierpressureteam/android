package nl.hr.shiptogether;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import objectslibrary.SocketObjectWrapper;
import objectslibrary.User;
import socketclient.SocketClient;

public class CreateAccount extends AppCompatActivity {

    Spinner spinner;
    ArrayList<String> listTypeBigName = new ArrayList();
    ArrayList<String> listTypeName = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        spinner = (Spinner) findViewById(R.id.spinner);

        SocketObjectWrapper sow = new SocketObjectWrapper(null, 5);
        new NetworkHandlerSpinner().execute(sow);

        final Button button = (Button) findViewById(R.id.btnCreateAcc);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        EditText name = (EditText) findViewById(R.id.txtUsername);
                        EditText pass = (EditText) findViewById(R.id.txtPassword);
                        EditText repeatPass = (EditText) findViewById(R.id.txtRepeatPassword);
                        EditText email = (EditText) findViewById(R.id.txtEmail);
                        EditText mmsi = (EditText) findViewById(R.id.nmbrMMSI);
                        String sMMSI = mmsi.getText().toString();
                        Spinner shipType = (Spinner) findViewById(R.id.spinner);

                        if (pass.getText().toString().length() < 6) {
                            notifyUser("Password needs to be at least 6 characters");
                        } else if (!pass.getText().toString().equals(repeatPass.getText().toString())) {
                            notifyUser("The repeat password is not the same as the password.");
                        } else {
                            String sUsername = name.getText().toString();
                            String sPassword = pass.getText().toString();
                            String sEmail = email.getText().toString();

                            int integerMMSI = Integer.valueOf(sMMSI);


                            String sShipTypeSelected = shipType.getSelectedItem().toString();
                            int shortNameLocation = listTypeBigName.indexOf(sShipTypeSelected);
                            String sShipType = listTypeName.get(shortNameLocation);

                            User user = new User(sUsername, sPassword, sEmail, integerMMSI, sShipType);
                            SocketObjectWrapper sow = new SocketObjectWrapper(user, 2);

                            new NetworkHandlerRegistration().execute(sow);

                        }
                    } catch (Exception e) {
                        notifyUser("Please enter a correct value in all fields.");
                    }
                }
            });
        }
    }

    private void notifyUser(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    class NetworkHandlerSpinner extends AsyncTask<SocketObjectWrapper, Void, ArrayList<String[]>> {
        SocketClient sc = new SocketClient();

        @Override
        protected ArrayList<String[]> doInBackground(SocketObjectWrapper... params) {
            ArrayList<String[]> list;
            SocketObjectWrapper sow = params[0];

            try {
                list = (ArrayList<String[]>) sc.communicateWithSocket(sow);
                return list;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String[]> strings) {

            for (String[] names : strings) {
                listTypeName.add(names[0]);
                listTypeBigName.add(names[1]);
            }

            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, listTypeBigName);
            spinner.setAdapter(adapter);
        }
    }

    class NetworkHandlerRegistration extends AsyncTask<SocketObjectWrapper, Void, Boolean> {
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
                Toast toast = Toast.makeText(getApplicationContext(), "Username and/or e-mail and/or MMSI are already taken.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
