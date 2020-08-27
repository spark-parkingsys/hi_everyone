package com.example.s_park;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;


public class ModifyActivity extends Activity {
    Spinner modify_spinner;
    String [] filtering = {"GREEN","YELLOW","PINK","RED"};
    Button yes_button;
    Button no_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_modify);

        yes_button = findViewById(R.id.yes_button);
        no_button = findViewById(R.id.no_button);

        yes_button.setOnClickListener(onClickListener);
        no_button.setOnClickListener(onClickListener);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_text , filtering);
        modify_spinner = findViewById(R.id.modify_spinner);
        modify_spinner.setAdapter(adapter);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent modify = new Intent();
            switch (v.getId()){
                case R.id.yes_button:
                    modify.putExtra("result", modify_spinner.getSelectedItem().toString());
                    setResult(RESULT_OK, modify);
                    finish();
                    break;
                case R.id.no_button:
                    finish();
                    break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                String result = data.getStringExtra("result2");
                finish();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
