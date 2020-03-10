package project.news;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity on management for favourite post tag group.
 *
 * @author Wyman Wong
 * @version 1.0
 *
 */
public class FavTagActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private Toolbar toolbar;
    private ArrayAdapter adapter;
    List<String> tagList = new ArrayList<>();
    FavPostManager favPostMgr = FavPostManager.getInstance(this);

    /**
     * Create views in favourite post page.
     *
     * The menu provide searching function on tag group.
     * The list view loads favourite page tag from date file.
     *
     * @author Wyman
     * @param savedInstanceState state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_tag_edit);

        // Setup the toolbar for menu.
        toolbar = findViewById(R.id.tag_toolbar);
        toolbar.setTitle("Tag Group");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Load tag list data.
        tagList = favPostMgr.getTagList();
        adapter = new ArrayAdapter<>(this, R.layout.list_tag_layout, tagList);
        ListView listView = findViewById(R.id.list_tag_view);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // Set item click listener for list view.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3)
            {
                // Return the name of tag group to update showing list.
                String tagStr = (String) parent.getItemAtPosition(position);

                Intent intent = new Intent();
                intent.putExtra("tag", tagStr);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * Create optional menu items in toolbar.
     *
     * This menu contains searching on tag group.
     *
     * @author Wyman
     * @param menu menu group
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tag_edit_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.tag_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        // Configure expansion and collapse behaviour for searching item.
        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        };
        menuItem.setOnActionExpandListener(onActionExpandListener);

        return true;
    }

    /**
     * Actions for other menu items.
     *
     * Home button for returning to the previous activity.
     *
     * @author Wyman
     * @param item the selected menu item.
     * @return
     *      true  - successfully execution;
     *      false - failed execution.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Submit query for searching.
     *
     * @author Wyman
     * @param query searching query.
     * @return
     *      true  - successfully execution;
     *      false - failed execution.
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * Update the current existing list with the temporary query text.
     *
     * @author Wyman
     * @param newText temporary searching query text.
     * @return
     *      true  - successfully execution;
     *      false - failed execution.
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        List<String> newList = new ArrayList<>();
        String userInput = newText.toLowerCase();
        tagList = favPostMgr.getTagList();

        for (String tag: tagList) {
            if (tag.toLowerCase().contains(userInput)) {
                newList.add(tag);
            }
        }

        adapter.clear();
        adapter.addAll(newList);
        adapter.notifyDataSetChanged();

        return true;
    }
}
