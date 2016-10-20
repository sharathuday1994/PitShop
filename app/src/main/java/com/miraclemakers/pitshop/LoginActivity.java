package com.miraclemakers.pitshop;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class LoginActivity extends AppCompatActivity {
    private Button bSignIn;
    private TextView tvSkip,tvRegister;
    private EditText etEmailID,etPassword;
    private View llImage,llDetail;
    public static String global_user="Guest";
    public static String global_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bSignIn = (Button) findViewById(R.id.signin_login);
        tvSkip = (TextView) findViewById(R.id.skip_login);
        tvRegister = (TextView) findViewById(R.id.register_login);
        llImage = (LinearLayout) findViewById(R.id.image_layout_login);
        llDetail = (RelativeLayout) findViewById(R.id.detail_layout_login);
        etEmailID = (EditText) findViewById(R.id.emailid_login);
        etPassword = (EditText) findViewById(R.id.password_login);
        
        llImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                View view = LoginActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });
        llDetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                View view = LoginActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });
        bSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
    public void attemptLogin(){
        String emailid,password;
        emailid = etEmailID.getText().toString();
        password = etPassword.getText().toString();
        View focusView = null;
        boolean cancel = false;

        if(TextUtils.isEmpty(emailid)){
            etEmailID.setError(getString(R.string.field_required));
            cancel = true;
            focusView = etEmailID;
        }else{
            if(!emailid.contains("@")){
                etEmailID.setError(getString(R.string.invalid_email));
                cancel = true;
                focusView = etEmailID;
            }
        }
        if(TextUtils.isEmpty(password)){
            etPassword.setError(getString(R.string.field_required));
            cancel = true;
            focusView = etPassword;
        }

        if(cancel){
            focusView.requestFocus();
        }
        else{
            new UserLoginTask(emailid,password).execute();
        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mEmail;
        private final String mPassword;
        private String error = "";
        private final String login_url = getString(R.string.php_url) + "login.php";


        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected String doInBackground(Void... params) {

            HttpURLConnection httpURLConnection = null;
            InputStream IS = null;
            int temp;

            try {
                // Simulate network access.

                URL url = new URL(login_url);
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
                parameters.put("emailid", mEmail);
                parameters.put("password", mPassword);
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
        @Override
        protected void onPostExecute(String result) {
            try {

                JSONObject root = new JSONObject(result);
                JSONObject user_data = root.getJSONObject("user_data");
                String check = user_data.getString("registered");
                if(check.equals("Successful")) {
                    String name = user_data.getString("firstname") + " " + user_data.getString("lastname");
                    Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                    global_user = mEmail;
                    global_name = name;
                    startActivity(i);
                }
                else
                {
                    Snackbar.make(getCurrentFocus(),"Login Failed. Make Sure Your Email/Password is correct",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
