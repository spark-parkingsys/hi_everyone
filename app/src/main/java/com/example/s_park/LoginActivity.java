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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        login_id = findViewById(R.id.login_id);
        login_password = findViewById(R.id.login_password);
        login_button = findViewById(R.id.login_button);
        signup_button = findViewById(R.id.signup_button);

        login_button.setOnClickListener(onClickListener);
        signup_button.setOnClickListener(onClickListener);


    }
    /*private class MyTask extends AsyncTask<Void , Void , Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
        }
    }*/

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.signup_button:
                    Intent gotoSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                    startActivity(gotoSignUp);
                    break;
                case R.id.login_button:
                    Login();
                    Intent gotoMain = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(gotoMain);
                    //new MyTask().execute();
                    break;
            }
        }
    };

    private void Login() {
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
                                    Toast.makeText(LoginActivity.this, "로그인 성공했다", Toast.LENGTH_SHORT).show();
                                }


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
                                Log.d("로그인 응답코드", "response code:" + "실패!!!!!!!!!!!!!!!!!!!!!!!");
                                e.printStackTrace();
                            }
                        }
                        //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("위치확인4", "여기로 나옴");
                            error.printStackTrace();
                            //Toast.makeText(TestActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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
//
//    private void request() {
//        // request 요청시 비동기 작업을 위해서 Thread 이용해서 통신 실행
//        new Thread(new Runnable() {
//            // json 으로 전달할 인자들
//            // 현재는 지정 문자를 정해두고 보내는 예시
//
//            // json 객체
//            String json = null;
//
//            @Override
//            public void run() {
//                try {
//                    Log.d(TAG, "Json 전달 스레드 실행");
//
//                    // 서버 URL, 만약에 로컬에서 테스트 하고 싶다면
//                    // cmd창에서 ipconfig이용해서 로컬 주소 적으면 됨
//                    // 안드로이드는 기본적으로 https 프로토콜만을 허용하기 때문에
//                    // https를 사용하기 위해서는
//                    // manifests의 <application>영역에
//                    // android:usesCleartextTraffic="true"
//                    // 추가해야함
//
//                    URL url = new URL(_url);
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//
//                    // Restful API, Get 방식으로 전달
//                    // GET / POST / DELETE / PUT ..
//                    String methodType = "GET";
//                    con.setRequestMethod(methodType);
//
//                    // request 코드가 200이면, 응답이 정상적으로 호출됨을 의미
//                    int responseCode = con.getResponseCode();
//                    Log.d(TAG, "response code:" + responseCode);
//
//                    // 입력값을 수용하는 버퍼
//                    BufferedReader br = null;
//                    if (responseCode == 200) { // 정상 호출
//                        Log.d(TAG, "정상호출");
//                        //정상적으로 호출이 되면, 스트림으로부터 버퍼로 데이터를 읽어온다.
//                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//
//                    } else {
//                        // 에러 발생 ( 응답 코드가 200이 아닐 경우 )
//                        Log.d(TAG, "비정상호출");
//                    }
//
//                    // json파일을 출력
//                    String inputLine;
//                    StringBuffer response = new StringBuffer();
//                    // 버퍼에 입력된 값들을 라인 단위로 읽어들임
//                    while ((inputLine = br.readLine()) != null) {
//                        response.append(inputLine);
//                    }
//                    br.close();
//                    // 응답값을 String 형태로 json에 저장
//                    json = response.toString();
//                    // json의 내용이 만약 null값이면 return시킴
//                    if (json == null) {
//                        return;
//                    }
//
//                    //json 객체가 제대로 수신되었는지 json에 저장된 String을 log출력
//                    Log.d("JSON TEST", "json => " + json);
//
//                    //json 객체가 제대로 수신되었는지 log출력
//                    JSONObject jsonObject = new JSONObject(json);
//                    JSONArray resultsArray = jsonObject.getJSONArray("results");
//                    Log.d("JSON TEST", "json => " + resultsArray);
//
//                    // json에 담겨있는 Object들을 Index를 이용해서 출력
//                    JSONObject jsonObject1 = resultsArray.getJSONObject(0);
//                    Log.d("JSON TEST", "json => " + jsonObject1.toString());
//
//                    // json에 담겨있는 Object들을 key값을 이용해서 출력
//                    JSONObject dataObject = (JSONObject) jsonObject1.get("id");
//                    Log.d("JSON TEST", "json => " + dataObject.toString());
//
//                } catch (Exception e) {
//                    //error 발생시 error를 출력
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
}