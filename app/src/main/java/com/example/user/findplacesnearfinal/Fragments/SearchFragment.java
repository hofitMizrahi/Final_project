package com.example.user.findplacesnearfinal.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.user.findplacesnearfinal.Activity.MainActivity;
import com.example.user.findplacesnearfinal.Adapters.MyRecyclerAdapter;
import com.example.user.findplacesnearfinal.Helper.SwipeController;
import com.example.user.findplacesnearfinal.Model.Place;
import com.example.user.findplacesnearfinal.Model.allResults;
import com.example.user.findplacesnearfinal.R;
import com.example.user.findplacesnearfinal.Service.GitHubService;
import com.example.user.findplacesnearfinal.remote.RetrofitClient;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;
import static com.example.user.findplacesnearfinal.Activity.MainActivity.isPermissionToLocation;

public class SearchFragment extends Fragment implements LocationListener{

    private String API_KEY = "AIzaSyBwpg6a0MQuMKzVTHlwzCmhTksktUCqHf8";
    private final int REQUEST_CODE = 9;


    LocationManager locationManager;
    String lastKnowLoc;
    /**
     * @value -  false , search by text
     * Call<allResults> repos = apiService.getTextSearchResults(textEnteredByTheUser, "AIzaSyDo6e7ZL0HqkwaKN-GwKgqZnW03FhJNivQ");
     *
     * @value - true , search by location
     * Call<allResults> repos = apiService.getTextSearchResults(textEnteredByTheUser, "AIzaSyBwpg6a0MQuMKzVTHlwzCmhTksktUCqHf8");
     */
    private String  myApiManager = "text";

    View myView;
    RecyclerView recyclerView;
    EditText searchTXT;
    Button locationBtn;
    SeekBar seekBar;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         //inflate the layout
        myView = inflater.inflate(R.layout.search_fragment, container, false);

        //initialization the RecyclerView, the location button, seekBar
        recyclerView = myView.findViewById(R.id.myList_RV);
        locationBtn = myView.findViewById(R.id.locationChangeBtn);
        seekBar = myView.findViewById(R.id.seekBar);

//-------------------------------------------------------------------------------------------------------------------

        /**
         * initialization the SeekBar ,check if there are permissions for GPS
         * if i have permission to the location, the Btn will be green
         */
        // if i have permission && GPS is on -- boolean
        if(isPermissionToLocation) {


        }

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if gps image is ON (R.drawable.location_green) --> the locationButton change to OFF
                if(locationBtn.getBackground().getConstantState() == getResources().getDrawable(R.drawable.location_green).getConstantState()){

                    locationBtn.setBackgroundResource(R.drawable.not_location);
                    seekBar.setVisibility(View.INVISIBLE);
                    myApiManager = "text";

                      // if gps image is off and the user wont it be ON && have permission && GPS --> the locationButton will change to ON
                }else if(locationBtn.getBackground().getConstantState() == getResources().getDrawable(R.drawable.not_location).getConstantState() && isPermissionToLocation)
                {
                    locationBtn.setBackgroundResource(R.drawable.location_green);
                    seekBar.setVisibility(View.VISIBLE);
                    myApiManager = "nearBy";
                }else {

                }
            }
        });

