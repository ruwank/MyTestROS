package com.relianceit.relianceorder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.relianceit.relianceorder.AppController;
import com.relianceit.relianceorder.R;
import com.relianceit.relianceorder.db.ROSDbHelper;
import com.relianceit.relianceorder.models.ROSCustomer;
import com.relianceit.relianceorder.models.ROSNewOrderItem;
import com.relianceit.relianceorder.models.ROSStock;
import com.relianceit.relianceorder.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NewOrderActivity extends RelianceBaseActivity implements OnItemSelectedListener{

    TableLayout orderTableLayout;
    Spinner productSpinner,batchSpinner;
    EditText quantityText,orderPriceText,orderDiscountText,freeItemText,invoiceValueText;
    TextView customerName,topSecondLabel,totalOutstanding,itemTotalAmount,totalAmountText,totalAmountTextLabel,grossValueLabel;
    ImageButton addOrderButton;
    int itemCount;
    Constants.Section section;
    ROSDbHelper dbHelper;
    ArrayList<String> products;
    ArrayList<String> batches;
    ROSStock stock;
    Map<String,ROSNewOrderItem> newOrderItemMap =  new HashMap<String,ROSNewOrderItem>();


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
        productSpinner.setOnItemSelectedListener(this);
        batchSpinner=(Spinner)findViewById(R.id.batch_spinner);
        batchSpinner.setOnItemSelectedListener(this);
        quantityText=(EditText)findViewById(R.id.order_quantity);
        quantityText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                updateItemTotalAmount();
            }
        });

        orderPriceText=(EditText)findViewById(R.id.order_price);
        orderPriceText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                updateItemTotalAmount();
            }
        });
        orderDiscountText=(EditText)findViewById(R.id.order_discount);
        orderDiscountText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                updateItemTotalAmount();
            }
        });
        freeItemText=(EditText)findViewById(R.id.order_free_item);
        freeItemText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                updateItemTotalAmount();
            }
        });

        itemTotalAmount=(TextView)findViewById(R.id.item_total_amount);
        totalAmountTextLabel=(TextView)findViewById(R.id.order_value_label);
       // totalAmountText=(TextView)findViewById(R.id.order_total_amount);

        grossValueLabel=(TextView)findViewById(R.id.gross_value);
        addOrderButton=(ImageButton)findViewById(R.id.btnAddOrder);
        addOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewOrder();

            }

        });
        dbHelper = new ROSDbHelper(getApplicationContext());

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


    }
    private void updateItemTotalAmount(){
        String productName= productSpinner.getSelectedItem().toString();
        String batchName= batchSpinner.getSelectedItem().toString();
        String quantity = quantityText.getText().toString();
        String orderPrice = orderPriceText.getText().toString();
        String orderDiscount = orderDiscountText.getText().toString();
        String freeItem = freeItemText.getText().toString();
        float total=0.00f;
        if(productName != null && productName.length()>0 && batchName !=null && batchName.length()>0 &&
        quantity != null && quantity.length()>0 && orderPrice !=null && orderPrice.length()>0){
            int quantityValue=Integer.parseInt(quantity);
            float orderPriceValue=Float.valueOf(orderPrice);
            total=orderPriceValue *quantityValue;

            if(orderDiscount !=null && orderDiscount.length()>0){
                float orderDiscountValue=Float.valueOf(orderDiscount);
                total= (float) (total*(1-(orderDiscountValue/100.0)));

            }

        }
        itemTotalAmount.setText(""+total);


    }
    private void updateTotalOrderValue(){
        double total=0.00;
        Iterator iterator = newOrderItemMap.keySet().iterator();
        while(iterator.hasNext()) {
            String key=(String)iterator.next();
            ROSNewOrderItem rosNewOrderItem=(ROSNewOrderItem)newOrderItemMap.get(key);
            total=total+rosNewOrderItem.getEffPrice();

        }
        grossValueLabel.setText(""+total);

    }

    private  void loadData(){
      ROSCustomer selectedCustomer= AppController.getInstance().getRosCustomer();
        totalOutstanding.setText(""+selectedCustomer.getOutstandingAmount());
        customerName.setText(selectedCustomer.getCustName());
        customizeActionBar();

        if(section == Constants.Section.ADD_SALE_RETURNS){
            topSecondLabel.setText("Invoice ");
            totalAmountTextLabel.setText("Return Value ");
            totalOutstanding.setVisibility(View.GONE);
            invoiceValueText.setVisibility(View.VISIBLE);

        }else{

            loadProductForSale();
            topSecondLabel.setText("Total outstanding : ");
            totalAmountTextLabel.setText("Order Value");
            totalOutstanding.setVisibility(View.VISIBLE);
            invoiceValueText.setVisibility(View.GONE);

        }
    }
    private void loadProductForSale(){
        products= dbHelper.getProductNamesForSale(getApplicationContext());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, products);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpinner.setAdapter(dataAdapter);
    }
    private void loadProductBatchForSale(String productName){
        Log.v("productName :",productName);
        batches= dbHelper.getBatchNamesForSale(getApplicationContext(),productName);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, batches);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        batchSpinner.setAdapter(dataAdapter);

    }
    private void loadStockForSale(){
        String productName= productSpinner.getSelectedItem().toString();
        String batchName= batchSpinner.getSelectedItem().toString();
        Log.v("productName :",productName);
        stock= dbHelper.getStockForSale(getApplicationContext(), productName, batchName);

    }

    private void addNewOrder(){

        String productName= productSpinner.getSelectedItem().toString();
        String batchName= batchSpinner.getSelectedItem().toString();
        String quantity = quantityText.getText().toString();
        String orderPrice = orderPriceText.getText().toString();
        String orderDiscount = orderDiscountText.getText().toString();
        String freeItem = freeItemText.getText().toString();
        final String total=itemTotalAmount.getText().toString();
        if(productName != null && productName.length()>0 && batchName !=null && batchName.length()>0 &&
                quantity != null && quantity.length()>0 && orderPrice !=null && orderPrice.length()>0) {
            itemCount++;
            final int index=itemCount;
            ROSNewOrderItem newOrderItem=new ROSNewOrderItem();
            newOrderItem.setProductBatchCode(batchName);
            newOrderItem.setProductDescription(productName);
            newOrderItem.setQtyOrdered(Integer.parseInt(quantity));
            newOrderItem.setQtyBonus(Integer.parseInt(freeItem));
            newOrderItem.setEffPrice(Float.valueOf(total));
            newOrderItem.setProductBatchCode(stock.getProductBatchCode());
            newOrderItem.setUnitPrice(Float.valueOf(orderPrice));
            newOrderItem.setProdDiscount(Float.valueOf(orderDiscount));


            newOrderItemMap.put(""+itemCount,newOrderItem);
            updateTotalOrderValue();

            TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsTableRow.topMargin = 5;
            layoutParamsTableRow.bottomMargin = 5;
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(layoutParamsTableRow);
            //tableRow.setBackgroundResource(R.drawable.border);


            TableRow.LayoutParams layoutParamsTextView = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
            layoutParamsTextView.gravity = Gravity.CENTER_VERTICAL;
            layoutParamsTextView.setMargins(5, 10, 5, 5);
            layoutParamsTextView.weight = 1.5f;

            TextView productTextView = new TextView(this);
            productTextView.setText(productName);
            productTextView.setGravity(Gravity.LEFT);
            productTextView.setLayoutParams(layoutParamsTextView);
            productTextView.setTextColor(getResources().getColor(R.color.color_black));
            productTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

            TextView batchTextView = new TextView(this);
            batchTextView.setText(batchName);
            batchTextView.setGravity(Gravity.LEFT);
            batchTextView.setLayoutParams(layoutParamsTextView);
            batchTextView.setTextColor(getResources().getColor(R.color.color_black));
            batchTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));


            TableRow.LayoutParams layoutParamsTextView2 = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
            layoutParamsTextView2.gravity = Gravity.CENTER_VERTICAL;
            layoutParamsTextView2.setMargins(5, 10, 5, 5);
            layoutParamsTextView2.weight = 1f;

            TextView qtyTextView = new TextView(this);
            qtyTextView.setText(quantity);
            qtyTextView.setGravity(Gravity.LEFT);
            qtyTextView.setLayoutParams(layoutParamsTextView2);
            qtyTextView.setTextColor(getResources().getColor(R.color.color_black));
            qtyTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

            TextView priceTextView = new TextView(this);
            priceTextView.setText(orderPrice);
            priceTextView.setGravity(Gravity.LEFT);
            priceTextView.setLayoutParams(layoutParamsTextView2);
            priceTextView.setTextColor(getResources().getColor(R.color.color_black));
            priceTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

            TextView discTextView = new TextView(this);
            discTextView.setText(orderDiscount);
            discTextView.setGravity(Gravity.LEFT);
            discTextView.setLayoutParams(layoutParamsTextView2);
            discTextView.setTextColor(getResources().getColor(R.color.color_black));
            discTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

            TextView freeItemTextView = new TextView(this);
            freeItemTextView.setText(freeItem);
            freeItemTextView.setGravity(Gravity.LEFT);
            freeItemTextView.setLayoutParams(layoutParamsTextView2);
            freeItemTextView.setTextColor(getResources().getColor(R.color.color_black));
            freeItemTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

            TextView totalValueTextView = new TextView(this);
            totalValueTextView.setText(total);
            totalValueTextView.setGravity(Gravity.LEFT);
            totalValueTextView.setLayoutParams(layoutParamsTextView2);
            totalValueTextView.setTextColor(getResources().getColor(R.color.color_black));
            totalValueTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

            TableRow.LayoutParams layoutParamsImageButton = new TableRow.LayoutParams(80, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsImageButton.gravity = Gravity.RIGHT;

            ImageButton removeItemButton = new ImageButton(this);
            removeItemButton.setImageResource(R.mipmap.btn_delete_item);
            //removeItemButton.setLayoutParams(layoutParamsTextView2);
            removeItemButton.setBackgroundResource(R.color.color_transparent);

            removeItemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeOrder(index);
                }

            });

            tableRow.setId(index);


            tableRow.addView(productTextView, 0);
            tableRow.addView(batchTextView, 1);
            tableRow.addView(qtyTextView, 2);
            tableRow.addView(priceTextView, 3);
            tableRow.addView(discTextView, 4);
            tableRow.addView(freeItemTextView, 5);
            tableRow.addView(totalValueTextView, 6);
            tableRow.addView(removeItemButton, 7);

            //tableRow.addView(productTextView, 0);
            //tableRow.addView(batchTextView, 1);
            orderTableLayout.addView(tableRow, 0);

            quantityText.setText("");
            orderPriceText.setText("");
            orderDiscountText.setText("");
            freeItemText.setText("");
            itemTotalAmount.setText("");
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
    private void  removeOrder(int index){

        for (int i = 0; i < orderTableLayout.getChildCount(); i++) {
            View child = orderTableLayout.getChildAt(i);

            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;
                Log.v("row","getId:"+row.getId() +"index: "+index);
                if(row.getId()==index){
                    newOrderItemMap.remove(""+row.getId());
                    updateTotalOrderValue();
                  //  itemCount--;
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

        switch (parent.getId()) {
            case R.id.product_spinner:
                loadProductBatchForSale(products.get(position));
                // do stuffs with you spinner 1
                break;
            case R.id.batch_spinner:
                loadStockForSale();
                // do stuffs with you spinner 2
                break;
            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
