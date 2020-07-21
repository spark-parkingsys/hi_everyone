package com.example.s_park;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
public class MainActivity extends AppCompatActivity {
    ImageView parkStatePic;
    TextView parkSateTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        parkStatePic = findViewById(R.id.parkingStatePic);
        parkSateTxt = findViewById(R.id.parkingStateTxt);

        this.InitializeLayout();

        //주차면 상태 설정
        updateSate(1);
    }

    public void updateSate(int now){
        if(now==1){
            parkStatePic.setImageResource(R.drawable.allowance);
            parkSateTxt.setText("나의 차량이 주차되어 있습니다.");
        }else if(now==2){
            parkStatePic.setImageResource(R.drawable.empty);
            parkSateTxt.setText("나의 주차면이 비어 있습니다.");
        }else if(now==3){
            parkStatePic.setImageResource(R.drawable.disallowance);
            parkSateTxt.setText("허용되지 않은 차량이 주차되어 있습니다.");
        }else if(now==4){
            parkStatePic.setImageResource(R.drawable.checking);
            parkSateTxt.setText("허용된 차량인지 확인해주세요.");
        }
    }

    public void InitializeLayout(){
        //toolBar를 통해 App Bar 생성
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //App Bar의 좌측 영영에 Drawer를 Open 하기 위한 Incon 추가

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_list);

        DrawerLayout drawLayout = (DrawerLayout) findViewById(R.id.drawer_layoutUser);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_viewUser);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawLayout, toolbar, R.string.open, R.string.close);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.menuitem1:
                        Toast.makeText(getApplicationContext(), "SelectedItem 1", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, MonitorActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.menuitem2:
                        Toast.makeText(getApplicationContext(), "SelectedItem 2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuitem3:
                        Toast.makeText(getApplicationContext(), "SelectedItem 3", Toast.LENGTH_SHORT).show();
                        break;
                }
                DrawerLayout drawer = findViewById(R.id.drawer_layoutUser);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        drawLayout.addDrawerListener(actionBarDrawerToggle);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layoutUser);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}


