package com.mikchaelsoloviev.musiccatalog.HostActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.mikchaelsoloviev.musiccatalog.Fragments.ArtistFragment;
import com.mikchaelsoloviev.musiccatalog.Fragments.ArtistsListFragment;
import com.mikchaelsoloviev.musiccatalog.R;

public class MainActivity extends AppCompatActivity implements ArtistsListFragment.IListItemSettable {
    public static final String LOG_TAG = "MC_MainActivity";
    public static final String KEY_EXTRA_ARTIST_ID = "KEY Extra Artist ID";
    public static final String KEY_EXTRA_ARTIST_NAME = "KEY Extra Artist Name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG,"onCreate ");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.title_main_activity));
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.fragment_container_list) == null) {
            Log.d(LOG_TAG,"onCreate fragmentManager.findFragmentById(R.id.fragment_container_list) == null");
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container_list, ArtistsListFragment.newInstance())
                    .commit();
        }
    }


    @Override
    public void setItem(long itemId,String...params) {

        Log.d(LOG_TAG,"setItem ");
        if (findViewById(R.id.fragment_container_item) == null) {
            Log.d(LOG_TAG,"setItem findViewById(R.id.fragment_container_item) == null");
            Intent intent=new Intent(this, ArtistActivity.class);
            intent.putExtra(KEY_EXTRA_ARTIST_ID, itemId);
            intent.putExtra(KEY_EXTRA_ARTIST_NAME, params[0]);
            startActivity(intent);

        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Log.d(LOG_TAG,"setItem findViewById(R.id.fragment_container_item) != null");
            fragmentTransaction.replace(R.id.fragment_container_item, ArtistFragment.newInstance(itemId))
                    .commit();
        }


    }
}



