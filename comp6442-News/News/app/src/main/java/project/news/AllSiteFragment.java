package project.news;

import android.content.Context;
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
import project.news.api.ApiClient;
import project.news.api.ApiOperation;
import project.news.model.AllSites;
import project.news.model.Site;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This fragment is to display all available sources to users.
 * Add sources to following is functioning by swipe left using outside library that call swipeableRecyclerView which defines swipe action for recycler view
 * This fragment automatically refreshes when tab changes to this fragment
 *
 * @author Jing Qian
 */

public class AllSiteFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private followingSitesAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Context mContext;

    private List<Site> siteList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.followed_sites_frag,container,false);

        recyclerView =view.findViewById(R.id.list_source);
        swipeRefreshLayout=view.findViewById(R.id.list_refresh);

        layoutManager=new LinearLayoutManager(this.mContext);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setNestedScrollingEnabled(false);


        swipeRefreshLayout.setOnRefreshListener(this);

        loadSourceFromJson();

        return view;
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.mContext =context;
    }

    /**
     * This method is using retrofit to enquiry NEWSAPI and get all available sources in English language
     *
     * This method writes source information in to local files using a external library Paper.io
     *
     * This method allows recyler view to be swipeable and functions add to follow when swipe to left.
     *
     * Reference:
     * Get some ideas on how to get source from NewsAPI and create recyclerview from this tutorial on Youtube
     * https://www.youtube.com/watch?v=APInjVO0WkQ
     *
     * @author JING Qian
     *
     */
    private void loadSourceFromJson() {
        ApiOperation apiOperation= ApiClient.getApiClient(Utility.BASE_URL).create(ApiOperation.class);

        //server code
        //ApiOperation apiOperation= ApiClient.getServer(Utility.Server_BASE_URL).create(ApiOperation.class);

        Call<AllSites> call;

        call = apiOperation.getAllSites("en",Utility.API_KEY);

        //server code
        //call = apiOperation.getAllSitesFromServer();


        call.enqueue(new Callback<AllSites>() {
            @Override
            public void onResponse(Call<AllSites> call, Response<AllSites> response) {
                if (response.isSuccessful() && response.body().getSiteList() != null) {

                    List<Site> bufferList = response.body().getSiteList();//buffer all the source info in temp list

                    for(Site s: bufferList){
                        boolean exisits = false; //check whether pulled news sources have already stored
                        for(String key:Paper.book("AllSites").getAllKeys()){ //compare keys for each stored sources. keys are unique source id
                            if(key.equals(s.getId())){
                                exisits = true;
                                break;
                            }
                        }
                        if(!exisits){//if news sources are not yet stored, store it
                            Paper.book("AllSites").write(s.getId(),s);
                        }
                    }

                    if(!siteList.isEmpty()){
                        siteList.clear();//empty list for storage
                    }

                    for(String key:Paper.book("AllSites").getAllKeys()){
                        Site s =  Paper.book("AllSites").read(key);//read sources from local file
                        if(!s.isFollowing()){ //check if user is following the source, the followed sources is not display in this fragment
                            siteList.add(s);
                        }
                    }

                    adapter = new followingSitesAdapter(mContext, siteList); //set adapter to all the sources read from local file

                    recyclerView.setAdapter(adapter);//set recycler view adapter to current adapter
                        // how to do swipe to delete cardview in android using support library from Stackoverflow,
                    // the answer proposed by brnunes
                    //  https://stackoverflow.com/a/27809847
                    SwipeableRecyclerViewTouchListener swipeTouchListener =     //here defines the specific swipe action
                            new SwipeableRecyclerViewTouchListener(recyclerView,
                                    new SwipeableRecyclerViewTouchListener.SwipeListener() {
                                        @Override
                                        public boolean canSwipeLeft(int position) {
                                            return true; //enable swipe to left
                                        }

                                        @Override
                                        public boolean canSwipeRight(int position) {
                                            return false; //disable swipe to right
                                        }

                                        @Override
                                        public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                            for (int position : reverseSortedPositions) {
                                                Site current = siteList.get(position);

                                                if(current.isFollowing()){//check if source is followed by user to avoid duplications
                                                    Toast.makeText(mContext,"Already Followed",Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    current.setFollowing(true);//set the swiped source to isFollowing(true)
                                                    siteList.remove(current);//remove this source from available sources
                                                    Paper.book("AllSites").delete(current.getId());//local file delete this source, since now isFollowing=true
                                                    Paper.book("AllSites").write(current.getId(),current);//rewrite this source
                                                    Toast.makeText(mContext, "Followed " + current.getName(), Toast.LENGTH_SHORT).show(); //notify user followed
                                                }
                                                adapter.notifyItemRemoved(position); //update adapter this cardview is removed

                                            }
                                            adapter.notifyDataSetChanged();//update adapter to remove swiped cardview

                                        }

                                        @Override
                                        public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {

                                        }
                                    });

                    recyclerView.addOnItemTouchListener(swipeTouchListener);//apply swipe action by using onitemtouchlistener

                }
                else {
                    Toast.makeText(mContext,"No Response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AllSites> call, Throwable t) {
                Toast.makeText(mContext,"Connection Failed",Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * automatically make refresh this fragment when selected
     *
     * This code is
     * Learned refresh fragments when changing tabs from Stackoverflow,
     * the answer proposed by ianhanniballake https://stackoverflow.com/a/41655894
     *
     * @author Jing Qian
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    /**
     * When refreshed reload sources
     * @author Jing Qian
     */
    @Override
    public void onRefresh(){
        loadSourceFromJson();
        swipeRefreshLayout.setRefreshing(false);
    }
}
