package com.example.s_park;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class SignUpActivity extends AppCompatActivity {
    EditText signUp_id;
    EditText signUp_password;
    EditText signUp_name;
    EditText signUp_Email;
    EditText signUp_mail_check;
    EditText signUp_phone;
    EditText signUp_phone_check;
    ImageView signUp_mail_check_button;
    ImageView signUp_confirm;
    ImageView signUp_overlap;
    ImageView signUp_phone_check_button;
    String TAG = "SignUpActivity";
    String _protocol = "http://";
    String _host = "27.96.131.40";
    String _port = ":3300";
    String _path = "/jsontest";
    String _url = "" + _protocol + _host + _port + _path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //mainthread 오류 해결 코드, 아마도 메인스레드와 분리시키는 코드인 듯
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        signUp_id = findViewById(R.id.signUp_id);
        signUp_password = findViewById(R.id.signUp_password);
        signUp_name = findViewById(R.id.signUp_name);
        signUp_Email = findViewById(R.id.signUp_Email);
        signUp_mail_check = findViewById(R.id.signUp_mail_check);
        signUp_phone = findViewById(R.id.signUp_phone);
        signUp_mail_check_button = findViewById(R.id.signUp_mail_check_button);
        signUp_confirm = findViewById(R.id.signUp_confirm);
        signUp_overlap = findViewById(R.id.signUp_overlap);
        signUp_phone_check = findViewById(R.id.signUp_phone_check);
        signUp_phone_check_button = findViewById(R.id.signUp_phone_check_button);

        signUp_overlap.setOnClickListener(onClickListener);
        signUp_mail_check_button.setOnClickListener(onClickListener);
        signUp_phone_check_button.setOnClickListener(onClickListener);
        signUp_confirm.setOnClickListener(onClickListener);

    }

    //아이디 중복확인
    public boolean confirmID(String email){
        return false;
    }

    //메일확인
    public boolean confrimEmail(String email){
        return false;
    }
    public boolean confrimPhonel(String phone){
        return false;
    }

    //회원가입
    public void confirmSignUp(){
        String id = signUp_id.toString();
        String password = signUp_password.toString();
        String name = signUp_name.toString();
        String phone = signUp_phone.toString();
        if(!confirmID(id)) {
            Toast.makeText(this, "중복된 아이디입니다.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "사용가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
        }
        if(password.length() < 6 || password.length() > 12){
            Toast.makeText(this, "비밀번호 조건을 충족하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.signUp_overlap:
                    break;
                case R.id.signUp_mail_check:
                    break;
                case R.id. signUp_phone_check_button:
                    break;
                case R.id. signUp_confirm:
                    request1();
                    break;
            }
        }
    };

    private void request1() {
        new Thread(new Runnable() {
            //JSON형식으로 데이터 통신을 진행합니다!
            JSONObject SignUpJson = new JSONObject();

            @Override
            public void run() {
                try {
                    //회원가입 내용을 json에 put함
                    SignUpJson.put("id", signUp_id);
                    SignUpJson.put("pw", signUp_password);
                    SignUpJson.put("name", signUp_name);
                    SignUpJson.put("email", signUp_Email);
                    SignUpJson.put("email_code", signUp_mail_check);
                    SignUpJson.put("phone", signUp_phone);
                    SignUpJson.put("phone_code", signUp_phone_check);

                    String jsonString = SignUpJson.toString(); //완성된 json 포맷
                    Log.d("json 내용 확인", jsonString);

                    final RequestQueue requestQueue = Volley.newRequestQueue(SignUpActivity.this);
                    final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, _url, SignUpJson, new Response.Listener<JSONObject>() {
                        //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d("데이터", "데이터 전송 성공");

                                URL url = new URL(_url);
                                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                                con.setRequestMethod("GET");
                                con.connect();
                                Log.d(TAG, "response code:" + "http connection");
                                int responseCode = con.getResponseCode();

                                Log.d(TAG, "response code:" + responseCode);

                                /*//받은 json형식의 응답을 받아
                                JSONObject jsonObject = new JSONObject(response.toString());

                                //key값에 따라 value값을 쪼개 받아옵니다.
                                String resultId = jsonObject.getString("approve_id");
                                String resultPassword = jsonObject.getString("approve_pw");

                                //만약 그 값이 같다면 로그인에 성공한 것입니다.
                                if (resultId.equals("OK") & resultPassword.equals("OK")) {
                                    //이 곳에 성공 시 화면이동을 하는 등의 코드를 입력하시면 됩니다.
                                } else {
                                    //로그인에 실패했을 경우 실행할 코드를 입력하시면 됩니다.
                                }*/
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
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
    }
}
