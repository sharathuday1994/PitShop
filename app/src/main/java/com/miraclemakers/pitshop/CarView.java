package com.miraclemakers.pitshop;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.miraclemakers.pitshop.adapters.CarViewAdapter;
import com.miraclemakers.pitshop.adapters.SearchListAdapter;
import com.miraclemakers.pitshop.model.SpecificationModel;
import com.miraclemakers.pitshop.model.VariantModel;
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
import java.util.List;

public class CarView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LocalActivityManager localActivityManager;
    SearchView searchView;
    String variant,carID,carName,carBrand,carMileage,carSeats,carBodyType,carYear;
    CarViewAdapter variantAdapter;
    CarViewAdapter fComfortAdapter,fExteriorAdapter,fInteriorAdapter;
    CarViewAdapter dimensionsAdapter;
    CarViewAdapter sEngineAdapter,sPerformanceAdapter,sPayloadAdapter,sTransmissionAdapter,sFuelAdapter,sSteeringAdapter,
    sSuspensionAdapter,sTyreAdapter,sBrakeAdapter,sOtherAdapter;
    CarView carview;
    ListView lvVariant;
    ListView lvDimensions;
    ListView lvFeaturesComfort, lvFeaturesExterior, lvFeaturesInterior;
    ListView lvSpecEngine,lvSpecPerformance,lvSpecPayload,lvSpecTransmission,lvSpecFuel,lvSpecSteering,lvSpecSuspension,lvSpecTyre,lvSpecBrake,lvSpecOther;
    ArrayList<VariantModel> variantArrayList;
    ArrayList<SpecificationModel> dimensionArrayList;
    ArrayList<SpecificationModel> fComfortArrayList,fExteriorArrayList,fInteriorArrayList;
    ArrayList<SpecificationModel> sEngineArrayList,sPerformanceArrayList,sPayloadArrayList,sTransmissionArrayList,
            sFuelArrayList,sSteeringArrayList,sSuspensionArrayList,sTyreArrayList,sBrakeArrayList,sOtherArrayList;
    LinearLayout llTabHost;
    TextView tvName,tvBrand,tvMileage,tvSeats,tvBodyType,tvYear,tvPrice;
    Button bBuy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_carview);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_carview_compare);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        carview = this;                     //Activity
        variantArrayList = new ArrayList<>(); // Assigning ArrayList Starts here
        dimensionArrayList = new ArrayList<>();
        fComfortArrayList = new ArrayList<>();
        fExteriorArrayList = new ArrayList<>();
        fInteriorArrayList = new ArrayList<>();
        sEngineArrayList = new ArrayList<>();
        sPerformanceArrayList = new ArrayList<>();
        sPayloadArrayList = new ArrayList<>();
        sTransmissionArrayList = new ArrayList<>();
        sFuelArrayList = new ArrayList<>();
        sSteeringArrayList = new ArrayList<>();
        sSuspensionArrayList = new ArrayList<>();
        sTyreArrayList = new ArrayList<>();
        sBrakeArrayList = new ArrayList<>();
        sOtherArrayList = new ArrayList<>();
        //ends here

        //Instantiating TextViews

        tvName = (TextView) findViewById(R.id.car_overview_name);
        tvBrand = (TextView) findViewById(R.id.car_overview_brand);
        tvMileage = (TextView) findViewById(R.id.car_overview_mileage);
        tvSeats = (TextView) findViewById(R.id.car_overview_seats);
        tvBodyType = (TextView) findViewById(R.id.car_overview_body_type);
        tvYear = (TextView) findViewById(R.id.car_overview_year);
        tvPrice = (TextView) findViewById(R.id.car_overview_price);

        //Instantiating Button
        bBuy = (Button) findViewById(R.id.car_overview_buy_button);
        localActivityManager = new LocalActivityManager(CarView.this,false);
        //TabHosts are created

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        localActivityManager.dispatchCreate(savedInstanceState);
        tabHost.setup(localActivityManager);

        TabHost.TabSpec tabSpecSpecifications = tabHost.newTabSpec("Specifications");
        tabSpecSpecifications.setContent(R.id.specifications);
        tabSpecSpecifications.setIndicator("Spec");
        tabHost.addTab(tabSpecSpecifications);

        TabHost.TabSpec tabSpecFeatures = tabHost.newTabSpec("Features");
        tabSpecFeatures.setContent(R.id.features);
        tabSpecFeatures.setIndicator("Features");
        tabHost.addTab(tabSpecFeatures);

        TabHost.TabSpec tabSpecDimensions = tabHost.newTabSpec("Dimensions");
        tabSpecDimensions.setContent(R.id.dimensions);
        tabSpecDimensions.setIndicator("Dimensions");
        tabHost.addTab(tabSpecDimensions);
        //List Views are assigned
        lvVariant = (ListView) findViewById(R.id.car_overview_variant_list);
        lvFeaturesComfort = (ListView) findViewById(R.id.features_list_comfort);
        lvFeaturesExterior = (ListView) findViewById(R.id.features_list_exterior);
        lvFeaturesInterior = (ListView) findViewById(R.id.features_list_interior);
        lvSpecEngine = (ListView) findViewById(R.id.specifications_engine);
        lvSpecPerformance = (ListView) findViewById(R.id.specifications_performance);
        lvSpecPayload = (ListView) findViewById(R.id.specifications_payload);
        lvSpecTransmission = (ListView) findViewById(R.id.specifications_transmission);
        lvSpecFuel = (ListView) findViewById(R.id.specifications_fuel);
        lvSpecSteering = (ListView) findViewById(R.id.specifications_steering);
        lvSpecSuspension = (ListView) findViewById(R.id.specifications_suspension);
        lvSpecTyre = (ListView) findViewById(R.id.specifications_tyre);
        lvSpecBrake = (ListView) findViewById(R.id.specifications_brake);
        lvSpecOther = (ListView) findViewById(R.id.specifications_other);
        lvDimensions = (ListView) findViewById(R.id.dimensions_list);
        llTabHost = (LinearLayout) findViewById(R.id.car_overview_tabhost_layout);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            lvVariant.setDivider(getDrawable(R.drawable.list_divider));
            lvFeatures.setDivider(getDrawable(R.drawable.list_divider));
            lvSpecifications.setDivider(getDrawable(R.drawable.list_divider));
            lvDimensions.setDivider(getDrawable(R.drawable.list_divider));
        }*/
        variant = "";
        Intent i = getIntent();
        //Values Passed from the calling activity
        carID = i.getStringExtra("id");
        carName = i.getStringExtra("name");
        carBrand = i.getStringExtra("brand");
        carMileage = i.getStringExtra("mileage");
        carSeats = i.getStringExtra("seats");
        carBodyType = i.getStringExtra("bodytype");
        carYear = i.getStringExtra("year");

        //Initialising TextViews
        tvName.setText(carName);
        tvBrand.setText(carBrand);
        tvMileage.setText(carMileage);
        tvSeats.setText(carSeats);
        tvBodyType.setText(carBodyType);
        tvYear.setText(carYear);

        bBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });
        new VariantFetchTask().execute(carID);

    }

    private void addToCart() {
        Intent intent = new Intent(CarView.this,CartActivity.class);
        intent.putExtra("id",variant);
        startActivity(intent);
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
                Intent intent = new Intent(CarView.this, SearchActivity.class);
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
                startActivity(new Intent(CarView.this,NewCarActivity.class));
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

    public void onItemClick(int mposition){
        VariantModel variantModel = variantArrayList.get(mposition);
        variant = variantModel.getId();
        tvName.setText(carName+" "+variantModel.getName());
        tvPrice.setText(variantModel.getPrice());
        llTabHost.setVisibility(View.VISIBLE);
        String id = variantModel.getId();
        new DimensionFetchTask().execute(id);
        new FeatureFetchTask().execute(id);
        new SpecFetchTask().execute(id);
    }

    public void onPause(){
        super.onPause();
        localActivityManager.dispatchPause(isFinishing());

    }
    public void onResume(){
        super.onResume();
        localActivityManager.dispatchResume();
    }

    public class VariantFetchTask extends AsyncTask<String,Void,String>{

        String error = "",id;
        String variant_url = getString(R.string.php_url) + "variant.php";
        @Override
        protected String doInBackground(String... params) {
            id = params[0];
            HttpURLConnection httpURLConnection = null;
            InputStream IS = null;
            int temp;

            try {
                // Simulate network access.

                URL url = new URL(variant_url);
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
                parameters.put("id", id);
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
                    variantArrayList.clear();
                        for (int i = 1; i <= count; i++) {
                            temp = user_data.getJSONObject("" + i);
                            VariantModel variantModel = new VariantModel();
                            variantModel.setName(temp.getString("name"));
                            variantModel.setPrice(temp.getString("price"));
                            variantModel.setId(temp.getString("id"));
                            variantModel.setColour(temp.getString("colour"));
                            variantArrayList.add(variantModel);
                        }
                        variantAdapter = new CarViewAdapter(carview,variantArrayList,getString(R.string.variant_list));
                        lvVariant.setAdapter(variantAdapter);
                    }

                } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class DimensionFetchTask extends AsyncTask<String,Void,String>{

        String error = "",id;
        String dimension_url = getString(R.string.php_url) + "dimension.php";
        @Override
        protected String doInBackground(String... params) {
            id = params[0];
            HttpURLConnection httpURLConnection = null;
            InputStream IS = null;
            int temp;

            try {
                // Simulate network access.

                URL url = new URL(dimension_url);
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
                parameters.put("id", id);
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
                    dimensionArrayList.clear();
                    for (int i = 2; i <= count; i++) {
                        temp = user_data.getJSONObject("" + i);
                        SpecificationModel specificationModel = new SpecificationModel();
                        specificationModel.setName(temp.getString("spec"));
                        specificationModel.setSpec(temp.getString("" + i));
                        dimensionArrayList.add(specificationModel);
                    }
                    dimensionsAdapter = new CarViewAdapter(carview,dimensionArrayList,getString(R.string.text_list));
                    lvDimensions.setAdapter(dimensionsAdapter);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    public class FeatureFetchTask extends AsyncTask<String,Void,String>{

        String error = "",id;
        String feature_url = getString(R.string.php_url) + "feature.php";
        @Override
        protected String doInBackground(String... params) {
            id = params[0];
            HttpURLConnection httpURLConnection = null;
            InputStream IS = null;
            int temp;

            try {
                // Simulate network access.

                URL url = new URL(feature_url);
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
                parameters.put("id", id);
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
            int i,comfort,exterior,interior;
            try {
                JSONObject root = new JSONObject(result);
                JSONObject user_data = root.getJSONObject("user_data");
                JSONObject temp = user_data.getJSONObject("0");
                validate = temp.getString("validate");
                if(validate.equals("Successful")){
                    comfort = Integer.parseInt(temp.getString("comfort"));
                    exterior = Integer.parseInt(temp.getString("exterior"));
                    interior = Integer.parseInt(temp.getString("interior"));
                    fComfortArrayList.clear();
                    for (i = 2; i <= comfort; i++) {
                        temp = user_data.getJSONObject("" + i);
                        SpecificationModel specificationModel = new SpecificationModel();
                        specificationModel.setName(temp.getString("spec"));
                        specificationModel.setSpec(temp.getString("" + i));
                        fComfortArrayList.add(specificationModel);
                    }
                    fComfortAdapter = new CarViewAdapter(carview,fComfortArrayList,getString(R.string.check_list));
                    lvFeaturesComfort.setAdapter(fComfortAdapter);
                    fExteriorArrayList.clear();
                    for(i = comfort + 2; i <= exterior; i++){
                        temp = user_data.getJSONObject("" + i);
                        SpecificationModel specificationModel = new SpecificationModel();
                        specificationModel.setName(temp.getString("spec"));
                        specificationModel.setSpec(temp.getString("" + i));
                        fExteriorArrayList.add(specificationModel);
                    }
                    fExteriorAdapter = new CarViewAdapter(carview,fExteriorArrayList,getString(R.string.check_list));
                    lvFeaturesExterior.setAdapter(fExteriorAdapter);
                    fInteriorArrayList.clear();
                    for(i = exterior + 2; i <= interior; i++){
                        temp = user_data.getJSONObject("" + i);
                        SpecificationModel specificationModel = new SpecificationModel();
                        specificationModel.setName(temp.getString("spec"));
                        specificationModel.setSpec(temp.getString("" + i));
                        fInteriorArrayList.add(specificationModel);
                    }
                    fInteriorAdapter = new CarViewAdapter(carview,fInteriorArrayList,getString(R.string.check_list));
                    lvFeaturesInterior.setAdapter(fInteriorAdapter);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public class SpecFetchTask extends AsyncTask<String,Void,String>{

        String error = "",id;
        String spec_url = getString(R.string.php_url) + "spec.php";
        @Override
        protected String doInBackground(String... params) {
            id = params[0];
            HttpURLConnection httpURLConnection = null;
            InputStream IS = null;
            int temp;

            try {
                // Simulate network access.

                URL url = new URL(spec_url);
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
                parameters.put("id", id);
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
            int i,engine,performance,payload,transmission,fuel,steering,suspension,tyre,brake,other;
            try {
                JSONObject root = new JSONObject(result);
                JSONObject user_data = root.getJSONObject("user_data");
                JSONObject temp = user_data.getJSONObject("0");
                validate = temp.getString("validate");
                if(validate.equals("Successful")){
                    engine =  Integer.parseInt(temp.getString("engine"));
                    performance = Integer.parseInt(temp.getString("performance"));
                    payload = Integer.parseInt(temp.getString("payload"));
                    transmission = Integer.parseInt(temp.getString("transmission"));
                    fuel = Integer.parseInt(temp.getString("fuel"));
                    steering = Integer.parseInt(temp.getString("steering"));
                    suspension = Integer.parseInt(temp.getString("suspension"));
                    tyre = Integer.parseInt(temp.getString("tyre"));
                    brake = Integer.parseInt(temp.getString("brake"));
                    other = Integer.parseInt(temp.getString("other"));
                    sEngineArrayList.clear();
                    for (i = 2; i <= engine; i++) {
                        temp = user_data.getJSONObject("" + i);
                        SpecificationModel specificationModel = new SpecificationModel();
                        specificationModel.setName(temp.getString("spec"));
                        specificationModel.setSpec(temp.getString("" + i));
                        sEngineArrayList.add(specificationModel);
                    }
                    sEngineAdapter = new CarViewAdapter(carview,sEngineArrayList,getString(R.string.text_list));
                    lvSpecEngine.setAdapter(sEngineAdapter);
                    sPerformanceArrayList.clear();
                    for (i = engine + 2; i <= performance; i++) {
                        temp = user_data.getJSONObject("" + i);
                        SpecificationModel specificationModel = new SpecificationModel();
                        specificationModel.setName(temp.getString("spec"));
                        specificationModel.setSpec(temp.getString("" + i));
                        sPerformanceArrayList.add(specificationModel);
                    }
                    sPerformanceAdapter = new CarViewAdapter(carview,sPerformanceArrayList,getString(R.string.text_list));
                    lvSpecPerformance.setAdapter(sPerformanceAdapter);
                    sPayloadArrayList.clear();
                    for (i = performance + 2; i <= payload; i++) {
                        temp = user_data.getJSONObject("" + i);
                        SpecificationModel specificationModel = new SpecificationModel();
                        specificationModel.setName(temp.getString("spec"));
                        specificationModel.setSpec(temp.getString("" + i));
                        sPayloadArrayList.add(specificationModel);
                    }
                    sPayloadAdapter = new CarViewAdapter(carview,sPayloadArrayList,getString(R.string.text_list));
                    lvSpecPayload.setAdapter(sPayloadAdapter);
                    sTransmissionArrayList.clear();
                    for (i = payload + 2; i <= transmission; i++) {
                        temp = user_data.getJSONObject("" + i);
                        SpecificationModel specificationModel = new SpecificationModel();
                        specificationModel.setName(temp.getString("spec"));
                        specificationModel.setSpec(temp.getString("" + i));
                        sTransmissionArrayList.add(specificationModel);
                    }
                    sTransmissionAdapter = new CarViewAdapter(carview,sTransmissionArrayList,getString(R.string.text_list));
                    lvSpecTransmission.setAdapter(sTransmissionAdapter);
                    sFuelArrayList.clear();
                    for (i = transmission + 2; i <= fuel; i++) {
                        temp = user_data.getJSONObject("" + i);
                        SpecificationModel specificationModel = new SpecificationModel();
                        specificationModel.setName(temp.getString("spec"));
                        specificationModel.setSpec(temp.getString("" + i));
                        sFuelArrayList.add(specificationModel);
                    }
                    sFuelAdapter = new CarViewAdapter(carview,sFuelArrayList,getString(R.string.text_list));
                    lvSpecFuel.setAdapter(sFuelAdapter);
                    sSteeringArrayList.clear();
                    for (i = fuel + 2; i <= steering; i++) {
                        temp = user_data.getJSONObject("" + i);
                        SpecificationModel specificationModel = new SpecificationModel();
                        specificationModel.setName(temp.getString("spec"));
                        specificationModel.setSpec(temp.getString("" + i));
                        sSteeringArrayList.add(specificationModel);
                    }
                    sSteeringAdapter = new CarViewAdapter(carview,sSteeringArrayList,getString(R.string.text_list));
                    lvSpecSteering.setAdapter(sSteeringAdapter);
                    sSuspensionArrayList.clear();
                    for (i = steering + 2; i <= suspension; i++) {
                        temp = user_data.getJSONObject("" + i);
                        SpecificationModel specificationModel = new SpecificationModel();
                        specificationModel.setName(temp.getString("spec"));
                        specificationModel.setSpec(temp.getString("" + i));
                        sSuspensionArrayList.add(specificationModel);
                    }
                    sSuspensionAdapter = new CarViewAdapter(carview,sSuspensionArrayList,getString(R.string.text_list));
                    lvSpecSuspension.setAdapter(sSuspensionAdapter);
                    sTyreArrayList.clear();
                    for (i = suspension + 2; i <= tyre; i++) {
                        temp = user_data.getJSONObject("" + i);
                        SpecificationModel specificationModel = new SpecificationModel();
                        specificationModel.setName(temp.getString("spec"));
                        specificationModel.setSpec(temp.getString("" + i));
                        sTyreArrayList.add(specificationModel);
                    }
                    sTyreAdapter = new CarViewAdapter(carview,sTyreArrayList,getString(R.string.text_list));
                    lvSpecTyre.setAdapter(sTyreAdapter);
                    sBrakeArrayList.clear();
                    for (i = tyre + 2; i <= brake; i++) {
                        temp = user_data.getJSONObject("" + i);
                        SpecificationModel specificationModel = new SpecificationModel();
                        specificationModel.setName(temp.getString("spec"));
                        specificationModel.setSpec(temp.getString("" + i));
                        sBrakeArrayList.add(specificationModel);
                    }
                    sBrakeAdapter = new CarViewAdapter(carview,sBrakeArrayList,getString(R.string.text_list));
                    lvSpecBrake.setAdapter(sBrakeAdapter);
                    sOtherArrayList.clear();
                    for (i = brake + 2; i <= other; i++) {
                        temp = user_data.getJSONObject("" + i);
                        SpecificationModel specificationModel = new SpecificationModel();
                        specificationModel.setName(temp.getString("spec"));
                        specificationModel.setSpec(temp.getString("" + i));
                        sOtherArrayList.add(specificationModel);
                    }
                    sOtherAdapter = new CarViewAdapter(carview,sOtherArrayList,getString(R.string.text_list));
                    lvSpecOther.setAdapter(sOtherAdapter);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
