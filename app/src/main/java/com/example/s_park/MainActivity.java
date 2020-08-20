package com.example.s_park;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//visibility로 필터링 재설정
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    public ArrayList<MarkerItem> sampleList = new ArrayList<>();
    public ArrayList<MarkerItem> stateList = new ArrayList<>();
    private GoogleMap mMap;
    Spinner filterSpinner;
    String [] filtering = {"ALL","GREEN","YELLOW","PINK","RED",};
    public TextView numOfpark;
    public TextView phoneOfpark;
    public TextView userOfpark;
    public ImageView refreshButton;
    public TextView refresh_txt;
    public TextView textView3;
    public String TAG = "MainActivity";
    String _protocol = "http://";
    String _host = "27.96.131.40";
    String _port = ":3300";
    String _path = "/jsontest";
    String _url = "" + _protocol + _host + _port + _path;
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

        numOfpark = findViewById(R.id.numOfpark);
        phoneOfpark = findViewById(R.id.phoneOfpark);
        userOfpark = findViewById(R.id.userOfpark);
        refreshButton = findViewById(R.id.refreshButton);
        refresh_txt = findViewById(R.id.refresh_txt);

        refreshButton.setOnClickListener(onClickListener);
        refresh_txt.setOnClickListener(onClickListener);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("잠시만 기다려주세요.");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);

        //필터 스피너의 어댑터 설정
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item , filtering);
        filterSpinner = findViewById(R.id.filterSpinner);
        InitializeLayout();
        filterSpinner.setAdapter(adapter);


        textView3 = findViewById(R.id.textView3);
        textView3.setOnClickListener(onClickListener);

        //필터 검색 커스텀
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(filtering[position].equals("ALL")){
                    //맵상태 초기화
                    mMap.clear();
                    //상세정보 초기화
                    numOfpark.setText(""); phoneOfpark.setText(""); userOfpark.setText("");
                    onMapReady(mMap);
                }else if(filtering[position].equals("GREEN")){
                    mMap.clear();
                    numOfpark.setText(""); phoneOfpark.setText(""); userOfpark.setText("");
                    filterGreen();
                }else if(filtering[position].equals("YELLOW")){
                    mMap.clear();
                    numOfpark.setText(""); phoneOfpark.setText(""); userOfpark.setText("");
                    filterYellow();
                }else if(filtering[position].equals("PINK")){
                    mMap.clear();
                    numOfpark.setText(""); phoneOfpark.setText(""); userOfpark.setText("");
                    filterPink();
                }else if(filtering[position].equals("RED")){
                    mMap.clear();
                    numOfpark.setText(""); phoneOfpark.setText(""); userOfpark.setText("");
                    filterRed();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //0~4 순서대로 그린, 옐로, 화이트, 핑크, 레드 상태임
        sampleList.add(new MarkerItem(37.52487, 126.98423, "GREEN", "123-456-789", "010-AAAA-AAAA", "박성진"));
        sampleList.add(new MarkerItem(37.53487, 126.92711, "YELLOW", "153-656-789", "010-BBBB-BBBB", "이승헌"));
        sampleList.add(new MarkerItem(37.55487, 126.96743, "RED", "853-146-589", "010-CCCC-CCCC", "한상일"));
        sampleList.add(new MarkerItem(37.51487, 126.92765, "PINK", "573-256-375", "010-DDDD-DDDD", "곽수인"));
        sampleList.add(new MarkerItem(37.51127, 126.94765, "RED", "846-584-221", "010-EEEE-EEEE", "김정현"));

        //정보를 받아옴
        request();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id. refresh_txt:
                    progressDialog.show();
                    numOfpark.setText(""); phoneOfpark.setText(""); userOfpark.setText("");
                    onMapReady(mMap);
                    progressDialog.dismiss();
                    break;
                case R.id. refreshButton:
                    progressDialog.show();
                    numOfpark.setText(""); phoneOfpark.setText(""); userOfpark.setText("");
                    onMapReady(mMap);
                    progressDialog.dismiss();
                    break;
            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // 구글 맵 객체를 불러온다.
        mMap = googleMap;
        googleMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
        for(final MarkerItem markerItem : sampleList){
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

        //서울에 대한 위치 설정
        LatLng seoul = new LatLng(37.52487, 126.92723);
        //카메라를 초기 위치 설정
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));

        // 최대, 최소 줌 설정
        mMap.setMinZoomPreference(13);
        mMap.setMaxZoomPreference(20);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                for(MarkerItem markerItem : sampleList)
                    //주차면 번호와 사용자 핸드폰 번호가 같다면
                    if(marker.getTitle().equals(markerItem.numOfpark)&&marker.getSnippet().equals(markerItem.phoneOfpark)){
                        numOfpark.setText(markerItem.numOfpark);
                        phoneOfpark.setText(markerItem.phoneOfpark);
                        userOfpark.setText(markerItem.userOfpark);
                    }
                return false;
            }
        });
    }

    public void filterGreen() {
        for(MarkerItem markerItem : sampleList){
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
        for(MarkerItem markerItem : sampleList){
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
        for(MarkerItem markerItem : sampleList){
            if(markerItem.state.equals("PINK")){
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
        for(MarkerItem markerItem : sampleList){
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

                    //json 객체가 제대로 수신되었는지 StateJson에 저장된 String을 log출력
                    Log.d("JSON TEST", "json => " + StateJson);

                    //json 객체가 제대로 수신되었는지 log출력
                    JSONObject jsonObject = new JSONObject(StateJson);
                    JSONArray resultsArray = jsonObject.getJSONArray("results");

                    Log.d("JSON TEST", "json => " + resultsArray);

                    // json에 담겨있는 Object들을 Index를 이용해서 출력
                    JSONObject jsonObject1 = resultsArray.getJSONObject(0);
                    Log.d("JSON TEST", "json => " + jsonObject1.toString());

                    // json에 담겨있는 Object들을 key값을 이용해서 출력
                    JSONObject dataObject = (JSONObject) jsonObject1.get("id");
                    Log.d("JSON TEST", "json => " + dataObject.toString());

                    for(int i=0; resultsArray.getJSONObject(i).equals(resultsArray.length()); i++){
                        JSONObject jsonObject_T = resultsArray.getJSONObject(i);
                        //sampleList.add(new MarkerItem(37.52487, 126.98423, "GREEN", "123-456-789", "010-AAAA-AAAA", "박성진"));
                        //{"lat": , "lon": , "state": , "numOfpark": , "phoneOfpark": , "userOfpark"}
                        stateList.add(new MarkerItem(
                                Double.parseDouble(jsonObject_T.get("lat").toString()),
                                Double.parseDouble(jsonObject_T.get("lon").toString()),
                                jsonObject_T.get("state").toString(),
                                jsonObject_T.get("numOfpark").toString(),
                                jsonObject_T.get("phoneOfpark").toString(),
                                jsonObject_T.get("userOfpark").toString()));
                    }


                } catch (Exception e) {
                    //error 발생시 error를 출력
                    e.printStackTrace();
                }
            }
        }).start();
    }
}