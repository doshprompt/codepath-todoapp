package rdoshi.codepath.todoapp;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

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
                    .where(TaskItem_Table.name.like(query))
                    .queryList());
            itemsAdapter = new TaskItemAdapter(this, items);
            lvItems.setAdapter(itemsAdapter);

            if (items.size() > 0) {
                findViewById(R.id.lblNotFound).setVisibility(View.GONE);
            } else {
                lvItems.setVisibility(View.GONE);
            }
        }
    }

}
