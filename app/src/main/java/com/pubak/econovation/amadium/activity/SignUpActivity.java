package com.pubak.econovation.amadium.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pubak.econovation.amadium.R;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    // 비밀번호 정규식(최소 8자리에 숫자, 문자, 특수문자가 1개 이상 포함)
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[~!@#$%^*+=-])(?=.*[0-9]).{8,16}$");
    private FirebaseAuth firebaseAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private Button buttonJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.editText_email);
        editTextPassword = (EditText) findViewById(R.id.editText_passWord);
        editTextName = (EditText) findViewById(R.id.editText_name);

        buttonJoin = (Button) findViewById(R.id.btn_join);
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
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "이미 가입된 계정입니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
