package com.example.s_park;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MonitorActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); //getMapAsync must be called on the main thread.

        InitializeLayout();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        // 구글 맵 객체를 불러온다.
        mMap = googleMap;

        ArrayList<MarkerItem> sampleList = new ArrayList();
        sampleList.add(new MarkerItem(37.52487, 126.98423, false, "123-456-789"));
        sampleList.add(new MarkerItem(37.53487, 126.92711, true, "153-656-789"));
        sampleList.add(new MarkerItem(37.55487, 126.96743, true, "853-146-589"));
        sampleList.add(new MarkerItem(37.51487, 126.92765, true, "573-256-375"));

        for(MarkerItem markerItem : sampleList){
            LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());

            if(markerItem.state){
                MarkerOptions markerOptions = new MarkerOptions();
                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.green_state);
                Bitmap b=bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);

                markerOptions.position(position).title(markerItem.numOfpark);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                mMap.addMarker(markerOptions);
            }else{
                MarkerOptions markerOptions = new MarkerOptions();
                BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.red_state);
                Bitmap b=bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);

                markerOptions.position(position).title(markerItem.numOfpark);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                mMap.addMarker(markerOptions);
            }
        }

        // 서울에 대한 위치 설정
        LatLng seoul = new LatLng(37.52487, 126.92723);
        //카메라를 서울 위치로 옮긴다.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));

        // 최대, 최소 줌 설정
        mMap.setMinZoomPreference(5);
        mMap.setMaxZoomPreference(20);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layoutGov);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}