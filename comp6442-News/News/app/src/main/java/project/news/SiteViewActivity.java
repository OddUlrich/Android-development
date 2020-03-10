package project.news;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
 * A new activity to view news articles form selected source website.
 *
 * @author Jing Qian
 * @version 1.0
 */
public class SiteViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArticleListAdapter adapter;
    private Toolbar toolbar;
    private Context mContext;

    private List<Article> articleList = new ArrayList<>();

    private String source="";

    /**
     * Create views in site page.
     *
     * @param savedInstanceState instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_site_view);  // Set the activity layout

        recyclerView =findViewById(R.id.siteview_recyclerView); // Set the recycler view.
        layoutManager = new LinearLayoutManager(this);   // Set layout manager as linear layout for recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);  // Disable scrolling for recyclerView.

        toolbar=findViewById(R.id.siteview_toolbar);  // Set toolbar layout.
        setSupportActionBar(toolbar);//show the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Show back button on toolbar.

        // When get intent from the previous page get the string(site name) parsed in and load the site.
        if(getIntent()!=null){
            source=getIntent().getStringExtra("source");
            if(!source.isEmpty()){
                loadWebsiteSource(source);
            }
        }
    }

    /**
     * The method to finish current activity and go back
     *
     * @author Jing Qian
     * @return true
     */
    @Override
    public boolean onSupportNavigateUp(){
        // Override method for back button, which finish up the current activity and go back.
        finish();
        return true;
    }

    /**
     * Use retrofit to create enquiring syntax
     * Query NEWS_API to get latest news
     *
     * @author Jing Qian
     * @param source news source website name to load news articles from a particular source website
     *
     */

    public void loadWebsiteSource(String source) {

        ApiOperation apiEnquire = ApiClient.getApiClient(Utility.BASE_URL).create(ApiOperation.class);//using defined retrofit instance and create NEWS API enquiring syntax

        Call<NewsAPI> call;

        call = apiEnquire.getSources(source, Utility.API_KEY);

        call.enqueue(new Callback<NewsAPI>() {  // Using retrofit library to call NEWS API
            @Override
            public void onResponse(Call<NewsAPI> call, Response<NewsAPI> response) {

                // If the response from NEWS API is successful and get article objects after Gson parsing, update the recyclerview.
                if(response.isSuccessful()&& response.body().getArticleList()!=null){
                    adapter= new ArticleListAdapter(getBaseContext(),response.body().getArticleList());

                    // Designed for refreshing.
                    // Clear stored article objects, store new article objects
                    if(!articleList.isEmpty()){
                        articleList.clear();
                    }

                    articleList = response.body().getArticleList();

                    recyclerView.setAdapter(adapter);  // Passing article objects to recycler view adapter

                    ArticleClickListener();

                    adapter.notifyDataSetChanged();  // Update recycler view
                }else {
                    // If not data received, notify user no response get
                    Toast.makeText(mContext,"No response from Json", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewsAPI> call, Throwable t) {
                // If the enquiry fails,notify user cannot connect to newsAPI
                Toast.makeText(mContext,"No Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Invoke onclickListener
     * Create customized onclick method for this recycler view
     * Forward an intent to open the news articles
     *
     * @author Dawen Yang
     */
    private void ArticleClickListener(){
        // Call setOnItemClickListener function of the adapter with an override OnItemClickListener object.
        adapter.setOnItemClickListener(new ArticleListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(mContext,NewsDetailsActivity.class);

                // Get the article object with recyclerView position
                Article article = articleList.get(position);
                intent.putExtra("Article",new Gson().toJson(article));
                startActivity(intent);
            }
        });
    }
}
