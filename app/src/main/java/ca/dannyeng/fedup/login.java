package ca.dannyeng.fedup;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


//        try{
//            //store data
//            HttpClient client = new DefaultHttpClient();
//            HttpPost httpPost = new HttpPost("http://52.88.98.2/index.php?_url=/interaction");
//            JSONObject data = new JSONObject();
//            data.put("username", "1");
//            data.put("filename", "2");
//            data.put("longitude", "3");
//            data.put("latitude", "4");
//            data.put("timestamp", "5");
//
//            StringEntity se = new StringEntity(data.toString());
//
//            //Encoding POST data
//            httpPost.setEntity(se);
//
//            //Headers
//            httpPost.setHeader("Accept", "application/json");
//            httpPost.setHeader("Content-type", "application/json");
//
//            HttpResponse response = client.execute(httpPost);
//        } catch (JSONException e){
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e){
//            e.printStackTrace();
//        } catch (ClientProtocolException e){
//            e.printStackTrace();
//        } catch (IOException e){
//            e.printStackTrace();
//        }


        //title button that brings you to the settings page
        Button pushenterbutton = (Button)findViewById(R.id.enterbutton);

        pushenterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText usernameET = (EditText) findViewById(R.id.editText2);
                String username = usernameET.getText().toString();
                if(username.equals("")){
                    loggedIn();
                    return;
                }

                EditText passwordET = (EditText) findViewById(R.id.editText3);
                String password = passwordET.getText().toString();

                new LoginTask().execute(username, password);

                loggedIn();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    private class LoginTask extends AsyncTask<String, Integer, Long> {
        protected Long doInBackground(String... credentials) {
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI("http://52.88.98.2/index.php?_url=/user/"+credentials[0]));
                ResponseHandler<String> responseHandler=new BasicResponseHandler();
                String response = client.execute(request, responseHandler);
                JSONObject user = new JSONObject(response);
                if(user.get("password").toString().equals(credentials[1])){
                    //Successful login
                    MyApp mApp = ((MyApp)getApplication());
                    mApp.setUsername(user.get("username").toString());
                    mApp.setId(user.get("id").toString());
                    return 0l;
                } else {
                    //Invalid credentials
                    return 10l;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();
            }
            return 0l;
        }


        protected void onPostExecute(Long result) {
            if(result == 10){
                makeToast();
            } else {
                loggedIn();
            }
        }
    }

    private void makeToast() {
        CharSequence text = "Invalid User";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }

    private void loggedIn() {

        Intent i2 = new Intent(getApplicationContext(), MainButton.class);
        startActivity(i2);
    }
}
