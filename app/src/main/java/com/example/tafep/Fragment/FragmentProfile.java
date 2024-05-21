package com.example.tafep.Fragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tafep.Adapters.DBAdapter;
import com.example.tafep.MyDatabaseHelper;
import com.example.tafep.R;

import java.util.ArrayList;

public class FragmentProfile extends Fragment {


    ListView listView;
    TextView nodata;
    MyDatabaseHelper myDB;

    ArrayList<Integer> arr_id = new ArrayList<>();
    ArrayList<String> arr_tafep = new ArrayList<>();
    ArrayList<String> arr_fullname = new ArrayList<>();
    ArrayList<String> arr_phone = new ArrayList<>();
    ArrayList<String> arr_emergency = new ArrayList<>();
    ArrayList<String> arr_createdby = new ArrayList<>();
    ArrayList<String> arr_lga = new ArrayList<>();
    ArrayList<String> arr_farmadd = new ArrayList<>();
    ArrayList<String> arr_ward = new ArrayList<>();
    ArrayList<String> arr_farmtype = new ArrayList<>();
    ArrayList<String> arr_farmsize = new ArrayList<>();
    ArrayList<String> arr_crop = new ArrayList<>();
    ArrayList<String> arr_lat1 = new ArrayList<>();
    ArrayList<String> arr_lat2 = new ArrayList<>();
    ArrayList<String> arr_lat3 = new ArrayList<>();
    ArrayList<String> arr_lat4 = new ArrayList<>();
    ArrayList<String> arr_long1 = new ArrayList<>();
    ArrayList<String> arr_long2 = new ArrayList<>();
    ArrayList<String> arr_long3 = new ArrayList<>();
    ArrayList<String> arr_long4 = new ArrayList<>();
    ArrayList<byte[]> arr_pic1 = new ArrayList<>();
    ArrayList<byte[]> arr_pic2 = new ArrayList<>();

    public FragmentProfile() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        listView = v.findViewById(R.id.listview_farmers);
        nodata = v.findViewById(R.id.nodata);

        myDB = new MyDatabaseHelper(getActivity());

        //get the number of row in the DB
        if (myDB.getRecordCount()==0){
            nodata.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else{
            //get all the records
            Cursor cursor = myDB.readAllData();
            if (cursor != null && cursor.moveToFirst()) {
                arr_id.clear();
                arr_tafep.clear();
                arr_fullname.clear();
                arr_phone.clear();
                arr_emergency.clear();
                arr_createdby.clear();
                arr_lga.clear();
                arr_farmadd.clear();
                arr_ward.clear();
                arr_farmtype.clear();
                arr_farmsize.clear();
                arr_crop.clear();
                arr_lat1.clear();
                arr_lat2.clear();
                arr_lat3.clear();
                arr_lat4.clear();
                arr_long1.clear();
                arr_long2.clear();
                arr_long3.clear();
                arr_long4.clear();
                arr_pic1.clear();
                arr_pic2.clear();

                do {
                    arr_id.add(cursor.getInt(0));
                    arr_tafep.add(cursor.getString(1));
                    arr_fullname.add(cursor.getString(2));
                    arr_phone.add(cursor.getString(3));
                    arr_emergency.add(cursor.getString(4));
                    arr_createdby.add(cursor.getString(5));
                    arr_lga.add(cursor.getString(6));
                    arr_farmadd.add(cursor.getString(7));
                    arr_ward.add(cursor.getString(8));
                    arr_farmtype.add(cursor.getString(9));
                    arr_farmsize.add(cursor.getString(10));
                    arr_crop.add(cursor.getString(11));
                    arr_lat1.add(cursor.getString(12));
                    arr_lat2.add(cursor.getString(13));
                    arr_lat3.add(cursor.getString(14));
                    arr_lat4.add(cursor.getString(15));
                    arr_long1.add(cursor.getString(16));
                    arr_long2.add(cursor.getString(17));
                    arr_long3.add(cursor.getString(18));
                    arr_long4.add(cursor.getString(19));
                    arr_pic1.add(cursor.getBlob(20));
                    arr_pic2.add(cursor.getBlob(21));
                }while (cursor.moveToNext());
            }
        }

        //add adapter class
        DBAdapter dbAdapter = new DBAdapter(getActivity(), arr_id, arr_tafep, arr_fullname, arr_phone, arr_emergency,
                arr_createdby, arr_lga, arr_farmadd, arr_ward, arr_farmtype, arr_farmsize, arr_crop, arr_lat1, arr_lat2,
                arr_lat3, arr_lat4, arr_long1, arr_long2, arr_long3, arr_long4, arr_pic1, arr_pic2);
        listView.setAdapter(dbAdapter);

        return v;
    }
}