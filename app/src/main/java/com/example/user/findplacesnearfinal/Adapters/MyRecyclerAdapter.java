package com.example.user.findplacesnearfinal.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.findplacesnearfinal.Model.Place;
import com.example.user.findplacesnearfinal.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyRecyclerAdapter extends RecyclerView.Adapter <MyRecyclerAdapter.myViewHolder> {

    private String myAPIsManager = "text";

    ArrayList<Place> placeArrayList;
    Context context;

    public MyRecyclerAdapter(ArrayList<Place> placeArrayList, Context context, String myApisManager) {
        this.placeArrayList = placeArrayList;
        this.context = context;
        this.myAPIsManager = myApisManager;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_item_model, null);//getContext refers to get value of context variable
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

        @SuppressLint("ResourceType")
        public void bindMyCityData(Place place) {

            // title
            TextView title = holderView.findViewById(R.id.title_TV);
            title.setText(place.getName());

            // address
            TextView address = holderView.findViewById(R.id.address_TV);

            switch (myAPIsManager){

                case "text":
                    address.setText(place.getFormatted_address());

                    break;
                case "nearBy":
                    address.setText(place.getVicinity().toString());

            }

            // open or close - boolean
            TextView openOrCloseSing = holderView.findViewById(R.id.openOrCloseSing);

            //if don't have open hours array at all
            if(place.getOpening_hours() == null){

                // don't show this text
                openOrCloseSing.setVisibility(View.INVISIBLE);
            }else if (place.getOpening_hours() != null && place.getOpening_hours().isOpen_now() == true) {

                openOrCloseSing.setBackgroundResource(R.drawable.open_shape);
                openOrCloseSing.setText(R.string.open);
            } else if (place.getOpening_hours() != null && place.getOpening_hours().isOpen_now() == false) {
                openOrCloseSing.setText(R.string.closed);
                openOrCloseSing.setBackgroundResource(R.drawable.close_shape);
            }



            //image resource
            ImageView imageView = holderView.findViewById(R.id.imageView);

            //check if there is any image resource in the Photo list array
            if(place.getPhotos() != null && place.getPhotos().get(0).getPhoto_reference() != null){

                String reference = place.getPhotos().get(0).getPhoto_reference();


                String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + reference + "&key=AIzaSyBwpg6a0MQuMKzVTHlwzCmhTksktUCqHf8";
                Picasso.with(context)
                        .load(url)
                        .resize(90, 90)
                        .centerCrop()
                        .into(imageView);
            }
        }
    }
}
