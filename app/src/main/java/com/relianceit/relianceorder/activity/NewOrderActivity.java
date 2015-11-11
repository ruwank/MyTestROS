package com.relianceit.relianceorder.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.relianceit.relianceorder.AppController;
import com.relianceit.relianceorder.R;
import com.relianceit.relianceorder.db.ROSDbHelper;
import com.relianceit.relianceorder.models.ROSCustomer;
import com.relianceit.relianceorder.models.ROSInvoice;
import com.relianceit.relianceorder.models.ROSNewOrder;
import com.relianceit.relianceorder.models.ROSNewOrderItem;
import com.relianceit.relianceorder.models.ROSProduct;
import com.relianceit.relianceorder.models.ROSReturnOrder;
import com.relianceit.relianceorder.models.ROSReturnOrderItem;
import com.relianceit.relianceorder.models.ROSStock;
import com.relianceit.relianceorder.services.NewOrderServiceHandler;
import com.relianceit.relianceorder.services.ROSLocationService;
import com.relianceit.relianceorder.services.ReturnOrderServiceHandler;
import com.relianceit.relianceorder.util.AppUtils;
import com.relianceit.relianceorder.util.ConnectionDetector;
import com.relianceit.relianceorder.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    ArrayList<ROSStock> rosStockList;
    ArrayList<ROSProduct> rosProductList;
    ArrayList<ROSProduct> rosReturnProducts;
    ArrayList<String> batches= new ArrayList<String>();
    ROSStock stock;
    HashMap<String,ROSNewOrderItem> newOrderItemMap =  new HashMap<String,ROSNewOrderItem>();
    HashMap<String,ROSReturnOrderItem> returnOrderItemMap =  new HashMap<String,ROSReturnOrderItem>();

    ROSCustomer selectedCustomer;
    RelativeLayout relativeLayout;
    TableLayout new_order_table_header_content;
    boolean isLoadFromInvoice;
    ROSInvoice rosInvoice;
    ROSProduct rosProduct;
    private Location orderLocation = null;

    private AlertDialog locationAlertDialog = null;
    private final int locationReqCode = 200;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_new_order);
        relativeLayout = (RelativeLayout)findViewById(R.id.container);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenBatchSpinner();
            }
        });
        relativeLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hiddenBatchSpinner();

            }
        });
        Intent intent = getIntent();
        section = (Constants.Section) intent.getSerializableExtra("section");



        itemCount=0;
       // new_order_table
        new_order_table_header_content=(TableLayout)findViewById(R.id.new_order_table_header_content);
        new_order_table_header_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenBatchSpinner();
            }
        });


        customerName=(TextView)findViewById(R.id.customer_name);
        topSecondLabel=(TextView)findViewById(R.id.top_second_label);
        totalOutstanding=(TextView)findViewById(R.id.total_outstanding);
        invoiceValueText=(EditText)findViewById(R.id.invoice_value);

