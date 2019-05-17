package com.example.calorietracker;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    View vMaps;
    GoogleMap gMap;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vMaps = inflater.inflate(R.layout.fragment_maps, container, false);

        MapFragment maps = (MapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        maps.getMapAsync(this);
    return vMaps;
    }

    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        SharedPreferences sharedPref = getActivity().getSharedPreferences("users", Context.MODE_PRIVATE);
        String address = sharedPref.getString("address", null);
        String postcode = sharedPref.getString("postcode", null);

        Geocoder geocoder = new Geocoder(this.getActivity());

        try
        {
            List<Address> addressList = geocoder.getFromLocationName(address + "," + postcode,1);
            Address location = addressList.get(0);
            LatLng myLocation = new LatLng(location.getLatitude(),location.getLongitude());
            gMap.addMarker(new MarkerOptions().position(myLocation).title("I'm Here"));
            //gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15.0f));
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,10.0f));

            String proximity = "5";
            String places = "park";
            Parks parks = new Parks();
            parks.execute(Double.toString(location.getLatitude()),Double.toString(location.getLongitude()),proximity,places);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }



    }

    private class Parks extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params) {
            String result = GoogleParks.search(params[0],params[1],params[2],params[3]);
            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {
            final List<Park> parkList = GoogleParks.getPlaces(result);

            //int i = parkList.size();

            for(int i = 0, l = parkList.size(); i<l; i++)
            {
                LatLng location = new LatLng(parkList.get(i).getLatitude(), parkList.get(i).getLongitude());
                String snippet = parkList.get(i).getName();
                gMap.addMarker(new MarkerOptions().position(location).title(snippet).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).setTag(i);
            }

            gMap.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Object markerObj = marker.getTag();
                    if(markerObj != null) {
                        int position = (int) (markerObj);
                        new AlertDialog.Builder(getActivity()).setTitle("Park details").setMessage(parkList.get(position)
                                .getName()).setPositiveButton("Ok", null).show();
                    }
            //Using position get Value from arraylist
                    return false;

        }
        });

        }

    }


}


