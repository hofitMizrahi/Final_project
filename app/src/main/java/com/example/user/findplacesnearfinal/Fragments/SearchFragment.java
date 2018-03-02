package com.example.user.findplacesnearfinal.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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

public class SearchFragment extends Fragment {


    private String API_KEY = "AIzaSyBwpg6a0MQuMKzVTHlwzCmhTksktUCqHf8";

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
        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            seekBar.setVisibility(View.VISIBLE);
            myApiManager = "nearBy";
            locationBtn.setBackgroundResource(R.drawable.location_green);
        };

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if gps is ON (R.drawable.location_green) --> the locationButton change to OFF
                if(locationBtn.getBackground().getConstantState()==getResources().getDrawable(R.drawable.location_green).getConstantState()){

                    locationBtn.setBackgroundResource(R.drawable.not_location);
                    seekBar.setVisibility(View.INVISIBLE);
                    myApiManager = "text";

                    // if gps is on and the Btn show off
                }else if((ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED)
                        && locationBtn.getBackground().getConstantState()
                        == getResources().getDrawable(R.drawable.not_location).getConstantState())
                {

                    locationBtn.setBackgroundResource(R.drawable.location_green);
                    seekBar.setVisibility(View.VISIBLE);
                    myApiManager = "nearBy";
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


                        Call<allResults> repos = apiService.getTextSearchResults(textEnteredByTheUser, API_KEY);

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

                        String latlonStringLocation = MainActivity.lastKnowLoc;
                        String radius = "500";

                        repos = apiService.getNearbyResults(latlonStringLocation, radius, textEnteredByTheUser, API_KEY);

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


                //if user use the GPS near location
//                if (myApiManager == true) {
//
//
//                    //AIzaSyCaoMlFGzsyd_OuCbObmoL-4tqhDjLqySU
//
//

        });

        return myView;
    }

    //get user enter text to search
    private String getUserText(){

        searchTXT = myView.findViewById(R.id.searchtext_ET);

        if(searchTXT.getText().toString() == null){
            return null;
        }
        return searchTXT.getText().toString();
    }
}
