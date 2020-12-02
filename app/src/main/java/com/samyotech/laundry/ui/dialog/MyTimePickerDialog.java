package com.samyotech.laundry.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.samyotech.laundry.R;

import java.util.Calendar;

import mehdi.sakout.fancybuttons.FancyButton;

public class MyTimePickerDialog extends DialogFragment {
    private final TimeListener listener;
    private TimePicker picker;
    private FancyButton simpanBtn;

    public MyTimePickerDialog(TimeListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_time_picker, null);
        picker = view.findViewById(R.id.time_picker);
        picker.setIs24HourView(true);
        simpanBtn = view.findViewById(R.id.simpan_btn);

        simpanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MINUTE, picker.getMinute());
                calendar.set(Calendar.HOUR, picker.getHour());
                listener.onSelected(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE));
                dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }

    public interface TimeListener {
        void onSelected(int hour, int minute);
    }
}
