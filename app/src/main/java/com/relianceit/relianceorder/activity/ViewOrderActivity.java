package com.relianceit.relianceorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.relianceit.relianceorder.AppController;
import com.relianceit.relianceorder.R;
import com.relianceit.relianceorder.models.ROSCustomer;
import com.relianceit.relianceorder.models.ROSNewOrder;
import com.relianceit.relianceorder.models.ROSNewOrderItem;
import com.relianceit.relianceorder.models.ROSReturnOrder;
import com.relianceit.relianceorder.models.ROSReturnOrderItem;
import com.relianceit.relianceorder.util.Constants;

import java.util.ArrayList;

public class ViewOrderActivity extends ActionBarActivity {

    int itemIndex;
    TableLayout orderTable;
    Constants.Section section;
    TextView orderNoLabel,orderNo,orderValueLabel,customerName;
    TextView dateValue,overallDis,grossValue,discountValue,orderValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        Intent intent = getIntent();
        section = (Constants.Section) intent.getSerializableExtra("section");

        orderTable=(TableLayout)findViewById(R.id.order_view_table);
        orderNoLabel=(TextView)findViewById(R.id.order_no_label);
        orderValueLabel=(TextView)findViewById(R.id.order_value_label);
        orderNo=(TextView)findViewById(R.id.order_no);
        dateValue=(TextView)findViewById(R.id.order_date);
        customerName=(TextView)findViewById(R.id.customer_name);
        grossValue=(TextView)findViewById(R.id.gross_value);
        overallDis=(TextView)findViewById(R.id.overall_dis);
        discountValue=(TextView)findViewById(R.id.discount_value);
        orderValue=(TextView)findViewById(R.id.order_value);

        updateLabel();
        loadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
        if(section == Constants.Section.VIEW_SALE_RETURNS){
            titleText=titleText+" - "+getString(R.string.section_view_return);
        }else{
            titleText=titleText+" - "+getString(R.string.section_order_view);

        }
        textViewTitle.setText(titleText);

        actionBar.setCustomView(viewActionBar,params);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void updateLabel(){
        customizeActionBar();

        if(section == Constants.Section.VIEW_SALE_RETURNS){
            orderNoLabel.setText("Return No : ");
            orderValueLabel.setText("Return Value");
        }else{
            orderNoLabel.setText("Order No : ");
            orderValueLabel.setText("Order Value ");
        }
    }

    private void loadData() {

        ROSCustomer selectedCustomer = AppController.getInstance().getRosCustomer();

        customerName.setText(selectedCustomer.getCustName());

        if(section == Constants.Section.VIEW_SALE_RETURNS) {
            ROSReturnOrder order = AppController.getInstance().getSelectedReturnOrder();
            orderNo.setText(order.getReturnNumb());
            dateValue.setText(order.getAddedDate());
            double discountValueText=order.getGrossValue()-order.getOrderValue();

            grossValue.setText(String.format("%.2f", order.getGrossValue()));
            discountValue.setText(String.format("%.2f", discountValueText));
            overallDis.setText(String.format("%.2f",order.getOVDiscount()));
            orderValue.setText(String.format("%.2f",order.getOrderValue()));

            ArrayList<ROSReturnOrderItem> itemArrayList = order.getProducts();
            itemIndex = 0;
            orderTable.removeAllViews();
            if (itemArrayList != null) {
                for (int i = 0; i < itemArrayList.size(); i++) {
                    itemIndex = i;
                    ROSReturnOrderItem item = itemArrayList.get(i);
                    addReturnOrderItem(item, itemIndex);
                }
            }
        }else {
            ROSNewOrder order = AppController.getInstance().getSelectedOrder();
            orderNo.setText(order.getSalesOrdNum());
            dateValue.setText(order.getAddedDate());
            double discountValueText=order.getGrossValue()-order.getOrderValue();

            grossValue.setText(String.format("%.2f", order.getGrossValue()));
            discountValue.setText(String.format("%.2f", discountValueText));
            overallDis.setText(String.format("%.2f",order.getOVDiscount()));
            orderValue.setText(String.format("%.2f",order.getOrderValue()));

            ArrayList<ROSNewOrderItem> itemArrayList = order.getProducts();
            itemIndex = 0;
            orderTable.removeAllViews();
            if (itemArrayList != null) {
                for (int i = 0; i < itemArrayList.size(); i++) {
                    itemIndex = i;
                    ROSNewOrderItem item = itemArrayList.get(i);
                    addSaleOrderItem(item, itemIndex);
                }
            }
        }
    }

