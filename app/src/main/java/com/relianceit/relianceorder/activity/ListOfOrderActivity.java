package com.relianceit.relianceorder.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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
import com.relianceit.relianceorder.fragment.DatePickerDialogFragment;
import com.relianceit.relianceorder.models.ROSCustomer;
import com.relianceit.relianceorder.models.ROSNewOrder;
import com.relianceit.relianceorder.services.NewOrderServiceHandler;
import com.relianceit.relianceorder.util.AppUtils;
import com.relianceit.relianceorder.util.ConnectionDetector;
import com.relianceit.relianceorder.util.Constants;

import java.util.ArrayList;
import java.util.Calendar;

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

    //
// test comment
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

        if(section == Constants.Section.VIEW_SALE_RETURNS_LIST){
            for (int i = 0; i <4 ; i++) {
                showOrderItem();
            }
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        updateButtonTapped();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppController.getInstance().cancelPendingRequests(TAG);
    }

    private void updateButtonTapped() {
        if(section != Constants.Section.VIEW_SALE_RETURNS_LIST){
            if (!ConnectionDetector.isConnected(this)) {
                AppUtils.showAlertDialog(this, Constants.MSG_NO_INTERNET_TITLE, Constants.MSG_NO_INTERNET_MSG);
            }else {
              //  getSalesOrderList("00001", "2014-01-01", "2016-01-01");
              //  Log.i("selectedCustomer.getCustCode()",selectedCustomer.getCustCode());
                getSalesOrderList(selectedCustomer.getCustomerId(), fromDate.getText().toString(), toDate.getText().toString());
            }
        }else {
            showOrderItem();
        }
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
            titleText=titleText+" - "+getString(R.string.section_sales_return);
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
    private  void updateLabel(){
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

    private void showSalesOrders(ArrayList<ROSNewOrder> orders) {

        Log.i(TAG, "Row count: " + itemIndex);

        orderListTable.removeAllViews();


        itemIndex = 0;
        for (int i = 0; i < orders.size(); i++) {
            itemIndex = i;
            ROSNewOrder order = orders.get(i);
            addSaleOrderToTable(order);
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

        TableRow.LayoutParams layoutParamsTextView = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,1.0f);
        layoutParamsTextView.gravity = Gravity.CENTER_VERTICAL;
        layoutParamsTextView.setMargins(1,5,1,5);

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

        TextView qtyTextView = new TextView(this);
        qtyTextView.setText("" + order.getOrderValue());
        qtyTextView.setGravity(Gravity.CENTER);
        qtyTextView.setLayoutParams(layoutParamsTextView);
        qtyTextView.setTextColor(getResources().getColor(R.color.color_black));
        qtyTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        final int index = itemIndex;

        //tableRow.setTag(ROW_TAG);
        tableRow.setId(index);
        tableRow.addView(productTextView,0);
        tableRow.addView(batchTextView,1);
        tableRow.addView(qtyTextView,2);

        tableRow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loadOrderScreen(index);
            }
        });

        orderListTable.addView(tableRow, 0);

    }

    private void showOrderItem(){

        itemIndex++;
        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsTableRow.topMargin=5;
        layoutParamsTableRow.bottomMargin=5;

        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(layoutParamsTableRow);
        tableRow.setBackgroundResource(R.drawable.border);

        TableRow.LayoutParams layoutParamsTextView = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,1.0f);
        layoutParamsTextView.gravity = Gravity.CENTER_VERTICAL;
        layoutParamsTextView.setMargins(1,5,1,5);

        TextView productTextView = new TextView(this);
        productTextView.setText("product 1");
        productTextView.setGravity(Gravity.CENTER);
        productTextView.setLayoutParams(layoutParamsTextView);
        productTextView.setTextColor(getResources().getColor(R.color.color_black));
        productTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView batchTextView = new TextView(this);
        batchTextView.setText("000002");
        batchTextView.setGravity(Gravity.CENTER);
        batchTextView.setLayoutParams(layoutParamsTextView);
        batchTextView.setTextColor(getResources().getColor(R.color.color_black));
        batchTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView qtyTextView = new TextView(this);
        qtyTextView.setText("500");
        qtyTextView.setGravity(Gravity.CENTER);
        qtyTextView.setLayoutParams(layoutParamsTextView);
        qtyTextView.setTextColor(getResources().getColor(R.color.color_black));
        qtyTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        tableRow.setId(itemIndex);
        tableRow.addView(productTextView,0);
        tableRow.addView(batchTextView,1);
        tableRow.addView(qtyTextView,2);

        tableRow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loadOrderScreen(itemIndex);
            }
        });

        orderListTable.addView(tableRow, 3);

    }

    private  void loadOrderScreen(int index){
        Intent intent = new Intent(getApplicationContext(),
                ViewOrderActivity.class);
        if(section == Constants.Section.VIEW_SALE_RETURNS_LIST){
            intent.putExtra("section", Constants.Section.VIEW_SALE_RETURNS);

        }else{
            intent.putExtra("section", Constants.Section.VIEW_ORDER);
        }
        startActivity(intent);
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

        StringBuilder dateString=new StringBuilder().append(selectedDay).append("-").append(selectedMonth + 1)
                .append("-").append(selectedYear)
                .append(" ");
        if(fromDateSelect){
            fromYear=selectedYear;
            fromMonth=selectedMonth;
            fromDay=selectedDay;
            fromDate.setText(dateString);
        }else{
            toYear=selectedYear;
            toMonth=selectedMonth;
            toDay=selectedDay;
            toDate.setText(dateString);
        }
    }

    /*
    Data service
     */

    private void getSalesSuccess(ArrayList<ROSNewOrder> orders) {
        AppUtils.dismissProgressDialog();
        showSalesOrders(orders);
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
}
