package com.relianceit.relianceorder.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.VolleyError;
import com.relianceit.relianceorder.R;
import com.relianceit.relianceorder.services.GeneralServiceHandler;
import com.relianceit.relianceorder.util.AppDataManager;
import com.relianceit.relianceorder.util.Constants;


public class MainActivity extends ActionBarActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String logged = AppDataManager.getData(this, Constants.DM_LOGGED_KEY);
        if (logged != null && logged.equalsIgnoreCase("yes")) {
            //loadHome();
        }else {
            //loadLogin();
        }

        loadLogin();

        //downloadDailyData();
    }

    private void downloadDailyData(){
        GeneralServiceHandler generalServiceHandler = new GeneralServiceHandler(this);
        generalServiceHandler.doDailyContentUpdate(TAG, new GeneralServiceHandler.DailyUpdateListener() {
            @Override
            public void onDailyUpdateSuccess() {
                loadHome();
            }

            @Override
            public void onDailyUpdateErrorResponse(VolleyError error) {

            }
        });
    }
    private void loadHome() {
        Intent intent = new Intent(getApplicationContext(),
                HomeActivity.class);
        startActivity(intent);
    }

    private void loadLogin() {
        Intent intent = new Intent(getApplicationContext(),
                LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
