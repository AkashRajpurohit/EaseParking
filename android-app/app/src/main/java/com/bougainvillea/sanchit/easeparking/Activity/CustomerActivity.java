package com.bougainvillea.sanchit.easeparking.Activity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bougainvillea.sanchit.easeparking.Adapter.CustomInfoWindowAdapter;
import com.bougainvillea.sanchit.easeparking.Adapter.PlaceAutocompleteAdapter;
import com.bougainvillea.sanchit.easeparking.Beans.CustomPosition;
import com.bougainvillea.sanchit.easeparking.Beans.PlaceInfo;
import com.bougainvillea.sanchit.easeparking.R;
import com.bougainvillea.sanchit.easeparking.Request.AddBookingPatchRequest;
import com.bougainvillea.sanchit.easeparking.Request.EndBookingPatchRequest;
import com.bougainvillea.sanchit.easeparking.Response.AddBookingPatchResponse;
import com.bougainvillea.sanchit.easeparking.Response.EndBookingPatchResponse;
import com.bougainvillea.sanchit.easeparking.Response.GetAllBookedPatchDetailsResponse;
import com.bougainvillea.sanchit.easeparking.Response.GetParkingCount;
import com.bougainvillea.sanchit.easeparking.Response.ViewParkingCoordinateResponse;
import com.bougainvillea.sanchit.easeparking.Utils.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.GsonBuilder;
import com.google.maps.android.PolyUtil;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,LocationListener,GoogleApiClient.ConnectionCallbacks {

    int old_Stroke_Colour, old_Fill_Colour;
    String vehicleNo = "";
    private static final int LOCATION_REQUEST = 500;
    String customername, customerid;
    Boolean status = false, booking_status = false;
    TextView customer_id, total_count, fill_count;
    Marker marker,mMarker;
    Location myLocation, mLastLocation;
    AutoCompleteTextView input_search;
    Button book_spot,remove_spot, shareASpot, plus, minus, book_free_line, end_booking, ok, cancel;
    private GoogleMap mMap;
    PlaceAutocompleteAdapter placeAutocompleteAdapter;
    GoogleApiClient mGoogleApiClient, mGoogleApiClientUses;
    PlaceInfo mPlace;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));
    ImageButton mapTypeDefault, mapTypeSatellite, mapTypeTerrain, mapDetailsDefault, mapDetailsParkingSpot, shareSpotBtn,information;
    Polygon old_polygon;
    List<Polygon> polyList, bookedPolyList;
    List<Polyline> polyLineList;
    FloatingActionButton filter,currentLocation;
    ArrayList<CustomPosition> final_Custom_Coordinates, final_Free_Coordinates;
    Polygon polygon;
    RelativeLayout main_ui;
    SupportMapFragment mapFragment;
    LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_customer);
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            customer_id = findViewById(R.id.customer_id);
            final_Custom_Coordinates = new ArrayList<>();
            final_Free_Coordinates = new ArrayList<>();
            polyList = new ArrayList<>();
            polyLineList = new ArrayList<>();
            bookedPolyList = new ArrayList<>();
            main_ui = findViewById(R.id.main_ui);
            filter = findViewById(R.id.filter);
            currentLocation = findViewById(R.id.myLocationButton);
            input_search = findViewById(R.id.input_search);
            mGoogleApiClientUses = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();
            placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClientUses, LAT_LNG_BOUNDS, null);
            SharedPreferences preferencesReader = getSharedPreferences("Customer", Context.MODE_PRIVATE);
            customername = preferencesReader.getString("Customer Name", null);
            customerid = preferencesReader.getString("Customer ID", null);
            customer_id.setText(customerid);
            end_booking = findViewById(R.id.end_booking);
            input_search.setAdapter(placeAutocompleteAdapter);
            input_search.setOnItemClickListener(mAutocompleteClickListener);
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        // Update UI with location data
                        // ...
                        myLocation = location;
                    }
                }
            };
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                myLocation = location;
                            }
                        }
                    });
            input_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH
                            || actionId == EditorInfo.IME_ACTION_DONE
                            || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                            || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                        String searchString = input_search.getText().toString();
                        Geocoder geocoder = new Geocoder(CustomerActivity.this);
                        List<Address> list = new ArrayList<>();
                        try {
                            list = geocoder.getFromLocationName(searchString, 1);
                        } catch (IOException e) {
                        }
                        if (list.size() > 0) {
                            Address address = list.get(0);
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(address.getLatitude(), address.getLongitude())));
                        }
                        CustomerActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(input_search.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    }
                    return false;
                }
            });
            currentLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myLocation != null)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 22));
                    else
                        Toast.makeText(CustomerActivity.this, "ENABLE LOCATION PERMISSION", Toast.LENGTH_SHORT).show();
                }
            });
            end_booking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEndBookingPatchToServer();
                    booking_status = false;
                }
            });
        }
        catch (Exception e){

        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        try {
            mMap = googleMap;
            mMap.getUiSettings().setZoomControlsEnabled(true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            LatLng project_location = new LatLng(19.114704, 72.895162);
            mMap.addMarker(new MarkerOptions().position(project_location).title("Project Location- ANDHERI"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(project_location, 20));
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(false);
            project_location = new LatLng(19.383646, 72.828986);
            try {
                final ViewGroup parent = (ViewGroup) mapFragment.getView().findViewWithTag("GoogleMapMyLocationButton").getParent();
                parent.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (int i = 0, n = parent.getChildCount(); i < n; i++) {
                                View view = parent.getChildAt(i);
                                RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) view.getLayoutParams();
                                rlp.topMargin = 380;
                                rlp.leftMargin = 900;
                                view.requestLayout();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            mMap.addMarker(new MarkerOptions().position(project_location).title("Project Location- VASAI"));
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
            // Get LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // Create a criteria object to retrieve provider
            Criteria criteria = new Criteria();
            // Get the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);
            // Get Current Location
            myLocation = locationManager.getLastKnownLocation(provider);
            filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.filter_layout, null);
                    final PopupWindow popupWindow = new PopupWindow(container, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
                    popupWindow.showAtLocation(main_ui, Gravity.RIGHT, 0, 0);
                    container.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                    mapTypeDefault = container.findViewById(R.id.defaultmap);
                    mapTypeSatellite = container.findViewById(R.id.satellite_map);
                    mapTypeTerrain = container.findViewById(R.id.terrain_map);
                    mapDetailsDefault = container.findViewById(R.id.normal);
                    mapDetailsParkingSpot = container.findViewById(R.id.parking_spot);
                    shareSpotBtn = container.findViewById(R.id.shareSpotBtn);
                    information = container.findViewById(R.id.information);
                    mapTypeDefault.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        }
                    });
                    mapTypeSatellite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        }
                    });
                    mapTypeTerrain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        }
                    });
                    mapDetailsParkingSpot.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewParking();
                            status = true;
                        }
                    });
                    mapDetailsDefault.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            polyList.clear();
                            mMap.clear();
                            status = false;
                        }
                    });
                    shareSpotBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                            LayoutInflater layoutInflater1 = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                            ViewGroup container1 = (ViewGroup) layoutInflater1.inflate(R.layout.share_a_spot_layout, null);
                            final PopupWindow popupWindow1 = new PopupWindow(container1, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
                            popupWindow1.showAtLocation(main_ui, Gravity.BOTTOM, 0, 0);
                            container1.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    popupWindow1.dismiss();
                                    currentLocation.setVisibility(View.VISIBLE);
                                    return true;
                                }
                            });
                            try {
                                minus = container1.findViewById(R.id.minus);
                                minus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mMarker.setRotation(mMarker.getRotation() - 10);
                                    }
                                });

                                plus = container1.findViewById(R.id.plus);
                                plus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        mMarker.setRotation(mMarker.getRotation() + 10);
                                    }
                                });

                            } catch (Exception e) {

                            }
                            shareASpot = container1.findViewById(R.id.share_spot);
                            shareASpot.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (myLocation != null) {
                                        MarkerOptions mp = new MarkerOptions();
                                        mp.position(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()));
                                        mp.title("my position");
                                        mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.car));
                                        mp.alpha(0.75f);
                                        mp.anchor(0.5f, 0.5f);
                                        mp.rotation(0);
                                        mp.draggable(true);
                                        mMarker = mMap.addMarker(mp);
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                                new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 22));
                                    }

                                }
                            });
                        }
                    });
                    information.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                            final ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.information_dialog, null);
                            final PopupWindow popupWindow1 = new PopupWindow(container, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
                            popupWindow1.showAtLocation(main_ui, Gravity.CENTER, 0, 0);
                            container.findViewById(R.id.red_info).setBackgroundResource(R.drawable.ic_red);
                            container.findViewById(R.id.yellow_info).setBackgroundResource(R.drawable.ic_yellow);
                            container.findViewById(R.id.green_info).setBackgroundResource(R.drawable.ic_green);
                            popupWindow.dismiss();
                            container.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    popupWindow1.dismiss();
                                    return true;
                                }
                            });
                        }
                    });
                }
            });


            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(final LatLng point) {
                    if (status) {
                        for (final Polyline polyObj : polyLineList) {
                            if (PolyUtil.isLocationOnPath(point, polyObj.getPoints(), false, 0.5)) {
                                if (polyObj.getColor() != Color.parseColor("#ff0000")) {
                                    final LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                                    ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.free_hand_booking_layout, null);
                                    final PopupWindow popupWindow = new PopupWindow(container, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
                                    popupWindow.showAtLocation(main_ui, Gravity.BOTTOM, 0, 0);
                                    popupWindow.setAnimationStyle(R.style.popup_window_animation_sms);
                                    container.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            popupWindow.dismiss();
                                            return true;
                                        }
                                    });
                                    total_count = container.findViewById(R.id.total_count);
                                    fill_count = container.findViewById(R.id.fill_count);
                                    Call<GetParkingCount> getParkingCountCall = Util.getWebservices().getParkingCount((Integer) polyObj.getTag());
                                    getParkingCountCall.enqueue(new Callback<GetParkingCount>() {
                                        @Override
                                        public void onResponse(Call<GetParkingCount> call, Response<GetParkingCount> response) {
                                            if (response.body() != null) {
                                                if (response.body().isSuccess()) {
                                                    total_count.setText(Integer.toString(response.body().getCapacity()));
                                                    fill_count.setText(Integer.toString(response.body().getCapacity_count()));
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<GetParkingCount> call, Throwable t) {

                                        }
                                    });
                                    if(polyObj.getColor() == Color.parseColor("#4f4f4f") && !booking_status) {
                                        book_free_line = container.findViewById(R.id.free_line_booking);
                                        book_free_line.setText("FILLED");
                                        book_free_line.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                popupWindow.dismiss();
                                                Toast.makeText(CustomerActivity.this,"Parking is Full",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    else{
                                        if (!booking_status) {
                                            book_free_line = container.findViewById(R.id.free_line_booking);
                                            book_free_line.setText("BOOK NOW");
                                            book_free_line.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    final LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                                                    final View container = layoutInflater.inflate(R.layout.prompts, null);
                                                    final PopupWindow popupWindow = new PopupWindow(container, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                                    popupWindow.showAtLocation(main_ui, Gravity.CENTER, 0, 0);
                                                    ok = container.findViewById(R.id.ok);
                                                    cancel = container.findViewById(R.id.cancel);
                                                    final TextView vehicleNoChoosen = container.findViewById(R.id.vehicleNoChoosen);
                                                    Spinner vehicleNoChoice = container.findViewById(R.id.vehicleNoChoice);
                                                    popupWindow.setOutsideTouchable(false);
                                                    final ArrayList<String> vehicleNoList = new ArrayList<>();
                                                    SharedPreferences preferencesReader = getSharedPreferences("Customer", Context.MODE_PRIVATE);
                                                    Set<String> s = preferencesReader.getStringSet("key", null);
                                                    vehicleNoList.addAll(s);
                                                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(CustomerActivity.this, android.R.layout.simple_spinner_item, vehicleNoList);
                                                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                    vehicleNoChoice.setAdapter(spinnerArrayAdapter);
                                                    vehicleNoChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                        @Override
                                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                            vehicleNoChoosen.setText(vehicleNoList.get(position));
                                                        }

                                                        @Override
                                                        public void onNothingSelected(AdapterView<?> parent) {

                                                        }
                                                    });
                                                    ok.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            popupWindow.dismiss();
                                                            if (vehicleNoChoosen.getText().toString().equals("")) {
                                                                Toast.makeText(CustomerActivity.this, "Please Select your Vehicle No", Toast.LENGTH_SHORT);
                                                            } else {
                                                                vehicleNo = vehicleNoChoosen.getText().toString();
                                                                sendPolylineBooking(polyObj);
                                                            }
                                                        }
                                                    });
                                                    cancel.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            popupWindow.dismiss();
                                                            Toast.makeText(CustomerActivity.this, "Selection Removed", Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                                }
                                            });
                                        } else {
                                            book_free_line = container.findViewById(R.id.free_line_booking);
                                            book_free_line.setText("END NOW");
                                            book_free_line.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    sendEndBookingPatchToServer();
                                                }
                                            });
                                        }
                                    }
                                } else {
                                    Toast.makeText(CustomerActivity.this, "No Parking", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        for (final Polygon pObj : polyList) {
                            if (pObj.getStrokeColor() != Color.parseColor("#000000")) {
                                if (PolyUtil.containsLocation(point, pObj.getPoints(), false)) {
                                    if (!booking_status) {
                                        if (marker == null) {
                                            old_polygon = pObj;
                                            marker = mMap.addMarker(new MarkerOptions()
                                                    .position(point)
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_marker)));
                                            polygon = pObj;
                                            old_Stroke_Colour = pObj.getStrokeColor();
                                            old_Fill_Colour = pObj.getFillColor();
                                            final LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                                            ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.bottom_booking_buttons, null);
                                            final PopupWindow popupWindow = new PopupWindow(container, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
                                            popupWindow.showAtLocation(main_ui, Gravity.BOTTOM, 0, 0);
                                            book_spot = container.findViewById(R.id.book);
                                            remove_spot = container.findViewById(R.id.cancel);
                                            changePatchColour(pObj);
                                            book_spot.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (marker != null) {
                                                        try {
                                                            CustomInfoWindowAdapter.setBookingId();
                                                            status = false;
                                                            popupWindow.dismiss();
                                                            final LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                                                            final View container = layoutInflater.inflate(R.layout.prompts, null);
                                                            final PopupWindow popupWindow = new PopupWindow(container, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                                            popupWindow.showAtLocation(main_ui, Gravity.CENTER, 0, 0);
                                                            ok = container.findViewById(R.id.ok);
                                                            cancel = container.findViewById(R.id.cancel);
                                                            final TextView vehicleNoChoosen = container.findViewById(R.id.vehicleNoChoosen);
                                                            Spinner vehicleNoChoice = container.findViewById(R.id.vehicleNoChoice);
                                                            popupWindow.setOutsideTouchable(false);
                                                            final ArrayList<String> vehicleNoList = new ArrayList<>();
                                                            SharedPreferences preferencesReader = getSharedPreferences("Customer", Context.MODE_PRIVATE);
                                                            Set<String> s = preferencesReader.getStringSet("key", null);
                                                            vehicleNoList.addAll(s);
                                                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(CustomerActivity.this, android.R.layout.simple_spinner_item, vehicleNoList);
                                                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                                            vehicleNoChoice.setAdapter(spinnerArrayAdapter);
                                                            vehicleNoChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                @Override
                                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                    vehicleNoChoosen.setText(vehicleNoList.get(position));
                                                                }

                                                                @Override
                                                                public void onNothingSelected(AdapterView<?> parent) {

                                                                }
                                                            });
                                                            ok.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    popupWindow.dismiss();
                                                                    if (vehicleNoChoosen.getText().toString().equals("")) {
                                                                        Toast.makeText(CustomerActivity.this, "Please Select your Vehicle No", Toast.LENGTH_SHORT);
                                                                    } else {
                                                                        vehicleNo = vehicleNoChoosen.getText().toString();
                                                                        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(CustomerActivity.this,vehicleNo));
                                                                        sendAddBookingPatchToServer(old_polygon);
                                                                    }
                                                                }
                                                            });
                                                            cancel.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    if (marker != null) {
                                                                        marker.remove();
                                                                        marker = null;
                                                                        restoreOldPolygonColour();
                                                                        popupWindow.dismiss();
                                                                        Toast.makeText(CustomerActivity.this, "Selection Removed", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                        } catch (Exception ignored) {
                                                        }
                                                    } else {
                                                        Toast.makeText(CustomerActivity.this, "Please select a spot", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            });
                                            remove_spot.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (marker != null) {
                                                        marker.remove();
                                                        marker = null;
                                                        restoreOldPolygonColour();
                                                        popupWindow.dismiss();
                                                        Toast.makeText(CustomerActivity.this, "Selection Removed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                            break;
                                        } else if (!PolyUtil.containsLocation(marker.getPosition(), pObj.getPoints(), false)) {
                                            marker.remove();
                                            restoreOldPolygonColour();
                                            marker = mMap.addMarker(new MarkerOptions()
                                                    .position(point)
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_marker)));
                                            mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(CustomerActivity.this,vehicleNo));
                                            old_polygon = pObj;
                                            old_Stroke_Colour = pObj.getStrokeColor();
                                            old_Fill_Colour = pObj.getFillColor();
                                            changePatchColour(pObj);
                                            break;
                                        }
                                    } else {
                                        Toast.makeText(CustomerActivity.this, "End your booking first", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }

                    }
                }
            });
        }
        catch (Exception e){

        }
    }

    private void viewParking() {
        try {
            final_Custom_Coordinates.clear();
            final_Free_Coordinates.clear();
            Call<ViewParkingCoordinateResponse> viewParkingCoordinateResponseCall = Util.getWebservices().viewParkingPatch();
            viewParkingCoordinateResponseCall.enqueue(new Callback<ViewParkingCoordinateResponse>() {
                @Override
                public void onResponse(Call<ViewParkingCoordinateResponse> call, Response<ViewParkingCoordinateResponse> response) {
                    ViewParkingCoordinateResponse viewParkingResponse = response.body();
                    Log.e("API CO-ORDINATE FETCHING", new GsonBuilder().create().toJson(response.body()));
                    if (viewParkingResponse.getData() != null) {
                        for (int i = 0; i < viewParkingResponse.getData().size(); i++) {
                            if (viewParkingResponse.getData().get(i).getPatch_type() != null)
                                if (viewParkingResponse.getData().get(i).getPatch_type().equals("freehand")) {
                                    List<ViewParkingCoordinateResponse.DataBean.CoordinatesBean> list = viewParkingResponse.getData().get(i).getCoordinates();
                                    for (int j = 0; j < list.size(); j++) {
                                        final_Free_Coordinates.add(new CustomPosition(list.get(j).getLat(), list.get(j).getLng(), viewParkingResponse.getData().get(i).getColor(), viewParkingResponse.getData().get(i).getPatch_id()));
                                    }
                                    plotFreeHandParkingSpot(final_Free_Coordinates);
                                } else {
                                    List<ViewParkingCoordinateResponse.DataBean.CoordinatesBean> list = viewParkingResponse.getData().get(i).getCoordinates();
                                    for (int j = 0; j < list.size(); j++) {
                                        final_Custom_Coordinates.add(new CustomPosition(list.get(j).getLat(), list.get(j).getLng(), viewParkingResponse.getData().get(i).getColor(), viewParkingResponse.getData().get(i).getPatch_id()));
                                    }
                                }
                        }
                    }
                    plotPatchParkingSpot(final_Custom_Coordinates);
                }

                @Override
                public void onFailure(Call<ViewParkingCoordinateResponse> call, Throwable t) {

                }
            });
        }
        catch (Exception e){

        }
    }

    private void getAllBookedSpot() {
        try {
            Call<GetAllBookedPatchDetailsResponse> getAllBookedPatchDetailsResponseCall = Util.getWebservices().viewBookedPatch();
            getAllBookedPatchDetailsResponseCall.enqueue(new Callback<GetAllBookedPatchDetailsResponse>() {
                @Override
                public void onResponse(Call<GetAllBookedPatchDetailsResponse> call, Response<GetAllBookedPatchDetailsResponse> response) {
                    GetAllBookedPatchDetailsResponse fullResponse = response.body();
                    try {
                        if (response.body() != null)
                            if (response.body().isSuccess()) {
                                for (int i = 0; i < fullResponse.getData().size(); i++) {
                                    if (fullResponse.getData().get(i).isBook_status())
                                        if (fullResponse.getData().get(i).get_pid().getPatch_type().equals("freehand")) {
                                            for (Polyline p : polyLineList) {
                                                if(p.getTag().equals(fullResponse.getData().get(i).getPatch_id()))
                                                    p.setColor(Color.parseColor(fullResponse.getData().get(i).getColor()));
                                            }
                                            if (response.body().getData().get(i).getUser_id().get_id().equals(customerid)) {
                                                booking_status = true;
                                            }
                                        }
                                    for (int x = 0; x < final_Custom_Coordinates.size(); x++) {
                                        CustomPosition c = final_Custom_Coordinates.get(x);
                                        if (fullResponse.getData() != null)
                                            if (fullResponse.getData().get(i).isBook_status())
                                                if (c.getPatch_id() == fullResponse.getData().get(i).getPatch_id()) {
                                                    for (Polygon m : polyList) {
                                                        if (m.getTag().equals(fullResponse.getData().get(i).getPatch_id())) {
                                                            m.setStrokeColor(Color.parseColor(fullResponse.getData().get(i).getColor()));
                                                            m.setFillColor(Color.parseColor("#c0c7d1"));
                                                        }
                                                    }
                                                    mMap.addMarker(new MarkerOptions()
                                                            .position(new LatLng(c.getLati(), c.getLongi()))
                                                            .title(fullResponse.getData().get(i).getUser_id().getName())
                                                            .snippet(fullResponse.getData().get(i).getVehicleNo())
                                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking_marker)));
                                                    x = x + 3;
                                                    if (response.body().getData().get(i).getUser_id().get_id().equals(customerid)) {
                                                        end_booking.setVisibility(View.VISIBLE);
                                                        booking_status = true;
                                                    }
                                                }
                                    }
                                }
                            }
                    }
                    catch (Exception e){

                    }
                }


                @Override
                public void onFailure(Call<GetAllBookedPatchDetailsResponse> call, Throwable t) {

                }
            });
        }
        catch (Exception e){

        }
    }


    private void plotFreeHandParkingSpot(ArrayList<CustomPosition> a) {
        try {
            polyLineList.clear();
            ArrayList<LatLng> newFreeLine = new ArrayList<>();
            for (CustomPosition c : a) {
                newFreeLine.add(new LatLng(c.getLati(), c.getLongi()));
            }
            for (int i = 0; i < newFreeLine.size() - 1; i++) {
                Polyline p = mMap.addPolyline(new PolylineOptions()
                        .add(newFreeLine.get(i), newFreeLine.get(i + 1))
                        .width(15)
                        .color(Color.parseColor(a.get(i).getColour())));
                p.setTag(a.get(i).getPatch_id());
                polyLineList.add(p);
            }
            final_Free_Coordinates.clear();
        }
        catch (Exception e){

        }
    }

    private void plotPatchParkingSpot(ArrayList<CustomPosition> a) {
        try {
            ArrayList<LatLng> newPolygon = new ArrayList<>();
            for (int i = 0; i < a.size(); i++) {
                newPolygon.add(new LatLng(a.get(i).getLati(), a.get(i).getLongi()));
                if ((i + 1) % 4 == 0) {
                    Polygon p = mMap.addPolygon(new PolygonOptions()
                            .addAll(newPolygon)
                            .strokeColor(Color.parseColor(a.get(i).getColour()))
                            .strokeWidth(5));
                    p.setFillColor(Color.parseColor("#ffffff"));
                    p.setTag(a.get(i).getPatch_id());
                    if (p.getStrokeColor() == Color.parseColor("#000000")) {
                        p.setFillColor(Color.parseColor("#c0c7d1"));
                        bookedPolyList.add(p);
                        newPolygon.clear();
                        continue;
                    }
                    polyList.add(p);
                    newPolygon.clear();
                }
            }
            getAllBookedSpot();
        }
        catch (Exception e){

        }
    }

    public void sendAddBookingPatchToServer(Polygon p){
        try {
            final AddBookingPatchRequest addBookingPatchRequest = new AddBookingPatchRequest();
            addBookingPatchRequest.setPatch_id((Integer) p.getTag());
            DateFormat df = new SimpleDateFormat("yy-MM-dd");
            Date dateobj = new Date();
            addBookingPatchRequest.setStart_Date(df.format(dateobj));
            if (p.getStrokeColor() == Color.parseColor("#ff0000"))
                addBookingPatchRequest.setCharge(100);
            df = new SimpleDateFormat("HH:mm:ss");
            dateobj = new Date();
            addBookingPatchRequest.setStart_time(df.format(dateobj));
            addBookingPatchRequest.setUser_id(customerid);
            addBookingPatchRequest.setVehicleNo(vehicleNo);
            Log.e("SEE HERE", new GsonBuilder().create().toJson(addBookingPatchRequest));
            Call<AddBookingPatchResponse> getAllBookedPatchDetailsResponseCall = Util.getWebservices().addBookingPatch(addBookingPatchRequest);
            getAllBookedPatchDetailsResponseCall.enqueue(new Callback<AddBookingPatchResponse>() {
                @Override
                public void onResponse(Call<AddBookingPatchResponse> call, Response<AddBookingPatchResponse> response) {
                    try {
                        if (response.body().isSuccess()) {
                            Toast.makeText(CustomerActivity.this, "BOOKING DONE SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                            booking_status = true;
                            end_booking.setVisibility(View.VISIBLE);
                        }
                    }
                    catch (Exception e){

                    }
                }

                @Override
                public void onFailure(Call<AddBookingPatchResponse> call, Throwable t) {

                }
            });
        }
        catch (Exception e){

        }
    }

    private void sendPolylineBooking(final Polyline p){
        try {
            final AddBookingPatchRequest addBookingPatchRequest = new AddBookingPatchRequest();
            addBookingPatchRequest.setPatch_id((Integer) p.getTag());
            DateFormat df = new SimpleDateFormat("yy-MM-dd");
            Date dateobj = new Date();
            addBookingPatchRequest.setStart_Date(df.format(dateobj));
            df = new SimpleDateFormat("HH:mm:ss");
            dateobj = new Date();
            addBookingPatchRequest.setStart_time(df.format(dateobj));
            addBookingPatchRequest.setUser_id(customerid);
            addBookingPatchRequest.setVehicleNo(vehicleNo);
            Call<AddBookingPatchResponse> getAllBookedPatchDetailsResponseCall = Util.getWebservices().addBookingPatch(addBookingPatchRequest);
            getAllBookedPatchDetailsResponseCall.enqueue(new Callback<AddBookingPatchResponse>() {
                @Override
                public void onResponse(Call<AddBookingPatchResponse> call, Response<AddBookingPatchResponse> response) {
                    if(response.body().isSuccess()){
                        Toast.makeText(CustomerActivity.this, "BOOKING DONE SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                        getAllBookedSpot();
                    }
                }

                @Override
                public void onFailure(Call<AddBookingPatchResponse> call, Throwable t) {


                }
            });
        }
        catch (Exception e){

        }
    }

    public void sendEndBookingPatchToServer(){
        try {
            EndBookingPatchRequest endBookingPatchRequest = new EndBookingPatchRequest();
            endBookingPatchRequest.setUser_id(customerid);
            DateFormat df = new SimpleDateFormat("yy-MM-dd");
            Date dateobj = new Date();
            endBookingPatchRequest.setEnd_date(df.format(dateobj));
            df = new SimpleDateFormat("HH:mm:ss");
            dateobj = new Date();
            endBookingPatchRequest.setEnd_time(df.format(dateobj));
            Call<EndBookingPatchResponse> endBookingPatchResponseCall = Util.getWebservices().endBookedPatch(endBookingPatchRequest);
            endBookingPatchResponseCall.enqueue(new Callback<EndBookingPatchResponse>() {
                @Override
                public void onResponse(Call<EndBookingPatchResponse> call, Response<EndBookingPatchResponse> response) {
                    Toast.makeText(CustomerActivity.this, "BOOKING REMOVED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                    getAllBookedSpot();
                    end_booking.setVisibility(View.GONE);
                    booking_status = false;
                    mMap.clear();
                    status = true;
                    polyList.clear();
                    viewParking();
                }

                @Override
                public void onFailure(Call<EndBookingPatchResponse> call, Throwable t) {

                }
            });
        }
        catch (Exception e){

        }
    }

    private void changePatchColour(Polygon polygon) {
        try {
                polygon.setStrokeColor(Color.BLACK);
                polygon.setFillColor(Color.parseColor("#c0c7d1"));

        }
        catch (Exception e){

        }
    }

    private void restoreOldPolygonColour() {
        old_polygon.setStrokeColor(old_Stroke_Colour);
        old_polygon.setFillColor(old_Fill_Colour);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            CustomerActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(input_search.getWindowToken(),
                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
            final AutocompletePrediction item = placeAutocompleteAdapter.getItem(i);
            final String placeId = item.getPlaceId();
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClientUses, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
            if(!places.getStatus().isSuccess()){
                places.release();
                return;
            }
            final Place place = places.get(0);

            try{
                mPlace = new PlaceInfo();
                mPlace.setName(place.getName().toString());
                mPlace.setAddress(place.getAddress().toString());
                mPlace.setId(place.getId());
                mPlace.setLatlng(place.getLatLng());
                mPlace.setRating(place.getRating());
                mPlace.setPhoneNumber(place.getPhoneNumber().toString());
                mPlace.setWebsiteUri(place.getWebsiteUri());
            }catch (NullPointerException e){
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(place.getViewport().getCenter().latitude,
                    place.getViewport().getCenter().longitude)));
            places.release();
        }
    };


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(500);
        mLocationRequest.setFastestInterval(50);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean mRequestingLocationUpdates = true;
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

}
