package com.example.tafep.Fragment;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tafep.Adapters.WardAdapter;
import com.example.tafep.Dashboard;
import com.example.tafep.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentHome extends Fragment {

    TextView txtlga;
    Button startverification;

    ArrayList<String> wards = new ArrayList<>();
    ArrayList<String> total_farms = new ArrayList<>();
    ArrayList<String> verifieds = new ArrayList<>();
    String username, lga, email;
    GridView gridview;


    public FragmentHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        // Get the SharedPreferences instance
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("fullname", "");
        lga = sharedPreferences.getString("lga", "");
        email = sharedPreferences.getString("email", "");

        gridview = v.findViewById(R.id.gridview);

        wards.clear();
        total_farms.clear();
        verifieds.clear();
        // Retrieve the JSON string
//        String json = sharedPreferences.getString("wards", null);
//        String login_lga = sharedPreferences.getString("lga", null);
        // Convert the JSON string back to an ArrayList using Gson
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList<String>>() {}.getType();
//        wards = gson.fromJson(json, type);

        getFarmStats();

        txtlga = v.findViewById(R.id.txtlga);
        txtlga.setText(lga);
        startverification = v.findViewById(R.id.startverification);
        startverification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dashboard.navigateFragment(2);
            }
        });

        return v;
    }

    private void getFarmStats() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tafepng.com/api/farm/getfarmstat",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("Farm stats response: " + response);

                        try {
                            JSONObject json = new JSONObject(response);
                            String lga_stat = json.getString("lga_stat");
                            JSONArray jsonArray = new JSONArray(lga_stat);
                            int len = jsonArray.length();
                            for (int i=0; i<len; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String ward = jsonObject.getString("ward");
                                String total_farm = jsonObject.getString("total_farms");
                                String verified = jsonObject.getString("verified");

                                wards.add(ward);
                                total_farms.add(total_farm);
                                verifieds.add(verified);
                            }

                            WardAdapter wardAdapter = new WardAdapter(getContext(), wards, total_farms, verifieds);
                            gridview.setAdapter(wardAdapter);

                        } catch (Exception e) {

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        if (volleyError == null) {
                            return;
                        }
                        Log.e(TAG, volleyError.toString());
                        System.out.println("Network Error " + volleyError);
                        Toast.makeText(getContext(), "Getting farmer failed!", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lga", lga);
                params.put("email", email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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