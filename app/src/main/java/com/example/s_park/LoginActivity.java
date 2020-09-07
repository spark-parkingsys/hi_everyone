package com.example.s_park;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    EditText login_id;
    EditText login_password;
    Button login_button;
    Button signup_button;
    String TAG = "LoginActivity";
    String _protocol = "http://";
    String _host = "27.96.131.40";
    String _port = ":3400";
    String _path = "/api/login";
    String _url = "" + _protocol + _host + _port + _path;
    public int loginCode = 999;

    //전역변수 설정;
    public MyApplication Success = (MyApplication) getApplication();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //전역변수 초기화
        Success = (MyApplication) getApplication();
        Success.setLoginSuccess(false);
        Success.setGlobalString("");
        Success.setGlobalString("");
        try{
            if(Success.getLoginSuccess()){
                Intent auth_verfication = new Intent(LoginActivity.this, MainActivity.class);
                finish();
                startActivity(auth_verfication);
            }
        } catch (NullPointerException e){
                Log.e(TAG, e.toString());
            }


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
            switch (v.getId()) {
                case R.id.signup_button:
                    Intent gotoSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(gotoSignUp);
                    break;
                case R.id.login_button:
                    Intent gotoMain = new Intent(LoginActivity.this, MainActivity.class);
                    Login();
                    startActivity(gotoMain);
                    break;
            }
        }
    };

    private int Login() {
        new Thread(new Runnable() {
            JSONObject LoginJson = new JSONObject();
            @Override
            public void run() {
                try {
                    LoginJson.put("id", login_id.getText());
                    LoginJson.put("password", login_password.getText());

                    String jsonString = LoginJson.toString(); //완성된 json 포맷
                    Log.d("json 내용 확인", jsonString);

                    final RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                    final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, _url, LoginJson, new Response.Listener<JSONObject>() {
                        //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e("데이터", "데이터 전송 성공");

                                URL url = new URL(_url);
                                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                                con.setRequestMethod("GET");
                                con.connect();
                                int responseCode = con.getResponseCode();

                                Log.d("로그인 응답코드", "response code:" + responseCode);
                                if(responseCode ==  200){
                                    Toast.makeText(LoginActivity.this, "로그인 성공하였습니다", Toast.LENGTH_SHORT).show();
                                    loginCode = 0;
                                    Success.setLoginSuccess(true);
                                    Success.setGlobalString(login_id.getText().toString());
                                    Success.setGlobalString(login_password.getText().toString());
                                }else{
                                    loginCode = 1;
                                    Success.setLoginSuccess(false);
                                    Success.setGlobalString("failed to log-in/ID");
                                    Success.setGlobalString("failed to log-in/PW");
                                }

                            } catch (Exception e) {
                                Log.d("로그인 응답코드", "response code:" + "실패!");
                                e.printStackTrace();
                            }
                        }
                        //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("위치확인4", "여기로 나옴");
                            error.printStackTrace();
                        }
                    });
                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(jsonObjectRequest);
                    //
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return loginCode;
    }
}