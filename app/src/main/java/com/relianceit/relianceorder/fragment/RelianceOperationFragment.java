package com.relianceit.relianceorder.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.relianceit.relianceorder.AppController;
import com.relianceit.relianceorder.R;
import com.relianceit.relianceorder.activity.ListOfOrderActivity;
import com.relianceit.relianceorder.activity.NewOrderActivity;
import com.relianceit.relianceorder.adapter.CustomerListAdapter;
import com.relianceit.relianceorder.db.ROSDbHelper;
import com.relianceit.relianceorder.models.ROSCustomer;
import com.relianceit.relianceorder.util.Constants;

import java.util.ArrayList;

public class RelianceOperationFragment extends Fragment{
	ListView customerListView;
	Button newOrderBtn,orderListBtn,saleReturnBtn,returnListBtn;
    ArrayList<ROSCustomer> customers;
    int selectedCustomerIndex;
    TextView customerName;
    ROSCustomer selectedCustomer;
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		RelativeLayout rootView = (RelativeLayout) inflater.inflate(
				R.layout.fragment_reliance_operation, container, false);

        customerListView=(ListView)rootView.findViewById(R.id.customerListView);
        customerName=(TextView)rootView.findViewById(R.id.customer_name);

		newOrderBtn=(Button)rootView.findViewById(R.id.newOrderBtn);
		newOrderBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
                loadAddOrderScreen();

			}
 
		});

        orderListBtn=(Button)rootView.findViewById(R.id.orderListBtn);
        orderListBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loadViewOrderListScreen();

            }

        });

        saleReturnBtn=(Button)rootView.findViewById(R.id.saleReturnBtn);
        saleReturnBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loadAddReturnScreen();

            }

        });
        returnListBtn=(Button)rootView.findViewById(R.id.returnListBtn);
        returnListBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loadViewReturnScreen();

            }

        });
        getActivity().registerReceiver(localDataChangeReceiver, new IntentFilter(Constants.LocalDataChange.ACTION_DAILY_SYNCED));

        loadCustomerList();
		return rootView;
	}
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(localDataChangeReceiver);
    }
    private void loadCustomerList(){
        selectedCustomerIndex=0;
        ROSDbHelper dbHelper = new ROSDbHelper(getActivity().getApplicationContext());
        customers = dbHelper.getCustomers(getActivity().getApplicationContext());
        if(customers !=null && customers.size()>0) {
            newOrderBtn.setVisibility(View.VISIBLE);
            orderListBtn.setVisibility(View.VISIBLE);
            saleReturnBtn.setVisibility(View.VISIBLE);
            returnListBtn.setVisibility(View.VISIBLE);

            CustomerListAdapter customerListAdapter = new CustomerListAdapter(getActivity().getApplicationContext(), customers);
            customerListView.setAdapter(customerListAdapter);
            customerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedCustomerIndex = position;
                    updateCustomerData();
                }
            });

            updateCustomerData();
        }else{
            newOrderBtn.setVisibility(View.INVISIBLE);
            orderListBtn.setVisibility(View.INVISIBLE);
            saleReturnBtn.setVisibility(View.INVISIBLE);
            returnListBtn.setVisibility(View.INVISIBLE);
        }
    }
    private void updateCustomerData(){
        if(customers != null && customers.size()>selectedCustomerIndex){
            selectedCustomer=customers.get(selectedCustomerIndex);
            AppController.getInstance().setRosCustomer(selectedCustomer);
            customerName.setText(selectedCustomer.getCustName());
        }
    }
	private void loadAddOrderScreen() {
		Intent intent = new Intent(getActivity().getApplicationContext(),
				NewOrderActivity.class);
        intent.putExtra("section", Constants.Section.ADD_NEW_ORDER);
		startActivity(intent);
	}
    private void loadViewOrderListScreen() {
        Intent intent = new Intent(getActivity().getApplicationContext(),
                ListOfOrderActivity.class);
        intent.putExtra("section", Constants.Section.VIEW_ORDER_LIST);
        startActivity(intent);
    }
    private void loadAddReturnScreen() {
        Intent intent = new Intent(getActivity().getApplicationContext(),
                NewOrderActivity.class);
        intent.putExtra("section", Constants.Section.ADD_SALE_RETURNS);
        startActivity(intent);
    }
    private void loadViewReturnScreen() {
        Intent intent = new Intent(getActivity().getApplicationContext(),
                ListOfOrderActivity.class);
        intent.putExtra("section", Constants.Section.VIEW_SALE_RETURNS_LIST);
        startActivity(intent);
    }
    private BroadcastReceiver localDataChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                loadCustomerList();
        }
    };
}
