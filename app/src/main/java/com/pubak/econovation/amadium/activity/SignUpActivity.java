package com.pubak.econovation.amadium.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.dto.UserDTO;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    // 비밀번호 정규식(최소 8자리에 숫자, 문자, 특수문자가 1개 이상 포함)
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[~!@#$%^*+=-])(?=.*[0-9]).{8,16}$");
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextCheckPassword;
    private TextView buttonJoin;
    private ImageView userImage;
    private Uri url;
    private Bitmap img;
    private String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        tedPermission();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();

        editTextEmail = (EditText) findViewById(R.id.editText_email);
        editTextPassword = (EditText) findViewById(R.id.editText_passWord);
        editTextName = (EditText) findViewById(R.id.editText_name);
        editTextCheckPassword = (EditText) findViewById(R.id.editText_check_passWord);

        editTextCheckPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = editTextPassword.getText().toString();
                String confirm = editTextCheckPassword.getText().toString();

                if (password.equals(confirm)) {
                    editTextPassword.setBackgroundColor(Color.GREEN);
                    editTextCheckPassword.setBackgroundColor(Color.GREEN);
                } else {
                    editTextPassword.setBackgroundColor(Color.RED);
                    editTextCheckPassword.setBackgroundColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        userImage = (ImageView) findViewById(R.id.image_user_photo);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        buttonJoin = (TextView) findViewById(R.id.btn_join);
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidEmail() && isValidPassword()) {
                    // 이메일과 비밀번호를 잘 입력한 경우
                    createUser(editTextEmail.getText().toString(), editTextPassword.getText().toString(), editTextName.getText().toString());
                }
            }
        });
    }

    private void tedPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setRationaleMessage(getResources().getString(R.string.permission_2))
                .setDeniedMessage(getResources().getString(R.string.permission_1))
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    // 이메일 유효성검사 메소드
    private boolean isValidEmail() {
        if (editTextEmail.getText().toString().isEmpty()) {
            // 이메일이 공백인 경우
            Toast.makeText(SignUpActivity.this, "계정을 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString()).matches()) {
            // 잘못된 메일 형식인 경우
            Toast.makeText(SignUpActivity.this, "계정 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
        } else
            return true;
    }

    // 비밀번호 유효성검사 메소드
    private boolean isValidPassword() {
        if (editTextPassword.getText().toString().isEmpty()) {
            // 비밀번호가 공백인 경우
            Toast.makeText(SignUpActivity.this, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(editTextPassword.getText().toString()).matches()) {
            // 잘못된 비밀번호 형식인 경우
            Toast.makeText(SignUpActivity.this, "최소 8자리인 문자, 숫자, 특수문자가 포함된 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
            return false;
        } else
            return true;
    }

    private void createUser(String email, String password, String name) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 회원가입 성공시
                            Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            upload();
                            Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "이미 가입된 계정입니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    img = BitmapFactory.decodeStream(in);
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

    private void upload() {
        user = firebaseAuth.getCurrentUser();
        final StorageReference storageRef = firebaseStorage.getReferenceFromUrl("gs://amadium-632d8.appspot.com/profile_image").child(user.getUid());

        userImage.setDrawingCacheEnabled(true);
        userImage.buildDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.JPEG, 100, baos);
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
                url = urlTask.getResult();
                uploadUserInfo(url);
                Log.d(TAG, "onSuccess: " + url);
            }
        });
    }

    private void uploadUserInfo(Uri imagePath) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(editTextName.getText().toString());
        userDTO.setEmail(editTextEmail.getText().toString());
        userDTO.setTier("0");
        userDTO.setSport("스포츠 선택 필요");
        userDTO.setWinTieLose("0/0/0");
        userDTO.setLatitude(37.541);
        userDTO.setLongitude(126.986);

        if (imagePath != null) {
            userDTO.setProfileImageUrl(String.valueOf(imagePath));
        } else {
            userDTO.setProfileImageUrl("");
        }

        firebaseDatabase.getReference().child("users").child(user.getUid()).setValue(userDTO);
    }
}
