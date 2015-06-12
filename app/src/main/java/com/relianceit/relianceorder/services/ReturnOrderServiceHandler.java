package com.relianceit.relianceorder.services;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.relianceit.relianceorder.AppController;
import com.relianceit.relianceorder.models.ROSInvoice;
import com.relianceit.relianceorder.models.ROSNewOrder;
import com.relianceit.relianceorder.models.ROSReturnOrder;
import com.relianceit.relianceorder.models.ROSReturnOrderItem;
import com.relianceit.relianceorder.models.ROSUser;
import com.relianceit.relianceorder.util.AppURLs;
import com.relianceit.relianceorder.util.Constants;

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
 * Created by Suresh on 5/5/15.
 */
public class ReturnOrderServiceHandler {

    public static final String TAG = ReturnOrderServiceHandler.class.getSimpleName();

    private Context context = null;

    public ReturnOrderServiceHandler(Context context) {
        this.context = context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void cancelRequests(final String requestTag) {
        AppController.getInstance().cancelPendingRequests(requestTag);
    }

    public static interface ReturnOrderSyncListener {
        public abstract void onOrderSyncSuccess(String orderId);
        public abstract void onOrderSyncError(String orderId, VolleyError error);
    }

    private void syncSuccess(String orderId, final String requestTag, final ReturnOrderSyncListener listener) {
        listener.onOrderSyncSuccess(orderId);
    }

    private void syncFailed(String orderId, final String requestTag, final ReturnOrderSyncListener listener, VolleyError volleyError) {
        listener.onOrderSyncError(orderId, volleyError);
    }

    public void syncReturnOrder(ROSReturnOrder order, final String requestTag, final ReturnOrderSyncListener listener) {

        ROSUser user = ROSUser.getInstance();
        //Authorization: Token <auth token>:<deviceId>
        final String params = "Token " + user.getAccessToken() + ":" + user.getDeviceToken();
        Log.i(TAG, "ReturnOrder Authorization: " + params);

        final String orderId = order.getReturnNumb();

        Type listType = new TypeToken<ROSReturnOrder>(){}.getType();
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String jsonString = gson.toJson(order);

        Log.i(TAG, "ReturnOrder Json: " + jsonString);

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

        JsonObjectRequest syncRequest = new JsonObjectRequest(Request.Method.POST, AppURLs.RETURN_ORDER_SYNC_ENDPOINT, postBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i(TAG, "Sync ReturnOrder success " + jsonObject.toString());
                        syncSuccess(orderId, requestTag, listener);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i(TAG, "Sync ReturnOrder error " + volleyError.toString());
                        if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 401) {
                            Log.i(TAG, "Sync ReturnOrder failed ====== Unauthorized");
                            syncFailed(orderId, requestTag, listener, volleyError);
                        }else if (volleyError.toString().contains("com.android.volley.ParseError")) {
                            Log.i(TAG, "Sync ReturnOrder success ====== Parse error");
                            syncSuccess(orderId, requestTag, listener);
                        }else {
                            Log.i(TAG, "Sync ReturnOrder failed ====== Server error");
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

    public void testSyncNewOrder() {

        ROSReturnOrderItem orderItem = new ROSReturnOrderItem();
        orderItem.setProductBatchCode("0001");
        orderItem.setProductDescription("Testing Product3");
        orderItem.setProductCode("001");
        orderItem.setQtyOrdered(1);
        orderItem.setUnitPrice(100);
        orderItem.setProdDiscount(0);
        orderItem.setQtyBonus(0);
        orderItem.setEffPrice(100.00);

        ArrayList<ROSReturnOrderItem> products = new ArrayList<ROSReturnOrderItem>();
        products.add(orderItem);

        ROSReturnOrder order = new ROSReturnOrder();
        order.setCustCode("00001");
        order.setGrossValue(100);
        order.setOVDiscount(1);
        order.setProducts(products);
        order.setOrderValue(1000.20);

        syncReturnOrder(order, TAG, new ReturnOrderSyncListener() {
            @Override
            public void onOrderSyncSuccess(String orderId) {

            }

            @Override
            public void onOrderSyncError(String orderId, VolleyError error) {

            }
        });
    }

    /*
    List Of Returns
     */

    public static interface ReturnOrderListListener {
        public abstract void onGetListSuccess(ArrayList<ROSReturnOrder> orders);
        public abstract void onGetListError(VolleyError error);
    }

    public void getReturnOrderList(String customerCode, String fromDate, String toDate, final String requestTag, final ReturnOrderListListener listener) {

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
        Log.i(TAG, "Return Order Authorization: " + params);

        String endPoint = AppURLs.RETURN_LIST_GET_ENDPOINT + customerCode;
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

        Log.i(TAG, "Return Order end point: " + endPoint);

        JsonArrayRequest listRequest = new JsonArrayRequest(endPoint, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                Log.i(TAG, "Return Order list success " + jsonArray.toString());

                Type listType = new TypeToken<ArrayList<ROSReturnOrder>>(){}.getType();
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                ArrayList<ROSReturnOrder> orders = gson.fromJson(jsonArray.toString(), listType);

                if(orders == null) orders = new ArrayList<ROSReturnOrder>();
                Log.i(TAG, "Return Order list size: " + orders.size());

                listener.onGetListSuccess(orders);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(TAG, "Return Order list error " + volleyError.toString());
                if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 401) {
                    Log.i(TAG, "Return Order list failed ====== Unauthorized");
                }else {
                    Log.i(TAG, "Return Order list failed ====== Server error");
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

        listRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(listRequest, requestTag);
    }

    public void testGetList() {
        //00001/2014-01-01/2016-01-01
        getReturnOrderList("00001", "2014-01-01", "2016-01-01", TAG, new ReturnOrderListListener() {
            @Override
            public void onGetListSuccess(ArrayList<ROSReturnOrder> orders) {

            }

            @Override
            public void onGetListError(VolleyError error) {

            }
        });
    }

    /*
    Order details
     */

    public static interface ReturnOrderDetailsListener {
        public abstract void onGetOrderSuccess(ROSReturnOrder order);
        public abstract void onGetOrderError(VolleyError error);
    }

    public void getReturnOrder(String customerCode, String orderId, final String requestTag, final ReturnOrderDetailsListener listener) {

        if (customerCode == null || customerCode.length() == 0) {
            listener.onGetOrderError(null);
            return;
        }

        ROSUser user = ROSUser.getInstance();
        //Authorization: Token <auth token>:<deviceId>
        final String params = "Token " + user.getAccessToken() + ":" + user.getDeviceToken();
        Log.i(TAG, "Get Return Order Authorization: " + params);

        String endPoint = AppURLs.RETURN_GET_ENDPOINT + customerCode + "/" + orderId;

        Log.i(TAG, "Get Return Order end point: " + endPoint);

        JsonObjectRequest orderRequest = new JsonObjectRequest(Request.Method.GET, endPoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonArray) {
                Log.i(TAG, "Get Return Order success " + jsonArray.toString());

                Type objType = new TypeToken<ROSReturnOrder>(){}.getType();
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                ROSReturnOrder order = gson.fromJson(jsonArray.toString(), objType);
                listener.onGetOrderSuccess(order);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(TAG, "Get Return Order error " + volleyError.toString());
                if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 401) {
                    Log.i(TAG, "Get Return Order failed ====== Unauthorized");
                }else {
                    Log.i(TAG, "Get Return Order failed ====== Server error");
                }
                listener.onGetOrderError(volleyError);
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

        AppController.getInstance().addToRequestQueue(orderRequest, requestTag);
    }

    public void testGetOrder() {
        getReturnOrder("00001", "000001", TAG, new ReturnOrderDetailsListener() {
            @Override
            public void onGetOrderSuccess(ROSReturnOrder order) {

            }

            @Override
            public void onGetOrderError(VolleyError error) {

            }
        });
    }

    /*
    Invoice details
     */
    public static interface InvoiceDetailsListener {
        public abstract void onGetInvoiceSuccess(ROSInvoice invoice);
        public abstract void onGetInvoiceError(VolleyError error);
    }

    public void getInvoice(String customerCode, String invoiceNo, final String requestTag, final InvoiceDetailsListener listener) {

        if (customerCode == null || customerCode.length() == 0) {
            listener.onGetInvoiceError(null);
            return;
        }

        ROSUser user = ROSUser.getInstance();
        //Authorization: Token <auth token>:<deviceId>
        final String params = "Token " + user.getAccessToken() + ":" + user.getDeviceToken();
        Log.i(TAG, "Get Invoice Authorization: " + params);

        String endPoint = AppURLs.INVOICE_GET_ENDPOINT + customerCode + "/" + invoiceNo;

        Log.i(TAG, "Get Invoice end point: " + endPoint);

        JsonObjectRequest orderRequest = new JsonObjectRequest(Request.Method.GET, endPoint, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonArray) {
                Log.i(TAG, "Get Invoice success " + jsonArray.toString());

                Type objType = new TypeToken<ROSInvoice>(){}.getType();
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                ROSInvoice invoice = gson.fromJson(jsonArray.toString(), objType);
                listener.onGetInvoiceSuccess(invoice);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(TAG, "Get Invoice error " + volleyError.toString());
                if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 401) {
                    Log.i(TAG, "Get Invoice failed ====== Unauthorized");
                }else {
                    Log.i(TAG, "Get Invoice failed ====== Server error");
                }
                listener.onGetInvoiceError(volleyError);
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

        AppController.getInstance().addToRequestQueue(orderRequest, requestTag);
    }

    public void testGetInvoice() {
        getInvoice("00001", "000044", TAG, new InvoiceDetailsListener() {
            @Override
            public void onGetInvoiceSuccess(ROSInvoice invoice) {

            }

            @Override
            public void onGetInvoiceError(VolleyError error) {

            }
        });
    }
}
