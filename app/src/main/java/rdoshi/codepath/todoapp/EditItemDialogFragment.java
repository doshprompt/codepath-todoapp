package rdoshi.codepath.todoapp;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.sql.Date;
import java.util.Calendar;

public class EditItemDialogFragment extends DialogFragment {
    private TaskItem taskItem;

    private EditText etEditItem;
    private DatePicker dpDueDate;
    private CheckBox cbDueDate;
    private Spinner spPriority;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public interface EditItemDialogListener {
        void onFinishEditDialog(TaskItem taskItem, int position);
    }


    public EditItemDialogFragment() {
    }


    public void setTaskitem(TaskItem taskItem) {
        this.taskItem = taskItem;
    }

    public static EditItemDialogFragment newInstance(TaskItem taskItem, int position) {
        EditItemDialogFragment frag = new EditItemDialogFragment();
        frag.setTaskitem(taskItem);
        Bundle args = new Bundle();
        args.putInt("pos", position);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etEditItem = (EditText) view.findViewById(R.id.etEditItem);
        etEditItem.requestFocus();

        if (taskItem.getName() != null) {
            etEditItem.append(taskItem.getName());
        }

        final Button btnEditItem = (Button) view.findViewById(R.id.btnEditItem);
        btnEditItem.setEnabled(etEditItem.getText().length() > 0);
        btnEditItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskItem.setName(etEditItem.getText().toString());

                if (cbDueDate.isChecked()) {
                    taskItem.setDueDate(getDueDate());
                } else {
                    taskItem.setDueDate(null);
                }

                taskItem.setPriority(spPriority.getSelectedItemPosition());

                if (taskItem.getId() == 0) {
                    taskItem.setId(System.currentTimeMillis());
                }

                EditItemDialogListener listener = (EditItemDialogListener) getActivity();
                listener.onFinishEditDialog(taskItem, getArguments().getInt("pos"));

                dismiss();
            }
        });

        etEditItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnEditItem.setEnabled(charSequence.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        spPriority = (Spinner) view.findViewById(R.id.spPriority);
        spPriority.setSelection(taskItem.getPriority());

        dpDueDate = (DatePicker) view.findViewById(R.id.dpDueDate);
        if (taskItem.getDueDate() != null) {
            dpDueDate.setVisibility(View.VISIBLE);
        } else {
            dpDueDate.setVisibility(View.GONE);
        }

        cbDueDate = (CheckBox) view.findViewById(R.id.cbDueDate);

        if (taskItem.getDueDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(taskItem.getDueDate());

            dpDueDate.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            cbDueDate.setChecked(true);
        }

        cbDueDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    dpDueDate.setVisibility(View.VISIBLE);
                } else {
                    dpDueDate.setVisibility(View.GONE);
                }
            }
        });
    }

    private Date getDueDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(dpDueDate.getYear(), dpDueDate.getMonth(), dpDueDate.getDayOfMonth());
        return new Date(calendar.getTime().getTime());
    }
}