    private void addSaleOrderItem(ROSNewOrderItem item, int index){

        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsTableRow.topMargin=5;
        layoutParamsTableRow.bottomMargin=5;
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(layoutParamsTableRow);
        tableRow.setBackgroundResource(R.drawable.border);

        TableRow.LayoutParams layoutParamsTextView = new TableRow.LayoutParams(
                0,ViewGroup.LayoutParams.MATCH_PARENT,1.0f);
        layoutParamsTextView.gravity = Gravity.CENTER_VERTICAL;
        layoutParamsTextView.setMargins(1,5,1,5);
        layoutParamsTextView.weight=1.5f;

        TextView productTextView = new TextView(this);
        productTextView.setText(item.getBrandName()+" - "+item.getProductDescription());
        productTextView.setGravity(Gravity.CENTER);
        productTextView.setLayoutParams(layoutParamsTextView);
        productTextView.setTextColor(getResources().getColor(R.color.color_black));
        productTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView batchTextView = new TextView(this);
        batchTextView.setText(item.getProductUserCode());
        batchTextView.setGravity(Gravity.CENTER);
        batchTextView.setLayoutParams(layoutParamsTextView);
        batchTextView.setTextColor(getResources().getColor(R.color.color_black));
        batchTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TableRow.LayoutParams layoutParamsTextView2 = new TableRow.LayoutParams(
                0,ViewGroup.LayoutParams.MATCH_PARENT,1.0f);
        layoutParamsTextView2.gravity = Gravity.CENTER_VERTICAL;
        layoutParamsTextView2.setMargins(1,5,1,5);
        layoutParamsTextView2.weight=1.0f;

        TextView qtyTextView = new TextView(this);
        qtyTextView.setText(""+item.getQtyOrdered());
        qtyTextView.setGravity(Gravity.CENTER);
        qtyTextView.setLayoutParams(layoutParamsTextView2);
        qtyTextView.setTextColor(getResources().getColor(R.color.color_black));
        qtyTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        Log.v("item ","item :" +item.getUnitPrice());
        TextView priceTextView = new TextView(this);
        priceTextView.setText(String.format("%.2f",item.getUnitPrice()));
        priceTextView.setGravity(Gravity.CENTER);
        priceTextView.setLayoutParams(layoutParamsTextView2);
        priceTextView.setTextColor(getResources().getColor(R.color.color_black));
        priceTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView discTextView = new TextView(this);
        discTextView.setText(""+item.getProdDiscount());
        discTextView.setGravity(Gravity.CENTER);
        discTextView.setLayoutParams(layoutParamsTextView2);
        discTextView.setTextColor(getResources().getColor(R.color.color_black));
        discTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView freeItemTextView = new TextView(this);
        freeItemTextView.setText(""+item.getQtyBonus());
        freeItemTextView.setGravity(Gravity.CENTER);
        freeItemTextView.setLayoutParams(layoutParamsTextView2);
        freeItemTextView.setTextColor(getResources().getColor(R.color.color_black));
        freeItemTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView totalValueTextView = new TextView(this);
        totalValueTextView.setText(String.format("%.2f",item.getEffPrice()));
        totalValueTextView.setGravity(Gravity.CENTER);
        totalValueTextView.setLayoutParams(layoutParamsTextView2);
        totalValueTextView.setTextColor(getResources().getColor(R.color.color_black));
        totalValueTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        tableRow.addView(productTextView,0);
        tableRow.addView(batchTextView,1);
        tableRow.addView(qtyTextView,2);
        tableRow.addView(priceTextView,3);
        tableRow.addView(discTextView,4);
        tableRow.addView(freeItemTextView,5);
        tableRow.addView(totalValueTextView,6);

        orderTable.addView(tableRow, index);
    }

