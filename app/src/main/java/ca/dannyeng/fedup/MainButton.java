package ca.dannyeng.fedup;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.io.File;


public class MainButton extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        final Chronometer myChronometer = (Chronometer)findViewById(R.id.chronometer);
        ImageButton buttonstart = (ImageButton)findViewById(R.id.buttonstart);
        Button buttonStop = (Button)findViewById(R.id.buttonstop);

        /*
        File file = new File("dopedpg.gif");
        String filePath = file.getAbsolutePath();

        ImageButton buttonGif = (ImageButton) findViewById(R.id.gif_button);
        Glide.with(this).load("http://imgur.com/LJVyRHm").into(buttonGif);
*/
        buttonstart.setOnClickListener(new Button.OnClickListener(){



            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                myChronometer.setBase(SystemClock.elapsedRealtime());
                myChronometer.start();
            }
        });

        buttonStop.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                myChronometer.stop();

            }});




        //title button that brings you to the settings page
        Button pushsettingbutton = (Button) findViewById(R.id.settingbutton);

        pushsettingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(getApplicationContext(), Settings.class);
                startActivity(i2);
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_button, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
