package com.relianceit.relianceorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.relianceit.relianceorder.R;
import com.relianceit.relianceorder.util.Constants;

public class ViewOrderActivity extends ActionBarActivity {

    int itemIndex;
    TableLayout orderTable;
    Constants.Section section;
    TextView orderNoLabel,orderValueLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        Intent intent = getIntent();
        section = (Constants.Section) intent.getSerializableExtra("section");

        orderNoLabel=(TextView)findViewById(R.id.order_no_label);
        orderValueLabel=(TextView)findViewById(R.id.order_value_label);


        updateLabel();

        itemIndex=0;
        orderTable=(TableLayout)findViewById(R.id.order_table);
        for (int i = 0; i <3 ; i++) {
            viewOrder();
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
    private  void updateLabel(){
        customizeActionBar();

        if(section == Constants.Section.VIEW_SALE_RETURNS){
            orderNoLabel.setText("Return No : ");
            orderValueLabel.setText("Return Value");

        }else{
            orderNoLabel.setText("Order No : ");
            orderValueLabel.setText("Order Value ");

        }
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

    private void viewOrder(){

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
        productTextView.setText("Product 1");
        productTextView.setGravity(Gravity.CENTER);
        productTextView.setLayoutParams(layoutParamsTextView);
        productTextView.setTextColor(getResources().getColor(R.color.color_black));
        productTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView batchTextView = new TextView(this);
        batchTextView.setText("batch 1");
        batchTextView.setGravity(Gravity.CENTER);
        batchTextView.setLayoutParams(layoutParamsTextView);
        batchTextView.setTextColor(getResources().getColor(R.color.color_black));
        batchTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView qtyTextView = new TextView(this);
        qtyTextView.setText("10");
        qtyTextView.setGravity(Gravity.CENTER);
        qtyTextView.setLayoutParams(layoutParamsTextView);
        qtyTextView.setTextColor(getResources().getColor(R.color.color_black));
        qtyTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView priceTextView = new TextView(this);
        priceTextView.setText("100.00");
        priceTextView.setGravity(Gravity.CENTER);
        priceTextView.setLayoutParams(layoutParamsTextView);
        priceTextView.setTextColor(getResources().getColor(R.color.color_black));
        priceTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView discTextView = new TextView(this);
        discTextView.setText("2");
        discTextView.setGravity(Gravity.CENTER);
        discTextView.setLayoutParams(layoutParamsTextView);
        discTextView.setTextColor(getResources().getColor(R.color.color_black));
        discTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView freeItemTextView = new TextView(this);
        freeItemTextView.setText("1");
        freeItemTextView.setGravity(Gravity.CENTER);
        freeItemTextView.setLayoutParams(layoutParamsTextView);
        freeItemTextView.setTextColor(getResources().getColor(R.color.color_black));
        freeItemTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView totalValueTextView = new TextView(this);
        totalValueTextView.setText("1200.00");
        totalValueTextView.setGravity(Gravity.CENTER);
        totalValueTextView.setLayoutParams(layoutParamsTextView);
        totalValueTextView.setTextColor(getResources().getColor(R.color.color_black));
        totalValueTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));


        tableRow.setId(itemIndex);
        //

        tableRow.addView(productTextView,0);
        tableRow.addView(batchTextView,1);
        tableRow.addView(qtyTextView,2);
        tableRow.addView(priceTextView,3);
        tableRow.addView(discTextView,4);
        tableRow.addView(freeItemTextView,5);
        tableRow.addView(totalValueTextView,6);

        orderTable.addView(tableRow, 2);

    }

}
