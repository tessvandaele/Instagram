package com.codepath.instagram;

import android.app.Application;

import com.codepath.instagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

/* connects to the backend to access and edit user data */

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //register our parse models
        ParseObject.registerSubclass(Post.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Ymb4uHNa3PekdrJtTx9lwOdZeTMIpQYs6tDI2kGP")
                .clientKey("vDw3U7CejAV9Rhwuru3A78E3CpTB8bh20Hd11PRF")
                .server("https://parseapi.back4app.com")
                .build());
    }
}
