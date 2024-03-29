package com.journaldev.reversegeocoding;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.cardview.widget.CardView;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    TextView txtLocationAddress;
    EditText mLatiTxt;
    EditText mLongTiTxt;
    Button mGoBtn;
    SupportMapFragment mapFragment;
    GoogleMap map;
    LatLng center;
    CardView cardView;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLocationAddress = findViewById(R.id.txtLocationAddress);
        mLatiTxt = findViewById(R.id.main_lati_txt);
        mLongTiTxt = findViewById(R.id.main_longi_txt);
        mGoBtn = findViewById(R.id.main_go_btn);
        txtLocationAddress.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        txtLocationAddress.setSingleLine(true);
        txtLocationAddress.setMarqueeRepeatLimit(-1);
        txtLocationAddress.setSelected(true);

        cardView = findViewById(R.id.cardView);

        mGoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLatiTxt.getText() != null && mLongTiTxt.getText() != null) {
//                    getAddressFromLocation(21.017002, 105.783795);
                    getAddressFromLocation(Double.valueOf(mLatiTxt.getText().toString()),
                            Double.valueOf(mLongTiTxt.getText().toString()));

                }
            }
        });

//        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
//        mapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                map = googleMap;
//                map.getUiSettings().setZoomControlsEnabled(true);
//
//                LatLng latLng = new LatLng(21.017002, 105.783795);
//                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
//                initCameraIdle();
//            }
//
//        });
//
//
//        cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                try {
//                    Intent intent =
//                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
//                                    .build(MainActivity.this);
//                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
//                } catch (GooglePlayServicesRepairableException e) {
//                    printToast("Google Play Service Repair");
//                } catch (GooglePlayServicesNotAvailableException e) {
//                    printToast("Google Play Service Not Available");
//                }
//            }
//        });
    }

    private void initCameraIdle() {
        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                center = map.getCameraPosition().target;
                getAddressFromLocation(center.latitude, center.longitude);
            }
        });
    }

    private void getAddressFromLocation(double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);


        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);


            if (addresses.size() > 0) {
                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();

//                Log.d("Address", fetchedAddress.getMaxAddressLineIndex() + "");
//                fetchedAddress.getThoroughfare() + fetchedAddress.getSubAdminArea() + fetchedAddress.getAdminArea() + fetchedAddress.getCountryName()
//                for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); i++) {
//                    strAddress.append(fetchedAddress.getAddressLine(i)).append(" ");
//                }
                strAddress.append(fetchedAddress.getThoroughfare() + ", ");
                strAddress.append(fetchedAddress.getSubAdminArea() + ", ");
                strAddress.append(fetchedAddress.getAdminArea() + ", ");
                strAddress.append(fetchedAddress.getCountryName() + ", ");

                Log.d("Address", strAddress.toString());

                txtLocationAddress.setText(strAddress.toString());

            } else {
                txtLocationAddress.setText("Searching Current Address");
            }

        } catch (IOException e) {
            e.printStackTrace();
            printToast("Could not get address..!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                if (!place.getAddress().toString().contains(place.getName())) {
                    txtLocationAddress.setText(place.getName() + ", " + place.getAddress());
                } else {
                    txtLocationAddress.setText(place.getAddress());
                }

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 16);
                map.animateCamera(cameraUpdate);


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                printToast("Error in retrieving place info");

            }
        }
    }

    private void printToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
