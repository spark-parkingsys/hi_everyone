package com.example.s_park;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;



public class LoginActivity extends AppCompatActivity {
    EditText login_id;
    EditText login_password;
    Button login_button;
    Button signup_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_id = findViewById(R.id.login_id);
        login_password = findViewById(R.id.login_password);
        login_button = findViewById(R.id.login_button);
        signup_button = findViewById(R.id.signup_button);

        login_button.setOnClickListener(onClickListener);
        signup_button.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.signup_button:
                    Intent gotoSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(gotoSignUp);
                    break;
                case R.id.login_button:
                    Intent gotoMain = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(gotoMain);

                    break;
            }
        }
    };



}
