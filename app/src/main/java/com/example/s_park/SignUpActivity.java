package com.example.s_park;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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


    public void testJson(){
        //jsons 파일 읽어오기
        AssetManager assetManager = getAssets();

        try{
            InputStream is = assetManager.open("jsons/signup.json");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while(line!=null){
                buffer.append(line+"\n");
                line = reader.readLine();
            }
            String jsonData = buffer.toString();

            //Jsons 데이터 분석
            JSONObject jsonObject = new JSONObject(jsonData);
            String userId = jsonObject.getString("userId");
            String passwords = jsonObject.getString("passwords");

        }catch (IOException e) {e.printStackTrace();}
        catch (JSONException e){e.printStackTrace();}
    }
}
