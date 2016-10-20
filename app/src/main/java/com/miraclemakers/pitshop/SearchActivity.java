package com.miraclemakers.pitshop;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.miraclemakers.pitshop.adapters.SearchListAdapter;
import com.miraclemakers.pitshop.model.SearchModel;
import com.miraclemakers.pitshop.serverfiles.QueryBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SearchView searchView;
    private String searchKey;
    ListView lvSearch;
    ArrayList<SearchModel> myArrayList;
    SearchListAdapter searchListAdapter;
    SearchActivity searchActivity;
    public static String globalSearchKey;
    TextView tvDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_search_filter);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_search);
        navigationView.setNavigationItemSelectedListener(this);

        searchActivity = this;
        lvSearch = (ListView) findViewById(R.id.list_search);
        tvDefault = (TextView) findViewById(R.id.search_default_text);
        myArrayList = new ArrayList<>();
        myArrayList.clear();
        searchKey = globalSearchKey;
        new SearchTask().execute(searchKey);

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
                myArrayList.clear();
                globalSearchKey = query;
                new SearchTask().execute(globalSearchKey);
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
                startActivity(new Intent(SearchActivity.this,NewCarActivity.class));
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

    public void onItemClick (int position){
        SearchModel tempValues = myArrayList.get(position);
        Intent i = new Intent(SearchActivity.this,CarView.class);
        i.putExtra("id",tempValues.getId());
        i.putExtra("name",tempValues.getName());
        i.putExtra("brand",tempValues.getBrand());
        i.putExtra("mileage",tempValues.getMileage());
        i.putExtra("seats",tempValues.getSeats());
        i.putExtra("bodytype",tempValues.getBodyType());
        i.putExtra("year",tempValues.getYear());
        startActivity(i);
    }

    public class SearchTask extends AsyncTask<String,Void,String>{
        String searchKey, error = "";
        String search_url = getString(R.string.php_url) + "search.php";
        @Override
        protected String doInBackground(String... params) {
            searchKey = params[0];
            HttpURLConnection httpURLConnection = null;
            InputStream IS = null;
            int temp;

            try {
                // Simulate network access.

                URL url = new URL(search_url);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(10000);

                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));

                ContentValues parameters = new ContentValues();
                parameters.put("searchKey", searchKey);
                String data = QueryBuilder.getQuery(parameters);

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                IS = httpURLConnection.getInputStream();

                while ((temp = IS.read()) != -1) {
                    error += (char) temp;
                }
                return error;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                error = "Sorry! Cannot Currently Render Service";
            } catch (IOException e) {
                e.printStackTrace();
                error = "Sorry! Cannot Currently Render Service";
            } finally {
                if (httpURLConnection != null)

                {
                    httpURLConnection.disconnect();
                }
                try {
                    if (IS != null) {
                        IS.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            return error;
        }


        protected void onPostExecute(String result) {
            String validate;
            int count;
            try {
                JSONObject root = new JSONObject(result);
                JSONObject user_data = root.getJSONObject("user_data");
                JSONObject temp = user_data.getJSONObject("0");
                validate = temp.getString("validate");
                if(validate.equals("Successful")){
                    count = Integer.parseInt(temp.getString("count"));
                    if(count == 0){
                        lvSearch.setVisibility(View.GONE);
                        tvDefault.setVisibility(View.VISIBLE);
                        tvDefault.setText(getString(R.string.search_default_text));
                    }
                    else {
                        for (int i = 1; i <= count; i++) {
                            temp = user_data.getJSONObject("" + i);
                            SearchModel searchModel = new SearchModel();
                            searchModel.setName(temp.getString("name"));
                            searchModel.setBrand(temp.getString("brand"));
                            searchModel.setSeats(temp.getString("seats"));
                            searchModel.setMileage(temp.getString("mileage"));
                            searchModel.setBodyType(temp.getString("body_type"));
                            searchModel.setYear(temp.getString("year"));
                            searchModel.setPrice(temp.getString("price"));
                            searchModel.setId(temp.getString("id"));
                            searchModel.setCountry(temp.getString("country"));
                            myArrayList.add(searchModel);
                        }
                        searchListAdapter = new SearchListAdapter(searchActivity,myArrayList);
                        lvSearch.setAdapter(searchListAdapter);
                        tvDefault.setVisibility(View.GONE);
                        lvSearch.setVisibility(View.VISIBLE);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
