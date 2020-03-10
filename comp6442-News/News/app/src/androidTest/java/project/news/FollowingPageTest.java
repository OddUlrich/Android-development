package project.news;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Root;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.RootMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.regex.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class FollowingPageTest {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mActivity=null;
    private Instrumentation.ActivityMonitor monitor = InstrumentationRegistry.getInstrumentation().addMonitor(SiteViewActivity.class.getName(),null, false);


    @Before
    public void setUp() throws Exception {
        mActivity=mainActivityActivityTestRule.getActivity();
    }

    @Test
    public void test_onLaunch(){
        FrameLayout mContainer= mActivity.findViewById(R.id.main_container);
        assertNotNull(mContainer);
        FollowingPage followingPage=new FollowingPage();
        mActivity.getSupportFragmentManager().beginTransaction().add(mContainer.getId(),followingPage).commitAllowingStateLoss();
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        View tablayout=followingPage.getView().findViewById(R.id.following_tabLayout);
        assertNotNull(tablayout);
        View pager=followingPage.getView().findViewById(R.id.following_pager);
        assertNotNull(pager);
    }

    @Test
    public void test_FollowedScroll(){
        FrameLayout mContainer = mActivity.findViewById(R.id.main_container);
        FollowedSiteFragment followedSiteFragment=new FollowedSiteFragment();
        mActivity.getSupportFragmentManager().beginTransaction().add(mContainer.getId(),followedSiteFragment).commitAllowingStateLoss();
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        RecyclerView view= followedSiteFragment.getView().findViewById(R.id.list_source);
        assertNotNull(view);
        int items=view.getAdapter().getItemCount();
        onView(ViewMatchers.withId(R.id.list_source))
                .inRoot(RootMatchers.withDecorView(
                        Matchers.is(mainActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.scrollToPosition(items-1));
    }
    @Test
    public void test_AvailableScroll(){
        FrameLayout mContainer = mActivity.findViewById(R.id.main_container);
        AllSiteFragment allSiteFragment=new AllSiteFragment();
        mActivity.getSupportFragmentManager().beginTransaction().add(mContainer.getId(),allSiteFragment).commitAllowingStateLoss();
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        RecyclerView view= allSiteFragment.getView().findViewById(R.id.list_source);
        onView(ViewMatchers.withId(R.id.list_source))
                .inRoot(RootMatchers.withDecorView(
                        Matchers.is(mainActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.scrollToPosition(20));
    }


    @Test
    public void test_launchFollowedFrag(){
        FrameLayout mContainer= mActivity.findViewById(R.id.main_container);
        assertNotNull(mContainer);
        FollowedSiteFragment followedPage=new FollowedSiteFragment();
        mActivity.getSupportFragmentManager().beginTransaction().add(mContainer.getId(),followedPage).commitAllowingStateLoss();
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        View recyclerview=followedPage.getView().findViewById(R.id.list_source);
        assertNotNull(recyclerview);
    }
    @Test
    public void test_launchAvailableFrag(){
        FrameLayout mContainer= mActivity.findViewById(R.id.main_container);
        assertNotNull(mContainer);
        AllSiteFragment allSiteFragment=new AllSiteFragment();
        mActivity.getSupportFragmentManager().beginTransaction().add(mContainer.getId(),allSiteFragment).commitAllowingStateLoss();
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        View recyclerview=allSiteFragment.getView().findViewById(R.id.list_source);
        assertNotNull(recyclerview);
    }
    /*@Test
    public void test_swipeAvailableFrag() {
        FrameLayout mContainer = mActivity.findViewById(R.id.main_container);
        AllSiteFragment allSiteFragment = new AllSiteFragment();
        mActivity.getSupportFragmentManager().beginTransaction().add(mContainer.getId(), allSiteFragment).commitAllowingStateLoss();
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        //RecyclerView view = allSiteFragment.getView().findViewById(R.id.list_source);
        onView(ViewMatchers.withId(R.id.list_source))
                .inRoot(RootMatchers.withDecorView(
                        Matchers.is(mainActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,swipeLeft()));
    }
    @Test
    public void test_swipeFollowedFrag(){
        FrameLayout mContainer = mActivity.findViewById(R.id.main_container);
        AllSiteFragment allSiteFragment = new AllSiteFragment();
        mActivity.getSupportFragmentManager().beginTransaction().add(mContainer.getId(), allSiteFragment).commitAllowingStateLoss();
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        //RecyclerView view = allSiteFragment.getView().findViewById(R.id.list_source);
        onView(ViewMatchers.withId(R.id.list_source))
                .inRoot(RootMatchers.withDecorView(
                        Matchers.is(mainActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,swipeLeft()));
        FollowedSiteFragment followedSiteFragment = new FollowedSiteFragment();
        mActivity.getSupportFragmentManager().beginTransaction().add(mContainer.getId(), followedSiteFragment).commitAllowingStateLoss();
        InstrumentationRegistry.getInstrumentation().waitForIdleSync();
        //RecyclerView view = allSiteFragment.getView().findViewById(R.id.list_source);
        onView(ViewMatchers.withId(R.id.list_source))
                .inRoot(RootMatchers.withDecorView(
                        Matchers.is(mainActivityActivityTestRule.getActivity().getWindow().getDecorView())))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,swipeRight()));
    }*/

    @After
    public void tearDown() throws Exception {
        mActivity=null;
    }
}