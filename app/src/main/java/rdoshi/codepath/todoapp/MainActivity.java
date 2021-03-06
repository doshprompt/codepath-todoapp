package rdoshi.codepath.todoapp;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements EditItemDialogFragment.EditItemDialogListener {

    ArrayList<TaskItem> items;
    TaskItemAdapter itemsAdapter;
    ListView lvItems;

    boolean sortOrder;
    Comparator sortComparator;

    Comparator<TaskItem> dueDateComparator = new Comparator<TaskItem>() {
        @Override
        public int compare(TaskItem t1, TaskItem t2) {
            if (t1.getDueDate() == null) {
                return (t2.getDueDate() == null) ? 0 : -1;
            }
            if (t2.getDueDate() == null) {
                return 1;
            }
            return t2.getDueDate().compareTo(t1.getDueDate());
        }
    };

    Comparator<TaskItem> priorityComparator = new Comparator<TaskItem>() {
        @Override
        public int compare(TaskItem t1, TaskItem t2) {
            return t1.getPriority() - t2.getPriority();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                FragmentManager fm = getSupportFragmentManager();
                EditItemDialogFragment editItemDialogFragment = EditItemDialogFragment
                        .newInstance(new TaskItem(), items.size());
                editItemDialogFragment.show(fm, "fragment_edit_item");
                return true;
            case R.id.action_search:
                return true;
            case R.id.action_sort_due_date:
                if (sortComparator != null && sortComparator.equals(dueDateComparator)) {
                    sortOrder = !sortOrder;
                } else {
                    sortComparator = dueDateComparator;
                    sortOrder = true;
                }
                sort();
                return true;
            case R.id.action_sort_priority:
                if (sortComparator != null && sortComparator.equals(priorityComparator)) {
                    sortOrder = !sortOrder;
                } else {
                    sortComparator = priorityComparator;
                    sortOrder = true;
                }
                sort();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);

        items = new ArrayList<>(SQLite.select().from(TaskItem.class).queryList());

        itemsAdapter = new TaskItemAdapter(this, items);
        lvItems.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    @Override
    public void onFinishEditDialog(TaskItem taskItem, int position) {
        taskItem.save();

        if (position != items.size()) {
            items.set(position, taskItem);
        } else {
            items.add(taskItem);
        }

        itemsAdapter.notifyDataSetChanged();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                TaskItem taskItem = items.get(pos);

                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), String.format(getString(R.string.task_complete), taskItem.getName()), Toast.LENGTH_SHORT).show();

                taskItem.delete();

                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                FragmentManager fm = getSupportFragmentManager();
                EditItemDialogFragment editItemDialogFragment = EditItemDialogFragment
                        .newInstance(items.get(pos), pos);
                editItemDialogFragment.show(fm, "fragment_edit_item");
            }
        });
    }

    private void sort() {
        Collections.sort(items, sortComparator);
        if (!sortOrder) {
            Collections.reverse(items);
        }

        itemsAdapter.notifyDataSetChanged();
    }

}
