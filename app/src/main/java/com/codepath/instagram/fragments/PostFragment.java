package com.codepath.instagram.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.instagram.models.Post;
import com.codepath.instagram.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/* fragment for activity in which a user can create a post */

public class PostFragment extends Fragment {

    //view elements
    private EditText etDescription;
    private ImageButton btnTakePicture;
    private ImageView ivPostImage;
    private ImageButton btnPost;

    private File photoFile;
    public String photoFileName = "photo.jpg";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;

    //empty constructor
    public PostFragment() {}

    //set up to view object hierarchy
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //set up views
        etDescription = view.findViewById(R.id.etDescription);
        btnTakePicture = view.findViewById(R.id.btnTakePicture);
        ivPostImage = view.findViewById(R.id.ivPostImage);
        btnPost = view.findViewById(R.id.btnPost);

        //on click for submit button to publish instagram post
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                //check that there is a description
                if(description.isEmpty()) {
                    Toast.makeText(getContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                //check that there is an image
                if(photoFile == null || ivPostImage.getDrawable() == null) {
                    Toast.makeText(getContext(), "No image available", Toast.LENGTH_SHORT).show();
                    return;
                }
                //pass data to savePost method
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description, currentUser, photoFile);
            }
        });

        //setting up take picture button
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //calling helper method to launch the camera application
                launchCamera();
            }
        });
    }

    //helper method to save data to a post and then commit to backend
    private void savePost(String description, ParseUser currentUser, File photoFile) {
        //setting data to post object
        Post post = new Post();
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);

        //saving to backend
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null) {
                    Log.e("MainActivity", "Error saving post", e);
                    Toast.makeText(getContext(), "Error saving", Toast.LENGTH_SHORT).show();
                    return;
                }
                //clearing input fields for better UI experience
                etDescription.setText("");
                ivPostImage.setImageResource(0);
            }
        });
    }

    //creates implicit intent to camera
    private void launchCamera() {
        //creating an implicit intent for action_image_capture
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        //defining where we want the output image to be stored (in photoFile)
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        //checking for an application that can handle the intent
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    //retrieves the image from the camera api
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //check that code is correct for camera use
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            //check that a picture was taken
            if (resultCode == RESULT_OK) {
                // set bitmap with image
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                //set image view with image from camera
                ivPostImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //helper method to return the file based on file name
    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "MainActivity");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("MainActivity", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }
}