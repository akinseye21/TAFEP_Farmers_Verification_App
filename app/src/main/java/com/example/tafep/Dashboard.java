package com.example.tafep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tafep.Fragment.FragmentAchievement;
import com.example.tafep.Fragment.FragmentDashboard;
import com.example.tafep.Fragment.FragmentHome;
import com.example.tafep.Fragment.FragmentProfile;
import com.example.tafep.Fragment.FragmentVerifyFarm;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Dashboard extends AppCompatActivity {

    TabLayout tabLayout;
    static ViewPager viewPager;

    static ArrayList<String> wards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Check if the location permission is granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    101);
        } else {
            // Permission is already granted, proceed with your operations
            // For example, start location updates or show the user's location on the map
        }

        viewPager = findViewById(R.id.viewpager);
        addTabs(viewPager);
        tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(
                new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        int tabIconColor = ContextCompat.getColor(Dashboard.this, R.color.green1);
                        Objects.requireNonNull(tab.getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        super.onTabUnselected(tab);
                        int tabIconColor = ContextCompat.getColor(Dashboard.this, R.color.white);
                        Objects.requireNonNull(tab.getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                    }


                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
//                        super.onTabReselected(tab);
                        if(tab.getPosition() == 0){
//                            Intent j = new Intent(getApplicationContext(), FeedsDashboard.class);
//                            j.putExtra("email", got_email);
//                            j.putExtra("sent from", "clear error");
//                            startActivity(j);
//                            finish();
                        }
                    }
                }
        );

        setupTabIcons();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.home);
        tabLayout.getTabAt(1).setIcon(R.drawable.achievement);
        tabLayout.getTabAt(2).setIcon(R.drawable.verifyfarm);
        tabLayout.getTabAt(3).setIcon(R.drawable.dashboard);
        tabLayout.getTabAt(4).setIcon(R.drawable.sync);
    }

    private void addTabs(ViewPager viewPager) {
        Dashboard.ViewPagerAdapter adapter = new Dashboard.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentHome(), "Home");
        adapter.addFrag(new FragmentAchievement(), "Achievement");
        adapter.addFrag(new FragmentVerifyFarm(), "Verify Farm");
        adapter.addFrag(new FragmentDashboard(), "Dashboard");
        adapter.addFrag(new FragmentProfile(), "Sync");
        viewPager.setAdapter(adapter);
    }

    public static void navigateFragment(int position){
        viewPager.setCurrentItem(position);
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Get the SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("Available Farmer", Context.MODE_PRIVATE);
        String tafep = sharedPreferences.getString("tafep", "");
        String fullname = sharedPreferences.getString("fullname", "");
        String phone = sharedPreferences.getString("phone", "");
        String emergency = sharedPreferences.getString("emergency", "");
        String lga = sharedPreferences.getString("lga", "");
        String farmadd = sharedPreferences.getString("farmadd", "");
        String farmtype = sharedPreferences.getString("farmtype", "");
        String farmsize = sharedPreferences.getString("farmsize", "");
        String crop = sharedPreferences.getString("crop", "");
        String createdby = sharedPreferences.getString("createdby", "");

        if (!tafep.equals("")){
            //show dialog to continue
            Dialog myDialog = new Dialog(Dashboard.this);
            myDialog.setContentView(R.layout.custom_popup_stored_record);
            TextView user = myDialog.findViewById(R.id.user);
            user.setText("Hi");
            TextView farmername = myDialog.findViewById(R.id.farmername);
            farmername.setText("Farmer name: "+fullname);
            Button continued = myDialog.findViewById(R.id.continued);
            Button ignore = myDialog.findViewById(R.id.ignore);

            continued.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //go to tab2
                    myDialog.dismiss();
//                    FragmentVerifyFarm fragmentVerifyFarm = new FragmentVerifyFarm();
//                    View fragmentView = fragmentVerifyFarm.getView();
//                    if (fragmentView != null) {
//                        // Reference views within the fragment
//                        LinearLayout lin1 = fragmentView.findViewById(R.id.lin1);
//                        LinearLayout lin2 = fragmentView.findViewById(R.id.lin2);
//                        ImageView back1 = fragmentView.findViewById(R.id.back1);
//                        // Now you can use the textView
//                        lin1.setVisibility(View.GONE);
//                        lin2.setVisibility(View.VISIBLE);
//                        back1.setVisibility(View.GONE);
//                    }
//                    fragmentVerifyFarm.onFunctionCall(0, tafep);
//                    fragmentVerifyFarm.syncFarmerInfo(tafep);
                    navigateFragment(2);

                }
            });
            ignore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = getSharedPreferences("Available Farmer", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    myDialog.dismiss();
                }
            });

            myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            myDialog.setCanceledOnTouchOutside(false);
            myDialog.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your operations
                Toast.makeText(this, "Location Permission is granted!", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied, handle accordingly (e.g., show a message or disable location-based features)
                Toast.makeText(this, "Location is disabled. Go to settings to enable it", Toast.LENGTH_SHORT).show();
            }
        }
    }
}