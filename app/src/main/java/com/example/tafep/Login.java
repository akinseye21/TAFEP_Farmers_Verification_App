package com.example.tafep;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    EditText edt_email, edt_password;
    String str_email, str_password;
    Boolean bool_email=false, bool_password=false;
    Button login, createaccount;

    String login_email, login_fullname, login_phone, login_address, login_lga;

    ArrayList<String> arr_ward = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        edt_email = findViewById(R.id.email);
        edt_password = findViewById(R.id.password);
        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Pattern pattern;
                Matcher matcher;
                String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                pattern = Pattern.compile(EMAIL_PATTERN);
                CharSequence cs = (CharSequence) editable;
                matcher = pattern.matcher(cs);
                if(!(matcher.matches()==true)){
                    str_email="";
                    bool_email=false;
                    edt_email.setError("Invalid");
                }else{
                    str_email=edt_email.getText().toString();
                    bool_email=true;
                }
            }
        });
        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_password.getText().toString().length()>5){
                    str_password = edt_password.getText().toString();
                    bool_password = true;
                }else{
                    bool_password = false;
                    edt_password.setError("at least 6 characters");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        login = findViewById(R.id.login);
        createaccount = findViewById(R.id.createaccount);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bool_email && bool_password){
                    login(str_email, str_password);
                }
//                startActivity(new Intent(Login.this, Dashboard.class));
            }
        });
        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, CreateAccount.class));
            }
        });
    }

    public void login(String email, String password) {
        Dialog myDialog = new Dialog(Login.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Logging you in, please wait!");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tafepng.com/api/farm/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        System.out.println("Login response: " + response);

                        try {
                            JSONObject json = new JSONObject(response);
                            String wards = json.getString("wards");
                            login_email = json.getString("email");
                            login_fullname = json.getString("fullname");
                            login_phone = json.getString("phone_number");
                            login_address = json.getString("address");
                            login_lga = json.getString("lga");

                            JSONArray jsonArray = new JSONArray(wards);
                            int len = jsonArray.length();
                            for (int j=0; j<len; j++){
                                JSONObject newstring = jsonArray.getJSONObject(j);
                                String key = newstring.getString("RA");
                                arr_ward.add(key);
                            }

                            SharedPreferences sharedPreferences = getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            // Convert the ArrayList to JSON using Gson
                            Gson gson = new Gson();
                            String ward_string = gson.toJson(arr_ward);
                            // Store the JSON string in SharedPreferences
                            editor.putString("wards", ward_string);
                            editor.putString("email", login_email);
                            editor.putString("fullname", login_fullname);
                            editor.putString("phone", login_phone);
                            editor.putString("address", login_address);
                            editor.putString("lga", login_lga);
                            // Commit the changes
                            editor.apply();

                            System.out.println("Wards = "+arr_ward);
                            Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Login.this, Dashboard.class);
                            startActivity(i);

                        }catch(Exception e) {
                            Toast.makeText(Login.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        myDialog.dismiss();
                        if(volleyError == null){
                            return;
                        }
                        Log.e(TAG, volleyError.toString());
                        System.out.println("Network Error "+volleyError);
                        Toast.makeText(Login.this, "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
    }
}