package com.example.tafep.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tafep.R;

import java.util.ArrayList;

public class AvailableFarmerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> tafepNum;
    private ArrayList<String> farmer_surname;
    private ArrayList<String> farmer_firstname;
    private ArrayList<String> farmer_phone_number;
    private ArrayList<String> farmer_address;
    private Dialog myDialog;

    private OnFragmentInteractionListener mListener;

    // Interface for interaction with the fragment
    public interface OnFragmentInteractionListener {
        void onFunctionCall(int position, String s);
    }

    // Method to set the listener
    public void setOnFragmentInteractionListener(OnFragmentInteractionListener listener) {
        mListener = listener;
    }



    public AvailableFarmerAdapter(Context context, ArrayList<String> tafepNum, ArrayList<String> farmer_surname, ArrayList<String> farmer_firstname,
                                  ArrayList<String> farmer_phone_number, ArrayList<String> farmer_address, Dialog myDialog) {
        this.context = context;
        this.tafepNum = tafepNum;
        this.farmer_surname = farmer_surname;
        this.farmer_firstname = farmer_firstname;
        this.farmer_phone_number = farmer_phone_number;
        this.farmer_address = farmer_address;
        this.myDialog = myDialog;
    }

    @Override
    public int getCount() {
        return tafepNum.size();
    }

    @Override
    public Object getItem(int i) {
        return tafepNum.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_available_farmers, parent, false);
        }

        RelativeLayout card = convertView.findViewById(R.id.card);
        TextView txt_name = convertView.findViewById(R.id.name);
        TextView txt_tafepnum = convertView.findViewById(R.id.tafepnum);
        TextView txt_phone = convertView.findViewById(R.id.phoneNumber);
        TextView txt_address = convertView.findViewById(R.id.address);

        txt_name.setText(farmer_firstname.get(i)+" "+farmer_surname.get(i));
        txt_tafepnum.setText("TAFEP Num: "+tafepNum.get(i));
        txt_phone.setText(farmer_phone_number.get(i));
        txt_address.setText(farmer_address.get(i));
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
                if (mListener != null) {
                    mListener.onFunctionCall(i, tafepNum.get(i));
                }
            }
        });


        return convertView;
    }
}
