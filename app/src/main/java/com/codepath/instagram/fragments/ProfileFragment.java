package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.instagram.models.Post;
import com.codepath.instagram.adapters.ProfileAdapter;
import com.codepath.instagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/* fragment for the profile page where user can view profile image and grid layout of their posts */

//extends timeline fragment such that they can share user and post data
public class ProfileFragment extends TimelineFragment{

    private ProfileAdapter profileAdapter;

    private ImageView ivImageProfile;
    private TextView tvUsernameProfile;
    private RecyclerView rvProfile;

    //inflating the profile fragment layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ivImageProfile = view.findViewById(R.id.ivProfileImage);
        tvUsernameProfile = view.findViewById(R.id.tvUsernameProfile);

        //retrieving profile image for current user
        ParseFile image = ParseUser.getCurrentUser().getParseFile("profile_image");
        Glide.with(getContext())
                .load(image.getUrl())
                .circleCrop()
                .transform(new RoundedCornersTransformation(30, 0))
                .into(ivImageProfile);
        tvUsernameProfile.setText(ParseUser.getCurrentUser().getUsername());

        rvProfile = view.findViewById(R.id.rvProfile);
        posts = new ArrayList<>();

        //creating and setting adapter
        profileAdapter = new ProfileAdapter(getContext(), posts);
        rvProfile.setAdapter(profileAdapter);

        Log.d("Profile", "setting adapter");

        rvProfile.setLayoutManager(new GridLayoutManager(getContext(),3));
        queryPosts();
    }

    //queries posts for just this user
    @Override
    protected void queryPosts() {

        Log.d("Profile", "quarying posts");
        //posts.clear();
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        //fetch posts of user only
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> allPosts, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e("TimelineFragment", "Issue with getting posts", e);
                    return;
                }

                // save received posts to list and notify adapter of new data
                posts.addAll(allPosts);
                profileAdapter.notifyDataSetChanged();
            }
        });
    }
}
