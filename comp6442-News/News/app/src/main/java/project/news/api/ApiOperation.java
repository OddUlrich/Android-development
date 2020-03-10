package project.news.api;

import project.news.model.AllSites;
import project.news.model.NewsAPI;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiOperation {

    //https://newsapi.org/v2/top-headlines?category=category&country="au"&apiKey=API_KEY
    //will be generated
    @GET("top-headlines")
    Call<NewsAPI> getTypeHeadlines(
            @Query("category") String category,
            @Query("country") String country,
            @Query("apiKey") String apiKey
    );

    @GET("top-headlines")
    Call<NewsAPI> getSources(
            @Query("sources")String source,
            @Query("apiKey") String apiKey
    );

    @GET("sources")
    Call<AllSites> getAllSites(
            @Query("language") String language,
            @Query("apiKey") String apiKey
    );

    @GET("/top-headlines/{category}")
    Call<NewsAPI> getHeadlinesFromServer(
            @Path("category") String category
    );

    @GET("/sources")
    Call<AllSites> getAllSitesFromServer();
}
