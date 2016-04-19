package com.mikchaelsoloviev.musiccatalog.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.mikchaelsoloviev.musiccatalog.R;


public class NetworkUtils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected();
        } else {
            showSnackBar(context);
            return false;
        }
    }

    private static void showSnackBar(Context context) {
        View parent = ((Activity) context).getWindow()
                .getDecorView().findViewById(android.R.id.content);
        final Snackbar snackbar = Snackbar.make(parent,
                context.getString(R.string.no_network_connection), Snackbar.LENGTH_INDEFINITE);
        View view = snackbar.getView();
        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }
}
