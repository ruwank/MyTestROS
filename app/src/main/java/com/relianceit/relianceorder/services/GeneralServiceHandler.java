package com.relianceit.relianceorder.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.relianceit.relianceorder.AppController;
import com.relianceit.relianceorder.db.ROSDbHelper;
import com.relianceit.relianceorder.models.ROSCustomer;
import com.relianceit.relianceorder.models.ROSProduct;
import com.relianceit.relianceorder.models.ROSStock;
import com.relianceit.relianceorder.models.ROSUser;
import com.relianceit.relianceorder.util.AppURLs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sura on 5/5/15.
 */
public class GeneralServiceHandler {

    public static final String TAG = GeneralServiceHandler.class.getSimpleName();

    private boolean customerListDownloaded = false;
    private boolean stockListDownloaded = false;
    private boolean productListDownloaded = false;

    private Context context = null;

    public GeneralServiceHandler(Context context) {
        this.context = context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void cancelRequests(final String requestTag) {
        AppController.getInstance().cancelPendingRequests(requestTag);
    }

    public static interface DailyUpdateListener {
        public abstract void onDailyUpdateSuccess();
        public abstract void onDailyUpdateErrorResponse(VolleyError error);
    }

    /*
    Daily sync section
     */
    public void doDailyContentUpdate(final String requestTag, final DailyUpdateListener listener) {

        if (!customerListDownloaded) {
            downloadCustomerList(requestTag, listener);
        } else if (!stockListDownloaded) {
            downloadStocksList(requestTag, listener);
        } else if (!productListDownloaded) {
            downloadProductsList(requestTag, listener);
        } else if (customerListDownloaded && stockListDownloaded && productListDownloaded) {
            this.context = null;
            listener.onDailyUpdateSuccess();
        }
    }

    private void customersDownloaded(ArrayList<ROSCustomer> customers, final String requestTag, final DailyUpdateListener listener) {
        ArrayList<ROSCustomer> customersToDb = new ArrayList<ROSCustomer>();
        for (int i = 0; i < customers.size(); i++) {
            ROSCustomer customer = customers.get(i);
            customer.fillDbFields();
            customersToDb.add(customer);
        }

        ROSDbHelper dbHelper = new ROSDbHelper(context);
        dbHelper.clearCustomerTable(context);
        dbHelper.insertCustomers(context, customersToDb);

        customerListDownloaded = true;
        doDailyContentUpdate(requestTag, listener);
    }

    private void stocksDownloaded(ArrayList<ROSStock> stocks, final String requestTag, final DailyUpdateListener listener) {
        ArrayList<ROSStock> stocksToDb = new ArrayList<ROSStock>();
        for (int i = 0; i < stocks.size(); i++) {
            ROSStock stock = stocks.get(i);
            stock.fillDbFields();
            stocksToDb.add(stock);
        }

        ROSDbHelper dbHelper = new ROSDbHelper(context);
        dbHelper.clearStockTable(context);
        dbHelper.insertStocks(context, stocksToDb);

        stockListDownloaded = true;
        doDailyContentUpdate(requestTag, listener);
    }

    private void productsDownloaded(ArrayList<ROSProduct> products, final String requestTag, final DailyUpdateListener listener) {

//        ArrayList<ROSProduct> productToDb = new ArrayList<ROSProduct>();
//        for (int i = 0; i < products.size(); i++) {
//            ROSProduct product = products.get(i);
//            productToDb.add(product);
//        }

        ROSDbHelper dbHelper = new ROSDbHelper(context);
        dbHelper.clearProductTable(context);
        //dbHelper.insertProducts(context, productToDb);
        dbHelper.addProducts(context);

        productListDownloaded = true;
        doDailyContentUpdate(requestTag, listener);
    }

    private void dailyUpdateFailed(final String requestTag, final DailyUpdateListener listener, VolleyError volleyError) {

        ROSDbHelper dbHelper = new ROSDbHelper(context);
        dbHelper.clearCustomerTable(context);
        dbHelper.clearStockTable(context);
        dbHelper.clearProductTable(context);

        this.context = null;
        listener.onDailyUpdateErrorResponse(volleyError);
    }

    private void downloadCustomerList(final String requestTag, final DailyUpdateListener listener) {
        ROSUser user = ROSUser.getInstance();
        //Authorization: Token <auth token>:<deviceId>
        final String params = "Token " + user.getAccessToken() + ":" + user.getDeviceToken();
        Log.i(TAG, "Customer Authorization: " + params);

        JsonArrayRequest customerRequest = new JsonArrayRequest(AppURLs.CUSTOMER_LIST_ENDPOINT, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.i(TAG, "Customer list success " + jsonArray.toString());

                Type listType = new TypeToken<ArrayList<ROSCustomer>>(){}.getType();
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                ArrayList<ROSCustomer> customers = gson.fromJson(jsonArray.toString(), listType);

                if(customers == null) customers = new ArrayList<ROSCustomer>();
                Log.i(TAG, "Customer list size: " + customers.size());
                customersDownloaded(customers, requestTag, listener);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(TAG, "Customer list error " + volleyError.toString());
                if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 401) {
                    Log.i(TAG, "Customer list failed ====== Unauthorized");
                }else {
                    Log.i(TAG, "Customer list failed ====== Server error");
                }
                dailyUpdateFailed(requestTag, listener, volleyError);
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

        AppController.getInstance().addToRequestQueue(customerRequest, requestTag);
    }

    private void downloadStocksList(final String requestTag, final DailyUpdateListener listener) {
        ROSUser user = ROSUser.getInstance();
        //Authorization: Token <auth token>:<deviceId>
        final String params = "Token " + user.getAccessToken() + ":" + user.getDeviceToken();
        Log.i(TAG, "Stock Authorization: " + params);

        JsonArrayRequest stockRequest = new JsonArrayRequest(AppURLs.STOCK_LIST_ENDPOINT, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.i(TAG, "Stock list success " + jsonArray.toString());

                Type listType = new TypeToken<ArrayList<ROSStock>>(){}.getType();
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                ArrayList<ROSStock> stocks = gson.fromJson(jsonArray.toString(), listType);

                if(stocks == null) stocks = new ArrayList<ROSStock>();
                Log.i(TAG, "Stock list size: " + stocks.size());
                stocksDownloaded(stocks, requestTag, listener);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(TAG, "Stock list error " + volleyError.toString());
                if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 401) {
                    Log.i(TAG, "Stock list failed ====== Unauthorized");
                }else {
                    Log.i(TAG, "Stock list failed ====== Server error");
                }
                dailyUpdateFailed(requestTag, listener, volleyError);
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

        AppController.getInstance().addToRequestQueue(stockRequest, requestTag);
    }

    private void downloadProductsList(final String requestTag, final DailyUpdateListener listener) {

        productsDownloaded(null, requestTag, listener);


//        ROSUser user = ROSUser.getInstance();
//        //Authorization: Token <auth token>:<deviceId>
//        final String params = "Token " + user.getAccessToken() + ":" + user.getDeviceToken();
//
//        JsonArrayRequest productRequest = new JsonArrayRequest(AppURLs.STOCK_LIST_ENDPOINT, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray jsonArray) {
//                Log.i(TAG, "Products list success " + jsonArray.toString());
//
//                Type listType = new TypeToken<ArrayList<ROSProduct>>(){}.getType();
//                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//                ArrayList<ROSProduct> products = gson.fromJson(jsonArray.toString(), listType);
//
//                if(products == null) products = new ArrayList<ROSProduct>();
//                Log.i(TAG, "Product list size: " + products.size());
//                productsDownloaded(products, requestTag, listener);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                Log.i(TAG, "Products list error " + volleyError.toString());
//                if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 401) {
//                    Log.i(TAG, "Products list failed ====== Unauthorized");
//                }else {
//                    Log.i(TAG, "Products list failed ====== Server error");
//                }
//                dailyUpdateFailed(requestTag, listener, volleyError);
//            }
//        })
//        {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", params);
//                return headers;
//            }
//        };
//
//        AppController.getInstance().addToRequestQueue(productRequest, requestTag);
    }

}
