package project.news;

import android.app.Instrumentation;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.view.View;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


import project.news.model.Article;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.*;

public class FavTagActivityTest {

    @Rule
    public ActivityTestRule<FavTagActivity> favActivityTestRule = new ActivityTestRule<>(FavTagActivity.class);

    private FavTagActivity favActivity = null;
    private FavPostManager favPostManager;
    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(FavTagActivity.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        favActivity = favActivityTestRule.getActivity();
        favPostManager = FavPostManager.getInstance(favActivity);
        favPostManager.clearCache();
        Article post1 = new Article();
        Article post2 = new Article();

        post1.setTag("abcdefg");
        post2.setTag("1234567");

        favPostManager.addNewFavPost(post1);
        favPostManager.addNewFavPost(post2);
    }

    @Test
    public void testViewExist() {
        // Waiting for the data loading in favourite page.
        getInstrumentation().waitForIdleSync();

        View tagToolBarView = favActivity.findViewById(R.id.tag_toolbar);
        assertNotNull(tagToolBarView);

        View listTagView = favActivity.findViewById(R.id.list_tag_view);
        assertNotNull(listTagView);

        View listItemView = favActivity.findViewById(R.id.list_item);
        assertNotNull(listItemView);

        View tagSearchView = favActivity.findViewById(R.id.tag_search);
        assertNotNull(tagSearchView);
    }

    @Test
    public void testSearchDigitalTag() {
        Espresso.onView(withId(R.id.tag_search)).perform(click());
        Espresso.onView(withId(android.support.design.R.id.search_src_text)).perform(typeText("234"));
        Espresso.closeSoftKeyboard();

        View tagListView = favActivity.findViewById(R.id.list_tag_view);
        assertNotNull(tagListView);

        onData(anything()).inAdapterView(withId(R.id.list_tag_view)).atPosition(0).perform(click());

        favActivity.isDestroyed();
    }

    @Test
    public void testSearchStringTag() {
        Espresso.onView(withId(R.id.tag_search)).perform(click());
        Espresso.onView(withId(android.support.design.R.id.search_src_text)).perform(typeText("bcd"));
        Espresso.closeSoftKeyboard();

        View tagListView = favActivity.findViewById(R.id.list_tag_view);
        assertNotNull(tagListView);

        onData(anything()).inAdapterView(withId(R.id.list_tag_view)).atPosition(0).perform(click());

        favActivity.isDestroyed();
    }

    @After
    public void tearDown() throws Exception {
        favActivity = null;
        favPostManager.clearCache();
    }
}