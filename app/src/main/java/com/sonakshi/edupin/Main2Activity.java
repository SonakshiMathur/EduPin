package com.sonakshi.edupin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    Intent intent;
    String username;
    String userPhone;
    String userEmail;
    String token;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dash_board);
        intent=getIntent();
        username=intent.getStringExtra("username");
        userPhone=intent.getStringExtra("phone");
        userEmail=intent.getStringExtra("email");
        token=intent.getStringExtra("token");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (NavigationView) findViewById(R.id.nav_view);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

          Window window = this.getWindow();
//
window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//
         window.setStatusBarColor(ContextCompat.getColor(this,R.color.blueApp));
//

        Bundle bundle=new Bundle();
        bundle.putString("token",token);
        Fragment fragment = new DashFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        mDrawerList.setCheckedItem(R.id.nav_dash);
        setTitle("DashBoard");

        mDrawerList.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                String title="";
                Fragment fragment = null;
                Bundle bundle=new Bundle();
                bundle.putString("token",token);
                Log.i("in ac","in ac");

                switch (id) {
                    case R.id.nav_dash:
                        fragment = new DashFragment();
                        title="DashBoard";
                        fragment.setArguments(bundle);
                        break;
                    case R.id.nav_callendar:
                        fragment = new CalendarFragment();
                        title="Calendar";
                        fragment.setArguments(bundle);

                        break;
                    case R.id.nav_slideshow:
                        break;
                    case R.id.nav_manage:
                        break;
                    default:
                        break;
                }

                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();

                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

                    mDrawerList.setCheckedItem(id);
                    setTitle(title);
                    mDrawerLayout.closeDrawer(mDrawerList);

                } else {
                    Log.i("Main2Activity", "Error in creating fragment");
                }


                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        setupDrawerToggle();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        ((TextView)findViewById(R.id.usersPhn)).setText(userPhone);
        ((TextView)findViewById(R.id.usersEmail)).setText(userEmail);
        ((TextView)findViewById(R.id.usersname)).setText(username);

        return super.onCreateOptionsMenu(menu);
    }


}
