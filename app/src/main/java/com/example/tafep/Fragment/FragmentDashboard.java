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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tafep.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentDashboard extends Fragment {

    ArrayList<String> arr_location = new ArrayList<>();
    ArrayList<Integer> arr_total = new ArrayList<>();

    ArrayList<String> arr_location_highest_farmer = new ArrayList<>();
    ArrayList<Integer> arr_total_highest_farmer = new ArrayList<>();

    String username, lga, email;
    TextView highestFarmerLocation1, highestFarmerLocation2, highestFarmerLocation3;
    TextView highestFarmerTotal1, highestFarmerTotal2, highestFarmerTotal3;
    TextView highestLoc1, highestLoc2, highestLoc3;
    TextView highestLocNum1, highestLocNum2, highestLocNum3;

    public FragmentDashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        arr_location.clear();
        arr_total.clear();
        arr_location_highest_farmer.clear();
        arr_total_highest_farmer.clear();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("fullname", "");
        lga = sharedPreferences.getString("lga", "");
        email = sharedPreferences.getString("email", "");

        highestFarmerLocation1 = v.findViewById(R.id.highest_farmer_location1);
        highestFarmerLocation2 = v.findViewById(R.id.highest_farmer_location2);
        highestFarmerLocation3 = v.findViewById(R.id.highest_farmer_location3);
        highestFarmerTotal1 = v.findViewById(R.id.highest_farmer_total1);
        highestFarmerTotal2 = v.findViewById(R.id.highest_farmer_total2);
        highestFarmerTotal3 = v.findViewById(R.id.highest_farmer_total3);

        highestLoc1 = v.findViewById(R.id.highest_loc1);
        highestLoc2 = v.findViewById(R.id.highest_loc2);
        highestLoc3 = v.findViewById(R.id.highest_loc3);
        highestLocNum1 = v.findViewById(R.id.highest_loc_num1);
        highestLocNum2 = v.findViewById(R.id.highest_loc_num2);
        highestLocNum3 = v.findViewById(R.id.highest_loc_num3);

        getFarmStats();



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
                            String verified_farms = json.getString("verified_farms");
                            String top_lga = json.getString("top_lga");

                            JSONArray jsonArray = new JSONArray(verified_farms);
                            int len = jsonArray.length();
                            for (int i=0; i<len; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String location = jsonObject.getString("location_lga");
                                int total = jsonObject.getInt("totals");

                                arr_location.add(location);
                                arr_total.add(total);
                            }
                            highestFarmerLocation1.setText(arr_location.get(0));
                            highestFarmerLocation2.setText(arr_location.get(1));
                            highestFarmerLocation3.setText(arr_location.get(2));
                            highestFarmerTotal1.setText(String.valueOf(arr_total.get(0)));
                            highestFarmerTotal2.setText(String.valueOf(arr_total.get(1)));
                            highestFarmerTotal3.setText(String.valueOf(arr_total.get(2)));
                            System.out.println("Location: " + arr_location);
                            System.out.println("Total: " + arr_total);

                            //for LGA with highest farmers
                            JSONArray jsonArray2 = new JSONArray(top_lga);
                            for (int j=0; j<jsonArray2.length(); j++){
                                JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
                                String location = jsonObject2.getString("location_lga");
                                int total = jsonObject2.getInt("totals");

                                arr_location_highest_farmer.add(location);
                                arr_total_highest_farmer.add(total);
                            }
                            highestLoc1.setText(arr_location_highest_farmer.get(0));
                            highestLoc2.setText(arr_location_highest_farmer.get(1));
                            highestLoc3.setText(arr_location_highest_farmer.get(2));
                            highestLocNum1.setText(String.valueOf(arr_total_highest_farmer.get(0)));
                            highestLocNum2.setText(String.valueOf(arr_total_highest_farmer.get(1)));
                            highestLocNum3.setText(String.valueOf(arr_total_highest_farmer.get(2)));

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