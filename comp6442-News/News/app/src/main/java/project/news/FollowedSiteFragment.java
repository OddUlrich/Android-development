package project.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.brnunes.swipeablerecyclerview.SwipeableRecyclerViewTouchListener;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import project.news.adapter.followingSitesAdapter;
import project.news.model.Site;

/**
 * This fragment is to display all followed sources to users.
 * Add sources to following is functioning by swipe left using outside library that call swipeableRecyclerView which defines swipe action for recycler view.
 * This fragment automatically refreshes when tab changes to this fragment.
 *
 * @author Jing Qian
 * @version 1.0
 */
public class FollowedSiteFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView listWebsite;
    private RecyclerView.LayoutManager layoutManager;
    private followingSitesAdapter adapter;
    private SwipeRefreshLayout swipeLayout;

    private Context mContext;

    private List<Site> followingList = new ArrayList<>();

    /**
     * Create a swipe layout.
     *
     * @param inflater inflates pages into container.
     * @param container container for all views in the current page.
     * @param savedInstanceState state.
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.followed_sites_frag, container, false);

        swipeLayout=view.findViewById(R.id.list_refresh);

        listWebsite=view.findViewById(R.id.list_source);
        layoutManager=new LinearLayoutManager(this.mContext);
        listWebsite.setLayoutManager(layoutManager);

        listWebsite.setNestedScrollingEnabled(false);

        swipeLayout.setOnRefreshListener(this);

        loadViewFromDB();
        return view;
    }

    /**
     * Save the context.
     *
     * @param context activity context.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    /**
     * This method loads sources from local file snd apply to recyclerview.
     *
     * Gives recyclerview a swipebale feature.
     * Remove from following is functioned through swipe to right.
     *
     * Get some ideas on how to get source from NewsAPI and create recyclerview
     * from this tutorial on Youtube
     * https://www.youtube.com/watch?v=APInjVO0WkQ
     *
     * @author Jing Qian
     */
    private void loadViewFromDB(){
        // Load following sources from local file.
        updateFollowingList();

        adapter = new followingSitesAdapter(mContext, followingList);
        listWebsite.setAdapter(adapter);

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(listWebsite,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {  // Defines the specific swipe action.
                            @Override
                            public boolean canSwipeLeft(int position) { // Disable swipe left
                                return false;
                            }

                            @Override
                            public boolean canSwipeRight(int position) { // Enable swipe right
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {

                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    Site current = followingList.get(position);

                                    // Set the swiped sources's isFollowing attribute to false, which prevent it from adding to display.
                                    current.setFollowing(false);
                                    Paper.book("AllSites").delete(current.getId());  // Delete this source from local file, since isFollowng changed.
                                    Paper.book("AllSites").write(current.getId(),current);  // Rewrite this source with updated isFollowing attribute.
                                    Toast.makeText(mContext, "Unfollowed " + current.getName(), Toast.LENGTH_SHORT).show();
                                    updateFollowingList(); // Reload from local file.

                                    adapter.notifyItemRemoved(position);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
        // Apply onclick listener to each recycler view.
        FollowingOnClickListener();

        // Apply swipe action to each recycler view by using onItemTouchListener.
        listWebsite.addOnItemTouchListener(swipeTouchListener);

    }

    /**
     * Automatically make refresh this fragment when selected.
     *
     * @author Jing Qian
     * @param isVisibleToUser flag
     */

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    /**
     * Invoke onclickListener.
     * Create customized onclick method for this recycler view.
     * Forward an intent to open the new activity which shows all the latest news articles of selected news source.
     *
     * @author Jing Qian
     */

    private void FollowingOnClickListener(){
        adapter.setOnItemClickListener(new followingSitesAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                Intent intent=new Intent(mContext,SiteViewActivity.class );
                Bundle bundle=new Bundle();
                Site site= followingList.get(position);
                bundle.putString("source", site.getId());
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }
    /**
     * Refreshed reload sources.
     *
     * @author Jing Qian
     */
    @Override
    public void onRefresh(){
        loadViewFromDB();
        Toast.makeText(mContext,"Refreshed", Toast.LENGTH_SHORT).show();
        swipeLayout.setRefreshing(false);

    }

    /**
     * This method checks and empty the temp list that stores sources.
     * Reload sources from local file.
     * Loaded sources to temp list for display if they are followed by user.
     *
     * @author Jing Qian
     */
    private void updateFollowingList(){

        if(!followingList.isEmpty()){
            followingList.clear();
        }

        for(String key:Paper.book("AllSites").getAllKeys()){
            Site current =  Paper.book("AllSites").read(key);
            if(current.isFollowing()){
                followingList.add(current);
            }
        }
    }

}
