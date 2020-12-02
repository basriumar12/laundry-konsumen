package com.samyotech.laundry.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.samyotech.laundry.R;

import java.util.Calendar;

import mehdi.sakout.fancybuttons.FancyButton;

public class MyDatePickerDialog extends DialogFragment {
    private final DateListener listener;
    private final Calendar calendar = Calendar.getInstance();
    private CalendarView picker;
    private FancyButton simpanBtn;

    public MyDatePickerDialog(DateListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_date_picker, null);
        picker = view.findViewById(R.id.calendar_picker);
        picker.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
            }
        });
        simpanBtn = view.findViewById(R.id.simpan_btn);

        simpanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelected(calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }

    public interface DateListener {
        void onSelected(int day, int month, int year);
    }
}

