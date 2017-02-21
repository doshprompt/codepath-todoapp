package rdoshi.codepath.todoapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class EditItemDialogFragment extends DialogFragment implements TextView.OnEditorActionListener {
    private EditText etEditItem;
    private int position;

    public interface EditItemDialogListener {
        void onFinishEditDialog(String name, int position);
    }


    public EditItemDialogFragment() {
    }

    public static EditItemDialogFragment newInstance(String name, int position) {
        EditItemDialogFragment frag = new EditItemDialogFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putInt("position", position);
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

        final View myView = view;

        position = getArguments().getInt("position");

        etEditItem = (EditText) view.findViewById(R.id.etEditItem);
        etEditItem.append(getArguments().getString("name"));
        etEditItem.setOnEditorActionListener(this);
        etEditItem.requestFocus();

        getDialog().setTitle("Edit Item");
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        myView.findViewById(R.id.btnEditItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditItem();
            }
        });

        ((EditText) myView.findViewById(R.id.etEditItem)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                myView.findViewById(R.id.btnEditItem).setEnabled(charSequence.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            onEditItem();

            return true;
        }

        return false;
    }

    private void onEditItem() {
        EditItemDialogListener listener = (EditItemDialogListener) getActivity();
        listener.onFinishEditDialog(etEditItem.getText().toString(), position);

        dismiss();
    }
}
