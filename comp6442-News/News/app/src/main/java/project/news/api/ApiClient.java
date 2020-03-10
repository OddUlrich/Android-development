package project.news.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Client api to receive and parse news data.
 *
 * @author Dawen
 * @version 1.0
 */
public class ApiClient {

    public static Retrofit retrofit = null;

    /**
     * Initialize an static instance of Retrofit as our API Enquiring client
     *
     * @author Dawen
     * @param baseUrl base url
     * @return retrofit
     */
    public static Retrofit getApiClient(String baseUrl){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(baseUrl) // Initialize the instance with the baseUrl
                    // Add Gson Json converter into retrofit object
                    // and automatically mapping the json data into NewsAPI object, Article object, and Source object
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public static Retrofit client = null;

    /**
     * Initialize an static instance of Retrofit as our API Enquiring client
     *
     * @author Dawen
     * @param baseUrl base url
     * @return client
     */
    public static Retrofit getServer(String baseUrl){
        if (client == null) {
            client = new Retrofit.Builder().baseUrl(baseUrl) // Initialize the instance with the baseUrl
                    // Add Gson Json converter into retrofit object
                    // and automatically mapping the json data into NewsAPI object, Article object, and Source object
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return client;
    }
}
