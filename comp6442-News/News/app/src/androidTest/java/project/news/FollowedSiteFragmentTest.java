package project.news;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.paperdb.Paper;
import project.news.model.Site;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class FollowedSiteFragmentTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mActivity=null;
    private Instrumentation.ActivityMonitor monitor = InstrumentationRegistry.getInstrumentation().addMonitor(SiteViewActivity.class.getName(),null, false);
    private Site site = new Site("abc-news-au","ABC News (AU)","Australia's most trusted source of local, national and world news. Comprehensive, independent, in-depth analysis, the latest business, sport, weather and more.","http://www.abc.net.au/news","general","en","au");

    @Before
    public void setUp() throws Exception {
        mActivity=mainActivityActivityTestRule.getActivity();

        //Site site = new Site("abc-news-au","ABC News (AU)","Australia's most trusted source of local, national and world news. Comprehensive, independent, in-depth analysis, the latest business, sport, weather and more.","http://www.abc.net.au/news","general","en","au");

        site.setFollowing(true);
        Paper.book("AllSites").write(site.getId(),site);
        //onView(withId(R.id.navigation_following)).perform(click());
        FrameLayout mContainer = mActivity.findViewById(R.id.main_container);
        FollowedSiteFragment followedSiteFragment=new FollowedSiteFragment();
       mActivity.getSupportFragmentManager().beginTransaction().add(mContainer.getId(),followedSiteFragment).commitAllowingStateLoss();
    }

    @Test
    public void test_onclick(){

        onView(withId(R.id.list_source))
                .inRoot(RootMatchers.withDecorView(
                        Matchers.is(mainActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        //onView(withId(R.id.following_source_name)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        Activity siteview=InstrumentationRegistry.getInstrumentation().waitForMonitorWithTimeout(monitor,5000);
        assertNotNull(siteview);
        siteview.finish();
    }

    @Test
    public void test_swipeFollowedFrag(){

        onView(withId(R.id.list_source))
                .inRoot(RootMatchers.withDecorView(
                        Matchers.is(mainActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeRight()));

    }


    @After
    public void tearDown() throws Exception {
        mActivity=null;
        mActivity=null;
        Paper.book("AllSites").delete(site.getId());
    }


}