package com.mikchaelsoloviev.musiccatalog.HostActivity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.mikchaelsoloviev.musiccatalog.Fragments.ArtistFragment;
import com.mikchaelsoloviev.musiccatalog.R;

public class ArtistActivity extends AppCompatActivity {

    public static final String LOG_TAG = "MC_ArtistActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!getSupportFragmentManager().popBackStackImmediate()) {
                        supportFinishAfterTransition();
                    }

                }
            });
        }
        long artistId = getIntent().getLongExtra(MainActivity.KEY_EXTRA_ARTIST_ID, 0);
        setTitle(getIntent().getStringExtra(MainActivity.KEY_EXTRA_ARTIST_NAME));
        Log.d(LOG_TAG, "onCreate " + artistId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.fragment_container_item) == null) {
            Log.d(LOG_TAG, "onCreate   if (fragmentManager.findFragmentById(R.id.fragment_container_item) == null)");
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container_item, ArtistFragment.newInstance(artistId))
                    .commit();
        }
    }

}
