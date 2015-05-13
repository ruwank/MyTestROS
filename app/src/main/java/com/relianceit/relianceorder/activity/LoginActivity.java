package com.relianceit.relianceorder.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.relianceit.relianceorder.AppController;
import com.relianceit.relianceorder.R;
import com.relianceit.relianceorder.db.ROSDbHelper;
import com.relianceit.relianceorder.models.ROSCustomer;
import com.relianceit.relianceorder.models.ROSStock;
import com.relianceit.relianceorder.models.ROSUser;
import com.relianceit.relianceorder.services.GeneralServiceHandler;
import com.relianceit.relianceorder.services.NewOrderServiceHandler;
import com.relianceit.relianceorder.util.AppDataManager;
import com.relianceit.relianceorder.util.AppURLs;
import com.relianceit.relianceorder.util.AppUtils;
import com.relianceit.relianceorder.util.ConnectionDetector;
import com.relianceit.relianceorder.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends ActionBarActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();

    private String username = "Reliance";
    private String password = "10";

    private EditText userNameET = null;
    private EditText passwordET = null;

    Button loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userNameET = (EditText)findViewById(R.id.userName);
        passwordET = (EditText)findViewById(R.id.password);
        loginBtn=(Button)findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loginButtonTapped();
            }
        });

        Button cancelBtn = (Button)findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelButtonTapped();
            }
        });

        customizeActionBar();
    }
    private void customizeActionBar(){
        final ActionBar actionBar = getSupportActionBar();
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar_custom_layout, null);
        TextView textViewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);

        String titleText=getString(R.string.app_name);

        textViewTitle.setText(titleText);

        actionBar.setCustomView(viewActionBar,params);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    private void cancelButtonTapped() {
        this.onBackPressed();
    }

    private void loginButtonTapped() {

        username = userNameET.getText().toString().trim();
        password = passwordET.getText().toString().trim();

        if (username.length() == 0) {
            AppUtils.showAlertDialog(this, "Invalid User Name", "Please enter a valid User Name.");
            return;
        }

        if (password.length() == 0) {
            AppUtils.showAlertDialog(this, "Invalid Password", "Please enter a valid Password.");
            return;
        }

        if (!ConnectionDetector.isConnected(this)) {
            AppUtils.showAlertDialog(this, Constants.MSG_NO_INTERNET_TITLE, Constants.MSG_NO_INTERNET_MSG);
        }else {
            sendLoginRequest();
        }
    }

    private void sendLoginRequest() {
        ROSUser user = ROSUser.getInstance();

        //Authorization: Basic <username>:<password>:<deviceId>
        final String params = "Basic " + username + ":" + password + ":" + "18388499282";
        Log.i(TAG, "Login Authorization: " + params);

        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, AppURLs.LOGIN_ENDPOINT, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i(TAG, "Login success " + jsonObject.toString());
                        String token = "";
                        try {
                            token = jsonObject.getString("AuthToken");
                            Log.i(TAG, "Access token " + token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loginCompleted(token);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //TODO 401 if unauthorized
                        Log.i(TAG, "Login error " + volleyError.toString());
                        if (volleyError.networkResponse != null && volleyError.networkResponse.statusCode == 401) {
                            Log.i(TAG, "Login failed ====== Unauthorized");
                            loginFailed(401);
                        }else {
                            Log.i(TAG, "Login failed ====== Server error");
                            loginFailed(500);
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

        AppController.getInstance().addToRequestQueue(loginRequest, TAG);
        AppUtils.showProgressDialog(this);
    }

    private void loginFailed(int errorCode) {
        AppUtils.dismissProgressDialog();
        if (errorCode == 401) {
            AppUtils.showAlertDialog(this, "Login Failed!", "Invalid user credentials.");
        }else {
            AppUtils.showAlertDialog(this, "Login Failed!", "Server error. Please try again.");
        }
    }

    private void loginCompleted(String accessToken) {
        ROSUser user = ROSUser.getInstance();
        user.setUsername(username);
        user.setPassword(password);
        user.setAccessToken(accessToken);
        user.setDeviceToken("18388499282");

        String encodedToken = accessToken;

        try {
            byte[] data = accessToken.getBytes("UTF-8");
            encodedToken = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        AppDataManager.saveData(getApplicationContext(), Constants.DM_ACCESS_TOKEN_KEY, encodedToken);
        AppDataManager.saveData(getApplicationContext(), Constants.DM_USERNAME_KEY, username);
        AppDataManager.saveData(getApplicationContext(), Constants.DM_LOGGED_KEY, "yes");
        AppDataManager.saveDataInt(getApplicationContext(), Constants.DM_OFFLINE_LOGOUT_KEY, 0);

        AppUtils.dismissProgressDialog();

        loadHome();
    }

    private void loadHome() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
