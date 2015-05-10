package com.relianceit.relianceorder.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.VolleyError;
import com.relianceit.relianceorder.AppController;
import com.relianceit.relianceorder.R;
import com.relianceit.relianceorder.db.ROSDbHelper;
import com.relianceit.relianceorder.models.ROSNewOrder;
import com.relianceit.relianceorder.models.ROSReturnOrder;
import com.relianceit.relianceorder.services.GeneralServiceHandler;
import com.relianceit.relianceorder.services.NewOrderServiceHandler;
import com.relianceit.relianceorder.util.AppDataManager;
import com.relianceit.relianceorder.util.AppUtils;
import com.relianceit.relianceorder.util.ConnectionDetector;
import com.relianceit.relianceorder.util.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends ActionBarActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int LOGIN_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(localDataChangeReceiver, new IntentFilter(Constants.LocalDataChange.ACTION_ORDER_ADDED));
        registerReceiver(localDataChangeReceiver, new IntentFilter(Constants.LocalDataChange.ACTION_ORDER_SYNCED));
        registerReceiver(localDataChangeReceiver, new IntentFilter(Constants.LocalDataChange.ACTION_DAILY_SYNCED));

        String logged = AppDataManager.getData(this, Constants.DM_LOGGED_KEY);
        if (logged != null && logged.equalsIgnoreCase("yes")) {
            if (shouldShowDailySync()) {
                downloadDailyData();
            }else {
                loadHome();
            }
        }else {
            loadLogin();
        }
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
            AppUtils.showProgressDialog(this);
        }
    }

    private void loadHome() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
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
        dbHelper.clearProductTable(this);

        loadLogin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                downloadDailyData();
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
        unregisterReceiver(localDataChangeReceiver);
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

    /*
    Data Sync section
     */
    private BroadcastReceiver localDataChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constants.LocalDataChange.ACTION_ORDER_ADDED.equals(action)) {
                setPendingSyncButtonStatus();
            } else if (Constants.LocalDataChange.ACTION_ORDER_SYNCED.equals(action)) {
                setPendingSyncButtonStatus();
            } else if (Constants.LocalDataChange.ACTION_DAILY_SYNCED.equals(action)) {

            }
        }
    };

    private void setPendingSyncButtonStatus() {

        ROSDbHelper dbHelper = new ROSDbHelper(this);
        int newCount = dbHelper.getNewOrderCountPending(this);
        int retCount = dbHelper.getReturnOrderCountPending(this);

        int total = newCount + retCount;
        if (total > 0) {
            //TODO Enable button
        }else {
            //TODO Disable button
        }
    }


    private ArrayList<ROSNewOrder> pendingNewOrders = null;
    private ArrayList<ROSReturnOrder> pendingRetOrders = null;
    private void doSyncPendingOrders() {

        if (!ConnectionDetector.isConnected(this)) {
            AppUtils.showAlertDialog(this, Constants.MSG_NO_INTERNET_TITLE, Constants.MSG_NO_INTERNET_MSG);
        }else {
            ROSDbHelper dbHelper = new ROSDbHelper(this);
            this.pendingNewOrders = dbHelper.getNewOrdersPending(this);
            this.pendingRetOrders = dbHelper.getReturnOrdersPending(this);
            doSyncPendingOrdersContinue();
        }
    }

    private void orderSyncCompleted() {
        setPendingSyncButtonStatus();
        //TODO show message if needed
    }

    private void orderSyncFailed() {
        pendingNewOrders.clear();
        pendingRetOrders.clear();
        //TODO show message
    }

    private void doSyncPendingOrdersContinue() {
        if (pendingNewOrders.size() > 0) {
            ROSNewOrder newOrder = pendingNewOrders.get(0);
            doNewOrderSync(newOrder);
        }else if (pendingRetOrders.size() > 0) {
            ROSReturnOrder returnOrder = pendingRetOrders.get(0);
            doReturnOrderSync(returnOrder);
        } else {
            if(pendingNewOrders != null) pendingNewOrders.clear();
            if(pendingRetOrders != null) pendingRetOrders.clear();
            orderSyncCompleted();
        }
    }

    private void updateNewOrderOnSyncComplete(String orderId) {
        ROSDbHelper dbHelper = new ROSDbHelper(this);
        dbHelper.updateNewOrderStatusToSynced(this, orderId);
    }

    private void updateReturnOrderOnSyncComplete(String orderId) {
        ROSDbHelper dbHelper = new ROSDbHelper(this);
        dbHelper.updateReturnOrderStatusToSynced(this, orderId);
    }

    private void doNewOrderSync(ROSNewOrder newOrder) {
        NewOrderServiceHandler newOrderServiceHandler = new NewOrderServiceHandler(this);
        newOrderServiceHandler.syncNewOrder(newOrder, TAG, new NewOrderServiceHandler.NewOrderSyncListener() {
            @Override
            public void onOrderSyncSuccess(String orderId) {
                pendingNewOrders.remove(0);
                updateNewOrderOnSyncComplete(orderId);
                doSyncPendingOrdersContinue();
            }

            @Override
            public void onOrderSyncError(String orderId, VolleyError error) {
                orderSyncFailed();
            }
        });
    }

    private void doReturnOrderSync(ROSReturnOrder returnOrder) {

    }
}
