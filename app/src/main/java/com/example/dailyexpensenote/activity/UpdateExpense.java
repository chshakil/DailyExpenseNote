package com.example.dailyexpensenote.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
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

public class UpdateExpense extends AppCompatActivity {
    String [] expType;
    Spinner spinner;

    private EditText expAmountET;
    private TextView dateET, timeET;
    private ImageView dateIV, timeIV,expImageIV;
    private Button galleryBTN, cameraBTN, saveBTN;

    private DatabaseHelper databaseHelper;

    private int expId;
    private String expTyepInput,expImageInput="";
    private long expDateInput=0, expTimeInput=0;
    private double expAmountInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_expense);
        expType = this.getResources().getStringArray(R.array.expense_type);
        spinner = findViewById(R.id.expenseSpinner);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this,R.layout.spiner_show_layout,R.id.expTypeTV,expType);
        spinner.setAdapter(stringArrayAdapter);

        expAmountET = findViewById(R.id.expAmountET);
        dateET = findViewById(R.id.dateShowET);
        timeET = findViewById(R.id.timeShowET);
        galleryBTN = findViewById(R.id.gallaryImageBTN);
        cameraBTN = findViewById(R.id.cameraImageBTN);
        saveBTN = findViewById(R.id.expeseSaveBTN);
        expImageIV = findViewById(R.id.expenseImageIV);
        dateIV = findViewById(R.id.datePickerIV);
        timeIV = findViewById(R.id.timePcikerIV);


        databaseHelper = new DatabaseHelper(this);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("Add Expense");



        Intent intent = getIntent();
        expId = intent.getIntExtra("expId",-1);
        expTyepInput = intent.getStringExtra("expType");
        expDateInput = intent.getLongExtra("expDate",-1);
        expTimeInput = intent.getLongExtra("expTime",-1);
        expImageInput = intent.getStringExtra("expImage");
        expAmountInput = intent.getDoubleExtra("expAmount",0);

        Toast.makeText(this, "Id "+expId, Toast.LENGTH_SHORT).show();

        SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM, YYYY");
        SimpleDateFormat timeSDF = new SimpleDateFormat("hh:mm aa");

        selectValue(spinner,expTyepInput);
        expAmountET.setText(String.valueOf(expAmountInput));
        dateET.setText(sdf2.format(expDateInput));
        timeET.setText(timeSDF.format(expTimeInput));
        if(!expImageInput.equals("")) {
            Bitmap bitmap = decodeBase64(expImageInput);
            expImageIV.setImageBitmap(bitmap);
        }

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
                    Toast.makeText(UpdateExpense.this, "Enter Expense Tyep", Toast.LENGTH_SHORT).show();
                }else if(expAmountET.getText().toString().equals("")){
                    Toast.makeText(UpdateExpense.this, "Enter Expense Amount", Toast.LENGTH_SHORT).show();
                } else if(expDateInput==0){
                    Toast.makeText(UpdateExpense.this, "Enter Expense Date", Toast.LENGTH_SHORT).show();

                }else if(expTimeInput==0){
                    Toast.makeText(UpdateExpense.this, "Enter Expense Time", Toast.LENGTH_SHORT).show();

                }else {
                    expAmountInput = Double.valueOf(expAmountET.getText().toString());
                    //Toast.makeText(getActivity(), "Expense :"+expTyepInput+"Amount: "+expAmountInput+" Date: "+expDateInput+" Time: "+expTimeInput+"Image "+expImageInput, Toast.LENGTH_LONG).show();
                    Toast.makeText(UpdateExpense.this, "Id "+expId+" Type: "+expTyepInput, Toast.LENGTH_LONG).show();
                    int id = databaseHelper.updateData(expId,expTyepInput,expAmountInput,expDateInput,expTimeInput,expImageInput);
                    if(id > -1) {
                        Intent intent1 = new Intent(UpdateExpense.this,MainActivity.class);
                        intent1.putExtra("Update",1);
                        startActivity(intent1);
                        //Toast.makeText(UpdateExpense.this, "Data Upade Successfully " + id, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(UpdateExpense.this, "Data Update Failed "+id, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void selectValue(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
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

            //image input
            expImageInput = encodeToBase64(bitmap,Bitmap.CompressFormat.JPEG,80);

            expImageIV.setImageBitmap(bitmap);
        }
        if (requestCode ==2){

            Uri uri= data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_timepicker_layout,null);

        builder.setView(view);
        final Dialog dialog = builder.create();
        dialog.show();

        final Button okBTN = view.findViewById(R.id.okBtn);
        final TimePicker timePicker = view.findViewById(R.id.timeTP);

        okBTN.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                SimpleDateFormat timeSDF = new SimpleDateFormat("hh:mm aa");
                int hh = timePicker.getHour();
                int mm = timePicker.getMinute();
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


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,onDateSetListener,yyyy,mm,dd);
        datePickerDialog.show();


    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
