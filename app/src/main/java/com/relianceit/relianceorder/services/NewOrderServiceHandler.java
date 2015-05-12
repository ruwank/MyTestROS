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
import com.relianceit.relianceorder.models.ROSNewOrder;
import com.relianceit.relianceorder.models.ROSNewOrderItem;
import com.relianceit.relianceorder.models.ROSStock;
import com.relianceit.relianceorder.models.ROSUser;
import com.relianceit.relianceorder.util.AppURLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sura on 5/5/15.
 */
public class NewOrderServiceHandler {

    public static final String TAG = NewOrderServiceHandler.class.getSimpleName();

    private Context context = null;

    public NewOrderServiceHandler(Context context) {
        this.context = context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void cancelRequests(final String requestTag) {
        AppController.getInstance().cancelPendingRequests(requestTag);
    }

    public static interface NewOrderSyncListener {
        public abstract void onOrderSyncSuccess(String orderId);
        public abstract void onOrderSyncError(String orderId, VolleyError error);
    }

    private void syncSuccess(String orderId, final String requestTag, final NewOrderSyncListener listener) {

        //update db
        listener.onOrderSyncSuccess(orderId);
    }

    private void syncFailed(String orderId, final String requestTag, final NewOrderSyncListener listener, VolleyError volleyError) {
        listener.onOrderSyncError(orderId, volleyError);
    }

    public void syncNewOrder(ROSNewOrder order, final String requestTag, final NewOrderSyncListener listener) {

        ROSUser user = ROSUser.getInstance();
        //Authorization: Token <auth token>:<deviceId>
        final String params = "Token " + user.getAccessToken() + ":" + user.getDeviceToken();
        Log.i(TAG, "NewOrder Authorization: " + params);

        final String orderId = order.getSalesOrdNum();

        Type listType = new TypeToken<ROSNewOrder>(){}.getType();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String jsonString = gson.toJson(order);

        Log.i(TAG, "NewOrder Json: " + jsonString);

        JSONObject postBody = null;
        try {
            postBody = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (postBody == null) {
            syncFailed(orderId, requestTag, listener, null);
            return;
        }

        JsonObjectRequest syncRequest = new JsonObjectRequest(Request.Method.POST, AppURLs.NEW_ORDER_SYNC_ENDPOINT, postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i(TAG, "Sync NewOrder success " + jsonObject.toString());
                        syncSuccess(orderId, requestTag, listener);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //TODO 401 if unauthorized
                        Log.i(TAG, "Sync NewOrder error " + volleyError.toString());
                        if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 401) {
                            Log.i(TAG, "Sync NewOrder failed ====== Unauthorized");
                            syncFailed(orderId, requestTag, listener, volleyError);
                        }else if (volleyError.toString().contains("com.android.volley.ParseError")) {
                            Log.i(TAG, "Sync NewOrder success ====== Parse error");
                            syncSuccess(orderId, requestTag, listener);
                        }else {
                            Log.i(TAG, "Sync NewOrder failed ====== Server error");
                            syncFailed(orderId, requestTag, listener, volleyError);
                        }
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

        AppController.getInstance().addToRequestQueue(syncRequest, requestTag);
    }

    private void testSticks() {
        ROSDbHelper dbHelper = new ROSDbHelper(context);
        ArrayList<ROSStock> stocks = dbHelper.getStocks(context);
        for (int i = 0; i < stocks.size(); i++) {
            ROSStock stock = stocks.get(i);
            stock.print();
        }
    }

    public void testSyncNewOrder() {

        ROSNewOrderItem orderItem = new ROSNewOrderItem();
        orderItem.setProductBatchCode("0001");
        orderItem.setProductDescription("Testing Product3");
        orderItem.setProductCode("001");
        orderItem.setQtyOrdered(1);
        orderItem.setUnitPrice(100);
        orderItem.setProdDiscount(0);
        orderItem.setQtyBonus(0);
        orderItem.setEffPrice(100.00);

        ArrayList<ROSNewOrderItem> products = new ArrayList<ROSNewOrderItem>();
        products.add(orderItem);

        ROSNewOrder order = new ROSNewOrder();
        order.setCustCode("00001");
        order.setGrossValue(100);
        order.setOVDiscount(1);
        order.setProducts(products);
        order.setOrderValue(1000.20);

        syncNewOrder(order, TAG, new NewOrderSyncListener() {
            @Override
            public void onOrderSyncSuccess(String orderId) {
                Log.v("onOrderSyncSuccess","orderId:"+orderId);
            }

            @Override
            public void onOrderSyncError(String orderId, VolleyError error) {
                Log.v("onOrderSyncError","orderId:"+orderId);

            }
        });
    }

    /*
    List Of Orders
     */

    public static interface SalesOrderListListener {
        public abstract void onGetListSuccess(ArrayList<ROSNewOrder> orders);
        public abstract void onGetListError(VolleyError error);
    }

    public void getSalesOrderList(String customerCode, String fromDate, String toDate, final String requestTag, final SalesOrderListListener listener) {

        if (customerCode == null || customerCode.length() == 0) {
            listener.onGetListError(null);
            return;
        }

        try {
            customerCode = URLEncoder.encode(customerCode, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ROSUser user = ROSUser.getInstance();
        //Authorization: Token <auth token>:<deviceId>
        final String params = "Token " + user.getAccessToken() + ":" + user.getDeviceToken();
        Log.i(TAG, "Sales Order Authorization: " + params);

        String endPoint = AppURLs.SALES_LIST_GET_ENDPOINT + customerCode;
        if (fromDate != null && fromDate.length() > 0) {
            try {
                fromDate = URLEncoder.encode(fromDate, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            endPoint = endPoint + "/" + fromDate;
        }

        if (toDate != null && toDate.length() > 0) {
            try {
                toDate = URLEncoder.encode(toDate, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            endPoint = endPoint + "/" + toDate;
        }

        JsonArrayRequest listRequest = new JsonArrayRequest(endPoint, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.i(TAG, "Sales Order list success " + jsonArray.toString());

                Type listType = new TypeToken<ArrayList<ROSNewOrder>>(){}.getType();
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                ArrayList<ROSNewOrder> orders = gson.fromJson(jsonArray.toString(), listType);

                if(orders == null) orders = new ArrayList<ROSNewOrder>();
                Log.i(TAG, "Sales Order list size: " + orders.size());

                listener.onGetListSuccess(orders);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(TAG, "Sales Order list error " + volleyError.toString());
                if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 401) {
                    Log.i(TAG, "Sales Order list failed ====== Unauthorized");
                }else {
                    Log.i(TAG, "Sales Order list failed ====== Server error");
                }
                listener.onGetListError(volleyError);
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

        AppController.getInstance().addToRequestQueue(listRequest, requestTag);
    }

    public void testGetList() {
        //00001/2014-01-01/2016-01-01
        getSalesOrderList("00001", "2014-01-01", "2016-01-01", TAG, new SalesOrderListListener() {
            @Override
            public void onGetListSuccess(ArrayList<ROSNewOrder> orders) {

            }

            @Override
            public void onGetListError(VolleyError error) {

            }
        });
    }
}
