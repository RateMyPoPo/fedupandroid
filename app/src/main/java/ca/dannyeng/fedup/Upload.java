package ca.dannyeng.fedup;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.media.MediaRecorder;
import java.io.IOException;
import android.widget.Toast;
import android.os.Environment;
import android.os.AsyncTask;
import android.util.Log;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.firebase.client.Firebase;

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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Upload extends AppCompatActivity {
    final static private String APP_KEY = "rnizd2wt4vhv4cn";
    final static private String APP_SECRET = "k8exfknbce45n5g";
    private DropboxAPI<AndroidAuthSession> mDBApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_button);

        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        mDBApi.getSession().startOAuth2Authentication(Upload.this);
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

    protected void onResume() {
        super.onResume();

        if (mDBApi.getSession().authenticationSuccessful()) {
            try {
                // Required to complete auth, sets the access token on the session
                mDBApi.getSession().finishAuthentication();

                String accessToken = mDBApi.getSession().getOAuth2AccessToken();
                MyApp mApp = ((MyApp)getApplication());

                new UploadFilesTask().execute(mApp.getFilename());

            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }

    private class UploadFilesTask extends AsyncTask<String, Integer, Long> {
        protected Long doInBackground(String... file_name) {
            Log.e("FILE", file_name[0]);
            File tmpFile = new File("/storage/sdcard0/", file_name[0]);

            try {
                //global variables
                MyApp mApp = ((MyApp)getApplication());
                Firebase firebase = new Firebase("https://fedup.firebaseio.com/");

                //File upload
                FileInputStream fis = new FileInputStream(tmpFile);
                Entry newEntry = mDBApi.putFile(file_name[0], fis, tmpFile.length(), null, null);
                fis.close();

                //get Geolocation
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                String locationProvider = LocationManager.NETWORK_PROVIDER;
                Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);

                //get Timestamp
                long unixTime = System.currentTimeMillis() / 1000L;

                //store data
                HttpClient client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://52.88.98.2/index.php?_url=/interaction/");
                JSONObject data = new JSONObject();
                data.put("username", mApp.getUsername());
                data.put("file_name", file_name[0]);
                data.put("longitude", Double.toString(lastKnownLocation.getLongitude()));
                data.put("latitude", Double.toString(lastKnownLocation.getLatitude()));
                data.put("timestamp", unixTime);
                StringEntity se = new StringEntity(data.toString());

                //Encoding POST data
                httpPost.setEntity(se);

                //Headers
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                HttpResponse response = client.execute(httpPost);

                firebase.child(mApp.getId()).child("file_name").setValue(file_name[0]);
                firebase.child(mApp.getId()).child("longitude").setValue(lastKnownLocation.getLongitude());
                firebase.child(mApp.getId()).child("latitude").setValue(lastKnownLocation.getLatitude());
                firebase.child(mApp.getId()).child("timestamp").setValue(unixTime);

            } catch (DropboxUnlinkedException e) {
                Log.e("DbExampleLog", "User has unlinked.");
            } catch (DropboxException e) {
                Log.e("DbExampleLog", "Something went wrong while uploading.");
            } catch (FileNotFoundException e) {
                Log.e("FILE IO", "file not found:"+e);
            } catch (IOException e){
                Log.e("FILE IO", "io exception: "+e);
            } catch (JSONException e){
                e.printStackTrace();
            }
            return 0l;
        }

        protected void onPostExecute(Long result) {
            returnToMain();
        }
    }

    private void returnToMain() {
        Intent i2 = new Intent(getApplicationContext(), MainButton.class);
        startActivity(i2);
    }



}
