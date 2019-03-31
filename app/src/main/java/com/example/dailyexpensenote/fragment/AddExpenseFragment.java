package com.example.dailyexpensenote.fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dailyexpensenote.R;
import com.example.dailyexpensenote.database.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddExpenseFragment extends Fragment {

    String [] expType;
    Spinner spinner;
    private EditText expAmountET;
    private TextView dateET, timeET;
    private ImageView dateIV, timeIV,expImageIV;
    private Button galleryBTN, cameraBTN, saveBTN;

    private DatabaseHelper databaseHelper;

    private String expTyepInput,expImageInput="";
    private long expDateInput=0, expTimeInput=0;
    private double expAmountInput;



    public AddExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_add_expense, container, false);
        expType = getActivity().getResources().getStringArray(R.array.expense_type);
        spinner = v.findViewById(R.id.expenseSpinner);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(getActivity(),R.layout.spiner_show_layout,R.id.expTypeTV,expType);
        spinner.setAdapter(stringArrayAdapter);
        databaseHelper = new DatabaseHelper(getActivity());

        dateET = v.findViewById(R.id.dateShowET);
        timeET = v.findViewById(R.id.timeShowET);

        dateIV = v.findViewById(R.id.datePickerIV);
        timeIV = v.findViewById(R.id.timePcikerIV);
        expAmountET = v.findViewById(R.id.expAmountET);
        expImageIV = v.findViewById(R.id.expenseImageIV);
        galleryBTN = v.findViewById(R.id.gallaryImageBTN);
        cameraBTN = v.findViewById(R.id.cameraImageBTN);
        saveBTN = v.findViewById(R.id.expeseSaveBTN);

        dateIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePicker();
            }
        });
        
        timeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePicker();
            }
        });

        galleryBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        
        cameraBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expTyepInput = spinner.getSelectedItem().toString();


                if(expTyepInput.equals("Enter Expense Type")){
                    Toast.makeText(getActivity(), "Enter Expense Type", Toast.LENGTH_SHORT).show();
                }else if(expAmountET.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "Enter Expense Amount", Toast.LENGTH_SHORT).show();
                } else if(expDateInput==0){
                    Toast.makeText(getActivity(), "Enter Expense Date", Toast.LENGTH_SHORT).show();

                }else if(expTimeInput==0){
                    Toast.makeText(getActivity(), "Enter Expense Time", Toast.LENGTH_SHORT).show();

                }else {
                    expAmountInput = Double.valueOf(expAmountET.getText().toString());
                    //Toast.makeText(getActivity(), "Expense :"+expTyepInput+"Amount: "+expAmountInput+" Date: "+expDateInput+" Time: "+expTimeInput+"Image "+expImageInput, Toast.LENGTH_LONG).show();
                    long id = databaseHelper.insertData(expTyepInput,expAmountInput,expDateInput,expTimeInput,expImageInput);
                    if(id > -1)
                        Toast.makeText(getActivity(), "Data Saved Successfully "+id, Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getActivity(), "Data Saved Failed "+id, Toast.LENGTH_SHORT).show();

                }
            }
        });

        return v;
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,1);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1 && resultCode == RESULT_OK){

            Bitmap bitmap =  (Bitmap) data.getExtras().get("data");
            // String image = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG,80);

            //IMAGE INPUTE
            expImageInput = encodeToBase64(bitmap,Bitmap.CompressFormat.JPEG,80);

            expImageIV.setImageBitmap(bitmap);
        }
        if (requestCode ==2){

            Uri uri= data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
                // String image = encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG,80);

                //IMAGE INPUTE
                expImageInput = encodeToBase64(bitmap,Bitmap.CompressFormat.JPEG,80);

                expImageIV.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // imageView.setImageURI(uri);
        }
    }

    private void openTimePicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getLayoutInflater().inflate(R.layout.dialog_timepicker_layout,null);

        builder.setView(view);
        final Dialog dialog = builder.create();
        dialog.show();

        final Button okBTN = view.findViewById(R.id.okBtn);
        final TimePicker timePicker = view.findViewById(R.id.timeTP);

        okBTN.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                SimpleDateFormat timeSDF = new SimpleDateFormat("hh:mm aa");
                int hh = timePicker.getCurrentHour();
                int mm = timePicker.getCurrentMinute();
                Time time = new Time(hh,mm,0);
                long fromTime = time.getTime();
                //TIME INPUTE
                expTimeInput = fromTime;
                timeET.setText(timeSDF.format(time));
                dialog.dismiss();
                //Toast.makeText(getActivity(), "long"+fromTime, Toast.LENGTH_SHORT).show();

            }
        });

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
                expDateInput = fromTime;

                SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MMM/YYYY");
                dateET.setText(sdf2.format(date));

            }
        };

        Calendar calendar = Calendar.getInstance();
        int yyyy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),onDateSetListener,yyyy,mm,dd);
        datePickerDialog.show();


    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

}