//        invoiceValueText.setOnKeyListener(new View.OnKeyListener() {
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                // If the event is a key-down event on the "enter" button
//                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
//                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    // Perform action on key press
//                    loadInvoiceData();
//
//                    return true;
//                }
//                return false;
//            }
//        });
        invoiceValueText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    loadInvoiceData();
                }
                return false;
            }
        });
        /*
        invoiceValueText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                loadInvoiceData();
            }

        });
*/
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

                loadProductForReturnsForBatch()  ;
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

        if(section != Constants.Section.ADD_SALE_RETURNS) {
            getLocation();
        }

        loadData();
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == locationReqCode) {
            getLocation();
        }
    }

    private void getLocation() {

        ROSLocationService locationService = new ROSLocationService();

        if (!locationService.isLocationEnabled(NewOrderActivity.this)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(NewOrderActivity.this);
            builder.setTitle("Please enable location access.");
            builder.setMessage("The location is required to add New Order.");
            builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    locationAlertDialog.dismiss();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, locationReqCode);
                }
            });
            locationAlertDialog = builder.create();
            locationAlertDialog.setCanceledOnTouchOutside(false);
            locationAlertDialog.setCancelable(false);
            locationAlertDialog.show();

        }else {
            updateLocation();
        }
    }

    private void updateLocation() {
        ROSLocationService locationService = new ROSLocationService();
        locationService.getCurrentLocation(this, new ROSLocationService.ROSLocationServiceListener() {
            @Override
            public void onLocationFound(Location location) {
                orderLocation = location;
            }

            @Override
            public void onLocationFailed() {

            }
        });
    }

    /*
    load initial data
     */
    private void loadData(){
        selectedCustomer= AppController.getInstance().getRosCustomer();
        totalOutstanding.setText(String.format("%.2f", selectedCustomer.getOutstandingAmount()));

        customerName.setText(selectedCustomer.getCustName());
        customizeActionBar();

        if(section == Constants.Section.ADD_SALE_RETURNS){
            isLoadFromInvoice=false;

            topSecondLabel.setText("Invoice ");
            totalAmountTextLabel.setText("Return Value ");
            totalOutstanding.setVisibility(View.GONE);
            invoiceValueText.setVisibility(View.VISIBLE);
            batchSpinner.setVisibility(View.INVISIBLE);

            loadAllProductNamesForReturns();

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
    private void hiddenBatchSpinner(){
        if (section == Constants.Section.ADD_SALE_RETURNS && !isLoadFromInvoice) {
            batchSpinner.setVisibility(View.INVISIBLE);
            batchNumber.setVisibility(View.VISIBLE);

        }
    }
    private void showProductBatch() {
        batchNumber.setVisibility(View.INVISIBLE);
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

    /*
        new  Order section
         */
    private void loadProductForSale(){
        ArrayList<ROSStock> productStock=dbHelper.getProductForSale(getApplicationContext());
        ArrayList<String> products=new ArrayList<String>(productStock.size());
        for (int i = 0; i <productStock.size() ; i++) {
            ROSStock stock1=productStock.get(i);
            String productName=stock1.getBrandName() +" - "+stock1.getProductDescription();
            products.add(productName);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, products);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpinner.setAdapter(dataAdapter);
    }
    private void loadProductBatchForSale(){
        String productSpinnerText= productSpinner.getSelectedItem().toString();
        String[] separated = productSpinnerText.split("-");
        if (separated.length >= 2) {
            String brandName = separated[0].trim();
            String productName = separated[1].trim();
            batches.clear();
            rosStockList= dbHelper.getBatchesForSale(getApplicationContext(), productName, brandName);
            for (int i = 0; i <rosStockList.size() ; i++) {
                ROSStock stock1=rosStockList.get(i);
                batches.add(stock1.getProductUserCode());
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, batches);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            batchSpinner.setAdapter(dataAdapter);
        }
    }
    private void loadStockForSale(int position){
       // String productSpinnerText= productSpinner.getSelectedItem().toString();
       // String[] separated = productSpinnerText.split("-");
        //String productName=separated[separated.length-1].trim();

       // String batchName= batchSpinner.getSelectedItem().toString();

       // stock= dbHelper.getStockForSale(getApplicationContext(), productName, batchName);
        stock=rosStockList.get(position);
        orderPriceText.setText(String.format("%.2f", stock.getUnitPrice()));
      //  Log.v("productName :",productName);

    }

    /*
    Return Order section
     */
    private void loadProductBatchForReturnOrder(int position){
        String productSpinnerText= productSpinner.getSelectedItem().toString();
        String[] separated = productSpinnerText.split("-");
        String productName=separated[separated.length-1].trim();

        Log.v("productName :",productName);
        if(rosReturnProducts != null){
            rosProduct=rosReturnProducts.get(position);
        }



        if(isLoadFromInvoice){
            rosProductList=rosInvoice.getBatchesForReturns(productName);

        }else{
            rosProductList= dbHelper.getBatchesForReturns(getApplicationContext(), productName);

        }
        batches.clear();
        for (int i = 0; i <rosProductList.size() ; i++) {
            ROSProduct rosProduct1=rosProductList.get(i);
            batches.add(rosProduct1.getProductUserCode());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, batches);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        batchSpinner.setAdapter(dataAdapter);

    }
    private void loadInvoiceProductNamesForReturns(){
        batches.clear();
        batchNumber.setVisibility(View.INVISIBLE);
        selectReturnBatch.setVisibility(View.INVISIBLE);
        batchSpinner.setVisibility(View.VISIBLE);
        isLoadFromInvoice=true;
        rosReturnProducts=rosInvoice.getProductsForReturns();
        ArrayList<String> products=new ArrayList<String>(rosReturnProducts.size());
        for (int i = 0; i <rosReturnProducts.size() ; i++) {
            ROSProduct rosProduct1=rosReturnProducts.get(i);
            String productName=rosProduct1.getBrandName() +" - "+rosProduct1.getProductDescription();
            products.add(productName);
        }


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, products);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpinner.setAdapter(dataAdapter);

//        if(products ==null || products.size()<1){
//            ArrayAdapter<String> batchDataAdapter = new ArrayAdapter<String>(this,
//                    android.R.layout.simple_spinner_item, products);
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            batchSpinner.setAdapter(batchDataAdapter);
//        }

    }
    private void loadAllProductNamesForReturns(){
        batches.clear();
        rosReturnProducts=dbHelper.getProductsForReturns(getApplicationContext());
        ArrayList<String> products=new ArrayList<String>(rosReturnProducts.size());
        for (int i = 0; i <rosReturnProducts.size() ; i++) {
            ROSProduct rosProduct1=rosReturnProducts.get(i);
            String productName=rosProduct1.getBrandName() +" - "+rosProduct1.getProductDescription();
            Log.v("productName ",productName);

            products.add(productName);
        }


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, products);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpinner.setAdapter(dataAdapter);
//        if(products ==null || products.size()<1){
//            ArrayAdapter<String> batchDataAdapter = new ArrayAdapter<String>(this,
//                    android.R.layout.simple_spinner_item, products);
//            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            batchSpinner.setAdapter(batchDataAdapter);
//        }
    }
    private void loadProductForReturns(int position){
        String productSpinnerText= productSpinner.getSelectedItem().toString();
        String[] separated = productSpinnerText.split("-");
        String productName=separated[separated.length-1].trim();
        String batchName= batchSpinner.getSelectedItem().toString();
        ROSProduct product=null;
        if(isLoadFromInvoice){
            //batches= rosInvoice.getBatchNames(productName);
            product=rosInvoice.getProduct(productName,batchName);

        }else{
            product=dbHelper.getProductForReturns(getApplicationContext(),productName,batchName);

        }
        if(product !=null){
            rosProduct=product;
        }

        if(isLoadFromInvoice) {
            batchSpinner.setVisibility(View.VISIBLE);
            batchNumber.setVisibility(View.INVISIBLE);

        }else{
            batchSpinner.setVisibility(View.INVISIBLE);
            batchNumber.setVisibility(View.VISIBLE);
            batchNumber.setText(batchName);

        }
        orderPriceText.setText(String.format("%.2f", rosProduct.getUnitPrice()));

    }
    private void loadProductForReturnsForBatch(){
        String productSpinnerText= productSpinner.getSelectedItem().toString();
        String[] separated = productSpinnerText.split("-");
        String productName=separated[separated.length-1].trim();
        String batchName= batchNumber.getText().toString();
        ROSProduct product=null;

        try {
            product=dbHelper.getProductForReturns(getApplicationContext(),productName,batchName);

        }catch (Exception e){

        }

        if(product !=null){
            rosProduct=product;
        }


        batchSpinner.setVisibility(View.INVISIBLE);
        batchNumber.setVisibility(View.VISIBLE);

        orderPriceText.setText(String.format("%.2f", rosProduct.getUnitPrice()));

    }
    private void loadInvoiceData(){
        isLoadFromInvoice=false;
        String  invoiceValue=  invoiceValueText.getText().toString();
        if(ConnectionDetector.isConnected(getApplicationContext()) && invoiceValue != null && invoiceValue.length()>0){
            AppUtils.showProgressDialog(this);

            ReturnOrderServiceHandler returnOrderServiceHandler=new ReturnOrderServiceHandler(getApplicationContext());
            returnOrderServiceHandler.getInvoice(selectedCustomer.getCustCode(),invoiceValue,"get_invoice",new ReturnOrderServiceHandler.InvoiceDetailsListener() {
                @Override
                public void onGetInvoiceSuccess(ROSInvoice invoice) {
                    AppUtils.dismissProgressDialog();

                    rosInvoice=invoice;
                    if(rosInvoice !=null){
                        ArrayList<ROSProduct> rosProducts= rosInvoice.getProductsForReturns();
                        if(rosProducts != null && rosProducts.size()>0){
                            loadInvoiceProductNamesForReturns();
                        }else{
                            AppUtils.showAlertDialog(NewOrderActivity.this, "No Product", "No product for this invoice");
                            loadAllProductNamesForReturns();
                        }
                    }else {
                        AppUtils.showAlertDialog(NewOrderActivity.this, "No Product", "No product for this invoice");
                        loadAllProductNamesForReturns();
                    }

                }

                @Override
                public void onGetInvoiceError(VolleyError error) {
                    AppUtils.dismissProgressDialog();

                    loadAllProductNamesForReturns();

                }
            });

        }else{
            isLoadFromInvoice=false;
        }
    }



    private void addNewOrder(){
if(isFieldHasValidAmount()) {
    String displayProductName = productSpinner.getSelectedItem().toString();
    String batchName = batchSpinner.getSelectedItem().toString();

    if (section == Constants.Section.ADD_SALE_RETURNS && !isLoadFromInvoice) {
        batchName=batchNumber.getText().toString();
    }
    String quantity = quantityText.getText().toString();
    String orderPrice = orderPriceText.getText().toString();
    String orderDiscount = orderDiscountText.getText().toString();
    String freeItem = freeItemText.getText().toString();
    String[] separated = displayProductName.split("-");
    String productName=separated[separated.length-1].trim();

//String displayProductName="";
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
       // ROSReturnOrderItem
        double unitPrice=0.0;

        if(section == Constants.Section.ADD_SALE_RETURNS){
            ROSReturnOrderItem returnOrderItem= new ROSReturnOrderItem();
            returnOrderItem.setProductBatchCode(rosProduct.getProductBatchCode());
            returnOrderItem.setProductDescription(productName);
            returnOrderItem.setQtyOrdered(Integer.parseInt(quantity));
            returnOrderItem.setQtyBonus(freeItemCount);
            returnOrderItem.setEffPrice(Float.valueOf(total));
            returnOrderItem.setProductCode(rosProduct.getProductCode());
            returnOrderItem.setSuppCode(rosProduct.getSuppCode());
            returnOrderItem.setUnitPrice(Double.parseDouble(orderPrice));
            returnOrderItem.setProdDiscount(orderDiscountValue);
            returnOrderItem.setProductUserCode(rosProduct.getProductUserCode());
            returnOrderItem.setBrandName(rosProduct.getBrandName());
            returnOrderItem.setBrandCode(rosProduct.getBrandCode());
            returnOrderItem.setAgenCode(rosProduct.getAgenCode());
            returnOrderItemMap.put("" + itemCount, returnOrderItem);
            unitPrice= rosProduct.getUnitPrice();
          //  displayProductName=rosProduct.getBrandName()+" - "+productName;

        }else {
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
            newOrderItem.setProductUserCode(stock.getProductUserCode());
            newOrderItem.setBrandName(stock.getBrandName());
            newOrderItem.setBrandCode(stock.getBrandCode());

            newOrderItemMap.put("" + itemCount, newOrderItem);
            unitPrice= stock.getUnitPrice();
           // displayProductName=stock.getBrandName()+" - "+productName;


        }

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
        productTextView.setText(displayProductName);
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
        orderPriceText.setText(String.format("%.2f", unitPrice));
        orderDiscountText.setText("");
        freeItemText.setText("");
        itemTotalAmount.setText("");
    }
}

    }

    /*
   Update field data
    */
    private boolean isProductBatchAlreadyAdded(String batchName){
        boolean returnValue= false;
        if(section == Constants.Section.ADD_SALE_RETURNS) {
            Iterator iterator = returnOrderItemMap.keySet().iterator();
            while(iterator.hasNext()) {
                String key=(String)iterator.next();
                ROSReturnOrderItem rosReturnOrderItem=(ROSReturnOrderItem)returnOrderItemMap.get(key);
                if(rosReturnOrderItem.getProductCode()==rosProduct.getProductCode() && rosReturnOrderItem.getProductBatchCode()==batchName ){
                    returnValue=true;
                }

            }
        }else{
            Iterator iterator = newOrderItemMap.keySet().iterator();
            while(iterator.hasNext()) {
                String key=(String)iterator.next();
                ROSNewOrderItem rosNewOrderItem=(ROSNewOrderItem)newOrderItemMap.get(key);
                if(rosNewOrderItem.getProductCode()==stock.getProductCode() && rosNewOrderItem.getProductBatchCode()==batchName ){
                    returnValue=true;
                }

            }
        }

        return returnValue;
    }
    private boolean isFieldHasValidAmount(){
        boolean returnValue= true;
        String batchName="";
       int availableQuantity= 0;

        if(section == Constants.Section.ADD_SALE_RETURNS){
            if(rosProduct != null) {
                batchName = rosProduct.getProductBatchCode();
                availableQuantity = rosProduct.getQuntityInStock();
            }

        }else{
            if(stock != null) {
                batchName = stock.getProductBatchCode();
                availableQuantity = stock.getAvailableQuantity();
            }else{
                return false;
            }

        }
        String quantity = quantityText.getText().toString();
        String orderDiscount = orderDiscountText.getText().toString();

        if(isProductBatchAlreadyAdded(batchName)){
            AppUtils.showAlertDialog(this, "Already added", "This product batch Already added try new batch");
            return false;
        }
        int freeItemCount=0;
        String freeItem = freeItemText.getText().toString();

        if (freeItem != null && freeItem.length() > 0){
            freeItemCount=  Integer.parseInt(freeItem);
        }
        int quantityValue=0;
        if(quantity != null && quantity.length()>0){
             quantityValue=Integer.parseInt(quantity);
        }
        if(quantityValue<1){
            return false;
        }

            if(section != Constants.Section.ADD_SALE_RETURNS && availableQuantity <(quantityValue+freeItemCount) ){
                AppUtils.showAlertDialog(this, "Over stock quantity", "Can not add this much of quantity. You have only "+availableQuantity+" quantity");
                return false;
            }
        if(orderDiscount != null && orderDiscount.length()>0){
            double orderDiscountValue=Double.parseDouble(orderDiscount);
            if(orderDiscountValue> 100.0){
                AppUtils.showAlertDialog(this, "Over discount", "Can not give this amount of discount");
                return false;
            }
        }

        return returnValue;

    }

    private void updateItemTotalAmount(){
        String productName="";
        String batchName="";
        if (section == Constants.Section.ADD_SALE_RETURNS){
            if(rosProduct !=null){
                productName = rosProduct.getProductCode();
                batchName = rosProduct.getProductBatchCode();
            }

        }else{
            if(stock !=null){
                productName = stock.getProductCode();
                batchName = stock.getProductBatchCode();
            }
        }

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
        if(section == Constants.Section.ADD_SALE_RETURNS) {
            Iterator iterator = returnOrderItemMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                ROSReturnOrderItem rosReturnOrderItem = (ROSReturnOrderItem) returnOrderItemMap.get(key);
                total = total + rosReturnOrderItem.getEffPrice();

            }
        }else {
            Iterator iterator = newOrderItemMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                ROSNewOrderItem rosNewOrderItem = (ROSNewOrderItem) newOrderItemMap.get(key);
                total = total + rosNewOrderItem.getEffPrice();

            }
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
        if(section == Constants.Section.ADD_SALE_RETURNS) {

            Object[] keys = returnOrderItemMap.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                removeOrder(Integer.parseInt((String) keys[i]));
            }
            //rosProduct=null;
            returnOrderItemMap.clear();
        }else {

            Object[] keys = newOrderItemMap.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                removeOrder(Integer.parseInt((String) keys[i]));
            }
            //stock=null;
            newOrderItemMap.clear();

        }
        orderValue.setText("");
        grossValueLabel.setText("");
        overallDisPreText.setText("");

    }

    /*
  save new order
   */
    private void saveOrder(){
        String orderValueText = orderValue.getText().toString();
        String grossValueText = grossValueLabel.getText().toString();
        String discountPre = overallDisPreText.getText().toString();

        if(section == Constants.Section.ADD_SALE_RETURNS) {
            if (orderValueText != null && orderValueText.length() > 0 && returnOrderItemMap.size() > 0) {
                ArrayList<ROSReturnOrderItem> returnOrderItemArrayList = new ArrayList<ROSReturnOrderItem>(returnOrderItemMap.values());
                ROSReturnOrder returnOrder = new ROSReturnOrder();
                returnOrder.setProducts(returnOrderItemArrayList);
                returnOrder.setOrderValue(Double.valueOf(orderValueText));
                returnOrder.setGrossValue(Double.valueOf(grossValueText));
                returnOrder.setDiscountValue(Double.valueOf(grossValueText) - Double.valueOf(orderValueText));
                returnOrder.setCustCode(selectedCustomer.getCustCode());

                if (discountPre != null && discountPre.length() > 0)
                    returnOrder.setOVDiscount(Double.valueOf(discountPre));
                final String orderIdStr = dbHelper.insertReturnOrder(getApplicationContext(), returnOrder);

                Log.v("returnOrder ", ""+returnOrder);

                if (orderIdStr != null) {

                    if (ConnectionDetector.isConnected(this)) {
                        AppUtils.showProgressDialog(NewOrderActivity.this);
                        ReturnOrderServiceHandler returnOrderServiceHandler = new ReturnOrderServiceHandler(getApplicationContext());
                        returnOrderServiceHandler.syncReturnOrder(returnOrder, "return_order_add", new ReturnOrderServiceHandler.ReturnOrderSyncListener() {
                            @Override
                            public void onOrderSyncSuccess(String orderId) {
                                dbHelper.updateReturnOrderStatusToSynced(getApplicationContext(), orderIdStr);
                                AppUtils.showAlertDialog(NewOrderActivity.this, "Return Order update Success", "Successfully updated return order ");
                                AppUtils.dismissProgressDialog();
                            }

                            @Override
                            public void onOrderSyncError(String orderId, VolleyError error) {
                                AppUtils.showAlertDialog(NewOrderActivity.this, "Return Order update Error", "Return Order only stored in locally. You must Sync it later");
                                AppUtils.dismissProgressDialog();
                                AppUtils.broadcastAction(NewOrderActivity.this, Constants.LocalDataChange.ACTION_ORDER_ADDED);
                            }
                        });

                    } else {
                        AppUtils.broadcastAction(NewOrderActivity.this, Constants.LocalDataChange.ACTION_ORDER_ADDED);
                        AppUtils.showAlertDialog(this, "No Network", "Return Order only stored in locally. You must Sync it later");
                    }
                    clearField();

                } else {
                    AppUtils.showAlertDialog(this, "Return Order Added Error!", "Try again later.");

                }

            }
        }else {
            if (orderValueText != null && orderValueText.length() > 0 && newOrderItemMap.size()>0) {
                ArrayList<ROSNewOrderItem> newOrderItemArrayList = new ArrayList<ROSNewOrderItem>(newOrderItemMap.values());
                ROSNewOrder rosNewOrder = new ROSNewOrder();
                rosNewOrder.setProducts(newOrderItemArrayList);
                rosNewOrder.setOrderValue(Double.valueOf(orderValueText));
                rosNewOrder.setGrossValue(Double.valueOf(grossValueText));
                rosNewOrder.setDiscountValue(Double.valueOf(grossValueText) - Double.valueOf(orderValueText));
                rosNewOrder.setCustCode(selectedCustomer.getCustCode());
                rosNewOrder.setAccountYear(stock.getAccountYear());
                rosNewOrder.setAccountMonth(stock.getAccountMonth());
                if (orderLocation != null) {
                    rosNewOrder.setLatitude(orderLocation.getLatitude());
                    rosNewOrder.setLongitude(orderLocation.getLongitude());
                }else {
                    rosNewOrder.setLatitude(0.0);
                    rosNewOrder.setLongitude(0.0);
                }
                Date now = new Date();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                rosNewOrder.setAddedDate(df.format(now));

                if (discountPre != null && discountPre.length() > 0)
                    rosNewOrder.setOVDiscount(Double.valueOf(discountPre));
                final String orderIdStr = dbHelper.insertNewOrder(getApplicationContext(), rosNewOrder);

                if (orderIdStr != null) {

                    if (ConnectionDetector.isConnected(this)) {
                        AppUtils.showProgressDialog(NewOrderActivity.this);
                        NewOrderServiceHandler newOrderServiceHandler = new NewOrderServiceHandler(getApplicationContext());
                        newOrderServiceHandler.syncNewOrder(rosNewOrder, "new_order_add", new NewOrderServiceHandler.NewOrderSyncListener() {
                            @Override
                            public void onOrderSyncSuccess(String orderId) {
                                Log.v("onOrderSyncSuccess", "orderId: " + orderId);
                                dbHelper.updateNewOrderStatusToSynced(getApplicationContext(), orderIdStr);
                                AppUtils.showAlertDialog(NewOrderActivity.this, "Order update Success", "Successfully updated order ");
                                AppUtils.dismissProgressDialog();
                            }

                            @Override
                            public void onOrderSyncError(String orderId, VolleyError error) {
                                Log.v("onOrderSyncError", "orderId: " + orderId);
                                AppUtils.showAlertDialog(NewOrderActivity.this, "Order update Error", "Order only stored in locally. You must Sync it later");
                                AppUtils.dismissProgressDialog();
                                AppUtils.broadcastAction(NewOrderActivity.this, Constants.LocalDataChange.ACTION_ORDER_ADDED);

                            }
                        });
                    } else {
                        AppUtils.broadcastAction(NewOrderActivity.this, Constants.LocalDataChange.ACTION_ORDER_ADDED);
                        AppUtils.showAlertDialog(this, "No Network", "Order only stored in locally. You must Sync it later");

                    }
                    clearField();

                } else {
                    AppUtils.showAlertDialog(this, "New Order Added Error!", "Try again later.");

                }
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
                    if(section == Constants.Section.ADD_SALE_RETURNS) {
                        returnOrderItemMap.remove(""+row.getId());

                    }else{
                        newOrderItemMap.remove(""+row.getId());

                    }
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

                if(section == Constants.Section.ADD_SALE_RETURNS){
                    loadProductBatchForReturnOrder(position);

                }else{
                    loadProductBatchForSale();

                }
                break;
            case R.id.batch_spinner:

                if(section == Constants.Section.ADD_SALE_RETURNS){
                    loadProductForReturns(position);
                }else{
                    loadStockForSale(position);
                }
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
