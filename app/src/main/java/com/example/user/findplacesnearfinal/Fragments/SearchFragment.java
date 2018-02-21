package com.example.user.findplacesnearfinal.Fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.findplacesnearfinal.Model.allResults;
import com.example.user.findplacesnearfinal.R;
import com.example.user.findplacesnearfinal.Service.GitHubService;
import com.example.user.findplacesnearfinal.remote.RetrofitClient;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_fragment, container, false);

        GitHubService apiService = RetrofitClient.getClient().create(GitHubService.class);

        Call<allResults> repos = apiService.getTextSearchResults("pizza", "AIzaSyBwpg6a0MQuMKzVTHlwzCmhTksktUCqHf8");

        repos.enqueue(new Callback<allResults>() {
            @Override
            public void onResponse(Call<allResults> call, Response<allResults> response) {

                Toast.makeText(getActivity(), "its works(:", Toast.LENGTH_SHORT).show();

                /// do sum(:
                //


            }

            @Override
            public void onFailure(Call<allResults> call, Throwable t) {

                Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

}
