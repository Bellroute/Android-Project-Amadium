package com.pubak.econovation.amadium.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.pubak.econovation.amadium.R;
import com.pubak.econovation.amadium.activity.MainActivity;
import com.pubak.econovation.amadium.activity.MatchUserActivity;
import com.pubak.econovation.amadium.dto.UserDTO;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class SearchUserFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "SearchUserFragment";
    private GoogleMap map;
    private MapView mapView;
    private UserDTO user;
    private FirebaseDatabase firebaseDatabase;
    private Map<String, String> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_user, container, false);
        firebaseDatabase = firebaseDatabase.getInstance();
        data = new HashMap<String, String>();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        initDatabase();
    }

    private void getMarkers(final UserDTO userDTO) {
        Log.d(TAG, "getMarkers: userDTO" + userDTO);
        final LatLng markValue = new LatLng(userDTO.getLatitude(), userDTO.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(markValue)
                .title(userDTO.getUsername())
                .snippet("스포츠: " + userDTO.getSport() + "\n티어: " + userDTO.getTier() + "\n" + userDTO.getEmail());

        if (userDTO.getEmail().equals(MainActivity.getCurrentUser().getEmail())) {
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }
        map.addMarker(markerOptions).showInfoWindow();

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getContext(), MatchUserActivity.class);

                String[] split = marker.getSnippet().split("\n");
                Log.d(TAG, "onInfoWindowClick: split" + split);
                intent.putExtra("uid", data.get(split[2]));
                intent.putExtra("email", split[2]);
                intent.putExtra("userName", marker.getTitle());
                intent.putExtra("userSports", split[0]);
                intent.putExtra("userTier", split[1]);
                getContext().startActivity(intent);
            }
        });

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        if (userDTO.getEmail().equals(MainActivity.getCurrentUser().getEmail())) {
            user = userDTO;

            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(user.getLatitude(), user.getLongitude())));
            map.animateCamera(CameraUpdateFactory.zoomTo(14));
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) view.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    private void initDatabase() {
        firebaseDatabase.getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                UserDTO userDTO = dataSnapshot.getValue(UserDTO.class);
                Log.d(TAG, "onChildAdded: dataSnapShot get key: " + dataSnapshot.getKey());
                getMarkers(userDTO);
                data.put(userDTO.getEmail(), dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
