package com.codepath.instagram.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.models.Post;
import com.codepath.instagram.adapters.PostsAdapter;
import com.codepath.instagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/* fragment for timeline of posts */

public class TimelineFragment extends Fragment {

    protected RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> posts;

    //empty constructor
    public TimelineFragment () {}

    //inflate the timeline fragment layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvPosts);
        posts = new ArrayList<>();

        //creating and setting adapter
        adapter = new PostsAdapter(getContext(), posts);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        queryPosts();
    }

    //helper method to retrieve the last 20 posts from backend
    protected void queryPosts() {
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
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
                adapter.notifyDataSetChanged();
            }
        });
    }
}