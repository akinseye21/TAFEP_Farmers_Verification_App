package com.example.tafep.Fragment;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.constraintlayout.motion.widget.Debug.getLocation;
import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tafep.Adapters.AvailableFarmerAdapter;
import com.example.tafep.CreateAccount;
import com.example.tafep.MyDatabaseHelper;
import com.example.tafep.R;
import com.example.tafep.Welcome;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class FragmentVerifyFarm extends Fragment implements AvailableFarmerAdapter.OnFragmentInteractionListener, LocationListener {

    MyDatabaseHelper myDB;
    TextView txt_lga;

    Spinner spinner_ward;
    String str_ward = "";
    RelativeLayout lin1, lin5;
    LinearLayout lin2, lin3, lin4;
    ImageView back1, back2, back3;
    Button button1;
    Button available;
    Button unavailable;
    Button gotofarmpics;
    Button verify;
    Button verify_another;


    ArrayList<String> wards;
    String login_lga;
    String login_name;

    ArrayList<String> arr_tafep_num;
    ArrayList<String> arr_farmer_surname;
    ArrayList<String> arr_farmer_firstname;
    ArrayList<String> arr_farmer_phone_number;
    ArrayList<String> arr_farmer_address;
    ArrayList<String> arr_company_address;

    EditText edt_name, edt_phone, edt_address, edt_emergency, edt_crops, edt_tafepnum;

    LocationManager locationManager;
    //    String userLatitude, userLongitude;
    TextView geo1, geo2, geo3, geo4;
    TextView latlong1, latlong2, latlong3, latlong4;
    LinearLayout radar1, radar2, radar3, radar4;
    String focus = "";

    RelativeLayout layoutpicture1, layoutpicture2;
    ImageView picture1, picture2;
    Boolean bool_pic1 = false, bool_pic2 = false;
    Bitmap bitmap;

    String str_tafep = "", str_fullname = "", str_phone = "", str_emergency = "", str_created_by = "", str_lga = "", str_farmadd = "";
    String str_farmtype = "", str_farmsize = "", str_crops = "";
    String str_lat1 = "", str_long1 = "", str_lat2 = "", str_long2 = "", str_lat3 = "", str_long3 = "", str_lat4 = "", str_long4 = "";
    byte[] b_pic1, b_pic2;

    TextView currentTextView;
    double latitude, longitude;


    public FragmentVerifyFarm() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_verify_farm, container, false);




        // Get the SharedPreferences instance
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login Pref", Context.MODE_PRIVATE);
        // Retrieve the JSON string
        String json = sharedPreferences.getString("wards", null);
        login_lga = sharedPreferences.getString("lga", null);
        str_created_by = sharedPreferences.getString("email", null);
        // Convert the JSON string back to an ArrayList using Gson
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        wards = gson.fromJson(json, type);

        myDB = new MyDatabaseHelper(getActivity());

        spinner_ward = v.findViewById(R.id.ward);
        ArrayAdapter wardAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_spinner_item, R.id.tx, wards);
        spinner_ward.setAdapter(wardAdapter);
        spinner_ward.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                str_ward = spinner_ward.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        txt_lga = v.findViewById(R.id.lga);
        txt_lga.setText(login_lga);
        lin1 = v.findViewById(R.id.lin1);
        lin2 = v.findViewById(R.id.lin2);
        lin3 = v.findViewById(R.id.lin3);
        lin4 = v.findViewById(R.id.lin4);
        lin5 = v.findViewById(R.id.lin5);
        back1 = v.findViewById(R.id.back1);
        back2 = v.findViewById(R.id.back2);
        back3 = v.findViewById(R.id.back3);
        button1 = v.findViewById(R.id.continue1);
        available = v.findViewById(R.id.available);
        unavailable = v.findViewById(R.id.unavailable);
        gotofarmpics = v.findViewById(R.id.gotofarmpics);
        verify = v.findViewById(R.id.verify);
        verify_another = v.findViewById(R.id.verify_another);

        //for view 2
        edt_name = v.findViewById(R.id.name);
        edt_phone = v.findViewById(R.id.phone);
        edt_address = v.findViewById(R.id.address);
        edt_emergency = v.findViewById(R.id.emergency);
        edt_crops = v.findViewById(R.id.crops);
        edt_tafepnum = v.findViewById(R.id.tafepnum);

        //for view 3
        geo1 = v.findViewById(R.id.geo1);
        geo2 = v.findViewById(R.id.geo2);
        geo3 = v.findViewById(R.id.geo3);
        geo4 = v.findViewById(R.id.geo4);
        latlong1 = v.findViewById(R.id.latlong1);
        latlong2 = v.findViewById(R.id.latlong2);
        latlong3 = v.findViewById(R.id.latlong3);
        latlong4 = v.findViewById(R.id.latlong4);
        radar1 = v.findViewById(R.id.radar1);
        radar2 = v.findViewById(R.id.radar2);
        radar3 = v.findViewById(R.id.radar3);
        radar4 = v.findViewById(R.id.radar4);

        //for view 4
        layoutpicture1 = v.findViewById(R.id.layoutpicture1);
        layoutpicture2 = v.findViewById(R.id.layoutpicture2);
        picture1 = v.findViewById(R.id.picture1);
        picture2 = v.findViewById(R.id.picture2);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (str_ward.equals("")) {
                    Toast.makeText(getContext(), "Please select a ward!", Toast.LENGTH_SHORT).show();
                } else {
                    //request for farmer
                    requestFarmer();
                }

            }
        });
        available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hold the data of the farmer that was selected
                holdAvailableData();
                showLin3();
            }
        });
        unavailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show unavailable
                Toast.makeText(getActivity(), "Farmer Unavailable, please search for another farmer", Toast.LENGTH_LONG).show();
                lin2.setVisibility(View.GONE);
                lin1.setVisibility(View.VISIBLE);
            }
        });
        gotofarmpics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (latlong1.getText().toString().equals("0.00, 0.00")
                || latlong2.getText().toString().equals("00.0 , 00.0")
                || latlong3.getText().toString().equals("00.0 , 00.0")
                || latlong4.getText().toString().equals("00.0 , 00.0")){
                    Toast.makeText(getContext(), "Kindly get appropriate coordinates for each point", Toast.LENGTH_SHORT).show();
                }else{
                    showLin4();
                }
            }
        });
        verify_another.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start the process again from lin1
                startAgain();
            }
        });

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        //check if there is a place the user stopped
//        SharedPreferences sharedPreferences_stored = getActivity().getSharedPreferences("Available Farmer", Context.MODE_PRIVATE);
//        String tafep_me = sharedPreferences_stored.getString("tafep", "");
//        if (!tafep_me.equals("") || !sharedPreferences_stored.getAll().isEmpty()){
//            onFunctionCall(0, tafep_me);
//        }else{
//        }

        return v;
    }


    public void startAgain() {
        lin5.setVisibility(View.GONE);
        lin1.setVisibility(View.VISIBLE);
    }

    public void showLin5() {
        lin4.setVisibility(View.GONE);
        lin5.setVisibility(View.VISIBLE);

        //clear all edittext and text displayed
        edt_name.setText("");
        edt_phone.setText("");
        edt_address.setText("");
        edt_emergency.setText("");
        edt_crops.setText("");
        edt_tafepnum.setText("");

        geo1.setText("Click here to select geo-location");
        geo2.setText("Click here to select geo-location");
        geo3.setText("Click here to select geo-location");
        geo4.setText("Click here to select geo-location");

        picture1.setImageResource(R.drawable.default_image);
        picture2.setImageResource(R.drawable.default_image);

        latlong1.setText("0.00, 0.00");
        latlong2.setText("00.0 , 00.0");
        latlong3.setText("00.0 , 00.0");
        latlong4.setText("00.0 , 00.0");
    }

    public void showLin4() {
        lin3.setVisibility(View.GONE);
        lin4.setVisibility(View.VISIBLE);
        back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin3.setVisibility(View.VISIBLE);
                lin4.setVisibility(View.GONE);
            }
        });
        layoutpicture1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open camera
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // here, Permission is not granted
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 50);
                } else {
                    // here, permission is granted
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 400);
                }
            }
        });
        layoutpicture2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open camera
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // here, Permission is not granted
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 50);
                } else {
                    // here, permission is granted
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 401);
                }
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bool_pic1 && bool_pic2) {
                    savetoDB();
                } else {
                    Toast.makeText(getContext(), "Please snap 2 photos of the farm", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void savetoDB() {
        //clear the record in sharedpreference holding the stored farmer
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Available Farmer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        //create DB instance
        myDB = new MyDatabaseHelper(getActivity());
        myDB.addToTable(str_tafep, str_fullname, str_phone, str_emergency, str_created_by, str_lga, str_farmadd, str_ward,
                str_farmtype, str_farmsize, str_crops, str_lat1, str_lat2, str_lat3, str_lat4, str_long1, str_long2, str_long3,
                str_long4, b_pic1, b_pic2);

        showLin5();
    }

    public void showLin3() {
        //farm geolocation
        lin2.setVisibility(View.GONE);
        lin3.setVisibility(View.VISIBLE);
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin2.setVisibility(View.VISIBLE);
                lin3.setVisibility(View.GONE);
            }
        });

        geo1.setOnClickListener(v -> {
            currentTextView = geo1;
            requestLocationUpdates();
            str_lat1 = String.valueOf(latitude);
            str_long1 = String.valueOf(longitude);
            String str_lat11 = String.format("%.2f", latitude);
            String str_long11 = String.format("%.2f", longitude);
            currentTextView.setText(latitude+", "+longitude);
            latlong1.setText(str_lat11 + ", " + str_long11);
            radar1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green1)));
        });

        geo2.setOnClickListener(v -> {
            currentTextView = geo2;
            requestLocationUpdates();
//            currentTextView.setText(latitude+", "+longitude);
            str_lat2 = String.valueOf(latitude);
            str_long2 = String.valueOf(longitude);
            String str_lat21 = String.format("%.2f", latitude);
            String str_long21 = String.format("%.2f", longitude);
            currentTextView.setText(latitude+", "+longitude);
            latlong2.setText(str_lat21 + ", " + str_long21);
            radar2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green1)));
        });

        geo3.setOnClickListener(v -> {
            currentTextView = geo3;
            requestLocationUpdates();
//            currentTextView.setText(latitude+", "+longitude);
            str_lat3 = String.valueOf(latitude);
            str_long3 = String.valueOf(longitude);
            String str_lat31 = String.format("%.2f", latitude);
            String str_long31 = String.format("%.2f", longitude);
            currentTextView.setText(latitude+", "+longitude);
            latlong3.setText(str_lat31 + ", " + str_long31);
            radar3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green1)));
        });

        geo4.setOnClickListener(v -> {
            currentTextView = geo4;
            requestLocationUpdates();
//            currentTextView.setText(latitude+", "+longitude);
            str_lat4 = String.valueOf(latitude);
            str_long4 = String.valueOf(longitude);
            String str_lat41 = String.format("%.2f", latitude);
            String str_long41 = String.format("%.2f", longitude);
            currentTextView.setText(latitude+", "+longitude);
            latlong4.setText(str_lat41+ ", " + str_long41);
            radar4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green1)));
        });

    }


    public void requestFarmer() {

        arr_tafep_num = new ArrayList<>();
        arr_farmer_surname = new ArrayList<>();
        arr_farmer_firstname = new ArrayList<>();
        arr_farmer_phone_number = new ArrayList<>();
        arr_farmer_address = new ArrayList<>();
        arr_company_address = new ArrayList<>();

        Dialog myDialog = new Dialog(getContext());
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Requesting Farmer, please wait!");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tafepng.com/api/farm/requestfarmers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        System.out.println("Request farmer response: " + response);

                        try {
                            JSONArray json = new JSONArray(response);
                            int len = json.length();
                            for (int i = 0; i < len; i++) {
                                JSONObject jsonObject = json.getJSONObject(i);
                                String tafep_num = jsonObject.getString("farmer_id");
                                String farmer_surname = jsonObject.getString("surname");
                                String farmer_firstname = jsonObject.getString("firstname");
                                String farmer_phone = jsonObject.getString("phone_number");
                                String farm_address = jsonObject.getString("address");
                                String company_address = jsonObject.getString("company_address");

                                arr_tafep_num.add(tafep_num);
                                arr_farmer_surname.add(farmer_surname);
                                arr_farmer_firstname.add(farmer_firstname);
                                arr_farmer_phone_number.add(farmer_phone);
                                arr_farmer_address.add(farm_address);
                                arr_company_address.add(company_address);
                            }

                            showAvailableFarmers();
//                            showLin2();

                        } catch (Exception e) {

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        myDialog.dismiss();
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
                params.put("farm_lga", login_lga);
                params.put("farm_ward", str_ward);
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

    public void showAvailableFarmers() {
        Dialog myDialog = new Dialog(getContext());
        myDialog.setContentView(R.layout.custom_popup_farmerslist);

        ListView farmerslist = myDialog.findViewById(R.id.farmerslist);
        AvailableFarmerAdapter availableFarmerAdapter = new AvailableFarmerAdapter(getContext(), arr_tafep_num, arr_farmer_surname, arr_farmer_firstname, arr_farmer_phone_number, arr_farmer_address, myDialog);
        availableFarmerAdapter.setOnFragmentInteractionListener(this);
        farmerslist.setAdapter(availableFarmerAdapter);

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();
    }

    @Override
    public void onFunctionCall(int position, String tafepNum) {
        lin1.setVisibility(View.GONE);
        lin2.setVisibility(View.VISIBLE);
        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin1.setVisibility(View.VISIBLE);
                lin2.setVisibility(View.GONE);
            }
        });
        syncFarmerInfo(tafepNum);
    }

    public void syncFarmerInfo(String tafepNum) {
        Dialog myDialog = new Dialog(getActivity());
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Synchronizing farmer information, please wait!");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tafepng.com/api/farm/availablefarmer",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        System.out.println("Available farmer response: " + response);

                        try {
                            JSONObject o = new JSONObject(response);
                            String farmer = o.getString("farmer");
                            JSONObject o2 = new JSONObject(farmer);

                            str_tafep = o2.getString("farmer_id");
                            str_fullname = o2.getString("surname") + " " + o2.getString("firstname");
                            str_phone = o2.getString("phone_number");
                            str_emergency = o2.getString("phone_number");
                            str_lga = o2.getString("location_lga");
                            str_farmadd = o2.getString("farm_address");
                            str_farmtype = o2.getString("farm_type");
                            str_farmsize = o2.getString("farm_size");
                            str_crops = o2.getString("crops_cultivated");


                            String surname = o2.getString("surname");
                            String firstname = o2.getString("firstname");
                            String phone_number = o2.getString("phone_number");
                            String farm_address = o2.getString("farm_address");
                            String emergency_contact = o2.getString("phone_number");
                            String crops_cultivated = o2.getString("crops_cultivated");
                            String tafep_number = o2.getString("farmer_id");

                            edt_name.setText(surname + " " + firstname);
                            edt_phone.setText(phone_number);
                            edt_address.setText(farm_address);
                            edt_emergency.setText(emergency_contact);
                            edt_crops.setText(crops_cultivated);
                            edt_tafepnum.setText(tafep_number);
                        } catch (JSONException e) {
                            try {
                                JSONObject json = new JSONObject(response);
                                String status = json.getString("status");
                                Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
//                                lin1.setVisibility(View.VISIBLE);
//                                lin2.setVisibility(View.GONE);
                            } catch (JSONException ex) {
                                throw new RuntimeException(ex);
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        myDialog.dismiss();
                        if (volleyError == null) {
                            return;
                        }
                        Log.e(TAG, volleyError.toString());
                        System.out.println("Network Error " + volleyError);
                        Toast.makeText(getContext(), "Network Error!", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("farmer_id", tafepNum);
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


    @Override
    public void onLocationChanged(@NonNull Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
//        if (currentTextView != null) {
//
//        }
//        double latitude = location.getLatitude();
//        double longitude = location.getLongitude();
//        Toast.makeText(getContext(), "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_SHORT).show();

//        if (focus.equals("geo1")){
//            str_lat1 = String.valueOf(location.getLatitude());
//            str_long1 = String.valueOf(location.getLongitude());
//        }else if(focus.equals("geo2")){
//            str_lat2 = String.valueOf(location.getLatitude());
//            str_long2 = String.valueOf(location.getLongitude());
//        }else if(focus.equals("geo3")){
//            str_lat3 = String.valueOf(location.getLatitude());
//            str_long3 = String.valueOf(location.getLongitude());
//        }else if(focus.equals("geo4")){
//            str_lat4 = String.valueOf(location.getLatitude());
//            str_long4 = String.valueOf(location.getLongitude());
//        }
    }

    public void requestLocationUpdates() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            Toast.makeText(getContext(), "Please enable location services", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            } else {
                Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 400) {
            bitmap = (Bitmap) data.getExtras().get("data");
            picture1.setImageBitmap(bitmap);
            bool_pic1=true;

            int targetWidth = 1080;
            int targetHeight = 1920;
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

            //converting bitmap to bytearray
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            b_pic1 = baos.toByteArray();

        } else if (requestCode == 401) {
            bitmap = (Bitmap) data.getExtras().get("data");
            picture2.setImageBitmap(bitmap);
            bool_pic2=true;

            int targetWidth = 1080;
            int targetHeight = 1920;
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

            //converting bitmap to bytearray
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            b_pic2 = baos.toByteArray();
        }
    }

    public void holdAvailableData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Available Farmer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("tafep", str_tafep);
        editor.putString("fullname", str_fullname);
        editor.putString("phone", str_phone);
        editor.putString("emergency", str_emergency);
        editor.putString("lga", str_lga);
        editor.putString("farmadd", str_farmadd);
        editor.putString("farmtype", str_farmtype);
        editor.putString("farmsize", str_farmsize);
        editor.putString("crop", str_crops);
        editor.putString("createdby", str_created_by);
        // Commit the changes
        editor.apply();

    }

}