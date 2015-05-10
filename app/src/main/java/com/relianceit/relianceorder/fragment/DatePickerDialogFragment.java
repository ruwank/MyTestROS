package com.relianceit.relianceorder.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment{

    private OnDateSetListener mDateSetListener;
    DatePickerDialog mDatePicker;

    public DatePickerDialogFragment() {
        // nothing to see here, move along
    }
    @SuppressLint("ValidFragment")
    public DatePickerDialogFragment(OnDateSetListener callback) {
        mDateSetListener = (OnDateSetListener) callback;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();

        mDatePicker= new DatePickerDialog(getActivity(),
                mDateSetListener, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        return  mDatePicker;
    }
    public void updateDate(int year, int monthOfYear, int dayOfMonth) {
        if(mDatePicker != null) {
            mDatePicker.updateDate(year, monthOfYear, dayOfMonth);
        }
    }
}