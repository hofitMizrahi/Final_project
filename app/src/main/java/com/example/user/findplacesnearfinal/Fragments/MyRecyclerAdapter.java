package com.example.user.findplacesnearfinal.Fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.findplacesnearfinal.Model.Place;
import com.example.user.findplacesnearfinal.R;

import java.util.ArrayList;

class MyRecyclerAdapter extends RecyclerView.Adapter <MyRecyclerAdapter.myViewHolder> {

    ArrayList<Place> placeArrayList;
    Context context;

    public MyRecyclerAdapter(ArrayList<Place> placeArrayList, Context context) {
        this.placeArrayList = placeArrayList;
        this.context = context;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_item, null);//getContext refers to get value of context variable
        myViewHolder viewHolder = new myViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(myViewHolder singleItem, int position) {

        Place place = placeArrayList.get(position);
        singleItem.bindMyCityData(place);
    }

    @Override
    public int getItemCount() {
        return placeArrayList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{

        View holderView;

         public myViewHolder(View itemView) {
         super(itemView);

         this.holderView = itemView;
        }

        public void bindMyCityData(Place place) {


            TextView title = holderView.findViewById(R.id.title_TV);
            title.setText(place.getName());


        }
    }
}
