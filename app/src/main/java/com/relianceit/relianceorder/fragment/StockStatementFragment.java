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
import com.relianceit.relianceorder.models.ROSStock;

import java.util.ArrayList;

public class StockStatementFragment extends Fragment{
    //TableLayout stock_statement_table;
    TableLayout stockStatementTable;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_stock_statement, container, false);

        stockStatementTable=(TableLayout)rootView.findViewById(R.id.stock_statement_table);

		return rootView;
	}

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData(){
    ROSDbHelper dbHelper = new ROSDbHelper(getActivity().getApplicationContext());
    ArrayList<ROSStock> stockArrayList=dbHelper.getStocks(getActivity().getApplicationContext());
    for (int i = 0; i <stockArrayList.size() ; i++) {
        ROSStock stock=stockArrayList.get(i);
        loadStockRow(stock,i);
    }
}
    private void loadStockRow(ROSStock stock,int index){


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



        TextView agencyNameTextView = new TextView(getActivity());
        agencyNameTextView.setText(stock.getAgenName());
        agencyNameTextView.setGravity(Gravity.CENTER);
        agencyNameTextView.setLayoutParams(layoutParamsTextView);
        agencyNameTextView.setBackgroundColor(getResources().getColor( R.color.app_bg_color));
        agencyNameTextView.setTextColor(getResources().getColor(R.color.color_black));
        agencyNameTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        layoutParamsTextView.weight=1f;

        TextView brandNameTextView = new TextView(getActivity());
        brandNameTextView.setText(stock.getBrandName());
        brandNameTextView.setGravity(Gravity.CENTER);
        brandNameTextView.setLayoutParams(layoutParamsTextView);
        brandNameTextView.setBackgroundColor(getResources().getColor( R.color.app_bg_color));
        brandNameTextView.setTextColor(getResources().getColor(R.color.color_black));
        brandNameTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView productNameTextView = new TextView(getActivity());
        productNameTextView.setText(stock.getProductDescription());
        productNameTextView.setGravity(Gravity.CENTER);
        productNameTextView.setLayoutParams(layoutParamsTextView);
        productNameTextView.setBackgroundColor(getResources().getColor( R.color.app_bg_color));
        productNameTextView.setTextColor(getResources().getColor(R.color.color_black));
        productNameTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView batchNoTextView = new TextView(getActivity());
        batchNoTextView.setText(stock.getProductBatchCode());
        batchNoTextView.setGravity(Gravity.CENTER);
        batchNoTextView.setLayoutParams(layoutParamsTextView);
        batchNoTextView.setBackgroundColor(getResources().getColor( R.color.app_bg_color));
        batchNoTextView.setTextColor(getResources().getColor(R.color.color_black));
        batchNoTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));

        TextView stockAmountTextView = new TextView(getActivity());
        stockAmountTextView.setText(stock.getAvailableQuantity()+" / "+stock.getQuntityInStock());
        stockAmountTextView.setGravity(Gravity.CENTER);
        stockAmountTextView.setLayoutParams(layoutParamsTextView);
        stockAmountTextView.setBackgroundColor(getResources().getColor( R.color.app_bg_color));
        stockAmountTextView.setTextColor(getResources().getColor(R.color.color_black));
        stockAmountTextView.setTextSize(getResources().getDimension(R.dimen.common_text_size));


        tableRow.addView(agencyNameTextView,0);
        tableRow.addView(brandNameTextView,1);
        tableRow.addView(productNameTextView,2);
        tableRow.addView(batchNoTextView,3);
        tableRow.addView(stockAmountTextView,4);

        stockStatementTable.addView(tableRow, index);

    }
}
