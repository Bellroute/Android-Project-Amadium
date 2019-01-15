package com.pubak.econovation.amadium.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class LoadImage extends AsyncTask<String, String, Bitmap> {
    private final String TAG = "LoadImage: ";
    private Bitmap image;
    private ImageView userImage;

    public LoadImage(ImageView userImage) {
        this.userImage = userImage;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG, "onPreExecute: ");
    }

    protected Bitmap doInBackground(String... args) {
        try {
            image = BitmapFactory
                    .decodeStream((InputStream) new URL(args[0])
                            .getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    protected void onPostExecute(Bitmap image) {
        if (image != null) {
            userImage.setImageBitmap(image);
            userImage.setBackground(new ShapeDrawable(new OvalShape()));
            userImage.setClipToOutline(true);
        } else {
        }
    }
}