package project.news;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.gson.Gson;

import project.news.model.Article;

/**
 * News activity display the content of news from remote sites.
 *
 * @version 1.0b
 * @author Shuaiqun Pan
 */
public class NewsDetailsActivity extends AppCompatActivity{

    private WebView webView;
    private Toolbar toolbar;
    private String mUrl;
    private String mSource;
    private Article article;

    Context context = this;

    /**
     * Create views for the news post.
     *
     * @param savedInstanceState state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // The first call of a sequence of method call of starting a new activity
        super.onCreate(savedInstanceState);

        // Method of activity class. It shows layout on screen.
        setContentView(R.layout.news_details_activity);

        // Android Bundle is used to pass data between activities.
        Bundle bundle = getIntent().getExtras();

        String jsonArticle;
        if(bundle!=null){
            jsonArticle = bundle.getString("Article");
            article = new Gson().fromJson(jsonArticle,Article.class);
            mUrl = article.getUrl();
            initWebView(mUrl);
        }
        else {
            Toast.makeText(this,"empty bundle",Toast.LENGTH_SHORT).show();
        }

        // Use the toolbar on the screen when the user enter the news site
        toolbar = findViewById(R.id.detail_toolbar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//display the back button


    }

    /**
     * An extension of Android's View class that allows you to display web pages as a part of your activity layout.
     * Get some ideas on how to click and show the news detail from this tutorial on Youtube https://www.youtube.com/watch?v=_j7C_px28lo
     *
     * @param url web url
     * @author Shuaiqun Pan
     */
    private void initWebView(String url) {
        webView = findViewById(R.id.newsdetail_webview);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    /**
     * Finish this activity and return to the previous activity.
     *
     * @return true
     */
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    /**
     * onCreateOptionsMenu() to specify the options menu for an activity.
     * In this method, you can inflate your menu resource (defined in XML) into the Menu provided in the callback.
     *
     * @param menu menu
     * @return boolean
     * @author Shuaiqun Pan
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.newsdeatilmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Press on back button.
     **/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Configure the action of each menu item.
     * @param item menu item
     * @return true
     * @author Shuaiqun Pan
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        final EditText editText = new EditText(this);

        // Here, we come true the function which could let the user read the news with the default browser
        // Get some idea on how to open the news with browser from this tutorial on Youtube https://www.youtube.com/watch?v=pJXQVE72z80
        if (id == R.id.view_web) {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(mUrl));
            startActivity(i);
            return true;
        }

        if (id == R.id.favorites) {
            // Alert dialog asking for input tag.

            final FavPostManager postMgr = FavPostManager.getInstance(context);
            if (postMgr.isExistedFavPost(article)) {
                Toast.makeText(getApplicationContext(), "This post has been added.", Toast.LENGTH_LONG).show();
            } else {
                editText.setImeOptions(EditorInfo.IME_ACTION_DONE);
                editText.setSingleLine(true);

                new AlertDialog.Builder(this)
                    .setTitle("Input a tag for this post:")
                    .setIcon(android.R.drawable.sym_def_app_icon)
                    .setView(editText)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String tagString = editText.getText().toString();
                            if (tagString.equals("")) {
                                article.setTag("Untagged");
                            } else {
                                article.setTag(tagString);
                            }

                            postMgr.addNewFavPost(article);

                            Toast.makeText(getApplicationContext(), "Add favourite post succeeded.", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
            }

            return true;

        }

        // Here, we come true the function which could let the user share the news to some social app such as facebook or twitter which they are interested in.
        // Get some ideas on how to share the news with some social apps from this tutorial on Youtube https://www.youtube.com/watch?v=pJXQVE72z80
        else if (id == R.id.action_share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, mSource);
                String body = "Share from News App:" + "\n" + mUrl + "\n";
                i.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(i, "Share with :"));


            }catch (Exception e) {
                Toast.makeText(this, "Hmm... Sorry, \nCannot be shared", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }




}
