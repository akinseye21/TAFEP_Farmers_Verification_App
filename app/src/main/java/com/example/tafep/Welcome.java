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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

public class Welcome extends AppCompatActivity {
    Button continue1;
    String fullname, email, password;
    TextView txt_fullname;

    String login_email, login_fullname, login_phone, login_address, login_lga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        fullname = i.getStringExtra("fullname");
        email = i.getStringExtra("email");
        password = i.getStringExtra("password");

        txt_fullname = findViewById(R.id.fullname);
        txt_fullname.setText(fullname);
        continue1 = findViewById(R.id.continue1);
        continue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
                startActivity(new Intent(Welcome.this, Dashboard.class));
            }
        });
    }

    private void login() {
        ArrayList<String> arr_ward = new ArrayList<>();

        Dialog myDialog = new Dialog(Welcome.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Verifying, please wait!");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tafepng.com/api/farm/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        System.out.println("Welcome response: " + response);

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

                            Toast.makeText(Welcome.this, "Verification successful. Welcome In!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Welcome.this, Dashboard.class);
                            startActivity(i);

                        }catch(Exception e) {
                            Toast.makeText(Welcome.this, "Verification failed. Please try again", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Welcome.this, "Verification failed!", Toast.LENGTH_SHORT).show();
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