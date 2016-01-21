package com.relianceit.relianceorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.relianceit.relianceorder.AppController;
import com.relianceit.relianceorder.R;
import com.relianceit.relianceorder.util.AppDataManager;
import com.relianceit.relianceorder.util.Constants;


public class MainActivity extends ActionBarActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int LOGIN_REQUEST_CODE = 1;
    public static final int HOME_REQUEST_CODE = 2;
    public static final int RESULT_LOGOUT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppController.getInstance().fillUserUsingSavedData();
        AppDataManager.saveDataInt(this, Constants.DM_DAILY_SYNC_SHOWN_KEY, 0);

        String logged = AppDataManager.getData(this, Constants.DM_LOGGED_KEY);
        if (logged != null && logged.equalsIgnoreCase("yes")) {
            loadHome();
        }else {
            //loadHome();
           loadLogin();
        }
    }

    private void loadHome() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

            }
        }else if (requestCode == HOME_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

            }else if (resultCode == RESULT_LOGOUT) {

            }
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
