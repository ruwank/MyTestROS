package com.relianceit.relianceorder.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.relianceit.relianceorder.R;
import com.relianceit.relianceorder.db.ROSDbHelper;
import com.relianceit.relianceorder.models.ROSCustomer;

import java.util.ArrayList;

public class CustomerOutstandingFragment extends Fragment{
    TableLayout customerOutstandingTable;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_customer_outstanding, container, false);

        customerOutstandingTable=(TableLayout)rootView.findViewById(R.id.customer_outstanding_table);
        loadData();
		return rootView;
	}
    private void loadData(){
        ROSDbHelper dbHelper = new ROSDbHelper(getActivity().getApplicationContext());
        ArrayList<ROSCustomer> stockArrayList=dbHelper.getCustomers(getActivity().getApplicationContext());
        for (int i = 0; i <stockArrayList.size() ; i++) {
            ROSCustomer customer=stockArrayList.get(i);
            loadRow(customer,i);
        }
    }
    private void loadRow(ROSCustomer customer,int index){


        TableRow.LayoutParams layoutParamsTableRow = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsTableRow.topMargin=5;
        layoutParamsTableRow.bottomMargin=5;
        TableRow tableRow = new TableRow(getActivity());
        tableRow.setLayoutParams(layoutParamsTableRow);
        tableRow.setBackgroundResource(R.color.background_floating_material_dark);


        TableRow.LayoutParams layoutParamsTextView = new TableRow.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT,1.0f);

        layoutParamsTextView.gravity = Gravity.CENTER_VERTICAL;
        layoutParamsTextView.setMargins(1,1,1,1);
        layoutParamsTextView.weight=1.5f;
        layoutParamsTextView.width=0;

        TextView customerNameTextView = new TextView(getActivity());
        customerNameTextView.setText(customer.getCustName());
        customerNameTextView.setGravity(Gravity.LEFT);
        customerNameTextView.setLayoutParams(layoutParamsTextView);
        customerNameTextView.setWidth(0);
        customerNameTextView.setPadding(5,0,0,0);
        customerNameTextView.setBackgroundColor(getResources().getColor( R.color.app_bg_color));
        customerNameTextView.setTextColor(getResources().getColor(R.color.color_black));
        customerNameTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));


        TextView townNameTextView = new TextView(getActivity());
        townNameTextView.setText(customer.getTownName());
        townNameTextView.setGravity(Gravity.LEFT);
        townNameTextView.setLayoutParams(layoutParamsTextView);
        townNameTextView.setWidth(0);
        townNameTextView.setPadding(5,0,0,0);
        townNameTextView.setBackgroundColor(getResources().getColor( R.color.app_bg_color));
        townNameTextView.setTextColor(getResources().getColor(R.color.color_black));
        townNameTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TableRow.LayoutParams layoutParamsTextView1 = new TableRow.LayoutParams(0,ViewGroup.LayoutParams.MATCH_PARENT,1.0f);

        layoutParamsTextView1.gravity = Gravity.CENTER_VERTICAL;
        layoutParamsTextView1.setMargins(1,1,1,1);
        layoutParamsTextView1.weight=1f;
        layoutParamsTextView1.width=0;

        TextView outstandingAmountTextView = new TextView(getActivity());
        outstandingAmountTextView.setText(String.format("%.2f", customer.getOutstanding()));
        outstandingAmountTextView.setGravity(Gravity.RIGHT);
        outstandingAmountTextView.setWidth(0);
        outstandingAmountTextView.setPadding(0,0,5,0);

        outstandingAmountTextView.setLayoutParams(layoutParamsTextView1);
        outstandingAmountTextView.setBackgroundColor(getResources().getColor( R.color.app_bg_color));
        outstandingAmountTextView.setTextColor(getResources().getColor(R.color.color_black));
        outstandingAmountTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        tableRow.addView(customerNameTextView,0);
        tableRow.addView(townNameTextView,1);
        tableRow.addView(outstandingAmountTextView,2);

        customerOutstandingTable.addView(tableRow, index);

    }

}
