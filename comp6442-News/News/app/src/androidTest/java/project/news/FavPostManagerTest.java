package project.news;

import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import project.news.model.Article;
import project.news.model.Source;
import project.news.testActivity.TestMainActivity;

import static org.junit.Assert.*;

public class FavPostManagerTest {
    @Rule
    public ActivityTestRule<TestMainActivity> mActivityTestRule = new ActivityTestRule<>(TestMainActivity.class);

    private TestMainActivity mActivity = null;
    private FavPostManager favPostManager;
    private List<Article> postListWithTag;
    private List<Article> postListWithoutTag;
    private List<String> tagList;

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
        favPostManager = FavPostManager.getInstance(mActivity);
        favPostManager.clearCache();

        postListWithTag = new ArrayList<>();
        postListWithoutTag = new ArrayList<>();
        tagList = new ArrayList<>();

        // Create 10 fake articles with tag.
        for (int i = 0; i < 10; i++) {
            Article post = new Article();
            Source source = new Source();

            source.setId(String.valueOf(i));
            source.setName("Tag source " + i);

            post.setTitle("Tag post Title "+i);
            post.setSource(source);
            post.setPublishedAt("2020-12-0"+i);
            post.setTag("Tag"+i);

            postListWithTag.add(post);
            tagList.add("Tag"+i);
        }
        Collections.sort(tagList);
        // Add "All" for default list.
        tagList.add(0, "All (default)");

        // Create 10 fake articles without tag.
        for (int i = 20; i < 30; i++) {
            Article post = new Article();
            Source source = new Source();

            source.setId(String.valueOf(i));
            source.setName("Source " + i);

            post.setTitle("Post Title "+i);
            post.setSource(source);
            post.setPublishedAt("2020-12-"+i);
            post.setTag("");

            postListWithoutTag.add(post);
        }
    }

    @Test
    public void testAddPostWithTag() {
        Article addPost = postListWithTag.get(0);
        favPostManager.addNewFavPost(addPost);
        assertTrue("Adding new tag post to favourite failed!", favPostManager.isExistedFavPost(addPost));

        Article post1 = favPostManager.getFavPostList().get(0);
        assertTrue(postListWithTag.contains(post1));

        Article post2 = favPostManager.getFavPostListWithTag(addPost.getTag()).get(0);
        assertEquals(post2.getTag(), addPost.getTag());
    }

    @Test
    public void testAddPostWithoutTag() {
        Article addPost = postListWithoutTag.get(0);
        favPostManager.addNewFavPost(addPost);
        assertTrue("Adding new post to favourite failed!", favPostManager.isExistedFavPost(addPost));

        Article post1 = favPostManager.getFavPostList().get(0);
        assertTrue(postListWithoutTag.contains(post1));

        Article post2 = favPostManager.getFavPostListWithTag("Untagged").get(0);
        assertEquals(post2.getTag(), "Untagged");
    }

    @Test
    public void testAddAllPostWithTag() {
        for (Article post: postListWithTag) {
            favPostManager.addNewFavPost(post);
        }
        List<Article> postListGet = favPostManager.getFavPostList();
        comparePostList(postListGet, postListWithTag);
    }

    @Test
    public void testRemovePost() {
        Article post4 = postListWithTag.get(4);
        favPostManager.addNewFavPost(post4);
        Article post7 = postListWithTag.get(7);
        favPostManager.addNewFavPost(post7);
        assertEquals(2, favPostManager.getFavPostList().size());


        favPostManager.removeFavPost(post4);
        assertFalse(favPostManager.isExistedFavPost(post4));
        assertEquals(1, favPostManager.getFavPostList().size());

        favPostManager.removeFavPost(post7);
        assertFalse(favPostManager.isExistedFavPost(post7));
        assertEquals(0, favPostManager.getFavPostList().size());
    }

    @Test
    public void testModifyTagOfPost() {
        Article post = postListWithTag.get(3);
        favPostManager.addNewFavPost(post);
        favPostManager.modifyTagOfPost(post, "newTag");
        Article post1 = favPostManager.getFavPostList().get(0);
        assertEquals("newTag", post1.getTag());
    }

    @Test
    public void testJSONLoad() throws IOException {
        // Add data.
        for (Article post: postListWithTag) {
            favPostManager.addNewFavPost(post);
        }

        // Save it to the specified file.
        String fileName = "test_post_list.json";
        File file = new File(fileName);

        // Ensure that there is nothing there right now:
        file.delete();

        favPostManager.transAndSaveToJson(fileName);

        // Load it from the specified file.
        favPostManager.loadAndTransFromJson(fileName);

        comparePostList(favPostManager.getFavPostList(), postListWithTag);
        compareTagList(favPostManager.getTagList(), tagList);

        // Delete file after test
        file.delete();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
        favPostManager.clearCache();
    }

    private void comparePostList(List<Article> list1, List<Article> list2) {
        int size = list1.size();

        for (int idx = 0; idx < size; idx++) {
            Article postA = list1.get(idx);
            Article postB = list2.get(size-1-idx);

            assertEquals(postA.getTitle(), postB.getTitle());
            assertEquals(postA.getSource().getId(), postB.getSource().getId());
            assertEquals(postA.getSource().getName(), postB.getSource().getName());
            assertEquals(postA.getPublishedAt(), postB.getPublishedAt());
            assertEquals(postA.getTag(), postB.getTag());
        }
    }

    private void compareTagList(List<String> list1, List<String> list2) {
        for (int idx = 0; idx < list1.size(); idx++) {
            String tagA = list1.get(idx);
            String tagB = list2.get(idx);

            assertEquals(tagA, tagB);
        }
    }
}