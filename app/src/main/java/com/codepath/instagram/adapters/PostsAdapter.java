package com.codepath.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.instagram.DetailsActivity;
import com.codepath.instagram.models.Post;
import com.codepath.instagram.R;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

/* adapter for the timeline recycler view of posts */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    //constructor
    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    //inflating the item_post layout as the view for the view holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    //finding correct post and calling the bind method to add it to the view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

        //views in item_post
        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;
        private TextView tvTimeStamp;
        private TextView tvUsername2;
        private ImageView ivProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUser);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            tvUsername2 = itemView.findViewById(R.id.tvUsername2);
            ivProfile = itemView.findViewById(R.id.ivProfileTimeline);
            itemView.setOnClickListener(this);
        }

        public void bind(Post post) {
            // Bind the post data to the view elements
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            tvUsername2.setText(post.getUser().getUsername());
            tvTimeStamp.setText(calculateTimeAgo(post.getCreatedAt()));

            // Retrieving images from the post and user models
            ParseFile image = post.getImage();
            ParseFile profile = post.getUser().getParseFile("profile_image");

            //setting post image if not null
            if (image != null) {
                Glide.with(context)
                        .load(image.getUrl())
                        .centerCrop()
                        .into(ivImage);
            }

            //setting profile image if not null
            if (profile != null) {
                Glide.with(context).load(profile.getUrl()).into(ivProfile);
            }
        }

        //on click method for when a view holder is clicked; creates an intent to the details activity
        //passes through the post using Parcels wrap
        @Override
        public void onClick(View v) {
            Log.d("click", "click");
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

    //helper method to create timestamp string
    public static String calculateTimeAgo(Date createdAt) {

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }

        return "";
    }
}
