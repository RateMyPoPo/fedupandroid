package ca.dannyeng.fedup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Upload extends AppCompatActivity {
    final static private String APP_KEY = "ozpikev4q244m1o";
    final static private String APP_SECRET = "geutw4qniw71iyh";
    private DropboxAPI<AndroidAuthSession> mDBApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                Log.e("DROPBOX", "access granted");

                new UploadFilesTask().execute("test123.mp4");

            } catch (IllegalStateException e) {
                Log.i("DbAuthLog", "Error authenticating", e);
            }
        }
    }

    private class UploadFilesTask extends AsyncTask<String, Integer, Long> {
        protected Long doInBackground(String... file_name) {
            Log.e("FILE", file_name[0]);
            File tmpFile = new File("/sdcard/", file_name[0]);

            try {
                FileInputStream fis = new FileInputStream(tmpFile);
                Entry newEntry = mDBApi.putFile(file_name[0], fis, tmpFile.length(), null, null);
                fis.close();
            } catch (DropboxUnlinkedException e) {
                Log.e("DbExampleLog", "User has unlinked.");
            } catch (DropboxException e) {
                Log.e("DbExampleLog", "Something went wrong while uploading.");
            } catch (FileNotFoundException e){
                Log.e("FILE IO", "file not found:"+e);
            } catch (IOException e){
                Log.e("FILE IO", "io exception: "+e);
            }
            return 0l;
        }


        protected void onPostExecute(Long result) {
            Log.e("FILE UPLOAD", "completed");
        }
    }

}
