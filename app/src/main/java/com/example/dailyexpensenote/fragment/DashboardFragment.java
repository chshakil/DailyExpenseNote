package com.example.dailyexpensenote.fragment;


import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dailyexpensenote.database.DatabaseHelper;
import com.example.dailyexpensenote.model.Expense;
import com.example.dailyexpensenote.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    String [] expType;
    Spinner spinner;
    private ArrayList<Expense> expenseArrayList;
    private TextView fromDateTV,toDateTV,amountTV;
    private ImageView fromDateIV, toDateIV;

    private long fromDateInput = 0, toDateInput = 0;

    private DatabaseHelper databaseHelper;


    public DashboardFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        expType = getActivity().getResources().getStringArray(R.array.expense_type);
        spinner = v.findViewById(R.id.expenseSpinner);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getActivity(),R.layout.spiner_show_layout,R.id.expTypeTV,expType);
        spinner.setAdapter(stringArrayAdapter);

        fromDateIV=v.findViewById(R.id.fromDateIV);
        toDateIV = v.findViewById(R.id.toDateIV);
        fromDateTV = v.findViewById(R.id.fromeDateTV);
        toDateTV = v.findViewById(R.id.toDateTV);
        amountTV = v.findViewById(R.id.amountTV);

        fromDateIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });

        toDateIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker2();
            }
        });

        databaseHelper = new  DatabaseHelper(getActivity());
        fromDateInput = getCurrentMonthFirstDate();
        toDateInput = getCurrentDate();
        double amount=0;
        Cursor cursor = databaseHelper.showData(fromDateInput,toDateInput);
        while (cursor.moveToNext()){
            amount += cursor.getDouble(cursor.getColumnIndex(databaseHelper.COL_AMOUNT));
        }
        amountTV.setText(String.valueOf(amount));

   /*    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String expTypeInput = adapterView.getItemAtPosition(i).toString();
                Cursor cursor;
                if(fromDateInput==0 && toDateInput == 0 && !expTypeInput.equals("Enter Expense Type")){
                    //Toast.makeText(getActivity(), expTyep, Toast.LENGTH_SHORT).show();
                    cursor  = databaseHelper.showData(expTypeInput);
                    expenseArrayList.clear();
                    setAdapterData(cursor);
                }else if(fromDateInput!=0 && !expTypeInput.equals("Enter Expense Type")){
                    if(toDateInput == 0){
                        toDateInput = getCurrentDate();
                    }
                    cursor  = databaseHelper.showData(expTypeInput,fromDateInput,toDateInput);
                    expenseArrayList.clear();
                    setAdapterData(cursor);
                }else if(fromDateInput !=0 && expTypeInput.equals("Enter Expense Type")){
                    if(toDateInput == 0){
                        toDateInput = getCurrentDate();
                    }
                    cursor  = databaseHelper.showData(fromDateInput,toDateInput);
                    expenseArrayList.clear();
                    setAdapterData(cursor);
                }
                else{
                    cursor  = databaseHelper.showData();
                    expenseArrayList.clear();
                    setAdapterData(cursor);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        // Inflate the layout for this fragment
        return v;
    }

    private void openDatePicker() {

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                //dateET.setText(String.valueOf(dayOfMonth)+"/"+month+"/"+year);
                String dateFormat = year + "/" + month + "/" + dayOfMonth;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                Date date =  null;
                try {
                    date = sdf.parse(dateFormat);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long fromTime = date.getTime();
                // Log.d("Long Date", "onDateSet: "+fromTime);

                //DATE INPUT
                fromDateInput = fromTime;

                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MMM/YYYY");
                fromDateTV.setText(sdf2.format(date));

//                if(fromDateInput!=0 && toDateInput!=0)
//                {
//                    selectDataByDate(fromDateInput,toDateInput);
//                }else if (fromDateInput!=0 && toDateInput==0){
//                    selectDataByDate(fromDateInput,getCurrentDate());
//                }


            }
        };

        Calendar calendar = Calendar.getInstance();
        int yyyy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),onDateSetListener,yyyy,mm,dd);
        //datePickerDialog.getDatePicker().setMaxDate(getCurrentDate());
        datePickerDialog.show();


    }
    private void openDatePicker2() {

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                //dateET.setText(String.valueOf(dayOfMonth)+"/"+month+"/"+year);
                String dateFormat = year + "/" + month + "/" + dayOfMonth;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                Date date =  null;
                try {
                    date = sdf.parse(dateFormat);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long fromTime = date.getTime();
                // Log.d("Long Date", "onDateSet: "+fromTime);

                //DATE INPUTE
                toDateInput = fromTime;

                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MMM/YYYY");
                toDateTV.setText(sdf2.format(date));
//                if(fromDateInput!=0 && toDateInput!=0)
//                {
//                    selectDataByDate(fromDateInput,toDateInput);
//                }

            }
        };

        Calendar calendar = Calendar.getInstance();
        int yyyy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),onDateSetListener,yyyy,mm,dd);
        datePickerDialog.show();


    }
    private  long getCurrentMonthFirstDate(){
        Calendar calendar = Calendar.getInstance();
        int yyyy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);

        String dateFormat = yyyy + "/" + mm + "/" + 1;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date =  null;
        try {
            date = sdf.parse(dateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long fromTime = date.getTime();
        // Log.d("Long Date", "onDateSet: "+fromTime);

        //DATE INPUTE
        return fromTime;

    }
    private  long getCurrentDate(){
        return  System.currentTimeMillis();
    }

}
