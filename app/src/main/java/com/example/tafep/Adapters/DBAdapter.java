package com.example.tafep.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.tafep.MyDatabaseHelper;
import com.example.tafep.R;
import com.example.tafep.VolleyMultipartRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBAdapter extends BaseAdapter {

    private RequestQueue rQueue;
    private Context context;
    ArrayList<Integer> arr_id;
    ArrayList<String> arr_tafep;
    ArrayList<String> arr_fullname;
    ArrayList<String> arr_phone;
    ArrayList<String> arr_emergency;
    ArrayList<String> arr_createdby;
    ArrayList<String> arr_lga;
    ArrayList<String> arr_farmadd;
    ArrayList<String> arr_ward;
    ArrayList<String> arr_farmtype;
    ArrayList<String> arr_farmsize;
    ArrayList<String> arr_crop;
    ArrayList<String> arr_lat1;
    ArrayList<String> arr_lat2;
    ArrayList<String> arr_lat3;
    ArrayList<String> arr_lat4;
    ArrayList<String> arr_long1;
    ArrayList<String> arr_long2;
    ArrayList<String> arr_long3;
    ArrayList<String> arr_long4;
    ArrayList<byte[]> arr_pic1;
    ArrayList<byte[]> arr_pic2;



    public DBAdapter(Context context, ArrayList<Integer> arr_id, ArrayList<String> arr_tafep, ArrayList<String> arr_fullname,
                     ArrayList<String> arr_phone, ArrayList<String> arr_emergency, ArrayList<String> arr_createdby, ArrayList<String> arr_lga,
                     ArrayList<String> arr_farmadd, ArrayList<String> arr_ward, ArrayList<String> arr_farmtype, ArrayList<String> arr_farmsize,
                     ArrayList<String> arr_crop, ArrayList<String> arr_lat1, ArrayList<String> arr_lat2, ArrayList<String> arr_lat3,
                     ArrayList<String> arr_lat4, ArrayList<String> arr_long1, ArrayList<String> arr_long2, ArrayList<String> arr_long3,
                     ArrayList<String> arr_long4, ArrayList<byte[]> arr_pic1, ArrayList<byte[]> arr_pic2){

        this.context = context;
        this.arr_id = arr_id;
        this.arr_tafep = arr_tafep;
        this.arr_fullname = arr_fullname;
        this.arr_phone = arr_phone;
        this.arr_emergency = arr_emergency;
        this.arr_createdby = arr_createdby;
        this.arr_lga = arr_lga;
        this.arr_farmadd = arr_farmadd;
        this.arr_ward = arr_ward;
        this.arr_farmtype = arr_farmtype;
        this.arr_farmsize = arr_farmsize;
        this.arr_crop = arr_crop;
        this.arr_lat1 = arr_lat1;
        this.arr_lat2 = arr_lat2;
        this.arr_lat3 = arr_lat3;
        this.arr_lat4 = arr_lat4;
        this.arr_long1 = arr_long1;
        this.arr_long2 = arr_long2;
        this.arr_long3 = arr_long3;
        this.arr_long4 = arr_long4;
        this.arr_pic1 = arr_pic1;
        this.arr_pic2 = arr_pic2;

    }

    @Override
    public int getCount() {
        return arr_id.size();
    }

    @Override
    public Object getItem(int i) {
        return arr_id.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_saved_farmers, parent, false);
        }

        MyDatabaseHelper myDB = new MyDatabaseHelper(context);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView phone = (TextView) convertView.findViewById(R.id.phone);
        TextView tafep = (TextView) convertView.findViewById(R.id.tafepNumber);
        Button sync = convertView.findViewById(R.id.sync);
        ProgressBar progressBar = convertView.findViewById(R.id.progressBar);
        ImageView check = convertView.findViewById(R.id.check);

        name.setText(arr_fullname.get(i));
        phone.setText(arr_phone.get(i));
        tafep.setText(arr_tafep.get(i));
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sync.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, "https://tafepng.com/api/farm/addfarm",
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                                System.out.println("Server response "+new String(response.data));
                                rQueue.getCache().clear();

                                if (new String(response.data).equals("success")){
                                    //change to checkmark
                                    progressBar.setVisibility(View.GONE);
                                    check.setVisibility(View.VISIBLE);
                                    //delete record from DB
                                    myDB.deleteRecord(arr_id.get(i));
                                    Toast.makeText(context, "Synchronisation completed!", Toast.LENGTH_SHORT).show();
//                                        count_local.setText(String.valueOf(myDB.getRecordCount()));
                                }else {
                                    progressBar.setVisibility(View.GONE);
                                    sync.setVisibility(View.VISIBLE);
                                    Toast.makeText(context, "Error syncing, please try again", Toast.LENGTH_SHORT).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("Server error "+error);
                                progressBar.setVisibility(View.GONE);
                                sync.setVisibility(View.VISIBLE);
                                Toast.makeText(context, "Error, please check for good internet connectivity", Toast.LENGTH_SHORT).show();
//                                myDialog.dismiss();
                            }
                        }) {

                    /*
                     * If you want to add more parameters with the image
                     * you can do it here
                     * here we have only one parameter with the image
                     * which is tags
                     * */
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("tafep", arr_tafep.get(i));
                        params.put("fullname", arr_fullname.get(i));
                        params.put("phone_number", arr_phone.get(i));
                        params.put("emergency_phone_number", arr_emergency.get(i));
                        params.put("created_by", arr_createdby.get(i));
                        params.put("location_lga", arr_lga.get(i));
                        params.put("farm_address", arr_farmadd.get(i));
                        params.put("ward", arr_ward.get(i));
                        params.put("farm_type", arr_farmtype.get(i));
                        params.put("farm_size", arr_farmsize.get(i));
                        params.put("crops_cultivated", arr_crop.get(i));
                        params.put("lat1", arr_lat1.get(i));
                        params.put("lat2", arr_lat2.get(i));
                        params.put("lat3", arr_lat3.get(i));
                        params.put("lat4", arr_lat4.get(i));
                        params.put("lng1", arr_long1.get(i));
                        params.put("lng2", arr_long2.get(i));
                        params.put("lng3", arr_long3.get(i));
                        params.put("lng4", arr_long4.get(i));
                        return params;
                    }

                    /*
                     *pass files using below method
                     * */
                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        params.put("picture1", new DataPart("picture" , arr_pic1.get(i)));
                        params.put("picture2", new DataPart("fingerprint" , arr_pic2.get(i)));
                        params.put("picture3", new DataPart("signature" , arr_pic1.get(i)));
                        return params;
                    }
                };


                volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                rQueue = Volley.newRequestQueue(context);
                rQueue.add(volleyMultipartRequest);
            }
        });


        return convertView;
    }
}