//--------------------------------------------------------------------------------------------------------------------

        /**
         * when the user click on the search button, start retrofit connection
         * Depends if the location service is on or off
         */
        ImageView imageView = myView.findViewById(R.id.search_image);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // take the text that the user enter to EditText
                String textEnteredByTheUser = getUserText();

                //if user don't write nothing
                if (textEnteredByTheUser.equals("")) {

                    Toast.makeText(getActivity(), "please enter something", Toast.LENGTH_SHORT).show();
                }

                GitHubService apiService = RetrofitClient.getClient().create(GitHubService.class);

                switch (myApiManager){

                    //if user wont to search by text and don't wont to use GPS!!
                    case "text":

                        Toast.makeText(getActivity(), "text", Toast.LENGTH_SHORT).show();


                        Call<allResults> repos = apiService.getTextSearchResults("pizza in jerusalem", API_KEY);

                        repos.enqueue(new Callback<allResults>() {

                            @Override
                            public void onResponse(Call<allResults> call, Response<allResults> response) {

                                Toast.makeText(getActivity(), "its works(:", Toast.LENGTH_SHORT).show();

                                /// do sum(:

                                if (response.isSuccessful()) {

                                    ArrayList<Place> myData = new ArrayList<>();

                                    allResults results = response.body();

                                    myData.addAll(results.getResults());

                                    if (response.isSuccessful() && myData.isEmpty()) {
                                        Toast.makeText(getActivity(), "No Results - no data in the array", Toast.LENGTH_SHORT).show();//TOAST MESSAGE IF WE HAVE JSON WITH ZERO RESULTS
                                    } else {

                                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//LinearLayoutManager, GridLayoutManager ,StaggeredGridLayoutManagerFor defining how single row of recycler view will look .  LinearLayoutManager shows items in horizontal or vertical scrolling list. Don't confuse with type of layout you use in xml
                                        //setting txt adapter
                                        RecyclerView.Adapter Adapter = new MyRecyclerAdapter(myData, getActivity(), "text");
                                        recyclerView.setAdapter(Adapter);

                                        SwipeController swipeController = new SwipeController();
                                        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
                                        itemTouchhelper.attachToRecyclerView(recyclerView);
                                        Adapter.notifyDataSetChanged();//refresh
                                    }

                                }

                            }


                            @Override
                            public void onFailure(Call<allResults> call, Throwable t) {

                                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        break;

                    case "nearBy":

                        Toast.makeText(getActivity(), "nearBy", Toast.LENGTH_SHORT).show();

                        String radius = "1000";

                        repos = apiService.getNearbyResults(lastKnowLoc, radius, textEnteredByTheUser, API_KEY);

                        repos.enqueue(new Callback<allResults>() {

                        @Override
                        public void onResponse(Call<allResults> call, Response<allResults> response) {

                            Toast.makeText(getActivity(), "its works(:", Toast.LENGTH_SHORT).show();

                            /// do sum(:

                            if (response.isSuccessful()) {

                                ArrayList<Place> myData = new ArrayList<>();

                                allResults results = response.body();

                                myData.addAll(results.getResults());

                                if (response.isSuccessful() && myData.isEmpty()) {
                                    Toast.makeText(getActivity(), "No Results - no data in the array", Toast.LENGTH_SHORT).show();//TOAST MESSAGE IF WE HAVE JSON WITH ZERO RESULTS
                                } else {

                                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//LinearLayoutManager, GridLayoutManager ,StaggeredGridLayoutManagerFor defining how single row of recycler view will look .  LinearLayoutManager shows items in horizontal or vertical scrolling list. Don't confuse with type of layout you use in xml
                                    //setting txt adapter
                                    RecyclerView.Adapter Adapter = new MyRecyclerAdapter(myData, getActivity(), "nearBy");
                                    recyclerView.setAdapter(Adapter);

                                    SwipeController swipeController = new SwipeController();
                                    ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
                                    itemTouchhelper.attachToRecyclerView(recyclerView);
                                    Adapter.notifyDataSetChanged();//refresh
                                }

                            }

                        }

                        @Override
                        public void onFailure(Call<allResults> call, Throwable t) {

                            }
                        });

                    }

                }

        });

//--------------------------------------------------------------------------------------------------------------------

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        checkLocationPermission();

        if(checkLocationPermission() == true){

            seekBar.setVisibility(View.VISIBLE);
            myApiManager = "nearBy";
            locationBtn.setBackgroundResource(R.drawable.location_green);
            getLocation();
        }




        return myView;
    }

    /**
     * location Permission
     */
    public boolean checkLocationPermission() {

        // if i don't have permission
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE);

            isPermissionToLocation = false;

            return false;

            //if i don't have GPS
        }else if(!isGpsEnaled()){

            // ask for gps and sent to it by intent
            isPermissionToLocation = false;

            return false;

            //if i have permission && GPS is on -- get the current location
        } else {

            isPermissionToLocation = true;
            getLocation();

            return true;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            locationManager.removeUpdates(this);
        }
    }

    //get user enter text to search
    private String getUserText(){

        searchTXT = myView.findViewById(R.id.searchtext_ET);
        return searchTXT.getText().toString();
    }

    @Override
    public void onLocationChanged(Location location) {

        if (location==null) {

        }else{
            String lastKnowLoc = location.getLatitude() + "," + location.getLongitude();
            //Will be called every time location gets updated
            Log.i("LOC", "lat: " + location.getLatitude() + " lon:" + location.getLongitude());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

        locationBtn.setBackgroundResource(R.drawable.location_green);
        seekBar.setVisibility(View.VISIBLE);
        myApiManager = "nearBy";
    }

    @Override
    public void onProviderDisabled(String provider) {

        locationBtn.setBackgroundResource(R.drawable.not_location);
        seekBar.setVisibility(View.INVISIBLE);
        myApiManager = "text";
    }


    private boolean isGpsEnaled(){

        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Toast.makeText(getActivity(), "GPS is disable!", Toast.LENGTH_LONG).show();
            return false;
        }
        Toast.makeText(getActivity(), "GPS is Enable!", Toast.LENGTH_LONG).show();
        return true;
    }

    @SuppressLint("MissingPermission")
    public void getLocation(){


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);

        Location myLocation = ((LocationManager) getActivity().getSystemService(LOCATION_SERVICE)).getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(myLocation == null){

            Toast.makeText(getActivity(), "location not available :(", Toast.LENGTH_SHORT).show();

        }else {

            Toast.makeText(getActivity(), "lat: " + myLocation.getLatitude() + " lon:" + myLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            lastKnowLoc = myLocation.getLatitude() + "," + myLocation.getLongitude();

        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 9: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    {

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000, 0, this);

                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }
}
