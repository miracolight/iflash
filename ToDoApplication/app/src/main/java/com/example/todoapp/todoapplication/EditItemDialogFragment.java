package com.example.todoapp.todoapplication;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.todoapp.todoapplication.data.TodoEntry;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by qingdi on 8/23/14.
 */
public class EditItemDialogFragment extends android.support.v4.app.DialogFragment{
    private TodoEntry       todoEntry;
    private TextView        descriptionTextView;
    private DatePicker      dueDatePicker;
    private RadioGroup      priorityRadioGroup;
    private RadioButton radioSexButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todoEntry = getArguments().getParcelable(EditItemActivity.TODO_ITEM);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialogfragment_edit_item, container, false);

        descriptionTextView = ((TextView)rootView.findViewById(R.id.etEditItem));
        descriptionTextView.setText(todoEntry.description);

        dueDatePicker = (DatePicker)rootView.findViewById(R.id.datePicker);
        Calendar c = Calendar.getInstance();
        c.setTime(todoEntry.dueDate);
        dueDatePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), null);

        priorityRadioGroup = (RadioGroup)rootView.findViewById(R.id.radioPrioirty);
        priorityRadioGroup.check(findRadioButtonByPriority(todoEntry.priority));

        Button saveButton = (Button)rootView.findViewById(R.id.btnSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                todoEntry.description = descriptionTextView.getText().toString();
                todoEntry.dueDate = new Date(dueDatePicker.getYear() - 1900, dueDatePicker.getMonth(), dueDatePicker.getDayOfMonth());

                RadioButton priority = (RadioButton)priorityRadioGroup.findViewById(priorityRadioGroup.getCheckedRadioButtonId());
                todoEntry.priority = Integer.parseInt(priority.getText().toString());
                todoEntry.save();
                getDialog().dismiss();
            }
        });

        Button cancelButton = (Button)rootView.findViewById(R.id.btnCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                getDialog().dismiss();
            }
        });

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return rootView;
    }

    private int findRadioButtonByPriority(int priority) {
        switch (priority) {
            case 1:
                return R.id.radio1;
            case 2:
                return R.id.radio2;
            default:
                return R.id.radio3;
        }
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        EditItemDialogListener activity = (EditItemDialogListener) getActivity();
        activity.onFinishEditDialog();
        this.dismiss();
    }

    public interface EditItemDialogListener {
       void onFinishEditDialog();
    }

}
