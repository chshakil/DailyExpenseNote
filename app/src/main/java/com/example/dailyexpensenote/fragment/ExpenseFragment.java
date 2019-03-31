package com.example.dailyexpensenote.fragment;


import android.app.DatePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dailyexpensenote.adapter.ExpenseShowAdapter;
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
public class ExpenseFragment extends Fragment {

    String [] expType;
    Spinner spinner;
    long fromDateInput=0, toDateInput=0;
    String expTypeInput = "";
    private RecyclerView expenseShowRCV;
    private TextView fromeDateTV, toDateTV;
    private ImageView fromDateIV, toDateIV;

    private ArrayList<Expense> expenseArrayList;
    private ExpenseShowAdapter expenseShowAdapter;
    private DatabaseHelper databaseHelper;

    public ExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_expense, container, false);
        expType = getActivity().getResources().getStringArray(R.array.expense_type);
        spinner = v.findViewById(R.id.expenseSpinner);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getActivity(),R.layout.spiner_show_layout,R.id.expTypeTV,expType);
        spinner.setAdapter(stringArrayAdapter);

        fromeDateTV = v.findViewById(R.id.fromeDateTV);
        toDateTV = v.findViewById(R.id.toDateTV);
        fromDateIV = v.findViewById(R.id.fromDateIV);
        toDateIV = v.findViewById(R.id.toDateIV);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        });


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
        expenseArrayList = new ArrayList<>();
        expenseShowRCV = v.findViewById(R.id.expenseShowRCV);
        expenseShowRCV.setLayoutManager(new LinearLayoutManager(getActivity()));

        databaseHelper = new DatabaseHelper(getActivity());

        Cursor cursor = databaseHelper.showData();
        setAdapterData(cursor);

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

                //DATE INPUTE
                fromDateInput = fromTime;

                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MMM/YYYY");
                fromeDateTV.setText(sdf2.format(date));
                if(fromDateInput!=0 && toDateInput!=0)
                {
                    selectDataByDate(fromDateInput,toDateInput);
                }else if (fromDateInput!=0 && toDateInput==0){
                    selectDataByDate(fromDateInput,getCurrentDate());
                }

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

    private void selectDataByDate(long fromDate, long toDate) {


            Cursor  cursor  = databaseHelper.showData(fromDate,toDate);
            expenseArrayList.clear();
            setAdapterData(cursor);

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
                if(fromDateInput!=0 && toDateInput!=0)
                {
                    selectDataByDate(fromDateInput,toDateInput);
                }

            }
        };

        Calendar calendar = Calendar.getInstance();
        int yyyy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),onDateSetListener,yyyy,mm,dd);
        datePickerDialog.show();


    }

    private void setAdapterData(Cursor cursor){
        //expenseArrayList.removeAll(expenseArrayList);

        while (cursor.moveToNext()){

            String expType = cursor.getString(cursor.getColumnIndex(databaseHelper.COL_TYPE));
            double expAmount = cursor.getDouble(cursor.getColumnIndex(databaseHelper.COL_AMOUNT));
            long expDate = cursor.getLong(cursor.getColumnIndex(databaseHelper.COL_DATE));
            long expTime = cursor.getLong(cursor.getColumnIndex(databaseHelper.COL_TIME));
            String expImage = cursor.getString(cursor.getColumnIndex(databaseHelper.COL_IMG));
            int expId = cursor.getInt(cursor.getColumnIndex(databaseHelper.COL_ID));
            Expense expense = new Expense(expType,expDate,expTime,expAmount,expImage);
            expense.setExpenseId(expId);
            //
            expenseArrayList.add(expense);
            Toast.makeText(getActivity(), "size: "+expenseArrayList.size(), Toast.LENGTH_LONG).show();

        }


        expenseShowAdapter = new ExpenseShowAdapter(expenseArrayList,getActivity());
        expenseShowRCV.setAdapter(expenseShowAdapter);
        expenseShowAdapter.notifyDataSetChanged();

    }

    private  long getCurrentDate(){
        return  System.currentTimeMillis();
    }





}
