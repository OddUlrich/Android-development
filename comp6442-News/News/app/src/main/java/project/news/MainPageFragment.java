package project.news;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import project.news.adapter.ArticleListAdapter;
import project.news.api.ApiClient;
import project.news.api.ApiOperation;
import project.news.model.Article;
import project.news.model.NewsAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * News Page tabView fragment triggered by tab selection.
 *
 * @author  Dawen
 * @version 1.0
 */

public class MainPageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArticleListAdapter adapter;

    // Store the article objects received from enquiring NEWS API.
    private List<Article> articleList = new ArrayList<>();

    private Context mContext;

    // Store tab title passed from its father fragment.
    // Used for enquiring NEWS API
    private String fragmentTitle;

    private int numOfCols;

    /**
     * Create views in main page fragment.
     *
     * This page contains a recycler view, holding all the article.
     *
     * @param inflater inflates pages into container.
     * @param container container for all views in the current page.
     * @param savedInstanceState state.
     * @return view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //initialize a main_page_fragment layout.
        View v = inflater.inflate(R.layout.main_page_fragment, container, false);

        swipeRefreshLayout = v.findViewById(R.id.mainPage_Swipe_Layout);
        recyclerView = v.findViewById(R.id.mainPage_recyclerView);

        numOfCols = 1;

        if(this.mContext.getResources().getConfiguration().orientation ==
                this.mContext.getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
            Toast.makeText(mContext, "landscape", Toast.LENGTH_SHORT).show();
            numOfCols = 2;
        }

        /**
         * RecyclerView provides built-in layoutManagers such as
         * LinearLayout Manager, GridLayout Manager, StraggeredGridLayoutManager.
         *
         * Here we want to display the nested cardView vertically,
         * thus we initialize an linearLayout Manager which is used to display
         * items in horizontal and vertical scroll.
         *
         * By default, the constructor of linearLayout manager set orientation vertically.
         */

        layoutManager = new GridLayoutManager(mContext, numOfCols,GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Disable scrolling for recyclerView.
        recyclerView.setNestedScrollingEnabled(false);

        // Disassemble arguments passed from its father fragment.
        Bundle args = getArguments();
        fragmentTitle = args.getString("key");

        swipeRefreshLayout.setOnRefreshListener(this);

        LoadViewFromJson(fragmentTitle);

        return v;
    }

    /**
     * Save the context.
     *
     * @param context activity context.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //attach the context of its father fragment MainPage to current fragment.
        this.mContext = context;
    }

    /**
     * Enquiring NEWS API using Retrofit Library
     *
     * @author Dawen
     * @param category News Category of NEWS API
     */
    public void LoadViewFromJson(String category) {
        // Refresh the current page with content received from NEWS API.
        swipeRefreshLayout.setRefreshing(true);

        // Call defined retrofit instance and create NEWS API enquiring syntax.
        ApiOperation apiEnquiry = ApiClient.getApiClient(Utility.BASE_URL).create(ApiOperation.class);

        // Call singleton retrofit instance and create localhost server enquiring syntax.
        //ApiOperation apiEnquiry = ApiClient.getServer(Utility.Server_BASE_URL).create(ApiOperation.class);

        // Call defined retrofit instance and create NEWS API enquiring syntax.

        Call<NewsAPI> call;

        if (category != null) {
            // Generated API enquiry will be:
            // https://newsapi.org/v2/top-headlines?category=category&country="au"&apiKey=API_KEY
            call = apiEnquiry.getTypeHeadlines(category,"au",Utility.API_KEY);

            //call = apiEnquiry.getHeadlinesFromServer(category);
        }
        else {
            Toast.makeText(mContext,"category is null", Toast.LENGTH_SHORT).show();
            return;
        }

        // Using retrofit library to call NEWS API.
        call.enqueue(new Callback<NewsAPI>() {
            @Override
            public void onResponse(Call<NewsAPI> call, Response<NewsAPI> response) {
                // If the response from NEWS API is successful and get article objects after Gson parsing, update the recyclerview.
                if (response.isSuccessful()&& response.body().getArticleList() != null){

                    // Designed for refreshing.
                    // Clear previously article objects, store new article objects.
                    if(!articleList.isEmpty()){
                        articleList.clear();
                    }

                    articleList = response.body().getArticleList();

                    // Passing article objects to recycler view adapter.
                    adapter = new ArticleListAdapter(mContext,response.body().getArticleList());

                    recyclerView.setAdapter(adapter);

                    // Update recyclerview
                    adapter.notifyDataSetChanged();

                    // Set onclick listener for the recyclerview adapter,
                    // but the OnItemClick method will be called,
                    // only when the onclick method of the viewholder is being invoked.
                    ArticleClickListener();

                    // Terminate refreshing after updating the content.
                    swipeRefreshLayout.setRefreshing(false);

                } else {
                    // If not data received, terminate refreshing.
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(mContext,"No response from Json", Toast.LENGTH_SHORT).show();
                }
            }

            // If the enquiry fails, terminate refreshing the page.
            @Override
            public void onFailure(Call<NewsAPI> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    /**
     * When recyclerview item is clicked, start NewsDetailActivity and load the article website in a webcontent view.
     *
     * @author Dawen
     */
    private void ArticleClickListener(){
        // Call setOnItemClickListener function of the adapter with an override OnItemClickListener object.
        adapter.setOnItemClickListener(new ArticleListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getActivity(),NewsDetailsActivity.class);

                // Get the article object with recyclerView position
                Article article = articleList.get(position);

                // Pass an article object to newsDetails activity
                intent.putExtra("Article",new Gson().toJson(article));
                startActivity(intent);
            }
        });
    }

    /**
     * Refresh the context in fragment.
     *
     * @author Dawen
     */
    @Override
    public void onRefresh() {
        if (fragmentTitle!=null) {
            //enquire the API one more time to update the content.
            LoadViewFromJson(fragmentTitle);
            Toast.makeText(mContext,fragmentTitle+" is refreshing", Toast.LENGTH_SHORT).show();
        }
        else {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * Apply new configuration based on different screen orientation.
     *
     * @param newConfig new configuration
     * @reference stackoverflow answer https://stackoverflow.com/a/51039983
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            try {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false);
                }
                ft.detach(this).attach(this).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
