package ca.dannyeng.fedup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        MyApp mApp = ((MyApp)getApplication());
        Firebase firebase = new Firebase("https://fedup.firebaseio.com/"+mApp.getId());

        setContentView(R.layout.activity_settings);

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String personal_message = snapshot.child("personal_message").getValue().toString();
                EditText personal_message_field = (EditText)findViewById(R.id.editText);
                personal_message_field.setText(personal_message);

                String ec1Phone = snapshot.child("ec1_phone").getValue().toString();
                EditText ec1_phone_field = (EditText)findViewById(R.id.contactphone1);
                ec1_phone_field.setText(ec1Phone);

                String ec2Phone = snapshot.child("ec2_phone").getValue().toString();
                EditText ec2_phone_field = (EditText)findViewById(R.id.contactphone2);
                ec2_phone_field.setText(ec2Phone);

                String ec3Phone = snapshot.child("ec3_phone").getValue().toString();
                EditText ec3_phone_field = (EditText)findViewById(R.id.contactphone3);
                ec3_phone_field.setText(ec3Phone);

                String ec1Email = snapshot.child("ec1_email").getValue().toString();
                EditText ec1_email_field = (EditText)findViewById(R.id.contactemail1);
                ec1_email_field.setText(ec1Email);

                String ec2Email = snapshot.child("ec2_email").getValue().toString();
                EditText ec2_email_field = (EditText)findViewById(R.id.contactemail2);
                ec2_email_field.setText(ec2Email);

                String ec3Email = snapshot.child("ec3_email").getValue().toString();
                EditText ec3_email_field = (EditText)findViewById(R.id.contactemail3);
                ec3_email_field.setText(ec3Email);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                //System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        //title button that brings you to the settings page
        Button pushsavebutton = (Button)findViewById(R.id.savebutton);

        pushsavebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyApp mApp = ((MyApp)getApplication());
                Firebase firebase = new Firebase("https://fedup.firebaseio.com/");

                //Personal message
                EditText personal_message = (EditText)findViewById(R.id.editText);
                firebase.child(mApp.getId()).child("personal_message").setValue(personal_message.getText().toString());

                //ec1Phone
                EditText ec1Phone = (EditText)findViewById(R.id.contactphone1);
                firebase.child(mApp.getId()).child("ec1_phone").setValue(ec1Phone.getText().toString());

                //ec1Email
                EditText ec1Email = (EditText)findViewById(R.id.contactemail1);
                firebase.child(mApp.getId()).child("ec1_email").setValue(ec1Email.getText().toString());

                //ec2Phone
                EditText ec2Phone = (EditText)findViewById(R.id.contactphone2);
                firebase.child(mApp.getId()).child("ec2_phone").setValue(ec2Phone.getText().toString());

                //ec2Email
                EditText ec2Email = (EditText)findViewById(R.id.contactemail2);
                firebase.child(mApp.getId()).child("ec2_email").setValue(ec2Email.getText().toString());

                //ec3Phone
                EditText ec3Phone = (EditText)findViewById(R.id.contactphone3);
                firebase.child(mApp.getId()).child("ec3_phone").setValue(ec3Phone.getText().toString());

                //ec3Email
                EditText ec3Email = (EditText)findViewById(R.id.contactemail3);
                firebase.child(mApp.getId()).child("ec3_email").setValue(ec3Email.getText().toString());


                Intent i2 = new Intent(getApplicationContext(), MainButton.class);
                startActivity(i2);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
