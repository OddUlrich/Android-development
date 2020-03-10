package project.news.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * The first layer of NEWS API Json data includes a string status, a number totalResults, an object articles.
 * We use Gson to annotate three attributes of the NewsAPI class the same name as the json data.
 * These annotations will be used to convert the json data to an equivalent NewsAPI object.
 *
 * @author  Dawen
 * @version 1.0
 */
public class NewsAPI {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("totalResults")
    @Expose
    private int totalResults;

    @SerializedName("articles")
    @Expose
    private List<Article> articleList;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }
}

