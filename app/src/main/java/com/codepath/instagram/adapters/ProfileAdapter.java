package com.codepath.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.instagram.DetailsActivity;
import com.codepath.instagram.models.Post;
import com.codepath.instagram.R;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

/* adapter for recycler view on profile page; creates a grid view of user posts */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{

    private Context context;
    private List<Post> posts;

    //constructor
    public ProfileAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    //inflate the item_image layout to the view holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    //retrieves the correct post, then calls the bind method to bind the post data to the view holder
    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

       //only view in item_image is the post image
        private ImageView ivImageProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImageProfile = itemView.findViewById(R.id.ivImageProfile);
            itemView.setOnClickListener(this);
        }

        public void bind(Post post) {
            // Bind the post image to the view element if not null
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .centerCrop()
                        .into(ivImageProfile);
            }
        }

        //on click method for view holder; creates an intent to the details activity of that post
        @Override
        public void onClick(View v) {
            //get item position
            int position = getAdapterPosition();

            //check that position is valid
            if (position != RecyclerView.NO_POSITION) {
                //retrieve movie at position
                Post post = posts.get(position);
                //create intent for new activity
                Intent intent = new Intent(context, DetailsActivity.class);
                //serialize the movie
                intent.putExtra("post", Parcels.wrap(post));
                //start the activity
                context.startActivity(intent);
            }

        }
    }
}
