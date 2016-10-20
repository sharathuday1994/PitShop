package com.miraclemakers.pitshop;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
*Created by Nihal Pradeep
 * HomeActivity is the home page for the user. User can view latest updates, search for cars,
 * read news etc.
 */

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView imNewCars, imUsedCars, imVintageCars, imMore;
    private SearchView searchView;
    private String emailid, username;
    private TextView tvNavigationUserName, tvNavigationEmail;
  //  private ViewPager homeViewPager;



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.logoong);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_home);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        imNewCars = (ImageView) findViewById(R.id.appbar_new_car);
        imUsedCars = (ImageView) findViewById(R.id.appbar_used_cars);
        imVintageCars = (ImageView) findViewById(R.id.appbar_vintage);
        imMore = (ImageView) findViewById(R.id.appbar_more);
        //homeViewPager = (ViewPager) findViewById(R.id.pager);

        View.OnClickListener onAppBarImageClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,SearchActivity.class));
            }
        };

        imNewCars.setOnClickListener(onAppBarImageClickListener);
        imUsedCars.setOnClickListener(onAppBarImageClickListener);
        imVintageCars.setOnClickListener(onAppBarImageClickListener);
        imMore.setOnClickListener(onAppBarImageClickListener);

        View.OnTouchListener onAppBarImageTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                startActivity(new Intent(HomeActivity.this,SearchActivity.class));
                return false;
            }
        };
        imNewCars.setOnTouchListener(onAppBarImageTouchListener);
        imUsedCars.setOnTouchListener(onAppBarImageTouchListener);
        imVintageCars.setOnTouchListener(onAppBarImageTouchListener);
        imMore.setOnTouchListener(onAppBarImageTouchListener);

        tvNavigationEmail = (TextView) header.findViewById(R.id.navigation_header_email);
        tvNavigationUserName = (TextView) header.findViewById(R.id.navigation_header_username);
        emailid = LoginActivity.global_user;
        username = LoginActivity.global_name;

        if(emailid.equals("Guest")){
            tvNavigationUserName.setText(emailid);
            tvNavigationEmail.setText("");
        }
        else{
            tvNavigationUserName.setText(username);
            tvNavigationEmail.setText(emailid);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem item = menu.findItem(R.id.homemenu_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(HomeActivity.this,SearchActivity.class);
                SearchActivity.globalSearchKey = query;
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.homenav_new_cars:
                startActivity(new Intent(HomeActivity.this,NewCarActivity.class));
                break;
            case R.id.homenav_used_cars:
                break;
            case R.id.homenav_vintage_cars:
                break;
            case R.id.homenav_compare_cars:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
