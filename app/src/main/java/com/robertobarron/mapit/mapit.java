package com.robertobarron.mapit;

import java.io.IOException;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class mapit extends AppCompatActivity {
    private WebView myweb;

    GoogleCloudMessaging gcmObj;
    Context applicationContext;
    String regId = "";

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    AsyncTask<Void, Void, String> createRegIdTask;

    public static final String REG_ID = "regId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapit);

        String str = getIntent().getStringExtra("msg");

        myweb = (WebView) findViewById(R.id.webMapIt);
        myweb.getSettings().setJavaScriptEnabled(true);
        myweb.getSettings().setAppCacheEnabled(false);
        myweb.addJavascriptInterface(new WebAppInterface(), "mapItInterface");
        myweb.loadUrl("http://map.robertobarron.com/");
    }

    public class WebAppInterface {
        public String getId () {
            System.out.println("Se llamo a getId");
            if (checkPlayServices()) {
                try {
                    if (gcmObj == null) {
                        gcmObj = GoogleCloudMessaging.getInstance(applicationContext);
                    }
                    regId = gcmObj.register(ApplicationConstants.GOOGLE_PROJ_ID);
                    return regId;

                } catch (IOException ex) {
                    return "0";
                }
            }
            return "0";
        }
    }

    // When Application is resumed, check for Play services support to make sure app will be running normally
    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    // Check if Google Playservices is installed in Device or not
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(applicationContext, "This device doesn't support Play services, App will not work normally", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        } else {
            Toast.makeText(applicationContext, "This device supports Play services, App will work normally", Toast.LENGTH_LONG).show();
        }
        return true;
    }
}


// PUSH NOTIFICATION API KEY : AIzaSyB-TM4lW9Q8JepdK8esWu8Ka0ZIKtdodmk
// PROJECT ID: 904215730520