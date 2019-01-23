package com.pubak.econovation.amadium.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.pubak.econovation.amadium.activity.MainActivity;
import com.pubak.econovation.amadium.dto.UserDTO;
import com.pubak.econovation.amadium.utils.LoadImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private final String TAG = "ProfileFragment";
    private ImageView userImage;
    private TextView deleteImage;
    private TextView userName;
    private TextView userSports;
    private TextView selectSports;
    private UserDTO userDTO;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private Bitmap image;
    private String userUid;
    public static FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        user = MainActivity.getCurrentUser();
        userUid = user.getUid();
        userImage = (ImageView) view.findViewById(R.id.image_user_profile);
        userImage.bringToFront();
        deleteImage = (TextView) view.findViewById(R.id.textView_profile_delete);
        userName = (TextView) view.findViewById(R.id.textView_profile_name);
        userSports = (TextView) view.findViewById(R.id.textView_profile_sports);
        selectSports = (TextView) view.findViewById(R.id.textView_select_sports);

        firebaseDatabase = firebaseDatabase.getInstance();
        firebaseStorage = firebaseStorage.getInstance();

        firebaseDatabase.getReference().child("users").child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userDTO = dataSnapshot.getValue(UserDTO.class);
                Log.d(TAG, "onDataChange: " + userDTO.getUsername());

                userName.setText(userDTO.getUsername());
                userSports.setText(userDTO.getSport());

                if (userDTO.getProfileImageUrl() != null) {
                    new LoadImage(userImage).execute(userDTO.getProfileImageUrl());
                }
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


        setSportsList();
        return view;
    }

    private void setSportsList() {
        ListView listView = new ListView(getContext());
        List<String> sportsList = new ArrayList<>();
        sportsList.add("Tennis");
        sportsList.add("TableTennis");
        sportsList.add("Billiards");
        sportsList.add("Football");
        sportsList.add("BasketBall");
        sportsList.add("BaseBall");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, sportsList);
        listView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setView(listView);
        final AlertDialog dialog = builder.create();

        selectSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                firebaseDatabase.getReference().child("users").child(user.getUid()).child("sport").setValue(adapter.getItem(position));
                userSports.setText(adapter.getItem(position));
                dialog.dismiss();
            }
        });
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
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                Uri url = urlTask.getResult();
                firebaseDatabase.getReference().child("users").child(user.getUid()).child("profileImageUrl").setValue(String.valueOf(url));
            }
        });
    }
}
