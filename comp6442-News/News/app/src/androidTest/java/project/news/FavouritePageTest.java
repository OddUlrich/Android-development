package project.news;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.constraint.ConstraintLayout;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import project.news.testActivity.TestMainActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class FavouritePageTest {

    @Rule
    public ActivityTestRule<TestMainActivity> mActivityTestRule = new ActivityTestRule<>(TestMainActivity.class);

    private TestMainActivity mActivity = null;
    private Fragment fragment = new FavouritePage();
    private FavPostManager favPostManager;
    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(TestMainActivity.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
        favPostManager = FavPostManager.getInstance(mActivity);
        // Switch fragment.
        ConstraintLayout constContainer = mActivity.findViewById(R.id.test_container);
        assertNotNull(constContainer);

        mActivity.getSupportFragmentManager().beginTransaction().add(R.id.test_container, fragment).commitAllowingStateLoss();

        // Waiting the fragment to load.
        getInstrumentation().waitForIdleSync();
    }

    @Test
    public void testLaunchFragmentOnButtonClick() {
        assertNotNull(fragment.getView().findViewById(R.id.tag_group));
        onView(withId(R.id.tag_group)).perform(click());

        Activity tagEditActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);
        assertNotNull(tagEditActivity);
        tagEditActivity.finish();
    }

    @Test
    public void testViewExistWithoutPost() {
        // test if the fragment is launched or not.
        View toolBarView = fragment.getView().findViewById(R.id.toolbar);
        assertNotNull(toolBarView);

        View localSearchView = fragment.getView().findViewById(R.id.local_search);
        assertNotNull(localSearchView);

        View tagGroupView = fragment.getView().findViewById(R.id.tag_group);
        assertNotNull(tagGroupView);

        View favRecyclerView = fragment.getView().findViewById(R.id.fav_recyclerView);
        assertNotNull(favRecyclerView);
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }
}