package ca.dannyeng.fedup;

import android.content.Intent;
import android.media.Image;
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
import com.koushikdutta.ion.Ion;

import android.widget.ImageView;

import java.io.File;


public class MainButton extends AppCompatActivity {

    boolean timerStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
        timerStarted = false;
        final Chronometer myChronometer = (Chronometer)findViewById(R.id.chronometer);
        //ImageButton buttonstart = (ImageButton)findViewById(R.id.buttonstart);

        /*
        ImageView gif = (ImageView) findViewById(R.id.gif_button);
        Ion.with(gif).load("http://i.imgur.com/LJVyRHm.gif");
        */

        File file = new File("dopedpg.gif");
        String filePath = file.getAbsolutePath();

        ImageButton buttonstart = (ImageButton) findViewById(R.id.buttonstart);
        Glide.with(this).load("http://i.imgur.com/LJVyRHm.gif").into(buttonstart);
        buttonstart.setOnClickListener(new Button.OnClickListener() {


            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!timerStarted) {
                    myChronometer.setBase(SystemClock.elapsedRealtime());
                    myChronometer.start();
                    timerStarted = true;
                }
                else {

                    myChronometer.stop();
                    timerStarted = false;
                    return;
                }

            }
        });

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
