package com.miraclemakers.pitshop;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.miraclemakers.pitshop.adapters.NewCarAdapter;
import com.miraclemakers.pitshop.model.NewCarListModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NewCarActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SearchView searchView;
    ListView lvBrand,lvTransmission,lvFuelType,lvBodyType;
    SeekBar sbPrice;
    int price;
    Button bPrice;
    TextView tvPrice,tvBrand,tvTransmission,tvFuel,tvBody,tvPriceView;
    ArrayList<NewCarListModel> arrayListFuel,arrayListBrand,arrayListBodyType,arrayListTransmission;
    NewCarAdapter newCarAdapterFuel,newCarAdapterBrand,newCarAdapterBodyType,newCarAdapterTransmission;
    NewCarActivity newCarActivity;
    LinearLayout llSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_car2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_new_car);
        navigationView.setNavigationItemSelectedListener(this);
        newCarActivity = this;
        lvBrand = (ListView) findViewById(R.id.new_car_brand_list);
        lvTransmission = (ListView) findViewById(R.id.new_car_transmission_list);
        lvFuelType = (ListView) findViewById(R.id.new_car_fuel_list);
        lvBodyType = (ListView) findViewById(R.id.new_car_body_list);
        bPrice = (Button) findViewById(R.id.new_car_price_button);
        tvPrice = (TextView) findViewById(R.id.new_car_price_text);
        tvBrand = (TextView) findViewById(R.id.new_car_brand_text);
        tvFuel = (TextView) findViewById(R.id.new_car_fuel_text);
        tvTransmission = (TextView) findViewById(R.id.new_car_transmission_text);
        tvBody = (TextView) findViewById(R.id.new_car_body_text);
        tvPriceView = (TextView) findViewById(R.id.new_car_priceview_text);
        llSeekBar = (LinearLayout) findViewById(R.id.new_car_seekbar_layout);
        sbPrice = (SeekBar) findViewById(R.id.new_car_seekBar);
        sbPrice.setMax(1000000000);
        tvPrice.setText("" + sbPrice.getProgress());
        sbPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvPrice.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                tvPrice.setText("" + seekBar.getProgress());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvPrice.setText("" + seekBar.getProgress());
            }
        });

        bPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = sbPrice.getProgress();
            }
        });

        arrayListFuel = new ArrayList<>();
        arrayListBodyType = new ArrayList<>();
        arrayListBrand = new ArrayList<>();
        arrayListTransmission = new ArrayList<>();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.new_car_brand_text:
                        if(lvBrand.getVisibility()==View.VISIBLE){
                            lvBrand.setVisibility(View.GONE);
                        }else if(lvBrand.getVisibility()==View.GONE){
                            lvBrand.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.new_car_fuel_text:
                        if(lvFuelType.getVisibility()==View.VISIBLE){
                            lvBrand.setVisibility(View.GONE);
                        }else if(lvFuelType.getVisibility()==View.GONE){
                            lvBrand.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.new_car_transmission_text:
                        if(lvTransmission.getVisibility()==View.VISIBLE){
                            lvBrand.setVisibility(View.GONE);
                        }else if(lvTransmission.getVisibility()==View.GONE){
                            lvBrand.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.new_car_body_text:
                        if(lvBrand.getVisibility()==View.VISIBLE){
                            lvBrand.setVisibility(View.GONE);
                        }else if(lvBrand.getVisibility()==View.GONE){
                            lvBrand.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.new_car_priceview_text:
                        if(llSeekBar.getVisibility()==View.VISIBLE){
                            lvBrand.setVisibility(View.GONE);
                        }else if(llSeekBar.getVisibility()==View.GONE){
                            lvBrand.setVisibility(View.VISIBLE);
                        }
                        break;
                    default:
                }
            }
        };

        tvBrand.setOnClickListener(onClickListener);
        tvFuel.setOnClickListener(onClickListener);
        tvTransmission.setOnClickListener(onClickListener);
        tvBody.setOnClickListener(onClickListener);
        tvPriceView.setOnClickListener(onClickListener);

        setFuelList();
        setBrandList();
        setBodyList();
        setTransmissionList();

    }

    private void setTransmissionList() {
        NewCarListModel newCarListModel = new NewCarListModel();
        newCarListModel.setImage1(getString(R.string.transmission_auto));
        newCarListModel.setImage2(getString(R.string.transmission_manual));
        arrayListTransmission.add(newCarListModel);
        newCarAdapterTransmission = new NewCarAdapter(newCarActivity,arrayListTransmission);
        lvTransmission.setAdapter(newCarAdapterTransmission);
    }

    private void setBodyList() {
        NewCarListModel newCarListModel = new NewCarListModel();
        newCarListModel.setImage1(getString(R.string.body_hatch_back));
        newCarListModel.setImage2(getString(R.string.body_sedan));
        newCarListModel.setImage3(getString(R.string.body_suv));
        arrayListBodyType.add(newCarListModel);
        newCarListModel = new NewCarListModel();
        newCarListModel.setImage1(getString(R.string.body_muv));
        newCarListModel.setImage2(getString(R.string.body_van));
        newCarListModel.setImage3(getString(R.string.body_coupe));
        arrayListBodyType.add(newCarListModel);
        newCarListModel = new NewCarListModel();
        newCarListModel.setImage1(getString(R.string.body_convertible));
        newCarListModel.setImage2(getString(R.string.body_crossover));
        arrayListBodyType.add(newCarListModel);
        newCarAdapterBodyType = new NewCarAdapter(newCarActivity,arrayListBodyType);
        lvBodyType.setAdapter(newCarAdapterBodyType);
    }

    private void setBrandList() {   
        new BrandFetchTask().execute();
    }

    private void setFuelList() {
        NewCarListModel newCarListModel = new NewCarListModel();
        newCarListModel.setImage1(getString(R.string.fuel_petrol));
        newCarListModel.setImage2(getString(R.string.fuel_diesel));
        newCarListModel.setImage3(getString(R.string.fuel_cng));
        arrayListFuel.add(newCarListModel);
        newCarListModel = new NewCarListModel();
        newCarListModel.setImage1(getString(R.string.fuel_lpg));
        newCarListModel.setImage2(getString(R.string.fuel_electric));
        newCarListModel.setImage3(getString(R.string.fuel_hybrid));
        arrayListFuel.add(newCarListModel);
        newCarAdapterFuel = new NewCarAdapter(newCarActivity,arrayListFuel);
        lvFuelType.setAdapter(newCarAdapterFuel);
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
                Intent intent = new Intent(NewCarActivity.this, SearchActivity.class);
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

    public void onItemClick(int mPosition) {

    }

    public class BrandFetchTask extends AsyncTask<Void,Void,String>{
        String brand_url = getString(R.string.php_url) + "brand.php";
        String error = "";
        @Override
        protected String doInBackground(Void... params) {
            HttpURLConnection httpURLConnection = null;
            InputStream IS = null;
            int temp;

            try {
                // Simulate network access.

                URL url = new URL(brand_url);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(10000);

                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

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
                    
                        for (int i = 1; i <= count; i++) {
                            temp = user_data.getJSONObject("" + i);
                            NewCarListModel newCarListModel = new NewCarListModel();
                            newCarListModel.setImage1(temp.getString("brand"));
                            i++;
                            if(i<=count){
                                temp = user_data.getJSONObject("" + i);
                                newCarListModel.setImage2(temp.getString("brand"));
                                i++;
                            }
                            if(i<=count){
                                temp = user_data.getJSONObject("" + i);
                                newCarListModel.setImage3(temp.getString("brand"));
                            }
                            arrayListBrand.add(newCarListModel);
                        }
                    newCarAdapterBrand = new NewCarAdapter(newCarActivity,arrayListBrand);
                    lvBrand.setAdapter(newCarAdapterBrand);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } 
        }
    }
}
