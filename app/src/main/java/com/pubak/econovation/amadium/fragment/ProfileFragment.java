package com.pubak.econovation.amadium.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.activity.MainActivity;

import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private ImageView userImage;
    private TextView deleteImage;
    private TextView userName;
    private TextView userTier;
    private FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        userImage = (ImageView) view.findViewById(R.id.image_user_profile);
        deleteImage = (TextView) view.findViewById(R.id.textView_profile_delete);
        userName = (TextView) view.findViewById(R.id.textView_profile_name);
        userTier = (TextView) view.findViewById(R.id.textView_profile_tier);


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
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    userImage.setImageBitmap(img);
                    userImage.setBackground(new ShapeDrawable(new OvalShape()));
                    userImage.setClipToOutline(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
