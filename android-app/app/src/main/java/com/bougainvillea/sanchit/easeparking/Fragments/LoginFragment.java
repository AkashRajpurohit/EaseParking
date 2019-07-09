package com.bougainvillea.sanchit.easeparking.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.bougainvillea.sanchit.easeparking.Activity.ContractorActivity;
import com.bougainvillea.sanchit.easeparking.Activity.CustomerActivity;
import com.bougainvillea.sanchit.easeparking.Beans.CustomPosition;
import com.bougainvillea.sanchit.easeparking.R;
import com.bougainvillea.sanchit.easeparking.Request.LoginRequest;
import com.bougainvillea.sanchit.easeparking.Response.LoginResponse;
import com.bougainvillea.sanchit.easeparking.Utils.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    Button signup, login;
    EditText login_edt, password_edt;
    View rootView;
    ArrayList<CustomPosition> final_Custom_Coordinates, final_Free_Coordinates;
    String usertype;

    public LoginFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        rootView = inflater.inflate(R.layout.login, container, false);
        signup = rootView.findViewById(R.id.signup);
        login = rootView.findViewById(R.id.login);
        final_Custom_Coordinates = new ArrayList<>();
        final_Free_Coordinates = new ArrayList<>();
        login_edt = rootView.findViewById(R.id.login_edt);
        password_edt = rootView.findViewById(R.id.password_edt);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpFragment();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapActivity();
            }
        });
        return rootView;
    }

    private void openSignUpFragment() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.red));
        }
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new SignUpFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void openMapActivity() {
        checkUser();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getActivity().findViewById(R.id.rellay1).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.rellay2).setVisibility(View.VISIBLE);
            }
        };
        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash
    }

    private void checkUser() {
        final LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail((login_edt.getText().toString()));
        loginRequest.setPassword(password_edt.getText().toString());
//        if(login_edt.getText().toString().equals("contractor@gmail.com") && password_edt.getText().toString().equals("contractor")){
//            Intent intent = new Intent(getContext(), ContractorActivity.class);
//            startActivity(intent);
//        }
//        if(login_edt.getText().toString().equals("user@gmail.com") && password_edt.getText().toString().equals("user")){
//            Intent intent = new Intent(getContext(), CustomerActivity.class);
//            startActivity(intent);
//        }
        Call<LoginResponse> loginResponseCall = Util.getWebservices().getlogin(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                try {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse.getData() != null) {
                        if(loginResponse.isSuccess()) {
                            usertype = loginResponse.getData().getUser_type();
                            String customer_id = loginResponse.getData().getUser_id();
                            String customer_name = loginResponse.getData().getName();
                            if (usertype.equals("customer")) {
                                SharedPreferences preferencesReader = getActivity().getSharedPreferences("Customer", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferencesReader.edit();
                                editor.putString("Customer ID", customer_id);
                                editor.putString("Customer Name", customer_name);
                                Set<String> set = new HashSet<String>();
                                for (int i = 0; i < loginResponse.getData().getVehicle().size(); i++) {
                                    set.add(loginResponse.getData().getVehicle().get(i).getNumber());
                                }
                                editor.putStringSet("key", set);
                                editor.commit();
                                Intent intent = new Intent(getContext(), CustomerActivity.class);
                                startActivity(intent);
                            }
                            if (usertype.equals("contractor")) {
                                SharedPreferences preferencesReader = getActivity().getSharedPreferences("Contractor", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferencesReader.edit();
                                editor.putString("Contractor ID", String.valueOf(customer_id));
                                editor.commit();
                                Intent intent = new Intent(getContext(), ContractorActivity.class);
                                startActivity(intent);
                            }
                            Toast.makeText(getContext(), "Successfully login ", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getActivity(),"Invalid Details",Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(),"Invalid Details",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Invalid Details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

