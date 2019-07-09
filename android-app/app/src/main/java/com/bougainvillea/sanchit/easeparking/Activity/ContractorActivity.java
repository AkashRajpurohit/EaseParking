package com.bougainvillea.sanchit.easeparking.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;

import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bougainvillea.sanchit.easeparking.Adapter.PlaceAutocompleteAdapter;
import com.bougainvillea.sanchit.easeparking.Adapter.ReportAdapter;
import com.bougainvillea.sanchit.easeparking.Adapter.SpinnerAdapter;
import com.bougainvillea.sanchit.easeparking.Beans.CustomPosition;
import com.bougainvillea.sanchit.easeparking.Beans.PlaceInfo;
import com.bougainvillea.sanchit.easeparking.Beans.Report;
import com.bougainvillea.sanchit.easeparking.Beans.SpinnerFormat;
import com.bougainvillea.sanchit.easeparking.R;
import com.bougainvillea.sanchit.easeparking.Request.AddParkingCoordinateRequest;
import com.bougainvillea.sanchit.easeparking.Response.AddParkingCoordinateResponse;
import com.bougainvillea.sanchit.easeparking.Response.ViewParkingCoordinateResponse;
import com.bougainvillea.sanchit.easeparking.Utils.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
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
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContractorActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener,LocationListener,GoogleApiClient.ConnectionCallbacks{

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;
    int free_point, custom_point, north_east_indicator, north_west_indicator = 0;
    Spinner colour_spinner, type_spinner, readymade_spinner;
    Button eraser, send, report, north_east, north_west, free_hand_send_btn;
    AutoCompleteTextView input_search;
    ImageButton mapTypeDefault, mapTypeSatellite, mapTypeTerrain, mapDetailsDefault, mapDetailsParkingSpot,information;
    FloatingActionButton currentLocation,filter;
    PopupWindow popupWindow;
    Location myLocation;
    RecyclerView report_view;
    TextView mode_remainder, type_remainder;
    ReportAdapter reportAdapter;
    PlaceAutocompleteAdapter placeAutocompleteAdapter;
    GoogleApiClient mGoogleApiClient;
    GoogleApiClient mGoogleApiClientUses;
    PlaceInfo mPlace;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));
    LayoutInflater layoutInflater;
    RelativeLayout main_ui,reportLayout;
    ConstraintLayout compassLayout;
    CustomPosition customPosition;
    ArrayList<CustomPosition> list_Points, list_Custom_Points, final_Coordinates, final_Custom_Coordinates, api_free_hand_coordinate, api_custom_patch_coordinate;
    ArrayList<SpinnerFormat> list, list1, list2;
    ArrayList<Marker> markers;
    ArrayList<Polyline> free_polyline, custom_polyline;
    List<Polygon> polyList;
    List<Report> reportList;
    List<LatLng> serializer_list;
    SupportMapFragment mapFragment;
    private FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_contractor);
            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            report = findViewById(R.id.report);
            eraser = findViewById(R.id.eraser);
            send = findViewById(R.id.send);
            north_east = findViewById(R.id.north_east);
            north_west = findViewById(R.id.north_west);
            compassLayout = findViewById(R.id.compass);
            type_remainder = findViewById(R.id.type_remainder);
            mode_remainder = findViewById(R.id.mode_reminder);
            input_search = findViewById(R.id.input_search);
            mGoogleApiClientUses = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();
            placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClientUses, LAT_LNG_BOUNDS, null);
            currentLocation = findViewById(R.id.myLocationButton);
            filter = findViewById(R.id.filter);
            colour_spinner = findViewById(R.id.color);
            readymade_spinner = findViewById(R.id.draw_readymade);
            type_spinner = findViewById(R.id.type);
            main_ui = findViewById(R.id.rel);
            report_view = findViewById(R.id.recycler_view);
            free_hand_send_btn = findViewById(R.id.free_hand_send_button);
            list_Points = new ArrayList<>();
            list_Custom_Points = new ArrayList<>();
            final_Custom_Coordinates = new ArrayList<>();
            final_Coordinates = new ArrayList<>();
            free_polyline = new ArrayList<>();
            custom_polyline = new ArrayList<>();
            polyList = new ArrayList<>();
            markers = new ArrayList<>();
            list = new ArrayList<>();
            list1 = new ArrayList<>();
            list2 = new ArrayList<>();
            api_custom_patch_coordinate = new ArrayList<>();
            api_free_hand_coordinate = new ArrayList<>();
            serializer_list = new ArrayList<>();
            reportList = new ArrayList<>();
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        myLocation = location;
                    }
                }
            };
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                myLocation = location;
                            }
                        }
                    });
            list.add(new SpinnerFormat(R.drawable.ic_red, "RED"));
            list.add(new SpinnerFormat(R.drawable.ic_yellow, "YELLOW"));
            list.add(new SpinnerFormat(R.drawable.ic_green, "GREEN"));
            SpinnerAdapter colour_spinner_adapter = new SpinnerAdapter(this, R.layout.spinner_layout, R.id.txt, list);
            colour_spinner.setAdapter(colour_spinner_adapter);
            list1.add(new SpinnerFormat(R.drawable.ic_settings_black_24dp, "SELECT YOUR TYPE"));
            list1.add(new SpinnerFormat(R.drawable.ic_free_hand, "FREE HAND"));
            list1.add(new SpinnerFormat(R.drawable.ic_custom_patch, "CUSTOM PATCH"));
            list1.add(new SpinnerFormat(R.drawable.ic_readymade_patch, "PATCH SERIALIZER"));
            SpinnerAdapter type_spinner_adapter = new SpinnerAdapter(this, R.layout.spinner_layout, R.id.txt, list1);
            type_spinner.setAdapter(type_spinner_adapter);
            list2.add(new SpinnerFormat(R.drawable.ic_create_black_24dp, "SELECT YOUR CHOICE"));
            list2.add(new SpinnerFormat(R.drawable.ic_directions_bike_black_24dp, "BIKE"));
            list2.add(new SpinnerFormat(R.drawable.ic_directions_car_black_24dp, "CAR"));
            SpinnerAdapter readymade_spinner_adapter = new SpinnerAdapter(this, R.layout.spinner_layout, R.id.txt, list2);
            readymade_spinner.setAdapter(readymade_spinner_adapter);

            north_west.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    north_west_indicator = 1;
                    north_east_indicator = 0;
                }
            });

            north_east.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    north_west_indicator = 0;
                    north_east_indicator = 1;
                }
            });
            colour_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    if (colour_spinner.getSelectedItemPosition() == 0) {
                        Snackbar.make(main_ui, "You are using RED LINE", Snackbar.LENGTH_SHORT).show();

                    }
                    if (colour_spinner.getSelectedItemPosition() == 1) {
                        Snackbar.make(main_ui, "You are using YELLOW LINE", Snackbar.LENGTH_SHORT).show();

                    }
                    if (colour_spinner.getSelectedItemPosition() == 2) {
                        Snackbar.make(main_ui, "You are using GREEN LINE", Snackbar.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    // TODO Auto-generated method stub
                    if (type_spinner.getSelectedItemPosition() == 0) {
                        if (final_Custom_Coordinates.size() % 4 != 0) {
                            Toast.makeText(getApplicationContext(), "FIRST COMPLETE THE PATCH", Toast.LENGTH_SHORT).show();
                            type_spinner.setSelection(1);
                        }
                        type_remainder.setText("");
                    }
                    if (type_spinner.getSelectedItemPosition() == 1) {
                        if (final_Custom_Coordinates.size() % 4 != 0) {
                            Toast.makeText(getApplicationContext(), "FIRST COMPLETE THE PATCH", Toast.LENGTH_SHORT).show();
                            type_spinner.setSelection(2);
                        } else
                            Snackbar.make(main_ui, "You are using FREE DRAW MODE. Select 2 points", Snackbar.LENGTH_SHORT).show();
                        readymade_spinner.setSelection(0);
                        compassLayout.setVisibility(View.INVISIBLE);
                        mode_remainder.setText("");
                        type_remainder.setText("");
                    }
                    if (type_spinner.getSelectedItemPosition() == 2) {
                        Snackbar.make(main_ui, "You are using CUSTOM PATCH MODE. Select 4 points", Snackbar.LENGTH_SHORT).show();
                        list_Points.clear();
                        free_point = 0;
                        compassLayout.setVisibility(View.INVISIBLE);
                        readymade_spinner.setSelection(0);
                        mode_remainder.setText("");
                        type_remainder.setText("");
                    }
                    if (type_spinner.getSelectedItemPosition() == 3) {
                        if (final_Custom_Coordinates.size() % 4 != 0) {
                            Toast.makeText(getApplicationContext(), "FIRST COMPLETE THE PATCH", Toast.LENGTH_SHORT).show();
                            type_spinner.setSelection(1);
                        } else
                            Snackbar.make(main_ui, "You are using PATCH SERIALIZER MODE. Select 2 points", Snackbar.LENGTH_SHORT).show();
                        type_remainder.setText("SERIALIZER");
                        north_west_indicator = 0;
                        north_east_indicator = 0;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });

            readymade_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (readymade_spinner.getSelectedItemPosition() == 0) {
                        if (final_Custom_Coordinates.size() % 4 != 0) {
                            Toast.makeText(getApplicationContext(), "FIRST COMPLETE THE PATCH", Toast.LENGTH_SHORT).show();
                            type_spinner.setSelection(2);
                            readymade_spinner.setSelection(0);
                        }
                        compassLayout.setVisibility(View.INVISIBLE);
                        mode_remainder.setText("");
                        type_remainder.setText("");
                    }
                    if (readymade_spinner.getSelectedItemPosition() == 1) {
                        if (final_Custom_Coordinates.size() % 4 != 0) {
                            Toast.makeText(getApplicationContext(), "FIRST COMPLETE THE PATCH", Toast.LENGTH_SHORT).show();
                            type_spinner.setSelection(2);
                            readymade_spinner.setSelection(0);
                        } else {
                            if (!(type_spinner.getSelectedItemPosition() == 3))
                                type_spinner.setSelection(0);
                            Snackbar.make(main_ui, "You are using READYMADE PATCH MODE for 2 Wheeler. Select 1 points", Snackbar.LENGTH_SHORT).show();
                            compassLayout.setVisibility(View.VISIBLE);
                            mode_remainder.setText("BIKE MODE ON");
                            north_east_indicator = 0;
                            north_west_indicator = 0;
                        }
                    }
                    if (readymade_spinner.getSelectedItemPosition() == 2) {
                        if (final_Custom_Coordinates.size() % 4 != 0) {
                            Toast.makeText(getApplicationContext(), "FIRST COMPLETE THE PATCH", Toast.LENGTH_SHORT).show();
                            type_spinner.setSelection(2);
                            readymade_spinner.setSelection(0);
                        } else {
                            if (!(type_spinner.getSelectedItemPosition() == 3))
                                type_spinner.setSelection(0);
                            Snackbar.make(main_ui, "You are using READYMADE PATCH MODE for 4 Wheeler. Select 1 points", Snackbar.LENGTH_SHORT).show();
                            compassLayout.setVisibility(View.VISIBLE);
                        }
                        mode_remainder.setText("CAR MODE ON");
                        north_east_indicator = 0;
                        north_west_indicator = 0;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            input_search.setAdapter(placeAutocompleteAdapter);
            input_search.setOnItemClickListener(mAutocompleteClickListener);
            input_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH
                            || actionId == EditorInfo.IME_ACTION_DONE
                            || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                            || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                        String searchString = input_search.getText().toString();
                        Geocoder geocoder = new Geocoder(ContractorActivity.this);
                        List<Address> list = new ArrayList<>();
                        try {
                            list = geocoder.getFromLocationName(searchString, 1);
                        } catch (IOException e) {
                        }
                        if (list.size() > 0) {
                            Address address = list.get(0);
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(address.getLatitude(), address.getLongitude())));
                        }
                        ContractorActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(input_search.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    }
                    return false;
                }
            });

            report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                    LayoutInflater inflater = LayoutInflater.from(v.getContext());
                    View alertview = inflater.inflate(R.layout.report_dialog, null);
                    reportLayout = alertview.findViewById(R.id.insert_point);
                    alert.setView(alertview);
                    alert.setTitle("GET YOUR CO-ORDINATES");
                    alert.setMessage("For FREE DRAW and CUSTOM PATCH and READYMADE PATCH");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "Report Closed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    report_view = alertview.findViewById(R.id.recycler_view);
                    reportAdapter = new ReportAdapter(reportList);
                    makeReport();
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    report_view.setLayoutManager(mLayoutManager);
                    report_view.setItemAnimator(new DefaultItemAnimator());
                    report_view.setAdapter(reportAdapter);
                    alert.create().show();
                }
            });

            eraser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type_spinner.getSelectedItemPosition() == 1)
                        removeFreeLine();
                    else {
                        removeCustomLine();
                    }
                }
            });

            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendCordinate();
                }
            });

            filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    final ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.filter_layout, null);
                    popupWindow = new PopupWindow(container, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
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
                        }
                    });
                    mapDetailsDefault.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMap.clear();
                        }
                    });
                    information.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
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

            currentLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (myLocation != null)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 22));
                    else
                        Toast.makeText(ContractorActivity.this, "ENABLE LOCATION PERMISSION", Toast.LENGTH_SHORT).show();
                }
            });
            free_hand_send_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendFreeHandCoordinate();
                    free_hand_send_btn.setVisibility(View.GONE);
                }
            });
            addCoordinate();
        }catch (Exception e){

        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            mMap.getUiSettings().setZoomControlsEnabled(true);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(false);
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
            LatLng project_location = new LatLng(19.114704, 72.895162);
            mMap.addMarker(new MarkerOptions().position(project_location).title("Project Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(project_location, 20));
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {

                    if (colour_spinner.getSelectedItemPosition() == 0 && type_spinner.getSelectedItemPosition() == 1) {
                        drawFreeLine(latLng, "#ff0000", 15);
                        free_hand_send_btn.setVisibility(View.VISIBLE);
                    }

                    if (colour_spinner.getSelectedItemPosition() == 1 && type_spinner.getSelectedItemPosition() == 1) {
                        drawFreeLine(latLng, "#ffff00", 15);
                        free_hand_send_btn.setVisibility(View.VISIBLE);
                    }

                    if (colour_spinner.getSelectedItemPosition() == 2 && type_spinner.getSelectedItemPosition() == 1) {
                        drawFreeLine(latLng, "#00ff00", 15);
                        free_hand_send_btn.setVisibility(View.VISIBLE);
                    }

                    if (colour_spinner.getSelectedItemPosition() == 0 && type_spinner.getSelectedItemPosition() == 2) {
                        drawCustomPatch(latLng, "#ff0000", 5);
                    }

                    if (colour_spinner.getSelectedItemPosition() == 1 && type_spinner.getSelectedItemPosition() == 2) {
                        drawCustomPatch(latLng, "#ffff00", 5);
                    }

                    if (colour_spinner.getSelectedItemPosition() == 2 && type_spinner.getSelectedItemPosition() == 2) {
                        drawCustomPatch(latLng, "#00ff00", 5);
                    }
                    if (type_spinner.getSelectedItemPosition() == 3 && readymade_spinner.getSelectedItemPosition() == 1 && colour_spinner.getSelectedItemPosition() == 0) {
                        if (north_west_indicator == 0 && north_east_indicator == 0) {
                            Toast.makeText(ContractorActivity.this, "SELECT DIRECTION PLEASE", Toast.LENGTH_SHORT).show();
                        } else {
                            serializer_list.add(latLng);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                            mMap.addMarker(markerOptions);
                            if (north_east_indicator == 0 && north_west_indicator == 1 && serializer_list.size() == 2) {
                                drawConsectiveBikePatchNW(serializer_list, "#ff0000", 5);
                            }
                            if (north_east_indicator == 1 && north_west_indicator == 0 && serializer_list.size() == 2) {
                                drawConsectiveBikePatchNE(serializer_list, "#ff0000", 5);
                            }
                            return;
                        }
                    }
                    if (type_spinner.getSelectedItemPosition() == 3 && readymade_spinner.getSelectedItemPosition() == 1 && colour_spinner.getSelectedItemPosition() == 1) {
                        if (north_west_indicator == 0 && north_east_indicator == 0) {
                            Toast.makeText(ContractorActivity.this, "SELECT DIRECTION PLEASE", Toast.LENGTH_SHORT).show();
                        } else {
                            serializer_list.add(latLng);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                            mMap.addMarker(markerOptions);
                            if (north_east_indicator == 0 && north_west_indicator == 1 && serializer_list.size() == 2) {
                                drawConsectiveBikePatchNW(serializer_list, "#ffff00", 5);
                            }
                            if (north_east_indicator == 1 && north_west_indicator == 0 && serializer_list.size() == 2) {
                                drawConsectiveBikePatchNE(serializer_list, "#ffff00", 5);
                            }
                            return;
                        }
                    }
                    if (type_spinner.getSelectedItemPosition() == 3 && readymade_spinner.getSelectedItemPosition() == 1 && colour_spinner.getSelectedItemPosition() == 2) {
                        if (north_west_indicator == 0 && north_east_indicator == 0) {
                            Toast.makeText(ContractorActivity.this, "SELECT DIRECTION PLEASE", Toast.LENGTH_SHORT).show();
                        } else {
                            serializer_list.add(latLng);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                            mMap.addMarker(markerOptions);
                            if (north_east_indicator == 0 && north_west_indicator == 1 && serializer_list.size() == 2) {
                                drawConsectiveBikePatchNW(serializer_list, "#00ff00", 5);
                            }
                            if (north_east_indicator == 1 && north_west_indicator == 0 && serializer_list.size() == 2) {
                                drawConsectiveBikePatchNE(serializer_list, "#00ff00", 5);
                            }
                            return;
                        }
                    }
                    if (type_spinner.getSelectedItemPosition() == 3 && readymade_spinner.getSelectedItemPosition() == 2 && colour_spinner.getSelectedItemPosition() == 0) {
                        if (north_west_indicator == 0 && north_east_indicator == 0) {
                            Toast.makeText(ContractorActivity.this, "SELECT DIRECTION PLEASE", Toast.LENGTH_SHORT).show();
                        } else {
                            serializer_list.add(latLng);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                            mMap.addMarker(markerOptions);
                            if (north_east_indicator == 0 && north_west_indicator == 1 && serializer_list.size() == 2) {
                                drawConsectiveCarPatchNW(serializer_list, "#ff0000", 5);
                            }
                            if (north_east_indicator == 1 && north_west_indicator == 0 && serializer_list.size() == 2) {
                                drawConsectiveCarPatchNE(serializer_list, "#ff0000", 5);
                            }
                            return;
                        }
                    }
                    if (type_spinner.getSelectedItemPosition() == 3 && readymade_spinner.getSelectedItemPosition() == 2 && colour_spinner.getSelectedItemPosition() == 1) {
                        if (north_west_indicator == 0 && north_east_indicator == 0) {
                            Toast.makeText(ContractorActivity.this, "SELECT DIRECTION PLEASE", Toast.LENGTH_SHORT).show();
                        } else {
                            serializer_list.add(latLng);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                            mMap.addMarker(markerOptions);
                            if (north_east_indicator == 0 && north_west_indicator == 1 && serializer_list.size() == 2) {
                                drawConsectiveCarPatchNW(serializer_list, "#ffff00", 5);
                            }
                            if (north_east_indicator == 1 && north_west_indicator == 0 && serializer_list.size() == 2) {
                                drawConsectiveCarPatchNE(serializer_list, "#ffff00", 5);
                            }
                            return;
                        }
                    }
                    if (type_spinner.getSelectedItemPosition() == 3 && readymade_spinner.getSelectedItemPosition() == 2 && colour_spinner.getSelectedItemPosition() == 2) {
                        if (north_west_indicator == 0 && north_east_indicator == 0) {
                            Toast.makeText(ContractorActivity.this, "SELECT DIRECTION PLEASE", Toast.LENGTH_SHORT).show();
                        } else {
                            serializer_list.add(latLng);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
                            mMap.addMarker(markerOptions);
                            if (north_east_indicator == 0 && north_west_indicator == 1 && serializer_list.size() == 2) {
                                drawConsectiveCarPatchNW(serializer_list, "#00ff00", 5);
                            }
                            if (north_east_indicator == 1 && north_west_indicator == 0 && serializer_list.size() == 2) {
                                drawConsectiveCarPatchNE(serializer_list, "#00ff00", 5);
                            }
                            return;
                        }
                    }
                    if (colour_spinner.getSelectedItemPosition() == 0 && readymade_spinner.getSelectedItemPosition() == 1) {
                        if (north_east_indicator == 1 && north_west_indicator == 0)
                            drawReadyMadePatchBikeNE(latLng, "#ff0000", 5);
                        if (north_east_indicator == 0 && north_west_indicator == 1)
                            drawReadyMadePatchBikeNW(latLng, "#ff0000", 5);
                    }
                    if (colour_spinner.getSelectedItemPosition() == 1 && readymade_spinner.getSelectedItemPosition() == 1) {
                        if (north_east_indicator == 1 && north_west_indicator == 0)
                            drawReadyMadePatchBikeNE(latLng, "#ffff00", 5);
                        if (north_east_indicator == 0 && north_west_indicator == 1)
                            drawReadyMadePatchBikeNW(latLng, "#ffff00", 5);
                    }
                    if (colour_spinner.getSelectedItemPosition() == 2 && readymade_spinner.getSelectedItemPosition() == 1) {
                        if (north_east_indicator == 1 && north_west_indicator == 0)
                            drawReadyMadePatchBikeNE(latLng, "#00ff00", 5);
                        if (north_east_indicator == 0 && north_west_indicator == 1)
                            drawReadyMadePatchBikeNW(latLng, "#00ff00", 5);
                    }
                    if (colour_spinner.getSelectedItemPosition() == 0 && readymade_spinner.getSelectedItemPosition() == 2) {
                        if (north_east_indicator == 1 && north_west_indicator == 0)
                            drawReadyMadePatchCarNE(latLng, "#ff0000", 5);
                        if (north_east_indicator == 0 && north_west_indicator == 1)
                            drawReadyMadePatchCarNW(latLng, "#ff0000", 5);
                    }
                    if (colour_spinner.getSelectedItemPosition() == 1 && readymade_spinner.getSelectedItemPosition() == 2) {
                        if (north_east_indicator == 1 && north_west_indicator == 0)
                            drawReadyMadePatchCarNE(latLng, "#ffff00", 5);
                        if (north_east_indicator == 0 && north_west_indicator == 1)
                            drawReadyMadePatchCarNW(latLng, "#ffff00", 5);
                    }
                    if (colour_spinner.getSelectedItemPosition() == 2 && readymade_spinner.getSelectedItemPosition() == 2) {
                        if (north_east_indicator == 1 && north_west_indicator == 0)
                            drawReadyMadePatchCarNE(latLng, "#00ff00", 5);
                        if (north_east_indicator == 0 && north_west_indicator == 1)
                            drawReadyMadePatchCarNW(latLng, "#00ff00", 5);
                    }
                }
            });
        }
        catch (Exception e){

        }
    }

    private void viewParking() {
        try {
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
                                        api_free_hand_coordinate.add(new CustomPosition(list.get(j).getLat(), list.get(j).getLng(), viewParkingResponse.getData().get(i).getColor(), viewParkingResponse.getData().get(i).getPatch_id()));
                                    }
                                    plotFreeHandParkingSpot(api_free_hand_coordinate);
                                } else {
                                    List<ViewParkingCoordinateResponse.DataBean.CoordinatesBean> list = viewParkingResponse.getData().get(i).getCoordinates();
                                    for (int j = 0; j < list.size(); j++) {
                                        api_custom_patch_coordinate.add(new CustomPosition(list.get(j).getLat(), list.get(j).getLng(), viewParkingResponse.getData().get(i).getColor(), viewParkingResponse.getData().get(i).getPatch_id()));
                                    }
                                }
                        }
                    }
                    plotPatchParkingSpot(api_custom_patch_coordinate);
                }

                @Override
                public void onFailure(Call<ViewParkingCoordinateResponse> call, Throwable t) {

                }
            });
        }
        catch (Exception e){

        }
    }

    private void plotFreeHandParkingSpot(ArrayList<CustomPosition> a) {
        try {
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
            }
            api_free_hand_coordinate.clear();
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
                    newPolygon.clear();
                }
            }
        }
        catch (Exception e){

        }
    }

    private void drawConsectiveBikePatchNE(List<LatLng> serializer, String color, int width) {
        try {
            LatLng latLng = serializer.get(0);
            int value = (int) SphericalUtil.computeDistanceBetween(serializer.get(0), serializer.get(1)) / 3;
            for (int index = 0; index < value; latLng = new LatLng(latLng.latitude + 0.000012, latLng.longitude + 0.000012)) {
                latLng = drawReadyMadePatchBikeNE(latLng, color, width);
                index++;
            }
            serializer_list.clear();
        }
        catch (Exception e){

        }
    }
    private void drawConsectiveCarPatchNE(List<LatLng> serializer, String color, int width) {
        try {
            LatLng latLng = serializer.get(0);
            int value = (int) SphericalUtil.computeDistanceBetween(serializer.get(0), serializer.get(1)) / 8;
            for (int index = 0; index < value; latLng = new LatLng(latLng.latitude + 0.00004, latLng.longitude + 0.00004)) {
                latLng = drawReadyMadePatchCarNE(latLng, color, width);
                index++;
            }
            serializer_list.clear();
        }catch (Exception e){

        }
    }

    private void drawConsectiveBikePatchNW(List<LatLng> serializer,String color, int width) {
        try {
            LatLng latLng = serializer.get(0);
            int value = (int) SphericalUtil.computeDistanceBetween(serializer.get(0), serializer.get(1)) / 3;
            for (int index = 0; index < value; latLng = new LatLng(latLng.latitude - 0.000025, latLng.longitude + 0.000015)) {
                latLng = drawReadyMadePatchBikeNW(latLng, color, width);
                index++;
            }
            serializer_list.clear();
        }
        catch (Exception e){

        }
    }
    private void drawConsectiveCarPatchNW(List<LatLng> serializer,String color, int width) {
        try {
            LatLng latLng = serializer.get(0);
            int value = (int) SphericalUtil.computeDistanceBetween(serializer.get(0), serializer.get(1)) / 8;
            for (int index = 0; index < value; latLng = new LatLng(latLng.latitude - 0.00005, latLng.longitude + 0.000038)) {
                latLng = drawReadyMadePatchCarNW(latLng, color, width);
                index++;
            }
            serializer_list.clear();
        }
        catch (Exception e){

        }
    }

    private LatLng drawReadyMadePatchBikeNW(LatLng latLng, String color, int width) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        if (markers.size() == 1) {
            removeMarker();
        }
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        Marker m = mMap.addMarker(markerOptions);
        markers.add(m);
        LatLng loc1 = latLng;
        LatLng loc2 = latLng;
        double distance = 0.0;
        for (double i = 0; !(distance > 2.5 && distance < 3); i = i + 0.00002) {
            loc2 = new LatLng(loc1.latitude - i, loc1.longitude + i - 0.000007);
            distance = SphericalUtil.computeDistanceBetween(loc1, loc2);
        }
        final_Custom_Coordinates.add(new CustomPosition(loc1.latitude, loc1.longitude, color, 1));
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(loc1, loc2)
                .width(width)
                .color(Color.parseColor(color)));
        custom_polyline.add(line);
        loc2 = new LatLng(loc1.latitude - 0.000003, loc1.longitude - 0.000003);
        final_Custom_Coordinates.add(new CustomPosition(loc2.latitude, loc2.longitude, color, 1));
        line = mMap.addPolyline(new PolylineOptions()
                .add(loc1, loc2)
                .width(width)
                .color(Color.parseColor(color)));
        custom_polyline.add(line);
        distance = 0.0;
        loc1 = new LatLng(loc2.latitude, loc2.longitude);
        for (double i = 0; !(distance > 2.5 && distance < 3); i = i + 0.00002) {
            loc1 = new LatLng(loc2.latitude - i, loc2.longitude + i - 0.000007);
            distance = SphericalUtil.computeDistanceBetween(loc2, loc1);
        }
        final_Custom_Coordinates.add(new CustomPosition(loc1.latitude, loc1.longitude, color, 1));
        line = mMap.addPolyline(new PolylineOptions()
                .add(loc2, loc1)
                .width(width)
                .color(Color.parseColor(color)));
        custom_polyline.add(line);
        loc2 = new LatLng(loc1.latitude + 0.000003, loc1.longitude + 0.000003);
        final_Custom_Coordinates.add(new CustomPosition(loc2.latitude, loc2.longitude, color, 1));
        line = mMap.addPolyline(new PolylineOptions()
                .add(loc1, loc2)
                .width(width)
                .color(Color.parseColor(color)));
        custom_polyline.add(line);
        return latLng;

    }


    private LatLng drawReadyMadePatchCarNW(LatLng latLng, String color, int width) {
        LatLng loc1 = latLng;
        LatLng loc2 = latLng;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        if (markers.size() == 1) {
            removeMarker();
        }
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        Marker m = mMap.addMarker(markerOptions);
        markers.add(m);
        double distance = 0.0;
        for (double i = 0; !(distance > 4.5 && distance < 5.5); i = i + 0.00002) {
            loc2 = new LatLng(loc1.latitude - i, loc1.longitude + ((i-0.00001)) );
            distance = SphericalUtil.computeDistanceBetween(loc1, loc2);
        }
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(loc1, loc2)
                .width(width)
                .color(Color.parseColor(color)));
        custom_polyline.add(line);
        final_Custom_Coordinates.add(new CustomPosition(loc1.latitude, loc1.longitude, color,1));
        loc2 = new LatLng(loc1.latitude-0.000005,loc1.longitude-0.000005);
        line = mMap.addPolyline(new PolylineOptions()
                .add(loc1, loc2)
                .width(width)
                .color(Color.parseColor(color)));
        custom_polyline.add(line);
        final_Custom_Coordinates.add(new CustomPosition(loc2.latitude, loc2.longitude, color,1));
        distance = 0.0;
        loc1 = new LatLng(loc2.latitude, loc2.longitude);
        for (double i = 0; !(distance > 4.5 && distance < 5.5); i = i + 0.00002) {
            loc1 = new LatLng(loc2.latitude - i, loc2.longitude + ((i-0.00001)));
            distance = SphericalUtil.computeDistanceBetween(loc2, loc1);
        }
        line = mMap.addPolyline(new PolylineOptions()
                .add(loc2, loc1)
                .width(width)
                .color(Color.parseColor(color)));
        custom_polyline.add(line);
        final_Custom_Coordinates.add(new CustomPosition(loc1.latitude, loc1.longitude, color,1));
        loc2 = new LatLng(loc1.latitude+0.000005,loc1.longitude+0.000005);
        line = mMap.addPolyline(new PolylineOptions()
                .add(loc1, loc2)
                .width(width)
                .color(Color.parseColor(color)));
        custom_polyline.add(line);
        final_Custom_Coordinates.add(new CustomPosition(loc2.latitude, loc2.longitude, color,1));
        return latLng;
    }

    private LatLng drawReadyMadePatchBikeNE(LatLng latLng, String color, int width) {
        LatLng loc1 = latLng;
        LatLng loc2 = latLng;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        if (markers.size() == 1) {
            removeMarker();
        }
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        Marker m = mMap.addMarker(markerOptions);
        markers.add(m);
        double distance = 0.0;
        for (double i = 0; !(distance > 1.5 && distance < 2); i = i + 0.00001) {
            loc2 = new LatLng(loc1.latitude + i, loc1.longitude + i);
            distance = SphericalUtil.computeDistanceBetween(loc1, loc2);
        }
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(loc1, loc2)
                .width(width)
                .color(Color.parseColor(color)));
        custom_polyline.add(line);
        final_Custom_Coordinates.add(new CustomPosition(loc1.latitude, loc1.longitude, color,1));
        loc2 = new LatLng(loc1.latitude-0.000002,loc1.longitude+0.000002);
        line = mMap.addPolyline(new PolylineOptions()
                .add(loc1, loc2)
                .width(width)
                .color(Color.parseColor(color)));
        custom_polyline.add(line);
        distance = 0.0;
        final_Custom_Coordinates.add(new CustomPosition(loc2.latitude, loc2.longitude, color,1));
        loc1 = new LatLng(loc2.latitude, loc2.longitude);
        for (double i = 0; !(distance > 1.5 && distance < 2); i = i + 0.00001) {
            loc1 = new LatLng(loc2.latitude + i, loc2.longitude + i);
            distance = SphericalUtil.computeDistanceBetween(loc2, loc1);
        }
        line = mMap.addPolyline(new PolylineOptions()
                .add(loc2, loc1)
                .width(width)
                .color(Color.parseColor(color)));
        custom_polyline.add(line);
        final_Custom_Coordinates.add(new CustomPosition(loc1.latitude, loc1.longitude, color,1));
        loc2 = new LatLng(loc1.latitude+0.000002,loc1.longitude-0.000002);
        line = mMap.addPolyline(new PolylineOptions()
                .add(loc1, loc2)
                .width(width)
                .color(Color.parseColor(color)));
        custom_polyline.add(line);
        final_Custom_Coordinates.add(new CustomPosition(loc2.latitude, loc2.longitude, color,1));
        return latLng;
    }

    private LatLng drawReadyMadePatchCarNE(LatLng latLng, String color, int width) {
        LatLng loc1 = latLng;
        LatLng loc2 = latLng;
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        if (markers.size() == 1) {
            removeMarker();
        }
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        Marker m = mMap.addMarker(markerOptions);
        markers.add(m);
        double distance = 0.0;
        for (double i = 0; !(distance > 4.5 && distance < 5); i = i + 0.00001) {
            loc2 = new LatLng(loc1.latitude + i, loc1.longitude + i);
            distance = SphericalUtil.computeDistanceBetween(loc1, loc2);
        }
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(loc1, loc2)
                .width(width)
                .color(Color.parseColor(color)));
        custom_polyline.add(line);
        final_Custom_Coordinates.add(new CustomPosition(loc1.latitude, loc1.longitude, color,1));
        loc2 = new LatLng(loc1.latitude-0.000005,loc1.longitude+0.000005);
        line = mMap.addPolyline(new PolylineOptions()
                .add(loc1, loc2)
                .width(width)
                .color(Color.parseColor(color)));
        custom_polyline.add(line);
        distance = 0.0;
        final_Custom_Coordinates.add(new CustomPosition(loc2.latitude, loc2.longitude, color,1));
        loc1 = new LatLng(loc2.latitude, loc2.longitude);
        for (double i = 0; !(distance > 4.5 && distance < 5); i = i + 0.00001) {
            loc1 = new LatLng(loc2.latitude + i, loc2.longitude + i);
            distance = SphericalUtil.computeDistanceBetween(loc2, loc1);
        }
        line = mMap.addPolyline(new PolylineOptions()
                .add(loc2, loc1)
                .width(width)
                .color(Color.parseColor(color)));
        custom_polyline.add(line);
        final_Custom_Coordinates.add(new CustomPosition(loc1.latitude, loc1.longitude, color,1));
        loc2 = new LatLng(loc1.latitude+0.000005,loc1.longitude-0.000005);
        line = mMap.addPolyline(new PolylineOptions()
                .add(loc1, loc2)
                .width(5)
                .color(Color.parseColor(color)));
        custom_polyline.add(line);
        final_Custom_Coordinates.add(new CustomPosition(loc2.latitude, loc2.longitude, color,1));
        return latLng;
    }

    private void drawFreeLine(LatLng latLng, String colour, int width) {
        customPosition = new CustomPosition(latLng.latitude, latLng.longitude, colour,1);
        list_Points.add(customPosition);
        final_Coordinates.add(customPosition);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        if (markers.size() == 2) {
            removeMarker();
        }
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        if (list_Points.size() != 1) {
            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(list_Points.get(free_point - 1).getLati(), list_Points.get(free_point - 1).getLongi()), new LatLng(list_Points.get(free_point).getLati(), list_Points.get(free_point).getLongi()))
                    .width(width)
                    .color(Color.parseColor(colour)));
            free_polyline.add(line);
        }
        Marker m = mMap.addMarker(markerOptions);
        markers.add(m);
        free_point++;
    }

    private void drawCustomPatch(LatLng latLng, String colour, int width) {
        try {
            customPosition = new CustomPosition(latLng.latitude, latLng.longitude, colour, 1);
            list_Custom_Points.add(customPosition);
            final_Custom_Coordinates.add(customPosition);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            if (markers.size() == 2) {
                removeMarker();
            }
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            if (list_Custom_Points.size() != 1) {
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(list_Custom_Points.get(custom_point - 1).getLati(), list_Custom_Points.get(custom_point - 1).getLongi()), new LatLng(list_Custom_Points.get(custom_point).getLati(), list_Custom_Points.get(custom_point).getLongi()))
                        .width(width)
                        .color(Color.parseColor(colour)));
                custom_polyline.add(line);
            }
            if (list_Custom_Points.size() % 4 == 0) {
                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(list_Custom_Points.get(custom_point).getLati(), list_Custom_Points.get(custom_point).getLongi()), new LatLng(list_Custom_Points.get(custom_point - 3).getLati(), list_Custom_Points.get(custom_point - 3).getLongi()))
                        .width(width)
                        .color(Color.parseColor(colour)));
                custom_point = -1;
                custom_polyline.add(line);
                list_Custom_Points.clear();
            }
            Marker m = mMap.addMarker(markerOptions);
            markers.add(m);
            custom_point++;
        }
        catch (Exception e){

        }
    }

    private void removeFreeLine() {
        try {
            if (list_Points.size() == 0) {
            } else if (list_Points.size() == 1) {
                removeMarker();
                list_Points.remove(list_Points.size() - 1);
                final_Coordinates.remove(final_Coordinates.size() - 1);
                free_point = 0;
            } else {
                free_polyline.get(free_polyline.size() - 1).remove();
                free_polyline.remove(free_polyline.size() - 1);
                removeMarker();
                list_Points.remove(list_Points.size() - 1);
                final_Coordinates.remove(final_Coordinates.size() - 1);
                free_point--;
            }
        }
        catch (Exception e){

        }
    }

    private void removeCustomLine() {try{
        if (final_Custom_Coordinates.size() == 0) {
        } else if (list_Custom_Points.size() == 1) {
            removeMarker();
            list_Custom_Points.remove(list_Custom_Points.size() - 1);
            final_Custom_Coordinates.remove(final_Custom_Coordinates.size() - 1);
            custom_point = 0;
        } else {
            if (final_Custom_Coordinates.size() % 4 == 0) {
                custom_polyline.get(custom_polyline.size() - 1).remove();
                custom_polyline.get(custom_polyline.size() - 2).remove();
                custom_polyline.remove(custom_polyline.size() - 1);
                custom_polyline.remove(custom_polyline.size() - 1);
                removeMarker();
                final_Custom_Coordinates.remove(final_Custom_Coordinates.size() - 1);
                list_Custom_Points.add(final_Custom_Coordinates.get(final_Custom_Coordinates.size() - 3));
                list_Custom_Points.add(final_Custom_Coordinates.get(final_Custom_Coordinates.size() - 2));
                list_Custom_Points.add(final_Custom_Coordinates.get(final_Custom_Coordinates.size() - 1));
                custom_point = 3;
            } else {
                custom_polyline.get(custom_polyline.size() - 1).remove();
                custom_polyline.remove(custom_polyline.size() - 1);
                removeMarker();
                final_Custom_Coordinates.remove(final_Custom_Coordinates.size() - 1);
                list_Custom_Points.remove(list_Custom_Points.size() - 1);
                custom_point--;
            }
        }
    }
    catch(Exception e){

    }
    }

    private void removeMarker() {
        try {
            for (Marker marker : markers) {
                marker.remove();
            }
            markers.clear();
        }
        catch (Exception e){

        }
    }

    private void makeReport() {
        try {
            int counter = 1;
            Report report = new Report("---------FREE HAND COORDINATE---------");
            reportList.add(report);
            for (CustomPosition c : final_Coordinates) {
                report = new Report("Latitue: " + c.getLati() + ", Longitude: " + c.getLongi() + ", Colour: " + c.getColour());
                reportList.add(report);
            }
            report = new Report("\n" + "------CUSTOM PATCH COORDINATE-----");
            reportList.add(report);
            for (CustomPosition c : final_Custom_Coordinates) {
                if (counter != 5) {
                    report = new Report("Latitue: " + c.getLati() + ", Longitude: " + c.getLongi() + ", Colour: " + c.getColour());
                    reportList.add(report);
                    counter++;
                } else {
                    report = new Report("*************************************");
                    reportList.add(report);
                    report = new Report("Latitue: " + c.getLati() + ", Longitude: " + c.getLongi() + ", Colour: " + c.getColour());
                    reportList.add(report);
                    counter = 1;
                }
            }
            reportAdapter.notifyDataSetChanged();
        }
        catch (Exception e){

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
        }
    }


    private void sendFreeHandCoordinate() {
        try {
            int value = 0;
            for (int i = 0; i < list_Points.size() - 1; i++) {
                value = value + (int) Math.floor((SphericalUtil.computeDistanceBetween(new LatLng(list_Points.get(i).getLati(), list_Points.get(i).getLongi()), new LatLng(list_Points.get(i + 1).getLati(), list_Points.get(i + 1).getLongi())) / 7.5));
            }
            List<AddParkingCoordinateRequest.CoordinatesBean> list = new ArrayList<>();
            for (CustomPosition c : list_Points) {
                AddParkingCoordinateRequest.CoordinatesBean coordinatesBean = new AddParkingCoordinateRequest.CoordinatesBean();
                coordinatesBean.setLat(c.getLati());
                coordinatesBean.setLng(c.getLongi());
                list.add(coordinatesBean);
            }
            AddParkingCoordinateRequest addParkingRequest = new AddParkingCoordinateRequest();
            addParkingRequest.setCoordinates(list);
            addParkingRequest.setName("Andheri East");
            addParkingRequest.setLandmark("Chandivali Road");
            addParkingRequest.setCity("Mumbai");
            addParkingRequest.setPincode(400072);
            addParkingRequest.setState("Maharashtra");
            addParkingRequest.setCapacity(value);
            addParkingRequest.setPatch_type("freehand");
            SharedPreferences preferencesReader = getSharedPreferences("Contractor", Context.MODE_PRIVATE);
            String customerid = preferencesReader.getString("Contractor ID", null);
            addParkingRequest.setId(customerid);
            addParkingRequest.setColor(list_Points.get(0).getColour());
            Log.e("LOOK HERE", new GsonBuilder().create().toJson(addParkingRequest));
            Call<AddParkingCoordinateResponse> parkingResponseCall = Util.getWebservices().addParkingPatch(addParkingRequest);
            parkingResponseCall.enqueue(new Callback<AddParkingCoordinateResponse>() {
                @Override
                public void onResponse(Call<AddParkingCoordinateResponse> call, Response<AddParkingCoordinateResponse> response) {
                    Toast.makeText(ContractorActivity.this, "Co-Ordinate Send Successfully", Toast.LENGTH_SHORT).show();
                    Log.e("HELLO THERE", new GsonBuilder().create().toJson(response.body()));
                }

                @Override
                public void onFailure(Call<AddParkingCoordinateResponse> call, Throwable t) {

                }
            });
            list_Points.clear();
            free_point = 0;
        }
        catch (Exception e){

        }
    }

    public void sendCordinate() {
        try {
            List<AddParkingCoordinateRequest.CoordinatesBean> list = new ArrayList<>();
            for (int i = 0; i < final_Custom_Coordinates.size(); i++) {
                AddParkingCoordinateRequest.CoordinatesBean coordinatesBean = new AddParkingCoordinateRequest.CoordinatesBean();
                coordinatesBean.setLat(final_Custom_Coordinates.get(i).getLati());
                coordinatesBean.setLng(final_Custom_Coordinates.get(i).getLongi());
                list.add(coordinatesBean);
                if ((i + 1) % 4 == 0) {
                    AddParkingCoordinateRequest addParkingRequest = new AddParkingCoordinateRequest();
                    addParkingRequest.setCoordinates(list);
                    addParkingRequest.setName("Andheri East");
                    addParkingRequest.setLandmark("Chandivali Road");
                    addParkingRequest.setCity("Mumbai");
                    addParkingRequest.setPincode(400072);
                    addParkingRequest.setState("Maharashtra");
                    SharedPreferences preferencesReader = getSharedPreferences("Contractor", Context.MODE_PRIVATE);
                    String customerid = preferencesReader.getString("Contractor ID", null);
                    addParkingRequest.setId(customerid);
                    addParkingRequest.setColor(final_Custom_Coordinates.get(i).getColour());
                    list = new ArrayList<>();
                    Call<AddParkingCoordinateResponse> parkingResponseCall = Util.getWebservices().addParkingPatch(addParkingRequest);
                    parkingResponseCall.enqueue(new Callback<AddParkingCoordinateResponse>() {
                        @Override
                        public void onResponse(Call<AddParkingCoordinateResponse> call, Response<AddParkingCoordinateResponse> response) {
                            AddParkingCoordinateResponse jsonresponse = response.body();
                            if (jsonresponse != null) {
                            }
                        }

                        @Override
                        public void onFailure(Call<AddParkingCoordinateResponse> call, Throwable t) {
                        }
                    });
                }
            }
            final_Custom_Coordinates.clear();
            Toast.makeText(ContractorActivity.this, "Co-Ordinate Sent Successsfully", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(ContractorActivity.this, "Co-Ordinate Sent Unuccesssfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void addCoordinate() {

    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            ContractorActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

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
        mLocationRequest.setFastestInterval(500);
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
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //stop location updates
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


