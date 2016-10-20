package com.miraclemakers.pitshop;

import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.miraclemakers.pitshop.adapters.CarViewAdapter;
import com.miraclemakers.pitshop.adapters.CompareAdapter;
import com.miraclemakers.pitshop.model.CompareModel;
import com.miraclemakers.pitshop.model.SpecificationModel;
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

public class CompareActivity extends AppCompatActivity {

    ListView lvCompare;
    public static String id1,id2,id1name,id2name;
    ArrayList<CompareModel> myArrayList;
    CompareAdapter compareAdapter;
    CompareModel tempvalues;
    int arrayCount;
    CompareActivity compareActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        compareActivity = this;

        lvCompare = (ListView) findViewById(R.id.compare_list);
        myArrayList = new ArrayList<>();
        myArrayList.clear();
        tempvalues = new CompareModel();
        tempvalues.setParamter("NAME");
        tempvalues.setSpec1(id1name);
        tempvalues.setSpec2(id2name);
        tempvalues.setType(getString(R.string.text_list));
        myArrayList.add(tempvalues);
        arrayCount = 0;
        new SpecFetchTask().execute("2");
    }

    public class SpecFetchTask extends AsyncTask<String,Void,String>{

        String error;
        String spec_url = getString(R.string.php_url) + "spec.php";
        int decide;
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            InputStream IS = null;
            int temp;
            decide = Integer.parseInt(params[0]);
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
                if(decide==2) {
                    parameters.put("id", id1);
                }else if(decide==1){
                    parameters.put("id", id2);
                }
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
            int i,k,engine,performance,payload,transmission,fuel,steering,suspension,tyre,brake,other;
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
                    if(decide==2) {
                        for (i = 1; i <= other; i++) {
                            if(i==engine+1||i==performance+1||i==payload+1||i==transmission+1||i==fuel+1||i==steering+1||
                                    i==suspension+1||i==tyre+1||i==brake+1){
                                continue;
                            }
                            if(i==1){
                                tempvalues = new CompareModel();
                                tempvalues.setParamter("Specifications");
                                tempvalues.setType(getString(R.string.title_list));
                                myArrayList.add(tempvalues);
                            }
                            else {
                                temp = user_data.getJSONObject("" + i);
                                tempvalues = new CompareModel();
                                tempvalues.setParamter(temp.getString("spec"));
                                tempvalues.setSpec1(temp.getString("" + i));
                                tempvalues.setType(getString(R.string.text_list));
                                myArrayList.add(tempvalues);
                            }
                        }
                        new SpecFetchTask().execute("1");
                    }else if(decide==1){
                        for(i = 2,k = 2; i <= other; i++){
                            if(i==engine+1||i==performance+1||i==payload+1||i==transmission+1||i==fuel+1||i==steering+1||
                                    i==suspension+1||i==tyre+1||i==brake+1){
                                continue;
                            }
                            temp = user_data.getJSONObject("" + i);
                            tempvalues = myArrayList.get(arrayCount + k);
                            tempvalues.setSpec2(temp.getString("" + i));
                            k++;
                        }
                        arrayCount = arrayCount + i - 10;
                        new FeatureFetchTask().execute("2");
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class FeatureFetchTask extends AsyncTask<String,Void,String>{

        String error;
        String feature_url = getString(R.string.php_url) + "feature.php";
        int decide;
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            InputStream IS = null;
            int temp,i=0;
            decide = Integer.parseInt(params[0]);

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
                if(decide==2) {
                    parameters.put("id", id1);
                }else if(decide==1){
                    parameters.put("id", id2);
                }
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
            int i,k,comfort,exterior,interior;
            try {
                JSONObject root = new JSONObject(result);
                JSONObject user_data = root.getJSONObject("user_data");
                JSONObject temp = user_data.getJSONObject("0");
                validate = temp.getString("validate");
                if(validate.equals("Successful")){
                    comfort = Integer.parseInt(temp.getString("comfort"));
                    exterior = Integer.parseInt(temp.getString("exterior"));
                    interior = Integer.parseInt(temp.getString("interior"));
                    if(decide==2) {
                        for (i = 1; i <= interior; i++) {
                            if(i==comfort+1||i==exterior+1){
                                continue;
                            }
                            if(i==1){
                                tempvalues = new CompareModel();
                                tempvalues.setParamter("Features");
                                tempvalues.setType(getString(R.string.title_list));
                                myArrayList.add(tempvalues);
                            }
                            else {
                                temp = user_data.getJSONObject("" + i);
                                tempvalues = new CompareModel();
                                tempvalues.setParamter(temp.getString("spec"));
                                tempvalues.setSpec1(temp.getString("" + i));
                                tempvalues.setType(getString(R.string.check_list));
                                myArrayList.add(tempvalues);
                            }
                        }
                        new FeatureFetchTask().execute("1");
                    }else if(decide==1){
                        for(i = 2,k = 2; i <= interior; i++){
                            if(i==comfort+1||i==exterior+1){
                                continue;
                            }
                            temp = user_data.getJSONObject("" + i);
                            tempvalues = myArrayList.get(arrayCount + k);
                            tempvalues.setSpec2(temp.getString("" + i));
                            k++;
                        }
                        arrayCount = arrayCount + i - 3;
                        new DimensionFetchTask().execute("2");
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public class DimensionFetchTask extends AsyncTask<String,Void,String>{

        String error;
        String dimension_url = getString(R.string.php_url) + "dimension.php";
        int decide;
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            InputStream IS = null;
            int temp,i=0;
            decide = Integer.parseInt(params[0]);

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
                if(decide==2) {
                    parameters.put("id", id1);
                }else if(decide==1){
                    parameters.put("id", id2);
                }
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
            int i,k,count;
            try {
                JSONObject root = new JSONObject(result);
                JSONObject user_data = root.getJSONObject("user_data");
                JSONObject temp = user_data.getJSONObject("0");
                validate = temp.getString("validate");
                if(validate.equals("Successful")){
                    count = Integer.parseInt(temp.getString("comfort"));
                    if(decide==2) {
                        for (i = 1; i <= count; i++) {
                            if(i==1){
                                tempvalues = new CompareModel();
                                tempvalues.setParamter("Dimensions");
                                tempvalues.setType(getString(R.string.title_list));
                                myArrayList.add(tempvalues);
                            }
                            else {
                                temp = user_data.getJSONObject("" + i);
                                tempvalues = new CompareModel();
                                tempvalues.setParamter(temp.getString("spec"));
                                tempvalues.setSpec1(temp.getString("" + i));
                                tempvalues.setType(getString(R.string.text_list));
                                myArrayList.add(tempvalues);
                            }
                        }
                        new FeatureFetchTask().execute("1");
                    }else if(decide==1){
                        for(i = 2,k = 2; i <= count; i++){
                            temp = user_data.getJSONObject("" + i);
                            tempvalues = myArrayList.get(arrayCount + k);
                            tempvalues.setSpec2(temp.getString("" + i));
                            k++;
                        }
                        arrayCount = arrayCount + i - 1;
                        compareAdapter = new CompareAdapter(compareActivity,myArrayList);
                        lvCompare.setAdapter(compareAdapter);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

}
