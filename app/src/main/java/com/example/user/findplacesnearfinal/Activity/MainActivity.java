package com.example.user.findplacesnearfinal.Activity;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.findplacesnearfinal.Fragments.FragmentB;
import com.example.user.findplacesnearfinal.R;
import com.example.user.findplacesnearfinal.Fragments.SearchFragment;
import com.example.user.findplacesnearfinal.Service.GitHubService;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        screenPositionOrder();




    }

    /**
     * method that Initializing the layouts by the device orientation and if its mobile or tablet.
     */

    public void screenPositionOrder(){

        //if its mobile and portrait
        if(!isTablet(this) && isPortrait()){

            SearchFragment searchFragment = new SearchFragment();
            getFragmentManager().beginTransaction().addToBackStack("replacing").replace(R.id.main_portrait_layout, searchFragment).commit();

            //if its mobile and landscape OR if its tablet
        }else if(!isTablet(this) && !isPortrait() || isTablet(this)){

            SearchFragment searchFragment = new SearchFragment();
            getFragmentManager().beginTransaction().addToBackStack("replacing").replace(R.id.search_tablet_layout, searchFragment).commit();

            FragmentB fragmentB = new FragmentB();
            getFragmentManager().beginTransaction().addToBackStack("replacing").replace(R.id.tablet_map_layout, fragmentB).commit();
        }
    }

    /**
     * Checks if the device is a tablet (7" or greater).
     */
    private boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * checks the orientation of the screen.
     */
    private boolean isPortrait(){

        // landscape checker
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

            return false;
        }
        // else is portrait
        return true;
    }
}
