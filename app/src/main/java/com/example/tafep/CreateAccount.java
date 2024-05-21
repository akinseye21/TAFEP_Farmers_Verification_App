package com.example.tafep;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAccount extends AppCompatActivity {

    ImageView back;
    ScrollView view1, view2;

    EditText edt_email, edt_fullname, edt_phone, edt_address, edt_password, edt_confirm_pass;
    String str_email="", str_fullname="", str_phone="", str_address="", str_password="", str_confirm_pass="";
    Boolean bool_email=false, bool_fullname=false, bool_phone=false, bool_address=false, bool_password=false, bool_confirmpass=false;
    EditText edt_accountnumber, edt_accountname;
    String str_accountnumber="", str_accountname="";
    Boolean bool_accountnumber=false, bool_accountname=false;
    Spinner spinner_lga;
    String str_lga="";
    Boolean bool_lga=false;
    Spinner spinner_bankname;
    String str_bank_name="";
    Boolean bool_bankname=false;
    Button btn_continue1, btn_continue2;

    ArrayList<String> bank_list = new ArrayList<>();
    ArrayAdapter<String> bankAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back = findViewById(R.id.back);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view1.getVisibility() == View.VISIBLE){
                    Intent i = new Intent(CreateAccount.this, MainActivity2.class);
                    startActivity(i);
                }else if(view2.getVisibility() == View.VISIBLE){
                    view2.setVisibility(View.GONE);
                    view1.setVisibility(View.VISIBLE);
                }
            }
        });

        edt_email = findViewById(R.id.email);
        edt_fullname = findViewById(R.id.name);
        edt_phone = findViewById(R.id.phone);
        edt_address = findViewById(R.id.address);
        edt_password = findViewById(R.id.password);
        edt_confirm_pass = findViewById(R.id.confirmpass);
        spinner_lga = findViewById(R.id.lga);
        btn_continue1 = findViewById(R.id.continue1);
        btn_continue2 = findViewById(R.id.continue2);
        edt_accountnumber = findViewById(R.id.accountnumber);
        edt_accountname = findViewById(R.id.accountname);
        spinner_bankname = findViewById(R.id.bankname);

        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Pattern pattern;
                Matcher matcher;
                String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                pattern = Pattern.compile(EMAIL_PATTERN);
                CharSequence cs = (CharSequence) editable;
                matcher = pattern.matcher(cs);
                if(!(matcher.matches()==true)){
                    str_email="";
                    bool_email=false;
                    edt_email.setError("Invalid");
                }else{
                    str_email=edt_email.getText().toString();
                    bool_email=true;
                }
            }
        });
        edt_fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_fullname.getText().toString().length()>3){
                    str_fullname = edt_fullname.getText().toString();
                    bool_fullname = true;
                }else{
                    bool_fullname = false;
                    edt_fullname.setError("Invalid");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_phone.getText().toString().length()>10){
                    str_phone = edt_phone.getText().toString();
                    bool_phone = true;
                }else{
                    bool_phone = false;
                    edt_phone.setError("Invalid");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_address.getText().toString().length()>5){
                    str_address = edt_address.getText().toString();
                    bool_address = true;
                }else{
                    bool_address = false;
                    edt_address.setError("Invalid");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_password.getText().toString().length()>5){
                    str_password = edt_password.getText().toString();
                    bool_password = true;
                }else{
                    bool_password = false;
                    edt_password.setError("at least 6 characters");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_confirm_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_confirm_pass.getText().toString().equals(str_password)){
                    str_confirm_pass = edt_confirm_pass.getText().toString();
                    bool_confirmpass = true;
                }else{
                    bool_confirmpass = false;
                    edt_confirm_pass.setError("password not matching");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        String lga[] = {"Select your LGA","ARDO-KOLA", "BALI", "DONGA", "GASHAKA", "GASSOL", "IBI", "JALINGO", "KARIM-LAMIDO",
                "KURMI", "LAU", "SARDAUNA", "TAKUM", "USSA", "WUKARI", "YORRO", "ZING"};
        ArrayAdapter lgaAdapter = new ArrayAdapter<>(this, R.layout.simple_spinner_item, R.id.tx, lga);
        spinner_lga.setAdapter(lgaAdapter);
        spinner_lga.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position==0){
                    bool_lga = false;
                }else {
                    str_lga=spinner_lga.getSelectedItem().toString();
                    bool_lga = true;
                }
                
                
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        edt_accountnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_accountnumber.getText().toString().length() == 10){
                    str_accountnumber = edt_accountnumber.getText().toString();
                    bool_accountnumber = true;
                }else{
                    bool_accountnumber = false;
                    edt_accountnumber.setError("Invalid");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edt_accountname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edt_accountname.getText().toString().length()>3){
                    str_accountname = edt_accountname.getText().toString();
                    bool_accountname = true;
                }else{
                    bool_accountname = false;
                    edt_accountname.setError("Invalid");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        btn_continue1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bool_email && bool_fullname && bool_phone && bool_address && bool_password && bool_confirmpass && bool_lga){
                    view1.setVisibility(View.GONE);
                    view2.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(CreateAccount.this, "One or more fields are invalid entries", Toast.LENGTH_SHORT).show();
                }
                
            }
        });
        btn_continue2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bool_accountnumber && bool_accountname && bool_bankname){
                    //send to server
                    signup();
                }else{
                    Toast.makeText(CreateAccount.this, "One or more fields are invalid entries", Toast.LENGTH_SHORT).show();
                }


            }
        });



        //add all banks
        bank_list.add("Select your Bank");
        bank_list.add("Access Bank Plc");
        bank_list.add("Citibank Nigeria Limited");
        bank_list.add("Ecobank Nigeria Plc");
        bank_list.add("Fidelity Bank Plc");
        bank_list.add("First Bank Nigeria Limited");
        bank_list.add("First City Monument Bank Plc");
        bank_list.add("Globus Bank Limited");
        bank_list.add("Guaranty Trust Bank Plc");
        bank_list.add("Heritage Banking Company Ltd.");
        bank_list.add("Keystone Bank Limited");
        bank_list.add("Parallex Bank Ltd");
        bank_list.add("Polaris Bank Plc");
        bank_list.add("Premium Trust Bank");
        bank_list.add("Providus Bank");
        bank_list.add("Stanbic IBTC Bank Plc");
        bank_list.add("Standard Chartered Bank Nigeria Ltd.");
        bank_list.add("Sterling Bank Plc");
        bank_list.add("SunTrust Bank Nigeria Limited");
        bank_list.add("Titan Trust Bank Ltd");
        bank_list.add("Union Bank of Nigeria Plc");
        bank_list.add("United Bank For Africa Plc");
        bank_list.add("Unity  Bank Plc");
        bank_list.add("Wema Bank Plc");
        bank_list.add("Zenith Bank Plc");

        bankAdapter = new ArrayAdapter<>(CreateAccount.this, R.layout.simple_spinner_item, R.id.tx, bank_list);
        spinner_bankname.setAdapter(bankAdapter);
        spinner_bankname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position==0){
                    bool_bankname = false;
                }else {
                    str_bank_name=spinner_bankname.getSelectedItem().toString();
                    bool_bankname = true;
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


    }

    private void signup() {
        Dialog myDialog = new Dialog(CreateAccount.this);
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Signing you up, please wait!");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://tafepng.com/api/farm/add_enumerators",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        myDialog.dismiss();
                        System.out.println("Signup response: " + response);

                        if (response.equals("User created successfully!")){
                            Toast.makeText(CreateAccount.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(CreateAccount.this, Welcome.class);
                            i.putExtra("fullname", str_fullname);
                            i.putExtra("email", str_email);
                            i.putExtra("password", str_password);
                            startActivity(i);
                        }else{
                            Toast.makeText(CreateAccount.this, "Registration failed, please contact administrator.", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(CreateAccount.this, "Registration failed. Please ensure your email, phone number and account information have not been used to signup before", Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("fullname", str_fullname);
                params.put("email", str_email);
                params.put("phonenumber", str_phone);
                params.put("address", str_address);
                params.put("lga", str_lga);
                params.put("password", str_password);
                params.put("accountname", str_accountname);
                params.put("bankname", str_bank_name);
                params.put("accountumber", str_accountnumber);
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