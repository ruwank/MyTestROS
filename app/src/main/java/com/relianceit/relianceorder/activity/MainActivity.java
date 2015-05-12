package com.relianceit.relianceorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.VolleyError;
import com.relianceit.relianceorder.AppController;
import com.relianceit.relianceorder.R;
import com.relianceit.relianceorder.db.ROSDbHelper;
import com.relianceit.relianceorder.services.GeneralServiceHandler;
import com.relianceit.relianceorder.util.AppDataManager;
import com.relianceit.relianceorder.util.AppUtils;
import com.relianceit.relianceorder.util.ConnectionDetector;
import com.relianceit.relianceorder.util.Constants;

import java.util.Calendar;
import java.util.Date;


public class MainActivity extends ActionBarActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int LOGIN_REQUEST_CODE = 1;
    public static final int HOME_REQUEST_CODE = 2;
    public static final int RESULT_LOGOUT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String logged = AppDataManager.getData(this, Constants.DM_LOGGED_KEY);
      //  downloadDailyData();
/*
        if (logged != null && logged.equalsIgnoreCase("yes")) {
            if (isPendingDataAvailable()) {
                loadHome();
            }else if (shouldShowDailySync()) {
                downloadDailyData();
            }else {
                loadHome();
            }
        }else {
            loadLogin();
        }
        */
        loadHome();
    }

    private void dailyDownloadFailed(int errorCode) {
        AppUtils.dismissProgressDialog();
        if (errorCode == 401) {
            AppUtils.showAlertDialog(this, "Oops!", "Login session expired. Please login again.");
            logout();
        }else {
            AppUtils.showAlertDialog(this, "Sync Failed!", "Data syncing failed due to Server error. Please try again.");
        }
    }

    private void downloadDailyData(){

        if (!ConnectionDetector.isConnected(this)) {
            AppUtils.showAlertDialog(this, Constants.MSG_NO_INTERNET_TITLE, Constants.MSG_NO_INTERNET_MSG);
        }else {
            GeneralServiceHandler generalServiceHandler = new GeneralServiceHandler(this);
            generalServiceHandler.doDailyContentUpdate(TAG, new GeneralServiceHandler.DailyUpdateListener() {
                @Override
                public void onDailyUpdateSuccess() {
                    AppUtils.dismissProgressDialog();
                    loadHome();
                }

                @Override
                public void onDailyUpdateErrorResponse(VolleyError error) {
                    dailyDownloadFailed(error.networkResponse != null ? error.networkResponse.statusCode : 500);
                }
            });
            AppUtils.showProgressDialog(this, "Daily stock is downloading...");
        }
    }

    private void loadHome() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivityForResult(intent, HOME_REQUEST_CODE);
    }

    private void loadLogin() {
        Intent intent = new Intent(getApplicationContext(),
                LoginActivity.class);
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }

    public void logout() {
        AppDataManager.saveData(getApplicationContext(), Constants.DM_ACCESS_TOKEN_KEY, "");
        AppDataManager.saveData(getApplicationContext(), Constants.DM_USERNAME_KEY, "");
        AppDataManager.saveData(getApplicationContext(), Constants.DM_LOGGED_KEY, "no");

        ROSDbHelper dbHelper = new ROSDbHelper(this);
        dbHelper.clearCustomerTable(this);
        dbHelper.clearStockTable(this);
        dbHelper.clearNewOrderItemTable(this);
        dbHelper.clearNewOrderTable(this);
        dbHelper.clearReturnOrderItemTable(this);
        dbHelper.clearReturnOrderTable(this);

        loadLogin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                downloadDailyData();
            }
        }else if (requestCode == HOME_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                finish();
            }else if (resultCode == RESULT_LOGOUT) {
                AppUtils.showAlertDialog(this, "Oops!", "Login session expired. Please login again.");
                logout();
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
        AppController.getInstance().cancelPendingRequests(TAG);
    }

    /*
    Daily Sync
     */
    private boolean shouldShowDailySync() {
        long timeMS = AppDataManager.getDataLong(this, Constants.DM_DAILY_SYNC_TIME_KEY);
        if (timeMS == 0) {
            return true;
        }
        Date lastSyncDate = new Date(timeMS);
        Date now = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastSyncDate);
        int lDate = calendar.get(Calendar.DATE);
        calendar.setTime(now);
        int nDate = calendar.get(Calendar.DATE);

        if (nDate != lDate) {
            return true;
        }else {
            return false;
        }
    }

    private boolean isPendingDataAvailable() {

        ROSDbHelper dbHelper = new ROSDbHelper(this);
        int newCount = dbHelper.getNewOrderCountPending(this);
        int retCount = dbHelper.getReturnOrderCountPending(this);

        int total = newCount + retCount;
        if (total > 0) {
            return true;
        }else {
            return false;
        }
    }
}
