package com.example.tafep.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tafep.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WardAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> wardName;
    private ArrayList<String> totalFarms;
    private ArrayList<String> verified;

    public WardAdapter(Context context, ArrayList<String> wardName, ArrayList<String> totalFarms, ArrayList<String> verified) {
        this.context = context;
        this.wardName = wardName;
        this.totalFarms = totalFarms;
        this.verified = verified;
    }

    @Override
    public int getCount() {
        return wardName.size();
    }

    @Override
    public Object getItem(int i) {
        return wardName.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_ward_num, parent, false);
        }

        TextView txt_wardname = convertView.findViewById(R.id.wardname);
        TextView txt_totalfarm = convertView.findViewById(R.id.totalfarm);
        TextView txt_verifiedfarm = convertView.findViewById(R.id.verifiedfarm);

        txt_wardname.setText(wardName.get(i));
        txt_totalfarm.setText(totalFarms.get(i));
        txt_verifiedfarm.setText(verified.get(i));


        return convertView;
    }
}
