package ca.dannyeng.fedup;

import android.app.Application;
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
import android.media.MediaRecorder;
import java.io.IOException;
import android.widget.Toast;
import android.os.Environment;
import android.media.MediaPlayer;
import android.util.Log;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.UUID;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.koushikdutta.ion.Ion;

import android.widget.ImageView;

import java.io.File;

import com.firebase.client.Firebase;


public class MainButton extends AppCompatActivity {
    private MediaRecorder mRecorder;
    private Boolean record = true;

    boolean timerStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
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

        //submit to dropbox button
        Button dropboxbutton = (Button) findViewById(R.id.button);
        dropboxbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3 = new Intent(getApplicationContext(), Upload.class);
                startActivity(i3);
            }
        });

        //audio record button
        ImageButton recordButton = (ImageButton)findViewById(R.id.buttonstart);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(record){
                    CharSequence text = "Recording started";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                    SecureRandom random = new SecureRandom();
                    String uuid = new BigInteger(130, random).toString(32);
                    String mFileName = uuid+"_test.3gp";
                    MyApp mApp = ((MyApp)getApplication());
                    mApp.setFilename(mFileName);
                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    Log.e("LINK", "/storage/sdcard0/" + mFileName);
                    mRecorder.setOutputFile("/storage/sdcard0/" + mFileName);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

                    try {
                        mRecorder.prepare();
                    } catch (IOException e) {
                        Log.e("AUDIO_RECORD", "prepare() failed");
                    }

                    mRecorder.start();
                } else {
                    CharSequence text = "Recording stopped";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();
                    mRecorder.stop();
                    mRecorder.reset();
                    mRecorder.release();
                    mRecorder = null;
                }
                record = !record;

            }
        });

        //title button that brings you to the settings page
        //ImageButton pushstartbutton = (ImageButton)findViewById(R.id.startbutton);

        //pushstartbutton.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View v) {
                //chrono.setBase(SystemClock.elapsedRealtime());
               // chrono.start();
           // }
        //});
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
