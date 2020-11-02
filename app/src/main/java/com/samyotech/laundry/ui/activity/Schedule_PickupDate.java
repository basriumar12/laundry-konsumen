package com.samyotech.laundry.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.material.snackbar.Snackbar;
import com.samyotech.laundry.MainActivity;
import com.samyotech.laundry.R;
import com.samyotech.laundry.databinding.ActivitySchedulePickupAddressBinding;
import com.samyotech.laundry.interfaces.Consts;
import com.samyotech.laundry.utils.ProjectUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Schedule_PickupDate extends AppCompatActivity implements View.OnClickListener {
    ActivitySchedulePickupAddressBinding binding;
    Context mContext;
    final Calendar myCalendar = Calendar.getInstance();
    final Calendar myCalendarAdop = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();
    private String TAG = Schedule_PickupDate.class.getSimpleName();
    int t = 123;
    TimePickerDialog timePickerDialog;
    DatePickerDialog datePickerDialogADp, datePickerDialogEnd;
    HashMap<String, String> hashMap ;
    boolean checkCLick=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_schedule__pickup_address);
        mContext = Schedule_PickupDate.this;

        hashMap = (HashMap<String, String>) getIntent().getSerializableExtra("map");
        binding.ctvPickupDate.setOnClickListener(this);
        binding.ctvPickupTime.setOnClickListener(this);
        binding.ctvDeliveryDate.setOnClickListener(this);
        binding.ctvDeliveryTime.setOnClickListener(this);
        binding.rlAdddate.setOnClickListener(this);
        binding.back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.ctvPickupDate:
                openDatePickerStart();
                break;
            case R.id.ctvPickupTime:
                t = 1;
                addtime();
                break;
            case R.id.ctvDeliveryDate:
                openDatePickerEnd();
                break;
            case R.id.ctvDeliveryTime:
                t = 2;
                addtime();
                break;
            case R.id.rlAdddate:

                if(!ProjectUtils.isEditTextFilled(binding.ctvPickupDate)){

                    showSickbar(getResources().getString(R.string.val_pdate));

                }else if(!ProjectUtils.isEditTextFilled(binding.ctvPickupTime)){
                    showSickbar(getResources().getString(R.string.val_ptime));

                }else if(!ProjectUtils.isEditTextFilled(binding.ctvDeliveryDate)){
                    showSickbar(getResources().getString(R.string.val_ddate));

                }else if(!ProjectUtils.isEditTextFilled(binding.ctvDeliveryTime)){
                    showSickbar(getResources().getString(R.string.val_dtime));

                }else {
                    if(checkCLick){
                    Intent in = new Intent(mContext, PaymentActivity.class);
                    in.putExtra("map", getParams());
                    startActivity(in);
                    checkCLick=false;
                }}
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkCLick=true;
    }

    private void addtime() {



        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

         timePickerDialog = new TimePickerDialog(Schedule_PickupDate.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker mTimePicker, int selectedHour, int selectedMinute) {
                String AM_PM ;
                if(selectedHour < 12) {

                    AM_PM = "AM";

                    if(selectedHour==0){
                        if (t == 1) {
                            binding.ctvPickupTime.setText(String.format("%02d", 12) + ":" + String.format("%02d", selectedMinute)+" "+AM_PM);
                        } else {
                            binding.ctvDeliveryTime.setText(String.format("%02d", 12) + ":" + String.format("%02d", selectedMinute) + " " + AM_PM);
                        } }else {
                    if (t == 1) {
                        binding.ctvPickupTime.setText(String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute)+" "+AM_PM);
                    } else {
                        binding.ctvDeliveryTime.setText(String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute)+" "+AM_PM);
                    }}
                } else {
                    AM_PM = "PM";

                    if(selectedHour==12){
                        if (t == 1) {
                            binding.ctvPickupTime.setText(String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute)+" "+AM_PM);
                        } else {
                            binding.ctvDeliveryTime.setText(String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute)+" "+AM_PM);
                        }
                    }else {   if (t == 1) {
                        binding.ctvPickupTime.setText(String.format("%02d", selectedHour-12) + ":" + String.format("%02d", selectedMinute)+" "+AM_PM);
                    } else {
                        binding.ctvDeliveryTime.setText(String.format("%02d", selectedHour-12) + ":" + String.format("%02d", selectedMinute)+" "+AM_PM);
                    }}


                }

            }
        }, hour, minute, true);
        timePickerDialog.setTitle("Select Time\n");
        timePickerDialog.show();
    }




    public void openDatePickerStart() {

        int year = myCalendar.get(Calendar.YEAR);
        int monthOfYear = myCalendar.get(Calendar.MONTH);
        int dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH);


        datePickerDialogADp = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                myCalendarAdop.set(Calendar.YEAR, y);
                myCalendarAdop.set(Calendar.MONTH, m);
                myCalendarAdop.set(Calendar.DAY_OF_MONTH, d);
                String myFormat = "dd-MMM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                binding.ctvPickupDate.setText(sdf.format(myCalendarAdop.getTime()));
                binding.ctvDeliveryDate.setText("");

                SimpleDateFormat crunchifyFormat = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
                String currentTime = crunchifyFormat.format(myCalendarAdop.getTime());
                try {
                    Date date = crunchifyFormat.parse(currentTime);

// getTime() returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object.
                /*    epochTimeS= date.getTime();

                    Log.e(TAG, "startDate:dialog time------------------"+epochTimeS);*/
                } catch (Exception e) {

                }


            }


        }, year, monthOfYear, dayOfMonth);
        datePickerDialogADp.setTitle("Select Date");

        datePickerDialogADp.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialogADp.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis() + (1000 * 60 * 60 * 24 * 15));
        datePickerDialogADp.show();
    }

    public void openDatePickerEnd() {

        int year = myCalendar.get(Calendar.YEAR);
        int monthOfYear = myCalendar.get(Calendar.MONTH);
        int dayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH);


        datePickerDialogEnd = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                myCalendarEnd.set(Calendar.YEAR, y);
                myCalendarEnd.set(Calendar.MONTH, m);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, d);
                String myFormat = "dd-MMM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                binding.ctvDeliveryDate.setText(sdf.format(myCalendarEnd.getTime()));

                SimpleDateFormat crunchifyFormat = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
                String currentTime = crunchifyFormat.format(myCalendarEnd.getTime());
                try {
                    Date date = crunchifyFormat.parse(currentTime);

// getTime() returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object.
                /*    epochTimeE= date.getTime();

                    Log.e(TAG, "enddialogtDate: time------------------"+epochTimeE);*/
                } catch (Exception e) {

                }


            }


        }, year, monthOfYear, dayOfMonth);
        datePickerDialogEnd.setTitle("Select Date");
        datePickerDialogEnd.getDatePicker().setMinDate(myCalendarAdop.getTimeInMillis() + (1000 * 60 * 60 * 24));
        datePickerDialogEnd.getDatePicker().setMaxDate(myCalendarAdop.getTimeInMillis() + (1000 * 60 * 60 * 24 * 7));
        datePickerDialogEnd.show();
    }



    public void showSickbar(String msg) {
        Snackbar snackbar = Snackbar.make(binding.RRsncbar, msg, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }


    private HashMap<String, String> getParams() {
        hashMap.put(Consts.PICKUP_DATE, ProjectUtils.getEditTextValue(binding.ctvPickupDate));
        hashMap.put(Consts.PICKUP_TIME,  ProjectUtils.getEditTextValue(binding.ctvPickupTime));
        hashMap.put(Consts.DELIVERY_DATE,  ProjectUtils.getEditTextValue(binding.ctvDeliveryDate));
        hashMap.put(Consts.DELIVERY_TIME,  ProjectUtils.getEditTextValue(binding.ctvDeliveryTime));

        return hashMap;
    }
}
