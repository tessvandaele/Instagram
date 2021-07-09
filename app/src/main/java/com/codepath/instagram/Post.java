package com.codepath.instagram;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";

    public Post() {}

    //returns description text
    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    //sets description text
    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    //returns image file
    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    //sets image file
    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    //returns user
    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    //sets user
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }
}
