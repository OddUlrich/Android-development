package project.news;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;


import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import project.news.adapter.FavouriteAdapter;
import project.news.model.Article;

/**
 * Favourite page fragment class.
 * This fragment contains multiple elements, including a toolbar with menu and a recycling view
 * of all favourite articles.
 *
 * @author Wyman Wong
 * @version 1.0
 *
 */
public class FavouritePage extends Fragment implements SearchView.OnQueryTextListener {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FavouriteAdapter adapter;
    private Context context;
    private FavPostManager favPostManager;

    // Code for activity request.
    public static final int RESULT_OK = -1;
    private static final int TAG_STRING_REQUEST_CODE = 1;

    /**
     * Create views in favourite page fragment.
     *
     * The menu provide searching function and tag group management.
     * The recycling view loads favourite page list from date file.
     * Swipe each article card view from right to left can show two options: edit tag and delete article.
     *
     * @author Wyman
     * @param inflater inflates pages into container.
     * @param container container for all views in the current page.
     * @param savedInstanceState state.
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fav_page_fragment, container, false);

        // Configuration on toolbar with menu.
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("Favourite");
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        // Recycler view.
        recyclerView = view.findViewById(R.id.fav_recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        // Loading favourite list from data file.
        favPostManager = FavPostManager.getInstance(context);
        adapter = new FavouriteAdapter(context, favPostManager);
        adapter.oldList.addAll(favPostManager.getFavPostList());

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recyclerView.setHasFixedSize(true);

        // Set item click listener to the post adapter.
        adapter.setOnItemClickListener(new FavouriteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(),NewsDetailsActivity.class);

                Article article = adapter.showingList.get(position);
                intent.putExtra("Article", new Gson().toJson(article));

                startActivity(intent);
            }
        });

        // Attach swiping helper to recycler view.
        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (final int position : reverseSortedPositions) {
                                    new AlertDialog.Builder(context)
                                            .setTitle("Confirm to delete the post?")
                                            .setIcon(android.R.drawable.sym_def_app_icon)
                                            .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    // Delete item and notify the recycler view.
                                                    adapter.deleteItem(position);
                                                    Toast.makeText(context, "Removed favourite post.", Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .setNegativeButton("Cancel", null)
                                            .show();
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (final int position : reverseSortedPositions) {
                                    final EditText editText = new EditText(context);

                                    editText.setText(adapter.showingList.get(position).getTag());
                                    editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
                                    editText.setSingleLine(true);
                                    editText.setSelection(0, editText.getText().length());

                                    new AlertDialog.Builder(context)
                                            .setTitle("Edit the tag for this post:")
                                            .setIcon(android.R.drawable.sym_def_app_icon)
                                            .setView(editText)
                                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    String tagString = editText.getText().toString();

                                                    if (tagString.equals("")) {
                                                        tagString = "Untagged";
                                                    }

                                                    adapter.modifyItem(position, tagString);
                                                    Toast.makeText(context, "Editing tag of post succeeded.", Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .setNegativeButton("Cancel", null)
                                            .show();
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

        recyclerView.addOnItemTouchListener(swipeTouchListener);

        return view;
    }

    /**
     * Create optional menu items in toolbar.
     *
     * This menu contains searching and tag group management.
     * Searching key works of title among all the saved articles to filter the target.
     *
     * @author Wyman
     * @param menu menu group
     * @param inflater inflates menu layout into menu group.
     */
    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);

        // Configure expansion and collapse behaviour for searching item.
        MenuItem searchItem = menu.findItem(R.id.local_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        // Set action expand listener for search item.
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
        searchItem.setOnActionExpandListener(onActionExpandListener);

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Actions for other menu items.
     *
     * Tag group management will start a new activity to select, edit, and delete tag group.
     * It returns all articles in specific tag group selected to favourite page and refresh the existing list.
     *
     * @author Wyman
     * @param item the selected menu item.
     * @return
     *      true  - successfully execution;
     *      false - failed execution.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.tag_group) {
            // Start a new activity to manage tag group.
            Intent intent = new Intent(getActivity(), FavTagActivity.class);
            startActivityForResult(intent, TAG_STRING_REQUEST_CODE);

            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Address the result from associated activities.
     *
     * @author Wyman
     * @param requestCode id for the request activity.
     * @param resultCode represents result, RESULT_OK or RESULT_NO.
     * @param data result data from the request activity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that it is tag_string_request with an OK result
        if (requestCode == TAG_STRING_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                // Get note string from Intent
                String returnTag = data.getStringExtra("tag");

                // Select the tag group and list all posts.
                List<Article> tagPostList;

                if (returnTag.equals("All (default)")) {
                    tagPostList = adapter.favPostManager.getFavPostList();
                } else {
                    tagPostList = adapter.favPostManager.getFavPostListWithTag(returnTag);
                }
                adapter.updateList(tagPostList);
                adapter.oldList = tagPostList;
            }
        }
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
        return true;
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
        List<Article> newList = new ArrayList<>();

        String userInput = newText.toLowerCase();

        for (Article article: adapter.oldList) {
            if (article.getTitle().toLowerCase().contains(userInput)) {
                newList.add(article);
            }
        }

        adapter.updateList(newList);
        return true;
    }

    /**
     * Save the context.
     *
     * @author Wyman
     * @param context activity context.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    /**
     * Refresh the favourite page when switch to this page.
     * see: MainActivity.
     *
     * @author Wyman
     */
    public void updateState() {
        List<Article> freshList;

        freshList = favPostManager.getFavPostList();
        adapter.updateList(freshList);
        adapter.oldList = freshList;
    }
}
