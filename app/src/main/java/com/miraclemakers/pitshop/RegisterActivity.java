package com.miraclemakers.pitshop;

import android.app.Dialog;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.miraclemakers.pitshop.serverfiles.QueryBuilder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    private View llregisterForm;
    private Button bRegister;
    private EditText etFirstName, etLastName, etEmailID, etPassword, etConfirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        llregisterForm = (LinearLayout) findViewById(R.id.register_form);
        llregisterForm.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }
        });
        etFirstName = (EditText) findViewById(R.id.register_first_name);
        etLastName = (EditText) findViewById(R.id.register_last_name);
        etEmailID = (EditText) findViewById(R.id.register_emailid);
        etPassword = (EditText) findViewById(R.id.register_password);
        etConfirmPassword = (EditText) findViewById(R.id.register_confirm_password);
        bRegister = (Button) findViewById(R.id.register_button);
        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegistration();
            }
        });
    }

    private void attemptRegistration(){
        String firstname,lastname,emailid,password,confirmPassword;
        firstname = etFirstName.getText().toString();
        lastname = etLastName.getText().toString();
        emailid = etEmailID.getText().toString();
        password = etPassword.getText().toString();
        confirmPassword = etConfirmPassword.getText().toString();

        View focusView = null;
        Boolean cancel = false;

        if(TextUtils.isEmpty(firstname)) {
            etFirstName.setError(getString(R.string.field_required));
            cancel = true;
            focusView = etFirstName;
        }

        if(TextUtils.isEmpty(lastname)){
            etLastName.setError(getString(R.string.field_required));
            cancel = true;
            focusView = etLastName;
        }
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
        if(TextUtils.isEmpty(confirmPassword)){
            etConfirmPassword.setError(getString(R.string.field_required));
            cancel = true;
            focusView = etConfirmPassword;
        }else{
            if(!confirmPassword.equals(password)){
                etConfirmPassword.setError(getString(R.string.password_mismatch));
                cancel = true;
                focusView = etConfirmPassword;
            }
        }

        if(cancel){
            focusView.requestFocus();
        }else{
            new UserRegisterTask().execute(firstname,lastname,emailid,password);
        }
    }

    public class UserRegisterTask extends AsyncTask<String,Void,String> {

        String reg_url = getString(R.string.php_url) + "registration.php";
        String firstname, lastname, emailid, password;
        @Override
        protected String doInBackground(String... params) {

            firstname = params[0];
            lastname = params[1];
            emailid = params[2];
            password = params[3];
            HttpURLConnection httpURLConnection = null;
            InputStream IS = null;
            String error = "";
            int temp;
            try {

                URL url = new URL(reg_url);
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
                parameters.put("firstname",firstname);
                parameters.put("lastname",lastname);
                parameters.put("emailid",emailid);
                parameters.put("password",password);
                String data = QueryBuilder.getQuery(parameters);

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                IS = httpURLConnection.getInputStream();

                while((temp=IS.read())!=-1){
                    error += (char)temp;
                }
                return error;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(httpURLConnection!=null)

                {
                    httpURLConnection.disconnect();

                }
                try {
                    if(IS!=null) {
                        IS.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            return null;
        }
        protected void onPostExecute(String result) {

            Toast.makeText(RegisterActivity.this,result,Toast.LENGTH_LONG).show();
            if(result.equals("Yay!! Your keys are ready. Login to enter your ride.")){
               startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        }
    }
}
