package com.bougainvillea.sanchit.easeparking.Fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bougainvillea.sanchit.easeparking.Adapter.VehicleTypeSpinnerAdapter;
import com.bougainvillea.sanchit.easeparking.Beans.VehicleTypeLayout;
import com.bougainvillea.sanchit.easeparking.Beans.VehicleTypeSpinnerFormat;
import com.bougainvillea.sanchit.easeparking.R;
import com.bougainvillea.sanchit.easeparking.Request.SignUpRequest;
import com.bougainvillea.sanchit.easeparking.Response.SignUpResponse;
import com.bougainvillea.sanchit.easeparking.Utils.Util;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends Fragment{

    EditText first_name, middle_name, last_name, dob_edittxt, address_1, address_2, address_3, city, no_of_vehicle, license_number, driving_license_date_edittxt, email, password, repassword, mobile;
    Button dob_btn, sign_btn, btn_click, driving_license_date_btn;
    RadioGroup radio_group_gender;
    RadioButton rb_male, rb_female, rb_other;
    Spinner state;
    CheckBox tnc_agree_btn;
    LinearLayout myview;
    static int datePickerValue;
    VehicleTypeSpinnerAdapter vehicletype_spinner_adapter;
    ArrayList<VehicleTypeSpinnerFormat> list;
    ArrayList<VehicleTypeLayout> list1;

    public SignUpFragment() {
    }

    public int getDatePickerValue() {
        return datePickerValue;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f96b69")));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("SIGN UP");
        final View rootView = inflater.inflate(R.layout.signup, container, false);
        first_name = rootView.findViewById(R.id.first_name);
        middle_name = rootView.findViewById(R.id.middle_name);
        last_name = rootView.findViewById(R.id.last_name);
        dob_edittxt = rootView.findViewById(R.id.dob_edittxt);
        address_1 = rootView.findViewById(R.id.address_1);
        address_2 = rootView.findViewById(R.id.address_2);
        address_3 = rootView.findViewById(R.id.address_3);
        city = rootView.findViewById(R.id.city);
        no_of_vehicle = rootView.findViewById(R.id.no_of_vehicle);
        license_number = rootView.findViewById(R.id.license_number);
        driving_license_date_edittxt =  rootView.findViewById(R.id.driving_license_date_edittxt);
        email = rootView.findViewById(R.id.email);
        password = rootView.findViewById(R.id.password);
        repassword = rootView.findViewById(R.id.repassword);
        dob_btn = rootView.findViewById(R.id.dob_btn);
        sign_btn = rootView.findViewById(R.id.signup);
        btn_click = rootView.findViewById(R.id.btn_tnc);
        driving_license_date_btn = rootView.findViewById(R.id.driving_license_date_btn);
        radio_group_gender = rootView.findViewById(R.id.radio_group_gender);
        rb_male = rootView.findViewById(R.id.rb_male);
        rb_female = rootView.findViewById(R.id.rb_female);
        rb_other = rootView.findViewById(R.id.rb_other);
        state = rootView.findViewById(R.id.state);
        tnc_agree_btn = rootView.findViewById(R.id.tnc_agree_btn);
        list = new ArrayList<>();
        list1 = new ArrayList<>();
        myview = rootView.findViewById(R.id.myview);
        mobile = rootView.findViewById(R.id.mobile);
        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEntries();
            }
        });
        dob_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerValue = R.id.dob_edittxt;
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "date picker");
            }
        });
        driving_license_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerValue = R.id.driving_license_date_edittxt;
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getFragmentManager(), "date picker");
            }
        });
        list.add(new VehicleTypeSpinnerFormat(R.drawable.ic_directions_car_black_16dp));
        list.add(new VehicleTypeSpinnerFormat(R.drawable.ic_directions_bike_black_16dp));
        vehicletype_spinner_adapter = new VehicleTypeSpinnerAdapter(getActivity(), R.layout.vehicle_spinner_layout, list);

        no_of_vehicle.addTextChangedListener(new TextWatcher() {
                                                 @Override
                                                 public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                 }

                                                 @Override
                                                 public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                     try{
                                                         myview.removeAllViews();
                                                         list1.clear();
                                                         int i = Integer.parseInt(no_of_vehicle.getText().toString());
                                                         for(int index = 0; index < i; index++){
                                                             LinearLayout mLinearLayout = new LinearLayout(getActivity());
                                                             mLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                                                             mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                                                             mLinearLayout.setWeightSum(3f);
                                                             Spinner select_vehicle_type = new Spinner(getActivity());
                                                             LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,0.3f);
                                                             select_vehicle_type.setLayoutParams(param);
                                                             select_vehicle_type.setBackgroundResource(R.drawable.background_signup);
                                                             select_vehicle_type.setLayoutMode(Spinner.MODE_DROPDOWN);
                                                             mLinearLayout.addView(select_vehicle_type);
                                                             select_vehicle_type.setAdapter(vehicletype_spinner_adapter);
                                                             EditText initial_no_plate = new EditText(getActivity());
                                                             initial_no_plate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_label_outline_black_24dp, 0, 0, 0);
                                                             initial_no_plate.setCompoundDrawablePadding(12);
                                                             initial_no_plate.setSingleLine();
                                                             initial_no_plate.setTextSize(15);
                                                             initial_no_plate.setPadding(8,8,8,8);
                                                             initial_no_plate.setHintTextColor(Color.GRAY);
                                                             initial_no_plate.setBackground(ContextCompat.getDrawable(getActivity(),android.R.color.transparent ));
                                                             initial_no_plate.setHint("MH");
                                                             param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,0.75f);
                                                             initial_no_plate.setLayoutParams(param);
                                                             mLinearLayout.addView(initial_no_plate);
                                                             EditText last_no_plate = new EditText(getActivity());
                                                             last_no_plate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_remove_black_24dp, 0, 0, 0);
                                                             last_no_plate.setCompoundDrawablePadding(12);
                                                             last_no_plate.setSingleLine();
                                                             last_no_plate.setTextSize(15);
                                                             last_no_plate.setPadding(8,8,8,8);
                                                             last_no_plate.setHintTextColor(Color.GRAY);
                                                             last_no_plate.setBackground(ContextCompat.getDrawable(getActivity(),android.R.color.transparent ));
                                                             last_no_plate.setHint("1234");
                                                             param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.95f);
                                                             last_no_plate.setLayoutParams(param);
                                                             mLinearLayout.addView(last_no_plate);
                                                             View mView = new View(getActivity());
                                                             mView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1));
                                                             mView.setBackgroundColor(Color.BLACK);
                                                             myview.addView(mLinearLayout);
                                                             myview.addView(mView);
                                                             list1.add(new VehicleTypeLayout(select_vehicle_type,initial_no_plate,last_no_plate));
                                                         }
                                                         int position = state.getSelectedItemPosition();

                                                         for (VehicleTypeLayout v:list1){
                                                             switch (position) {
                                                                 case 0:
                                                                     v.getState_edittxt().setText("AR");
                                                                     break;
                                                                 case 1:
                                                                     v.getState_edittxt().setText("BR");
                                                                     break;
                                                                 case 2:
                                                                     v.getState_edittxt().setText("CG");
                                                                     break;
                                                                 case 3:
                                                                     v.getState_edittxt().setText("GA");
                                                                     break;
                                                                 case 4:
                                                                     v.getState_edittxt().setText("GJ");
                                                                     break;
                                                                 case 5:
                                                                     v.getState_edittxt().setText("MH");
                                                                     break;
                                                                 case 6:
                                                                     v.getState_edittxt().setText("KA");
                                                                     break;
                                                                 case 7:
                                                                     v.getState_edittxt().setText("PB");
                                                                     break;
                                                                 case 8:
                                                                     v.getState_edittxt().setText("TN");
                                                                     break;
                                                             }
                                                         }
                                                     }
                                                     catch (Exception e){

                                                     }
                                                 }

                                                 @Override
                                                 public void afterTextChanged(Editable s) {

                                                 }
                                             }
        );

        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();
            }
        });

        return rootView;
    }

    private void showAlert() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.signup_tnc, null);
        alert.setView(view);
        alert.setTitle("TERM AND CONDITION");
        alert.setMessage("Click OK to dismiss");
        alert.setCancelable(false);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.create().show();
    }

    private void checkEntries() {
        if (validateEditText(first_name.getText().toString()) && validateEditText(middle_name.getText().toString())
                && validateEditText(last_name.getText().toString()) && validateEditText(dob_edittxt.getText().toString())
                && validateEditText(address_1.getText().toString()) && validateEditText(address_2.getText().toString())
                && validateEditText(address_3.getText().toString()) && validateEditText(city.getText().toString())
                && validateEditText(license_number.getText().toString()) && validateEditText(driving_license_date_edittxt.getText().toString())
                && validateEditText(email.getText().toString()) && validateEditText(mobile.getText().toString())
                && validateEditText(password.getText().toString()) && validateEditText(repassword.getText().toString())) {

        } else {
            Toast.makeText(getContext(), "Please Enter All Entries", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rb_male.isChecked() || rb_female.isChecked() || rb_other.isChecked()) {
        } else {
            Toast.makeText(getContext(), "Select Your Gender", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tnc_agree_btn.isChecked()) {
        } else {
            Toast.makeText(getContext(), "Please Accept The Argeement", Toast.LENGTH_SHORT).show();
            return;
        }
        for (VehicleTypeLayout v: list1) {
            if(validateEditText(v.getState_edittxt().getText().toString()) && validateEditText(v.getNumber_plate_edittxt().getText().toString())){

            }
            else{
                Toast.makeText(getContext(), "Please Enter All Vehicle Number", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        validateData();
    }

    private boolean validateEditText(String value) {
        return !value.matches("");
    }

    private int validateDob() {
        Date date1;
        int yourAge;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dob_edittxt.getText().toString());
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);
            Calendar dateOfYourBirth = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            Calendar today = Calendar.getInstance();
            yourAge = today.get(Calendar.YEAR) - dateOfYourBirth.get(Calendar.YEAR);
            dateOfYourBirth.add(Calendar.YEAR, yourAge);
            if (today.before(dateOfYourBirth)) {
                yourAge--;
            }
        } catch (ParseException e) {
            Toast.makeText(getContext(), "Invalid Date", Toast.LENGTH_SHORT).show();
            return 0;
        }
        return yourAge;
    }

    private void validateData() {
        if (password.getText().toString().equals(repassword.getText().toString())) {
        } else {
            Toast.makeText(getContext(), "Password Does Not Match", Toast.LENGTH_SHORT).show();
            password.requestFocus();
            return;
        }
        if (email.getText().toString().matches("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$")) {
        } else {
            Toast.makeText(getContext(), "Email Must Be Of Type a@b.c", Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return;
        }
        int age = validateDob();
        if (age < 18) {
            Toast.makeText(getContext(), "Your age is " + String.valueOf(age) + " And Not Allowed To Drive", Toast.LENGTH_SHORT).show();
            dob_edittxt.requestFocus();
            return;
        }
        fireUpData();
    }

    private void fireUpData() {
        signup();
    }

    private void signup() {
        SignUpRequest signupRequest = new SignUpRequest();
        signupRequest.setName(first_name.getText().toString() + " " + middle_name.getText().toString() + " " + last_name.getText().toString());
        String gender = "Other";
        switch (radio_group_gender.getCheckedRadioButtonId()) {
            case R.id.rb_male:
                gender = "Male";
                break;
            case R.id.rb_female:
                gender = "Female";
                break;
            case R.id.rb_other:
                gender = "Other";
                break;
        }
        signupRequest.setGender(gender);
        signupRequest.setEmail(email.getText().toString());
        signupRequest.setPassword(password.getText().toString());
        signupRequest.setDob(dob_edittxt.getText().toString());
        signupRequest.setMobileNo(Long.parseLong(mobile.getText().toString()));
        signupRequest.setLicenseNo(license_number.getText().toString());
        signupRequest.setLicenseValidDate(license_number.getText().toString());
        signupRequest.setAddress(address_1.getText().toString()+ " " +address_2.getText().toString()+ " " +address_3.getText().toString());
        signupRequest.setCity(city.getText().toString());
        signupRequest.setState(state.getSelectedItem().toString());
        List<SignUpRequest.VehicleBean> svb =  new ArrayList<>();
        for(VehicleTypeLayout v:list1){
            SignUpRequest.VehicleBean x = new SignUpRequest.VehicleBean();
            int position = v.getVehicle_type().getSelectedItemPosition();
            if(position == 0){
                x.setType("car");
            }
            else{
                x.setType("bike");
            }
            x.setNumber(v.getState_edittxt().getText().toString() + "-" + v.getNumber_plate_edittxt().getText().toString());
            svb.add(x);
        }
        signupRequest.setVehicle(svb);
        Call<SignUpResponse> signupResponseCall = Util.getWebservices().getSignup(signupRequest);
        signupResponseCall.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                try {
                    SignUpResponse signupResponse = response.body();
                    if (signupResponse != null) {
                        switch (signupResponse.getFlag()) {
                            case -1:
                                Toast.makeText(getContext(), signupResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                break;
                            case -2:
                                Toast.makeText(getContext(), signupResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                break;
                            case -3:
                                Toast.makeText(getContext(), signupResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                break;
                            default: {
                                openLoginFragment();
                                Snackbar.make(getActivity().findViewById(R.id.main), signupResponse.getMessage(), Snackbar.LENGTH_SHORT).show();
                            }
                            break;
                        }
                    }
                }
                catch (Exception e){

                }
            }
            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
            }
        });
    }

    private void openLoginFragment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.red));
        }
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getActivity().findViewById(R.id.rellay1).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.rellay2).setVisibility(View.VISIBLE);
            }
        };
        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new LoginFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
