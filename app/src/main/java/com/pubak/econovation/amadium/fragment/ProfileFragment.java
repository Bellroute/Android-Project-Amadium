package com.pubak.econovation.amadium.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.adapter.ListViewAdapter;
import com.pubak.econovation.amadium.dto.UserDTO;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private final String TAG = "ProfileFragment";
    private ImageView userImage;
    private TextView deleteImage;
    private TextView userName;
    private TextView userTier;
    private UserDTO userDTO;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private Bitmap image;
    private String userUid;
    private FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        user = firebaseAuth.getInstance().getCurrentUser();
        userUid = user.getUid();
        userImage = (ImageView) view.findViewById(R.id.image_user_profile);
        deleteImage = (TextView) view.findViewById(R.id.textView_profile_delete);
        userName = (TextView) view.findViewById(R.id.textView_profile_name);
        userTier = (TextView) view.findViewById(R.id.textView_profile_tier);
        firebaseDatabase = firebaseDatabase.getInstance();
        firebaseStorage = firebaseStorage.getInstance();

        firebaseDatabase.getReference().child("users").child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userDTO = dataSnapshot.getValue(UserDTO.class);
                Log.d(TAG, "onDataChange: " + userDTO.getUsername());

                userName.setText(userDTO.getUsername());
                userTier.setText(userDTO.getEmail());

                if (userDTO.getProfileImageUrl() != null) {
                    new LoadImage().execute(userDTO.getProfileImageUrl());
                }

//                if (userDTO.getProfileImageUrl() != null) {
//                    Thread mThread = new Thread() {
//                        @Override
//                        public void run() {
//                            try {
//                                URL url = new URL(userDTO.getProfileImageUrl());
//                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                                conn.setDoInput(true);
//                                conn.connect();
//
//                                InputStream is = conn.getInputStream();
//                                image = BitmapFactory.decodeStream(is);
//                                Log.d(TAG, "run: thread");
//                            } catch (MalformedURLException ee) {
//                                ee.printStackTrace();
//                                Log.d(TAG, "MalformedURLException: ");
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                                Log.d(TAG, "IOException: ");
//                            }
//                        }
//                    };
//                    mThread.start();
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImage.setBackground(null);
                userImage.setImageResource(R.drawable.ic_user);
                firebaseDatabase.getReference().child("users").child(user.getUid()).child("profileImageUrl").setValue(null);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getActivity().getContentResolver().openInputStream(data.getData());
                    image = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    userImage.setImageBitmap(image);
                    userImage.setBackground(new ShapeDrawable(new OvalShape()));
                    userImage.setClipToOutline(true);
                    upload();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void upload() {
        StorageReference storageRef = firebaseStorage.getReferenceFromUrl("gs://amadium-632d8.appspot.com/profile_image").child(userUid);
        Log.d(TAG, "upload: " + storageRef);
        userImage.setDrawingCacheEnabled(true);
        userImage.buildDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Uri url = taskSnapshot.getUploadSessionUri();
                firebaseDatabase.getReference().child("users").child(user.getUid()).child("profileImageUrl").setValue(String.valueOf(url));
            }
        });
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
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
                userImage.setBackground(new ShapeDrawable(new OvalShape()));
                userImage.setClipToOutline(true);
                userImage.setImageBitmap(image);
            } else {
            }
        }
    }
}
