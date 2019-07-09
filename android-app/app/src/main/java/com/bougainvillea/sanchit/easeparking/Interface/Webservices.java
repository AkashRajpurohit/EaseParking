package com.bougainvillea.sanchit.easeparking.Interface;

import com.bougainvillea.sanchit.easeparking.Request.AddBookingPatchRequest;
import com.bougainvillea.sanchit.easeparking.Request.AddParkingCoordinateRequest;
import com.bougainvillea.sanchit.easeparking.Request.EndBookingPatchRequest;
import com.bougainvillea.sanchit.easeparking.Request.LoginRequest;
import com.bougainvillea.sanchit.easeparking.Request.SignUpRequest;
import com.bougainvillea.sanchit.easeparking.Response.AddBookingPatchResponse;
import com.bougainvillea.sanchit.easeparking.Response.AddParkingCoordinateResponse;
import com.bougainvillea.sanchit.easeparking.Response.EndBookingPatchResponse;
import com.bougainvillea.sanchit.easeparking.Response.GetAllBookedPatchDetailsResponse;
import com.bougainvillea.sanchit.easeparking.Response.GetParkingCount;
import com.bougainvillea.sanchit.easeparking.Response.LoginResponse;
import com.bougainvillea.sanchit.easeparking.Response.SignUpResponse;
import com.bougainvillea.sanchit.easeparking.Response.ViewParkingCoordinateResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Webservices {

    @POST("users/register")
    Call<SignUpResponse> getSignup(@Body SignUpRequest signUpRequest);

    @POST("users/login")
    Call<LoginResponse> getlogin(@Body LoginRequest loginRequest);

    @POST("parkings/add")
    Call<AddParkingCoordinateResponse> addParkingPatch(@Body AddParkingCoordinateRequest addParkingCoordinateRequest);

    @GET("parkings/all")
    Call<ViewParkingCoordinateResponse> viewParkingPatch();

    @POST("bookings/add")
    Call<AddBookingPatchResponse> addBookingPatch(@Body AddBookingPatchRequest addBookingPatchRequest);

    @GET("bookings/all")
    Call<GetAllBookedPatchDetailsResponse> viewBookedPatch();

    @PATCH("bookings/disable")
    Call<EndBookingPatchResponse> endBookedPatch(@Body EndBookingPatchRequest endBookingPatchRequest);

    @GET("parkings/{id}")
    Call<GetParkingCount> getParkingCount(@Path("id") int id);
}
