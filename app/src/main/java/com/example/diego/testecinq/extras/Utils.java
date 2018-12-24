package com.example.diego.testecinq.extras;

import android.text.TextUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Utils {
    public static void toolbarSettings (AppCompatActivity activity, String title, Toolbar toolbar, boolean showIcons){
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(title);

        if(showIcons){
            //activity.getSupportActionBar().setLogo(R.drawable.logo_bco);
            activity.getSupportActionBar().setDisplayUseLogoEnabled(true);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }else {
            toolbar.setLogo(null);
            activity.getSupportActionBar().setHomeButtonEnabled(true);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    public static void toolbarMain (AppCompatActivity activity, String title, Toolbar toolbar, boolean showIcons){
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle(title);

        if(showIcons){
            //activity.getSupportActionBar().setLogo(R.drawable.logo_bco);
//            activity.getSupportActionBar().setDisplayUseLogoEnabled(true);
//            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }else {
            toolbar.setLogo(null);
//            activity.getSupportActionBar().setHomeButtonEnabled(true);
//            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    //VALIDATION
    public static boolean isValidEmail(String target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
            return true;
        }
    }
}
