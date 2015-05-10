package com.relianceit.relianceorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.relianceit.relianceorder.R;
import com.relianceit.relianceorder.db.ROSDbHelper;
import com.relianceit.relianceorder.models.ROSProduct;
import com.relianceit.relianceorder.util.Constants;

import java.util.ArrayList;

public class NewOrderActivity extends RelianceBaseActivity implements OnItemSelectedListener{

    TableLayout orderTableLayout;
    Spinner productSpinner,batchSpinner;
    EditText quantityText,orderPriceText,orderDiscountText,freeItemText,invoiceValueText;
    TextView customerName,topSecondLabel,totalOutstanding,totalAmountText,totalAmountTextLabel;
    ImageButton addOrderButton;
    int itemCount;
    Constants.Section section;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_order);

        Intent intent = getIntent();
        section = (Constants.Section) intent.getSerializableExtra("section");



        itemCount=0;
       // new_order_table

        customerName=(TextView)findViewById(R.id.customer_name);
        topSecondLabel=(TextView)findViewById(R.id.top_second_label);
        totalOutstanding=(TextView)findViewById(R.id.total_outstanding);
        invoiceValueText=(EditText)findViewById(R.id.invoice_value);


        orderTableLayout=(TableLayout)findViewById(R.id.new_order_table);


        productSpinner=(Spinner)findViewById(R.id.product_spinner);
        batchSpinner=(Spinner)findViewById(R.id.batch_spinner);
        quantityText=(EditText)findViewById(R.id.order_quantity);
        orderPriceText=(EditText)findViewById(R.id.order_price);
        orderDiscountText=(EditText)findViewById(R.id.order_discount);
        freeItemText=(EditText)findViewById(R.id.order_free_item);
        totalAmountTextLabel=(TextView)findViewById(R.id.order_value_label);
        totalAmountText=(TextView)findViewById(R.id.order_total_amount);

        addOrderButton=(ImageButton)findViewById(R.id.btnAddOrder);

        addOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewOrder();

            }

        });
        loadData();

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
        if(section == Constants.Section.ADD_SALE_RETURNS){
            titleText=titleText+" - "+getString(R.string.section_sales_return);
        }else{
            titleText=titleText+" - "+getString(R.string.section_new_order);

        }
        textViewTitle.setText(titleText);

        actionBar.setCustomView(viewActionBar,params);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        /*
        //Customize the ActionBar
        final ActionBar actionBar = getSupportActionBar();
        View viewActionBar = getLayoutInflater().inflate(R.layout.action_bar_custom_layout, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT,
                Gravity.CENTER);
        TextView textViewTitle = (TextView) viewActionBar.findViewById(R.id.actionbar_textview);
        textViewTitle.setText("Test");
        actionBar.setCustomView(viewActionBar,params);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        */
    }
    private  void loadData(){
        customizeActionBar();

        if(section == Constants.Section.ADD_SALE_RETURNS){
            topSecondLabel.setText("Invoice ");
            totalAmountTextLabel.setText("Return Value ");
            totalOutstanding.setVisibility(View.GONE);
            invoiceValueText.setVisibility(View.VISIBLE);

        }else{
            ROSDbHelper dbHelper = new ROSDbHelper(getApplicationContext());
            ArrayList<ROSProduct> products= dbHelper.getProducts(getApplicationContext());
            topSecondLabel.setText("Total outstanding : ");
            totalAmountTextLabel.setText("Order Value");
            totalOutstanding.setVisibility(View.VISIBLE);
            invoiceValueText.setVisibility(View.GONE);

        }
    }
    private void addNewOrder(){

        itemCount++;
        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsTableRow.topMargin=5;
        layoutParamsTableRow.bottomMargin=5;
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(layoutParamsTableRow);
        //tableRow.setBackgroundResource(R.drawable.border);



        TableRow.LayoutParams layoutParamsTextView = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT,1.0f);
        layoutParamsTextView.gravity = Gravity.CENTER_VERTICAL;
        layoutParamsTextView.setMargins(1,10,1,1);


        TextView productTextView = new TextView(this);
        productTextView.setText("Product 1");
        productTextView.setGravity(Gravity.CENTER);
        productTextView.setLayoutParams(layoutParamsTextView);
        productTextView.setTextColor(getResources().getColor(R.color.color_black));
        productTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView batchTextView = new TextView(this);
        batchTextView.setText("00004");
        batchTextView.setGravity(Gravity.CENTER);
        batchTextView.setLayoutParams(layoutParamsTextView);
        batchTextView.setTextColor(getResources().getColor(R.color.color_black));
        batchTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView qtyTextView = new TextView(this);
        qtyTextView.setText("100");
        qtyTextView.setGravity(Gravity.CENTER);
        qtyTextView.setLayoutParams(layoutParamsTextView);
        qtyTextView.setTextColor(getResources().getColor(R.color.color_black));
        qtyTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView priceTextView = new TextView(this);
        priceTextView.setText("500.00");
        priceTextView.setGravity(Gravity.CENTER);
        priceTextView.setLayoutParams(layoutParamsTextView);
        priceTextView.setTextColor(getResources().getColor(R.color.color_black));
        priceTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView discTextView = new TextView(this);
        discTextView.setText("1");
        discTextView.setGravity(Gravity.CENTER);
        discTextView.setLayoutParams(layoutParamsTextView);
        discTextView.setTextColor(getResources().getColor(R.color.color_black));
        discTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView freeItemTextView = new TextView(this);
        freeItemTextView.setText("2");
        freeItemTextView.setGravity(Gravity.CENTER);
        freeItemTextView.setLayoutParams(layoutParamsTextView);
        freeItemTextView.setTextColor(getResources().getColor(R.color.color_black));
        freeItemTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView totalValueTextView = new TextView(this);
        totalValueTextView.setText("4000.00");
        totalValueTextView.setGravity(Gravity.CENTER);
        totalValueTextView.setLayoutParams(layoutParamsTextView);
        totalValueTextView.setTextColor(getResources().getColor(R.color.color_black));
        totalValueTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TableRow.LayoutParams layoutParamsImageButton = new TableRow.LayoutParams(80,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsImageButton.gravity = Gravity.CENTER;

        ImageButton removeItemButton = new ImageButton(this);
        removeItemButton.setImageResource(R.mipmap.btn_delete_item);
        removeItemButton.setLayoutParams(layoutParamsImageButton);
        removeItemButton.setBackgroundResource(R.color.color_transparent);

        removeItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeOrder(itemCount);
            }

        });

        tableRow.setId(itemCount);
        //
       // removeItemButton.setMaxWidth(80);
       // removeItemButton.setTextColor(getResources().getColor(R.color.color_black));

        tableRow.addView(productTextView,0);
        tableRow.addView(batchTextView,1);
        tableRow.addView(qtyTextView,2);
        tableRow.addView(priceTextView,3);
        tableRow.addView(discTextView,4);
        tableRow.addView(freeItemTextView,5);
        tableRow.addView(totalValueTextView,6);
        tableRow.addView(removeItemButton,7);

        //tableRow.addView(productTextView, 0);
        //tableRow.addView(batchTextView, 1);
        orderTableLayout.addView(tableRow, 0);

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
    private void  removeOrder(int index){

        for (int i = 0; i < orderTableLayout.getChildCount(); i++) {
            View child = orderTableLayout.getChildAt(i);

            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;
                if(row.getId()==index){
                    itemCount--;
                    orderTableLayout.removeView(row);
                }

//                for (int x = 0; x < row.getChildCount(); x++) {
//                    View view = row.getChildAt(x);
//                    view.setEnabled(false);
//                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
