package sk.durovic.financie.ui.main;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class addDateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private Context context;
    private NotifyOnPickDate notifier;

    public addDateDialog() {
    }

    public static addDateDialog newInstance(Context context, NotifyOnPickDate window) {
        addDateDialog fragment = new addDateDialog();
        fragment.context = context;
        fragment.notifier = window;
        return fragment;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dueDate = (month+1) + "/" + dayOfMonth + "/" + year;
        notifier.onPositiveClick(dueDate);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        notifier.onNegativeClick();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(context, this, year, month, day);
    }


    interface NotifyOnPickDate {
        void onPositiveClick(String dateString);
        void onNegativeClick();
    }
}
