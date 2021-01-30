package com.anypli.megamall.ar.core.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LocalisationPermissionHelper {

    private static String[] permissions = { Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

    /** Check to see we have the necessary permissions for this app. */
    public static boolean hasLocalisationPermission(Activity activity) {
        return (ContextCompat.checkSelfPermission(activity, permissions[0])
                == PackageManager.PERMISSION_GRANTED )&& (ContextCompat.checkSelfPermission(activity, permissions[1])
                == PackageManager.PERMISSION_GRANTED );
    }

    /** Check to see we have the necessary permissions for this app, and ask for them if we don't. */
    public static void requestLocalisationPermission(Activity activity) {
        ActivityCompat.requestPermissions(
                activity, permissions, 0);
    }

}
