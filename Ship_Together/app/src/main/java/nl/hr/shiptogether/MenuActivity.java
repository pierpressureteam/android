package nl.hr.shiptogether;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mapdatabutton;
    private Button databutton;
    private Button graphbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mapdatabutton = (Button) findViewById(R.id.mapdatabutton);
        databutton = (Button) findViewById(R.id.databutton);
        graphbutton = (Button) findViewById(R.id.graphbutton);

        mapdatabutton.setOnClickListener( MenuActivity.this);
        databutton.setOnClickListener( MenuActivity.this);
        graphbutton.setOnClickListener( MenuActivity.this);
    }

    public void onClick(View v) {

        if (v.getId() == R.id.mapdatabutton) {
            Intent intent = new Intent(this, MapDataActivity.class);

            startActivity(intent);
        }

        if (v.getId() == R.id.databutton) {
            Intent intent = new Intent(this, DataActivity.class);

            startActivity(intent);
        }

        if (v.getId() == R.id.graphbutton) {
            Intent intent = new Intent(this, GraphActivity.class);

            startActivity(intent);
        }

    }
}
