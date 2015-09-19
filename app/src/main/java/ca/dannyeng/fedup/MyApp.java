package ca.dannyeng.fedup;

import android.app.Application;

/**
 * Created by Kevin on 15-09-19.
 */
public class MyApp extends Application {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String str) {
        username = str;
    }
}
