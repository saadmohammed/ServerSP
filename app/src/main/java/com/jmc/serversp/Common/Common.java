package com.jmc.serversp.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Common {

    //Internet Connection Checking
    public static boolean isConnectedToInternet(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {

                for (int i = 0; i < info.length; i++) {

                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }


        }
        return false;
    }

    //Delete and Update
    public static final String DELETE = "Delete";
    public static final String UPDATE = "Update";

    public static final int PICK_IMAGE_REQUEST = 71;

}

