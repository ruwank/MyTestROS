package com.relianceit.relianceorder.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.relianceit.relianceorder.AppController;
import com.relianceit.relianceorder.R;
import com.relianceit.relianceorder.db.ROSDbHelper;
import com.relianceit.relianceorder.fragment.DatePickerDialogFragment;
import com.relianceit.relianceorder.models.ROSCustomer;
import com.relianceit.relianceorder.models.ROSNewOrder;
import com.relianceit.relianceorder.models.ROSReturnOrder;
import com.relianceit.relianceorder.services.NewOrderServiceHandler;
import com.relianceit.relianceorder.services.ReturnOrderServiceHandler;
import com.relianceit.relianceorder.util.AppUtils;
import com.relianceit.relianceorder.util.ConnectionDetector;
import com.relianceit.relianceorder.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ListOfOrderActivity extends ActionBarActivity implements  DatePickerDialog.OnDateSetListener {

    public static final String TAG = ListOfOrderActivity.class.getSimpleName();
    public static final String ROW_TAG = "table_row_tag";

    TextView fromDate,toDate;
    DialogFragment datePickerFragment;
    TableLayout orderListTable;
    TextView tblHeaderCol1,tblHeaderCol2,tblHeaderCol3;
    TextView customerName;
    boolean fromDateSelect;
    private int fromYear;
    private int fromMonth;
    private int fromDay;
    private int toYear;
    private int toMonth;
    private int toDay;
    int itemIndex;
    Button btnGetOrder;
    Constants.Section section;
    ROSCustomer selectedCustomer;

    ArrayList<ROSNewOrder> salesOrderArrayList;
    ArrayList<ROSReturnOrder> returnOrderArrayList;
    private boolean loadedFromDb;
    private String fromDateSelected;
    private String toDateSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_order);

        Intent intent = getIntent();
        section = (Constants.Section) intent.getSerializableExtra("section");

        itemIndex=0;

        orderListTable=(TableLayout)findViewById(R.id.order_list_table);
        fromDate=(TextView)findViewById(R.id.from_date);
        toDate=(TextView)findViewById(R.id.to_date);

        Calendar cal = Calendar.getInstance();
        fromYear=toYear = cal.get(Calendar.YEAR);
        fromMonth=toMonth = cal.get(Calendar.MONTH);
        fromDay=toDay = cal.get(Calendar.DAY_OF_MONTH);

        StringBuilder dateString=new StringBuilder().append(fromDay).
                append("-").append(fromMonth + 1)
                .append("-").append(fromYear)
                .append(" ");
        fromDate.setText(dateString);
        toDate.setText(dateString);

        datePickerFragment = new DatePickerDialogFragment(ListOfOrderActivity.this);

        fromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fromDateSelect=true;
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        toDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fromDateSelect=false;
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        btnGetOrder=(Button)findViewById(R.id.btnGetOrder);
        btnGetOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                updateButtonTapped();
            }

        });

        tblHeaderCol1=(TextView)findViewById(R.id.tbl_header_col1);
        tblHeaderCol2=(TextView)findViewById(R.id.tbl_header_col2);
        tblHeaderCol3=(TextView)findViewById(R.id.tbl_header_col3);

        customerName=(TextView)findViewById(R.id.customer_name);
        updateLabel();

        showLocalDataForToday();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppController.getInstance().cancelPendingRequests(TAG);
    }

    private void updateButtonTapped() {

        if (!ConnectionDetector.isConnected(this)) {
            AppUtils.showAlertDialog(this, Constants.MSG_NO_INTERNET_TITLE, Constants.MSG_NO_INTERNET_MSG);
            return;
        }

        StringBuilder fromDateString=new StringBuilder().append(fromYear).
                append("-").append(fromMonth + 1)
                .append("-").append(fromDay);
        fromDateSelected = fromDateString.toString();

        StringBuilder toDateString=new StringBuilder().append(toYear).
                append("-").append(toMonth + 1)
                .append("-").append(toDay);
        toDateSelected = toDateString.toString();

        if(section == Constants.Section.VIEW_SALE_RETURNS_LIST){
            getReturnOrderList(selectedCustomer.getCustomerId(), fromDateString.toString(), toDateString.toString());
        }else {
            getSalesOrderList(selectedCustomer.getCustomerId(), fromDateString.toString(), toDateString.toString());
        }
    }

    private void orderSelected(int index){

        if (loadedFromDb) {
            if(section == Constants.Section.VIEW_SALE_RETURNS_LIST){
                ROSDbHelper dbHelper = new ROSDbHelper(this);
                ROSReturnOrder order = returnOrderArrayList.get(index);
                order = dbHelper.getReturnOrder(this, order.getReturnNumb());
                getReturnDetailsSuccess(order);
            }else {
                ROSDbHelper dbHelper = new ROSDbHelper(this);
                ROSNewOrder order = salesOrderArrayList.get(index);
                order = dbHelper.getNewOrder(this, order.getSalesOrdNum());
                getSaleDetailsSuccess(order);
            }

            return;
        }

        if (!ConnectionDetector.isConnected(this)) {
            AppUtils.showAlertDialog(this, Constants.MSG_NO_INTERNET_TITLE, Constants.MSG_NO_INTERNET_MSG);
            return;
        }

        if(section == Constants.Section.VIEW_SALE_RETURNS_LIST){
            ROSReturnOrder order = returnOrderArrayList.get(index);
            if (order.getOrderStatus() == Constants.OrderStatus.PENDING) {
                ROSDbHelper dbHelper = new ROSDbHelper(this);
                order = dbHelper.getReturnOrder(this, order.getReturnNumb());
                getReturnDetailsSuccess(order);
            }else {
                getReturnOrderDetails(order.getReturnNumb());
            }
        }else {
            ROSNewOrder order = salesOrderArrayList.get(index);
            if (order.getOrderStatus() == Constants.OrderStatus.PENDING) {
                ROSDbHelper dbHelper = new ROSDbHelper(this);
                order = dbHelper.getNewOrder(this, order.getSalesOrdNum());
                getSaleDetailsSuccess(order);
            }else {
                getSaleOrderDetails(order.getSalesOrdNum());
            }
        }
    }

    private void loadOrderViewActivity(){
        Intent intent = new Intent(getApplicationContext(),
                ViewOrderActivity.class);

        if(section == Constants.Section.VIEW_SALE_RETURNS_LIST){
            intent.putExtra("section", Constants.Section.VIEW_SALE_RETURNS);

        }else{
            intent.putExtra("section", Constants.Section.VIEW_ORDER);
        }
        startActivity(intent);
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
        if(section == Constants.Section.VIEW_SALE_RETURNS_LIST){
            titleText=titleText+" - "+getString(R.string.section_sales_return_list);
        }else{
            titleText=titleText+" - "+getString(R.string.section_order_list);

        }
        textViewTitle.setText(titleText);

        actionBar.setCustomView(viewActionBar,params);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

    }

    private void updateLabel(){
        selectedCustomer= AppController.getInstance().getRosCustomer();

        customerName.setText(selectedCustomer.getCustName());
        customizeActionBar();

        if(section == Constants.Section.VIEW_SALE_RETURNS_LIST){
            tblHeaderCol1.setText("Return No");
            tblHeaderCol2.setText("Return Date");
            tblHeaderCol3.setText("Return Value");

        }else{
            tblHeaderCol1.setText("Order No ");
            tblHeaderCol2.setText("Order Date");
            tblHeaderCol3.setText("Order Value");

        }
    }

    private void showLocalDataForToday() {
        if(section == Constants.Section.VIEW_SALE_RETURNS_LIST){
            ROSDbHelper dbHelper = new ROSDbHelper(this);
            ArrayList<ROSReturnOrder> orders = dbHelper.getReturnOrders(this, selectedCustomer.getCustomerId());

            StringBuilder toDateString=new StringBuilder().append(toDay).
                    append("/").append(toMonth + 1)
                    .append("/").append(toYear);
            String dateStr = toDateString.toString();

            for (int i = 0; i < orders.size(); i++) {
                ROSReturnOrder order = orders.get(i);
                order.setAddedDate(dateStr);
            }
            loadedFromDb = true;
            showReturnOrders(orders);
        }else {
            ROSDbHelper dbHelper = new ROSDbHelper(this);
            ArrayList<ROSNewOrder> orders = dbHelper.getNewOrders(this, selectedCustomer.getCustomerId());

            StringBuilder toDateString=new StringBuilder().append(toDay).
                    append("/").append(toMonth + 1)
                    .append("/").append(toYear);
            String dateStr = toDateString.toString();

            for (int i = 0; i < orders.size(); i++) {
                ROSNewOrder order = orders.get(i);
                order.setAddedDate(dateStr);
            }
            loadedFromDb = true;
            showSalesOrders(orders);
        }
    }

    private void showSalesOrders(ArrayList<ROSNewOrder> orders) {

        orderListTable.removeAllViews();

        salesOrderArrayList =orders;
        itemIndex = 0;
        for (int i = 0; i < salesOrderArrayList.size(); i++) {
            itemIndex = i;
            ROSNewOrder order = salesOrderArrayList.get(i);
            addSaleOrderToTable(order);
        }
    }

    private void showReturnOrders(ArrayList<ROSReturnOrder> orders) {

        orderListTable.removeAllViews();

        returnOrderArrayList = orders;
        itemIndex = 0;
        for (int i = 0; i < returnOrderArrayList.size(); i++) {
            itemIndex = i;
            ROSReturnOrder order = returnOrderArrayList.get(i);
            addReturnOrderToTable(order);
        }
    }

    private void addSaleOrderToTable(ROSNewOrder order){

        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsTableRow.topMargin=5;
        layoutParamsTableRow.bottomMargin=5;

        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(layoutParamsTableRow);
        tableRow.setBackgroundResource(R.drawable.border);

        TableRow.LayoutParams layoutParamsTextView = new TableRow.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT,1.0f);
        layoutParamsTextView.gravity = Gravity.CENTER_VERTICAL;
        layoutParamsTextView.setMargins(1,5,1,5);
        layoutParamsTextView.weight=1.5f;

        TextView productTextView = new TextView(this);
        productTextView.setText(order.getSalesOrdNum());
        productTextView.setGravity(Gravity.CENTER);
        productTextView.setLayoutParams(layoutParamsTextView);
        productTextView.setTextColor(getResources().getColor(R.color.color_black));
        productTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView batchTextView = new TextView(this);
        batchTextView.setText(order.getAddedDate());
        batchTextView.setGravity(Gravity.CENTER);
        batchTextView.setLayoutParams(layoutParamsTextView);
        batchTextView.setTextColor(getResources().getColor(R.color.color_black));
        batchTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TableRow.LayoutParams layoutParamsStatusTextView2 = new TableRow.LayoutParams(
                0,ViewGroup.LayoutParams.MATCH_PARENT,1.0f);
        layoutParamsStatusTextView2.gravity = Gravity.CENTER_VERTICAL;
        layoutParamsStatusTextView2.setMargins(1,5,1,5);
        layoutParamsStatusTextView2.weight=1.0f;

        TextView qtyTextView = new TextView(this);
        qtyTextView.setText(String.format("%.2f", order.getOrderValue()));
        qtyTextView.setGravity(Gravity.CENTER);
        qtyTextView.setLayoutParams(layoutParamsStatusTextView2);
        qtyTextView.setTextColor(getResources().getColor(R.color.color_black));
        qtyTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        String statusText="Online";
        if(order.getOrderStatus() == Constants.OrderStatus.PENDING){
            statusText="Pending";
        }else if(order.getOrderStatus() == Constants.OrderStatus.SYNCED){
            statusText="Sent";
        }

        TextView statusTextView = new TextView(this);
        statusTextView.setText(statusText);
        statusTextView.setGravity(Gravity.CENTER);
        statusTextView.setLayoutParams(layoutParamsStatusTextView2);
        statusTextView.setTextColor(getResources().getColor(R.color.color_black));
        statusTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        final int index = itemIndex;

        tableRow.setId(index);
        tableRow.addView(productTextView,0);
        tableRow.addView(batchTextView,1);
        tableRow.addView(qtyTextView,2);
        tableRow.addView(statusTextView,3);

        tableRow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                orderSelected(index);
            }
        });

        orderListTable.addView(tableRow, 0);

    }

    private void addReturnOrderToTable(ROSReturnOrder order){

        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsTableRow.topMargin=5;
        layoutParamsTableRow.bottomMargin=5;

        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(layoutParamsTableRow);
        tableRow.setBackgroundResource(R.drawable.border);

        TableRow.LayoutParams layoutParamsTextView = new TableRow.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT,1.0f);
        layoutParamsTextView.gravity = Gravity.CENTER_VERTICAL;
        layoutParamsTextView.setMargins(1,5,1,5);
        layoutParamsTextView.weight=1.5f;


        TextView productTextView = new TextView(this);
        productTextView.setText(order.getReturnNumb());
        productTextView.setGravity(Gravity.CENTER);
        productTextView.setLayoutParams(layoutParamsTextView);
        productTextView.setTextColor(getResources().getColor(R.color.color_black));
        productTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView batchTextView = new TextView(this);
        batchTextView.setText(order.getAddedDate());
        batchTextView.setGravity(Gravity.CENTER);
        batchTextView.setLayoutParams(layoutParamsTextView);
        batchTextView.setTextColor(getResources().getColor(R.color.color_black));
        batchTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TableRow.LayoutParams layoutParamsStatusTextView2 = new TableRow.LayoutParams(
                0,ViewGroup.LayoutParams.MATCH_PARENT,1.0f);
        layoutParamsStatusTextView2.gravity = Gravity.CENTER_VERTICAL;
        layoutParamsStatusTextView2.setMargins(1,5,1,5);
        layoutParamsStatusTextView2.weight=1.0f;

        TextView qtyTextView = new TextView(this);
        qtyTextView.setText(String.format("%.2f", order.getOrderValue()));
        qtyTextView.setGravity(Gravity.CENTER);
        qtyTextView.setLayoutParams(layoutParamsStatusTextView2);
        qtyTextView.setTextColor(getResources().getColor(R.color.color_black));
        qtyTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        String statusText="Online";
        if(order.getOrderStatus() == Constants.OrderStatus.PENDING){
            statusText="Pending";
        }else if(order.getOrderStatus() == Constants.OrderStatus.SYNCED){
            statusText="Sent";
        }

        TextView statusTextView = new TextView(this);
        statusTextView.setText(statusText);
        statusTextView.setGravity(Gravity.CENTER);
        statusTextView.setLayoutParams(layoutParamsStatusTextView2);
        statusTextView.setTextColor(getResources().getColor(R.color.color_black));
        statusTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));
        final int index = itemIndex;

        tableRow.setId(index);
        tableRow.addView(productTextView,0);
        tableRow.addView(batchTextView,1);
        tableRow.addView(qtyTextView,2);
        tableRow.addView(statusTextView,3);

        tableRow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                orderSelected(index);
            }
        });

        orderListTable.addView(tableRow, 0);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int selectedYear,
                          int selectedMonth, int selectedDay) {

        view.updateDate(selectedYear, selectedMonth, selectedDay);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DATE, selectedDay);
        c.set(Calendar.MONTH, selectedMonth);
        c.set(Calendar.YEAR, selectedYear);

        Date selectedDate = c.getTime();

        StringBuilder dateString=new StringBuilder().append(selectedDay).append("-").append(selectedMonth + 1)
                .append("-").append(selectedYear)
                .append(" ");
        if(fromDateSelect){

            c = Calendar.getInstance();
            c.set(Calendar.DATE, toDay);
            c.set(Calendar.MONTH, toMonth);
            c.set(Calendar.YEAR, toYear);

            Date toLDate = c.getTime();

            if (selectedDate.compareTo(toLDate) <= 0) {
                fromYear = selectedYear;
                fromMonth = selectedMonth;
                fromDay = selectedDay;
                fromDate.setText(dateString);
            }

        }else{

            c = Calendar.getInstance();
            c.set(Calendar.DATE, fromDay);
            c.set(Calendar.MONTH, fromMonth);
            c.set(Calendar.YEAR, fromYear);

            Date fromLDate = c.getTime();

            if (selectedDate.compareTo(fromLDate) >= 0) {
                toYear=selectedYear;
                toMonth=selectedMonth;
                toDay=selectedDay;
                toDate.setText(dateString);
            }
        }
    }

    /*
    Data service
    Sales
     */

    private void getSalesSuccess(ArrayList<ROSNewOrder> orders) {
        loadedFromDb = false;
        if (orders != null && orders.size() == 0) {//no online orders for dates

            ROSDbHelper dbHelper = new ROSDbHelper(this);
            String startDateStr = fromDateSelected + " 00:00:00";
            String endDateStr = toDateSelected + " 00:00:00";
            ArrayList<ROSNewOrder> localOrders = dbHelper.getNewOrders(this, selectedCustomer.getCustomerId(), startDateStr, endDateStr);

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

            for (int i = 0; i < localOrders.size(); i++) {
                ROSNewOrder order = localOrders.get(i);
                String dateStr = order.getAddedDate();

                try {
                    Date date = format1.parse(dateStr);
                    order.setAddedDate(format2.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            loadedFromDb = true;
            showSalesOrders(localOrders);
        }else {

            ROSDbHelper dbHelper = new ROSDbHelper(this);
            String startDateStr = fromDateSelected + " 00:00:00";
            String endDateStr = toDateSelected + " 00:00:00";
            ArrayList<ROSNewOrder> pendingOrders = dbHelper.getNewOrdersPending(this, selectedCustomer.getCustomerId(), startDateStr, endDateStr);

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

            for (int i = 0; i < pendingOrders.size(); i++) {
                ROSNewOrder order = pendingOrders.get(i);
                String dateStr = order.getAddedDate();

                try {
                    Date date = format1.parse(dateStr);
                    order.setAddedDate(format2.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            pendingOrders.addAll(orders);
            showSalesOrders(pendingOrders);
        }

        AppUtils.dismissProgressDialog();

    }

    private void getSalesFailed(int errorCode) {
        AppUtils.dismissProgressDialog();
        AppUtils.showAlertDialog(this, "Server error!", "Please try again.");
    }

    private void getSalesOrderList(String customerCode, String fromDate, String toDate) {
        AppUtils.showProgressDialog(this);
        NewOrderServiceHandler newOrderServiceHandler = new NewOrderServiceHandler(this);
        newOrderServiceHandler.getSalesOrderList(customerCode, fromDate, toDate, TAG, new NewOrderServiceHandler.SalesOrderListListener() {
            @Override
            public void onGetListSuccess(ArrayList<ROSNewOrder> orders) {
                getSalesSuccess(orders);
            }

            @Override
            public void onGetListError(VolleyError error) {
                if (error != null) {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                        getSalesFailed(401);
                    }else {
                        getSalesFailed(501);
                    }
                }else {
                    getSalesFailed(501);
                }
            }
        });
    }

    private void getSaleDetailsSuccess(ROSNewOrder order) {
        AppController.getInstance().setSelectedOrder(order);
        loadOrderViewActivity();
    }

    private void getSaleDetailsFailed(int errorCode) {
        AppUtils.showAlertDialog(ListOfOrderActivity.this, "Server Error", "Please try again.");
    }

    private void getSaleOrderDetails(String orderId){
        AppUtils.showProgressDialog(this);
        NewOrderServiceHandler newOrderServiceHandler = new NewOrderServiceHandler(getApplicationContext());
        newOrderServiceHandler.getSalesOrder(selectedCustomer.getCustCode(),orderId,TAG,new NewOrderServiceHandler.SalesOrderDetailsListener() {
            @Override
            public void onGetOrderSuccess(ROSNewOrder order) {
                AppUtils.dismissProgressDialog();
                getSaleDetailsSuccess(order);
            }

            @Override
            public void onGetOrderError(VolleyError error) {
                AppUtils.dismissProgressDialog();
                getSaleDetailsFailed(0);
            }
        });
    }

    /*
    Returns
     */

    private void getReturnsSuccess(ArrayList<ROSReturnOrder> orders) {
        loadedFromDb = false;
        if (orders != null && orders.size() == 0) {//no online orders for dates

            ROSDbHelper dbHelper = new ROSDbHelper(this);
            String startDateStr = fromDateSelected + " 00:00:00";
            String endDateStr = toDateSelected + " 00:00:00";
            ArrayList<ROSReturnOrder> localOrders = dbHelper.getReturnOrders(this, selectedCustomer.getCustomerId(), startDateStr, endDateStr);

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

            for (int i = 0; i < localOrders.size(); i++) {
                ROSReturnOrder order = localOrders.get(i);
                String dateStr = order.getAddedDate();

                try {
                    Date date = format1.parse(dateStr);
                    order.setAddedDate(format2.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            loadedFromDb = true;
            showReturnOrders(localOrders);
        }else {

            ROSDbHelper dbHelper = new ROSDbHelper(this);
            String startDateStr = fromDateSelected + " 00:00:00";
            String endDateStr = toDateSelected + " 00:00:00";
            ArrayList<ROSReturnOrder> pendingOrders = dbHelper.getReturnOrdersPending(this, selectedCustomer.getCustomerId(), startDateStr, endDateStr);

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");

            for (int i = 0; i < pendingOrders.size(); i++) {
                ROSReturnOrder order = pendingOrders.get(i);
                String dateStr = order.getAddedDate();

                try {
                    Date date = format1.parse(dateStr);
                    order.setAddedDate(format2.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            pendingOrders.addAll(orders);
            showReturnOrders(pendingOrders);
        }

        AppUtils.dismissProgressDialog();


    }

    private void getReturnsFailed(int errorCode) {
        AppUtils.dismissProgressDialog();
        AppUtils.showAlertDialog(this, "Server error!", "Please try again.");
    }

    private void getReturnOrderList(String customerCode, String fromDate, String toDate) {
        AppUtils.showProgressDialog(this);
        ReturnOrderServiceHandler returnOrderServiceHandler = new ReturnOrderServiceHandler(this);
        returnOrderServiceHandler.getReturnOrderList(customerCode, fromDate, toDate, TAG, new ReturnOrderServiceHandler.ReturnOrderListListener() {
            @Override
            public void onGetListSuccess(ArrayList<ROSReturnOrder> orders) {
                getReturnsSuccess(orders);
            }

            @Override
            public void onGetListError(VolleyError error) {
                if (error != null) {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                        getReturnsFailed(401);
                    }else {
                        getReturnsFailed(501);
                    }
                }else {
                    getReturnsFailed(501);
                }
            }
        });
    }

    private void getReturnDetailsSuccess(ROSReturnOrder order) {
        AppController.getInstance().setSelectedReturnOrder(order);
        loadOrderViewActivity();
    }

    private void getReturnDetailsFailed(int errorCode) {
        AppUtils.showAlertDialog(ListOfOrderActivity.this, "Server Error", "Please try again.");
    }

    private void getReturnOrderDetails(String orderId){
        AppUtils.showProgressDialog(this);
        ReturnOrderServiceHandler returnOrderServiceHandler = new ReturnOrderServiceHandler(this);
        returnOrderServiceHandler.getReturnOrder(selectedCustomer.getCustCode(), orderId, TAG, new ReturnOrderServiceHandler.ReturnOrderDetailsListener() {
            @Override
            public void onGetOrderSuccess(ROSReturnOrder order) {
                AppUtils.dismissProgressDialog();
                if (order == null) {
                    getReturnDetailsFailed(0);
                }else {
                    getReturnDetailsSuccess(order);
                }
            }

            @Override
            public void onGetOrderError(VolleyError error) {
                AppUtils.dismissProgressDialog();
                getReturnDetailsFailed(0);
            }
        });

    }
}
