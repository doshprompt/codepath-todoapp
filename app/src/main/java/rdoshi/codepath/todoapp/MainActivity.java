package rdoshi.codepath.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayList<Integer> ids;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    enum REQUEST_CODES {
        EDIT_ITEM
    }

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODES.EDIT_ITEM.ordinal()) {
            String item = data.getExtras().getString("item");
            int position = data.getExtras().getInt("position");

            writeItem(item, position);

            items.set(position, item);
            itemsAdapter.notifyDataSetChanged();
        }
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
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);

                intent.putExtra("item", items.get(pos));
                intent.putExtra("position", pos);

                startActivityForResult(intent, REQUEST_CODES.EDIT_ITEM.ordinal());
            }
        });
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        etNewItem.setText("");

        writeItem(itemText, items.size());

        ids.add(items.size());
        items.add(itemText);
        itemsAdapter.notifyDataSetChanged();
    }

}
