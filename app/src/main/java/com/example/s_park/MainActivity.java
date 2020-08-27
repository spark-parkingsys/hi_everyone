package com.example.s_park;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

//visibility로 필터링 재설정
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    public ArrayList<MarkerItem> stateList = new ArrayList<>();
    private GoogleMap mMap;
    Spinner filterSpinner;
    String [] filtering = {"ALL","GREEN","YELLOW","PINK","RED"};
    public TextView numOfpark;
    public TextView phoneOfpark;
    public TextView userOfpark;
    public ImageView refreshButton;
    public TextView refresh_txt;
    public TextView parkingStatus;
    public Button settingButton;
    String temp;
    String TAG = "MainActivity";
    String _protocol = "http://";
    String _host = "27.96.131.40";
    String _port = ":3400";
    String _modify_path = "/api/updateParkingLot";
    String _path = "/api/getParkingLots";
    String _url = "" + _protocol + _host + _port + _path;
    String _modify_url = "" + _protocol + _host + _port + _modify_path;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //mainthread 오류 해결 코드, 아마도 메인스레드와 분리시키는 코드인 듯
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); //getMapAsync must be called on the main thread.

        parkingStatus = findViewById(R.id.parkingStatus);
        numOfpark = findViewById(R.id.numOfpark);
        phoneOfpark = findViewById(R.id.phoneOfpark);
        userOfpark = findViewById(R.id.userOfpark);
        refreshButton = findViewById(R.id.refreshButton);
        refresh_txt = findViewById(R.id.refresh_txt);
        settingButton = findViewById(R.id.settingButton);

        settingButton.setOnClickListener(onClickListener);
        refreshButton.setOnClickListener(onClickListener);
        refresh_txt.setOnClickListener(onClickListener);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("잠시만 기다려주세요.");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);

        //필터 스피너의 어댑터 설정
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, filtering);
        filterSpinner = findViewById(R.id.filterSpinner);
        InitializeLayout();
        filterSpinner.setAdapter(adapter);

        //필터 검색 커스텀
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(filtering[position].equals("ALL")){
                    //맵상태 초기화
                    mMap.clear();
                    //상세정보 초기화
                    parkingStatus.setText("");numOfpark.setText(""); phoneOfpark.setText(""); userOfpark.setText("");
                    onMapReady(mMap);
                }else if(filtering[position].equals("GREEN")){
                    mMap.clear();
                    parkingStatus.setText("");numOfpark.setText(""); phoneOfpark.setText(""); userOfpark.setText("");
                    filterGreen();
                }else if(filtering[position].equals("YELLOW")){
                    mMap.clear();
                    parkingStatus.setText("");numOfpark.setText(""); phoneOfpark.setText(""); userOfpark.setText("");
                    filterYellow();
                }else if(filtering[position].equals("PINK")){
                    mMap.clear();
                    parkingStatus.setText("");numOfpark.setText(""); phoneOfpark.setText(""); userOfpark.setText("");
                    filterPink();
                }else if(filtering[position].equals("RED")){
                    mMap.clear();
                    parkingStatus.setText("");numOfpark.setText(""); phoneOfpark.setText(""); userOfpark.setText("");
                    filterRed();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //정보를 받아옴
        request();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent refresh_page = new Intent(MainActivity.this, MainActivity.class);
            switch(v.getId()){
                case R.id. refresh_txt:
                    finish();
                    startActivity(refresh_page);
                    break;
                case R.id. settingButton:
                    temp = numOfpark.getText().toString();
                    Intent modify = new Intent(MainActivity.this, ModifyActivity.class);
                    startActivityForResult(modify, 1);
                    break;
                case R.id. refreshButton:
                    finish();
                    startActivity(refresh_page);
                    break;
            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // 구글 맵 객체를 불러온다.
        mMap = googleMap;
        googleMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
        for(final MarkerItem markerItem : stateList){
            LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());

            MarkerOptions markerOptions = new MarkerOptions();
            BitmapDrawable bitmapdraw; Bitmap b; Bitmap smallMarker;

            switch(markerItem.state){
                case "GREEN":
                    bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.green_state);
                    b = bitmapdraw.getBitmap();
                    smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    break;
                case "YELLOW":
                    bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.yellow_state);
                    b = bitmapdraw.getBitmap();
                    smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    break;
                case "PINK":
                    bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.pink_state);
                    b = bitmapdraw.getBitmap();
                    smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    break;
                case "RED":
                    bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.red_state);
                    b = bitmapdraw.getBitmap();
                    smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    break;
            }
            //마커에 정보 표시
            markerOptions.position(position).title(markerItem.numOfpark);
            markerOptions.position(position).snippet(markerItem.phoneOfpark);
            //마커 생성
            mMap.addMarker(markerOptions);
            //마커 클릭 이벤트 설정
        }

        //카메라를 초기 위치 설정
        LatLng seoul = new LatLng(37.59293382, 127.0892611);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));

        // 최대, 최소 줌 설정
        mMap.setMinZoomPreference(14);
        mMap.setMaxZoomPreference(20);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for(MarkerItem markerItem : stateList)
                    //주차면 번호와 사용자 핸드폰 번호가 같다면
                    if(marker.getTitle().equals(markerItem.numOfpark) && marker.getSnippet().equals(markerItem.phoneOfpark)){
                        parkingStatus.setText(markerItem.state);
                        numOfpark.setText(markerItem.numOfpark);
                        phoneOfpark.setText(markerItem.phoneOfpark);
                        userOfpark.setText(markerItem.userOfpark);
                    }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                String result = data.getStringExtra("result");
                modify(temp, result);
            }
        }
    }

    public void filterGreen() {
        for(MarkerItem markerItem : stateList){
            if(markerItem.state.equals("GREEN")){
                LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());

                MarkerOptions markerOptions = new MarkerOptions();
                BitmapDrawable bitmapdraw; Bitmap b; Bitmap smallMarker;

                bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.green_state);
                b = bitmapdraw.getBitmap();
                smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                markerOptions.position(position).title(markerItem.numOfpark);
                markerOptions.position(position).snippet(markerItem.phoneOfpark);
                mMap.addMarker(markerOptions);
            }
        }
    }
    public void filterYellow() {
        for(MarkerItem markerItem : stateList){
            if(markerItem.state.equals("YELLOW")){
                LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());

                MarkerOptions markerOptions = new MarkerOptions();
                BitmapDrawable bitmapdraw; Bitmap b; Bitmap smallMarker;

                bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.yellow_state);
                b = bitmapdraw.getBitmap();
                smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                markerOptions.position(position).title(markerItem.numOfpark);
                markerOptions.position(position).snippet(markerItem.phoneOfpark);
                mMap.addMarker(markerOptions);
            }
        }
    }
    public void filterPink() {
        for(MarkerItem markerItem : stateList){
            if(markerItem.state.equals("GREEN")){
                LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());
                MarkerOptions markerOptions = new MarkerOptions();
                BitmapDrawable bitmapdraw; Bitmap b; Bitmap smallMarker;
                bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.pink_state);
                b = bitmapdraw.getBitmap();
                smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                markerOptions.position(position).title(markerItem.numOfpark);
                markerOptions.position(position).snippet(markerItem.phoneOfpark);
                mMap.addMarker(markerOptions);
            }
        }
    }
    public void filterRed() {
        for(MarkerItem markerItem : stateList){
            if(markerItem.state.equals("RED")){
                LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());

                MarkerOptions markerOptions = new MarkerOptions();
                BitmapDrawable bitmapdraw; Bitmap b; Bitmap smallMarker;
                bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.red_state);
                b = bitmapdraw.getBitmap();
                smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                markerOptions.position(position).title(markerItem.numOfpark);
                markerOptions.position(position).snippet(markerItem.phoneOfpark);
                mMap.addMarker(markerOptions);
            }
        }
    }
    public void InitializeLayout(){
        //toolBar를 통해 App Bar 생성
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //App Bar의 좌측 영영에 Drawer를 Open 하기 위한 Incon 추가

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_list);

        DrawerLayout drawLayout = (DrawerLayout) findViewById(R.id.drawer_layoutGov);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewGov);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawLayout, toolbar, R.string.open, R.string.close);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.menuitem_1:
                        Toast.makeText(getApplicationContext(), "SelectedItem 1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuitem_2:
                        Toast.makeText(getApplicationContext(), "SelectedItem 2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuitem_3:
                        Toast.makeText(getApplicationContext(), "SelectedItem 3", Toast.LENGTH_SHORT).show();
                        break;
                }
                DrawerLayout drawer = findViewById(R.id.drawer_layoutGov);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        drawLayout.addDrawerListener(actionBarDrawerToggle);
    }

    //Drawer 뒤로가기
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layoutGov);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void request() {
        // request 요청시 비동기 작업을 위해서 Thread 이용해서 통신 실행
        new Thread(new Runnable() {
            // json 으로 전달할 인자들
            // 현재는 지정 문자를 정해두고 보내는 예시
            // json 객체
            String StateJson = null;

            @Override
            public void run() {
                try {
                    Log.d(TAG, "Json 전달 스레드 실행");
                    URL url = new URL(_url);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    // Restful API, Get 방식으로 전달
                    // GET / POST / DELETE / PUT ..
                    String methodType = "GET";
                    con.setRequestMethod(methodType);

                    // request 코드가 200이면, 응답이 정상적으로 호출됨을 의미
                    int responseCode = con.getResponseCode();
                    Log.d(TAG, "response code:" + responseCode);

                    // 입력값을 수용하는 버퍼
                    BufferedReader br = null;
                    if (responseCode == 200) { // 정상 호출
                        Log.d(TAG, "정상호출");
                        //정상적으로 호출이 되면, 스트림으로부터 버퍼로 데이터를 읽어온다.
                        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    } else {
                        // 에러 발생 ( 응답 코드가 200이 아닐 경우 )
                        Log.d(TAG, "비정상호출");
                    }
                    // json파일을 출력
                    String inputLine;
                    StringBuffer response = new StringBuffer();
                    // 버퍼에 입력된 값들을 라인 단위로 읽어들임
                    while ((inputLine = br.readLine()) != null) {
                        response.append(inputLine);
                    }
                    br.close();
                    // 응답값을 String 형태로 json에 저장
                    StateJson = response.toString();
                    // json의 내용이 만약 null값이면 return시킴
                    if (StateJson == null) {
                        return;
                    }
                    Log.d("JSON TEST", "json => " + StateJson);
                    JsonParser parser = new JsonParser();
                    Object obj = parser.parse(StateJson);
                    JsonObject jsonObj = (JsonObject) obj;
                    JsonArray jsonArray = (JsonArray) jsonObj.get("content");


                    //세부속성
                    //JsonObject jsonObject = (JsonObject) obj;
                    //JsonPrimitive addrName = (JsonPrimitive) jsonObject.get("addrName");

                    //json 객체가 제대로 수신되었는지 StateJson에 저장된 String을 log출력.
                    Log.d("JSON TEST", "json => " + StateJson);
                    for(int i=0; i < jsonArray.size(); i++){
                        JsonObject jsonObject = (JsonObject) jsonArray.get(i);

                        JsonObject jsonObject_latlng = (JsonObject)jsonObject.get("latlng");
                        JsonPrimitive lat= (JsonPrimitive)jsonObject_latlng.get("lat");
                        JsonPrimitive lng= (JsonPrimitive)jsonObject_latlng.get("lng");
                        JsonPrimitive pID = (JsonPrimitive) jsonObject.get("pID");
                        JsonPrimitive placeName = (JsonPrimitive) jsonObject.get("placeName");
                        JsonPrimitive hasSensor = (JsonPrimitive) jsonObject.get("hasSensor");
                        JsonPrimitive status = (JsonPrimitive) jsonObject.get("status");
                        JsonPrimitive addrName = (JsonPrimitive) jsonObject.get("addrName");
                        stateList.add(new MarkerItem(
                                Double.parseDouble(lat.toString()),
                                Double.parseDouble(lng.toString()),
                                status.toString().replace("\"",""),
                                pID.toString().replace("\"",""),
                                "010~",
                                null));
                    }


                } catch (Exception e) {
                    //error 발생시 error를 출력
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void modify(final String pID, final String state) {
        new Thread(new Runnable() {
            //JSON형식으로 데이터 통신을 진행합니다!
            JSONObject ModifyJson = new JSONObject();

            @Override
            public void run() {

                try {
                    ModifyJson.put("pID", pID);
                    ModifyJson.put("status", state);
                    String jsonString = ModifyJson.toString(); //완성된 json 포맷
                    Log.d("json 내용 확인", jsonString);

                    final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, _modify_url, ModifyJson, new Response.Listener<JSONObject>() {
                        //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d("데이터", "데이터 수정 성공");

                                URL url = new URL(_url);
                                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                                con.setRequestMethod("GET");
                                con.connect();
                                int responseCode = con.getResponseCode();

                                Log.d(TAG, "response code:" + responseCode);
                                if(responseCode == 200){
                                    Intent refresh_page = new Intent(MainActivity.this, MainActivity.class);
                                    Toast.makeText(MainActivity.this, "성공적으로 수정되었습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(refresh_page);
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
                                e.printStackTrace();
                                Toast.makeText(MainActivity.this, "수정중 오류가 발생하였습니다.", Toast.LENGTH_SHORT).show();
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