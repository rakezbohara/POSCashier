package com.app.rakez.poscashier;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by RAKEZ on 2/26/2017.
 */
public class MainDecider extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPrefCashier", 0); // 0 - for private mode
        if(pref.contains("login")){
            Log.d("asd0","Data is "+pref.getString("login",null));
            Intent in = new Intent(getApplicationContext(),CashierHome.class);
            startActivity(in);
            finish();

        } else{
            Intent in = new Intent(getApplicationContext(),ScrollingActivity.class);
            startActivity(in);
            finish();
        }
    }
}
