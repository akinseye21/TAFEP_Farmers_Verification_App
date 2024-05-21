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
import com.example.tafep.Adapters.WardAdapter;
import com.example.tafep.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class FragmentAchievement extends Fragment {

    String username, lga, email;
    TextView txt_username;
    int totals_farms;
    int totals_wards;

    TextView farmverified, wardverified;
    TextView txtbalance;


    public FragmentAchievement() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_achievement, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("fullname", "");
        lga = sharedPreferences.getString("lga", "");
        email = sharedPreferences.getString("email", "");

        totals_farms = 0;
        totals_wards = 0;

        txt_username = (TextView) v.findViewById(R.id.txtusername);
        txt_username.setText(username);
        farmverified = v.findViewById(R.id.farmsverified);
        wardverified = v.findViewById(R.id.wardsverified);
        txtbalance = v.findViewById(R.id.txtbalance);

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
                            String my_verified_farms = json.getString("my_verified_farms");
                            JSONArray jsonArray = new JSONArray(my_verified_farms);
                            int len = jsonArray.length();
                            for (int i=0; i<len; i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int totals = jsonObject.getInt("totals");

                                totals_farms = totals_farms + totals;
                            }
                            farmverified.setText(String.valueOf(totals_farms));
                            int balanceCalc = 1000*totals_farms;
                            txtbalance.setText("N "+balanceCalc);


                            String my_verified_farms_wards = json.getString("my_verified_farms_wards");
                            JSONArray jsonArray2 = new JSONArray(my_verified_farms_wards);
                            for (int j=0; j<jsonArray2.length(); j++){
                                JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
                                int totals = jsonObject2.getInt("totals");

                                totals_wards = totals_wards + totals;
                            }
                            wardverified.setText(String.valueOf(totals_wards));


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