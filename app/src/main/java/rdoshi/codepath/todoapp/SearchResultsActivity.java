package rdoshi.codepath.todoapp;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    ArrayList<TaskItem> items;
    TaskItemAdapter itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);

        lvItems = (ListView) findViewById(R.id.lvItems);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            items = new ArrayList<>(SQLite
                    .select()
                    .from(TaskItem.class)
                    .where(TaskItem_Table.name.like("%" + query + "%"))
                    .queryList());
            itemsAdapter = new TaskItemAdapter(this, items);
            lvItems.setAdapter(itemsAdapter);

            if (items.size() > 0) {
                findViewById(R.id.lblNotFound).setVisibility(View.GONE);
                setupListViewListener();
            } else {
                lvItems.setVisibility(View.GONE);
            }
        }
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

                if (items.size() == 0) {
                    findViewById(R.id.lblNotFound).setVisibility(View.VISIBLE);
                    lvItems.setVisibility(View.GONE);
                }

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
}
