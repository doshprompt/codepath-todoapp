package rdoshi.codepath.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EditItemDialogFragment.EditItemDialogListener {

    ArrayList<String> items;
    ArrayList<Integer> ids;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    private void readItems() {
        items = new ArrayList<>();
        ids = new ArrayList<>();

        List<TaskItem> taskItems = SQLite.select().from(TaskItem.class).queryList();
        for(TaskItem taskItem : taskItems) {
            items.add(taskItem.getName());
            ids.add(taskItem.getId());
        }
    }

    private void writeItem(String name, int id) {
        TaskItem taskItem = new TaskItem();

        taskItem.setName(name);
        taskItem.setId(id);

        taskItem.save();
    }

    private void deleteItem(int id) {
        List<TaskItem> taskItems = SQLite.select().
                from(TaskItem.class).
                where(TaskItem_Table.id.eq(id)).queryList();

        taskItems.get(0).delete();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);

        readItems();

        itemsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();

        ((EditText) findViewById(R.id.etNewItem)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                findViewById(R.id.btnAddItem).setEnabled(charSequence.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onFinishEditDialog(String name, int position) {
        writeItem(name, position);

        items.set(position, name);
        itemsAdapter.notifyDataSetChanged();
    }

    private void showEditDialog(String name, int position) {
        FragmentManager fm = getSupportFragmentManager();
        EditItemDialogFragment editItemDialogFragment = EditItemDialogFragment
                .newInstance(name, position);
        editItemDialogFragment.show(fm, "fragment_edit_item");
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                deleteItem(ids.get(pos));

                items.remove(pos);
                ids.remove(pos);
                itemsAdapter.notifyDataSetChanged();

                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                showEditDialog(items.get(pos), pos);
            }
        });
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        etNewItem.setText("");

        writeItem(itemText, items.size());

        ids.add(items.size());
        items.add(itemText);
        itemsAdapter.notifyDataSetChanged();
    }

}
