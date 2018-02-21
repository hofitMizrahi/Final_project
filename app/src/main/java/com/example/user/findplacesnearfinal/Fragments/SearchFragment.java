package com.example.user.findplacesnearfinal.Fragments;


import android.location.Location;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.findplacesnearfinal.Model.Geometry;
import com.example.user.findplacesnearfinal.Model.Place;
import com.example.user.findplacesnearfinal.Model.allResults;
import com.example.user.findplacesnearfinal.R;
import com.example.user.findplacesnearfinal.Service.GitHubService;
import com.example.user.findplacesnearfinal.remote.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment {

    RecyclerView recyclerView;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_fragment, container, false);

        recyclerView = v.findViewById(R.id.myList_RV);



        GitHubService apiService = RetrofitClient.getClient().create(GitHubService.class);

        Call<allResults> repos = apiService.getTextSearchResults("pizza", "AIzaSyBwpg6a0MQuMKzVTHlwzCmhTksktUCqHf8");

        repos.enqueue(new Callback<allResults>() {
            @Override
                public void onResponse(Call<allResults> call, Response<allResults> response) {

                Toast.makeText(getActivity(), "its works(:", Toast.LENGTH_SHORT).show();

                /// do sum(:
                //

                if (response.isSuccessful()) {

                    ArrayList<Place> myData = new ArrayList<>();

                    allResults results = response.body();

                    Toast.makeText(getActivity(),String.valueOf(response.body()),Toast.LENGTH_LONG).show();//TOAST MESSAGE IF WE HAVE JSON WITH ZERO RESULTS


                    if(results == null){

                        Toast.makeText(getActivity(),"Result == null",Toast.LENGTH_SHORT).show();//TOAST MESSAGE IF WE HAVE JSON WITH ZERO RESULTS

                    }

                    myData.addAll(results.getResults());

                    if (myData.isEmpty()) {
                        Toast.makeText(getActivity(),"No Results",Toast.LENGTH_SHORT).show();//TOAST MESSAGE IF WE HAVE JSON WITH ZERO RESULTS
                    }

                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//LinearLayoutManager, GridLayoutManager ,StaggeredGridLayoutManagerFor defining how single row of recycler view will look .  LinearLayoutManager shows items in horizontal or vertical scrolling list. Don't confuse with type of layout you use in xml
                    //setting txt adapter
                    RecyclerView.Adapter Adapter = new MyRecyclerAdapter(myData, getActivity());
                    recyclerView.setAdapter(Adapter);
                    Adapter.notifyDataSetChanged();//refresh

                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<allResults> call, Throwable t) {

                Toast.makeText(getActivity(), "DOT WORK", Toast.LENGTH_SHORT).show();
            }

        });

        return v;
    }

}
