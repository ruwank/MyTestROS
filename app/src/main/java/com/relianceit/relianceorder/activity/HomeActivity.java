package com.relianceit.relianceorder.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.relianceit.relianceorder.AppController;
import com.relianceit.relianceorder.R;
import com.relianceit.relianceorder.appsupport.tab.SlidingTabLayout;
import com.relianceit.relianceorder.db.ROSDbHelper;
import com.relianceit.relianceorder.fragment.CustomerOutstandingFragment;
import com.relianceit.relianceorder.fragment.RelianceOperationFragment;
import com.relianceit.relianceorder.fragment.StockStatementFragment;
import com.relianceit.relianceorder.models.ROSNewOrder;
import com.relianceit.relianceorder.models.ROSReturnOrder;
import com.relianceit.relianceorder.models.ROSUser;
import com.relianceit.relianceorder.models.ROSVisit;
import com.relianceit.relianceorder.services.GeneralServiceHandler;
import com.relianceit.relianceorder.services.NewOrderServiceHandler;
import com.relianceit.relianceorder.services.ReturnOrderServiceHandler;
import com.relianceit.relianceorder.util.AppDataManager;
import com.relianceit.relianceorder.util.AppURLs;
import com.relianceit.relianceorder.util.AppUtils;
import com.relianceit.relianceorder.util.ConnectionDetector;
import com.relianceit.relianceorder.util.Constants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends RelianceBaseActivity {

    public static final String TAG = HomeActivity.class.getSimpleName();

    private AlertDialog logoutAlertDialog = null;
    private AlertDialog exitAlertDialog = null;
    private boolean exitConfirmed = false;

     /**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
    private Toolbar toolbar;
    SlidingTabLayout mTab;
    ViewPager mPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

        mTab =(SlidingTabLayout)findViewById(R.id.sliding_tabs);
        mTab.setCustomTabView(R.layout.custom_tab, 0);

        mPager=(ViewPager)findViewById(R.id.view_pager);
        mPager.setAdapter(new sectionPageAdapter(getSupportFragmentManager()));
        mTab.setViewPager(mPager);
        customizeActionBar(0);

        registerReceiver(localDataChangeReceiver, new IntentFilter(Constants.LocalDataChange.ACTION_ORDER_ADDED));
        registerReceiver(localDataChangeReceiver, new IntentFilter(Constants.LocalDataChange.ACTION_ORDER_SYNCED));
        registerReceiver(localDataChangeReceiver, new IntentFilter(Constants.LocalDataChange.ACTION_DAILY_SYNCED));
	}

    @Override
    protected void onResume() {
        super.onResume();

        if (isPendingDataAvailable()) {
            //don't remove this if statement
        }else {
            int alreadyShown = AppDataManager.getDataInt(this, Constants.DM_DAILY_SYNC_SHOWN_KEY);
            if (alreadyShown == 0 && shouldShowDailySync()) {
                AppDataManager.saveDataInt(this, Constants.DM_DAILY_SYNC_SHOWN_KEY, 1);
                downloadDailyData();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppController.getInstance().cancelPendingRequests(TAG);
        unregisterReceiver(localDataChangeReceiver);
    }

    @Override
    public void onBackPressed() {

        if(exitConfirmed || !isPendingDataAvailable()) {
            exitConfirmed = false;
            super.onBackPressed();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure you want to exit?");
            builder.setMessage("There is some local data in the app. It is better sync them before you exit.");
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    exitAlertDialog.dismiss();
                    exitConfirmed = true;
                    onBackPressed();
                }
            });
            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    exitAlertDialog.dismiss();
                }
            });
            exitAlertDialog = builder.create();
            exitAlertDialog.setCanceledOnTouchOutside(false);
            exitAlertDialog.setCancelable(false);
            exitAlertDialog.show();
        }
    }

    private void customizeActionBar(int section){
        final ActionBar actionBar = getSupportActionBar();
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar_custom_layout, null);
        TextView textViewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);

        String titleText=getString(R.string.app_name);

        switch (section) {
            case 0:
            {
            }
            break;
            case 1:
            {
            }
            break;
            case 2:
            {
            }
            break;
            default:
                //titleText=titleText;
                break;
        }


        textViewTitle.setText(titleText);

        actionBar.setCustomView(viewActionBar,params);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
       // actionBar.setDisplayHomeAsUpEnabled(true);
       // actionBar.setHomeButtonEnabled(true);

    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}else if (id == R.id.action_logout) {
            Log.i(TAG, "Logout button tapped.");
            logOutButtonTapped();
            return true;
        }else if (id == R.id.action_refresh) {
            Log.i(TAG, "Refresh button tapped.");
            syncButtonTapped();
            return true;
        }else if (id == R.id.action_force_sync) {
            Log.i(TAG, "Force sync button tapped.");
            forceSyncButtonTapped();
            return true;
        }
		return super.onOptionsItemSelected(item);
	}

    private void logOutButtonTapped() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to logout?");
        if(isPendingDataAvailable()) builder.setMessage("There is some local data in the app. If you continue logout that data will loss.");
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logoutAlertDialog.dismiss();
                logout();
            }
        });
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logoutAlertDialog.dismiss();
            }
        });
        logoutAlertDialog = builder.create();
        logoutAlertDialog.setCanceledOnTouchOutside(false);
        logoutAlertDialog.setCancelable(false);
        logoutAlertDialog.show();
    }

    private void syncButtonTapped() {
        if (isPendingDataAvailable()) {
            doSyncPendingOrders();
        }else if (shouldShowDailySync()) {
            downloadDailyData();
        }
    }

    private void forceSyncButtonTapped() {
        if (isPendingDataAvailable()) {
            AppUtils.showAlertDialog(this, "Sync required!", "There is some local data in the app. Please sync them and Force Sync again.");
        }else {
            AppDataManager.saveDataInt(this, Constants.DM_DAILY_SYNC_PRODUCT_KEY, 0);
            downloadDailyData();
        }
    }

    private void logout() {
        AppDataManager.saveData(getApplicationContext(), Constants.DM_ACCESS_TOKEN_KEY, "");
        AppDataManager.saveData(getApplicationContext(), Constants.DM_USERNAME_KEY, "");
        AppDataManager.saveData(getApplicationContext(), Constants.DM_LOGGED_KEY, "no");

        ROSDbHelper dbHelper = new ROSDbHelper(this);
        dbHelper.clearNewOrderItemTable(this);
        dbHelper.clearNewOrderTable(this);
        dbHelper.clearReturnOrderItemTable(this);
        dbHelper.clearReturnOrderTable(this);
        dbHelper.clearVisitTable(this);

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void logoutFailed() {
        AppUtils.showAlertDialog(this, "Logout Failed!", "Something went wrong. Please try again.");
    }

    private void sendLogoutRequest() {

        if (!ConnectionDetector.isConnected(this)) {
            AppUtils.showAlertDialog(this, Constants.MSG_NO_INTERNET_TITLE, Constants.MSG_NO_INTERNET_MSG);
            return;
        }

        AppUtils.showProgressDialog(this);

        ROSUser user = ROSUser.getInstance();
        //Authorization: Token <auth token>:<deviceId>
        final String params = "Token " + user.getAccessToken() + ":" + user.getDeviceToken();
        Log.i(TAG, "Logout Authorization: " + params);

        JsonObjectRequest lRequest = new JsonObjectRequest(Request.Method.GET, AppURLs.getLOGOUT_ENDPOINT(getApplicationContext()), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i(TAG, "Logout success " + jsonObject.toString());
                AppUtils.dismissProgressDialog();
                logout();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(TAG, "Logout error " + volleyError.toString());
                if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 401) {
                    Log.i(TAG, "Logout failed ====== Unauthorized");
                }else {
                    Log.i(TAG, "Logout failed ====== Server error");
                }

                AppUtils.dismissProgressDialog();
                logoutFailed();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", params);
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(lRequest, TAG);
    }

    /*
    Data and sync section
     */
    private void dailyDownloadSuccess() {
        AppUtils.dismissProgressDialog();
    }

    private void dailyDownloadFailed(int errorCode) {
        AppUtils.dismissProgressDialog();
        if (errorCode == 401) {
            logout();
        }else {
            setPendingSyncButtonStatus(true);
            AppUtils.showAlertDialog(this, "Sync Failed!", "Daily syncing failed due to Server error. Please try again to update your daily stock.");
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
                    dailyDownloadSuccess();
                }

                @Override
                public void onDailyUpdateErrorResponse(VolleyError error) {
                    dailyDownloadFailed(error.networkResponse != null ? error.networkResponse.statusCode : 500);
                }
            });
            AppUtils.showProgressDialog(this, "Daily stock is downloading...");
        }
    }

    private BroadcastReceiver localDataChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constants.LocalDataChange.ACTION_ORDER_ADDED.equals(action)) {
                isPendingDataAvailable();
            } else if (Constants.LocalDataChange.ACTION_ORDER_SYNCED.equals(action)) {
                isPendingDataAvailable();
            } else if (Constants.LocalDataChange.ACTION_DAILY_SYNCED.equals(action)) {

            }
        }
    };

    private boolean shouldShowDailySync() {
        long timeMS = AppDataManager.getDataLong(this, Constants.DM_DAILY_SYNC_TIME_KEY);
        if (timeMS == 0) {
            return true;
        }else {
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
    }

    private boolean isPendingDataAvailable() {

        ROSDbHelper dbHelper = new ROSDbHelper(this);
        int newCount = dbHelper.getNewOrderCountPending(this);
        int retCount = dbHelper.getReturnOrderCountPending(this);
        int visitCount = dbHelper.getVisitCountPending(this);

        int total = newCount + retCount + visitCount;
        if (total > 0) {
            setPendingSyncButtonStatus(true);
            return true;
        }else {
            setPendingSyncButtonStatus(false);
            return false;
        }
    }

    private void setPendingSyncButtonStatus(boolean enable) {
        //TODO
    }

    private ArrayList<ROSNewOrder> pendingNewOrders = null;
    private ArrayList<ROSReturnOrder> pendingRetOrders = null;
    private ArrayList<ROSVisit> pendingVisits = null;
    private void doSyncPendingOrders() {

        if (!ConnectionDetector.isConnected(this)) {
            AppUtils.showAlertDialog(this, Constants.MSG_NO_INTERNET_TITLE, Constants.MSG_NO_INTERNET_MSG);
        }else {
            ROSDbHelper dbHelper = new ROSDbHelper(this);
            this.pendingNewOrders = dbHelper.getNewOrdersPending(this);
            this.pendingRetOrders = dbHelper.getReturnOrdersPending(this);
            this.pendingVisits = dbHelper.getPendingVisits(this);
            AppUtils.showProgressDialog(this, "Syncing data...");
            doSyncPendingOrdersContinue();
        }
    }

    private void orderSyncCompleted() {
        AppUtils.dismissProgressDialog();
        setPendingSyncButtonStatus(false);
        if (shouldShowDailySync()) {
            downloadDailyData();
        }
    }

    private void orderSyncFailed() {
        AppUtils.dismissProgressDialog();
        pendingNewOrders.clear();
        pendingRetOrders.clear();
        pendingVisits.clear();
        AppUtils.showAlertDialog(this, "Sync Failed!", "Data syncing failed due to Server error. Please try again.");
        setPendingSyncButtonStatus(true);
    }

    private void doSyncPendingOrdersContinue() {
        if (pendingNewOrders.size() > 0) {
            ROSNewOrder newOrder = pendingNewOrders.get(0);
            doNewOrderSync(newOrder);
        } else if (pendingRetOrders.size() > 0) {
            ROSReturnOrder returnOrder = pendingRetOrders.get(0);
            doReturnOrderSync(returnOrder);
        } else if (pendingVisits.size() > 0) {
            ROSVisit visit = pendingVisits.get(0);
            doVisitSync(visit);
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
        ReturnOrderServiceHandler returnOrderServiceHandler = new ReturnOrderServiceHandler(this);
        returnOrderServiceHandler.syncReturnOrder(returnOrder, TAG, new ReturnOrderServiceHandler.ReturnOrderSyncListener() {
            @Override
            public void onOrderSyncSuccess(String orderId) {
                pendingRetOrders.remove(0);
                updateReturnOrderOnSyncComplete(orderId);
                doSyncPendingOrdersContinue();
            }

            @Override
            public void onOrderSyncError(String orderId, VolleyError error) {
                orderSyncFailed();
            }
        });
    }

    private void updateVisitOnSyncComplete(int visitId) {
        ROSDbHelper dbHelper = new ROSDbHelper(this);
        dbHelper.deleteVisit(this, visitId);
    }

    private void doVisitSync(ROSVisit visit) {
        GeneralServiceHandler generalServiceHandler = new GeneralServiceHandler(this);
        generalServiceHandler.sendVisit(TAG, visit, new GeneralServiceHandler.CustomerVisitSyncListener() {
            @Override
            public void onVisitSyncSuccess(int visitId) {
                pendingVisits.remove(0);
                updateVisitOnSyncComplete(visitId);
                doSyncPendingOrdersContinue();
            }

            @Override
            public void onVisitSyncError(VolleyError error) {
                orderSyncFailed();
            }
        });
    }


    class sectionPageAdapter extends FragmentPagerAdapter {

    String [] tabs;
    public sectionPageAdapter(FragmentManager fm) {
        super(fm);
        tabs=getResources().getStringArray(R.array.navigation_section);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        customizeActionBar(position);
        switch (position) {
            case 0:
            {

               fragment = new RelianceOperationFragment();

            }
            break;
            case 1:
            {
               fragment = new StockStatementFragment();

            }
            break;
            case 2:
            {
               fragment = new CustomerOutstandingFragment();

            }
            break;
            default:
                fragment = new RelianceOperationFragment();
                break;


        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}

}
