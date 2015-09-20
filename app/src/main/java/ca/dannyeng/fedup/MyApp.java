package ca.dannyeng.fedup;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by Kevin on 15-09-19.
 */
public class MyApp extends Application {

    private String username;

    private String id;

    private String file_name;

    public String getUsername() {
        return username;
    }

    public void setUsername(String str) {
        username = str;
    }

    public String getId() {
        return id;
    }

    public void setId(String str) {
        id = str;
    }

    public String getFilename() {
        return file_name;
    }

    public void setFilename(String str) {
        file_name = str;
    }
}
