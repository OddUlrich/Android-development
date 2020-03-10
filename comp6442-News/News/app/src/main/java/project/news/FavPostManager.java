package project.news;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import project.news.model.Article;

/**
 * Favourite post management.
 *
 * This class provide variable method to access data of favourite posts and their related tag group,
 * including add, delete, modify get methods, as well as load from and saving data to json file.
 *
 * @author Wyman Wong
 * @version 1.0
 *
 */
public class FavPostManager {

    private static FavPostManager instanceMgr;
    private Context context;
    private List<Article> list;

    // Tmp cache for searching tag group.
    private HashMap<String, List<Article>> tagMap = new HashMap<>();
    private static final String FILENAME = "FavPostList.json";


    FavPostManager(Context context) {
        this.context = context;

        // Load user favourite post list.
        list = loadAndTransFromJson(FILENAME);
    }

    /**
     * Singleton pattern for instance access.
     *
     * @author Wyman
     * @param context context
     * @return instance.
     */
     static FavPostManager getInstance(Context context) {
        if(instanceMgr == null) {
            instanceMgr = new FavPostManager(context);
        }

        return instanceMgr;
    }

    /**
     * Check whether a post has been added to favourite.
     *
     * @author Wyman
     * @param curPost the post to be checked.
     * @return
     *      true  - the post has been added.
     *      false - the post has not been added.
     */
    public boolean isExistedFavPost(Article curPost) {
        for (Article article: list) {
            if (article.getTitle().equals(curPost.getTitle())
                && article.getSource().getName().equals(curPost.getSource().getName())
                && article.getPublishedAt().equals(curPost.getPublishedAt())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Add a new favourite post.
     *
     * @author Wyman
     * @param newPost the post to be added.
     */
    public void addNewFavPost(Article newPost) {
        // Assign default tag if the user does not do it.
        if (newPost.getTag().equals("")) {
            newPost.setTag("Untagged");
        }

        list.add(newPost);

        String tag = newPost.getTag();

        // Update the cache map: add the new post to the associated list.
        if (tagMap.containsKey(tag)) {
            List<Article> postList = tagMap.get(tag);
            postList.add(newPost);
        }

        // Save to json file.
        transAndSaveToJson(FILENAME);
    }

    /**
     * Remove an existing favourite post.
     *
     * @author Wyman
     * @param post the post to be removed.
     */
    public void removeFavPost(Article post) {
        list.remove(post);

        String tag = post.getTag();

        // Update the cache map: remove the post from the associated list.
        if (tagMap.containsKey(tag)) {
            List<Article> postList = tagMap.get(tag);
            postList.remove(post);

            // Remove tag group if there is no post belonging to it.
            if (postList.size() <= 0) {
                tagMap.remove(tag);
            }
        }

        // Save to json file.
        transAndSaveToJson(FILENAME);
    }

    /**
     * Get the whole favourite post list in adding date descending order.
     *
     * @author Wyman
     * @return list
     */
    public List<Article> getFavPostList() {
        List<Article> retList = new ArrayList<>();

        for (Article post: list) {
            retList.add(0, post);
        }

        return retList;
    }

    /**
     * Modify the tag of a post
     *
     * @author Wyman
     * @param post the post to be modified.
     * @param newTag new tag for the post.
     */
    public void modifyTagOfPost(Article post, String newTag) {
        String oldTag = post.getTag();

        // Modify the post tag.
        post.setTag(newTag);

        // Remove the post from the previous tag group.
        if (tagMap.containsKey(oldTag)) {
            List<Article> postList = tagMap.get(oldTag);
            postList.remove(post);

            if (postList.size() == 0) {
                tagMap.remove(oldTag);
            }
        }

        // Update or create the new tag group.
        if (tagMap.containsKey(newTag)) {
            List<Article> postList = tagMap.get(newTag);
            postList.add(0,post);
        } else {
            List<Article> postList = new ArrayList<>();
            postList.add(0,post);
            tagMap.put(newTag, postList);
        }

        transAndSaveToJson(FILENAME);
    }

    /**
     * Get a post list according to a tag group.
     *
     * @author Wyman
     * @param tag tag group
     * @return list
     */
    public List<Article> getFavPostListWithTag(String tag) {
        // Get the list from cache map.
        if (tagMap.containsKey(tag)) {
            return tagMap.get(tag);
        }

        List<Article> listWithTag = new ArrayList<>();

        for (Article article: list) {
            if (article.getTag().equals(tag)) {
                listWithTag.add(0, article);
            }
        }

        // Add the list to the map for quick access.
        tagMap.put(tag, listWithTag);

        return listWithTag;
    }

    /**
     * Get the list of tag group for tag management.
     *
     * @author Wyman
     * @return tag list
     */
    public List<String> getTagList() {
        List<String> tagList = new ArrayList<>();

        for (Article article: list) {
            String tag = article.getTag();
            if (!tagList.contains(tag)) {
                tagList.add(tag);
            }
        }

        // Alphabetical order.
        Collections.sort(tagList);

        // Add "All" for default list.
        tagList.add(0, "All (default)");

        return tagList;
    }

    /**
     * Transform list into json format and save to json file.
     *
     * @author Wyman
     */
    void transAndSaveToJson(String fileName) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(list);

        Utility.saveListToJson(context, jsonString, fileName);
    }

    /**
     * Load data from json file and transform into list format.
     *
     * @author Wyman
     */
    List<Article> loadAndTransFromJson(String fileName) {
        List<Article> list;
        String data = Utility.LoadListFromJson(context, fileName);

        Gson gson = new Gson();
        list = gson.fromJson(data, new TypeToken<ArrayList<Article>>() {}.getType());

        if (list == null) {
            list = new ArrayList<>();
            Log.e("Loading", "Loading a null pointer!");
        }
        return list;
    }

    /**
     * Clear all cache data (only for test).
     *
     * @author Wyman
     */
    void clearCache() {
        list = new ArrayList<>();
        tagMap = new HashMap<>();
    }
}