    private void addReturnOrderItem(ROSReturnOrderItem item, int index){

        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsTableRow.topMargin=5;
        layoutParamsTableRow.bottomMargin=5;
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(layoutParamsTableRow);
        tableRow.setBackgroundResource(R.drawable.border);

        TableRow.LayoutParams layoutParamsTextView = new TableRow.LayoutParams(
                0,ViewGroup.LayoutParams.MATCH_PARENT,1.0f);
        layoutParamsTextView.gravity = Gravity.CENTER_VERTICAL;
        layoutParamsTextView.setMargins(1,5,1,5);
        layoutParamsTextView.weight=1.5f;

        TextView productTextView = new TextView(this);
        productTextView.setText(item.getBrandName()+ " - "+ item.getProductDescription());
        productTextView.setGravity(Gravity.CENTER);
        productTextView.setLayoutParams(layoutParamsTextView);
        productTextView.setTextColor(getResources().getColor(R.color.color_black));
        productTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView batchTextView = new TextView(this);
        batchTextView.setText(item.getProductUserCode());
        batchTextView.setGravity(Gravity.CENTER);
        batchTextView.setLayoutParams(layoutParamsTextView);
        batchTextView.setTextColor(getResources().getColor(R.color.color_black));
        batchTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TableRow.LayoutParams layoutParamsTextView2 = new TableRow.LayoutParams(
                0,ViewGroup.LayoutParams.MATCH_PARENT,1.0f);
        layoutParamsTextView2.gravity = Gravity.CENTER_VERTICAL;
        layoutParamsTextView2.setMargins(1,5,1,5);
        layoutParamsTextView2.weight=1.0f;

        TextView qtyTextView = new TextView(this);
        qtyTextView.setText(""+item.getQtyOrdered());
        qtyTextView.setGravity(Gravity.CENTER);
        qtyTextView.setLayoutParams(layoutParamsTextView2);
        qtyTextView.setTextColor(getResources().getColor(R.color.color_black));
        qtyTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView priceTextView = new TextView(this);
        priceTextView.setText(String.format("%.2f",item.getUnitPrice()));
        priceTextView.setGravity(Gravity.CENTER);
        priceTextView.setLayoutParams(layoutParamsTextView2);
        priceTextView.setTextColor(getResources().getColor(R.color.color_black));
        priceTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView discTextView = new TextView(this);
        discTextView.setText(""+item.getProdDiscount());
        discTextView.setGravity(Gravity.CENTER);
        discTextView.setLayoutParams(layoutParamsTextView2);
        discTextView.setTextColor(getResources().getColor(R.color.color_black));
        discTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView freeItemTextView = new TextView(this);
        freeItemTextView.setText(""+item.getQtyBonus());
        freeItemTextView.setGravity(Gravity.CENTER);
        freeItemTextView.setLayoutParams(layoutParamsTextView2);
        freeItemTextView.setTextColor(getResources().getColor(R.color.color_black));
        freeItemTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView totalValueTextView = new TextView(this);
        totalValueTextView.setText(String.format("%.2f",item.getEffPrice()));
        totalValueTextView.setGravity(Gravity.CENTER);
        totalValueTextView.setLayoutParams(layoutParamsTextView2);
        totalValueTextView.setTextColor(getResources().getColor(R.color.color_black));
        totalValueTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        tableRow.addView(productTextView,0);
        tableRow.addView(batchTextView,1);
        tableRow.addView(qtyTextView,2);
        tableRow.addView(priceTextView,3);
        tableRow.addView(discTextView,4);
        tableRow.addView(freeItemTextView,5);
        tableRow.addView(totalValueTextView,6);

        orderTable.addView(tableRow, index);
    }
}
