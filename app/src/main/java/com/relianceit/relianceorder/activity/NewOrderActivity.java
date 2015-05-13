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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.relianceit.relianceorder.AppController;
import com.relianceit.relianceorder.R;
import com.relianceit.relianceorder.db.ROSDbHelper;
import com.relianceit.relianceorder.models.ROSCustomer;
import com.relianceit.relianceorder.models.ROSNewOrder;
import com.relianceit.relianceorder.models.ROSNewOrderItem;
import com.relianceit.relianceorder.models.ROSStock;
import com.relianceit.relianceorder.services.NewOrderServiceHandler;
import com.relianceit.relianceorder.util.AppUtils;
import com.relianceit.relianceorder.util.ConnectionDetector;
import com.relianceit.relianceorder.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class NewOrderActivity extends RelianceBaseActivity implements OnItemSelectedListener{

    TableLayout orderTableLayout;
    Spinner productSpinner,batchSpinner;
    EditText quantityText,orderPriceText,orderDiscountText,freeItemText,
            invoiceValueText,overallDisPreText,batchNumber;
    TextView customerName,topSecondLabel,totalOutstanding,itemTotalAmount,
            totalAmountTextLabel,grossValueLabel,discountValueText,orderValue;
    ImageButton selectReturnBatch,addOrderButton;
    Button btnSaveOrder;
    int itemCount;
    Constants.Section section;
    ROSDbHelper dbHelper;
    ArrayList<String> products;
    ArrayList<String> batches;
    ROSStock stock;
    HashMap<String,ROSNewOrderItem> newOrderItemMap =  new HashMap<String,ROSNewOrderItem>();
    ROSCustomer selectedCustomer;

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
        batchSpinner.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && (section == Constants.Section.ADD_SALE_RETURNS)) v.setVisibility(View.INVISIBLE);


            }
        });
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

        overallDisPreText=(EditText)findViewById(R.id.overall_dis_pre);
        overallDisPreText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                updateTotalOrderValue();
            }
        });

        batchNumber=(EditText)findViewById(R.id.batch_number);
        batchNumber.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                    Log.v("onTextChanged",""+s)  ;
            }
        });

        discountValueText=(TextView)findViewById(R.id.order_discount_value);

        itemTotalAmount=(TextView)findViewById(R.id.item_total_amount);
        totalAmountTextLabel=(TextView)findViewById(R.id.order_value_label);
        orderValue=(TextView)findViewById(R.id.order_value);

        grossValueLabel=(TextView)findViewById(R.id.gross_value);
        addOrderButton=(ImageButton)findViewById(R.id.btnAddOrder);
        addOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewOrder();

            }

        });

        selectReturnBatch=(ImageButton)findViewById(R.id.select_batch_btn);
        selectReturnBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showProductBatch();

            }


        });

        btnSaveOrder=(Button)findViewById(R.id.btnSaveOrder);
        btnSaveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrder();

            }

        });
        dbHelper = new ROSDbHelper(getApplicationContext());

        loadData();

	}
    private void showProductBatch() {

        batchSpinner.setVisibility(View.VISIBLE);
        batchSpinner.performClick();
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


    private  void loadData(){
      selectedCustomer= AppController.getInstance().getRosCustomer();
        totalOutstanding.setText(String.format("%.2f", selectedCustomer.getOutstandingAmount()));

        customerName.setText(selectedCustomer.getCustName());
        customizeActionBar();

        if(section == Constants.Section.ADD_SALE_RETURNS){
            topSecondLabel.setText("Invoice ");
            totalAmountTextLabel.setText("Return Value ");
            totalOutstanding.setVisibility(View.GONE);
            invoiceValueText.setVisibility(View.VISIBLE);
            batchSpinner.setVisibility(View.INVISIBLE);

            loadProductForSale();

        }else{


            topSecondLabel.setText("Total outstanding : ");
            totalAmountTextLabel.setText("Order Value");
            totalOutstanding.setVisibility(View.VISIBLE);
            invoiceValueText.setVisibility(View.GONE);
            batchNumber.setVisibility(View.GONE);
            selectReturnBatch.setVisibility(View.GONE);

            loadProductForSale();

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
        if(section == Constants.Section.ADD_SALE_RETURNS) {
            batchSpinner.setVisibility(View.INVISIBLE);
            batchNumber.setText(batchName);
        }else{
            stock= dbHelper.getStockForSale(getApplicationContext(), productName, batchName);
            orderPriceText.setText(String.format("%.2f", stock.getUnitPrice()));
        }
            Log.v("productName :",productName);


    }

    private void addNewOrder(){
if(isFieldHasValidAmount()) {
    String productName = productSpinner.getSelectedItem().toString();
    String batchName = batchSpinner.getSelectedItem().toString();
    String quantity = quantityText.getText().toString();
    String orderPrice = orderPriceText.getText().toString();
    String orderDiscount = orderDiscountText.getText().toString();
    String freeItem = freeItemText.getText().toString();

    final String total = itemTotalAmount.getText().toString();
    if (productName != null && productName.length() > 0 && batchName != null && batchName.length() > 0 &&
            quantity != null && quantity.length() > 0 && orderPrice != null && orderPrice.length() > 0) {
        itemCount++;
        final int index = itemCount;
        float orderDiscountValue=0.0f;
        if (orderDiscount != null && orderDiscount.length() > 0){
            orderDiscountValue=  Float.valueOf(orderDiscount);
        }
        int freeItemCount=0;
        if (freeItem != null && freeItem.length() > 0){
            freeItemCount=  Integer.parseInt(freeItem);
        }
        ROSNewOrderItem newOrderItem = new ROSNewOrderItem();
        newOrderItem.setProductBatchCode(stock.getProductBatchCode());
        newOrderItem.setProductDescription(productName);
        newOrderItem.setQtyOrdered(Integer.parseInt(quantity));
        newOrderItem.setQtyBonus(freeItemCount);
        newOrderItem.setEffPrice(Float.valueOf(total));
        newOrderItem.setProductCode(stock.getProductCode());
        newOrderItem.setSuppCode(stock.getSuppCode());
        newOrderItem.setStockLocationCode(stock.getStockLocationCode());
        newOrderItem.setUnitPrice(Double.parseDouble(orderPrice));
        newOrderItem.setProdDiscount(orderDiscountValue);


        newOrderItemMap.put("" + itemCount, newOrderItem);
        updateOrderGrossValue();

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
        discTextView.setText(""+orderDiscountValue);
        discTextView.setGravity(Gravity.LEFT);
        discTextView.setLayoutParams(layoutParamsTextView2);
        discTextView.setTextColor(getResources().getColor(R.color.color_black));
        discTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView freeItemTextView = new TextView(this);
        freeItemTextView.setText(""+freeItemCount);
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
        orderPriceText.setText(String.format("%.2f", stock.getUnitPrice()));
        orderDiscountText.setText("");
        freeItemText.setText("");
        itemTotalAmount.setText("");
    }
}

    }
    private boolean isProductBatchAlreadyAdded(String batchName){
        boolean returnValue= false;

        Iterator iterator = newOrderItemMap.keySet().iterator();
        while(iterator.hasNext()) {
            String key=(String)iterator.next();
            ROSNewOrderItem rosNewOrderItem=(ROSNewOrderItem)newOrderItemMap.get(key);
            if(rosNewOrderItem.getProductCode()==stock.getProductCode() && rosNewOrderItem.getProductBatchCode()==batchName ){
                returnValue=true;
            }

        }
        return returnValue;
    }
    private boolean isFieldHasValidAmount(){
        boolean returnValue= true;
        String batchName= stock.getProductBatchCode();
        String quantity = quantityText.getText().toString();
        String orderDiscount = orderDiscountText.getText().toString();

        if(isProductBatchAlreadyAdded(batchName)){
            AppUtils.showAlertDialog(this, "Already added", "This product batch Already added try new batch");
            returnValue=false;
        }
        if(quantity != null && quantity.length()>0){
            int quantityValue=Integer.parseInt(quantity);
            if(stock.getAvailableQuantity() <quantityValue){
                AppUtils.showAlertDialog(this, "Over stock Quantity", "Can not add this much of quantity. You have only "+stock.getAvailableQuantity()+" quantity");
                returnValue=false;
            }
        }
        if(orderDiscount != null && orderDiscount.length()>0){
            double orderDiscountValue=Double.parseDouble(orderDiscount);
            if(orderDiscountValue> 100.0){
                AppUtils.showAlertDialog(this, "Over discount", "Can not give this amount of discount");
                returnValue=false;
            }
        }

        return returnValue;

    }
    private void updateItemTotalAmount(){
            String productName = stock.getProductCode();
            String batchName = stock.getProductBatchCode();
            String quantity = quantityText.getText().toString();
            String orderPrice = orderPriceText.getText().toString();
            String orderDiscount = orderDiscountText.getText().toString();
            double total = 0.00f;

            if (productName != null && productName.length() > 0 && batchName != null && batchName.length() > 0 &&
                    quantity != null && quantity.length() > 0 && orderPrice != null && orderPrice.length() > 0) {

                int quantityValue = Integer.parseInt(quantity);
                double orderPriceValue = Double.valueOf(orderPrice);
                total = orderPriceValue * quantityValue;

                if (orderDiscount != null && orderDiscount.length() > 0) {
                    double orderDiscountValue = Double.valueOf(orderDiscount);
                    total = (total * (1 - (orderDiscountValue / 100.0)));
                }

            }
            itemTotalAmount.setText(String.format("%.2f", total));



    }
    private void updateOrderGrossValue(){
        double total=0.00;
        Iterator iterator = newOrderItemMap.keySet().iterator();
        while(iterator.hasNext()) {
            String key=(String)iterator.next();
            ROSNewOrderItem rosNewOrderItem=(ROSNewOrderItem)newOrderItemMap.get(key);
            total=total+rosNewOrderItem.getEffPrice();

        }
        grossValueLabel.setText(String.format("%.2f", total));
        updateTotalOrderValue();


    }
    private void updateTotalOrderValue(){
        double total=0.00;
        double discountValue=0.00;
        String grossValueText = grossValueLabel.getText().toString();

        if(grossValueText != null && grossValueText.length()>0){
            total=Double.valueOf(grossValueText);
        }
        String overallDisPre = overallDisPreText.getText().toString();
        //discountValueText

        if(overallDisPre != null && overallDisPre.length()>0){

            double overallDisPreValue= Double.valueOf(overallDisPre);

            if(overallDisPreValue> 100.0){
                AppUtils.showAlertDialog(this, "Over discount", "Can not give this amount of discount");
                return;
            }
            discountValue=total*(overallDisPreValue/100.0);

        }
        discountValueText.setText(String.format("%.2f", discountValue));
        total=total-discountValue;
       // String discountValue = discountValueText.getText().toString();
//        if(discountValue != null && discountValue.length()>0){
//            total=total-Double.valueOf(discountValue);
//        }

        orderValue.setText(String.format("%.2f", total));
    }

    private void clearField(){
       //Set<String> keys= newOrderItemMap.keySet();
        Object[] keys =newOrderItemMap.keySet().toArray();
        for(int i=0;i<keys.length;i++) {
            removeOrder(Integer.parseInt((String)keys[i]));
        }
        stock=null;

        newOrderItemMap.clear();
        overallDisPreText.setText("0");

    }

    private void saveOrder(){
        String orderValueText = orderValue.getText().toString();
        String grossValueText = grossValueLabel.getText().toString();
        String discountPre = overallDisPreText.getText().toString();

        if(orderValueText != null && orderValueText.length()>0) {
            ArrayList<ROSNewOrderItem> newOrderItemArrayList = new ArrayList<ROSNewOrderItem>(newOrderItemMap.values());
            ROSNewOrder rosNewOrder = new ROSNewOrder();
            rosNewOrder.setProducts(newOrderItemArrayList);
            rosNewOrder.setOrderValue(Double.valueOf(orderValueText));
            rosNewOrder.setGrossValue(Double.valueOf(grossValueText));
            rosNewOrder.setDiscountValue(Double.valueOf(grossValueText) - Double.valueOf(orderValueText));
            rosNewOrder.setCustCode(selectedCustomer.getCustCode());

            if(discountPre != null && discountPre.length()>0)
            rosNewOrder.setOVDiscount(Double.valueOf(discountPre));
           final String orderIdStr= dbHelper.insertNewOrder(getApplicationContext(),rosNewOrder);

            if(orderIdStr !=null){
                double customerOutstanding=selectedCustomer.getOutstanding()+Double.valueOf(orderValueText);
                dbHelper.updateCustomerOutstanding(getApplicationContext(),selectedCustomer.getCustomerId(),customerOutstanding);
                totalOutstanding.setText(String.format("%.2f", customerOutstanding));

                if (ConnectionDetector.isConnected(this)) {
                    AppUtils.showProgressDialog(NewOrderActivity.this);
                    NewOrderServiceHandler newOrderServiceHandler = new NewOrderServiceHandler(getApplicationContext());
                    newOrderServiceHandler.syncNewOrder(rosNewOrder, "new_order_add", new NewOrderServiceHandler.NewOrderSyncListener() {
                        @Override
                        public void onOrderSyncSuccess(String orderId) {
                            Log.v("onOrderSyncSuccess", "orderId: " + orderId);
                            dbHelper.updateNewOrderStatusToSynced(getApplicationContext(),orderIdStr);
                            AppUtils.showAlertDialog(NewOrderActivity.this, "Order update Success", "Successfully updated order ");
                            AppUtils.dismissProgressDialog();
                        }

                        @Override
                        public void onOrderSyncError(String orderId, VolleyError error) {
                            Log.v("onOrderSyncError", "orderId: " + orderId);
                            AppUtils.showAlertDialog(NewOrderActivity.this, "Order update Error", "Order only stored in locally. You must Sync it later");
                            AppUtils.dismissProgressDialog();
                            AppUtils.broadcastAction(NewOrderActivity.this,Constants.LocalDataChange.ACTION_ORDER_ADDED);

                        }
                    });
                }else{
                    AppUtils.broadcastAction(NewOrderActivity.this,Constants.LocalDataChange.ACTION_ORDER_ADDED);
                    AppUtils.showAlertDialog(this, "No Network", "Order only stored in locally. You must Sync it later");

                }
                clearField();

            }else{
                AppUtils.showAlertDialog(this, "New Order Added Error!", "Try again later.");

            }

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
                    updateOrderGrossValue();
                    orderTableLayout.removeView(row);
                }

            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.product_spinner:

                loadProductBatchForSale(products.get(position));
                break;
            case R.id.batch_spinner:

                loadStockForSale();
                break;
            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        switch (parent.getId()) {
            case R.id.product_spinner:

                break;
            case R.id.batch_spinner:
                if(section == Constants.Section.ADD_SALE_RETURNS) {
                    batchSpinner.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                break;
        }
    }
}
